package cn.ngame.store.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ngame.store.R;
import cn.ngame.store.activity.rank.RankCommentFragment;
import cn.ngame.store.activity.rank.RankDownloadFragment;
import cn.ngame.store.adapter.DCViewPagerAdapter2;
import cn.ngame.store.base.fragment.BaseSearchFragment;

/**
 * 排行
 * Created by gp on 2017/3/14 0014.
 */

public class RankingFragment extends BaseSearchFragment implements View.OnClickListener {

    private TextView tv_download, tv_comment;
    private ViewPager viewpager;

    private ArrayList<Fragment> fragments;
    DCViewPagerAdapter2 adapter;
    private RankDownloadFragment downloadFragment;
    private RankCommentFragment commentFragment;

    public static RankingFragment newInstance(String arg) {
        RankingFragment fragment = new RankingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.ranking_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        //        typeValue = getArguments().getInt("", 1);
        tv_download = (TextView) view.findViewById(R.id.tv_download);
        tv_comment = (TextView) view.findViewById(R.id.tv_comment);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        tv_download.setOnClickListener(this);
        tv_comment.setOnClickListener(this);

        viewpager.setOffscreenPageLimit(2);//预加载  多缓存2个页面
        initViewPager();

//        commentFragment = new RankCommentFragment();
//        getChildFragmentManager().beginTransaction().add(fl_ment, commentFragment).commit();
//        downloadFragment = new RankDownloadFragment();
//        getChildFragmentManager().beginTransaction().add(fl_ment, downloadFragment).commit();
    }

    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(RankDownloadFragment.newInstance(0));
        fragments.add(RankCommentFragment.newInstance(0));
        adapter = new DCViewPagerAdapter2(getChildFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_download.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tv_download.setBackgroundResource(R.drawable.shape_white_rank_download);
                        tv_comment.setTextColor(getResources().getColor(R.color.white));
                        tv_comment.setBackgroundResource(R.drawable.shape_green_rank_comment);
                        break;
                    case 1:
                        tv_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tv_comment.setBackgroundResource(R.drawable.shape_white_rank_comment);
                        tv_download.setTextColor(getResources().getColor(R.color.white));
                        tv_download.setBackgroundResource(R.drawable.shape_green_rank_download);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_download:
                viewpager.setCurrentItem(0);
                tv_download.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_download.setBackgroundResource(R.drawable.shape_white_rank_download);
                tv_comment.setTextColor(getResources().getColor(R.color.white));
                tv_comment.setBackgroundResource(R.drawable.shape_green_rank_comment);
//                if (downloadFragment.isAdded()) {
//                    getSupportFragmentManager().beginTransaction().hide(commentFragment).show(downloadFragment).commit();
//                } else {
//                    downloadFragment = new RankDownloadFragment();
//                    getSupportFragmentManager().beginTransaction().add(fl_ment, downloadFragment).commit();
//                }
                break;
            case R.id.tv_comment:
                viewpager.setCurrentItem(1);
                tv_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_comment.setBackgroundResource(R.drawable.shape_white_rank_comment);
                tv_download.setTextColor(getResources().getColor(R.color.white));
                tv_download.setBackgroundResource(R.drawable.shape_green_rank_download);
//                if (commentFragment.isAdded()) {
//                    getSupportFragmentManager().beginTransaction().hide(downloadFragment).show(commentFragment).commit();
//                } else {
//                    commentFragment = new RankCommentFragment();
//                    getSupportFragmentManager().beginTransaction().add(fl_ment, commentFragment).commit();
//                }
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
