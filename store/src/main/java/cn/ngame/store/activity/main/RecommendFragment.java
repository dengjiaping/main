package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.recommend.RecommendClient;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.MainHomeRaiderAdapter;
import cn.ngame.store.adapter.RecommendListAdapter;
import cn.ngame.store.adapter.SelectGameAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.RecommendTopicsItemInfo;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.core.utils.Constant.URL_RECOMMEND_TOPICS;

/**
 * 精选
 * Created by gp on 2017/3/14 0014.
 */

public class RecommendFragment extends BaseSearchFragment {
    public static final String TAG = RecommendFragment.class.getSimpleName();
    PullToRefreshListView pullListView;
    private Timer timer = new Timer();
    private static Handler uiHandler = new Handler();
    /**
     * list集合
     */
    List<YunduanBean.DataBean> yunduanList = new ArrayList<>();
    List<YunduanBean.DataBean> xuniList = new ArrayList<>();
    List<YunduanBean.DataBean> tencentList = new ArrayList<>();
    List<GameInfo> dailyRecommendList = new ArrayList<>();
    List<GameInfo> mobaList = new ArrayList<>();
    List<GameInfo> qiangzhanList = new ArrayList<>();
    List<GameInfo> bTencentList = new ArrayList<>();
    List<GameInfo> allBradnList = new ArrayList<>();
    List<GameHubMainBean.DataBean> homeList = new ArrayList<>();

    private ListView listView_tBarnd, listView_allBarnd;

    private SelectGameAdapter gameListAdapter;
    private MainHomeRaiderAdapter raiderAdapter;
    private SimpleDraweeView from_img_1, from_img_2, game_big_pic_1, game_big_pic_2;
    private TextView gamename_1, gamename_2, summary_2;
    private TextView from_1, from_2, summary_1;

    private IFileLoad fileLoad; //文件下载公共类接口

    private Handler handler = new Handler();
    private RecommendListAdapter adapter;

    private PageAction pageAction;
    public static int PAGE_SIZE = 8;
    List<RecommendListBean.DataBean> topList = new ArrayList<>();
    List<RecommendListBean.DataBean> list = new ArrayList<>();
    private LinearLayout horizontalViewContainer;
    private List<RecommendTopicsItemInfo> gameInfo;
    private FragmentActivity context;
    private Intent singeTopicsDetailIntent = new Intent();
    private LinearLayout.LayoutParams hParams;
    private int wrapContent;
    private SimpleDraweeView picassoImageView;
    private boolean mIsShow = false;
    private ListView refreshableView;

    public static RecommendFragment newInstance(int arg) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initViewsAndEvents(View view) {
//        typeValue = getArguments().getInt("", 1);
        context = getActivity();
        initListView(view);     //初始化
    }

