package cn.ngame.store.game.view;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DCViewPagerAdapter;
import cn.ngame.store.adapter.ProgressBarStateListener;
import cn.ngame.store.bean.GameAgent;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.GameType;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.AutoHeightViewPager;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.ReviewScoreView;
import cn.ngame.store.view.StickyScrollView;

import static cn.ngame.store.R.id.sdv_img;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class GameDetailActivity extends BaseFgActivity implements StickyScrollView.OnScrollChangedListener,
        HomeFragmentChangeLayoutListener {
    private RelativeLayout rl_top, rl_top2;
    private StickyScrollView scrollView;
    private SimpleDraweeView game_big_img;
    private Button leftBt;
    private GameDetailActivity content;
    private TabLayout tablayout;
    private AutoHeightViewPager viewpager;
    private ArrayList<Fragment> fragments;
    private DCViewPagerAdapter adapter;
    List<String> tabList = new ArrayList<>();
    //游戏id
    private long gameId = 0;
    private GameInfo gameInfo;
    //下载进度条
    private GameLoadProgressBar progressBar;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private IFileLoad fileLoad;
    private Paint paint;
    private SimpleDraweeView game_logo_img;
    private TextView gameNameTv;
    private TextView downLoadCountTv;
    private String gameName = "";
    private TextView changShangTv;
    private TextView gameSizeTv;
    private ImageView likeIv;
    private TextView feedbackTv;
    private TextView percentageTv;
    private ReviewScoreView reviewScoreView;
    private float tabTextSize;
    private RatingBar ratingBarBig;
    private float rate;
    private TextView sumbitTv;
    private boolean isPrecented = false;
    private FragmentManager fm;
    private TextView gameType0, gameType1, gameType2, gameType3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //........ ....................通知栏...................
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
        }
        //-----------------------------------------------------------------------------

        setContentView(R.layout.activity_game_detail);
        try {
            gameId = getIntent().getLongExtra(KeyConstant.ID, 0L);
        } catch (Exception e) {
        }
        content = this;
        fm = getSupportFragmentManager();
        tabTextSize = CommonUtil.dip2px(content, 16f);

        //初始化
        initStatus();
        initTabViewPager();
        initView();
        //请求数据
        getGameInfo();
    }

    //初始化其他控件
    private void initView() {
        game_big_img = (SimpleDraweeView) findViewById(sdv_img);
        game_logo_img = (SimpleDraweeView) findViewById(R.id.img_1);
        gameNameTv = (TextView) findViewById(R.id.tv_title);//游戏名字
        downLoadCountTv = (TextView) findViewById(R.id.download_count_tv);//下载次数
        changShangTv = (TextView) findViewById(R.id.game_chang_shang_tv);//厂商
        gameSizeTv = (TextView) findViewById(R.id.game_detail_size);//大小
        feedbackTv = (TextView) findViewById(R.id.game_detail_feedback_bt);//大小
        percentageTv = (TextView) findViewById(R.id.game_detail_percentage_tv);
        gameType0 = (TextView) findViewById(R.id.game_detail_type0);
        gameType1 = (TextView) findViewById(R.id.game_detail_type1);
        gameType2 = (TextView) findViewById(R.id.game_detail_type2);
        gameType3 = (TextView) findViewById(R.id.game_detail_type3);
        likeIv = (ImageView) findViewById(R.id.game_detail_like_iv);//喜欢按钮
        progressBar = (GameLoadProgressBar) findViewById(R.id.game_detail_progress_bar);//下载按钮

        likeIv.setOnClickListener(gameDetailClickListener);
        feedbackTv.setOnClickListener(gameDetailClickListener);
        percentageTv.setOnClickListener(gameDetailClickListener);
    }

    //设置数据
    private void setView() {
        if (gameInfo == null) {
            return;
        }
        gameName = gameInfo.gameName;
        gameNameTv.setText(gameName);//名字
        //类型
        List<GameType> typeList = gameInfo.gameTypeList;
        if (typeList != null) {
            int typeSize = typeList.size();
            if (typeSize >= 0) {
                gameType0.setText(typeList.get(0).typeName);
                gameType0.setVisibility(View.VISIBLE);
            }
            if (typeSize >= 1) {
                gameType1.setText(typeList.get(1).typeName);
                gameType1.setVisibility(View.VISIBLE);
            }
            if (typeSize >= 2) {
                gameType2.setText(typeList.get(2).typeName);
                gameType2.setVisibility(View.VISIBLE);
            }
            if (typeSize >= 3) {
                gameType3.setText(typeList.get(3).typeName);
                gameType3.setVisibility(View.VISIBLE);
            }
        }
        gameSizeTv.setText(Formatter.formatFileSize(content, gameInfo.gameSize));//大小
        downLoadCountTv.setText(gameInfo.downloadCount + "");//下载次数
        percentageTv.setText(gameInfo.percentage + "");//评分0

        game_logo_img.setImageURI(gameInfo.gameLogo);//游戏 -头像
        game_big_img.setImageURI(gameInfo.gameLogo);//游戏 -大图

        //厂商
        List<GameAgent> gameAgentList = gameInfo.gameAgentList;
        if (gameAgentList != null && gameAgentList.size() > 0) {
            changShangTv.setText(gameAgentList.get(0).agentName);
        } else {
            changShangTv.setText("");
        }

        //更新下载按钮
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameFileStatus fileStatus = fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink,
                                gameInfo.packages, ConvUtil.NI(gameInfo.versionCode));
                        progressBar.setLoadState(fileStatus);
                    }
                });
            }
        }, 0, 500);

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
                    progressBar.setLoadState(fileLoad.getGameFileLoadStatus(gameInfo.filename, gameInfo.gameLink, gameInfo
                            .packages, ConvUtil.NI(gameInfo.versionCode)));
                    //必须设置，否则点击进度条后无法进行响应操作
                    FileLoadInfo fileLoadInfo = new FileLoadInfo(gameInfo.filename, gameInfo.gameLink, gameInfo.md5, ConvUtil
                            .NI(gameInfo.versionCode), gameInfo.gameName, gameInfo.gameLogo, gameInfo.id, FileLoadInfo.TYPE_GAME);
                    progressBar.setFileLoadInfo(fileLoadInfo);
                    fileLoadInfo.setPackageName(gameInfo.packages);
                    fileLoadInfo.setVersionCode(ConvUtil.NI(gameInfo.versionCode));
                    progressBar.setOnStateChangeListener(new ProgressBarStateListener(GameDetailActivity.this,
                            GameDetailActivity.this.getSupportFragmentManager()));
                    progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.toggle();
                        }
                    });

                    //设置ViewPager
                    fragments = new ArrayList<>();
                    fragments.add(GameDetailFragment.newInstance(gameInfo));
                    fragments.add(GameReadFragment.newInstance(gameInfo));

                    adapter.setList(fragments, tabList);
                    viewpager.setAdapter(adapter);

                    setView();
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
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
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(GameDetailFragment.newInstance(gameInfo));
        fragments.add(GameReadFragment.newInstance(gameInfo));
        adapter = new DCViewPagerAdapter(fm, fragments, tabList);//getChildFragmentManager()
        viewpager.setAdapter(adapter);
    }

    private void initTabs() {
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_FIXED); //固定模式
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
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
                ViewGroup view = (ViewGroup) viewGroup.getChildAt(position);
                TextView textView = (TextView) view.getChildAt(1);
                Paint paint = textView.getPaint();
                //paint.setTextSize(tabTextSize);
                paint.setAntiAlias(true);//抗锯齿
                paint.setUnderlineText(true);
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                ViewGroup view = (ViewGroup) viewGroup.getChildAt(position);
                TextView textView = (TextView) view.getChildAt(1);
                Paint paint = textView.getPaint();
                //paint.setTextSize(tabTextSize);
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
                int color = 1 - alpha > 0 ? R.color.mainColor : R.color.transparent;
                rl_top.setBackgroundResource(color);
                leftBt.setText(gameName);
            }
        } else {
            rl_top.setAlpha(1f);
            leftBt.setText(gameName);
            rl_top.setBackgroundResource(R.color.mainColor);
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


    private void initStatus() {
        //获取状态栏高度设置给标题栏==========================================
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        rl_top.setBackgroundResource(R.color.transparent);
        int statusBarHeight = ImageUtil.getStatusBarHeight(content);
        rl_top.setPadding(0, statusBarHeight, 0, 0);
        //======================================================================
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

    //初始化TabLayout   &   ViewPager
    private void initTabViewPager() {
        tabList.add("详情");
        tabList.add("必读");
        scrollView = (StickyScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollListener(this);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(2);
        initViewPager();
        initTabs();
    }

    private View.OnClickListener gameDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.game_detail_like_iv:
                    if (CommonUtil.isLogined()) {
                        //已登录,评分框
                        boolean isLiked = likeIv.isSelected();
                        ToastUtil.show(content, isLiked ? "取消成功" : "收藏成功,可在管理界面中查看");
                        likeIv.setSelected(!isLiked);
                    } else {//未登录
                        CommonUtil.showUnLoginDialog(fm, content,R.string.unlogin_msg);
                    }

                    break;
                case R.id.game_detail_feedback_bt:
                    if (CommonUtil.isLogined()) {
                        ToastUtil.show(content, "反馈成功");
                    } else {
                        CommonUtil.showUnLoginDialog(fm,content, R.string.unlogin_msg);
                    }
                    break;
                case R.id.game_detail_percentage_tv:
                    if (CommonUtil.isLogined()) {
                        //已登录,评分框
                        showPercentDialog();
                    } else {//未登录
                        CommonUtil.showUnLoginDialog(fm, content,R.string.unlogin_msg);
                    }
                    break;
            }
        }
    };

    //提示绑定对话框
    public void showPercentDialog() {
        final Dialog mUnboundDialog = new Dialog(content);
        mUnboundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //填充对话框的布局
        View percentView = LayoutInflater.from(content).inflate(R.layout.layout_percentage_dialog, null);

        //用户头像+昵称
        SimpleDraweeView iconIv = (SimpleDraweeView) percentView.findViewById(R.id.ic_percent_icon);
        TextView nameTv = (TextView) percentView.findViewById(R.id.ic_percent_user_name);
        iconIv.setImageURI(StoreApplication.userHeadUrl);
        nameTv.setText(StoreApplication.nickName);

        reviewScoreView = (ReviewScoreView) percentView.findViewById(R.id.review_scoreView);
        ratingBarBig = (RatingBar) percentView.findViewById(R.id.rating_bar);
        reviewScoreView.setData(gameInfo);

        sumbitTv = (TextView) percentView.findViewById(R.id.ic_percent_sumbit_bt);
        sumbitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取评分的值
                rate = ratingBarBig.getRating();
                if (rate <= 0) {
                    ToastUtil.show(content, "请点击星星打分哦!");
                    return;
                }
                //评分成功
                ToastUtil.show(content, "评分提交成功!");
                isPrecented = true;
                mUnboundDialog.dismiss();
                //提交评分
                //submitPercent(mUnboundDialog);
            }
        });
        if (isPrecented) {
            sumbitTv.setText(getString(R.string.precented));
            sumbitTv.setClickable(false);
            sumbitTv.setBackgroundResource(R.drawable.shape_corner12px_cccccc);
        }
        mUnboundDialog.setContentView(percentView);//将布局设置给Dialog
        Window dialogWindow = mUnboundDialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        //dialogWindow.setGravity(Gravity.CENTER);//设置Dialog从窗体顶部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.dm445);
        dialogWindow.setAttributes(params); //将属性设置给窗体
        if (content != null && !mUnboundDialog.isShowing()) {
            mUnboundDialog.show();//显示对话框
        }
    }

    //评论
    private void submitPercent(final Dialog mUnboundDialog) {
        String url = Constant.WEB_SITE + Constant.URL_COMMENT_ADD_COMMENT;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    ToastUtil.show(content, "评分提交失败,服务端异常");
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "评分提交成功!");
                    if (content != null && mUnboundDialog != null && mUnboundDialog.isShowing()) {
                        mUnboundDialog.dismiss();
                    }
                } else {
                    ToastUtil.show(content, result.msg);
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(content, "评论失败，请稍候重试!");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, "gameId");
                params.put(KeyConstant.VALUE, (int) Math.floor(rate) + ""); //评分值
                params.put(KeyConstant.TOKEN, StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
        }
    }
}
