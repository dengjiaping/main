/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ngame.store.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.fileload.FileLoadService;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.SPUtils;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.util.ConvUtil;

import static cn.ngame.store.StoreApplication.deviceId;

/**
 * App启动时的等待窗口，处理进入home页时需要预先加载的内容
 *
 * @author flan
 * @since 2016年5月3日
 */
public class BeginActivity extends BaseFgActivity {

    public static final String TAG = BeginActivity.class.getSimpleName();
    private boolean isFirstInstall = true;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_begin);

        TextView tv_version = (TextView) findViewById(R.id.text1);
        tv_version.setText(CommonUtil.getVersionName(this));
        //启动后台服务
        Intent serviceIntent = new Intent(this, FileLoadService.class);
        startService(serviceIntent);
        //得到设备id
        CommonUtil.verifyStatePermissions(this);
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        //友盟相关
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);

        //判断是否是安装后第一次启动
        isFirstInstall = ConvUtil.NB(SPUtils.get(this, Constant.CONFIG_FIRST_INSTALL, true));
        final long pushMsgId = getIntent().getLongExtra("msgId", 0);
        final int pushMsgType = getIntent().getIntExtra("type", 0);
        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        if (isFirstInstall) {
            Intent intent = new Intent(BeginActivity.this, GuideViewActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            startActivity(intent);
            finish();
            //更新安装状态值
            SPUtils.put(this, Constant.CONFIG_FIRST_INSTALL, false);
            finish();
        } else {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(BeginActivity.this, AdvertisingPageActivity.class);
                    if (pushMsgId > 0) {
                        intent.putExtra("msgId", pushMsgId);
                        intent.putExtra("type", pushMsgType);
                        intent.putExtra("msg", msg);
                    }
                    startActivity(intent);
                    finish();
                }
            }, 1 * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}