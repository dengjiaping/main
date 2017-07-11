package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.HomeHotRaiderClient;
import com.jzt.hol.android.jkda.sdk.services.main.XuniClient;
import com.jzt.hol.android.jkda.sdk.services.main.YunduanClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.adapter.HomeGameTypeListAdapter;
import cn.ngame.store.adapter.MainHomeRaiderAdapter;
import cn.ngame.store.adapter.ProgressBarStateListener;
import cn.ngame.store.adapter.SelectGameAdapter;
import cn.ngame.store.adapter.TencentGridAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.QueryHomeGameBean;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.game.view.GameDetail2Activity;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.game.view.GameListActivity;
import cn.ngame.store.gamehub.view.MsgDetailActivity;
import cn.ngame.store.gamehub.view.StrategyActivity;
import cn.ngame.store.push.model.IPushMessageModel;
import cn.ngame.store.push.model.PushMessageModel;
import cn.ngame.store.push.view.PushMessageActivity;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 精选
 * Created by gp on 2017/3/14 0014.
 */

public class SelectedFragment extends BaseSearchFragment implements View.OnClickListener {
    public static final String TAG = SelectedFragment.class.getSimpleName();
    private static final int TYPE_HAND = 21; // 9
    private static final int TYPE_XUNI = 11;
    PullToRefreshListView pullListView;
    /**
     * 顶部栏搜索-消息
     */
    private TextView tv_top_bar_bg;
    private TextView tv_toSearch;
    private FrameLayout fl_notifi;
    private TextView tv_notifi_num;
    /**
     * banner
     */
    private BannerView banner_view;
    /**
     * 专题精选-云端适配-原生手柄-虚拟现实
     */
    private LinearLayout ll_zhuanti, ll_yunduan, ll_yuansheng, ll_xuni;
    private ImageView iv_zhuanti, iv_yunduan, iv_yuansheng, iv_xuni;
    private TextView tv_zhuanti, tv_yunduan, tv_yuansheng, tv_xuni;
    /**
     * 每日精选
     */
    private LinearLayout ll_everyday;
    private ImageView iv_recommend;
    private TextView tv_title, tv_memory, tv_download_num, tv_content;
    private RatingBar rating_bar;
    private GameLoadProgressBar day_progress_bar;
    private Timer timer = new Timer();
    private static Handler uiHandler = new Handler();
    /**
     * MOBA精选、枪战精选、新品尝鲜
     */
    private GridView gridView_bzrm, gridView_rqwy, gridView_yxjx;
    /**
     * gridView
     */
    private GridView gridView_tencent;
    /**
     * 热门攻略
     */
    private RelativeLayout rl_hot_more;

    private IFileLoad fileLoad; //文件下载公共类接口
    /**
     * list集合
     */
    List<YunduanBean.DataBean> yunduanList = new ArrayList<>();
    List<YunduanBean.DataBean> xuniList = new ArrayList<>();
    List<YunduanBean.DataBean> tencentList = new ArrayList<>();
    List<GameInfo> dailyRecommendList = new ArrayList<>();
    List<GameInfo> mobaList = new ArrayList<>();
    List<GameInfo> qiangzhanList = new ArrayList<>();
    List<GameInfo> xinpingList = new ArrayList<>();

    List<GameInfo> bTencentList = new ArrayList<>();
    List<GameInfo> allBradnList = new ArrayList<>();
    List<GameHubMainBean.DataBean> homeList = new ArrayList<>();

    private ListView listView_tBarnd, listView_allBarnd;

    private Handler handler = new Handler();
    private HomeGameTypeListAdapter typeListAdapter;
    private SelectGameAdapter gameListAdapter;
    private MainHomeRaiderAdapter raiderAdapter;

    public static SelectedFragment newInstance(int arg) {
        SelectedFragment fragment = new SelectedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.selected_fragment;
    }

