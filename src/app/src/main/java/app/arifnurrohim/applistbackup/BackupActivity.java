package app.arifnurrohim.applistbackup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.arifnurrohim.applistbackup.util.BackupItemUtil;
import app.arifnurrohim.applistbackup.util.ThemeUtils;
import app.arifnurrohim.applistbackup.util.UIUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.zemna.android.app.AppItem;
import net.zemna.android.app.AppItemComparable;
import net.zemna.android.app.AppItemDateComparable;
import net.zemna.android.app.AppItemLoader;
import net.zemna.android.app.DbAdapter;

/* JADX INFO: loaded from: classes.dex */
public class BackupActivity extends ListActivity {
    private static String MY_AD_UNIT_ID = "a14d5a15f9c1e8b";
    private static final String TAG = "ZEMNA";
    private static final boolean WRITE_LOG = false;
    AppListBackupApp _app = null;
    List<PackageInfo> _pkgList = null;
    List<AppItem> _appItems = null;
    List<AppItem> _appItemsInDB = null;
    AppItemAdapter _adapter = null;
    PackageLoader _packageLoader = null;
    int _sortType = 0;
    boolean _showSystemApps = false;

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreate(this)) {
            recreate();
        } else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREF_KEY_SHOW_SYSTEM_APPS", false) != this._showSystemApps) {
            recreate();
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        UIUtils.setTitle(this);
        this._app = (AppListBackupApp) getApplicationContext();
        this._pkgList = this._app.getPkgList();
        this._appItems = this._app.getAppItems();
        this._appItemsInDB = this._app.getDbItems();
        if (savedInstanceState != null) {
            restore();
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String sortType = pref.getString("PREF_KEY_DEFAULT_SORT_TYPE", "0");
            this._sortType = Integer.parseInt(sortType);
        }
        this._showSystemApps = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("PREF_KEY_SHOW_SYSTEM_APPS", false);
        this._adapter = new AppItemAdapter(this, R.layout.listitem_appitem, this._appItems);
        setListAdapter(this._adapter);
        getListView().setItemsCanFocus(false);
        this._packageLoader = new PackageLoader(this, null);
        this._packageLoader.execute(this._appItems);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sortType", Integer.valueOf(this._sortType));
        return map;
    }

    private void restore() {
        Object o = getLastNonConfigurationInstance();
        if (o != null) {
            HashMap<String, Object> map = (HashMap) o;
            this._sortType = Integer.parseInt(map.get("sortType").toString());
        }
    }

    @Override // android.app.ListActivity, android.app.Activity
    protected void onDestroy() {
        this._packageLoader.cancel(true);
        super.onDestroy();
    }

    private class PackageLoader extends AsyncTask<List<AppItem>, Void, Void> {
        private PackageLoader() {
        }

        /* synthetic */ PackageLoader(BackupActivity backupActivity, PackageLoader packageLoader) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(List<AppItem>... listArr) {
            DbAdapter adapter = new DbAdapter(BackupActivity.this).open(false);
            if (BackupActivity.this._appItemsInDB == null) {
                BackupActivity.this._appItemsInDB = adapter.getAllApps();
                BackupActivity.this._app.setDbItems(BackupActivity.this._appItemsInDB);
                adapter.getDB().beginTransaction();
                for (int i = 0; i < BackupActivity.this._appItemsInDB.size(); i++) {
                    try {
                        boolean bFind = false;
                        AppItem appDB = BackupActivity.this._appItemsInDB.get(i);
                        for (int j = 0; j < BackupActivity.this._appItems.size(); j++) {
                            AppItem app2 = BackupActivity.this._appItems.get(j);
                            if (app2.equals(appDB)) {
                                app2.setIcon(appDB.getIcon());
                                app2.setAppName(appDB.getAppName());
                                app2.setExistInDB(true);
                                app2.setLoadCompleted(true);
                                bFind = true;
                                break;
                            }
                        }
                        if (!bFind) {
                            adapter.deleteApp(appDB.getPackageName());
                        }
                    } catch (Throwable th) {
                        adapter.getDB().endTransaction();
                        throw th;
                    }
                }
                adapter.getDB().setTransactionSuccessful();
                adapter.getDB().endTransaction();
                adapter.close();
                publishProgress(new Void[0]);
            }
            ArrayList<AppItem> queue = new ArrayList<>();
            for (AppItem item : BackupActivity.this._appItems) {
                if (!item.isLoadcompleted()) {
                    queue.add(item);
                }
            }
            PackageManager pm = BackupActivity.this.getApplicationContext().getPackageManager();
            while (queue.size() > 0 && !isCancelled()) {
                AppItem item2 = queue.remove(0);
                PackageInfo pkgInfo = null;
                for (PackageInfo pi : BackupActivity.this._pkgList) {
                    if (pi.packageName.equals(item2.getPackageName())) {
                        pkgInfo = pi;
                        break;
                    }
                }
                try {
                    AppItemLoader.loadAppInfo(pm, pkgInfo, item2);
                    if (isCancelled()) {
                        return null;
                    }
                    item2.setUpdated(true);
                    item2.setLoadCompleted(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) {
                    return null;
                }
                publishProgress(new Void[0]);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Void... values) {
            BackupActivity.this._adapter.notifyDataSetChanged();
            super.onProgressUpdate(values);
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            BackupActivity.this.findViewById(R.id.title_check).setEnabled(false);
            BackupActivity.this.findViewById(R.id.title_save).setEnabled(false);
            BackupActivity.this.findViewById(R.id.title_sort).setEnabled(false);
            BackupActivity.this.findViewById(R.id.title_refresh_progress).setVisibility(0);
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            BackupActivity.this.findViewById(R.id.title_check).setEnabled(true);
            BackupActivity.this.findViewById(R.id.title_save).setEnabled(true);
            BackupActivity.this.findViewById(R.id.title_sort).setEnabled(true);
            BackupActivity.this.findViewById(R.id.title_refresh_progress).setVisibility(8);
            BackupActivity.this.sortApp(BackupActivity.this._sortType);
            super.onPostExecute(result);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void onHomeClick(View v) {
        this._packageLoader.cancel(true);
        UIUtils.goHome(this);
    }

    public void onSortClick(View v) {
        new AlertDialog.Builder(this).setTitle(R.string.select_sort_type).setSingleChoiceItems(R.array.app_sort_type_category, this._sortType, new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.BackupActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                BackupActivity.this.sortApp(which);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
    }

    final void sortApp(int sortType) {
        switch (sortType) {
            case 0:
            case 1:
                AppItemComparable comparator = new AppItemComparable();
                Collections.sort(this._appItems, comparator);
                if (sortType == 1) {
                    Collections.reverse(this._appItems);
                }
                break;
            case 2:
            case 3:
                AppItemDateComparable comparator2 = new AppItemDateComparable();
                Collections.sort(this._appItems, comparator2);
                if (sortType == 3) {
                    Collections.reverse(this._appItems);
                }
                break;
        }
        this._sortType = sortType;
        this._adapter.notifyDataSetChanged();
    }

    public void onCheckClick(View v) {
        if (isAllItemChecked()) {
            setAllItemChecked(false);
        } else {
            setAllItemChecked(true);
        }
        this._adapter.notifyDataSetChanged();
    }

    public void onSaveClick(View v) {
        int cnt = 0;
        SparseBooleanArray arr = getListView().getCheckedItemPositions();
        ArrayList<AppItem> list = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.valueAt(i)) {
                AppItem item = this._appItems.get(arr.keyAt(i));
                list.add(item);
                cnt++;
            }
        }
        if (cnt < 1) {
            Toast.makeText(this, R.string.error_no_item_checked, 0).show();
        } else {
            saveList(list);
        }
    }

    private boolean isAllItemChecked() {
        for (int i = 0; i < getListView().getCount(); i++) {
            if (!getListView().isItemChecked(i)) {
                return false;
            }
        }
        return true;
    }

    private void setAllItemChecked(boolean checked) {
        for (int i = 0; i < getListView().getCount(); i++) {
            getListView().setItemChecked(i, checked);
        }
    }

    private void saveList(final ArrayList<AppItem> list) {
        String ess = Environment.getExternalStorageState();
        if (!ess.equals("mounted")) {
            Toast.makeText(this, R.string.error_media_not_mounted, 1).show();
            return;
        }
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd_'at'_HH-mm-ss");
        String df = simpleDate.format(date);
        final EditText input = new EditText(this);
        input.setText("AppList_" + df);
        new AlertDialog.Builder(this).setView(input).setTitle(R.string.input_backfile_name).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.BackupActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                String folderPath = BackupItemUtil.getBackupDir(BackupActivity.this);
                String path = String.valueOf(folderPath) + "/" + input.getText().toString() + ".xml";
                new File(folderPath).mkdirs();
                try {
                    DomAppItemParser.writeXml(list, path);
                    Toast.makeText(BackupActivity.this, R.string.save_successfully, 1).show();
                    BackupActivity.this.getListView().clearChoices();
                    BackupActivity.this._app.reloadBackupList();
                    BackupActivity.this.finish();
                } catch (Exception e) {
                }
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
    }
}
