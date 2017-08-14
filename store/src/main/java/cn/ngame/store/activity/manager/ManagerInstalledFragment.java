package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.GameDownload2Adapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class ManagerInstalledFragment extends BaseSearchFragment {

    ListView listView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;

    private GameDownload2Adapter alreadyLvAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private FragmentActivity content;
    private boolean mHidden = false;
    private long DELAY_TIME = 2000L;

    public static ManagerInstalledFragment newInstance(String type, int arg) {
        ManagerInstalledFragment fragment = new ManagerInstalledFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("typeValue", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.loadint_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        typeValue = getArguments().getInt("typeValue", 1);
        type = getArguments().getString("type");

        listView = (ListView) view.findViewById(R.id.listView);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        initPop();
        initListView();
    }

    public void initListView() {
        alreadyLvAdapter = new GameDownload2Adapter(content, getSupportFragmentManager(), mItemClickQuickAction);
        listView.setAdapter(alreadyLvAdapter);
        fileLoad = FileLoadManager.getInstance(content);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "====: onStart==" + mHidden);
        if (!mHidden) {
            alreadyLvAdapter.setDate(fileLoad.getOpenFileInfo());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mHidden = hidden;
        if (!mHidden) {
            alreadyLvAdapter.setDate(fileLoad.getOpenFileInfo());
        }
    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(0, "卸载", null);
        mItemClickQuickAction.addActionItem(pointItem);
        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    FileLoadInfo fileInfo = (FileLoadInfo) alreadyLvAdapter.getItemInfo();
                    GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(fileInfo.getName(), fileInfo.getUrl(),
                            fileInfo.getPackageName(), fileInfo.getVersionCode());
                    Log.d(TAG, "status: " + fileStatus.getStatus());
                    if (GameFileStatus.STATE_HAS_INSTALL == fileStatus.getStatus()) {
                        //卸载
                        AppInstallHelper.unstallApp(content, fileInfo.getPackageName());
                        List<FileLoadInfo> loadedFileInfo = fileLoad.getLoadedFileInfo();
                        fileLoad.delete(fileInfo.getUrl());
                    } else {
                        //删除安装包和正在下载的文件
                        fileLoad.delete(fileInfo.getUrl());
                    }
                    //取消弹出框
                    mItemClickQuickAction.dismiss();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    alreadyLvAdapter.setDate(fileLoad.getOpenFileInfo());
                }

            }
        });
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
