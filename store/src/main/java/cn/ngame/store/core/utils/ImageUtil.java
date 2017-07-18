package cn.ngame.store.core.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Base64;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取图片的Base64字符串
 * Created by zeng on 2016/6/20.
 */
public class ImageUtil {

    private static float mRatio;
    private static int mScreenWidth;
    private static int mScreenHeight;
    private static float scale;

    public static String getImageStr(File imgFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            try {
                in = new FileInputStream(imgFile);
                data = new byte[in.available()];
                in.read(data);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = new String(Base64.encode(data, Base64.DEFAULT));
        return str;
    }

    public static int getScreenWidth(Activity activity) {
        if (mScreenWidth == 0) {
            initialScreenParams(activity);
        }
        return mScreenWidth;
    }

    public static int getScreenHeight(Activity activity) {
        if (mScreenHeight == 0) {
            initialScreenParams(activity);
        }
        return mScreenHeight;
    }

    public static void initialScreenParams(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - statusBarHeight;
        mRatio = mScreenWidth / (float) mScreenHeight;
        scale = activity.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //直接调用此方法即可实现对状态栏的控制；
}
