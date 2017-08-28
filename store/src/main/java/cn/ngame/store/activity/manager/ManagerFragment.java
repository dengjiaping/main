package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.DCViewPagerAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;


/**
 * 管理
 * Created by gp on 2017/3/14 0014.
 */

public class ManagerFragment extends BaseSearchFragment {
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments2 = new ArrayList<>();
    private ArrayList<Fragment> fragments3 = new ArrayList<>();
    private DCViewPagerAdapter adapter;
    String typeValue = "";
    private FragmentActivity context;
    private String pwd;
    private boolean isNeedLoad = true;
    private InstalledFragment installedFragment;
    private NecessaryFragment necessaryFragment;
    private LikeFragment likeFragment;

    public static ManagerFragment newInstance() {
        Bundle args = new Bundle();
        ManagerFragment fragment = new ManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_manager;
    }

    @Override
    protected void initViewsAndEvents(View view) {//初始化
        context = getActivity();
        likeFragment = LikeFragment.newInstance(typeValue, 1);
        installedFragment = InstalledFragment.newInstance(typeValue, 2);
        necessaryFragment = NecessaryFragment.newInstance(typeValue, 0);
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        //viewpager每次切换的时候， 会重新创建当前界面及左右界面三个界面， 每次切换都要重新oncreate,
        // 表示三个界面之间来回切换都不会重新加载
        //viewpager.setOffscreenPageLimit(0);
        //setTabViewPagerData();
        fragments2.add(installedFragment);
        fragments2.add(necessaryFragment);

        fragments3.add(installedFragment);
        fragments3.add(likeFragment);
        fragments3.add(necessaryFragment);
        adapter = new DCViewPagerAdapter(getChildFragmentManager(), fragments2, tabList2);
    }


    @Override
    public void onStart() {
        super.onStart();
        //是否显示了,显示了就去加载
        Log.d(TAG, "ManagerFragment onStart:加载数据 " + isNeedLoad);
        if (isNeedLoad) {
            setTabViewPagerData();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isNeedLoad = !hidden;
        Log.d(TAG, "ManagerFragment onHiddenChanged: 加载数据" + isNeedLoad);
        if (isNeedLoad) {
            setTabViewPagerData();
        }
        installedFragment.onHiddenChanged(hidden);
        likeFragment.onHiddenChanged(hidden);
        necessaryFragment.onHiddenChanged(hidden);
    }

    String[] tabList3 = {"已装", "喜欢", "必备"};
    String[] tabList2 = {"已装", "必备"};

    private void setTabViewPagerData() {
        //没有登录
        pwd = StoreApplication.passWord;
        if ((pwd != null && !"".endsWith(pwd)) || !Constant.PHONE.equals(StoreApplication.loginType)) {
            //已登录
            adapter.setList(fragments3, tabList3);
        } else {
            //未登录
            adapter.setList(fragments2, tabList2);
        }
        initViewPagerTabs();
    }

    private void initViewPagerTabs() {
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_FIXED); //固定模式
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        int childCount = viewGroup.getChildCount() - 1;
        for (int i = 0; i <= childCount; i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(CommonUtil.dip2px(context, 16));
            if (i < childCount) {
                layoutParams.setMargins(0, 0, 2, 0);
            }
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
