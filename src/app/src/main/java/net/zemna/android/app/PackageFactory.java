package net.zemna.android.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PackageFactory {
    public static List<PackageInfo> getInstalledPackage(Context context) {
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        ArrayList<PackageInfo> items = new ArrayList<>(context.getPackageManager().getInstalledPackages(0));
        return items;
    }

    public static List<PackageInfo> getInstalledPackage(Context context, boolean exceptSystemApp) {
        List<PackageInfo> items = getInstalledPackage(context);
        if (exceptSystemApp) {
            int i = 0;
            while (i < items.size()) {
                boolean bSystemApp = (items.get(i).applicationInfo.flags & 1) > 0;
                if (bSystemApp) {
                    items.remove(i);
                    i--;
                }
                i++;
            }
        }
        return items;
    }
}
