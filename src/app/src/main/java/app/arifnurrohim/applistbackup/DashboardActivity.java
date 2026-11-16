package app.arifnurrohim.applistbackup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import app.arifnurrohim.applistbackup.util.ThemeUtils;
import app.arifnurrohim.applistbackup.util.UIUtils;

/* JADX INFO: loaded from: classes.dex */
public class DashboardActivity extends Activity {
    private static String MY_AD_UNIT_ID = "a14d5a15f9c1e8b";
    AppListBackupApp _app = null;

    public void onAboutClick(View v) {
        startActivity(new Intent(this, (Class<?>) AboutActivity.class));
    }

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
        setContentView(R.layout.activity_dashboard);
        StoragePermissionHelper.ensure(this);
        UIUtils.setTitle(this);
        this._app = (AppListBackupApp) getApplicationContext();
    }

    public void onBackupClick(View v) {
        startActivity(new Intent(this, (Class<?>) BackupActivity.class));
    }

    public void onViewClick(View v) {
        startActivity(new Intent(this, (Class<?>) BackupListActivity.class));
    }

    public void onPreferenceClick(View v) {
        startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
    }

    public void onHelpClick(View v) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.help_url))));
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this._app.saveAppList();
        this._app.clearObject();
        super.onDestroy();
    }
}
