package cn.ngame.store.activity.rank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameCommentListClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.adapter.RankingListAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;


/**
 * 下载榜
 * Created by gp on 2017/3/22 0022.
 */

public class Rank01234Fragment extends BaseSearchFragment {
    private PullToRefreshListView pullListView;
    /**
     * headerView
     */
    private GameLoadProgressBar day_progress_bar_one, day_progress_bar_two, day_progress_bar_three;
    private SimpleDraweeView sdv_img_1, sdv_img_2, sdv_img_3;
    private TextView tv_rank_name_one, tv_rank_name_two, tv_rank_name_three;
    private TextView tv_download_num_one, tv_download_num_two, tv_download_num_three;

    private Timer timer = new Timer();
    private IFileLoad fileLoad; //文件下载公共类接口

    private RankingListAdapter adapter;

    private PageAction pageAction;
    public static int PAGE_SIZE = 30;
    List<GameRankListBean.DataBean> topList = new ArrayList<>();
    List<GameRankListBean.DataBean> list = new ArrayList<>();
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean IS_LOADED = false;
    private static int mSerial = 0;
    private int mTabPosition = 0;
    private boolean isFirst = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (!IS_LOADED) {
                IS_LOADED = true;
                //这里执行加载数据的操作
                getRankList();
                Log.d(TAG, "去请求数据," + mTabPosition);
            } else {
                Log.d(TAG, "不请求数据," + mTabPosition);
            }
            return;
        }
    };
    private LinearLayout mTopLlay;
    private TabLayout tablayout2;
    private FragmentActivity content;
    private int tabPosition2 = 1;

    public static Rank01234Fragment newInstance() {
        Rank01234Fragment fragment = new Rank01234Fragment(0);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public Rank01234Fragment(int serial) {
        mSerial = serial;
    }

    public void sendMessage() {
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    @Override
    protected int getContentViewLayoutID() {
        android.util.Log.d(TAG, "0123getContentViewLayoutID: ");
        if (isFirst && mTabPosition == mSerial) {
            isFirst = false;
            sendMessage();
        }
        return R.layout.rank01234_fragment;
    }

    public void setTabPos(int mTabPos) {
        this.mTabPosition = mTabPos;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initViewsAndEvents(View view) {
        android.util.Log.d(TAG, "0123initViewsAndEvents: ");
        //        typeValue = getArguments().getInt("", 1);
        content = getActivity();
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        mTopLlay = (LinearLayout) view.findViewById(R.id.rank01234_top_llay);

        pullListView.setPullRefreshEnabled(true); //刷新
        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
//                getGameList();
                getRankList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction
                            .getCurrentPage() + 1);
//                    getGameList();
                    getRankList();
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
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });

        if (0 == mTabPosition) {
            mTopLlay.setVisibility(View.GONE);
        } else {
            tablayout2 = (TabLayout) view.findViewById(R.id.rank01234_tablayout);
            int length = tabList.length;
            for (int i = 0; i < length; i++) {
                tablayout2.addTab(tablayout2.newTab().setText(tabList[i]));
            }
            //二级标签
            initTabs2();
            final float topLLayHeight = CommonUtil.dip2px(content, 40);
            //设置滑动事件
            pullListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    android.util.Log.d("onScroll", "StateChanged " + scrollState);
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View childAt0 = pullListView.getChildAt(0);
                    if (firstVisibleItem == 0 && childAt0 != null && childAt0.getTop() !=
                            0) {
                        int viewScrollHeigh = Math.abs(childAt0.getTop());
                        android.util.Log.d("onScroll", "viewScrollHeigh " + viewScrollHeigh);
                        android.util.Log.d("onScroll", " topLLayHeight " + topLLayHeight);
                        if (viewScrollHeigh < topLLayHeight) {

                        } else {

                        }
                    } else {
                        android.util.Log.d("onScroll", "else " + firstVisibleItem);
                        if (firstVisibleItem != 0) {

                        } else {

                        }
                    }
                }
            });
        }
    }

    private String tabList[] = new String[]{"全部", "大陆", "美国", "韩国", "日本", "港澳台"};

    //顶部下面的二级标签
    private void initTabs2() {
        tablayout2.getTabAt(1).select();
        ViewGroup viewGroup = (ViewGroup) tablayout2.getChildAt(0);
        int childCount = viewGroup.getChildCount() - 1;
        int dp12 = CommonUtil.dip2px(content, 12);
        for (int i = 0; i <= childCount; i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(dp12);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);//textciew的宽度   AT_MOST
            int width = textView.getMeasuredWidth();
            layoutParams.weight = width;
            if (i < childCount) {
                layoutParams.setMargins(0, 0, 2, 0);
            }
        }
        //二级标签栏
        tablayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //0=全部   1=大陆   2=美国   3=韩国   4=日本   5=港澳台
                tabPosition2 = tab.getPosition();
                getRankList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 排行榜列表
     */
    private void getRankList() {
        //mTabPosition :0=全部   1=手柄   2=破解   3=汉化  4=特色
        android.util.Log.d(TAG, "请求数据,当前一级标签:" + mTabPosition);
        android.util.Log.d(TAG, "        ,当前二级标签: " + tabPosition2);
        GameListBody bodyBean = new GameListBody();
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameCommentListClient(getActivity(), bodyBean).observable()
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
    }

    public void listData(GameRankListBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            this.topList.clear();
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                return;
            }
        }
        if (result.getData().size() > 0) {
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
        }
        if (result.getData().size() > 0 && pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            this.topList.clear();
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
            this.topList.addAll(result.getData());
        }
        if (adapter == null) {
            adapter = new RankingListAdapter(getActivity(), getSupportFragmentManager(), list, 0);
            pullListView.getRefreshableView().setAdapter(adapter);
        } else {
            adapter.setList(list);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
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