    @Override
    protected void initViewsAndEvents(View view) {
//        typeValue = getArguments().getInt("", 1);
        initListView(view);     //初始化
        getBannerData();    //轮播图片
        queryHomeGame(); //每日精选、MOBA精选、枪战精选、新平尝鲜、品牌游戏列表（12个）
        doYunduanInfo(TYPE_HAND); //请求 云端适配-原生手柄-虚拟现实
        doXuniInfo();
        doYunduanInfo(TYPE_XUNI); //请求 腾讯、网易..游戏
        doHotRaider(); //请求 热门攻略
    }

    public void initListView(final View view) {
        tv_top_bar_bg = (TextView) view.findViewById(R.id.tv_top_bar_bg);
        tv_toSearch = (TextView) view.findViewById(R.id.tv_toSearch);
        fl_notifi = (FrameLayout) view.findViewById(R.id.fl_notifi);
        tv_notifi_num = (TextView) view.findViewById(R.id.tv_notifi_num); //右上角消息数目
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        tv_toSearch.setOnClickListener(this);
        fl_notifi.setOnClickListener(this);
        pullListView.setPullLoadEnabled(false); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                initViewsAndEvents(view);
                getBannerData();    //轮播图片
                queryHomeGame(); //每日精选、MOBA精选、枪战精选、新平尝鲜、品牌游戏列表（12个）
                doYunduanInfo(TYPE_HAND); //请求 云端适配-原生手柄-虚拟现实
                doXuniInfo();
                doYunduanInfo(TYPE_XUNI); //请求 腾讯、网易..游戏
                doHotRaider(); //请求 热门攻略
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        //点击事件
        pullListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getActivity(), MsgDetailActivity.class);
                i.putExtra("msgId", homeList.get(position - 1).getId());
                startActivity(i);
            }
        });
        //滑动事件(搜索栏渐变)
        pullListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0 && pullListView.getRefreshableView().getChildAt(0) != null && pullListView.getRefreshableView().getChildAt(0).getTop() != 0) {
                    float total_height = ll_everyday.getTop() * 3;
                    int viewScrollHeigh = Math.abs(pullListView.getRefreshableView().getChildAt(0).getTop());
                    if (viewScrollHeigh < total_height) {
                        float alpha = (total_height - viewScrollHeigh) / total_height;
                        tv_top_bar_bg.setAlpha(1 - alpha);
                        tv_top_bar_bg.setBackgroundColor(1 - alpha > 0 ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.white));
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
                }
            }
        });
        //添加头布局
        View headView = View.inflate(getActivity(), R.layout.fragment_home, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
    }

    //每日精选、MOBA精选、枪战精选、新平尝鲜、品牌游戏列表（12个）
    private void queryHomeGame() {
        String url = Constant.WEB_SITE + Constant.URL_HOME_GAME_SELECTION;
        Response.Listener<JsonResult<QueryHomeGameBean>> successListener = new Response.Listener<JsonResult<QueryHomeGameBean>>() {
            @Override
            public void onResponse(JsonResult<QueryHomeGameBean> result) {

                if (result == null || result.code != 0) {
//                    Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
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
        //每日精选
        if (result.dailyRecommendedList.size() > 0) {
            GameInfo item = result.dailyRecommendedList.get(0);
            StringUtil.setPicassoImg(getActivity(), item.gameLogo, iv_recommend);
            tv_title.setText(item.gameName);
            rating_bar.setRating(item.percentage);
            tv_memory.setText("游戏大小：" + TextUtil.formatFileSize(item.gameSize));
//            tv_download_num.setText("/" + item.downloadCount + "次下载");
            tv_content.setText(item.gameDesc);
            dailyRecommendList.clear();
            dailyRecommendList.addAll(result.dailyRecommendedList);
            setEvertDayInfo();
        }
        //MODA精选
        if (result.MOBAGameList.size() > 0) {
            mobaList = result.MOBAGameList;
            typeListAdapter = new HomeGameTypeListAdapter(getActivity(), getSupportFragmentManager(), mobaList);
            gridView_bzrm.setAdapter(typeListAdapter);
        }
        //枪战精选
        if (result.gunGameList.size() > 0) {
            qiangzhanList = result.gunGameList;
            typeListAdapter = new HomeGameTypeListAdapter(getActivity(), getSupportFragmentManager(), qiangzhanList);
            gridView_rqwy.setAdapter(typeListAdapter);
        }
        //新平尝鲜
        if (result.newGameList.size() > 0) {
            xinpingList = result.newGameList;
            typeListAdapter = new HomeGameTypeListAdapter(getActivity(), getSupportFragmentManager(), xinpingList);
            gridView_yxjx.setAdapter(typeListAdapter);
        }
        //腾讯品牌游戏（2个）
        if (result.TENCENTGameList.size() > 0) {
            bTencentList = result.TENCENTGameList;
            gameListAdapter = new SelectGameAdapter(getActivity(), getSupportFragmentManager());
            listView_tBarnd.setAdapter(gameListAdapter);
            gameListAdapter.setDate(bTencentList);
            gameListAdapter.notifyDataSetChanged();
            StringUtil.setListViewHeightBasedOnChildren(listView_tBarnd, 0);
        }
        // 品牌游戏 剩余（10个）
        allBradnList.clear();
        allBradnList.addAll(result.WANGYIGameList);
        allBradnList.addAll(result.HUYUGameList);
        allBradnList.addAll(result.KUNLUNGameList);
        allBradnList.addAll(result.SHENGLIGameList);
        allBradnList.addAll(result.ZHANGQUGameList);
        if (allBradnList.size() > 0) {
            gameListAdapter = new SelectGameAdapter(getActivity(), getSupportFragmentManager());
            listView_allBarnd.setAdapter(gameListAdapter);
            gameListAdapter.setDate(allBradnList);
            gameListAdapter.notifyDataSetChanged();
            StringUtil.setListViewHeightBasedOnChildren(listView_allBarnd, 0);
        }
    }

    //请求 云端适配-原生手柄-虚拟现实
    private void doYunduanInfo(final int id) {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        bodyBean.setMarkId(id);
        new YunduanClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (result.getData() == null) {
                                return;
                            }
                            if (result.getData().size() <= 0) {
                                return;
                            }
                            if (id == TYPE_HAND) {
                                yunduanList.clear();
                                yunduanList.addAll(result.getData());
                                StringUtil.setPicassoImg(getActivity(), yunduanList.get(0).getLogoUrl(), iv_zhuanti);
                                StringUtil.setPicassoImg(getActivity(), yunduanList.get(1).getLogoUrl(), iv_yunduan);
                                StringUtil.setPicassoImg(getActivity(), yunduanList.get(2).getLogoUrl(), iv_yuansheng);
                                tv_zhuanti.setText(yunduanList.get(0).getTypeName());
                                tv_yunduan.setText(yunduanList.get(1).getTypeName());
                                tv_yuansheng.setText(yunduanList.get(2).getTypeName());
                            } else {
                                tencentList.clear();
                                tencentList.addAll(result.getData());
                                TencentGridAdapter tencentGridView = new TencentGridAdapter(getActivity(), tencentList);
                                gridView_tencent.setAdapter(tencentGridView);
                                StringUtil.setGridViewHeightBasedOnChildren(gridView_tencent, 3);
                            }
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    //请求 虚拟现实
    private void doXuniInfo() {
        YunduanBodyBean bodyBean = new YunduanBodyBean();
        bodyBean.setMarkId(5);
        new XuniClient(getActivity(), bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<YunduanBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(YunduanBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (result.getData() == null) {
                                return;
                            }
                            if (result.getData().size() <= 0) {
                                return;
                            }
                            xuniList.clear();
                            xuniList.addAll(result.getData());
                            StringUtil.setPicassoImg(getActivity(), xuniList.get(0).getLogoUrl(), iv_xuni);
                            tv_xuni.setText(xuniList.get(0).getText());
                        } else {
//                            ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
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

    private void initHeadView(View view) {
        banner_view = (BannerView) view.findViewById(R.id.banner_view);
        ll_yunduan = (LinearLayout) view.findViewById(R.id.ll_yunduan);
        ll_yuansheng = (LinearLayout) view.findViewById(R.id.ll_yuansheng);
        ll_xuni = (LinearLayout) view.findViewById(R.id.ll_xuni);
        ll_zhuanti = (LinearLayout) view.findViewById(R.id.ll_zhuanti);
        iv_yunduan = (ImageView) view.findViewById(R.id.iv_yunduan);
        iv_yuansheng = (ImageView) view.findViewById(R.id.iv_yuansheng);
        iv_xuni = (ImageView) view.findViewById(R.id.iv_xuni);
        iv_zhuanti = (ImageView) view.findViewById(R.id.iv_zhuanti);
        tv_yunduan = (TextView) view.findViewById(R.id.tv_yunduan);
        tv_yuansheng = (TextView) view.findViewById(R.id.tv_yuansheng);
        tv_xuni = (TextView) view.findViewById(R.id.tv_xuni);
        tv_zhuanti = (TextView) view.findViewById(R.id.tv_zhuanti);

        ll_everyday = (LinearLayout) view.findViewById(R.id.ll_everyday);
        iv_recommend = (ImageView) view.findViewById(R.id.iv_recommend);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_memory = (TextView) view.findViewById(R.id.tv_memory);
        tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
        day_progress_bar = (GameLoadProgressBar) view.findViewById(R.id.day_progress_bar);
        rl_hot_more = (RelativeLayout) view.findViewById(R.id.rl_hot_more);

        gridView_bzrm = (GridView) view.findViewById(R.id.gridView_bzrm);
        gridView_rqwy = (GridView) view.findViewById(R.id.gridView_rqwy);
        gridView_yxjx = (GridView) view.findViewById(R.id.gridView_yxjx);

        listView_tBarnd = (ListView) view.findViewById(R.id.listView_tBarnd);
        listView_allBarnd = (ListView) view.findViewById(R.id.listView_allBarnd);

        gridView_tencent = (GridView) view.findViewById(R.id.gridView_tencent);

        ll_yunduan.setOnClickListener(this);
        ll_yuansheng.setOnClickListener(this);
        ll_xuni.setOnClickListener(this);
        ll_zhuanti.setOnClickListener(this);
        ll_everyday.setOnClickListener(this);
        rl_hot_more.setOnClickListener(this);

        gridView_bzrm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", mobaList.get(position).id);
                startActivity(intent);
            }
        });
        gridView_rqwy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", qiangzhanList.get(position).id);
                startActivity(intent);
            }
        });
        gridView_yxjx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", xinpingList.get(position).id);
                startActivity(intent);
            }
        });
        listView_tBarnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", bTencentList.get(position).id);
                startActivity(intent);
            }
        });
        listView_allBarnd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", allBradnList.get(position).id);
                startActivity(intent);
            }
        });
        gridView_tencent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameListActivity.class);
                intent.putExtra("categoryId", tencentList.get(position).getId());
                intent.putExtra("title", tencentList.get(position).getTypeName());
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 每日精选-下载状态
     */
    public void setEvertDayInfo() {
        fileLoad = FileLoadManager.getInstance(getActivity());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(dailyRecommendList.get(0).filename, dailyRecommendList.get(0).gameLink, dailyRecommendList.get(0).packages, ConvUtil.NI(dailyRecommendList.get(0).versionCode));
                        day_progress_bar.setLoadState(fileStatus);
                    }
                });
            }
        }, 0, 500);

        //设置进度条状态
        day_progress_bar.setLoadState(fileLoad.getGameFileLoadStatus(dailyRecommendList.get(0).filename, dailyRecommendList.get(0).gameLink, dailyRecommendList.get(0).packages, ConvUtil.NI(dailyRecommendList.get(0).versionCode)));
        //必须设置，否则点击进度条后无法进行响应操作
        FileLoadInfo fileLoadInfo = new FileLoadInfo(dailyRecommendList.get(0).filename, dailyRecommendList.get(0).gameLink, dailyRecommendList.get(0).md5, ConvUtil.NI(dailyRecommendList.get(0).versionCode), dailyRecommendList.get(0).gameName, dailyRecommendList.get(0).gameLogo, dailyRecommendList.get(0).id, FileLoadInfo.TYPE_GAME);
        fileLoadInfo.setPackageName(dailyRecommendList.get(0).packages);
        day_progress_bar.setFileLoadInfo(fileLoadInfo);
        day_progress_bar.setOnStateChangeListener(new ProgressBarStateListener(getActivity(), getSupportFragmentManager()));
        day_progress_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day_progress_bar.toggle();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //右上角消息状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                IPushMessageModel pushModel = new PushMessageModel(getActivity());
                final int count = pushModel.getUnReadMsgCount(0);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            tv_notifi_num.setVisibility(View.GONE);
                        } else {
                            tv_notifi_num.setVisibility(View.VISIBLE);
                            tv_notifi_num.setText(count + "");
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_zhuanti:
                //专题精选
                Intent intent2 = new Intent(getActivity(), SelectedTopicsActivity.class);
                intent2.putExtra("title", yunduanList.get(0).getTypeName());
                getActivity().startActivity(intent2);
                break;
            case R.id.ll_yunduan:
                //云端适配-游戏列表
                if (yunduanList.size() > 0) {
                    Intent intent = new Intent(getActivity(), GameListActivity.class);
                    intent.putExtra("categoryId", yunduanList.get(1).getId());
                    intent.putExtra("title", yunduanList.get(1).getTypeName());
                    getActivity().startActivity(intent);
                }
//                startActivity(new Intent(getActivity(), GameDetail2Activity.class));
                break;
            case R.id.ll_yuansheng:
                //原生手柄-游戏列表
                if (yunduanList.size() > 0) {
                    Intent yuansheng = new Intent(getActivity(), GameListActivity.class);
                    yuansheng.putExtra("categoryId", yunduanList.get(2).getId());
                    yuansheng.putExtra("title", yunduanList.get(2).getTypeName());
                    getActivity().startActivity(yuansheng);
                }
                break;
            case R.id.ll_xuni:
                //虚拟现实
                Intent xuni = new Intent();
                xuni.setClass(getActivity(), FictitiousActivity.class);
                xuni.putExtra("title", xuniList.get(0).getText());
                startActivity(xuni);
                break;
            case R.id.ll_everyday: //点击每日精选-游戏详情
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", dailyRecommendList.get(0).id);
                startActivity(intent);
                break;
            case R.id.rl_hot_more: //热门攻略-更多
                Intent i = new Intent();
                i.setClass(getActivity(), StrategyActivity.class);
                i.putExtra("typeValue", "1");
                startActivity(i);
                break;
            case R.id.tv_toSearch:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.fl_notifi:
                startActivity(new Intent(getActivity(), PushMessageActivity.class));
                break;
        }
    }

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {
        String url = Constant.WEB_SITE + Constant.URL_BANNER;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {
                if (result == null) {
//                    Toast.makeText(getActivity(), "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    List<ImageView> list = createBannerView(result.data);
                    banner_view.setData(list);
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

        Request<JsonResult<List<HotInfo>>> request = new GsonRequest<JsonResult<List<HotInfo>>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", String.valueOf(1));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 创建轮播视图
     */
    private List<ImageView> createBannerView(List<HotInfo> hotInfoList) {
        if (hotInfoList == null || hotInfoList.size() <= 0) {
            return null;
        }
        ArrayList<ImageView> list = new ArrayList<>();
        for (int i = 0; i < hotInfoList.size(); i++) {
            final HotInfo info = hotInfoList.get(i);
            PicassoImageView img = new PicassoImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(params);
            img.setId((int) info.id);
            img.setTag(info.advImageLink);
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.type == 1) {
                        Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }
        return list;
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
    public void onDestroy() {
        super.onDestroy();
        yunduanList = null;
        xuniList = null;
        tencentList = null;
        dailyRecommendList = null;
        bTencentList = null;
        xinpingList = null;
        qiangzhanList = null;
        mobaList = null;
        allBradnList = null;
        dailyRecommendList = null;
        typeListAdapter = null;
        gameListAdapter = null;
        homeList = null;
        raiderAdapter = null;
    }
}
