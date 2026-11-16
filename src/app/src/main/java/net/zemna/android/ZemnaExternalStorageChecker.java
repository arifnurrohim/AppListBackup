package net.zemna.android;

import android.os.Environment;

/* JADX INFO: loaded from: classes.dex */
public final class ZemnaExternalStorageChecker {
    public static boolean isMediaMounted() {
        String ess = Environment.getExternalStorageState();
        return ess.equals("mounted");
    }
}