    private void getGameList() {
        RecommendListBody bodyBean = new RecommendListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new RecommendClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<RecommendListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        android.util.Log.d(TAG, "getGameListonError: ");
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(RecommendListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }


    private void getHorizontalData() {
        String url_recommend_topics = Constant.WEB_SITE + URL_RECOMMEND_TOPICS;
        //请求失败
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(context, getString(R.string.pull_to_refresh_network_error));
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };
        // 获取横向滑动的数据
        Response.Listener<JsonResult<List<RecommendTopicsItemInfo>>> successListener = new Response
                .Listener<JsonResult<List<RecommendTopicsItemInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<RecommendTopicsItemInfo>> result) {
                //数据为空
                if (result == null || result.code != 0) {
                    ToastUtil.show(context, getString(R.string.requery_failed));
                    return;
                }
                //GameInfo类  : 请求游戏JN数据后,封装的javabean
                gameInfo = result.data;
                if (null == gameInfo ||  gameInfo.size() == 0) {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                } else {
                    horizontalViewContainer.removeAllViews();
                    int dp10 = CommonUtil.dip2px(context, 10);
                    int width240 = CommonUtil.dip2px(context, 240f);
                    int heght114 = CommonUtil.dip2px(context, 114f);
                    int ic_def_logo_480_228 = R.drawable.ic_def_logo_480_228;
                    int size = gameInfo.size();
                    for (int i = 0; i < size; i++) {
                        final RecommendTopicsItemInfo info = gameInfo.get(i);
                        final String gameImage = info.getSelectImage();//获取每一张图片
                        picassoImageView = new SimpleDraweeView(context);
                        picassoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        //为  PicassoImageView设置属性
                        hParams = new LinearLayout.LayoutParams(
                                wrapContent, wrapContent);
                        hParams.width = width240;
                        hParams.height = heght114;
                        //有多个图片的话
                        if (0 == i) {
                            hParams.setMargins(dp10, 0, dp10, 0);
                        } else {
                            hParams.setMargins(0, 0, dp10, 0);
                        }
                        picassoImageView.setLayoutParams(hParams);
                        //加载网络图片
                        picassoImageView.setImageURI(gameImage);
                        picassoImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                singeTopicsDetailIntent.putExtra(KeyConstant.ID, info.getId());
                                singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, info.getTitle());
                                singeTopicsDetailIntent.putExtra(KeyConstant.DESC, info.getSelectDesc());
                                singeTopicsDetailIntent.putExtra(KeyConstant.URL, gameImage);
                                startActivity(singeTopicsDetailIntent);
                            }
                        });
                        horizontalViewContainer.addView(picassoImageView, i);
                    }
                }


            }
        };

        //------------------------------------------------------------------------------
        Request<JsonResult<List<RecommendTopicsItemInfo>>> request = new GsonRequest<JsonResult<List<RecommendTopicsItemInfo>>>
                (Request.Method.POST, url_recommend_topics,
                        successListener, errorListener, new TypeToken<JsonResult<List<RecommendTopicsItemInfo>>>() {
                }.getType()) {
            @Override//填写参数
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);//安卓
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    public void listData(RecommendListBean result) {
        android.util.Log.d(TAG, "setData: " + result.getData());
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {//当前页
            this.list.clear(); //清除数据
            this.topList.clear();
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        if (result.getData().size() > 0) {//刷新后进来
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0) {
            //第一次进来
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
            //删除第1,2,3名游戏
            setHeaderInfo(list);//设置头部布局
            //删除第1,2,3名游戏
            list.remove(0);
            list.remove(0);
        }
        if (adapter == null) {
            adapter = new RecommendListAdapter(context, getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //设置下位
       /* if ((mStickyLV.size() == 0 && pageAction.getTotal() == 0) || mStickyLV.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(true);
        } else {
            pullListView.setPullLoadEnabled(true);
        }*/
        //设置上拉刷新后停留的地方  // TODO: 2017/7/17 0017
        android.util.Log.d(TAG, "mStickyLV.size(): " + list.size() + ",pageAction.getTotal():" + pageAction.getTotal());

        if (0 == pageAction.getCurrentPage() && result.getData().size() <= 2) {
            //pullListView.setScrollLoadEnabled(false);
            //pullListView.setPullRefreshEnabled(false);
            //pullListView.setPullLoadEnabled(false);
            pullListView.getRefreshableView().setSelection(0);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 2) {//// TODO: 2017/7/17 0017
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    public void initListView(final View view) {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
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
                    getGameList();//竖着的,游戏位
                    getHorizontalData();//横着 精选位游戏
                }
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
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
                    getGameList();
                } else {
                    ToastUtil.show(context, getString(R.string.no_more_data));
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        refreshableView = pullListView.getRefreshableView();
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    int i = position - 1;
                    /**   pullListView的头部position是0  第一个item 索引是 1
                     1-1= 0(所以position是1时,要拿list里的0处数据, position是2时,拿1处数据)   */
                    RecommendListBean.DataBean dataBean = list.get(i);
                    //埋点
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KeyConstant.index, i + 2 + "");
                    map.put(KeyConstant.game_Name, dataBean.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map);

                    Intent intent = new Intent(context, GameDetailActivity.class);
                    intent.putExtra(KeyConstant.ID, dataBean.getGameId());
                    startActivity(intent);
                }
            }
        });
        //滑动事件(搜索栏渐变)
        refreshableView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                }

            }

            //向下头部颜色渐变
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //todo添加头布局
        View headView = View.inflate(getActivity(), R.layout.recommend_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        //第一次进来,请求数据
        getGameList();
        getHorizontalData();
    }

    public void scroll2Top() {
        android.util.Log.d(TAG, "是否在显示: " + mIsShow);
        if (mIsShow && pullListView != null) {
            ListView refreshableView = pullListView.getRefreshableView();
            //refreshableView.setSelectionAfterHeaderView();
            refreshableView.setSelection(0);
            //getGameList();
        }
    }

    //顶部2个位置
    private void initHeadView(View view) {
        from_img_1 = (SimpleDraweeView) view.findViewById(R.id.img_from_1);//来自 头像
        from_img_2 = (SimpleDraweeView) view.findViewById(R.id.img_from_2);

        from_1 = (TextView) view.findViewById(R.id.text_from_1);//来自 名字
        from_2 = (TextView) view.findViewById(R.id.text_from_2);

        gamename_1 = (TextView) view.findViewById(R.id.tv_gamename_1);//游戏名字
        gamename_2 = (TextView) view.findViewById(R.id.tv_gamename_2);

        game_big_pic_1 = (SimpleDraweeView) view.findViewById(R.id.recommend_game_pic_1);//游戏图片
        game_big_pic_2 = (SimpleDraweeView) view.findViewById(R.id.recommend_game_pic_2);

        summary_1 = (TextView) view.findViewById(R.id.tv_summary1);//游戏摘要
        summary_2 = (TextView) view.findViewById(R.id.tv_summary2);

        view.findViewById(R.id.recommend_head_llay_0).setOnClickListener(headClickListener);
        view.findViewById(R.id.recommend_head_llay_1).setOnClickListener(headClickListener);
        view.findViewById(R.id.recommend_topics_more_tv).setOnClickListener(headClickListener);

        //横向滑动控件
        horizontalViewContainer = (LinearLayout) view.findViewById(R.id.horizontalView_container);
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
        wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    //头部点击
    private View.OnClickListener headClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, GameDetailActivity.class);
            switch (v.getId()) {
                case R.id.recommend_head_llay_0:
                    RecommendListBean.DataBean dataBean = topList.get(0);
                    //埋点
                    HashMap<String, String> map = new HashMap<>();
                    map.put(KeyConstant.index, 0 + "");
                    map.put(KeyConstant.game_Name, dataBean.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map);

                    intent.putExtra(KeyConstant.ID, dataBean.getGameId());
                    startActivity(intent);
                    break;
                case R.id.recommend_head_llay_1:
                    RecommendListBean.DataBean dataBean1 = topList.get(1);
                    //埋点
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put(KeyConstant.index, 1 + "");
                    map1.put(KeyConstant.game_Name, dataBean1.getGameName());
                    MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map1);
                    intent.putExtra(KeyConstant.ID, dataBean1.getGameId());
                    startActivity(intent);
                    break;
                case R.id.recommend_topics_more_tv://专题
                    startActivity(new Intent(context, TopicsListActivity.class));
                    break;
            }
        }
    };

    //设置头部数据
    public void setHeaderInfo(List<RecommendListBean.DataBean> list) {
        RecommendListBean.DataBean dataBean0 = list.get(0);
        RecommendListBean.DataBean dataBean1 = list.get(1);

        from_img_1.setImageURI(dataBean0.getGameLogo());//来自...头像
        from_img_2.setImageURI(dataBean1.getGameLogo());//来自...头像

        game_big_pic_1.setImageURI(dataBean0.getGameRecommendImg());
        game_big_pic_2.setImageURI(dataBean1.getGameRecommendImg());
        gamename_1.setText(dataBean0.getGameName());
        gamename_2.setText(dataBean1.getGameName());

        from_1.setText("来自" + dataBean0.getRecommender());
        from_2.setText("来自" + dataBean1.getRecommender());

        summary_1.setText(dataBean0.getRecommend());
        summary_2.setText(dataBean1.getRecommend());
    }

    @Override
    protected void onFirstUserVisible() {
        Log.d(TAG, "onUserVisible:当前 " + pageAction.getCurrentPage());
    }

    @Override
    protected void onUserVisible() {
        Log.d(TAG, "onUserVisible:当前 " + pageAction.getCurrentPage());
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }

    public void setShow(boolean isShow) {
        mIsShow = isShow;
    }

}
