package app.arifnurrohim.applistbackup;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.widget.Toast;
import app.arifnurrohim.applistbackup.backupitem.BackupItem;
import app.arifnurrohim.applistbackup.util.BackupItemUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import net.zemna.android.ZemnaExternalStorageChecker;
import net.zemna.android.app.AppItem;
import net.zemna.android.app.DbAdapter;
import net.zemna.android.app.PackageFactory;
import net.zemna.android.market.IMarketProvider;
import net.zemna.android.market.MarketProviderFactory;

/* JADX INFO: loaded from: classes.dex */
public class AppListBackupApp extends Application {
    private static final String TAG = "ZEMNA";
    private final boolean WRITE_LOG = false;
    List<PackageInfo> _pkgList = null;
    boolean _pkgListShowSystem = false;
    List<AppItem> _appItems = null;
    List<AppItem> _dbItems = null;
    List<BackupItem> _backupItems = null;
    Hashtable<String, IMarketProvider> _marketProviders = null;
    AppItem _selectedItem = null;

    public List<PackageInfo> getPkgList() {
        boolean z = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREF_KEY_SHOW_SYSTEM_APPS", false);
        if (this._pkgList == null || z != this._pkgListShowSystem) {
            this._pkgList = PackageFactory.getInstalledPackage(this, !z);
            this._pkgListShowSystem = z;
            this._appItems = null;
        }
        return this._pkgList;
    }

    public List<AppItem> getAppItems() {
        if (this._appItems == null) {
            ArrayList arrayList = new ArrayList();
            this._appItems = arrayList;
            BitmapDrawable defaultIcon = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
            List<PackageInfo> pkgs = getPkgList();
            this._appItems = arrayList;
            for (PackageInfo pInfo : pkgs) {
                AppItem mi = new AppItem();
                mi.setPackageName(pInfo.packageName);
                mi.setVersionCode(pInfo.versionCode);
                mi.setAppName(pInfo.packageName);
                mi.setIcon(defaultIcon);
                String appFile = pInfo.applicationInfo.sourceDir;
                mi.setInstallationDate(new File(appFile).lastModified());
                mi.setSystem((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
                arrayList.add(mi);
            }
        }
        return this._appItems;
    }

    public List<AppItem> getDbItems() {
        return this._dbItems;
    }

    public void setDbItems(List<AppItem> list) {
        this._dbItems = list;
    }

    public AppItem getSelectedItem() {
        return this._selectedItem;
    }

    public void setSelectedItem(AppItem item) {
        this._selectedItem = item;
    }

    public List<BackupItem> getBackupItems() {
        if (this._backupItems == null) {
            reloadBackupList();
        }
        return this._backupItems;
    }

    public void reloadBackupList() {
        if (!ZemnaExternalStorageChecker.isMediaMounted()) {
            Toast.makeText(this, R.string.error_media_not_mounted, 0).show();
        } else {
            this._backupItems = BackupItemUtil.loadBackupList(this);
        }
    }

    public void setBackupItems(List<BackupItem> list) {
        this._backupItems = list;
    }

    public Hashtable<String, IMarketProvider> getMarketProviders() {
        if (this._marketProviders == null) {
            this._marketProviders = MarketProviderFactory.getAvailableMarketProviders(this);
        }
        return this._marketProviders;
    }

    public void setMarketProviders(Hashtable<String, IMarketProvider> providers) {
        this._marketProviders = providers;
    }

    public void clearObject() {
        if (this._pkgList != null) {
            this._pkgList.clear();
            this._pkgList = null;
        }
        if (this._appItems != null) {
            this._appItems.clear();
            this._appItems = null;
        }
        if (this._dbItems != null) {
            this._dbItems.clear();
            this._dbItems = null;
        }
        if (this._backupItems != null) {
            this._backupItems.clear();
            this._backupItems = null;
        }
    }

    public void saveAppList() {
        if (this._appItems != null) {
            DbAdapter adapter = new DbAdapter(this).open(true);
            adapter.getDB().beginTransaction();
            try {
                for (AppItem item : this._appItems) {
                    if (item.isLoadcompleted()) {
                        if (!item.isExistInDB()) {
                            adapter.insertApp(item);
                        } else if (item.isUpdated()) {
                            adapter.updateApp(item);
                        }
                    }
                }
                adapter.getDB().setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                adapter.getDB().endTransaction();
            }
            adapter.close();
        }
    }
}
