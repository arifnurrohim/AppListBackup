package app.arifnurrohim.applistbackup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Locale;
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
    List<AppItem> _filteredList = new ArrayList<>();
    ViewListAdapter _adapter = null;
    int _filterType = 0;
    boolean _showSystemApps = true;
    String _searchQuery = "";
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
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.6
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppItem item = ViewListActivity.this._adapter.getItem(position);
                if (item == null) {
                    return false;
                }
                ViewListActivity.this.copyPackageName(item.getPackageName());
                return true;
            }
        });
        setupFilterBar();
    }

    private void setupFilterBar() {
        EditText search = (EditText) findViewById(R.id.search_apps);
        search.addTextChangedListener(new TextWatcher() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.7
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                ViewListActivity.this._searchQuery = s.toString();
                ViewListActivity.this.rebuildList();
            }
        });
        CheckBox showSystem = (CheckBox) findViewById(R.id.chk_show_system);
        showSystem.setChecked(this._showSystemApps);
        showSystem.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() { // from class: app.arifnurrohim.applistbackup.ViewListActivity.8
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                ViewListActivity.this._showSystemApps = isChecked;
                ViewListActivity.this.rebuildList();
            }
        });
    }

    private void copyPackageName(String packageName) {
        if (packageName == null) {
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getString(R.string.package_name), packageName));
        Toast.makeText(this, getString(R.string.package_copied, packageName), Toast.LENGTH_SHORT).show();
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
            List<AppItem> filteredList = (List) map.get("filteredList");
            if (filteredList != null) {
                this._filteredList = filteredList;
            }
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
        updateInstalledStatus();
        rebuildList();
    }

    private void updateInstalledStatus() {
        if (this._appItems == null) {
            return;
        }
        PackageManager pm = getPackageManager();
        for (AppItem ai : this._appItems) {
            try {
                PackageInfo pi = pm.getPackageInfo(ai.getPackageName(), 0);
                ai.setInstalled(true);
                ai.setSystem((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            } catch (PackageManager.NameNotFoundException e) {
                ai.setInstalled(false);
            }
        }
    }

    private void rebuildList() {
        if (this._appItems == null) {
            return;
        }
        String query = this._searchQuery.trim().toLowerCase(Locale.US);
        this._filteredList.clear();
        for (AppItem item : this._appItems) {
            if (!this._showSystemApps && item.isSystem()) {
                continue;
            }
            if (this._filterType == 1 && !item.isInstalled()) {
                continue;
            }
            if (this._filterType == 2 && item.isInstalled()) {
                continue;
            }
            if (query.length() > 0) {
                String name = item.getAppName() == null ? "" : item.getAppName().toLowerCase(Locale.US);
                String pkg = item.getPackageName() == null ? "" : item.getPackageName().toLowerCase(Locale.US);
                if (!name.contains(query) && !pkg.contains(query)) {
                    continue;
                }
            }
            this._filteredList.add(item);
        }
        if (this._adapter == null) {
            this._adapter = new ViewListAdapter(this, R.layout.listitem_view, this._filteredList);
            setListAdapter(this._adapter);
        } else {
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
                Collections.sort(this._appItems, comparator);
                if (sortType == 1) {
                    Collections.reverse(this._appItems);
                }
                break;
        }
        rebuildList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAppList(int filterType) {
        this._filterType = filterType;
        updateInstalledStatus();
        rebuildList();
    }
}
