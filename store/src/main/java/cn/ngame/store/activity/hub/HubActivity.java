package cn.ngame.store.activity.hub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.HubAdapter;
import cn.ngame.store.core.utils.ImageUtil;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */

public class HubActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private HubActivity mContext;
    private TextView titleTv;
    private ArrayList<String> mDatas;
    private RecyclerView mRecyclerView;
    private HubAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        initStatusBar();
        mContext = this;
        init();
    }

    private void init() {
        ll_back = findViewById(R.id.ll_back);
        titleTv = findViewById(R.id.tv_title);

        titleTv.setText("圈子");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDatas();
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(Color.WHITE);
        refreshLayout.autoRefresh();
        mRecyclerView = findViewById(R.id.recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HubAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //设置 Header的样式
        ClassicsHeader header = new ClassicsHeader(this);
        header.getTitleText().setTextSize(14);
        header.setDrawableProgressSizePx(60);
        header.setDrawableArrowSizePx(56);
        refreshLayout.setRefreshHeader(header, ImageUtil.getScreenWidth(this), 200);
        //设置 Footer
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.d(TAG, "刷新!!");
                refreshlayout.finishRefresh();
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.d(TAG, "加载更多!!");
                refreshlayout.finishLoadmore();
                mDatas.add("新数据");
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList("新游", "明品", "小霸王", "金貂", "科腾", "北通"));
    }


}