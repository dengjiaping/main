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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.sdk.InMobiSdk;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.activity.NewsSnippet;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.core.fileload.FileLoadService;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
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
public class BeginActivity extends Activity {

    public static final String TAG = BeginActivity.class.getSimpleName();
    private boolean isFirstInstall = true;
    private Timer timer;
    private BeginActivity content;
    private long SHOW_TIME = 1000;
    private long SHOW_TIME_isFirstInstall = 1000;
    private Button skipBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        // this.setContentView(R.layout.activity_begin);
        setContentView(R.layout.activity_splash_fullscreen);
        content = this;
        //启动后台服务
        skipBt = (Button) findViewById(R.id.skip_bt);
        Intent serviceIntent = new Intent(this, FileLoadService.class);
        startService(serviceIntent);
        //得到设备id
        CommonUtil.verifyStatePermissions(this);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        //友盟相关
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);

        //判断是否是安装后第一次启动
        isFirstInstall = ConvUtil.NB(SPUtils.get(this, Constant.CONFIG_FIRST_INSTALL, true));
        timer = new Timer();

        //广告
        InMobiSdk.init(content, Constant.InMobiSdk_Id);
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
        Log.d(TAG, "开始");
        mFrameLayoutView = (LinearLayout) findViewById(R.id.fullscreen_view);

        nativeAd = new InMobiNative(this, Constant.AD_PlacementID_BeginActivity, new InMobiNative.NativeAdListener() {
            @Override
            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "广告加载成功.");
                //JSONObject content = inMobiNative.getCustomAdContent();
                NewsSnippet item = new NewsSnippet();
                item.title = inMobiNative.getAdTitle();//content.getString(Constants.AdJsonKeys.AD_TITLE);
                //item.landingUrl = content.getString(Constants.AdJsonKeys.AD_CLICK_URL);
                item.imageUrl = inMobiNative.getAdIconUrl();//content.getJSONObject(Constants.AdJsonKeys.AD_IMAGE_OBJECT).
                // getString(Constants.AdJsonKeys.AD_IMAGE_URL);
                item.description = inMobiNative.getAdDescription();//content.getString(Constants.AdJsonKeys.AD_DESCRIPTION);
                item.inMobiNative = new WeakReference<>(inMobiNative);
                //item.view =inMobiNative.getPrimaryViewOfWidth(mAdapter.,viewGroup,0);
                mFrameLayoutView.removeAllViews();

                mFrameLayoutView.addView(inMobiNative.getPrimaryViewOfWidth(mFrameLayoutView, mFrameLayoutView,
                        mFrameLayoutView.getWidth()));
                mFrameLayoutView.setBackgroundColor(ContextCompat.getColor(content, R.color.white));
                skipBt.setVisibility(View.VISIBLE);
                skipBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skip2Main();
                    }
                });
            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.d(TAG, "广告加载失败.");
                skip2Main();
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDismissed");
            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenWillDisplay");
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDisplayed");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                Log.d(TAG, "onUserWillLeaveApplication");
            }

            @Override
            public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdImpressed");
            }

            @Override
            public void onAdClicked(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onMediaPlaybackComplete");
            }
            @Override
            public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdStatusChanged");
            }
        });

        Map<String, String> map = new HashMap<>();
        nativeAd.setExtras(map);
        nativeAd.setDownloaderEnabled(true);
        nativeAd.load();

        go2Main();
    }

    private void go2Main() {
        final long pushMsgId = getIntent().getLongExtra("msgId", 0);
        final int pushMsgType = getIntent().getIntExtra("type", 0);
        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        if (timer==null) {
            skip2Main();
            return;
        }
        if (isFirstInstall){
            Log.d(TAG, "滑动页");
            final Intent intent = new Intent(content, GuideViewActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                    //更新安装状态值
                    SPUtils.put(content, Constant.CONFIG_FIRST_INSTALL, false);
                    finish();
                }
            }, SHOW_TIME_isFirstInstall);
        } else {
            Log.d(TAG, "跳主页面");
            final Intent intent = new Intent(content, MainHomeActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);

                intent.putExtra("msg", msg);
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, SHOW_TIME);
        }
    }
    private LinearLayout mFrameLayoutView;
    InMobiNative nativeAd;
  /*  private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.show(content, "收到消息");
            //handler.sendEmptyMessageAtTime(0, 1000);

        }
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void skip2Main() {
        // handler.sendEmptyMessageAtTime(0, 1000);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        final long pushMsgId = getIntent().getLongExtra("msgId", 0);
        final int pushMsgType = getIntent().getIntExtra("type", 0);
        final PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
        if (isFirstInstall) {
            Log.d(TAG, "滑动页");
            final Intent intent = new Intent(content, GuideViewActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            startActivity(intent);
            //更新安装状态值
            SPUtils.put(content, Constant.CONFIG_FIRST_INSTALL, false);
            finish();

        } else {
            Log.d(TAG, "跳主页面");
            final Intent intent = new Intent(content, MainHomeActivity.class);
            if (pushMsgId > 0) {
                intent.putExtra("msgId", pushMsgId);
                intent.putExtra("type", pushMsgType);
                intent.putExtra("msg", msg);
            }
            startActivity(intent);
            finish();
        }

    }
}