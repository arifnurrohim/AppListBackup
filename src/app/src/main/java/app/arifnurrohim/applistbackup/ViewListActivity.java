package app.arifnurrohim.applistbackup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import app.arifnurrohim.applistbackup.util.BackupItemUtil;
import app.arifnurrohim.applistbackup.util.ThemeUtils;
import app.arifnurrohim.applistbackup.util.UIUtils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import net.zemna.android.app.AppItem;
import net.zemna.android.app.AppItemComparable;
import net.zemna.android.market.IMarketProvider;
import net.zemna.android.ui.ZemnaPopupWindow;

/* JADX INFO: loaded from: classes.dex */
public class ViewListActivity extends ListActivity {
    private static String MY_AD_UNIT_ID = "a14d5a15f9c1e8b";
    Hashtable<String, IMarketProvider> _marketProviders = null;
    AppListBackupApp _app = null;
    List<AppItem> _appItems = null;
    List<AppItem> _filteredList = null;
    ViewListAdapter _adapter = null;
    int _filterType = 0;
    private ProgressDialog _progressDialog = null;
    private Handler _handler = new Handler() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ViewListActivity.this.showAppList(ViewListActivity.this._filterType);
                    ViewListActivity.this._progressDialog.dismiss();
                    break;
                case 1:
                    ViewListActivity.this._progressDialog.dismiss();
                    ViewListActivity.this.loadBackupFile(msg.obj.toString());
                    break;
                case 9:
                    ViewListActivity.this._progressDialog.dismiss();
                    Toast.makeText(ViewListActivity.this, msg.obj.toString(), 0).show();
                    break;
            }
        }
    };
    BroadcastReceiver _receiver = new BroadcastReceiver() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.compareTo("android.intent.action.PACKAGE_ADDED") == 0) {
                ViewListActivity.this.changeAppStatus();
            }
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlist);
        UIUtils.setTitle(this);
        this._app = (AppListBackupApp) getApplicationContext();
        this._marketProviders = this._app.getMarketProviders();
        if (savedInstanceState != null) {
            restore();
        }
        Intent intent = getIntent();
        String scheme = intent.getScheme();
        String filePath = intent.getData().getPath();
        if ("content".equals(scheme)) {
            loadContent(intent.getData());
        } else {
            loadBackupFile(filePath);
        }
        getListView().setItemsCanFocus(false);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("filterType", Integer.valueOf(this._filterType));
        map.put("filteredList", this._filteredList);
        return map;
    }

    private void restore() {
        Object o = getLastNonConfigurationInstance();
        if (o != null) {
            HashMap<String, Object> map = (HashMap) o;
            this._filterType = Integer.parseInt(map.get("filterType").toString());
            this._filteredList = (List) map.get("filteredList");
        }
    }

    private void loadContent(final Uri uri) {
        final Resources res = getResources();
        this._progressDialog = ProgressDialog.show(this, res.getString(R.string.reading), res.getString(R.string.reading_backup_file), true, true);
        Thread thread = new Thread(new Runnable() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    InputStream is = ViewListActivity.this.getContentResolver().openInputStream(uri);
                    try {
                        String filePath = BackupItemUtil.makeBackupFile(ViewListActivity.this, is);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = filePath;
                        ViewListActivity.this._handler.sendMessage(msg);
                    } catch (Exception e) {
                        Message msg2 = new Message();
                        msg2.what = 9;
                        msg2.obj = res.getString(R.string.backup_file_read_failed);
                        ViewListActivity.this._handler.sendMessage(msg2);
                    }
                } catch (FileNotFoundException e2) {
                }
            }
        });
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadBackupFile(final String fileName) {
        Resources res = getResources();
        this._progressDialog = ProgressDialog.show(this, res.getString(R.string.loading), res.getString(R.string.loading_backup_file), true, true);
        Thread thread = new Thread(new Runnable() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ViewListActivity.this._appItems = BackupItemUtil.loadBackupFile(fileName);
                    ViewListActivity.this._handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    Toast.makeText(ViewListActivity.this, R.string.backup_file_loading_error, 1);
                    ViewListActivity.this.finish();
                }
            }
        });
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeAppStatus() {
        if (this._adapter != null) {
            PackageManager pm = getPackageManager();
            for (int i = 0; i < this._adapter.getCount(); i++) {
                AppItem ai = this._adapter.getItem(i);
                try {
                    pm.getPackageInfo(ai.getPackageName(), 0);
                    ai.setInstalled(true);
                    if (this._filterType == 2) {
                        this._adapter.remove(ai);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    ai.setInstalled(false);
                    if (this._filterType == 1) {
                        this._adapter.remove(ai);
                    }
                }
            }
            this._adapter.notifyDataSetChanged();
        }
    }

    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }

    public void onSortClick(View v) {
        SortPopupWindow popup = new SortPopupWindow(v, this);
        popup.showLikePopDownMenu();
    }

    public void onFilterClick(View v) {
        new AlertDialog.Builder(this).setTitle(R.string.select_filter_type).setSingleChoiceItems(R.array.filter_type_category, this._filterType, new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ViewListActivity.this.showAppList(which);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).show();
    }

    public void gotoMarket(String providerKey, AppItem item) {
        this._marketProviders.get(providerKey).viewAppDetail(item.getPackageName(), item.getAppName());
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreate(this)) {
            recreate();
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        registerReceiver(this._receiver, intentFilter);
        if (this._adapter != null) {
            changeAppStatus();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this._receiver);
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AppPopupWindow pw = new AppPopupWindow(v, this, this._adapter.getItem(position));
        pw.showLikePopDownMenu();
    }

    private static class AppPopupWindow extends ZemnaPopupWindow implements View.OnClickListener {
        private final ViewListActivity _context;
        private final AppItem _item;

        public AppPopupWindow(View anchor, ViewListActivity context, AppItem item) {
            super(anchor);
            this._context = context;
            this._item = item;
        }

        @Override // net.zemna.android.ui.ZemnaPopupWindow
        protected void onCreate() {
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext().getSystemService("layout_inflater");
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.popup_appitem_layout, (ViewGroup) null);
            AppListBackupApp app2 = (AppListBackupApp) this.anchor.getContext().getApplicationContext();
            for (IMarketProvider imp : app2.getMarketProviders().values()) {
                ImageButton ib = new ImageButton(this.anchor.getContext());
                ib.setTag(imp.getKey());
                ib.setBackgroundDrawable(imp.getIcon());
                ib.setOnClickListener(this);
                root.addView(ib);
            }
            setContentView(root);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            String key = String.valueOf(v.getTag());
            this._context._marketProviders.get(key).viewAppDetail(this._item.getPackageName(), this._item.getAppName());
            dismiss();
        }
    }

    private static class SortPopupWindow extends ZemnaPopupWindow implements View.OnClickListener {
        private final ViewListActivity _context;

        public SortPopupWindow(View anchor, ViewListActivity context) {
            super(anchor);
            this._context = context;
        }

        @Override // net.zemna.android.ui.ZemnaPopupWindow
        protected void onCreate() {
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext().getSystemService("layout_inflater");
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.popup_sort_layout, (ViewGroup) null);
            int icount = root.getChildCount();
            for (int i = 0; i < icount; i++) {
                View v = root.getChildAt(i);
                if (v instanceof ImageButton) {
                    ImageButton b = (ImageButton) v;
                    b.setOnClickListener(this);
                }
            }
            setContentView(root);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ascending /* 2131427350 */:
                    this._context.sortApp(0);
                    break;
                case R.id.descending /* 2131427351 */:
                    this._context.sortApp(1);
                    break;
            }
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sortApp(int sortType) {
        switch (sortType) {
            case 0:
            case 1:
                AppItemComparable comparator = new AppItemComparable();
                Collections.sort(this._filteredList, comparator);
                if (sortType == 1) {
                    Collections.reverse(this._filteredList);
                }
                break;
        }
        this._adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAppList(int filterType) {
        this._filterType = filterType;
        this._filteredList = new ArrayList();
        for (AppItem item : this._appItems) {
            if (filterType != 1 || item.isInstalled()) {
                if (filterType != 2 || !item.isInstalled()) {
                    this._filteredList.add(item);
                }
            }
        }
        this._adapter = new ViewListAdapter(this, R.layout.listitem_view, this._filteredList);
        setListAdapter(this._adapter);
        changeAppStatus();
    }
}
