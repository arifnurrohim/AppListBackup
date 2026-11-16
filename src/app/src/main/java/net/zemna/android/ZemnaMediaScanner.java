package net.zemna.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

/* JADX INFO: loaded from: classes.dex */
public final class ZemnaMediaScanner {
    public static void StartMediaScan(Context context) {
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
    }
}
