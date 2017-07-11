package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.LvSbGameAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.BaseTitleBar;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

/**
 * 显示VR游戏列表的页面
 * Created by zeng on 2016/6/16.
 */
public class GameListActivity extends BaseFgActivity {

    public static final String TAG = GameListActivity.class.getSimpleName();
    private PullToRefreshListView pullListView;
    private LoadStateView loadStateView;
    private LvSbGameAdapter adapter;
    private List<GameInfo> gameInfoList;

    private PageAction pageAction;
    public static int PAGE_SIZE = 20;

    private int lastItem;

    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_vr_game_list);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        categoryId = intent.getLongExtra("categoryId",0);

        BaseTitleBar title_bar = (BaseTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameListActivity.this.finish();
            }
        });
        title_bar.setTitleText(title);

        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGameList(); //重新加载
            }
        });

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
                getGameList();
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
                    getGameList();
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
                Intent intent = new Intent(GameListActivity.this, GameDetailActivity.class);
                intent.putExtra("id", ((GameInfo)adapter.getItem(position)).id);
                startActivity(intent);
            }
        });
        gameInfoList = new ArrayList<>();
        //加载游戏分类，默认加载第一个分类
        getGameList();
        setListener();
    }

    private void setListener() {

        adapter = new LvSbGameAdapter(this,getSupportFragmentManager());
        pullListView.getRefreshableView().setAdapter(adapter);

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//                long count = adapter.getCount();
//
//                if (lastItem >= (count - 1) && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE ) {
//                    if(count < totals){
//                        pageIndex += 1;
//                        getGameList();
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                lastItem = firstVisibleItem + visibleItemCount;
//                Log.d(TAG,"-------------------------lastItem: "+lastItem);
//            }
//        });
    }

    /**
     * 获取制定分类下的游戏列表
     */
    private void getGameList() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_LIST;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {

                if(result == null){
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }
                if (pageAction.getCurrentPage() == 0) {
                    gameInfoList.clear(); //清除数据
                    if (result.data == null || result.data.size() == 0) {
                        pullListView.onPullUpRefreshComplete();
                        pullListView.onPullDownRefreshComplete();
                        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                        return;
                    }
                }
                if(result.code == 0){

                    gameInfoList = result.data;
                    pageAction.setTotal(result.totals);
                    if(gameInfoList != null && gameInfoList.size() > 0){

                        loadStateView.setVisibility(View.GONE);

                        adapter.setDate(gameInfoList);
                        adapter.notifyDataSetChanged();
                    }else {
                        loadStateView.isShowLoadBut(false);
                        loadStateView.setVisibility(View.VISIBLE);
                        loadStateView.setState(LoadStateView.STATE_END,"没有数据");
                    }

                }else {
                    Log.d(TAG,"HTTP请求成功：服务端返回错误！");
                    loadStateView.setState(LoadStateView.STATE_END);
                }
                //设置下位列表
                if ((gameInfoList.size() == 0 && pageAction.getTotal() == 0) || gameInfoList.size() >= pageAction.getTotal()) {
                    pullListView.setPullLoadEnabled(false);
                } else {
                    pullListView.setPullLoadEnabled(true);
                }
                if (pageAction.getCurrentPage() > 0 && result.data.size() > 0) { //设置上拉刷新后停留的地方
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
                loadStateView.setState(LoadStateView.STATE_END);
            }
        };

        Request<JsonResult<List<GameInfo>>> request = new GsonRequest<JsonResult<List<GameInfo>>>(
                Request.Method.POST, url, successListener,
                errorListener, new TypeToken<JsonResult<List<GameInfo>>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("categoryId", String.valueOf(categoryId));
                params.put("gameLabelId", String.valueOf(0));
                params.put("pageIndex", String.valueOf(pageAction.getCurrentPage()));
                params.put("pageSize", String.valueOf(PAGE_SIZE));

                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }
}
