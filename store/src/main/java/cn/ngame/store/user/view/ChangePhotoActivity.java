package cn.ngame.store.user.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.view.PicassoImageView;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 用户修改头像的界面
 * Created by zeng on 2016/6/20.
 */
public class ChangePhotoActivity extends BaseFgActivity {

    public static final String TAG = ChangePhotoActivity.class.getSimpleName();

    private static final int REQUEST_CODE_CAPTURE_CAMERA = 1458;

    private SimpleTitleBar title_bar;
    private PicassoImageView img_photo;
    private LinearLayout lay_select, lay_photograph;
    private String mCurrentPhotoPath;

    private File mTempDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_photo);

        img_photo = (PicassoImageView) findViewById(R.id.img_photo);

        String headPhoto = getIntent().getStringExtra("imgUrl");
        if (headPhoto != null && headPhoto.length() > 0) {
            img_photo.setImageUrl(headPhoto,130f,130f,R.drawable.login_photo_big);
        }


        lay_select = (LinearLayout) findViewById(R.id.layout_1);
        lay_photograph = (LinearLayout) findViewById(R.id.layout_2);

        String url = getIntent().getStringExtra("url");
        if (url != null && url.length() > 0) {
            //StoreService.loadImage(url, this);
        }

        lay_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(ChangePhotoActivity.this);
            }
        });

        lay_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
            }
        });

        title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            mTempDir = new File(CommonUtil.getImageBasePath());
        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
        if (mTempDir != null && !mTempDir.exists()) {
            mTempDir.mkdirs();
        }
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

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            img_photo.setImageURI(uri);

            String path = uri.getPath();
            File file = new File(path);

            //StoreService.uploadImage(file); //上传图片
            uploadImage(file);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(File file){

        DialogHelper.showWaiting(getSupportFragmentManager(),"正在上传图片...");
        final String imgStr = ImageUtil.getImageStr(file);

        String url = Constant.WEB_SITE + Constant.URL_IMAGE_UPLOAD;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {
                if(result.code == 0){
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                    Toast.makeText(ChangePhotoActivity.this,"头像修改成功！",Toast.LENGTH_SHORT).show();
                }else {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                    Toast.makeText(ChangePhotoActivity.this,"头像修改失败！",Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                Toast.makeText(ChangePhotoActivity.this,"头像修改失败，网络连接错误！",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {}.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                params.put("pictureSuffix", "jpg");
                params.put("strImg", imgStr);
                return params;
            }
        };
        versionRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        StoreApplication.requestQueue.add(versionRequest);
    }

}
