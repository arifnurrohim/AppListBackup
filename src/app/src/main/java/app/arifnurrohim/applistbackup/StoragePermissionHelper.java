package app.arifnurrohim.applistbackup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

/* JADX INFO: loaded from: classes.dex */
public final class StoragePermissionHelper {
    public static void ensure(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT < 30 || Environment.isExternalStorageManager()) {
                return;
            }
            Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        } catch (Throwable unused) {
        }
    }
}
