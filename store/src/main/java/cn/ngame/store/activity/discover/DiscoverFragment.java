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
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.DiscoverClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.activity.main.TopicsListActivity;
import cn.ngame.store.adapter.discover.DiscoverClassifyTopAdapter;
import cn.ngame.store.adapter.discover.DiscoverIvAdapter;
import cn.ngame.store.adapter.discover.DiscoverTvIvAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.RecommendTopicsItemInfo;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.game.view.LabelGameListActivity;
import cn.ngame.store.util.ToastUtil;
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
    private DiscoverAdapter adapter;
    private List<DiscoverListBean.DataBean.GameCategroyListBean> categroyList = new ArrayList<>();

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
        context = getActivity();
        init(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {//3
        super.onActivityCreated(savedInstanceState);
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

        //添加头部
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
        getData();
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
        remenAdapter = new DiscoverClassifyTopAdapter(context, categroyList);
        mRVClassifyAll.setAdapter(remenAdapter);
        //分类条目点击
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
            }/* else if (id == R.id.more_strategy_tv) {
                intent.setClass(context, LabelGameListActivity.class);
                intent.putExtra(KeyConstant.category_Id, 368 + "");//单机id 336
                intent.putExtra(KeyConstant.TITLE, "策略");
                context.startActivity(intent);
            }*/
        }
    };

    private void init(View view) {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setPullLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            //下拉
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                //getGameList();
                if (!NetUtil.isNetworkConnected(context)) {
                    ToastUtil.show(context, "无网络连接");
                    pullListView.onPullUpRefreshComplete();
                    pullListView.onPullDownRefreshComplete();
                    if (0 == pageAction.getCurrentPage()) {
                        pullListView.getRefreshableView().setSelection(0);
                    }
                } else {
                    android.util.Log.d(TAG, "onPullDownToRefresh: 下拉请求数据");
                    //下拉请求数据
                    getData();
                }
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                pullListView.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ?
                            pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    //上拉请求数据
                    getData();
                } else {
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });


        //onRecyclerViewItemClick(mRVClassifyAll, 1);
        adapter = new DiscoverAdapter(context, getSupportFragmentManager(), listNew, 0);
        pullListView.getRefreshableView().setAdapter(adapter);
        //getGameList();
        getBannerData();//轮播图
    }

    protected final static String TAG = DiscoverFragment.class.getSimpleName();

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
        }

        //请求数据
        RecommendListBody bodyBean = new RecommendListBody();
        new DiscoverClient(context, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<DiscoverListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        android.util.Log.d(TAG, "getGameListonError: ");
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(DiscoverListBean result) {
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                        if (result != null && result.getCode() == 0) {
                            setData(result);
                        } else {
                            //请求失败
                            android.util.Log.d(TAG, "请求失败");
                        }
                    }
                });
    }

    //todo 设置数据
    public void setData(DiscoverListBean result) {
        DiscoverListBean.DataBean data = result.getData();
        android.util.Log.d(TAG, "setData: " + data);
        if (data == null) {
            return;
        }
        categroyList = data.getGameCategroyList();
        if (null != categroyList) {
            remenAdapter.setList(categroyList);
        }
        android.util.Log.d(TAG, "categroyList: " + categroyList.size());
        adapter.setList(listNew);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setPullLoadEnabled(false);
    }

    private PageAction pageAction;
    public static int PAGE_SIZE = 8;
    private List<RecommendListBean.DataBean> listNew = new ArrayList<>();

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
