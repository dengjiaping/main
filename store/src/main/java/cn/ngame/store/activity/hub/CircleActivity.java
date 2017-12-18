package cn.ngame.store.activity.hub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.CircleAdapter;
import cn.ngame.store.bean.CirclePostsInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ToastUtil;

/**
 * 圈子
 * Created by liguoliang on 2017/11/23 0023.
 */
@SuppressLint("WrongConstant")
public class CircleActivity extends BaseFgActivity {
    protected static final String TAG = CircleActivity.class.getSimpleName();
    private LinearLayout ll_back;
    private CircleActivity mContext;
    private TextView titleTv, postIdTv, mHeaderPostsNum, mHeaderName;
    private int postId = 0;
    private List<CirclePostsInfo.DataBean> mDatas = new ArrayList<>();
    private ListView mListView;
    private CircleAdapter mAdapter;
    private TextView headerLastUpdateTv;
    private LinearLayout mTopLayout;
    private View mTopItemBt;
    private TextView mTopTv;
    private String pageSize = "10";
    private CirclePostsInfo.DataBean.ShowPostCategoryBean showPostCategoryBean;
    private SimpleDraweeView mHeaderSdv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);
        initStatusBar();
        postId = getIntent().getIntExtra(KeyConstant.postId, 0);
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
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setPrimaryColors(Color.WHITE);
        refreshLayout.autoRefresh();
        mListView = findViewById(R.id.hub_circle_lv);

        //头部
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_hub_circle_header, null);
        mTopLayout = headerView.findViewById(R.id.circle_post_top_layout);
        mHeaderSdv= headerView.findViewById(R.id.circle_header_imageview);
        mHeaderName = headerView.findViewById(R.id.circle_name_tv);
        mHeaderPostsNum= headerView.findViewById(R.id.circle_post_nub_tv);

        mListView.addHeaderView(headerView);


        //设置布局管理器
        mAdapter = new CircleAdapter(mContext, mDatas);
        mListView.setAdapter(mAdapter);

        //设置下拉刷新和加载
        setLoadHeaderFooter(refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                getDatas(refreshLayout);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(0);
            /*    mDatas.add("新数据11");
                mAdapter.setData(mDatas);*/
            }
        });

    }

    private void setLoadHeaderFooter(RefreshLayout refreshLayout) {
        // Header
        final ClassicsHeader header = new ClassicsHeader(this);
        header.getTitleText().setTextSize(14);
        headerLastUpdateTv = header.getLastUpdateText();
        headerLastUpdateTv.setVisibility(View.GONE);
        header.setDrawableProgressSizePx(62);
        header.setDrawableArrowSizePx(57);
        refreshLayout.setRefreshHeader(header, ImageUtil.getScreenWidth(this), 200);
        // Footer
        ClassicsFooter footer = new ClassicsFooter(mContext);
        footer.getTitleText().setTextSize(14);
        footer.setDrawableArrowSizePx(57);
        refreshLayout.setRefreshFooter(footer, ImageUtil.getScreenWidth(this), 180);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
    }

    private void getDatas(final RefreshLayout refreshLayout) {
        String url = Constant.WEB_SITE + Constant.URL_CIRCLE_POSTS_LIST;
        Response.Listener<CirclePostsInfo> successListener = new Response.Listener<CirclePostsInfo>() {
            @Override
            public void onResponse(CirclePostsInfo result) {
                if (result == null || result.getCode() != 0) {
                    ToastUtil.show(mContext, getString(R.string.server_exception));
                    return;
                }
                mDatas = result.getData();
                if (mDatas != null) {
                    mTopLayout.setPadding(0, 12, 0, 10);
                    mTopLayout.removeAllViews();
                    for (final CirclePostsInfo.DataBean mData : mDatas) {
                        if (mData != null) {
                            //顶部
                            if (showPostCategoryBean == null) {
                                showPostCategoryBean = mData.getShowPostCategory();
                                mHeaderSdv.setImageURI(showPostCategoryBean.getPostCategoryUrl());
                                mHeaderName.setText(showPostCategoryBean.getPostCategoryName());
                                mHeaderPostsNum.setText("帖子："+showPostCategoryBean.getPostCategoryCount());
                            }
                            mTopItemBt = LayoutInflater.from(mContext).inflate(R.layout.layout_circle_top_item, null);
                            mTopTv = mTopItemBt.findViewById(R.id.circle_top_title_tv);
                            mTopTv.setText(mData.getPostTitle());
                            mTopItemBt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.putExtra(KeyConstant.postId, mData.getId());
                                    intent.setClass(mContext, HubItemActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                            mTopLayout.addView(mTopItemBt);
                        }
                    }
                    mAdapter.setData(mDatas);
                }

                refreshLayout.finishRefresh(0);
                headerLastUpdateTv.setVisibility(View.GONE);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                refreshLayout.finishRefresh(0);
                headerLastUpdateTv.setVisibility(View.GONE);
            }
        };

        Request<CirclePostsInfo> request = new GsonRequest<CirclePostsInfo>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<CirclePostsInfo>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.postCategoryId, String.valueOf(postId));
                params.put(KeyConstant.pageIndex, String.valueOf(0));
                params.put(KeyConstant.PAGE_SIZE, pageSize);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }


}