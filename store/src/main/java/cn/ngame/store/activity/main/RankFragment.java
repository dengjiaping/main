package cn.ngame.store.activity.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.rank.RankCommentFragment;
import cn.ngame.store.activity.rank.RankDownloadFragment;
import cn.ngame.store.adapter.RankTopPagerAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.core.utils.CommonUtil;


/**
 * 排行
 * Created by gp on 2017/3/14 0014.
 */

public class RankFragment extends BaseSearchFragment {
    String typeValue = "";
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private RankTopPagerAdapter adapter;
    private RankDownloadFragment downloadFragment;
    private RankCommentFragment commentFragment;
    private TabLayout tablayout;
    private TextView textView;
    private LinearLayout.LayoutParams layoutParams;

    public static RankFragment newInstance(String arg) {
        RankFragment fragment = new RankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int curTab = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        //        typeValue = getArguments().getInt("", 1);
        tablayout = (TabLayout) view.findViewById(R.id.rank_tablayout);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        viewpager.setOffscreenPageLimit(1);//预加载  多缓存2个页面
        initViewPager();
        initTabs();
    }

    private void initTabs() {
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE); //固定模式
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tablayout.setupWithViewPager(viewpager);
        ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        int dp10 = CommonUtil.dip2px(getActivity(), 10);
        int dp14 = CommonUtil.dip2px(getActivity(), 14);
        int dp34 = CommonUtil.dip2px(getActivity(), 34);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            textView = (TextView) view.getChildAt(1);
            textView.setTextSize(dp14);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);//textciew的宽度   AT_MOST
            int width = textView.getMeasuredWidth();

            layoutParams.height = dp34;
            layoutParams.weight = width;
            if (0 == i) {
                layoutParams.setMargins(dp10, 0, dp10, 0);
            } else {
                layoutParams.setMargins(0, 0, dp10, 0);
            }
        }
    }

    private List<String> tabList = new ArrayList<String>();

    private void initViewPager() {
        tabList.add("全部");
        tabList.add("手柄游戏");
        tabList.add("破解游戏");
        tabList.add("汉化游戏");
        tabList.add("特色游戏");
        tabList.add("模拟器");
        fragments = new ArrayList<>();

        for (int i = 0; i < tabList.size(); i++) {
            RankCommentFragment fragment = new RankCommentFragment(curTab);
            fragment.setTabPos(i);
            fragments.add(fragment);
        }
        adapter = new RankTopPagerAdapter(getChildFragmentManager(), fragments, tabList);
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //滑动监听加载数据，一次只加载一个标签页
                ((RankCommentFragment) adapter.getItem(position)).sendMessage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
