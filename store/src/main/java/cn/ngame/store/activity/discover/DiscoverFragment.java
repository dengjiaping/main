package cn.ngame.store.activity.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.classification.ClassifiHomeBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.activity.main.TopicsListActivity;
import cn.ngame.store.adapter.HomeRaiderAdapter;
import cn.ngame.store.adapter.discover.DiscoverClassifyTopAdapter;
import cn.ngame.store.adapter.discover.DiscoverIvAdapter;
import cn.ngame.store.adapter.discover.DiscoverTvIvAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.RecommendTopicsItemInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.LabelGameListActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.view.RecyclerViewDivider;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 分类
 * Created by gp on 2017/3/14 0014.
 */

public class DiscoverFragment extends BaseSearchFragment implements View.OnClickListener {
    private FragmentActivity context;
    private PullToRefreshListView pullListView;
    private RecyclerView mRVClassifyAll;
    DiscoverClassifyTopAdapter remenAdapter;
    private BannerView bannerView;
    private List<String> classifyList = new ArrayList<>(Arrays.asList("角色", "动作", "原生", "策略", "模拟", "VR", "枪战", "体育", "格斗"));
    private List<String> mEverydayList = new ArrayList();
    private DiscoverIvAdapter mTopicsAdapter;
    private RecyclerView mEverydayRv;
    private RecyclerView mActionRv;
    private RecyclerView mHotRecentRv;
    private RecyclerView mSubjectRv;
    private DiscoverTvIvAdapter mEverydayAdapter;
    private RecyclerView mBigChang_Rv;
    private RecyclerView mStrategyRv;
    private List<String> mHotRecentList = new ArrayList();
    private List<String> mActionList = new ArrayList();
    private List<String> mStrategyList = new ArrayList();
    private DiscoverIvAdapter mBigChangAdapter;
    private DiscoverTvIvAdapter mHotRecentAdapter;
    private DiscoverTvIvAdapter mActionAdapter;
    private DiscoverTvIvAdapter mStrategyAdapter;
    private PicassoImageView picassoImageView;
    private int match_parent;
    private LinearLayout.LayoutParams hParams;
    private String selectImage;
    private List<RecommendTopicsItemInfo> topicsInfoList = new ArrayList<>();
    private List<RecommendTopicsItemInfo> mBigChangInfoList = new ArrayList<>();
    private RecommendTopicsItemInfo topicsInfo;

    public DiscoverFragment() {
        android.util.Log.d(TAG, "DiscoverFragment: ()");
    }

