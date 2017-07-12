package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.admin.SystemMsgBean;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.admin.GameUpdateClient;
import com.jzt.hol.android.jkda.sdk.services.admin.QuerySystemMsgClient;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.PadToolActivity;
import cn.ngame.store.activity.admin.HelpAndFeedbackActivity;
import cn.ngame.store.activity.admin.MyGameDownloadActivity;
import cn.ngame.store.activity.admin.SettingActivity;
import cn.ngame.store.activity.admin.SystemInfoActivity;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.base.activity.AboutActivity;
import cn.ngame.store.base.activity.DisclaimerActivity;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.local.view.WatchHistoryActivity;
import cn.ngame.store.ota.view.OtaActivity;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.user.view.UserCenterActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 管理
 * Created by gp on 2017/3/14 0014.
 */

public class AdministrationFragment extends BaseSearchFragment implements View.OnClickListener {

    private PullToRefreshListView pullListView;
    /**
     * headerView
     */
    private ImageView iv_icon;
    private TextView tv_to_login;
    private RelativeLayout rl_my_device, rl_jingling;
    private RelativeLayout rl_download, rl_history, rl_system_msg, rl_system_set, rl_disclaimer, rl_feedback, rl_about;
    private TextView tv_download_num, tv_system_msg_num;
    private IFileLoad fileLoad;

    ImageLoader imageLoader = ImageLoader.getInstance();
    private String pwd;
    private GameRankListBean infoBean;
    private DatabaseManager dbManager;
    SystemMsgBean msgbean;
    private int systemMsgNum; //已读消息数
    private int resultSize;

    public static AdministrationFragment newInstance() {
        Bundle args = new Bundle();
        AdministrationFragment fragment = new AdministrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.administration_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {//初始化
//        typeValue = getArguments().getInt("", 1);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pulllistview);
        initListView();
    }

    public void initListView() {
        dbManager = DatabaseManager.getInstance(getActivity());
        pullListView.setPullRefreshEnabled(false);
        pullListView.setPullLoadEnabled(false);
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        //添加头布局
        View view = View.inflate(getActivity(), R.layout.administration_head_view, null);
        initHeadView(view);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(view);
        }
        List<String> list = new ArrayList<>();
        list.add(new String(""));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(getActivity(), list, "0");
        pullListView.getRefreshableView().setAdapter(adapter);
    }

    private void initHeadView(View view) {
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_to_login = (TextView) view.findViewById(R.id.tv_to_login);
        rl_my_device = (RelativeLayout) view.findViewById(R.id.rl_my_device);
        rl_jingling = (RelativeLayout) view.findViewById(R.id.rl_jingling);
        rl_download = (RelativeLayout) view.findViewById(R.id.rl_download);
        tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
        rl_history = (RelativeLayout) view.findViewById(R.id.rl_history);
        rl_system_msg = (RelativeLayout) view.findViewById(R.id.rl_system_msg);
        tv_system_msg_num = (TextView) view.findViewById(R.id.tv_system_msg_num);
        rl_system_set = (RelativeLayout) view.findViewById(R.id.rl_system_set);
        rl_disclaimer = (RelativeLayout) view.findViewById(R.id.rl_disclaimer);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_about = (RelativeLayout) view.findViewById(R.id.rl_about);
        iv_icon.setOnClickListener(this);
        tv_to_login.setOnClickListener(this);
        rl_my_device.setOnClickListener(this);
        rl_jingling.setOnClickListener(this);
        rl_download.setOnClickListener(this);
        rl_history.setOnClickListener(this);
        rl_system_msg.setOnClickListener(this);
        rl_system_set.setOnClickListener(this);
        rl_disclaimer.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        pwd = StoreApplication.passWord;
        if (pwd != null && !"".endsWith(pwd)) {
            imageLoader.displayImage(StoreApplication.userHeadUrl, iv_icon, FileUtil.getRoundOptions(R.drawable
                    .manage_usericon, 360));
            tv_to_login.setText(StoreApplication.nickName);
        } else {
            imageLoader.displayImage("", iv_icon, FileUtil.getRoundOptions(R.drawable.manage_usericon, 360));
            tv_to_login.setText("点击登录");
        }
        //下载更新 数目
        fileLoad = FileLoadManager.getInstance(getActivity());
        List<FileLoadInfo> updateList = fileLoad.getLoadedFileInfo();
        String updateGameInfo = "";
        for (int i = 0; i < updateList.size(); i++) {
            updateGameInfo = ((updateGameInfo + ",").equals(",") ? "" : (updateGameInfo + ",")) + (updateList.get(i)
                    .getServerId() + "_" + updateList.get(i).getVersionCode());
        }
//        updateGameInfo = "149_1365,137_102004001";
        runSelectUpdateGame(updateGameInfo);

        // 系统消息
        runSelectSystemMsg();
    }

