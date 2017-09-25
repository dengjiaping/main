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
import android.view.Gravity;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cn.ngame.store.bean.GameImage;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.UpLoadBean;
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
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.AutoHeightViewPager;
import cn.ngame.store.view.GameLoadProgressBar;
import cn.ngame.store.view.MarqueTextView;
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
    String[] tabList = {"详情", "必读"};
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
    private MarqueTextView gameNameTv;
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
            gameId = getIntent().getLongExtra(KeyConstant.ID, 0l);
            Log.d(TAG, "游戏详情" + gameId);
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
        gameNameTv = (MarqueTextView) findViewById(R.id.tv_title);//游戏名字
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

    private String TAG = GameDetailActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        hasStop = false;
    }

    //设置数据
    private void setView() {
        if (gameInfo == null) {
            return;
        }
        List<GameImage> imagesList = gameInfo.gameDetailsImages;
        for (GameImage gameImage : imagesList) {
            byte type = gameImage.type;
            if (3 == type) {
                game_big_img.setImageURI(gameImage.imageLink);//游戏 -大图
                break;
            }
        }
        gameName = gameInfo.gameName;
        gameNameTv.setText(gameName == null ? "" : gameName);//名字
        likeIv.setSelected(1 == gameInfo.isFavoriteGame ? true : false);
        //类型
        String typeName = gameInfo.cName;
        if (typeName != null) {
            if (typeName.contains("原生手柄")) {
                gameType0.setText("原生手柄");
                gameType0.setVisibility(View.VISIBLE);
            }
            if (typeName.contains("云适配")) {
                gameType0.setText("云适配");
                gameType0.setVisibility(View.VISIBLE);
            }
            if (typeName.contains("破解")) {
                gameType1.setText("破解");
                gameType1.setVisibility(View.VISIBLE);
            }
            if (typeName.contains("汉化")) {
                gameType2.setText("汉化");
                gameType2.setVisibility(View.VISIBLE);
            }
            if (typeName.contains("特色游戏")) {
                gameType3.setText("特色游戏");
                gameType3.setVisibility(View.VISIBLE);
            }

           /*
               int typeSize = typeList.size();
           if (typeSize > 0) {
                gameType0.setText(typeList.get(0).typeName);
                gameType0.setVisibility(View.VISIBLE);
            }
            if (typeSize > 1) {
                gameType1.setText(typeList.get(1).typeName);
                gameType1.setVisibility(View.VISIBLE);
            }
            if (typeSize > 2) {
                gameType2.setText(typeList.get(2).typeName);
                gameType2.setVisibility(View.VISIBLE);
            }
            if (typeSize > 3) {
                gameType3.setText(typeList.get(3).typeName);
                gameType3.setVisibility(View.VISIBLE);
            }*/
        }
        gameSizeTv.setText(Formatter.formatFileSize(content, gameInfo.gameSize));//大小
        downLoadCountTv.setText(gameInfo.downloadCount + "");//下载次数
        percentageTv.setText(gameInfo.percentage + "");//评分0
        game_logo_img.setImageURI(gameInfo.gameLogo);//游戏 -头像

        //厂商
        String gameAgent = gameInfo.gameAgent;
        changShangTv.setText(gameAgent == null ? "" : gameAgent);

        //更新下载按钮
        timer.cancel();
        timer = new Timer();
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

    boolean hasStop;

    /**
     * 获取游戏详情
     */
    private void getGameInfo() {
        String url = Constant.WEB_SITE + Constant.URL_GAME_DETAIL;
        Response.Listener<JsonResult<GameInfo>> successListener = new Response.Listener<JsonResult<GameInfo>>() {
            @Override
            public void onResponse(JsonResult<GameInfo> result) {
                Log.d(TAG, "服务器返回成功: " + df.format(new Date()) + result == null ? ",数据为空" : "," + result.code);
                if (result == null || result.code != 0) {
                    ToastUtil.show(content, getString(R.string.server_exception));
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
                    if (null != adapter && !hasStop) {
                        adapter.setList(fragments, tabList);
                        if (null != viewpager) {
                            viewpager.setAdapter(adapter);
                        }
                    }
                    Log.d(TAG, "服务器返回成功,有数据 " + df.format(new Date()));
                    setView();
                } else {
                    Log.d(TAG, "服务端返回错误: " + df.format(new Date()));
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "请求服务器失败: " + df.format(new Date()) + "," + volleyError);
            }
        };

        Request<JsonResult<GameInfo>> request = new GsonRequest<JsonResult<GameInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<GameInfo>>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        Log.d(TAG, "UseCode:"+StoreApplication.userCode + "访问服务器开始: " + df.format(new Date()));
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
      /*  tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d(TAG, "选中: " + position);
                ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
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
                Log.d(TAG, "未选中: " + tab.getPosition());
                int position = tab.getPosition();
                ViewGroup viewGroup = (ViewGroup) tablayout.getChildAt(0);
                ViewGroup view = (ViewGroup) viewGroup.getChildAt(position);
                TextView textView = (TextView) view.getChildAt(1);
                Paint paint = textView.getPaint();
                //paint.setTextSize(tabTextSize);
                paint.setAntiAlias(true);//抗锯齿
                paint.setUnderlineText(false);
                //viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
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
        int statusBarHeight = ImageUtil.getStatusBarHeight(content) - 5;
        rl_top.setPadding(0, statusBarHeight, 0, 0);
        //======================================================================
        leftBt = (Button) findViewById(R.id.left_bt);
        leftBt.setPadding(CommonUtil.dip2px(content, 18), statusBarHeight, 0, 0);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fileLoad = FileLoadManager.getInstance(this);
    }

    //初始化TabLayout   &   ViewPager
    private void initTabViewPager() {
        scrollView = (StickyScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollListener(this);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (AutoHeightViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(2);
        initViewPager();
        initTabs();
    }

    private void cancelFavorite() {
        String url = Constant.WEB_SITE + Constant.URL_DEL_FAVORITE;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    Toast.makeText(content, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    likeIv.setSelected(false);
                    ToastUtil.show(content, "收藏取消成功");
                } else {
                    ToastUtil.show(content, "收藏取消失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "取消喜欢失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","gameId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void addFavorite() {
        String url = Constant.WEB_SITE + Constant.URL_ADD_FAVORITE;
        Response.Listener<UpLoadBean> successListener = new Response.Listener<UpLoadBean>() {
            @Override
            public void onResponse(UpLoadBean result) {
                if (result == null) {
                    Toast.makeText(content, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.getCode() == 0) {
                    likeIv.setSelected(true);
                    ToastUtil.show(content, "收藏成功");
                } else {
                    ToastUtil.show(content, "收藏失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "添加喜欢失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<UpLoadBean> versionRequest = new GsonRequest<UpLoadBean>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<UpLoadBean>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","gameId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.TOKEN, StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    private int UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 1;
    private View.OnClickListener gameDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.game_detail_like_iv:
                    if (CommonUtil.isLogined()) {
                        if (!NetUtil.isNetworkConnected(content)) {
                            ToastUtil.show(content, getString(R.string.no_network));
                            return;
                        }
                        //已登录,评分框
                        boolean isLiked = likeIv.isSelected();
                        if (isLiked) {
                            //喜欢==>取消
                            cancelFavorite();
                        } else {//喜欢
                            addFavorite();
                        }

                    } else {//未登录
                        CommonUtil.showUnLoginDialog(fm, content, R.string.unlogin_msg);
                    }

                    break;
                case R.id.game_detail_feedback_bt:
                    if (CommonUtil.isLogined()) {
                      /*  QuickAction quickAction = new QuickAction(content, QuickAction.VERTICAL);
                        ActionItem pointItem = new ActionItem(1, "更新提醒");
                        ActionItem pointItem2 = new ActionItem(2, "下载异常");
                        quickAction.addActionItem(pointItem);
                        quickAction.addActionItem(pointItem2);

                        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

                            @Override
                            public void onItemClick(QuickAction source, int pos, int actionId) {
                                if (0 == pos) {//更新提醒
                                    //获取gameId
                                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 1;
                                } else if (1 == pos) {
                                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 2;
                                }
                                //提交反馈
                                if (gameInfo == null) {
                                    ToastUtil.show(content, "反馈失败,请稍后重试");
                                    return;
                                }
                                postFeedBack();
                                //取消弹出框
                                source.dismiss();
                            }
                        });
                        quickAction.show(feedbackTv);*/
                        showFeedbackDialog();
                    } else {
                        CommonUtil.showUnLoginDialog(fm, content, R.string.unlogin_msg);
                    }
                    break;
                case R.id.game_detail_percentage_tv:
                    if (CommonUtil.isLogined()) {
                        //已登录,评分框
                        showPercentDialog();
                    } else {//未登录
                        CommonUtil.showUnLoginDialog(fm, content, R.string.unlogin_msg);
                    }
                    break;
            }
        }
    };

    //反馈
    public void showFeedbackDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_feedback, null);

        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                int id = v.getId();
                if (id == R.id.choose_01_tv) {//游戏更新
                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 1;
                } else if (id == R.id.choose_02_tv) {//下载异常
                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 2;
                }
                //提交反馈
                if (gameInfo != null) {
                    postFeedBack();
                } else {
                    ToastUtil.show(content, "网络异常,请稍后重试.");
                }
            }
        };
        inflate.findViewById(R.id.choose_01_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_02_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

        dialog.setContentView(inflate);//将布局设置给Dialog
        setDialogWindow(dialog);
    }

    private void setDialogWindow(Dialog dialog) {
        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    //游戏反馈
    private void postFeedBack() {
        final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), content);
        dialogHelper.showAlert("提交反馈", true);
        String url = Constant.WEB_SITE + Constant.URL_SUBMIT_FEED_BACK_V4;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (dialogHelper != null) {
                    dialogHelper.hideAlert();
                }
                if (result == null) {
                    ToastUtil.show(content, "反馈失败,服务器异常,请稍后重试.");
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "反馈成功");
                } else {
                    ToastUtil.show(content, "反馈失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (dialogHelper != null) {
                    dialogHelper.hideAlert();
                }
                ToastUtil.show(content, "反馈失败," + getString(R.string.server_exception));
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","gameId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.gameVersion, gameInfo.versionName);
                params.put(KeyConstant.brand, android.os.Build.BRAND);//手机品牌
                params.put(KeyConstant.model, android.os.Build.MODEL);//手机型号
                params.put(KeyConstant.bluetooth, "");//蓝牙
                params.put(KeyConstant.system, android.os.Build.VERSION.RELEASE);//系统
                params.put(KeyConstant.cpu, android.os.Build.CPU_ABI);//蓝牙
                Log.d(TAG, "手机MODEL:" + android.os.Build.MODEL);
                Log.d(TAG, "手机.VERSION.RELEASE:" + android.os.Build.VERSION.RELEASE);
                Log.d(TAG, "手机BRAND:" + android.os.Build.BRAND);
                Log.d(TAG, "手机CPU_ABI:" + android.os.Build.CPU_ABI);
                params.put(KeyConstant.isSupportOTG, "0");//是否支持OTG（0表示不支持，1表示支持）
                params.put(KeyConstant.updateOrDownloadTips, String.valueOf(UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION));
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

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
        hasStop = true;
    }
}
