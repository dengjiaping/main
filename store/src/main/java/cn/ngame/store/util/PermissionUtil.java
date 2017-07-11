package cn.ngame.store.util;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by gp on 16/10/8.
 * <p>
 * Build.VERSION.SDK_INT>=23
 */

public class PermissionUtil {

    private PermissionUtil() {
        throw new IllegalArgumentException("工具类不能实例化");
    }

    public static void requestPermison(Activity activity, String[] permissionList, int requestCode) {
        if (permissionList == null || permissionList.length == 0) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissionList, requestCode);
    }
}
