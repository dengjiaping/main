package cn.ngame.store.game.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.GameDetailAdapter;
import cn.ngame.store.adapter.ProgressBarStateListener;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.GameType;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.GameTabView;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 显示游戏详情的窗口
 * Created by zeng on 2016/5/17.
 */
public class GameDetailActivity extends BaseFgActivity {

    private static final String TAG = GameDetailActivity.class.getSimpleName();
    private GameDetailAdapter adapter;
    private RatingBar rating_bar;

    private TextView tv_title, tv_size, tv_count, tv_type, tv_operation, tv_score;
    private PicassoImageView img_logo;
    //private DownLoadProgressBar progressBar;    //下载进度条
    private GameLoadProgressBar progressBar;    //下载进度条

    private long gameId = 0;
    private GameInfo gameInfo;

    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private IFileLoad fileLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_game_detail);

        SimpleTitleBar titleBar = (SimpleTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                GameDetailActivity.this.finish();
            }
        });

        fileLoad = FileLoadManager.getInstance(this);

        tv_count = (TextView) findViewById(R.id.text1);
        tv_size = (TextView) findViewById(R.id.text2);
        tv_type = (TextView) findViewById(R.id.text3);
        tv_operation = (TextView) findViewById(R.id.text4);
        tv_score = (TextView) findViewById(R.id.text5);

        tv_title = (TextView) findViewById(R.id.tv_title);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        progressBar = (GameLoadProgressBar) findViewById(R.id.progress_bar);

        adapter = new GameDetailAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        GameTabView tag_view = (GameTabView) findViewById(R.id.tag_view);
        tag_view.setViewPager(viewPager);   //viewpager必须在前面实例化
    }

    @Override
    protected void onResume() {
        super.onResume();
        //加载数据
        gameId = getIntent().getLongExtra("id", 0);
        getGameInfo();
    }

    /**
     * 获取游戏详情
     */
    private void getGameInfo() {

        String url = Constant.WEB_SITE + Constant.URL_GAME_DETAIL;
        Response.Listener<JsonResult<GameInfo>> successListener = new Response.Listener<JsonResult<GameInfo>>() {
            @Override
            public void onResponse(JsonResult<GameInfo> result) {

                if (result == null || result.code != 0) {
                    return;
                }
                gameInfo = result.data;
                if (gameInfo != null) {

                    //设置进度条状态
                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages, ConvUtil.NI(gameInfo.versionCode)));
                    //必须设置，否则点击进度条后无法进行响应操作
                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil.NI(gameInfo.versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
                    progressBar.setFileLoadInfo(fileLoadInfo);
                    fileLoadInfo.setPackageName(gameInfo.packages);
                    fileLoadInfo.setVersionCode(ConvUtil.NI(gameInfo.versionCode));
                    progressBar.setOnStateChangeListener(new ProgressBarStateListener(GameDetailActivity.this, GameDetailActivity.this.getSupportFragmentManager()));
                    progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.toggle();
                        }
                    });

                    initView();

                    ArrayList<Fragment> fragments = new ArrayList<>();
                    fragments.add(GameStrategyFragment.newInstance(gameInfo));
                    fragments.add(GameDetailFragment.newInstance(gameInfo));
                    fragments.add(GameReviewFragment.newInstance(gameInfo));
                    fragments.add(GameKeyFragment.newInstance(gameInfo));

                    adapter.setDate(fragments);
                    adapter.notifyDataSetChanged();

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

        Request<JsonResult<GameInfo>> request = new GsonRequest<JsonResult<GameInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameInfo>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gameId", String.valueOf(gameId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }


    private void initView() {

        String gameName = gameInfo.gameName;
        if (!"".equals(gameName)) {
//            gameName = gameName.length() > 9 ? gameName.substring(0, 9) : gameName;
            tv_title.setText(gameName);
        }

        rating_bar.setRating(gameInfo.percentage);
        tv_score.setText(gameInfo.percentage + "分");

        long gameSize = gameInfo.gameSize;
        String gameSizeStr = TextUtil.formatFileSize(gameSize);
        tv_size.setText(gameSizeStr);

        tv_count.setText(gameInfo.downloadCount + "次");

        List<GameType> typeList = gameInfo.gameTypeList;
        String typeStr = "";
        if (typeList != null) {
            StringBuffer sb = new StringBuffer();
            for (GameType t : typeList) {
                sb.append(t.typeName).append("、");
            }
            if (sb.length() > 1) {
                typeStr = sb.substring(0, sb.length() - 1);
            }
        }
        tv_type.setText(typeStr);

        tv_operation.setText(gameInfo.operation);

        img_logo = (PicassoImageView) findViewById(R.id.img_1);
        img_logo.setImageUrl(gameInfo.gameLogo, 70f, 70f, R.drawable.default_logo);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages, ConvUtil.NI(gameInfo.versionCode));
                        progressBar.setLoadState(fileStatus);
                    }
                });
            }
        }, 0, 500);
    }

}
