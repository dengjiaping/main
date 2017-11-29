package cn.ngame.store.activity.hub;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.ImageUtil;

/**
 * 圈子
 * Created by gp on 2017/3/23 0023.
 */

public class HubActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private HubActivity mContext;
    private TextView titleTv;
    private ArrayList<String> mDatas;
    private RecyclerView mRecyclerView;
    private HubAdapter mAdapter;
    private LinearLayout.LayoutParams params;

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
        params = new LinearLayout.LayoutParams(ImageUtil.getScreenWidth(this) / 2, ViewGroup
                .LayoutParams
                .WRAP_CONTENT);
        titleTv.setText("圈子");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDatas();
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(Color.BLUE);
        refreshLayout.autoRefresh();
        mRecyclerView = findViewById(R.id.recyclerview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new HubAdapter(mContext, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //设置 Header的样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(this), ImageUtil.getScreenWidth(this), 220);
        //设置 Footer
        refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
                mDatas.remove(0);
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                mDatas.add("新数据");
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList("新游", "明品", "小霸王", "金貂", "科腾", "GT-COUPE机器酷博",
                "WELCOME惠康", "北通"));
    }

    public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<String> mDatas;
        private TextView tv;
        private View itemView;

        public HubAdapter(Context context, List<String> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        public void setData(ArrayList<String> data) {
            this.mDatas = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            GridLayout mLayoutTags;
            TextView mTxt;
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public HubAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.item_hub_rv_item,
                    viewGroup, false);
            HubAdapter.ViewHolder viewHolder = new HubAdapter.ViewHolder(view);
            viewHolder.mTxt = view.findViewById(R.id.singer_item_tv);
            viewHolder.mLayoutTags = view.findViewById(R.id.layout_tags2);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(HubAdapter.ViewHolder viewHolder, final int position) {
            viewHolder.mTxt.setText(mDatas.get(position));
            for (int i = 0; i < 5; i++) {
                itemView = mInflater.inflate(R.layout.layout_hub_gl_item, null);
                itemView.setLayoutParams(params);
                //这是显示数据的控件
                tv = itemView.findViewById(R.id.hub_gl_item_tv);
                tv.setText("条目" + i);
                viewHolder.mLayoutTags.addView(itemView);
            }
        }

    }
}