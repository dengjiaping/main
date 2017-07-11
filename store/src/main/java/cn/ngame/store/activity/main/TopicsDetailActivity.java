package cn.ngame.store.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.GameSelectClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.TopicsDetailAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 专题详情
 * Created by gp on 2017/4/13 0013.
 */

public class TopicsDetailActivity extends BaseFgActivity {

    private LinearLayout ll_back;
    private TextView tv_title;
    private SimpleDraweeView sdv_img;
    private TextView tv_title2, tv_info;
    private PullToRefreshListView pullListView;
    TopicsDetailAdapter adapter;
    List<GameRankListBean.DataBean> list = new ArrayList<>();
    long id;
    String title, desc, url;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_detail_activity);
        init();
    }

    private void init() {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("专题详情");
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicsDetailActivity.this.finish();
            }
        });
        pullListView = (PullToRefreshListView) findViewById(R.id.pullListView);

        id = getIntent().getLongExtra("id", 0);
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        url = getIntent().getStringExtra("url");

        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                runService();
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
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    runService();
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
                if (position > 0) {
                    Intent i = new Intent();
                    i.setClass(TopicsDetailActivity.this, GameDetailActivity.class);
                    i.putExtra("id", list.get(position - 1).getId());
                    startActivity(i);
                }
            }
        });
        //添加头布局
        View headView = View.inflate(this, R.layout.select_game_header, null);
        initHeadView(headView);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(headView);
        }
        runService();
    }

    private void initHeadView(View headView) {
        sdv_img = (SimpleDraweeView) headView.findViewById(R.id.sdv_img);
        tv_title2 = (TextView) headView.findViewById(R.id.tv_title2);
        tv_info = (TextView) headView.findViewById(R.id.tv_info);
        tv_title2.setText(title);
        tv_info.setText(desc);
        sdv_img.setImageURI(url);
    }

    public void runService() {
        GameListBody bodyBean = new GameListBody();
        bodyBean.setCategoryId2(ConvUtil.NI(id));
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new GameSelectClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<GameRankListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                    }

                    @Override
                    public void onNext(GameRankListBean result) {
                        if (result != null && result.getCode() == 0) {
                            setData(result);
                        } else {
                            pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
//                          ToastUtil.show(getActivity(), result.getMsg());
                        }
                    }
                });
    }

    private void setData(GameRankListBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                pullListView.getRefreshableView().setAdapter(adapter); //数据位空时，加载头部
                return;
            }
        }
        pageAction.setTotal(result.getTotals());
        list.addAll(result.getData());
        if (adapter == null) {
            adapter = new TopicsDetailAdapter(this, getSupportFragmentManager(), list, 1);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        list = null;
    }
}
