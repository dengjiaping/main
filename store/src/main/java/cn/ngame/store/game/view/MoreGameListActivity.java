package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.MoreGameListAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;


/**
 * 游戏列表
 * Created by zeng on 2016/6/16.
 */
public class MoreGameListActivity extends BaseFgActivity {

    private String TAG = MoreGameListActivity.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private LoadStateView loadStateView;
    private MoreGameListAdapter adapter;
    private List<LikeListBean.DataBean.GameListBean> gameInfoList;
    private PageAction pageAction;
    public int PAGE_SIZE = 10;
    private String mLabelId;
    private MoreGameListActivity content;
    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_more_game_list);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        content = MoreGameListActivity.this;
        Intent intent = getIntent();
        String title = intent.getStringExtra(KeyConstant.TITLE);
        mLabelId = intent.getStringExtra(KeyConstant.category_Id);
        android.util.Log.d(TAG, title + "分类id:" + mLabelId);
        Button leftBt = (Button) findViewById(R.id.left_bt);
        findViewById(R.id.center_tv).setVisibility(View.GONE);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        leftBt.setText(title);

        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.isShowLoadBut(false);

        pullListView = (PullToRefreshListView) findViewById(R.id.pullListView);
        pullListView.setPullRefreshEnabled(true); //刷新
        pullListView.setPullLoadEnabled(true); //false,不允许上拉加载
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                loadStateView.setVisibility(View.GONE);
                getGameList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                int total = pageAction.getTotal();
                if (total < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                int currentPage = pageAction.getCurrentPage();
                if (currentPage * pageAction.getPageSize() < total) {
                    pageAction.setCurrentPage(currentPage + 1);
                    getGameList();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
        //点击事件
        ListView refreshableView = pullListView.getRefreshableView();
        refreshableView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoreGameListActivity.this, GameDetailActivity.class);
                intent.putExtra(KeyConstant.ID, adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        gameInfoList = new ArrayList<>();
        //加载游戏分类，默认加载第一个分类
        getGameList();
        adapter = new MoreGameListAdapter(this, getSupportFragmentManager(), timerTasks);
        refreshableView.setAdapter(adapter);
    }

    private void getGameList() {
        gameInfoList.clear();
        if (adapter != null) {
            adapter.setDate(gameInfoList);
            adapter.notifyDataSetChanged();
        }
        String url = Constant.WEB_SITE + Constant.URL_LABEL_GAME_LIST;
        Response.Listener<LikeListBean> successListener = new Response.Listener<LikeListBean>() {
            @Override
            public void onResponse(LikeListBean result) {
                if (result == null || result.getData() == null) {
                    if (gameInfoList != null && gameInfoList.size() > 0) {
                        loadStateView.setVisibility(View.GONE);
                        ToastUtil.show(content, getString(R.string.server_exception));
                    } else {
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.server_exception));
                        loadStateView.setVisibility(View.VISIBLE);
                    }
                    pullListView.onPullUpRefreshComplete();
                    pullListView.onPullDownRefreshComplete();
                    return;
                }
                LikeListBean.DataBean data = result.getData();
                if (pageAction.getCurrentPage() == 0) {
                    List<LikeListBean.DataBean.GameListBean> gameList = data.getGameList();
                    if (gameList == null || gameList.size() == 0) {
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
                        return;
                    }
                }
                if (result.getCode() == 0) {
                    gameInfoList = data.getGameList();
                    pageAction.setTotal(gameInfoList.size());
                    Log.d(TAG, gameInfoList.size() + "返回");
                    if (gameInfoList != null && gameInfoList.size() > 0) {
                        loadStateView.setVisibility(View.GONE);
                        if (null != adapter) {
                            adapter.setDate(gameInfoList);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END, getString(R.string.no_data));
                    }

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                    loadStateView.setState(LoadStateView.STATE_END);
                }
                //设置下位列表
          /*      if ((gameInfoList.size() == 0 && pageAction.getTotal() == 0) || gameInfoList.size() >= pageAction.getTotal()) {
                    pullListView.setPullLoadEnabled(false);
                } else {
                    pullListView.setPullLoadEnabled(true);
                }*/
                if (pageAction.getCurrentPage() > 0 && result.getData().getGameList().size() > 0) { //设置上拉刷新后停留的地方
                    int index = pullListView.getRefreshableView().getFirstVisiblePosition();
                    View v = pullListView.getRefreshableView().getChildAt(0);
                    int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
                    pullListView.getRefreshableView().setSelectionFromTop(index, top);
                }
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                if (gameInfoList != null && gameInfoList.size() > 0) {
                    loadStateView.setVisibility(View.GONE);
                    ToastUtil.show(content, getString(R.string.server_exception));
                } else {
                    loadStateView.setState(LoadStateView.STATE_END, getString(R.string.server_exception));
                    loadStateView.setVisibility(View.VISIBLE);
                }
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
            }
        };

        Request<LikeListBean> request = new GsonRequest<LikeListBean>(
                Request.Method.POST, url, successListener,
                errorListener, new TypeToken<LikeListBean>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.category_Id, mLabelId);
                params.put(KeyConstant.start_Record, String.valueOf(0));
                params.put(KeyConstant.RECORDS, String.valueOf(PAGE_SIZE));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != adapter) {
            adapter.clean();
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }
    }
}
