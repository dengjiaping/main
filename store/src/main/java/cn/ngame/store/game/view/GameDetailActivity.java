package cn.ngame.store.game.view;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.view.AutoHeightViewPager;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.StickyScrollView;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class GameDetailActivity extends BaseFgActivity implements StickyScrollView.OnScrollChangedListener,
        HomeFragmentChangeLayoutListener {
    private RelativeLayout rl_top, rl_top2;
    private StickyScrollView scrollView;
    private SimpleDraweeView sdv_img;
    private Button leftBt;
    private GameDetailActivity content;
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
    private Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        }
        // todo  xml
        setContentView(R.layout.activity_game_detail);
        content = this;
        gameId = getIntent().getLongExtra("id", 0);

        initStatus();
        initView();
    }

    private void initStatus() {
        //获取状态栏高度设置给标题栏==========================================
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        rl_top.setBackgroundResource(R.color.transparent);
        int statusBarHeight = ImageUtil.getStatusBarHeight(content);
        rl_top.setPadding(0, statusBarHeight, 0, 0);
        //======================================================================
        //rl_top2 = (RelativeLayout) findViewById(R.id.rl_top2);
        leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setPadding(CommonUtil.dip2px(content, 20), statusBarHeight, 0, 0);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.finish();
            }
        });
        fileLoad = FileLoadManager.getInstance(this);
    }

    private void initView() {
        tabList.add("详情");
        tabList.add("必读");
        scrollView = (StickyScrollView) findViewById(R.id.scrollView);
        sdv_img = (SimpleDraweeView) findViewById(R.id.sdv_img);

        scrollView.setOnScrollListener(this);


        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(2);
        initViewPager();
        initTabs();
        getGameInfo(); //请求
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
                    setViewPager();
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

    private void setViewPager() {
        fragments = new ArrayList<>();
        fragments.add(GameDetailFragment.newInstance(gameInfo));
        fragments.add(GameStrategyFragment.newInstance(gameInfo));

        adapter.setList(fragments, tabList);
        viewpager.setAdapter(adapter);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(GameDetailFragment.newInstance(gameInfo));
        // fragments.add(GameDetail2Fragment.newInstance());
        fragments.add(GameStrategyFragment.newInstance(gameInfo));

        adapter = new DCViewPagerAdapter(getSupportFragmentManager(), fragments, tabList);
        viewpager.setAdapter(adapter);
    }

    private void initTabs() {
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_FIXED); //固定模式
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
        final int dp18 = CommonUtil.dip2px(content,18);
/*        //中间加分隔线
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ViewGroup view = (ViewGroup) viewGroup.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            TextView textView = (TextView) view.getChildAt(1);
            textView.setTextSize(dp16);
            textView.setBackgroundColor(Color.WHITE);
            textView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
            int screenWidth = ImageUtil.getScreenWidth(content);
            layoutParams.width=screenWidth/2;
            layoutParams.weight=1;
            layoutParams.setMargins(0, 0, 2, 0);
            textView.setLayoutParams(layoutParams);
        }*/
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("666", "onTabSelected: " + position);
                ViewGroup view = (ViewGroup) viewGroup.getChildAt(position);
                TextView textView = (TextView) view.getChildAt(1);
                textView.setTextSize(dp18);
                Paint paint = textView.getPaint();
                paint.setAntiAlias(true);//抗锯齿
                paint.setUnderlineText(true);
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("666", "onTabSelected: " + position);
                ViewGroup view = (ViewGroup) viewGroup.getChildAt(position);
                TextView textView = (TextView) view.getChildAt(1);
                textView.setTextSize(dp18);
                Paint paint = textView.getPaint();
                paint.setAntiAlias(true);//抗锯齿
                paint.setUnderlineText(false);
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        float height = 400f;
        if (t < height) {
            float alpha = (height - t) / height;
            if (t < 4) {
                rl_top.setBackgroundResource(R.color.transparent);
                rl_top.setAlpha(1f);
                leftBt.setText("");
            } else {
                rl_top.setAlpha(1 - alpha);
                int color = 1 - alpha > 0 ? R.color.colorPrimary : R.color.transparent;
                rl_top.setBackgroundResource(color);
                leftBt.setText("游戏名字");
            }
        } else {
            rl_top.setAlpha(1f);
            leftBt.setText("游戏名字");
            rl_top.setBackgroundResource(R.color.colorPrimary);
        }
        scrollView.setStickTop(rl_top.getMeasuredHeight());//设置距离多少悬浮
    }

    @Override
    public void changeLayoutHeight(int height) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        layoutParams.height = height;
        viewpager.setLayoutParams(layoutParams);
    }
}
