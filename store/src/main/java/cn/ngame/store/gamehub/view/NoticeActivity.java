package cn.ngame.store.gamehub.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DCViewPagerAdapter;
import cn.ngame.store.core.utils.ImageUtil;

/**
 * 通知
 * Created by gp on 2017/3/10 0010.
 */

public class NoticeActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private DCViewPagerAdapter adapter;
    List<String> tabList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strategy_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("通知");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeActivity.this.finish();
            }
        });
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(3);

        initViewPager();
        initTabs();
    }

    private void initViewPager() {
        tabList.add("浏览");
        tabList.add("帖子");
        tabList.add("评论");
        fragments = new ArrayList<Fragment>();
        fragments.add(BrowseFragment.newInstance(1));
        fragments.add(BrowseFragment.newInstance(2));
        fragments.add(CommentFragment.newInstance(0));

        adapter = new DCViewPagerAdapter(getSupportFragmentManager(), fragments, tabList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTabs() {
//        if (tabList.size() <= 3) {
        tablayout.setTabMode(TabLayout.MODE_FIXED); //固定模式
//        } else {
//        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE); //可滑动的tab
//        }
        tablayout.setupWithViewPager(viewpager);
        ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(16);

            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int width = textView.getMeasuredWidth();
            int screenWidth = ImageUtil.getScreenWidth(this);
            int margin = (screenWidth / tabList.size() - width) / tabList.size() + 5;
            if (tabList.size() <= 4) {
                layoutParams.setMargins(30, 0, 30, 0);
            } else {
                layoutParams.setMargins(margin, 0, margin, 0);
            }
        }
    }
}
