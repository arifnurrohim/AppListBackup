package app.arifnurrohim.applistbackup;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import app.arifnurrohim.applistbackup.util.ThemeUtils;
import app.arifnurrohim.applistbackup.util.UIUtils;

/* JADX INFO: loaded from: classes.dex */
public class AboutActivity extends Activity {
    private void openUrl(String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        intent.addFlags(268435456);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public void onCatppuccinClick(View v) {
        openUrl("https://github.com/catppuccin/catppuccin/blob/main/docs/style-guide.md");
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        UIUtils.setTitle(this);
    }

    public void onCreatorClick(View v) {
        openUrl("https://arifnurrohim.my.id/");
    }

    public void onEmailClick(View v) {
        openUrl("mailto:jobs.arifnurrohim@gmail.com");
    }

    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }

    public void onMaterialClick(View v) {
        openUrl("https://github.com/google/material-design-icons");
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreate(this)) {
            recreate();
        }
    }

}
