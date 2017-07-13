package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.LoginHelper;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 用户中心界面
 * Created by zeng on 2016/5/26.
 */
public class UserCenterActivity extends BaseFgActivity {

    public static final String TAG = UserCenterActivity.class.getSimpleName();
    private User user;

    private PicassoImageView img_photo;
    private TextView tv_account, tv_nickname;

    private String nickName;

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_center);

        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);

        SimpleTitleBar title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
        RelativeLayout layout_2 = (RelativeLayout) findViewById(R.id.layout_2);

        img_photo = (PicassoImageView) findViewById(R.id.img_photo);
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);

        user = StoreApplication.user;

        if (user == null) {
            //getUserInfo();

            LoginHelper loginHelper = new LoginHelper(UserCenterActivity.this);
            loginHelper.reLogin();

        } else {

            nickName = user.nickName;
            if (nickName != null) {
                nickName = nickName.length() > 10 ? nickName.substring(0, 10) : nickName;
            }
            tv_nickname.setText(nickName);
            tv_account.setText(user.mobile);
            img_photo.setImageUrl(user.headPhoto, 50f, 50f, R.drawable.login_photo_big);

        }

        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCenterActivity.this, ChangePhotoActivity.class);
                if (user != null && user.headPhoto != null) {
                    intent.putExtra("imgUrl", user.headPhoto);
                } else {
                    intent.putExtra("imgUrl", "");
                }

                startActivity(intent);
            }
        });

        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //重新登录
        LoginHelper loginHelper = new LoginHelper(this);
        loginHelper.reLogin();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserInfo(); //重新加载用户信息
    }

    /**
     * 处理退出操作
     */
    public void onLogoutClick(View view) {

        SharedPreferences preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.CONFIG_USER_PWD, "");
        editor.apply();

        StoreApplication.userHeadUrl = "";
        StoreApplication.nickName = "";
        StoreApplication.userName = "";
        StoreApplication.passWord = "";
        StoreApplication.token = null;
        StoreApplication.user = null;

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();

        dialogFragment.setTitle("编辑");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_edit, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.et_content);

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });

        dialogFragment.setNegativeButton(R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName = editText.getText().toString();
                if (nickName.length() > 0) {
                    if (nickName.length() > 10) {
                        Toast.makeText(UserCenterActivity.this, "亲，您的昵称太长了！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!TextUtil.isLegal(nickName.trim(), "[A-Za-z0-9\\u4e00-\\u9fa5_]+")) {
                        Toast.makeText(UserCenterActivity.this, "亲，只允许中文，英文，数字等字符哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogFragment.dismiss();
                    changeNickname();
                } else {
                    Toast.makeText(UserCenterActivity.this, "昵称不能为空哦！", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialogFragment.show(ft, "progressDialog");

    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        final String url = Constant.WEB_SITE + Constant.URL_USER_INFO;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {

                if (result == null) {
                    Toast.makeText(UserCenterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0 && result.data != null) {

                    user = result.data;
                    StoreApplication.user = user;
                    String userHeadPhoto = user.headPhoto;
                    if (userHeadPhoto != null && StoreApplication.userHeadUrl != null &&
                            !StoreApplication.userHeadUrl.equals(userHeadPhoto)) {
                        StoreApplication.userHeadUrl = userHeadPhoto;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constant.CONFIG_USER_HEAD, userHeadPhoto);
                        editor.apply();
                    } else {
                        StoreApplication.userHeadUrl = userHeadPhoto;
                    }

                    nickName = user.nickName;
                    if (nickName != null) {
                        nickName = nickName.length() > 10 ? nickName.substring(0, 10) : nickName;
                    }
                    tv_nickname.setText(nickName);
                    tv_account.setText(user.mobile);
                    img_photo.setImageUrl(user.headPhoto, 50f, 50f, R.drawable.login_photo_big);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(UserCenterActivity.this, "服务端异常，请重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(UserCenterActivity.this, "加载用户信息，请检查网络连接!", Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 修改昵称
     */
    private void changeNickname() {

        final String url = Constant.WEB_SITE + Constant.URL_CHANGE_NICKNAME;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    Toast.makeText(UserCenterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    if (nickName != null) {
                        nickName = nickName.length() > 10 ? nickName.substring(0, 10) : nickName;
                    }
                    tv_nickname.setText(nickName);
                    if (user != null) {
                        user.nickName = nickName;
                    }
                    if (StoreApplication.user != null) {
                        StoreApplication.user.nickName = nickName;
                        StoreApplication.nickName = nickName;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constant.CONFIG_NICK_NAME, nickName);
                        editor.apply();
                    }
                    Toast.makeText(UserCenterActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(UserCenterActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(UserCenterActivity.this, "修改失败，请检查网络连接!");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                params.put("userCode", user.userCode);
                params.put("newNickName", nickName);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }
}
