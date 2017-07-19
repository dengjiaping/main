package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.local.model.IWatchRecordModel;
import cn.ngame.store.local.model.WatchRecordModel;

/**
 * 用户登录界面
 * Created by zeng on 2016/5/12.
 */
public class LoginActivity extends BaseFgActivity implements View.OnClickListener {

    public static final String TAG = "777";

    private EditText et_user, et_pwd;
    private String userName, password;
    private TextView bt_find_pwd, bt_register;
    private Button bt_login;
    private ImageButton bt_show_pwd;

    private boolean isShowPwd = false;

    private SharedPreferences preferences;
    private ImageView deleteIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        View.SYSTEM_UI_FLAG_FULLSCREEN,   //全屏，状态栏和导航栏不显示
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, //隐藏导航栏
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, //全屏，状态栏会盖在布局上
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE,
                View.SYSTEM_UI_FLAG_LOW_PROFILE,
                View.SYSTEM_UI_FLAG_VISIBLE,  //显示状态栏和导航栏*/
/*
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        }

        this.setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        //获取状态栏高度设置给标题栏
        RelativeLayout titleRlay = (RelativeLayout) findViewById(R.id.title_rlay);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                titleRlay.getLayoutParams());
        int statusBarHeight = ImageUtil.getStatusBarHeight(this);
        layoutParams.setMargins(0, statusBarHeight, 0, 0);
        titleRlay.setLayoutParams(layoutParams);

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_user = (EditText) findViewById(R.id.et_login_user);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        bt_show_pwd.setOnClickListener(this);
        bt_find_pwd = (TextView) findViewById(R.id.tv_find_pwd);
        bt_find_pwd.setOnClickListener(this);
        bt_register = (TextView) findViewById(R.id.tv_register);
        deleteIv = (ImageView) findViewById(R.id.delete_iv);
        deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_user.setText("");
            }
        });
        bt_register.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.but_login);
        bt_login.setOnClickListener(this);
        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteIv.setVisibility(View.VISIBLE);
                } else {
                    deleteIv.setVisibility(View.GONE);
                }
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userName = preferences.getString(Constant.CONFIG_USER_NAME, "");
        if (!userName.equals("")) {
            et_user.setText(userName);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_show_pwd:
                if (!isShowPwd) {
                    isShowPwd = true;
                    bt_show_pwd.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                } else {
                    isShowPwd = false;
                    bt_show_pwd.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }
                break;
            case R.id.but_login:
                doLogin();
                break;
            case R.id.tv_find_pwd:

                Intent fIntent = new Intent(this, FindPwdActivity.class);
                startActivity(fIntent);

                break;
            case R.id.tv_register:

                Intent rIntent = new Intent(this, RegisterActivity.class);
                startActivity(rIntent);
                break;
        }
    }

    /**
     * 执行HTTP登录操作
     */
    private void doLogin() {
        userName = et_user.getText().toString();
        password = et_pwd.getText().toString();

        if (userName == null || "".equals(userName)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtil.isMobile(userName)) {
            Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null || "".equals(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogHelper.showWaiting(getSupportFragmentManager(), "登录中...");
        String url = Constant.WEB_SITE + Constant.URL_USER_LOGIN;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {
                if (result == null) {
                    Toast.makeText(LoginActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {
                    Token token = result.data;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_NAME, userName);
                    editor.putString(Constant.CONFIG_USER_PWD, password);
                    editor.apply();

                    StoreApplication.token = token.token;
                    StoreApplication.passWord = password;

                    //加载用户信息

                    editor.putString(Constant.CONFIG_USER_HEAD, token.headPhoto);
                    editor.putString(Constant.CONFIG_NICK_NAME, token.nickName);
                    editor.apply();

                    //加载用户头像
                    StoreApplication.userHeadUrl = token.headPhoto;
                    //StoreApplication.nickName = token.nickName;
                    StoreApplication.nickName = token.nickName;
                    StoreApplication.userCode = token.userCode;

//                    //跳转到用户中心
//                    Intent intent = new Intent(LoginActivity.this,UserCenterActivity.class);
//                    startActivity(intent);
                    LoginActivity.this.finish();

                    //同步本地观看记录到服务器
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IWatchRecordModel watchRecordModel = new WatchRecordModel(LoginActivity.this);
                            watchRecordModel.synchronizeWatchRecord();
                        }
                    }).start();


                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(LoginActivity.this, "登录失败，" + result.msg, Toast.LENGTH_SHORT).show();
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
            }
        };
        Response.Listener<JsonResult<User>> succesListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                if (result == null) {
                    Toast.makeText(LoginActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    User user = result.data;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_NAME, userName);
                    editor.putString(Constant.CONFIG_USER_PWD, password);
                    editor.apply();

                    StoreApplication.token = user.token;
                    StoreApplication.passWord = password;
                    android.util.Log.d(TAG, "user返回: " + user.token);

                    //加载用户信息

                    editor.putString(Constant.CONFIG_USER_HEAD, user.headPhoto);
                    editor.putString(Constant.CONFIG_NICK_NAME, user.nickName);
                    editor.apply();

                    //加载用户头像
                    StoreApplication.userHeadUrl = user.headPhoto;
                    //StoreApplication.nickName = token.nickName;
                    StoreApplication.nickName = user.nickName;
                    StoreApplication.userCode = user.userCode;

//                    //跳转到用户中心
//                    Intent intent = new Intent(LoginActivity.this,UserCenterActivity.class);
//                    startActivity(intent);
                    LoginActivity.this.finish();

                    //同步本地观看记录到服务器
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IWatchRecordModel watchRecordModel = new WatchRecordModel(LoginActivity.this);
                            watchRecordModel.synchronizeWatchRecord();
                        }
                    }).start();


                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(LoginActivity.this, "登录失败，" + result.msg, Toast.LENGTH_SHORT).show();
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(LoginActivity.this, "登录失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                DialogHelper.hideWaiting(getSupportFragmentManager());
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.NICK_NAME, nicknameStr);
                params.put(KeyConstant.LOGIN_NAME, userName);
                Log.d(TAG, "参数: " + userName);
                params.put(KeyConstant.pass_word, password);
                params.put(KeyConstant.TYPE, LOGIN_TYPE); //（1手机，2QQ，3微信，4新浪微博）
                params.put(KeyConstant.HEAD_PHOTO, URL_HEAD_PHOTO);  //头像
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0);  //
                return params;
            }
        };
      /*  Request<JsonResult<User>> versionRequest1 = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                succesListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.NICK_NAME, nicknameStr);
                params.put(KeyConstant.LOGIN_NAME, userName);
                Log.d(TAG, "参数: " + userName);
                params.put(KeyConstant.pass_word, password);
                params.put(KeyConstant.TYPE, LOGIN_TYPE); //（1手机，2QQ，3微信，4新浪微博）
                params.put(KeyConstant.HEAD_PHOTO, URL_HEAD_PHOTO);  //头像
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0);  //
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest1);*/
        StoreApplication.requestQueue.add(versionRequest);

    }

    private String LOGIN_TYPE = "1";
    private String nicknameStr = "";
    private String URL_HEAD_PHOTO = "";

    /**
     * 获取用户信息
     */
   /* private void getUserInfo() {

        final String url = Constant.WEB_SITE + Constant.URL_USER_INFO;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    Toast.makeText(LoginActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {

                    User user = result.data;
                    StoreApplication.user = user;

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_HEAD, user.headPhoto);
                    editor.putString(Constant.CONFIG_NICK_NAME, user.nickName);
                    editor.apply();

                    //加载用户头像
                    StoreApplication.userHeadUrl = user.headPhoto;
                    StoreApplication.nickName = user.nickName;

//                    //跳转到用户中心
//                    Intent intent = new Intent(LoginActivity.this,UserCenterActivity.class);
//                    startActivity(intent);
                    LoginActivity.this.finish();

                    //同步本地观看记录到服务器
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IWatchRecordModel watchRecordModel = new WatchRecordModel(LoginActivity.this);
                            watchRecordModel.synchronizeWatchRecord();
                        }
                    }).start();

                    finish();

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(LoginActivity.this, "服务端异常，请重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                Toast.makeText(LoginActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }*/

}
