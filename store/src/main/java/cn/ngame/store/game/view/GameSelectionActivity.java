package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.GameListAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.HotInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.BannerView;
import cn.ngame.store.view.BaseTitleBar;
import cn.ngame.store.view.LoadStateView;
import cn.ngame.store.view.PicassoImageView;

/**
 * 游戏精选列表的页面
 * Created by zeng on 2016/8/24.
 */
public class GameSelectionActivity extends BaseFgActivity {

    public static final String TAG = GameSelectionActivity.class.getSimpleName();

    private BannerView bannerView;
    private LoadStateView loadStateView;
    private ListView listView;
    private GameListAdapter adapter;
    private List<GameInfo> gameInfoList;
    private TextView tv_desc;

    private int moreId  = 1;
    private long selectId = 38;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_game_selection);

        String title = "游戏精选";
        Intent intent = getIntent();
        if(intent != null){
            title = intent.getStringExtra("title");
            moreId = intent.getIntExtra("moreId",1);
            selectId = intent.getIntExtra("selectId",38);
        }

        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitleText(title);

        loadStateView = (LoadStateView) findViewById(R.id.loadStateView);
        loadStateView.setReLoadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGameList(); //重新加载
            }
        });

        bannerView = (BannerView) findViewById(R.id.banner_view);
        listView = (ListView) findViewById(R.id.listView);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        getBannerData();
        getGameList();
        setListener();
    }

    private void setListener() {

        adapter = new GameListAdapter(this,getSupportFragmentManager());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(GameSelectionActivity.this, GameDetailActivity.class);
                intent.putExtra("id", ((GameInfo)adapter.getItem(position)).id);
                startActivity(intent);

            }
        });
    }

    /**
     * 游戏列表数据
     */
    private void getGameList() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_SELECTION_MORE;
        Response.Listener<JsonResult<List<GameInfo>>> successListener = new Response.Listener<JsonResult<List<GameInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<GameInfo>> result) {

                if(result == null){
                    loadStateView.setVisibility(View.VISIBLE);
                    loadStateView.setState(LoadStateView.STATE_END);
                    return;
                }

                if(result.code == 0){

                    gameInfoList = result.data;
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
                params.put("moreId", String.valueOf(moreId));
                params.put("selectId", String.valueOf(selectId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 获取轮播图片数据
     */
    private void getBannerData() {

        String url = Constant.WEB_SITE + Constant.URL_BANNER;
        Response.Listener<JsonResult<List<HotInfo>>> successListener = new Response.Listener<JsonResult<List<HotInfo>>>() {
            @Override
            public void onResponse(JsonResult<List<HotInfo>> result) {

                if (result == null || result.code != 0) {
                    Toast.makeText(GameSelectionActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<HotInfo> hotInfos = result.data;
                if (hotInfos!= null && hotInfos.size() > 0) {
                    tv_desc.setText(hotInfos.get(0).advDesc);
                    List<ImageView> list = createBannerView(result.data);
                    bannerView.setData(list);

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
                successListener, errorListener, new TypeToken<JsonResult<List<HotInfo>>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                final int id = moreId;
                switch (id){
                    case 1:
                        params.put("type", String.valueOf(9));
                        break;
                    case 2:
                        params.put("type", String.valueOf(7));
                        break;
                    case 3:
                        params.put("type", String.valueOf(8));
                        break;
                }

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
            PicassoImageView img = new PicassoImageView(this);
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
                        Intent intent = new Intent(GameSelectionActivity.this, GameDetailActivity.class);
                        intent.putExtra("id", info.gameId);
                        startActivity(intent);
                    } else if (info.type == 2) {
                        Intent intent = new Intent(GameSelectionActivity.this, VideoDetailActivity.class);
                        intent.putExtra("id", info.videoId);
                        startActivity(intent);
                    }
                }
            });
            list.add(img);
        }

        return list;
    }
}
