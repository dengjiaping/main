package cn.ngame.store.user.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.UpLoadBean;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.util.ToastUtil;


/**
 * 用户中心界面
 * Created by zeng on 2016/5/26.
 */
public class UserCenterActivity extends BaseFgActivity {

    public static final String TAG = "777";
    private String pwd;

    private ImageView img_photo;
    private TextView tv_account;
    private EditText tv_nickname;

    private String nickName;

    private SharedPreferences preferences;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 1458;

    private String mCurrentPhotoPath;

    private File mTempDir;
    private User user;
    private String imgStrPost;
    private String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_center);

        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);

        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setText("个人资料设置");

        try {
            mTempDir = new File(CommonUtil.getImageBasePath());
        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
        if (mTempDir != null && !mTempDir.exists()) {
            mTempDir.mkdirs();
        }
        img_photo = (ImageView) findViewById(R.id.img_photo);
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_nickname = (EditText) findViewById(R.id.tv_nickname);

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改头像
                showChangeAvatarDialog();
            }
        });
        user = StoreApplication.user;
        pwd = StoreApplication.passWord;
        imgStrPost = StoreApplication.userHeadUrl;
        android.util.Log.d(TAG, "imgStrPost: "+imgStrPost);
        setUserInfo();
     /*   if (pwd == null) {
            //getUserInfo();
            android.util.Log.d(TAG, "user == null");
            LoginHelper loginHelper = new LoginHelper(UserCenterActivity.this);
            loginHelper.reLogin();
            //setUserInfo();
        } else {
            setUserInfo();
        }*/


        //*/重新登录
     /*   LoginHelper loginHelper = new LoginHelper(this);
        loginHelper.reLogin();*/
        TextView titleRightBt = (TextView) findViewById(R.id.title_right_tv);
        titleRightBt.setVisibility(View.VISIBLE);
        titleRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickNameStr = tv_nickname.getText().toString();
                if (nickNameStr.length() == 0) {
                    ToastUtil.show(UserCenterActivity.this, "昵称为空哦！");
                    return;
                }
                nickName = nickNameStr;
                uploadImage();
            }
        });
    }

    //修改头像
    public void showChangeAvatarDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_change_avatar, null);

        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                int id = v.getId();
                if (id == R.id.choose_local_tv) {//本地相册
                    Crop.pickImage(UserCenterActivity.this);
                } else if (id == R.id.choose_camera_tv) {//相机
                    getImageFromCamera();
                } else if (id == R.id.choose_recomend_tv) {//选择推荐头像
                }
            }
        };
        inflate.findViewById(R.id.choose_local_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_recomend_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_camera_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

        dialog.setContentView(inflate);//将布局设置给Dialog
        setDialogWindow(dialog);
    }

    //修改头像
    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_logout, null);

        inflate.findViewById(R.id.logout_yes_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                logout();
            }
        });
        inflate.findViewById(R.id.logout_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
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

    protected void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf(System.currentTimeMillis());
        File cropFile = new File(mTempDir, fileName);
        Uri fileUri = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        mCurrentPhotoPath = fileUri.getPath();
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK) {
                beginCrop(result.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMERA) {
                if (mCurrentPhotoPath != null) {
                    beginCrop(Uri.fromFile(new File(mCurrentPhotoPath)));
                }
            }
        }
    }

    private void beginCrop(Uri source) {
        String fileName = "Temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File cropFile = new File(mTempDir, fileName);
        Uri outputUri = Uri.fromFile(cropFile);
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.color.transparent, 360);

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            //StoreService.uploadImage(file);
            avatarUrl = uri.toString();

            imageLoader.displayImage(avatarUrl, img_photo, roundOptions);

            // todo 上传图片

            String path = uri.getPath();
            File file = new File(path);
            imgStrPost = ImageUtil.getImageStr(file);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImage() {
        DialogHelper.showWaiting(getSupportFragmentManager(), "正在上传图片...");
        String url = Constant.WEB_SITE + Constant.URL_MODIFY_USER_DATA;
        Response.Listener<JsonResult<UpLoadBean>> successListener =
                new Response.Listener<JsonResult<UpLoadBean>>() {
                    @Override
                    public void onResponse(JsonResult<UpLoadBean> result) {
                        if (result.code == 0) {
                            StoreApplication.userHeadUrl = avatarUrl;
                            StoreApplication.nickName = nickName;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Constant.CONFIG_USER_HEAD, avatarUrl);
                            editor.putString(Constant.CONFIG_NICK_NAME, nickName);
                            editor.apply();

                            Toast.makeText(UserCenterActivity.this, "资料修改成功！", Toast.LENGTH_SHORT).show();
                            UserCenterActivity.this.finish();
                        } else {
                            Toast.makeText(UserCenterActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                        }
                        //隐藏提示框
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                    }
                };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                Toast.makeText(UserCenterActivity.this, "头像修改失败，网络连接错误！", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<JsonResult<UpLoadBean>> versionRequest = new GsonRequest<JsonResult<UpLoadBean>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.USER_CODE, user.userCode);
                params.put(KeyConstant.PICTURE_STR, imgStrPost);
                params.put(KeyConstant.GENDER, "男");
                params.put(KeyConstant.NICK_NAME, nickName);
                params.put(KeyConstant.TOKEN, user.token);

                android.util.Log.d(TAG, "userTOKEN" + user.token);
                android.util.Log.d(TAG, "userCode" + user.userCode);
                return params;
            }
        };
   /*     versionRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        StoreApplication.requestQueue.add(versionRequest);
    }

    ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 处理退出操作
     */
    public void onLogoutClick(View view) {
        showLogoutDialog();
    }

    //退出登录
    private void logout() {
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

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showChangeNicknameDialog() {

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();

        dialogFragment.setTitle("昵称修改");
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
                    if (nickName.length() > 13) {
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

    private void setUserInfo() {
        DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.drawable.ic_icon_title, 360);
        nickName = StoreApplication.nickName;
        imageLoader.displayImage(StoreApplication.userHeadUrl, img_photo, roundOptions);
        tv_nickname.setText(nickName);
        tv_nickname.setSelection(nickName.length());
        tv_account.setText(user.loginName);
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
                    user = user;
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
                    imageLoader.displayImage(StoreApplication.userHeadUrl, img_photo, roundOptions);

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
                    if (user != null) {
                        user.nickName = nickName;
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

    public void onPwdChangeClick(View view) {
        ToastUtil.show(UserCenterActivity.this, "修改密码");
    }
}
