package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DCViewPagerAdapter;
import cn.ngame.store.core.utils.ImageUtil;

/**
 * 管理-下载更新
 * Created by gp on 2017/3/23 0023.
 */

public class MyGameDownloadActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    private TabLayout tablayout;
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private DCViewPagerAdapter adapter;
    List<String> tabList = new ArrayList<String>();
    String typeValue = "";
    GameRankListBean bean;
    String updateValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_download_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("下载更新");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyGameDownloadActivity.this.finish();
            }
        });
        bean = (GameRankListBean) getIntent().getSerializableExtra("updateGame");
        if (bean != null) {
            if (bean.getData().size() > 0) {
                updateValue = "(" + bean.getData().size() + ")";
            } else {
                updateValue = "";
            }
        }

        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(3);
        initViewPager();
        initTabs();
    }

    private void initViewPager() {
        tabList.add("下载中");
        tabList.add("已下载");
        tabList.add("可更新" );
        fragments = new ArrayList<Fragment>();
        fragments.add(ManagerLikeFragment.newInstance(typeValue, 1));
        fragments.add(ManagerInstalledFragment.newInstance(typeValue, 2));
        fragments.add(ManagerNecessaryFragment.newInstance(typeValue, 0));

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
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(16);

            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int width = textView.getMeasuredWidth();//每个textView宽度
            int screenWidth = ImageUtil.getScreenWidth(this);//屏幕宽度
            int margin = (screenWidth / tabList.size() - width) / tabList.size() + 5;
            if (tabList.size() <= 4) {
                layoutParams.setMargins(30, 0, 30, 0);
            } else {
                layoutParams.setMargins(margin, 0, margin, 0);
            }
        }
    }
}
