package net.zemna.android.market;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
public class AndroidMarketProvider implements IMarketProvider {
    private Context _context;
    private boolean _isAvailable;

    public AndroidMarketProvider(Context context) {
        this._context = null;
        this._isAvailable = true;
        this._context = context;
        try {
            this._context.getPackageManager().getPackageInfo("com.android.vending", 0);
        } catch (PackageManager.NameNotFoundException e) {
            this._isAvailable = false;
        }
    }

    @Override // net.zemna.android.market.IMarketProvider
    public String getKey() {
        return "AM";
    }

    @Override // net.zemna.android.market.IMarketProvider
    public String getMarketName() {
        return this._context.getResources().getString(app.arifnurrohim.applistbackup.R.string.android_market);
    }

    @Override // net.zemna.android.market.IMarketProvider
    public Drawable getIcon() {
        return this._context.getResources().getDrawable(app.arifnurrohim.applistbackup.R.drawable.market);
    }

    @Override // net.zemna.android.market.IMarketProvider
    public void viewAppDetail(String packageName, String appName) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(getAppLink(packageName, appName)));
        intent.setFlags(268435456);
        this._context.startActivity(intent);
    }

    @Override // net.zemna.android.market.IMarketProvider
    public String getAppLink(String packageName, String appName) {
        String strUri = "market://details?id=" + packageName;
        return strUri;
    }

    @Override // net.zemna.android.market.IMarketProvider
    public boolean isAvailable() {
        return this._isAvailable;
    }
}
