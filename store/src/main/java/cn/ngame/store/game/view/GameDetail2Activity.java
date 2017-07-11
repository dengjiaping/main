package cn.ngame.store.game.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DCViewPagerAdapter;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.view.AutoHeightViewPager;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.StickyScrollView;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class GameDetail2Activity extends BaseFgActivity implements StickyScrollView.OnScrollChangedListener, HomeFragmentChangeLayoutListener {
    private RelativeLayout rl_top, rl_top2;
    private StickyScrollView scrollView;
    private SimpleDraweeView sdv_img;
    private ImageView iv_back;
    private TextView tv_gameName;
    private LinearLayout ll_download;

    private TabLayout tablayout;
    private AutoHeightViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private DCViewPagerAdapter adapter;
    List<String> tabList = new ArrayList<String>();
    //游戏id
    private long gameId = 0;
    private GameInfo gameInfo;
    //下载进度条
    private GameLoadProgressBar progressBar;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private IFileLoad fileLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_detail2_activity);
        init();
    }

    private void init() {
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        rl_top2 = (RelativeLayout) findViewById(R.id.rl_top2);
        scrollView = (StickyScrollView) findViewById(R.id.scrollView);
        sdv_img = (SimpleDraweeView) findViewById(R.id.sdv_img);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_gameName = (TextView) findViewById(R.id.tv_gameName);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);
        scrollView.setOnScrollListener(this);
        gameId = getIntent().getLongExtra("id", 0);

//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rl_top.getLayoutParams();
//        params.setMargins(0, getStatusHeight(), 0, 0);
//        rl_top.setLayoutParams(params);
//        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) rl_top2.getLayoutParams();
//        params2.setMargins(0, getStatusHeight(), 0, 0);
//        rl_top2.setLayoutParams(params2);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(3);
        getGameInfo(); //请求
    }

    /**
     * 获取游戏详情
     */
    private void getGameInfo() {
        tabList.add("详情");
        tabList.add("圈子");
        tabList.add("必读");
        String url = Constant.WEB_SITE + Constant.URL_GAME_DETAIL;
        Response.Listener<JsonResult<GameInfo>> successListener = new Response.Listener<JsonResult<GameInfo>>() {
            @Override
            public void onResponse(JsonResult<GameInfo> result) {
                if (result == null || result.code != 0) {
                    return;
                }
                gameInfo = result.data;
                if (gameInfo != null) {

//                    //设置进度条状态
//                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo.packages, ConvUtil.NI(gameInfo.versionCode)));
//                    //必须设置，否则点击进度条后无法进行响应操作
//                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil.NI(gameInfo.versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
//                    progressBar.setFileLoadInfo(fileLoadInfo);
//                    fileLoadInfo.setPackageName(gameInfo.packages);
//                    fileLoadInfo.setVersionCode(ConvUtil.NI(gameInfo.versionCode));
//                    progressBar.setOnStateChangeListener(new ProgressBarStateListener(GameDetail2Activity.this, GameDetail2Activity.this.getSupportFragmentManager()));
//                    progressBar.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            progressBar.toggle();
//                        }
//                    });

//                    ArrayList<Fragment> fragments = new ArrayList<>();
//                    fragments.add(GameStrategyFragment.newInstance(gameInfo));
//                    fragments.add(GameDetailFragment.newInstance(gameInfo));
//                    fragments.add(GameReviewFragment.newInstance(gameInfo));
//                    fragments.add(GameKeyFragment.newInstance(gameInfo));
                    initViewPager();
                    initTabs();
                } else {

                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        };

        Request<JsonResult<GameInfo>> request = new GsonRequest<JsonResult<GameInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameInfo>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gameId", String.valueOf(146));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusHeight() {
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    private void initViewPager() {
        fragments = new ArrayList<Fragment>();
        fragments.add(GameDetailFragment.newInstance( gameInfo));
        fragments.add(GameDetail2Fragment.newInstance());
        fragments.add(GameDetail2Fragment.newInstance());

        adapter = new DCViewPagerAdapter(getSupportFragmentManager(), fragments, tabList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabs() {
//        if (tabList.size() <= 3) {
        tablayout.setTabMode(TabLayout.MODE_FIXED); //固定模式
//        } else {
//        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE); //可滑动的tab
//        }
        tablayout.setupWithViewPager(viewpager);
        ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(16);

            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int width = textView.getMeasuredWidth();
            int screenWidth = ImageUtil.getScreenWidth(this);
            int margin = (screenWidth / tabList.size() - width) / tabList.size() + 5;
            if (tabList.size() <= 4) {
                layoutParams.setMargins(30, 0, 30, 0);
            } else {
                layoutParams.setMargins(margin, 0, margin, 0);
            }
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        float height = sdv_img.getHeight() - rl_top.getHeight();
//                float scrollHeigh = Math.abs(scrollView.getScaleY());
        if (t < height) {
            float alpha = (height - t) / height;
            tv_gameName.setAlpha(1 - alpha);
            rl_top.setAlpha(0);
        } else {
            rl_top.setAlpha(1);
            tv_gameName.setAlpha(1);
        }
        scrollView.setStickTop(rl_top.getMeasuredHeight());//设置距离多少悬浮
    }

    @Override
    public void changeLayoutHeight(int height) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.height = height;
        viewpager.setLayoutParams(layoutParams);
    }
}
