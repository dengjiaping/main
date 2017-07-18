package cn.ngame.store.activity.sm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DataCleanManager;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.SPUtils;
import cn.ngame.store.util.ToastUtil;

/**
 * App设置页面
 * Created by zeng on 2016/12/7.
 */
public class ManagerSettingsActivity extends BaseFgActivity implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton but_push, but_load, but_install, but_update;
    private int delayMillis = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_manager_settings);

        Button left_but = (Button) findViewById(R.id.left_bt);
        left_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        but_push = (ToggleButton) findViewById(R.id.but_push);
        if (StoreApplication.isReceiveMsg) {
            but_push.setChecked(true);
        } else {
            but_push.setChecked(false);
        }

        but_load = (ToggleButton) findViewById(R.id.but_load);
        if (StoreApplication.allowAnyNet) {
            but_load.setChecked(true);
        } else {
            but_load.setChecked(false);
        }

        but_install = (ToggleButton) findViewById(R.id.but_install);
        if (StoreApplication.isDeleteApk) {
            but_install.setChecked(true);
        } else {
            but_install.setChecked(false);
        }

        //but_update = (ToggleButton) findViewById(R.id.but_update);

        final TextView tv_clear = (TextView) findViewById(R.id.tv_clear);
        RelativeLayout layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tv_clear.getText().toString();
                if ("0KB".equals(text)) {
                    ToastUtil.show(ManagerSettingsActivity.this, "没有缓存了~");
                    return;
                }
                if (text.endsWith("MB")) {
                    delayMillis = 1000;
                } else if (text.endsWith("KB")) {
                    delayMillis = 100;
                } else {
                    delayMillis = 1000;
                }
                DataCleanManager.clearAllCache(ManagerSettingsActivity.this);
                final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), ManagerSettingsActivity.this);
                dialogHelper.showAlert("清理中...", true);

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogHelper.hideAlert();
                        ToastUtil.show(ManagerSettingsActivity.this, "清空缓存成功~");
                        tv_clear.setText("0KB");
                    }
                }, delayMillis);

            }
        });

        but_push.setOnCheckedChangeListener(this);
        but_load.setOnCheckedChangeListener(this);
        but_install.setOnCheckedChangeListener(this);
        //but_update.setOnCheckedChangeListener(this);

        //显示App缓存
        try {
            String cacheSize = DataCleanManager.getTotalCacheSize(this);
            tv_clear.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.but_push:
                doPushClick(isChecked);
                break;
            case R.id.but_load:
                doLoadClick(isChecked);
                break;
            case R.id.but_install:
                StoreApplication.isDeleteApk = isChecked;
                if (isChecked) {
                    SPUtils.put(this, Constant.CFG_DELETE_APK, true);
                } else {
                    SPUtils.put(this, Constant.CFG_DELETE_APK, false);
                }
                break;
            //游戏更新提醒
           /* case R.id.but_update:
                if(isChecked){
                    SPUtils.put(this,Constant.CFG_NOTIFY_GAME_UPDATE,true);
                }else {
                    SPUtils.put(this,Constant.CFG_NOTIFY_GAME_UPDATE,false);
                }
                break;*/
        }
    }

    private void doPushClick(boolean isChecked) {
        StoreApplication.isReceiveMsg = isChecked;
        if (isChecked) {
            SPUtils.put(this, Constant.CFG_RECEIVE_MSG, true);
            //启动百度推送
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constant.PUSH_API_KEY);
            PushManager.disableLbs(this);   //关闭精确LBS推送模式
        } else {
            SPUtils.put(this, Constant.CFG_RECEIVE_MSG, false);
            //关闭百度推送
            PushManager.stopWork(this);
        }
    }

    private void doLoadClick(boolean isChecked) {
        StoreApplication.allowAnyNet = isChecked;
        if (isChecked) {

            DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), this);
            dialogHelper.showConfirm("温馨提示", "运营商网络下载可能导致流量超额，确认开启？", "取消", "开启", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    but_load.setChecked(false);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtils.put(ManagerSettingsActivity.this, Constant.CFG_ALLOW_4G_LOAD, true);
                }
            });

        } else {
            SPUtils.put(this, Constant.CFG_ALLOW_4G_LOAD, false);
        }
    }

}
