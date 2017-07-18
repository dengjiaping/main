package cn.ngame.store.ota.view;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.ota.model.DeviceInfo;
import cn.ngame.store.ota.model.OtaService;
import cn.ngame.store.ota.presenter.IOtaPresenter;
import cn.ngame.store.ota.presenter.OtaPresenter;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.view.RoundProgressBar;

/**
 * 用于OTA升级，我的设备页面
 * Created by zeng on 2016/8/15.
 */
public class DeviceOtaUpdateActivity extends BaseFgActivity implements View.OnClickListener,IOtaView{

    public static final String TAG = DeviceOtaUpdateActivity.class.getSimpleName();

    public static final int REQUEST_CODE_BLUETOOTH_SETTINGS = 1;

    private IOtaPresenter presenter;

    private ListView listView;
    private DeviceLvAdapter adapter;

    private RoundProgressBar progressBar;
    private Button bt_check,bt_back;
    private TextView tv_title_left,tv_title_right;

    private boolean isUpdating = false;
    private Timer timer = new Timer();


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OtaService.OtcBinder otcBinder = (OtaService.OtcBinder) service;
            OtaService otaService = otcBinder.getService();

            //创建控制层
            presenter = new OtaPresenter(DeviceOtaUpdateActivity.this,otaService);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isUpdating){
                        presenter.scanDevice();
                        timer.cancel();
                    }
                }
            },0,1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.ota_activity_ota);

        progressBar = (RoundProgressBar) findViewById(R.id.progress_bar);
        bt_check = (Button) findViewById(R.id.but1);
        bt_back = (Button) findViewById(R.id.left_but);
        tv_title_left = (TextView) findViewById(R.id.left_tv);
        tv_title_right = (TextView) findViewById(R.id.right_tv);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new DeviceLvAdapter(this);
        listView.setAdapter(adapter);

        //初始化圆形进度条
        progressBar.setProgress(100);
        progressBar.setState("连接手柄");
        progressBar.setStateDetail("");

        //启动OTA升级服务
        Intent otaIntent = new Intent(this, OtaService.class);
        startService(otaIntent);

        bindService(otaIntent,conn, Context.BIND_AUTO_CREATE);

        setListener();

        //申请下载权限
        CommonUtil.verifyStoragePermissions(this);
    }

    private void setListener(){

        bt_back.setOnClickListener(this);
        tv_title_left.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);

        progressBar.setOnClickListener(this);
        bt_check.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(presenter != null){

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(presenter != null){
            //presenter.clear();
            presenter = null;
        }
        listView = null;
        adapter = null;

        unbindService(conn);
        unregisterReceiver(gattUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_BLUETOOTH_SETTINGS){

            if(presenter != null){
                progressBar.setState("正在连接手柄");
                progressBar.setStateDetail("正在创建连接...");
                presenter.scanDevice();
            }else {
                progressBar.setState("绑定服务失败");
                progressBar.setStateDetail("退出页面重新进入");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_but:

                if(isUpdating){
                    showNotifyBack();
                }else {
                    finish();
                }

                break;
            case R.id.left_tv:
                if(isUpdating){
                    showNotifyBack();
                }else {
                    finish();
                }
                break;
            case R.id.right_tv:

                Intent helpIntent = new Intent(DeviceOtaUpdateActivity.this,OtaHelpActivity.class);
                startActivity(helpIntent);

                break;
            case R.id.progress_bar:

                Intent bluetoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(bluetoothIntent,REQUEST_CODE_BLUETOOTH_SETTINGS);

                break;
            case R.id.but1:

                presenter.checkNewVersion();

                break;
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_NONSUPPORT);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_DISABLE);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_FIND_DEVICE);
        intentFilter.addAction(OtaService.ACTION_BLUETOOTH_CHECK_UPDATE);
        intentFilter.addAction(OtaService.ACTION_OTA_UPDATING);

        return intentFilter;
    }

    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(OtaService.ACTION_BLUETOOTH_NONSUPPORT.equals(action)){

                isUpdating = false;
                listView.setVisibility(View.GONE);
                progressBar.setState("连接失败");
                progressBar.setStateDetail("不支持蓝牙设备");
                progressBar.setProgress(100);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment updateDialog = getSupportFragmentManager().findFragmentByTag("updateDialog");
                Fragment notifyBackDialog = getSupportFragmentManager().findFragmentByTag("notifyBackDialog");
                if (updateDialog != null) {
                    ft.remove(updateDialog);
                }
                if(notifyBackDialog != null){
                    ft.remove(notifyBackDialog);
                }
                ft.commit();

            }else if(OtaService.ACTION_BLUETOOTH_DISABLE.equals(action)){

                isUpdating = false;
                listView.setVisibility(View.GONE);
                progressBar.setState("连接失败");
                progressBar.setStateDetail("未开启蓝牙功能");
                progressBar.setProgress(100);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment updateDialog = getSupportFragmentManager().findFragmentByTag("updateDialog");
                Fragment notifyBackDialog = getSupportFragmentManager().findFragmentByTag("notifyBackDialog");
                if (updateDialog != null) {
                    ft.remove(updateDialog);
                }
                if(notifyBackDialog != null){
                    ft.remove(notifyBackDialog);
                }
                ft.commit();

            }else if(OtaService.ACTION_BLUETOOTH_FIND_DEVICE.equals(action)){

                isUpdating = false;
                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                ArrayList<DeviceInfo> deviceInfos = intent.getParcelableArrayListExtra("devices");

                if(deviceInfos != null && deviceInfos.size() > 0){
                    listView.setVisibility(View.VISIBLE);
                    adapter.setData(deviceInfos);
                    adapter.notifyDataSetChanged();
                }else {
                    listView.setVisibility(View.GONE);
                }

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(100);


            }else if(OtaService.ACTION_BLUETOOTH_CHECK_UPDATE.equals(action)){

                isUpdating = false;
                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                ArrayList<DeviceInfo> deviceInfos = intent.getParcelableArrayListExtra("devices");

                listView.setVisibility(View.VISIBLE);
                adapter.setData(deviceInfos);
                adapter.notifyDataSetChanged();

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(100);


            }else if(OtaService.ACTION_OTA_UPDATING.equals(action)){

                String title = intent.getStringExtra("title");
                String subtitle = intent.getStringExtra("subtitle");
                int progress = intent.getIntExtra("progress",100);
                isUpdating = intent.getBooleanExtra("isUpdating",false);
                boolean isFinished = intent.getBooleanExtra("isFinished",false);

                if(progress >= 100 && isFinished){
                    adapter.setData(null);
                    adapter.notifyDataSetChanged();
                    listView.setVisibility(View.GONE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                }

                progressBar.setState(title);
                progressBar.setStateDetail(subtitle);
                progressBar.setProgress(progress);

            }

        }
    };

    @Override
    public void showCheckVersionResult(final int state) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (state == -1){
                    Toast.makeText(DeviceOtaUpdateActivity.this,"请先连接手柄设备",Toast.LENGTH_SHORT).show();
                }else if (state == -2){
                    Toast.makeText(DeviceOtaUpdateActivity.this,"正在检测，请稍后",Toast.LENGTH_SHORT).show();
                }else if (state == -3){
                    Toast.makeText(DeviceOtaUpdateActivity.this,"正在进行OTA升级",Toast.LENGTH_SHORT).show();
                }else if (state == 0){
                    Toast.makeText(DeviceOtaUpdateActivity.this,"当前设备是最新版本",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DeviceOtaUpdateActivity.this,"检测完成",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showUpdateDialog(final DeviceInfo info){

        if(isUpdating){
            Toast.makeText(DeviceOtaUpdateActivity.this,"正在进行OTA升级",Toast.LENGTH_SHORT).show();
            return;
        }

        //final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        final SimpleDialogFragment dialogFragment = SimpleDialogFragment.newInstance();

        if(dialogFragment.isShow()){
            return;
        }

        dialogFragment.setTitle("更新");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update,null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText("发现新版本："+info.getNewVersionName());
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText(info.getContent());
        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.update_later, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.setNegativeButton(R.string.update_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                showNotify(info);
            }
        });
        if(!dialogFragment.isAdded()){
            dialogFragment.show(getSupportFragmentManager(),"updateDialog");
            dialogFragment.setIsShow(true);
        }
    }

    private void showNotify(final DeviceInfo info){


        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setTitle("温馨提示");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update,null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText("固件升级过程中请保持手机和手柄设备电量充足，请将手机和手柄放在一起。请勿断开蓝牙连接！请勿退出APP，及其他操作！升级过程中手柄重启是正常情况。");

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton("待会儿再说", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFragment.isVisible())
                    dialogFragment.dismiss();
            }
        });
        dialogFragment.setNegativeButton("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFragment.isVisible())
                    dialogFragment.dismiss();
                presenter.updateDevice(info);
            }
        });
        if(dialogFragment != null)
            dialogFragment.show(getSupportFragmentManager(),"notifyDialog");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isUpdating){
                showNotifyBack();
            }else {
                finish();
            }
        }
        return false;
    }

    private void showNotifyBack(){

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setTitle("温馨提示");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update,null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText("正在进行手柄固件升级，退出后将会导致升级失败！");

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton("仍然退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                finish();
            }
        });
        dialogFragment.setNegativeButton("继续升级", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialogFragment.isVisible())
                    dialogFragment.dismiss();
            }
        });
        if(dialogFragment != null)
            dialogFragment.show(getSupportFragmentManager(),"notifyBackDialog");
    }
}