    public static DiscoverFragment newInstance(String arg) {
        android.util.Log.d(TAG, "discover newInstance: ()");
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putString("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void initViewsAndEvents(View view) {//2
        init(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//3
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        View headView = View.inflate(context, R.layout.discover_header_view, null);//头部
        //分类
        init0ClassifyView(headView);
        //每日新发现
        init1EverydayDiscoverView(headView);
        //近期最热
        initHotRecentView(headView);
        //专题
        initTopicsView(headView);
        //大厂
        initBigChangView(headView);
        //动作
        initActionView(headView);
        //策略
        initStrategyView(headView);

        //添加头部
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
    }

    //近期最热
    private void initHotRecentView(View headView) {
        for (int i = 0; i < 10; i++) {
            mHotRecentList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_20.png");
        }
        mHotRecentRv = (RecyclerView) headView.findViewById(R.id.rv_hot_recent);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mHotRecentRv.setLayoutManager(linearLayoutManager2);
        mHotRecentAdapter = new DiscoverTvIvAdapter(context, mHotRecentList);
        mHotRecentRv.setAdapter(mHotRecentAdapter);
        mHotRecentRv.addItemDecoration(new RecyclerViewDivider(context,
                20, 18, mHotRecentList.size()));
        setOnMoreBtClickListener(headView, R.id.more_hot_recent_tv);
    }

    //分类
    private void init0ClassifyView(View headView) {
        bannerView = (BannerView) headView.findViewById(R.id.banner_view);
        //获取RecyclerView实例
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false);

        mRVClassifyAll = (RecyclerView) headView.findViewById(R.id.discover_head_rv_classify);//条目
        mRVClassifyAll.setLayoutManager(linearLayoutManager1);
        remenAdapter = new DiscoverClassifyTopAdapter(this.getActivity(), classifyList);
        mRVClassifyAll.setAdapter(remenAdapter);
        //分类条目点击
        remenAdapter.setmOnItemClickListener(new DiscoverClassifyTopAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position, String itemText) {
                Intent classifyIntent = new Intent(context, LabelGameListActivity.class);
                classifyIntent.putExtra(KeyConstant.category_Id, 367 + "");//原生手柄 id 367
                classifyIntent.putExtra(KeyConstant.TITLE, itemText);
                context.startActivity(classifyIntent);
            }
        });
        //分类
        headView.findViewById(R.id.discover_top_classify_all_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AllClassifyActivity.class));
            }
        });
    }

    //每日新发现
    private void init1EverydayDiscoverView(View headView) {
        for (int i = 0; i < 10; i++) {
            mEverydayList.add("http://oss.ngame.cn/upload/userHead/1500626608632.png");
        }
        mEverydayRv = (RecyclerView) headView.findViewById(R.id.everyday_discover_recyclerview);
        setOnMoreBtClickListener(headView, R.id.everyday_more_tv1);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mEverydayRv.setLayoutManager(linearLayoutManager2);
        mEverydayAdapter = new DiscoverTvIvAdapter(this.getActivity(), mEverydayList);
        mEverydayRv.setAdapter(mEverydayAdapter);
        mEverydayRv.addItemDecoration(new RecyclerViewDivider(context,
                20, 18, mEverydayList.size()));
    }

    //专题
    private void initTopicsView(View headView) {
        mSubjectRv = (RecyclerView) headView.findViewById(R.id.rv_subject);
        setOnMoreBtClickListener(headView, R.id.more_subject_tv);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mSubjectRv.setLayoutManager(linearLayoutManager2);

        mTopicsAdapter = new DiscoverIvAdapter(context, topicsInfoList);
        mSubjectRv.setAdapter(mTopicsAdapter);
        getTopicsInfoList();
        mTopicsAdapter.setOnItemClickListener(new DiscoverIvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                RecommendTopicsItemInfo topicsInfo = topicsInfoList.get(position);
                singeTopicsDetailIntent.putExtra(KeyConstant.ID, topicsInfo.getId());
                singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, topicsInfo.getTitle());
                singeTopicsDetailIntent.putExtra(KeyConstant.DESC, topicsInfo.getSelectDesc());
                singeTopicsDetailIntent.putExtra(KeyConstant.URL, topicsInfo.getSelectImage());
                startActivity(singeTopicsDetailIntent);
            }
        });
        //条目距离
        mSubjectRv.addItemDecoration(new RecyclerViewDivider(context,
                20, 18, topicsInfoList.size()));
    }

    //大厂
    private void initBigChangView(View headView) {
        mBigChang_Rv = (RecyclerView) headView.findViewById(R.id.rv_big_chang);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBigChang_Rv.setLayoutManager(linearLayoutManager2);
        mBigChangAdapter = new DiscoverIvAdapter(context, mBigChangInfoList);
        mBigChang_Rv.setAdapter(mBigChangAdapter);

        //获取大厂数据
        getBigChangInfoList();
        mBigChangAdapter.setOnItemClickListener(new DiscoverIvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                topicsInfo = mBigChangInfoList.get(position);
                singeTopicsDetailIntent.putExtra(KeyConstant.ID, topicsInfo.getId());
                singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, topicsInfo.getTitle());
                singeTopicsDetailIntent.putExtra(KeyConstant.DESC, topicsInfo.getSelectDesc());
                singeTopicsDetailIntent.putExtra(KeyConstant.URL, topicsInfo.getSelectImage());
                startActivity(singeTopicsDetailIntent);
            }
        });
        mBigChang_Rv.addItemDecoration(new RecyclerViewDivider(context,
                20, 18, mBigChangInfoList.size()));
        setOnMoreBtClickListener(headView, R.id.more_big_chang_tv);
    }

    //todo 动作
    private void initActionView(View headView) {
        for (int i = 0; i < 10; i++) {
            mActionList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_14.png");
        }
        mActionRv = (RecyclerView) headView.findViewById(R.id.rv_action);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mActionRv.setLayoutManager(linearLayoutManager2);
        mActionAdapter = new DiscoverTvIvAdapter(context, mActionList);
        mActionRv.setAdapter(mActionAdapter);
        mActionRv.addItemDecoration(new RecyclerViewDivider(context,
                20, 18, mActionList.size()));
        setOnMoreBtClickListener(headView, R.id.more_action_tv);
    }

    //策略
    private void initStrategyView(View headView) {
        for (int i = 0; i < 10; i++) {
            mStrategyList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_15.png");
        }
        mStrategyRv = (RecyclerView) headView.findViewById(R.id.rv_strategy);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(
                this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mStrategyRv.setLayoutManager(linearLayoutManager2);
        mStrategyAdapter = new DiscoverTvIvAdapter(context, mStrategyList);
        mStrategyRv.setAdapter(mStrategyAdapter);
        mStrategyRv.addItemDecoration(new RecyclerViewDivider(context, 20, 18, mStrategyList.size()));
        setOnMoreBtClickListener(headView, R.id.more_strategy_tv);
    }

    //更多按钮设置点击监听
    private void setOnMoreBtClickListener(View headView, int moreBtId) {
        headView.findViewById(moreBtId).setOnClickListener(mMoreBtClickListener);
    }

    //查看更多 按钮点击
    View.OnClickListener mMoreBtClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent = new Intent();
            //每日新发现
            if (id == R.id.everyday_more_tv1) {
                intent.setClass(context, LabelGameListActivity.class);
                intent.putExtra(KeyConstant.category_Id, 366 + "");//云端适配id 336
                intent.putExtra(KeyConstant.TITLE, "每日新发现");
                context.startActivity(intent);
                //近期最热
            } else if (id == R.id.more_hot_recent_tv) {
                intent.setClass(context, LabelGameListActivity.class);
                intent.putExtra(KeyConstant.category_Id, 380 + "");//原生手柄 id 336   一周新游 380
                intent.putExtra(KeyConstant.TITLE, "近期最热");
                context.startActivity(intent);
                //专题
            } else if (id == R.id.more_subject_tv) {
                intent.setClass(context, TopicsListActivity.class);
                context.startActivity(intent);
                //大厂
            } else if (id == R.id.more_big_chang_tv) {
                intent.setClass(context, TopicsListActivity.class);
                context.startActivity(intent);
                //动作
            } else if (id == R.id.more_action_tv) {
                Intent i = new Intent();
                i.setClass(context, LabelGameListActivity.class);
                i.putExtra(KeyConstant.ID, 369 + "");// 动作游戏精选 getId()==369
                i.putExtra(KeyConstant.TITLE, "动作");//mStickyLV.get(position).getTypeName()
                startActivity(i);
                //策略
            } else if (id == R.id.more_strategy_tv) {
                intent.setClass(context, LabelGameListActivity.class);
                intent.putExtra(KeyConstant.category_Id, 368 + "");//单机id 336
                intent.putExtra(KeyConstant.TITLE, "策略");
                context.startActivity(intent);
            }
        }
    };

    private void init(View view) {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            //下拉
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //getGameList();
                getData();
                pullListView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.onPullUpRefreshComplete();
            }
        });


        //onRecyclerViewItemClick(mRVClassifyAll, 1);
        List<String> list = new ArrayList<>();
        list.add(new String("测试"));
        list.add(new String("测试2"));
        HomeRaiderAdapter adapter = new HomeRaiderAdapter(context, list, "0");
        pullListView.getRefreshableView().setAdapter(adapter);

        //getGameList();
        getBannerData();//轮播图
        ClassifiHomeBean result = new ClassifiHomeBean();

        //listData(result);
    }

    //请求数据
    private void getData() {
        //每日
        mEverydayList.clear();
        mActionList.clear();
        mHotRecentList.clear();
        //mSubjectList.clear();
        //mBigChangList.clear();
        for (int i = 0; i < 10; i++) {
            mEverydayList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_16.png");
            mEverydayAdapter.setList(mEverydayList);
            //每日

            mHotRecentList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_13.png");
            mHotRecentAdapter.setList(mHotRecentList);
            //专题
            //大厂
            //动作
            mActionList.add("http://ngame.oss-cn-hangzhou.aliyuncs.com/userRecommendAvatar/tuijian_touxiang_12.png");
            mActionAdapter.setList(mActionList);
        }
    }
    /**
     * 获取专题数据
     */
    private void getBigChangInfoList() {//0  专题
        String url = Constant.WEB_SITE + Constant.URL_DISCOVER_BANNER;
        Response.Listener<JsonResult<List<RecommendTopicsItemInfo>>> successListener = new Response
                .Listener<JsonResult<List<RecommendTopicsItemInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<RecommendTopicsItemInfo>> result) {
                if (result == null) {
                    return;
                }
                if (result.code == 0) {
                    mBigChangInfoList = result.data;
                    mBigChangAdapter.setList(mBigChangInfoList);
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    //ToastUtil.show(context, getString(R.string.requery_failed));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<RecommendTopicsItemInfo>>> request = new GsonRequest<JsonResult<List<RecommendTopicsItemInfo>>>
                (Request.Method.POST, url,
                        successListener, errorListener, new TypeToken<JsonResult<List<RecommendTopicsItemInfo>>>() {
                }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }
    /**
     * 获取专题数据
     */
    private void getTopicsInfoList() {//0  专题
        String url = Constant.WEB_SITE + Constant.URL_DISCOVER_BANNER;
        Response.Listener<JsonResult<List<RecommendTopicsItemInfo>>> successListener = new Response
                .Listener<JsonResult<List<RecommendTopicsItemInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<RecommendTopicsItemInfo>> result) {
                if (result == null) {
                    return;
                }
                if (result.code == 0) {
                    topicsInfoList = result.data;
                    mTopicsAdapter.setList(topicsInfoList);
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    //ToastUtil.show(context, getString(R.string.requery_failed));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<RecommendTopicsItemInfo>>> request = new GsonRequest<JsonResult<List<RecommendTopicsItemInfo>>>
                (Request.Method.POST, url,
                        successListener, errorListener, new TypeToken<JsonResult<List<RecommendTopicsItemInfo>>>() {
                }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {
        String url = Constant.WEB_SITE + Constant.URL_DISCOVER_BANNER;
        Response.Listener<JsonResult<List<RecommendTopicsItemInfo>>> successListener = new Response
                .Listener<JsonResult<List<RecommendTopicsItemInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<RecommendTopicsItemInfo>> result) {
                if (result == null) {
                    return;
                }
                if (result.code == 0) {
                    List<RecommendTopicsItemInfo> topicsInfoList = result.data;

                    //创建轮播图BannerData
                    List<ImageView> list = createBannerView(topicsInfoList);
                    bannerView.setData(list);
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    //ToastUtil.show(context, getString(R.string.requery_failed));
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<List<RecommendTopicsItemInfo>>> request = new GsonRequest<JsonResult<List<RecommendTopicsItemInfo>>>
                (Request.Method.POST, url,
                        successListener, errorListener, new TypeToken<JsonResult<List<RecommendTopicsItemInfo>>>() {
                }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 创建轮播视图
     */
    private ArrayList<ImageView> list = new ArrayList<>();
    private Intent singeTopicsDetailIntent = new Intent();

    private List<ImageView> createBannerView(List<RecommendTopicsItemInfo> bannerInfoList) {

        int bannerListSize = bannerInfoList.size();
        if (bannerInfoList == null || bannerListSize <= 0) {
            return null;
        }
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
        int heght = CommonUtil.dip2px(context, 158f);
        int ic_def_logo_720_228 = R.drawable.ic_def_logo_720_288;
        match_parent = ViewGroup.LayoutParams.MATCH_PARENT;
        for (int i = 0; i < bannerListSize; i++) {
            final RecommendTopicsItemInfo info = bannerInfoList.get(i);
            selectImage = info.getSelectImage();
            picassoImageView = new PicassoImageView(context);
            picassoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            hParams = new LinearLayout.LayoutParams(
                    match_parent, match_parent);
            hParams.height = heght;
            picassoImageView.setLayoutParams(hParams);
            // picassoImageView.setId(info.getId());
            // picassoImageView.setTag(info.getSelectImage());

            picassoImageView.setImageUrl(selectImage, ic_def_logo_720_228);
            picassoImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            picassoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singeTopicsDetailIntent.putExtra(KeyConstant.ID, info.getId());
                    singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, info.getTitle());
                    singeTopicsDetailIntent.putExtra(KeyConstant.DESC, info.getSelectDesc());
                    singeTopicsDetailIntent.putExtra(KeyConstant.URL, selectImage);
                    startActivity(singeTopicsDetailIntent);
                }
            });
            list.add(picassoImageView);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
