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
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameListClient;
import com.jzt.hol.android.jkda.sdk.services.main.HomeHotRaiderClient;

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
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.QueryHomeGameBean;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.gamehub.view.ShowViewActivity;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.core.utils.Constant.URL_GAME_DETAIL;

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
    private SimpleDraweeView sdv_img_1, sdv_img_2, game_pic_1, game_pic_2;
    private TextView gamename_1, gamename_2, summary_2;
    private TextView from_1, from_2, summary_1;

    private IFileLoad fileLoad; //文件下载公共类接口

    private Handler handler = new Handler();
    private RecommendListAdapter adapter;

    private PageAction pageAction;
    public static int PAGE_SIZE = 5;
    List<GameRankListBean.DataBean> topList = new ArrayList<>();
    List<GameRankListBean.DataBean> list = new ArrayList<>();
    private LinearLayout horizontalViewContainer;
    private GameInfo gameInfo;
    private FragmentActivity context;

    public static RecommendFragment newInstance(int arg) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", arg);
        Log.d(TAG, "初始化newInstance");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        Log.d(TAG, "推荐getContentViewLayoutID");
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initViewsAndEvents(View view) {
//        typeValue = getArguments().getInt("", 1);
        Log.d(TAG, "推荐initViewsAndEvents");
        context = getActivity();
        initListView(view);     //初始化
        //getBannerData();    //轮播图片
    }

    private void getGameList() {
        GameListBody bodyBean = new GameListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameListClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(GameRankListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
        getHorizontalData();
    }

    private ArrayList<String> imgUrlList = new ArrayList<String>();

    private void getHorizontalData() {
        String url_game_detail = Constant.WEB_SITE + URL_GAME_DETAIL;
        //请求失败
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };
        //todo 获取横向滑动的数据
        Response.Listener<JsonResult<GameInfo>> successListener = new Response.Listener<JsonResult<GameInfo>>() {
            @Override
            public void onResponse(JsonResult<GameInfo> result) {
                //数据为空
                if (result == null || result.code != 0) {
                    return;
                }
                //GameInfo类  : 请求游戏JSON数据后,封装的javabean
                gameInfo = result.data;
                if (null == gameInfo) {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                } else {
                    List<GameImage> gameImgsList = gameInfo.gameDetailsImages;
                    if (null != gameImgsList && gameImgsList.size() > 0) {
                        horizontalViewContainer.removeAllViews();
                        imgUrlList.clear();
                        for (int i = 0; i < gameImgsList.size(); i++) {
                            GameImage gameImage = gameImgsList.get(i);//获取每一张图片
                            PicassoImageView imageView = new PicassoImageView(getActivity());
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            //为  PicassoImageView设置属性
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.width = CommonUtil.dip2px(context, 240f);
                            params.height = CommonUtil.dip2px(context, 114f);
                            //有多个图片的话
                            if (i > 0) {
                                params.setMargins(CommonUtil.dip2px(context, 10), 0, 0, 0);
                            }
                            imageView.setLayoutParams(params);

                            //加载网络图片
                            imageView.setImageUrl(gameImage.imageLink, 240f, 114f, R.drawable.default_game);
                            horizontalViewContainer.addView(imageView);
                            imgUrlList.add(gameImage.imageLink);
                        }
                    }
                }

                horizontalViewContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gv = new Intent();
                        gv.setClass(context, ShowViewActivity.class);
                        gv.putStringArrayListExtra("viewImages", imgUrlList);
                        gv.putExtra("selectPosition", 0);
                        startActivity(gv);
                    }
                });

            }
        };

        //------------------------------------------------------------------------------
        Request<JsonResult<GameInfo>> request = new GsonRequest<JsonResult<GameInfo>>(Request.Method.POST, url_game_detail,
                successListener, errorListener, new TypeToken<JsonResult<GameInfo>>() {
        }.getType()) {
            @Override//填写参数
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gameId", 638 + "");//146.638
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    public void listData(GameRankListBean result) {
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
        android.util.Log.d(TAG, "lpageAction.getCurrentPage()  " + pageAction.getCurrentPage());
        android.util.Log.d(TAG, "result.getData().size()  " + result.getData().size());
        android.util.Log.d(TAG, "result.getTotals()  " + result.getTotals());
        if (result.getData().size() > 0) {//刷新后进来
            android.util.Log.d(TAG, "进来  size> 0 ");
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0) {
            //第一次进来
            android.util.Log.d(TAG, "进来size>0   pageAction.getCurrentPage() == 0 ");
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
            adapter = new RecommendListAdapter(getActivity(), getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        //设置下位列表
        if ((list.size() == 0 && pageAction.getTotal() == 0) || list.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(false);
        } else {
            pullListView.setPullLoadEnabled(true);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 0) { //设置上拉刷新后停留的地方
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
        //tv_top_bar_bg = (TextView) view.findViewById(R.id.tv_top_bar_bg);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                Log.d(TAG, "下拉: ");
                getGameList();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                Log.d(TAG, "上拉: ");
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ?
                            pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    getGameList();
//                    getCommentList();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        pullListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {//减去头部,不让点击
                    Intent intent = new Intent(context, GameDetailActivity.class);
                    intent.putExtra(KeyConstant.ID, list.get(position - 1).getId());//// TODO: 2017/7/15 0015
                    startActivity(intent);
                }
            }
        });
        //滑动事件(搜索栏渐变)
        pullListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //向下头部颜色渐变
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
               /* if (firstVisibleItem == 0 && pullListView.getRefreshableView().getChildAt(0) != null && pullListView
               .getRefreshableView().getChildAt(0).getTop() != 0) {
                    float total_height = ll_everyday.getTop() * 3;
                    int viewScrollHeigh = Math.abs(pullListView.getRefreshableView().getChildAt(0).getTop());
                    if (viewScrollHeigh < total_height) {
                        float alpha = (total_height - viewScrollHeigh) / total_height;
                        tv_top_bar_bg.setAlpha(1 - alpha);
                        tv_top_bar_bg.setBackgroundColor(1 - alpha > 0 ? getResources().getColor(R.color.colorPrimary) :
                        getResources().getColor(R.color.white));
                    } else {
                        tv_top_bar_bg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        tv_top_bar_bg.setAlpha(0.9f);
                    }
                } else {
                    if (firstVisibleItem != 0) {
                        tv_top_bar_bg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        tv_top_bar_bg.setAlpha(0.9f);
                    } else {
                        tv_top_bar_bg.setBackgroundColor(getResources().getColor(R.color.white));
                        tv_top_bar_bg.setAlpha(0.2f);
                    }
                }*/
            }
        });

        //todo添加头布局
        View headView = View.inflate(getActivity(), R.layout.recommend_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
        getGameList();
    }

    private void initHeadView(View view) {
        sdv_img_1 = (SimpleDraweeView) view.findViewById(R.id.img_from_1);//来自 头像
        sdv_img_2 = (SimpleDraweeView) view.findViewById(R.id.img_from_2);

        from_1 = (TextView) view.findViewById(R.id.text_from_1);//来自 名字
        from_2 = (TextView) view.findViewById(R.id.text_from_2);

        gamename_1 = (TextView) view.findViewById(R.id.tv_gamename_1);//游戏名字
        gamename_2 = (TextView) view.findViewById(R.id.tv_gamename_2);

        game_pic_1 = (SimpleDraweeView) view.findViewById(R.id.recommend_game_pic_1);//游戏图片
        game_pic_2 = (SimpleDraweeView) view.findViewById(R.id.recommend_game_pic_2);

        summary_1 = (TextView) view.findViewById(R.id.tv_summary1);//游戏摘要
        summary_2 = (TextView) view.findViewById(R.id.tv_summary2);

        view.findViewById(R.id.recommend_head_llay_0).setOnClickListener(headClickListener);
        view.findViewById(R.id.recommend_head_llay_1).setOnClickListener(headClickListener);

        //横向滑动控件
        horizontalViewContainer = (LinearLayout) view.findViewById(R.id.horizontalView_container);
    }

    //头部点击
    private View.OnClickListener headClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recommend_head_llay_0:
                    Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                    intent.putExtra(KeyConstant.ID, topList.get(0).getId());
                    startActivity(intent);
                    break;
                case R.id.recommend_head_llay_1:
                    Intent i2 = new Intent(getActivity(), GameDetailActivity.class);
                    i2.putExtra(KeyConstant.ID, topList.get(1).getId());
                    startActivity(i2);
                    break;
            }
        }
    };

    //设置头部数据
    public void setHeaderInfo(List<GameRankListBean.DataBean> list) {
        GameRankListBean.DataBean dataBean0 = list.get(0);
        GameRankListBean.DataBean dataBean1 = list.get(1);

        sdv_img_1.setImageURI(dataBean0.getGameLogo());
        sdv_img_2.setImageURI(dataBean1.getGameLogo());

        game_pic_1.setImageURI(dataBean0.getGameLogo());
        game_pic_2.setImageURI(dataBean1.getGameLogo());
        gamename_1.setText(dataBean0.getGameName());
        gamename_2.setText(dataBean1.getGameName());

        from_1.setText("来自" + dataBean0.getGameName());
        from_2.setText("来自" + dataBean1.getGameName());

        summary_1.setText(dataBean0.getGameDesc());
        summary_2.setText(dataBean1.getGameDesc());
    }

    //每日精选、MOBA精选、枪战精选、新平尝鲜、品牌游戏列表（12个）
    private void queryHomeGame() {
        String url = Constant.WEB_SITE + Constant.URL_HOME_GAME_SELECTION;
        Response.Listener<JsonResult<QueryHomeGameBean>> successListener = new Response.Listener<JsonResult<QueryHomeGameBean>>
                () {
            @Override
            public void onResponse(JsonResult<QueryHomeGameBean> result) {

                if (result == null || result.code != 0) {
                    //  "加载数据失败"
                    return;
                }

                QueryHomeGameBean hotInfo = result.data;
                if (hotInfo != null) {
                    setHomeAllGame(hotInfo);
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
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

        Request<JsonResult<QueryHomeGameBean>> request = new GsonRequest<JsonResult<QueryHomeGameBean>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<QueryHomeGameBean>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("appTypeId", String.valueOf(0));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    //设置首页game数据
    private void setHomeAllGame(QueryHomeGameBean result) {
        // 品牌游戏 剩余（10个）
        allBradnList.clear();
        if (allBradnList.size() > 0) {
            gameListAdapter = new SelectGameAdapter(getActivity(), getSupportFragmentManager());
            listView_allBarnd.setAdapter(gameListAdapter);
            gameListAdapter.setDate(allBradnList);
            gameListAdapter.notifyDataSetChanged();
            StringUtil.setListViewHeightBasedOnChildren(listView_allBarnd, 0);
        }
    }

    //请求 热门攻略
    private void doHotRaider() {
        BrowseHistoryBodyBean bodyBean = new BrowseHistoryBodyBean();
        bodyBean.setType(1);
        new HomeHotRaiderClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameHubMainBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(GameHubMainBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (result.getData() == null) {
                                pullListView.getRefreshableView().setAdapter(raiderAdapter); //数据位空时，加载头部
                                return;
                            }
                            if (result.getData().size() <= 0) {
                                pullListView.getRefreshableView().setAdapter(raiderAdapter); //数据位空时，加载头部
                                return;
                            }
                            homeList.clear();
                            homeList.addAll(result.getData());
                            raiderAdapter = new MainHomeRaiderAdapter(getActivity(), homeList);
                            pullListView.getRefreshableView().setAdapter(raiderAdapter);
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        android.util.Log.d(TAG, "onHiddenChanged: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
