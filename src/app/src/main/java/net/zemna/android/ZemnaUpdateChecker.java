package net.zemna.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import net.zemna.android.app.DbAdapter;

/* JADX INFO: loaded from: classes.dex */
public final class ZemnaUpdateChecker {
    public static void CheckUpdate(Context context, int updateText) {
        SharedPreferences pref = context.getSharedPreferences("VER", 0);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            int VERSION = packageInfo.versionCode;
            int OLD_VER = pref.getInt(DbAdapter.KEY_VERSION, 0);
            if (OLD_VER < VERSION) {
                ShowUpdate(context, updateText);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(DbAdapter.KEY_VERSION, VERSION);
                editor.commit();
            }
        } catch (Exception e) {
        }
    }

    public static void ShowUpdate(Context context, int updateText) {
        new AlertDialog.Builder(context).setTitle(app.arifnurrohim.applistbackup.R.string.update_history).setMessage(updateText).setPositiveButton(app.arifnurrohim.applistbackup.R.string.ok, (DialogInterface.OnClickListener) null).show();
    }
}