    private void runSelectSystemMsg() {
        AdminGameUpdateBody body = new AdminGameUpdateBody();
        new QuerySystemMsgClient(getActivity(), body).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<SystemMsgBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(SystemMsgBean result) {
                        if (result != null && result.getCode() == 0) {
                            SystemMsg(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    private void SystemMsg(SystemMsgBean result) {
        if (result.getData() == null) {
            return;
        }
        if (result.getData().size() > 0) {
            resultSize = result.getData().size();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = 0;
                    message.obj = dbManager.getReadSystemCount();
                    handler.sendMessage(message);
                }
            }).start();
        } else {
            tv_system_msg_num.setVisibility(View.GONE);
            return;
        }
        msgbean = result;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //已读消息数
                    tv_system_msg_num.setVisibility(View.VISIBLE);
                    //总数-已读数 = 未读数
                    if (resultSize - ConvUtil.NI(msg.obj) > 0) {
                        tv_system_msg_num.setText((resultSize - ConvUtil.NI(msg.obj)) + "");
                    } else {
                        tv_system_msg_num.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 已下载的游戏，是否需要更新
     *
     * @param gameInfo {"gameIdAndVisionCodeStr":"136_2,137_102004001","appTypeId":2}, gameIdAndVisionCodeStr
     *                 表示多个已下载游戏id和versionCode字段拼接。
     */
    private void runSelectUpdateGame(String gameInfo) {
        AdminGameUpdateBody body = new AdminGameUpdateBody();
        body.setGameIdAndVisionCodeStr(gameInfo);
        new GameUpdateClient(getActivity(), body).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(GameRankListBean result) {
                        if (result != null && result.getCode() == 0) {
                            infoBean = result;

                            if (result.getData() == null) {
                                return;
                            }
                            if (result.getData().size() > 0) {
                                tv_download_num.setVisibility(View.VISIBLE);
                                tv_download_num.setText(result.getData().size() + "");
                            } else {
                                tv_download_num.setVisibility(View.GONE);
                            }
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                if (pwd != null && !"".endsWith(pwd)) {
                    startActivity(new Intent(getActivity(), UserCenterActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.tv_to_login:
                if (pwd != null && !"".endsWith(pwd)) {

                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.rl_my_device:
                Intent intent0 = new Intent(getActivity(), OtaActivity.class);
                startActivity(intent0);
                break;
            case R.id.rl_jingling:
                Intent intent1 = new Intent(getActivity(), PadToolActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_download:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyGameDownloadActivity.class);
                intent.putExtra("updateGame", infoBean);
                startActivity(intent);
                break;
            case R.id.rl_history:
                Intent historyIntent = new Intent(getActivity(), WatchHistoryActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.rl_system_msg:
                Intent msg = new Intent();
                msg.setClass(getActivity(), SystemInfoActivity.class);
                msg.putExtra("msgbean", msgbean);
                startActivity(msg);
                break;
            case R.id.rl_system_set:
                Intent setIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(setIntent);
                break;
            case R.id.rl_disclaimer:
                Intent intent2 = new Intent(getActivity(), DisclaimerActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_feedback:
                Intent intent3 = new Intent(getActivity(), HelpAndFeedbackActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_about:
                Intent intent4 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
