package net.zemna.android.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class AppItemLoader {
    public static void loadAppInfo(PackageManager pm, PackageInfo pkgInfo, AppItem appItem) throws IOException {
        appItem.setAppName(pm.getApplicationLabel(pkgInfo.applicationInfo).toString());
        Drawable bd = IconCache.readIcon(pkgInfo.packageName, pkgInfo.versionCode);
        if (bd == null) {
            bd = pm.getApplicationIcon(pkgInfo.applicationInfo);
        }
        appItem.setIcon(bd);
    }
}
