package com.example.llj32.enjoymusic.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {
    public static boolean isGranted(Context context, String permission) {
        return !isMarshmallow() || isGrantedPermission(context, permission);
    }

//    public static void reqPermission(Activity context, String permission, int reqCode) {
//        if (!isGranted(context, permission)) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
//
//            } else {
//                ActivityCompat.requestPermissions(context, new String[]{permission}, reqCode);
//            }
//        }
//    }

    public static boolean isGrantedPermission(Context context, String permission) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(context, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
