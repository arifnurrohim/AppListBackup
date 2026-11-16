package net.zemna.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* JADX INFO: loaded from: classes.dex */
public class ZemnaNetworkInfo {
    public static boolean IsWifiAvailable(Context context) {
        return IsNetworkAvailable(context, 1);
    }

    public static boolean Is3GAvailable(Context context) {
        return IsNetworkAvailable(context, 0);
    }

    public static boolean IsAnyAvailable(Context context) {
        return IsWifiAvailable(context) || Is3GAvailable(context);
    }

    private static boolean IsNetworkAvailable(Context context, int networkType) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        if (connMgr == null) {
            return false;
        }
        try {
            NetworkInfo info = connMgr.getNetworkInfo(networkType);
            boolean bConnect = info.isAvailable() && info.isConnected();
            return bConnect;
        } catch (Exception e) {
            return false;
        }
    }
}
