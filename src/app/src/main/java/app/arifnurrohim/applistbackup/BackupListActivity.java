package app.arifnurrohim.applistbackup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import app.arifnurrohim.applistbackup.backupitem.BackupItem;
import app.arifnurrohim.applistbackup.util.BackupItemUtil;
import app.arifnurrohim.applistbackup.util.ThemeUtils;
import app.arifnurrohim.applistbackup.util.UIUtils;
import java.util.List;
import net.zemna.android.ui.ZemnaPopupWindow;

/* JADX INFO: loaded from: classes.dex */
public class BackupListActivity extends ListActivity {
    private static String BACKUP_PATH = "Zemna AppList Backup";
    private static String MY_AD_UNIT_ID = "a14d5a15f9c1e8b";
    private AppListBackupApp _app = null;
    private List<BackupItem> _backupList = null;
    private BackupListAdapter _adapter = null;

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreate(this)) {
            recreate();
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backuplist);
        UIUtils.setTitle(this);
        this._app = (AppListBackupApp) getApplicationContext();
        this._backupList = this._app.getBackupItems();
        if (this._backupList != null) {
            this._adapter = new BackupListAdapter(this, R.layout.listitem_backupfile, this._backupList);
            setListAdapter(this._adapter);
        }
        getListView().setItemsCanFocus(false);
    }

    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }

    public void onReloadClick(View v) {
        this._app.reloadBackupList();
        this._adapter = new BackupListAdapter(this, R.layout.listitem_backupfile, this._backupList);
        setListAdapter(this._adapter);
    }

    public void deleteBackupItem(final BackupItem item) {
        new AlertDialog.Builder(this).setTitle(R.string.delete).setMessage(R.string.confirm_delete).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.BackupListActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                BackupItemUtil.deleteBackupFile(item);
                BackupListActivity.this._backupList.remove(item);
                BackupListActivity.this._adapter.notifyDataSetChanged();
                Toast.makeText(BackupListActivity.this, R.string.item_deleted, 0).show();
            }
        }).setNegativeButton(R.string.no, (DialogInterface.OnClickListener) null).show();
    }

    public void sendBackupItem(BackupItem item) {
        Intent intent = new Intent("android.intent.action.SEND");
        String backupDir = BackupItemUtil.getBackupDir(this);
        Uri uri = Uri.parse("file://" + backupDir + "/" + item.getBackupName());
        intent.setFlags(268435456);
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setType("text/xml");
        intent.putExtra("android.intent.extra.SUBJECT", "AppList Backup");
        String text = "FileName = " + item.getBackupName() + "\r\n\r\n";
        intent.putExtra("android.intent.extra.TEXT", String.valueOf(text) + getResources().getString(R.string.email_message));
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.send)));
    }

    public void showBackupItem(BackupItem item) {
        Intent intent = new Intent(this, (Class<?>) ViewListActivity.class);
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(item.getFullPath()));
        startActivity(intent);
    }

    @Override // android.app.ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ViewPopupWindow pw = new ViewPopupWindow(v, this, this._backupList.get(position));
        pw.showLikePopDownMenu();
    }

    private static class ViewPopupWindow extends ZemnaPopupWindow implements View.OnClickListener {
        private final BackupListActivity _context;
        private final BackupItem _item;

        public ViewPopupWindow(View anchor, BackupListActivity context, BackupItem item) {
            super(anchor);
            this._context = context;
            this._item = item;
        }

        @Override // net.zemna.android.ui.ZemnaPopupWindow
        protected void onCreate() {
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext().getSystemService("layout_inflater");
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.popup_backuplist_layout, (ViewGroup) null);
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
                case R.id.detail /* 2131427347 */:
                    this._context.showBackupItem(this._item);
                    break;
                case R.id.send /* 2131427348 */:
                    this._context.sendBackupItem(this._item);
                    break;
                case R.id.delete /* 2131427349 */:
                    this._context.deleteBackupItem(this._item);
                    break;
            }
            dismiss();
        }
    }
}
