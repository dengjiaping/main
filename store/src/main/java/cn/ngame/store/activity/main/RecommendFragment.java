package cn.ngame.store.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hubcloud.adhubsdk.NativeAd;
import com.hubcloud.adhubsdk.NativeAdListener;
import com.hubcloud.adhubsdk.NativeAdResponse;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdEventListener;
import com.hubcloud.adhubsdk.internal.nativead.NativeAdUtil;
import com.hubcloud.adhubsdk.internal.network.ServerResponse;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;
import com.jzt.hol.android.jkda.sdk.services.recommend.RecommendClient;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.RecommendListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 精选
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class RecommendFragment extends BaseSearchFragment {
    public static final String TAG = RecommendFragment.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private ImageView game_big_pic_1, game_big_pic_2;
    private SimpleDraweeView from_img_1, from_img_ad, from_img_2;
    private TextView gamename_1, gamename_2, summary_2, summary_ad, title_ad;
    private TextView from_1, from_2, summary_1;
    private LoadStateView loadStateView;
    private RecommendListAdapter adapter;
    private PageAction pageAction;
    public static int PAGE_SIZE = 8;
    List<RecommendListBean.DataBean> topList = new ArrayList<>();
    List<RecommendListBean.DataBean> list = new ArrayList<>();
    private LinearLayout horizontalViewContainer;
    private FragmentActivity context;
    private Intent singeTopicsDetailIntent = new Intent();
    private LinearLayout.LayoutParams hParams;
    private int wrapContent;
    private SimpleDraweeView simpleImageView;
    private boolean mIsShow = false;
    private ListView refreshableView;
    private Picasso picasso;
    private LinearLayout adContainer;
    private LinearLayout adLayout;
    private LinearLayout parent;
    private LinearLayout ad_container;
    private ImageView adIv;

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
        picasso = Picasso.with(context);
        initListView(view);     //初始化
    }

    private void getGameList() {
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        RecommendListBody bodyBean = new RecommendListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new RecommendClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<RecommendListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        if (list != null && list.size() > 0) {
                            loadStateView.setVisibility(View.GONE);
                            ToastUtil.show(context, getString(R.string.server_exception_2_pullrefresh));
                        } else {
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.server_exception_2_pullrefresh));
                            loadStateView.setVisibility(View.VISIBLE);
                        }
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                    }

                    @Override
                    public void onNext(RecommendListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            loadStateView.setVisibility(View.GONE);
                            pullListView.onPullUpRefreshComplete();
                            pullListView.onPullDownRefreshComplete();
                        }
                    }
                });
    }


    private List<YunduanBean.DataBean> gameInfo;

    private void getHorizontalData() {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        new YunduanClient(context, bodyBean).observable()
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            gameInfo = result.getData();
                            if (null == gameInfo || gameInfo.size() == 0) {
                                Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                            } else {
                                horizontalViewContainer.removeAllViews();
                                int dp10 = CommonUtil.dip2px(context, 10);
                                int width240 = CommonUtil.dip2px(context, 240f);
                                int heght114 = CommonUtil.dip2px(context, 114f);
                                int size = gameInfo.size();
                                for (int i = 0; i < size; i++) {
                                    final YunduanBean.DataBean info = gameInfo.get(i);
                                    final String gameImage = info.getLogoUrl();//获取每一张图片
                                    simpleImageView = new SimpleDraweeView(context);
                                    GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder
                                            .newInstance(getResources())
                                            .setActualImageScaleType(ScalingUtils.ScaleType.CENTER)
                                            .setRoundingParams(RoundingParams.fromCornersRadius(16))
                                            .setFadeDuration(0)
                                            .build();
                                    simpleImageView.setHierarchy(hierarchy);
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
                                    simpleImageView.setLayoutParams(hParams);
                                    //加载网络图片
                                    simpleImageView.setImageURI(gameImage);
                                    simpleImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            singeTopicsDetailIntent.putExtra(KeyConstant.category_Id, info.getId());
                                            singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, info.getTypeName());
                                            singeTopicsDetailIntent.putExtra(KeyConstant.DESC, info.getTypeDesc());
                                            singeTopicsDetailIntent.putExtra(KeyConstant.URL, gameImage);
                                            startActivity(singeTopicsDetailIntent);
                                        }
                                    });
                                    horizontalViewContainer.addView(simpleImageView, i);
                                }
                            }
                        } else {
                        }
                    }
                });
        //GameInfo类  : 请求游戏JN数据后,封装的javabean

    }

    public void listData(RecommendListBean result) {
        loadStateView.setVisibility(View.GONE);
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {//当前页
            if (list != null) {
                this.list.clear(); //清除数据
                this.topList.clear();
            }
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        List<RecommendListBean.DataBean> resultData = result.getData();
        int totals = result.getTotals();
        if (result.getData().size() > 0) {//刷新后进来
            pageAction.setTotal(totals);
            if (list != null) {
                this.list.addAll(resultData);
                this.topList.addAll(resultData);
            }
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0 && list != null) {
            //第一次进来
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(totals);
            this.list.addAll(resultData); //清除数据
            this.topList.addAll(resultData);
            if (list.size() > 1) {
                setHeaderInfo(list);//设置头部布局
                list.remove(0);
                list.remove(0);
            }
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
        loadStateView = (LoadStateView) view.findViewById(R.id.load_state_view2);
        loadStateView.isShowLoadBut(false);
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        pullListView.setPullLoadEnabled(true);
        pullListView.setPullRefreshEnabled(true);
        pullListView.setScrollLoadEnabled(true);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadStateView.setVisibility(View.GONE);
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                if (!NetUtil.isNetworkConnected(context)) {
                    pullListView.onPullUpRefreshComplete();
                    pullListView.onPullDownRefreshComplete();
                    if (0 == pageAction.getCurrentPage()) {
                        pullListView.getRefreshableView().setSelection(0);
                    }
                    if (list != null && list.size() > 0) {
                        ToastUtil.show(context, getString(R.string.no_network));
                    } else {
                        ToastUtil.show(context, getString(R.string.no_network));
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_network));
                    }
                } else {
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
     /*   //滑动事件(搜索栏渐变)
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
        });*/

        //todo添加头布局
        View headView = View.inflate(context, R.layout.recommend_header_view, null);
        initHeadView(headView);
        //头布局放入listView中
        if (refreshableView.getHeaderViewsCount() == 0) {
            refreshableView.addHeaderView(headView);
        }
        //第一次进来,请求数据
        if (!NetUtil.isNetworkConnected(context)) {
            pullListView.onPullUpRefreshComplete();
            pullListView.onPullDownRefreshComplete();
            if (0 == pageAction.getCurrentPage()) {
                pullListView.getRefreshableView().setSelection(0);
            }
            if (list != null && list.size() > 0) {
                ToastUtil.show(context, getString(R.string.no_network));
            } else {
                loadStateView.setVisibility(View.VISIBLE);
                loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_network));
            }
        } else {
            getGameList();
            getHorizontalData();
        }
    }

    public void scroll2Top() {
        if (mIsShow && pullListView != null) {
            ListView refreshableView = pullListView.getRefreshableView();
            //refreshableView.setSelectionAfterHeaderView();
            int firstVisiblePosition = refreshableView.getFirstVisiblePosition();
            View childAt0 = refreshableView.getChildAt(0);
            if (null == childAt0) {
                return;
            }
            int top = childAt0.getTop();
            if (firstVisiblePosition == 0 && top == 0) {
                getGameList();
            } else {
                refreshableView.setSelection(0);
            }
        }
    }

    //顶部2个位置
    private void initHeadView(View view) {

        //================广告 开始======================
        adLayout = (LinearLayout) view.findViewById(R.id.ad_layout);
        //广告位
        adIv = (ImageView) view.findViewById(R.id.recommend_game_pic_ad);
        title_ad = (TextView) view.findViewById(R.id.tv_gamename_ad);
        summary_ad = (TextView) view.findViewById(R.id.tv_summary_ad);//游戏摘要
        from_img_ad = (SimpleDraweeView) view.findViewById(R.id.img_from_ad);
        fetchAd();
        //================广告 结束======================

        from_img_1 = (SimpleDraweeView) view.findViewById(R.id.img_from_1);//来自 头像
        from_img_2 = (SimpleDraweeView) view.findViewById(R.id.img_from_2);

        from_1 = (TextView) view.findViewById(R.id.text_from_1);//来自 名字
        from_2 = (TextView) view.findViewById(R.id.text_from_2);

        gamename_1 = (TextView) view.findViewById(R.id.tv_gamename_1);//游戏名字
        gamename_2 = (TextView) view.findViewById(R.id.tv_gamename_2);


        game_big_pic_1 = (ImageView) view.findViewById(R.id.recommend_game_pic_1);//游戏图片
        game_big_pic_2 = (ImageView) view.findViewById(R.id.recommend_game_pic_2);

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

    private void fetchAd() {
        final NativeAd nativeAd = new NativeAd(context, Constant.AdHub_AD_NATIVE_ID, new NativeAdListener() {

            @Override
            public void onAdFailed(int errorcode) {
                android.util.Log.d(TAG, "获取广告失败:onAdFailed: " + errorcode);
            }


            @Override
            public void onAdLoaded(NativeAdResponse response) {
                adLayout.setVisibility(View.VISIBLE);
                // 一个广告只允许展现一次，多次展现、点击只会计入一次
                // demo仅简单地显示一条。可将返回的多条广告保存起来备用。
                // updateView(response);
                //返回设置的广告的多个图片的URL，SDK并未处理加载urls里面的图片，需要集成者自己去加载展示
                if (response == null) {
                    return;
                }
                ArrayList<String> imageUrls = response.getImageUrls();
                if (imageUrls != null && imageUrls.size() > 0) {
                    picasso.load(imageUrls.get(0)).placeholder(R.drawable.ic_def_logo_720_288)
                            .error(R.drawable.ic_def_logo_720_288)
                            // .resize(screenWidth,150)
                            .into(adIv);
                }
                //返回设置的广告的多个视频流的URL，SDK并未处理加载urls里面的视频，需要集成者自己去加载展示`
                // ArrayList<String> vedioUrls = response.getVedioUrls();
                //广告====标题
                String title = response.getHeadline();
                title_ad.setText(title == null ? "ADHUB" : title);
                //广告描述===返回设置的广告的多个文本信息
                ArrayList<String> texts = response.getTexts();
                if (texts != null && texts.size() > 0) {
                    summary_ad.setText(texts.get(0));
                } else {
                    summary_ad.setText("Adhub助力广告技术公司构建核心优势。");
                }
                //根据广告法新规定，必须加入广告标识和广告来源。此处返回广告字样及广告来源标识图片。需要开发者分别放置于广告左下和右下角
                //sdk内部提供了NativeAdUtil.addADLogo（View v，NativeAdResponse
                // response）方法，可以将一个view加上logo并返回一个加入了logo的FrameLayout替代原本无logo的view;
                //注意若传入此方法的view之前已经有父view，调用了此方法之后原来的view会从父view中移除，须将方法返回的framelayout加入之前view的父view之中。
                //若此方法不满足要求，请开发者自己实现加入logo及广告字样。具体请参考本样例及样例效果
                // ServerResponse.AdLogoInfo结构
                // public static class AdLogoInfo {
                //    public static int TYPE_PIC = 0;
                //    public static int TYPE_TEXT = 1;
                //    String adurl;
                //    int type = 0;
                //    }
                //其中属性type为广告表示的类型共2种：图片和文字，如果type==TYPE_PIC则属性adurl是图片的url
                //如果是type==TYPE_TEXT则属性adurl是文字字符串
                //广告字样
                ServerResponse.AdLogoInfo adUrl = response.getAdUrl();//广告2个字的那个图
                //广告来源标识
                ServerResponse.AdLogoInfo adLogoInfo = response.getlogoUrl();
                if (adLogoInfo != null) {
                    from_img_ad.setImageURI(adLogoInfo.getAdurl());//来自...头像
                }
                //注册原生广告展示及点击曝光，必须调用。
                NativeAdUtil.registerTracking(response, adLayout, new NativeAdEventListener() {
                    @Override
                    public void onAdWasClicked() {
                    }

                    @Override
                    public void onAdWillLeaveApplication() {
                    }
                });
            }
        });
        //此方法已过时，请不要使用。SDK不再为接入者下载资源图片
        //nativeAd.shouldLoadIcon(true);
        //此方法已过时，请不要使用。SDK不再为接入者下载资源图片
        //nativeAd.shouldLoadImage(true);
        nativeAd.loadAd();

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
                    map1.put(KeyConstant.index, 2 + "");
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

        if (null == dataBean0) {
            return;
        }

        from_img_1.setImageURI(dataBean0.getGameLogo());//来自...头像
        picasso.load(dataBean0.getGameRecommendImg()).placeholder(R.drawable.ic_def_logo_720_288)
                .error(R.drawable.ic_def_logo_720_288)
                // .resize(screenWidth,150)
                .into(game_big_pic_1);
        gamename_1.setText(dataBean0.getGameName());
        from_1.setText("来自" + dataBean0.getRecommender());
        summary_1.setText(dataBean0.getRecommend());

        if (null == dataBean1) {
            return;
        }
        picasso.load(dataBean1.getGameRecommendImg()).placeholder(R.drawable.ic_def_logo_720_288)
                .error(R.drawable.ic_def_logo_720_288)
                // .resize(screenWidth,150)
                .into(game_big_pic_2);
        from_img_2.setImageURI(dataBean1.getGameLogo());//来自...头像

        gamename_2.setText(dataBean1.getGameName());
        from_2.setText("来自" + dataBean1.getRecommender());
        summary_2.setText(dataBean1.getRecommend());

    }

    /**
     * 广告
     */
  /*  private void setAdView() {
        InMobiSdk.init(context, Constant.InMobiSdk_Id);

        InMobiNative nativeAd = new InMobiNative(context, Constant.AD_PlacementID_RecommendFragment, new InMobiNative
                .NativeAdListener() {
            @Override
            public void onAdLoadSucceeded(@NonNull InMobiNative inMobiNative) {
                adLayout.setVisibility(View.VISIBLE);
                //JSONObject content = inMobiNative.getCustomAdContent();
                NewsSnippet item = new NewsSnippet();
                item.title = inMobiNative.getAdTitle();
                title_ad.setText(item.title);//广告标题

                item.imageUrl = inMobiNative.getAdIconUrl();
                from_img_ad.setImageURI(item.imageUrl);

                item.description = inMobiNative.getAdDescription();
                summary_ad.setText(item.description);

                item.inMobiNative = new WeakReference<>(inMobiNative);
                //item.view =inMobiNative.getPrimaryViewOfWidth(mAdapter.,viewGroup,0);
                adContainer.removeAllViews();
                adContainer.addView(inMobiNative.getPrimaryViewOfWidth(adContainer, adContainer,
                        adContainer.getWidth()));
            }

            @Override
            public void onAdLoadFailed(@NonNull InMobiNative inMobiNative, @NonNull InMobiAdRequestStatus inMobiAdRequestStatus) {
                Log.d(TAG, "广告加载失败");
                adLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAdFullScreenDismissed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDismissed");
            }

            @Override
            public void onAdFullScreenWillDisplay(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenWillDisplay");
            }

            @Override
            public void onAdFullScreenDisplayed(InMobiNative inMobiNative) {
                Log.d(TAG, "onAdFullScreenDisplayed");
            }

            @Override
            public void onUserWillLeaveApplication(InMobiNative inMobiNative) {
                Log.d(TAG, "onUserWillLeaveApplication");
            }

            @Override
            public void onAdImpressed(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdImpressed");
            }

            @Override
            public void onAdClicked(@NonNull InMobiNative inMobiNative) {
                if (inMobiNative == null) {
                    return;
                }
                //广告埋点
                HashMap<String, String> map = new HashMap<>();
                map.put(KeyConstant.index, 1 + "");
                map.put(KeyConstant.game_Name, inMobiNative.getAdTitle());
                MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendPositionClickCount, map);
            }

            @Override
            public void onMediaPlaybackComplete(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onMediaPlaybackComplete");
            }

            @Override
            public void onAdStatusChanged(@NonNull InMobiNative inMobiNative) {
                Log.d(TAG, "onAdStatusChanged");
            }
        });

        Map<String, String> map = new HashMap<>();
        nativeAd.setExtras(map);
        nativeAd.setDownloaderEnabled(true);
        nativeAd.load();
    }*/
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
