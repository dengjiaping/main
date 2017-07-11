package cn.ngame.store.user.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.view.BaseTitleBar;

/**
 * 找回密码界面
 * Created by zeng on 2016/5/17.
 */
public class FindPwdActivity extends BaseFgActivity {

    public static final String TAG = FindPwdActivity.class.getSimpleName();

    private Button bt_find_pwd;
    private ImageButton bt_show_pwd;
    private TextView tv_captcha;
    private EditText et_name, et_captcha, et_pwd;

    private boolean isShowPwd = false;

    private Handler handler = new Handler();

    private static final int WAIT_TIME = 130;
    private int second = 120;

    /**
     * 执行倒计时操作
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for(int i=0;i<WAIT_TIME;i++){
                if (second <= 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText(getResources().getString(R.string.register_get_captcha));
                            tv_captcha.setClickable(true);
                            return;
                        }
                    });
                } else {
                    second--;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_captcha.setText(second + "s");
                            tv_captcha.setClickable(false);
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_findpwd);

        BaseTitleBar titleBar = (BaseTitleBar) findViewById(R.id.title_bar);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_find_pwd = (Button) findViewById(R.id.bt_find_pwd);
        et_name = (EditText) findViewById(R.id.et_login_user);
        et_captcha = (EditText) findViewById(R.id.et_captcha);
        bt_show_pwd = (ImageButton) findViewById(R.id.bt_show_pwd);
        et_pwd = (EditText) findViewById(R.id.et_login_pwd);

        tv_captcha = (TextView) findViewById(R.id.tv_captcha);
        tv_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = et_name.getText().toString();

                if (mobile != null && !"".equals(mobile)) {
                    if(!TextUtil.isMobile(mobile)){
                        Toast.makeText(FindPwdActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getCaptcha(mobile);
                } else {
                    Toast.makeText(FindPwdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_find_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = et_name.getText().toString();
                String pwd = et_pwd.getText().toString();
                String captcha = et_captcha.getText().toString();

                if (userName == null || userName.length() <= 0) {
                    Toast.makeText(FindPwdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtil.isMobile(userName)){
                    Toast.makeText(FindPwdActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (captcha == null || captcha.length() <= 0) {
                    Toast.makeText(FindPwdActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd == null || pwd.length() <= 0) {
                    Toast.makeText(FindPwdActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                doFindPwd(userName, pwd, captcha);
            }
        });

        bt_show_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShowPwd){
                    isShowPwd = true;
                    bt_show_pwd.setSelected(true);
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }else {
                    isShowPwd = false;
                    bt_show_pwd.setSelected(false);
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_pwd.setSelection(et_pwd.getText().length());
                }
            }
        });

    }

    /**
     * 获取手机验证码
     */
    private void getCaptcha(final String mobile) {

        String url = Constant.WEB_SITE + Constant.URL_FIND_CAPTCHA;
        Response.Listener<JsonResult<Object>> successListener = new Response.Listener<JsonResult<Object>>() {
            @Override
            public void onResponse(JsonResult<Object> result) {

                if (result == null) {
                    Toast.makeText(FindPwdActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {

                    second = 120;
                    new Thread(runnable).start();

                    Toast.makeText(FindPwdActivity.this, "验证码已发送，请查收！", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "找回失败：服务端错误：" + result.msg);
                    showDialog(false, result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(FindPwdActivity.this, "找回失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Object>> versionRequest = new GsonRequest<JsonResult<Object>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Object>>() {}.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", mobile);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 处理HTTP找回密码操作
     */
    private void doFindPwd(final String userName, final String pwd, final String captcha) {

        String url = Constant.WEB_SITE + Constant.URL_FIND_PWD;
        Response.Listener<JsonResult<String>> successListener = new Response.Listener<JsonResult<String>>() {
            @Override
            public void onResponse(JsonResult<String> result) {

                if (result == null) {
                    Toast.makeText(FindPwdActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.code == 0) {

                    SharedPreferences preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.CONFIG_USER_NAME, userName);
                    editor.commit();

                    showDialog(true,"密码已重置，请重新登录！");
                } else {

                    showDialog(false,result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(FindPwdActivity.this, "更新失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<String>> versionRequest = new GsonRequest<JsonResult<String>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<String>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //设置POST请求参数
                Map<String, String> params = new HashMap<>();
                params.put("mobileNumber", userName);
                params.put("newPassword", pwd);
                params.put("smsCode", captcha);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 显示注册结果对话框
     */
    private void showDialog(final boolean isSuccess, String msg) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setTitle("提示框");
        dialogFragment.setDialogWidth(250);

        TextView tv = new TextView(FindPwdActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 20, 0, 0);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(msg);
        tv.setTextColor(getResources().getColor(R.color.black));
        dialogFragment.setContentView(tv);

        int stringId;
        if (isSuccess) {
            stringId = R.string.login;
        } else {
            stringId = R.string.sure;
        }

        dialogFragment.setNegativeButton(stringId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                if (isSuccess) {
                    Intent intent = new Intent(FindPwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialogFragment.show(ft,"successDialog");
    }
}
