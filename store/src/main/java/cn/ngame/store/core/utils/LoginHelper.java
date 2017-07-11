package cn.ngame.store.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;

/**
 * 用户登录辅助类
 * Created by zeng on 2016/7/26.
 */
public class LoginHelper {

    public static final String TAG = LoginHelper.class.getSimpleName();

    private Context context;
    private SharedPreferences preferences;
    private String userName;
    private String passWord;

    public LoginHelper(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Constant.CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        userName = preferences.getString(Constant.CONFIG_USER_NAME,"");
        passWord = preferences.getString(Constant.CONFIG_USER_PWD,"");
    }

    /**
     * 重新登录
     */
    public void reLogin(){

        String url = Constant.WEB_SITE + Constant.URL_USER_LOGIN;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {

            @Override
            public void onResponse(JsonResult<Token> result) {

                if (result == null) {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    return;
                }

                if (result.code == 0) {

                    Token token = result.data;
                    StoreApplication.token = token.token;
                    //Log.e("LoginHelper","--------------------------------------->>>> 刷新了token");
                    getUser();

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
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

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("passWord", passWord);
                params.put("terminalVersion", "1212121331313"); //设备ID
                params.put("terminalTypeCode", "3333444");  //系统版本号
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    private void getUser(){

        final String url = Constant.WEB_SITE + Constant.URL_USER_INFO;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {

                if (result == null) {
                    return;
                }
                if (result.code == 0 && result.data != null) {

                    User user = result.data;
                    StoreApplication.user = user;

                    //加载用户头像
                    StoreApplication.userHeadUrl = user.headPhoto;
                    StoreApplication.nickName = user.nickName;

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
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

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>( Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

}
