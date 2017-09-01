package cn.ngame.store.activity.rank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.rank.RankListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.game.GameCommentListClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.Ranking012345Adapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 下载榜
 * Created by gp on 2017/3/22 0022.
 */
@SuppressLint("ValidFragment")
public class Rank012345Fragment extends BaseSearchFragment {
    private PullToRefreshListView pullListView;
    private Ranking012345Adapter adapter;
    protected final static String TAG = Rank012345Fragment.class.getSimpleName();
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private List<LikeListBean.DataBean.GameListBean> list = new ArrayList<>();
    private boolean IS_LOADED = false;
    private static int mSerial = 0;
    private int tab_position = 0;
    private boolean isFirst = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (!IS_LOADED) {
                IS_LOADED = true;
                //这里执行加载数据的操作
                getRankList();
            } else {
                // Log.d(TAG, tab_position + "不请求数据," + tab2_position);
            }
            return;
        }
    };
    private LinearLayout mTopLlay;
    private TabLayout tablayout2;
    private FragmentActivity content;
    private int tab2_position = 0;
    private FragmentManager fm;
    private LoadStateView loadStateView;

    public static Rank012345Fragment newInstance(int type) {
        Rank012345Fragment fragment = new Rank012345Fragment(0);
        Bundle bundle = new Bundle();
        //bundle.putString("type", type+"");
        fragment.setArguments(bundle);
        return fragment;
    }

    public Rank012345Fragment(int serial) {
        mSerial = serial;
    }

    public Rank012345Fragment() {
    }

    public void sendMessage() {
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    @Override
    protected int getContentViewLayoutID() {
        if (isFirst && tab_position == mSerial) {
            isFirst = false;
            sendMessage();
        }
        return R.layout.rank01234_fragment;
    }

    //第一级标签
    private int tab_ids[] = new int[]{0, 2, 3, 4, 6, 53};

    public void setTabPos(int mTabPos) {
        this.tab_position = tab_ids[mTabPos];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        fm = getSupportFragmentManager();
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pullListView);
        mTopLlay = (LinearLayout) view.findViewById(R.id.rank01234_top_llay);

        loadStateView = (LoadStateView) view.findViewById(R.id.load_state_view);
        loadStateView.isShowLoadBut(false);
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
                Log.d(TAG, pageAction.getTotal() + "onPullUpToRefresh: " + pageAction.getPageSize());
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    ToastUtil.show(content, "已经到底了");
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                int currentPage = pageAction.getCurrentPage();
                if (currentPage * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(currentPage == 0 ? currentPage + 2 : currentPage + 1);
//                    getGameList();
                    getRankList();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        final ListView refreshableView = pullListView.getRefreshableView();
        adapter = new Ranking012345Adapter(content, fm, list, 0);
        refreshableView.setAdapter(adapter);
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameDetailActivity.class);
                intent.putExtra(KeyConstant.ID, list.get(position).getId());
                startActivity(intent);
            }
        });

        if (0 == tab_position) {
            mTopLlay.setVisibility(View.GONE);
        } else {
            tablayout2 = (TabLayout) view.findViewById(R.id.rank01234_tablayout);
            if (53 == tab_position) {
                //默认FC的id
                tab2_position = 50;
                int length = tabList5.length;
                for (int i = 0; i < length; i++) {
                    tablayout2.addTab(tablayout2.newTab().setText(tabList5[i]));
                }
                tab2_all = tab2_id5;
                initTabs5();
            } else {
                int length = tabList.length;
                for (int i = 0; i < length; i++) {
                    tablayout2.addTab(tablayout2.newTab().setText(tabList[i]));
                }
                tab2_all = tab2_Id01234;
                //二级标签
                initTabs1234();
            }
            final float topLLayHeight = CommonUtil.dip2px(content, 40);
            //设置滑动事件
            pullListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View childAt0 = refreshableView.getChildAt(0);
                    if (firstVisibleItem == 0 && childAt0 != null && childAt0.getTop() !=
                            0) {
                        int viewScrollHeigh = Math.abs(childAt0.getTop());
                        if (viewScrollHeigh > 2 * topLLayHeight) {//滑动超过头部高度
                        } else {//滑动没有超过头部高度
                        }
                    } else {
                        //第一个条目
                        if (firstVisibleItem == 0) {
                        } else {//下滑
                        }
                    }
                }
            });

            //二级标签栏
            tablayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    //0=全部   1=大陆   2=美国   3=韩国   4=日本   5=港澳台
                    int position = tab.getPosition();
                    int id = tab2_all[position];

                    tab2_position = id;
                    android.util.Log.d(TAG, tab.getText() + ",点击id," + id);

                    //请求数据
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
    }

    private String tabList[] = new String[]{"全部", "大陆", "美国", "韩国", "日本", "港澳台"};
    private int tab2_Id01234[] = new int[]{0, 47, 49, 51, 50, 48};
    private String tabList5[] = new String[]{"FC", "MAME", "SFC", "GBA", "PS", "PSP", "MD", "GBC", "NDS"};
    private int tab2_id5[] = new int[]{54, 55, 56, 57, 58, 59, 60, 61, 62};
    private int tab2_all[];

    //顶部下面的二级标签
    private void initTabs1234() {
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
    }

    //模拟器
    private void initTabs5() {
        tablayout2.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams tab5Params = (LinearLayout.LayoutParams) tablayout2.getLayoutParams();
        tab5Params.setMargins(0, 0, 0, 0);
        tablayout2.setTabTextColors(ContextCompat.getColor(content, R.color.color999999), Color.WHITE);
        ViewGroup viewGroup = (ViewGroup) tablayout2.getChildAt(0);
        int dp20 = CommonUtil.dip2px(content, 20);
        int dp42 = CommonUtil.dip2px(content, 42);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.dm012));
            textView.setWidth(1 == i ? dp42 + dp20 / 2 : dp42);
            textView.setHeight(dp20);
            textView.setBackgroundResource(R.drawable.selector_rank5_tab2_bg);
        }
    }

    /**
     * 获取排行榜列表数据
     */
    private void getRankList() {
        // Log.d(TAG, tab_position + ",请求数据,当前索引:" + tab2_position);
        //tab_position :0=全部   1=手柄   2=破解   3=汉化  4=特色
        loadStateView.setVisibility(View.VISIBLE);
        loadStateView.setState(LoadStateView.STATE_ING);
        Log.d(TAG, "请求索引? StartRecord是 pageAction.getCurrentPage()" + pageAction.getCurrentPage());
        Log.d(TAG, "请求大小" + PAGE_SIZE);
        RankListBody bodyBean = new RankListBody();
        bodyBean.setStartRecord(pageAction.getCurrentPage());
        bodyBean.setRecords(PAGE_SIZE);
        bodyBean.setParentCategoryId(tab_position);
        bodyBean.setCategoryId(tab2_position);
        new GameCommentListClient(content, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<LikeListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();

                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.requery_failed));
                    }

                    @Override
                    public void onNext(LikeListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            android.util.Log.d(TAG, "onNext: 查询无结果");
                            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
                        }
                    }
                });
    }

    //设置数据
    public void listData(LikeListBean dataBean) {
        LikeListBean.DataBean result = dataBean.getData();
        if (result == null || result.getGameList() == null) {
            android.util.Log.d(TAG, "gameList.NULL ");
            android.util.Log.d(TAG, "list.size:NULL " + list.size());
            loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
            return;
        }
        List<LikeListBean.DataBean.GameListBean> gameList = result.getGameList();
        int size = gameList.size();
        if (pageAction.getCurrentPage() == 0) {
            android.util.Log.d(TAG, "1请求的.size: " + gameList.size());
            android.util.Log.d(TAG, "1本地size: " + list.size());
            this.list.clear(); //清除数据
            adapter.setList(list);
            if (gameList == null || size == 0) {
                Log.d(TAG, "1 请求的数据 == null请求的size == 0: ");
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
                return;
            }
        }
        loadStateView.setVisibility(View.GONE);
        if (size > 0) {
            android.util.Log.d(TAG, "2请求的size: " + gameList.size());
            android.util.Log.d(TAG, "2本地listsize: " + list.size());
            android.util.Log.d(TAG, "result.getTotals() " + result.getTotals());
            pageAction.setTotal(result.getTotals());
            this.list.addAll(gameList);
        }
        if (size > 0 && pageAction.getCurrentPage() == 0) {
            android.util.Log.d(TAG, "3请求的.size: " + gameList.size());
            android.util.Log.d(TAG, "3本地list.size: " + list.size());
            this.list.clear(); //清除数据
            pageAction.setTotal(result.getTotals());
            this.list.addAll(gameList);
        }
        ListView refreshableView = pullListView.getRefreshableView();
        //设置适配器
        adapter.setList(list);
        if (pageAction.getCurrentPage() > 0 && size > 0) { //设置上拉刷新后停留的地方
            int index = refreshableView.getFirstVisiblePosition();
            View v = refreshableView.getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            refreshableView.setSelectionFromTop(index, top);
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
