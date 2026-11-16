package net.zemna.android.market;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public interface IMarketProvider {
    String getAppLink(String str, String str2);

    Drawable getIcon();

    String getKey();

    String getMarketName();

    boolean isAvailable();

    void viewAppDetail(String str, String str2);
}
