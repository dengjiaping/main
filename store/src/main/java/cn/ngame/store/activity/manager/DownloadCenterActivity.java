package cn.ngame.store.activity.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DCViewPagerAdapter;

/**
 * 管理-下载更新
 * Created by gp on 2017/3/23 0023.
 */

public class DownloadCenterActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    private ViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private DCViewPagerAdapter adapter;
    List<String> tabList = new ArrayList<String>();
    String typeValue = "";
    GameRankListBean bean;
    String updateValue = "";
    private DownloadCenterActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_download_center);
        mContext = this;
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("下载中心");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
    }

}
