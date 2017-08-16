package cn.ngame.store.activity.manager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.InstalledGameAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
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
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;

    private InstalledGameAdapter alreadyLvAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private FragmentActivity content;
    private boolean mHidden = false;
    private FileLoadInfo mfileUnstalledInfo;
    private PackageManager packageManager;

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
        return R.layout.fragment_installed;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        packageManager = content.getPackageManager();
        typeValue = getArguments().getInt("typeValue", 1);
        type = getArguments().getString("type");
        listView = (ListView) view.findViewById(R.id.listView);

        initPop();
        initListView();
    }

    public void initListView() {
        alreadyLvAdapter = new InstalledGameAdapter(content, getSupportFragmentManager(), mItemClickQuickAction);
        listView.setAdapter(alreadyLvAdapter);
        fileLoad = FileLoadManager.getInstance(content);
    }

    private List<PackageInfo> localAppList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        List<PackageInfo> appList = getLocalApp();

      /*  if (!mHidden && null != alreadyLvAdapter && null != fileLoad) {

            alreadyLvAdapter.setDate();
            if (null != mfileUnstalledInfo) {
                boolean containInfo = openFileInfo.contains(mfileUnstalledInfo);
                if (!containInfo) {
                    //删除安装包
                    fileLoad.delete(mfileUnstalledInfo.getUrl());
                }

            }
        }*/
    }

    private List<PackageInfo> getLocalApp() {
        final List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        localAppList.clear();
        PackageInfo packageInfo;
        for (int i = 0; i < packageInfos.size(); i++) {
            packageInfo = packageInfos.get(i);
            // 获取 非系统的应用
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                String appName = applicationInfo.loadLabel(packageManager).toString();
                Drawable drawable = applicationInfo.loadIcon(packageManager);
                Log.d(TAG, appName + "getLocalApp" + packageInfo.packageName);
                localAppList.add(packageInfo);
            }
        }
        return localAppList;
    }

    protected final static String TAG = "2222";

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: ");
        mHidden = hidden;
        if (!mHidden && null != alreadyLvAdapter && null != fileLoad) {
            DatabaseManager dbManager = DatabaseManager.getInstance(content);
            List<FileLoadInfo> fileLoadInfos = dbManager.queryAllFileLoadInfo(1);
            alreadyLvAdapter.setDate(fileLoadInfos);
        }
    }

    private void initPop() {
        // 设置Action
        Log.d(TAG, "initPop: ");
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(0, "卸载", null);
        mItemClickQuickAction.addActionItem(pointItem);
        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    mfileUnstalledInfo = (FileLoadInfo) alreadyLvAdapter.getItemInfo();
                    //卸载
                    AppInstallHelper.unstallApp(content, mfileUnstalledInfo.getPackageName());

                    //删除安装包和正在下载的文件
                    //fileLoad.delete(fileInfo.getUrl());
                    //取消弹出框
                    mItemClickQuickAction.dismiss();
                    source.dismiss();
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
