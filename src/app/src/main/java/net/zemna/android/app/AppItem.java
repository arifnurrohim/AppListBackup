package net.zemna.android.app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import net.zemna.android.graphics.BitmapUtil;

/* JADX INFO: loaded from: classes.dex */
public class AppItem implements Parcelable {
    public static final Parcelable.Creator<AppItem> CREATOR = new Parcelable.Creator<AppItem>() { // from class: net.zemna.android.app.AppItem.1
        @Override // android.os.Parcelable.Creator
        public AppItem[] newArray(int size) {
            return new AppItem[size];
        }

        @Override // android.os.Parcelable.Creator
        public AppItem createFromParcel(Parcel source) {
            AppItem item = new AppItem();
            item.setPackageName(source.readString());
            item.setVersionCode(source.readInt());
            item.setAppName(source.readString());
            Bitmap bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(source);
            item.setIcon(new BitmapDrawable(bitmap));
            item.setLoadCompleted(source.readInt() > 0);
            item.setSystem(source.readInt() > 0);
            return item;
        }
    };
    private String _appName;
    private Drawable _icon;
    private long _installationDate;
    private boolean _isExistInDB;
    private boolean _isInstalled;
    private boolean _isSystem;
    private boolean _isUpdated = false;
    private boolean _loadCompleted;
    private String _packageName;
    private long _packageSize;
    private int _versionCode;

    public String getPackageName() {
        return this._packageName;
    }

    public void setPackageName(String pkgName) {
        this._packageName = pkgName;
    }

    public String getAppName() {
        return this._appName;
    }

    public void setAppName(String appName) {
        this._appName = appName;
    }

    public int getVersionCode() {
        return this._versionCode;
    }

    public void setVersionCode(int versionCode) {
        this._versionCode = versionCode;
    }

    public Drawable getIcon() {
        return this._icon;
    }

    public void setIcon(Drawable icon) {
        this._icon = icon;
    }

    public long getPackageSize() {
        return this._packageSize;
    }

    public void setPackageSize(long packageSize) {
        this._packageSize = packageSize;
    }

    public boolean isLoadcompleted() {
        return this._loadCompleted;
    }

    public void setLoadCompleted(boolean loadCompleted) {
        this._loadCompleted = loadCompleted;
    }

    public boolean isExistInDB() {
        return this._isExistInDB;
    }

    public void setExistInDB(boolean existInDB) {
        this._isExistInDB = existInDB;
    }

    public boolean isUpdated() {
        return this._isUpdated;
    }

    public void setUpdated(boolean isUpdated) {
        this._isUpdated = isUpdated;
    }

    public boolean isInstalled() {
        return this._isInstalled;
    }

    public void setInstalled(boolean installed) {
        this._isInstalled = installed;
    }

    public boolean isSystem() {
        return this._isSystem;
    }

    public void setSystem(boolean system) {
        this._isSystem = system;
    }

    public long getInstallationDate() {
        return this._installationDate;
    }

    public void setInstallationDate(long date) {
        this._installationDate = date;
    }

    public boolean equals(AppItem item) {
        return this._packageName.equals(item.getPackageName()) && this._versionCode == item.getVersionCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._packageName);
        dest.writeInt(this._versionCode);
        dest.writeString(this._appName);
        Bitmap bitmap = BitmapUtil.getBitmap(this._icon, this._icon.getIntrinsicWidth(), this._icon.getIntrinsicHeight());
        bitmap.writeToParcel(dest, 0);
        dest.writeInt(this._loadCompleted ? 1 : 0);
        dest.writeInt(this._isSystem ? 1 : 0);
    }
}
