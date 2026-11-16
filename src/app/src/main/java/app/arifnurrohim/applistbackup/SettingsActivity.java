package app.arifnurrohim.applistbackup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class SettingsActivity extends Activity {
    private static final String DEFAULT_DIR = "/storage/emulated/0/Documents/AppList Backup";
    private static final String K_ACCENT = "PREF_KEY_THEME_ACCENT";
    private static final String K_DIR = "PREF_KEY_BACKUP_DIR";
    private static final String K_FLAVOR = "PREF_KEY_THEME_FLAVOR";
    private static final String K_SORT = "PREF_KEY_DEFAULT_SORT_TYPE";
    private static final String K_SYSTEM = "PREF_KEY_SHOW_SYSTEM_APPS";
    private static final String[] SORT_NAMES = {"Name Asc", "Name Desc", "Date Asc", "Date Desc"};
    private static final String[] SORT_VALUES = {"0", "1", "2", "3"};
    private int accent;
    private LinearLayout content;
    private Palette p;
    private SharedPreferences prefs;

    private interface ToggleAction {
        void onChanged(boolean z);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        applyTheme();
        super.onCreate(bundle);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.p = Palette.forKey(this.prefs.getString(K_FLAVOR, "mocha"));
        this.accent = this.p.accent(this.prefs.getString(K_ACCENT, "mauve"));
        build();
    }

    private void applyTheme() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String string = defaultSharedPreferences.getString(K_FLAVOR, "mocha");
        String string2 = defaultSharedPreferences.getString(K_ACCENT, "mauve");
        setTheme(resId("style", "SettingsTheme"));
        int iResId = resId("style", "ThemeOverlay_Flavor_" + string);
        if (iResId != 0) {
            getTheme().applyStyle(iResId, true);
        }
        int iResId2 = resId("style", "ThemeOverlay_Accent_" + string + "_" + string2);
        if (iResId2 != 0) {
            getTheme().applyStyle(iResId2, true);
        }
    }

    private void build() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setVerticalScrollBarEnabled(false);
        this.content = vertical();
        this.content.setPadding(dp(16.0f), dp(4.0f), dp(16.0f), dp(100.0f));
        scrollView.addView(this.content, new FrameLayout.LayoutParams(-1, -2));
        setContentView(scrollView);
        section("Appearance");
        LinearLayout linearLayoutCard = card();
        for (int i = 0; i < Palette.KEYS.length; i++) {
            String str = Palette.KEYS[i];
            linearLayoutCard.addView(flavorRow(Palette.forKey(str), str.equals(this.p.key)));
            if (i < Palette.KEYS.length - 1) {
                linearLayoutCard.addView(innerDivider());
            }
        }
        addCard(linearLayoutCard);
        TextView textViewText = text("Accent", 12.0f, c("colorSubtext0"), true);
        LinearLayout.LayoutParams layoutParamsLp = lp(-1, -2);
        layoutParamsLp.topMargin = dp(18.0f);
        layoutParamsLp.bottomMargin = dp(8.0f);
        this.content.addView(textViewText, layoutParamsLp);
        FlowLayout flowLayout = new FlowLayout(this);
        flowLayout.setSpacing(dp(10.0f), dp(10.0f));
        for (String str2 : Palette.ACCENT_KEYS) {
            flowLayout.addView(accentDot(str2));
        }
        this.content.addView(flowLayout, lp(-1, -2));
        section("Preview");
        this.content.addView(previewCard(), lp(-1, -2));
        section("Behavior");
        LinearLayout linearLayoutCard2 = card();
        linearLayoutCard2.setPadding(dp(15.0f), dp(4.0f), dp(15.0f), dp(4.0f));
        linearLayoutCard2.addView(toggleRow("ic_apps", "Show system apps", "Include system apps in the backup list", this.prefs.getBoolean(K_SYSTEM, false), new ToggleAction() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.1
            @Override // app.arifnurrohim.applistbackup.SettingsActivity.ToggleAction
            public void onChanged(boolean z) {
                SettingsActivity.this.prefs.edit().putBoolean(SettingsActivity.K_SYSTEM, z).apply();
            }
        }));
        addCard(linearLayoutCard2);
        section("Storage");
        LinearLayout linearLayoutCard3 = card();
        linearLayoutCard3.addView(infoRow("ic_folder", "Backup directory", this.prefs.getString(K_DIR, DEFAULT_DIR), new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SettingsActivity.this.showDirDialog();
            }
        }));
        addCard(linearLayoutCard3);
        section("Sort");
        LinearLayout linearLayoutCard4 = card();
        linearLayoutCard4.addView(infoRow("ic_sort", "Default sort type", sortName(this.prefs.getString(K_SORT, "0")), new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SettingsActivity.this.showSortDialog();
            }
        }));
        addCard(linearLayoutCard4);
        section("About");
        addCard(aboutCard());
    }

    private void section(String str) {
        TextView textViewText = text(str.toUpperCase(Locale.US), 11.0f, c("colorOverlay1"), true);
        textViewText.setLetterSpacing(0.08f);
        LinearLayout.LayoutParams layoutParamsLp = lp(-1, -2);
        layoutParamsLp.topMargin = dp(20.0f);
        layoutParamsLp.bottomMargin = dp(8.0f);
        layoutParamsLp.leftMargin = dp(4.0f);
        this.content.addView(textViewText, layoutParamsLp);
    }

    private LinearLayout card() {
        LinearLayout linearLayoutVertical = vertical();
        linearLayoutVertical.setBackground(roundedStroke(c("colorSurface0"), c("colorSurface1"), 1.0f, 18.0f));
        linearLayoutVertical.setPadding(dp(4.0f), dp(4.0f), dp(4.0f), dp(4.0f));
        return linearLayoutVertical;
    }

    private void addCard(View view) {
        this.content.addView(view, lp(-1, -2));
    }

    private View flavorRow(final Palette palette, boolean z) {
        LinearLayout linearLayoutHorizontal = horizontal();
        linearLayoutHorizontal.setPadding(dp(12.0f), dp(12.0f), dp(12.0f), dp(12.0f));
        ripple(linearLayoutHorizontal, this.p.overlay2, 14.0f, roundedStroke(z ? withAlpha(this.accent, 0.12f) : 0, z ? this.accent : 0, z ? 1.4f : 0.0f, 14.0f));
        clickable(linearLayoutHorizontal);
        LinearLayout linearLayoutVertical = vertical();
        linearLayoutHorizontal.addView(linearLayoutVertical, lp(0, -2, 1.0f));
        linearLayoutVertical.addView(text(palette.name, 15.0f, c("colorText"), true));
        View viewText = text(palette.dark ? "Dark" : "Light", 11.0f, c("colorOverlay1"), false);
        LinearLayout.LayoutParams layoutParamsLp = lp(-2, -2);
        layoutParamsLp.topMargin = dp(3.0f);
        linearLayoutVertical.addView(viewText, layoutParamsLp);
        int[] iArr = {palette.base, palette.mauve, palette.blue, palette.text};
        LinearLayout linearLayoutHorizontal2 = horizontal();
        for (int i = 0; i < 4; i++) {
            View view = new View(this);
            view.setBackground(roundedStroke(iArr[i], palette.surface1, 1.0f, 99.0f));
            LinearLayout.LayoutParams layoutParamsLp2 = lp(dp(16.0f), dp(16.0f));
            if (i > 0) {
                layoutParamsLp2.leftMargin = dp(4.0f);
            }
            linearLayoutHorizontal2.addView(view, layoutParamsLp2);
        }
        linearLayoutHorizontal.addView(linearLayoutHorizontal2);
        TextView textViewText = text(z ? "✓" : "○", 20.0f, z ? this.accent : c("colorOverlay0"), false);
        textViewText.setGravity(17);
        LinearLayout.LayoutParams layoutParamsLp3 = lp(dp(26.0f), dp(26.0f));
        layoutParamsLp3.leftMargin = dp(10.0f);
        linearLayoutHorizontal.addView(textViewText, layoutParamsLp3);
        linearLayoutHorizontal.setOnClickListener(new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!SettingsActivity.this.p.key.equals(palette.key)) {
                    SettingsActivity.this.prefs.edit().putString(SettingsActivity.K_FLAVOR, palette.key).apply();
                    SettingsActivity.this.recreate();
                }
            }
        });
        return linearLayoutHorizontal;
    }

    private View accentDot(final String str) {
        boolean zEquals = this.prefs.getString(K_ACCENT, "mauve").equals(str);
        int iAccent = this.p.accent(str);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setPadding(dp(3.0f), dp(3.0f), dp(3.0f), dp(3.0f));
        ripple(frameLayout, this.p.overlay2, 99.0f, rounded(0, 99.0f));
        clickable(frameLayout);
        FrameLayout frameLayout2 = new FrameLayout(this);
        frameLayout2.setBackground(roundedStroke(iAccent, zEquals ? c("colorText") : 0, zEquals ? 2.0f : 0.0f, 99.0f));
        frameLayout.addView(frameLayout2, new FrameLayout.LayoutParams(dp(38.0f), dp(38.0f), 17));
        if (zEquals) {
            TextView textViewText = text("✓", 18.0f, onColor(iAccent), true);
            textViewText.setGravity(17);
            frameLayout2.addView(textViewText, new FrameLayout.LayoutParams(-1, -1));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!SettingsActivity.this.prefs.getString(SettingsActivity.K_ACCENT, "mauve").equals(str)) {
                    SettingsActivity.this.prefs.edit().putString(SettingsActivity.K_ACCENT, str).apply();
                    SettingsActivity.this.recreate();
                }
            }
        });
        frameLayout.setContentDescription(Palette.accentName(str));
        return frameLayout;
    }

    private View previewCard() {
        LinearLayout linearLayoutVertical = vertical();
        linearLayoutVertical.setBackground(roundedStroke(this.p.surface0, this.p.surface1, 1.0f, 16.0f));
        linearLayoutVertical.setPadding(dp(15.0f), dp(14.0f), dp(15.0f), dp(12.0f));
        ripple(linearLayoutVertical, this.p.overlay2, 16.0f, linearLayoutVertical.getBackground());
        LinearLayout linearLayoutHorizontal = horizontal();
        View view = new View(this);
        view.setBackground(roundedStroke(this.p.surface1, this.p.surface2, 1.0f, 5.0f));
        LinearLayout.LayoutParams layoutParamsLp = lp(dp(20.0f), dp(20.0f));
        layoutParamsLp.rightMargin = dp(9.0f);
        linearLayoutHorizontal.addView(view, layoutParamsLp);
        TextView textViewText = text("Catppuccin in AppList Backup", 16.0f, this.p.text, true);
        textViewText.setSingleLine(true);
        linearLayoutHorizontal.addView(textViewText, lp(0, -2, 1.0f));
        View view2 = new View(this);
        view2.setBackground(oval(this.accent));
        LinearLayout.LayoutParams layoutParamsLp2 = lp(dp(8.0f), dp(8.0f));
        layoutParamsLp2.leftMargin = dp(8.0f);
        linearLayoutHorizontal.addView(view2, layoutParamsLp2);
        linearLayoutVertical.addView(linearLayoutHorizontal);
        View viewText = text("arifnurrohim.my.id", 12.0f, this.p.blue, false);
        LinearLayout.LayoutParams layoutParamsLp3 = lp(-1, -2);
        layoutParamsLp3.topMargin = dp(4.0f);
        linearLayoutVertical.addView(viewText, layoutParamsLp3);
        TextView textViewText2 = text("Soothing pastel theme for the high-spirited. Cards use Surface 0 with a soft border, tags use Blue, and links use Blue.", 13.0f, this.p.subtext0, false);
        textViewText2.setMaxLines(3);
        textViewText2.setLineSpacing(dp(2.0f), 1.0f);
        LinearLayout.LayoutParams layoutParamsLp4 = lp(-1, -2);
        layoutParamsLp4.topMargin = dp(6.0f);
        linearLayoutVertical.addView(textViewText2, layoutParamsLp4);
        FlowLayout flowLayout = new FlowLayout(this);
        flowLayout.setSpacing(dp(6.0f), dp(5.0f));
        flowLayout.addView(pill("#theme", withAlpha(this.p.blue, 0.16f), this.p.blue));
        flowLayout.addView(pill("#design", withAlpha(this.p.blue, 0.16f), this.p.blue));
        LinearLayout.LayoutParams layoutParamsLp5 = lp(-1, -2);
        layoutParamsLp5.topMargin = dp(8.0f);
        linearLayoutVertical.addView(flowLayout, layoutParamsLp5);
        View viewText2 = text("Today", 11.0f, this.p.overlay1, false);
        LinearLayout.LayoutParams layoutParamsLp6 = lp(-1, -2);
        layoutParamsLp6.topMargin = dp(8.0f);
        linearLayoutVertical.addView(viewText2, layoutParamsLp6);
        return linearLayoutVertical;
    }

    private View toggleRow(String str, String str2, String str3, boolean z, final ToggleAction toggleAction) {
        LinearLayout linearLayoutHorizontal = horizontal();
        linearLayoutHorizontal.setPadding(dp(11.0f), dp(11.0f), dp(11.0f), dp(11.0f));
        linearLayoutHorizontal.addView(image(str, c("colorOverlay1")), lp(dp(24.0f), dp(24.0f)));
        LinearLayout linearLayoutVertical = vertical();
        LinearLayout.LayoutParams layoutParamsLp = lp(0, -2, 1.0f);
        layoutParamsLp.leftMargin = dp(10.0f);
        linearLayoutHorizontal.addView(linearLayoutVertical, layoutParamsLp);
        linearLayoutVertical.addView(text(str2, 14.5f, c("colorText"), true));
        TextView textViewText = text(str3, 11.5f, c("colorOverlay1"), false);
        LinearLayout.LayoutParams layoutParamsLp2 = lp(-2, -2);
        layoutParamsLp2.topMargin = dp(3.0f);
        linearLayoutVertical.addView(textViewText, layoutParamsLp2);
        final Switch r8 = new Switch(this);
        r8.setChecked(z);
        tintSwitch(r8);
        r8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.6
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                toggleAction.onChanged(z2);
            }
        });
        r8.setClickable(false);
        r8.setFocusable(false);
        linearLayoutHorizontal.addView(r8);
        linearLayoutHorizontal.setOnClickListener(new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                r8.setChecked(!r8.isChecked());
            }
        });
        return linearLayoutHorizontal;
    }

    private LinearLayout infoRow(String str, String str2, String str3, View.OnClickListener onClickListener) {
        LinearLayout linearLayoutHorizontal = horizontal();
        linearLayoutHorizontal.setPadding(dp(11.0f), dp(12.0f), dp(11.0f), dp(12.0f));
        linearLayoutHorizontal.addView(image(str, c("colorOverlay1")), lp(dp(22.0f), dp(22.0f)));
        View viewText = text(str2, 13.5f, c("colorSubtext0"), false);
        LinearLayout.LayoutParams layoutParamsLp = lp(-2, -2);
        layoutParamsLp.leftMargin = dp(10.0f);
        linearLayoutHorizontal.addView(viewText, layoutParamsLp);
        TextView textViewText = text(str3, 13.0f, c("colorText"), true);
        textViewText.setSingleLine(true);
        textViewText.setGravity(8388613);
        textViewText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        LinearLayout.LayoutParams layoutParamsLp2 = lp(0, -2, 1.0f);
        layoutParamsLp2.leftMargin = dp(10.0f);
        linearLayoutHorizontal.addView(textViewText, layoutParamsLp2);
        if (onClickListener != null) {
            clickable(linearLayoutHorizontal);
            ripple(linearLayoutHorizontal, this.p.overlay2, 10.0f, rounded(0, 10.0f));
            linearLayoutHorizontal.setOnClickListener(onClickListener);
        }
        return linearLayoutHorizontal;
    }

    private View aboutCard() {
        String str;
        LinearLayout linearLayoutCard = card();
        linearLayoutCard.setPadding(dp(16.0f), dp(16.0f), dp(16.0f), dp(12.0f));
        LinearLayout linearLayoutHorizontal = horizontal();
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getApplicationInfo().loadIcon(getPackageManager()));
        linearLayoutHorizontal.addView(imageView, lp(dp(46.0f), dp(46.0f)));
        LinearLayout linearLayoutVertical = vertical();
        LinearLayout.LayoutParams layoutParamsLp = lp(0, -2, 1.0f);
        layoutParamsLp.leftMargin = dp(12.0f);
        linearLayoutHorizontal.addView(linearLayoutVertical, layoutParamsLp);
        linearLayoutVertical.addView(text("AppList Backup", 17.0f, c("colorText"), true));
        try {
            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            str = "2.0.4";
        }
        TextView textViewText = text("Version " + str + " · " + getPackageName(), 11.5f, c("colorOverlay1"), false);
        LinearLayout.LayoutParams layoutParamsLp2 = lp(-2, -2);
        layoutParamsLp2.topMargin = dp(3.0f);
        linearLayoutVertical.addView(textViewText, layoutParamsLp2);
        linearLayoutCard.addView(linearLayoutHorizontal);
        TextView textViewText2 = text("A native app list backup tool. Back up installed apps to a simple XML file and view them later. Every surface follows the Catppuccin palette.", 12.5f, c("colorSubtext0"), false);
        textViewText2.setLineSpacing(dp(3.0f), 1.0f);
        LinearLayout.LayoutParams layoutParamsLp3 = lp(-1, -2);
        layoutParamsLp3.topMargin = dp(14.0f);
        layoutParamsLp3.bottomMargin = dp(6.0f);
        linearLayoutCard.addView(textViewText2, layoutParamsLp3);
        linearLayoutCard.addView(linkRow("ic_person", "Creator · arifnurrohim.my.id", "https://arifnurrohim.my.id/"));
        linearLayoutCard.addView(linkRow("ic_email", "jobs.arifnurrohim@gmail.com", "mailto:jobs.arifnurrohim@gmail.com"));
        linearLayoutCard.addView(linkRow("ic_material_link", "GitHub repository", "https://github.com/arifnurrohim/AppListBackup"));
        linearLayoutCard.addView(innerDivider());
        linearLayoutCard.addView(linkRow("ic_palette", "Catppuccin style guide", "https://github.com/catppuccin/catppuccin/blob/main/docs/style-guide.md"));
        linearLayoutCard.addView(innerDivider());
        linearLayoutCard.addView(linkRow("ic_material_palette", "Icons · Google Material Design Icons", "https://github.com/google/material-design-icons"));
        linearLayoutCard.addView(linkRow("ic_material_link", "Material Symbols · fonts.google.com/icons", "https://fonts.google.com/icons"));
        TextView textViewText3 = text("Your app list stays on your own device.", 10.5f, c("colorOverlay0"), false);
        textViewText3.setLineSpacing(dp(2.0f), 1.0f);
        LinearLayout.LayoutParams layoutParamsLp4 = lp(-1, -2);
        layoutParamsLp4.topMargin = dp(12.0f);
        linearLayoutCard.addView(textViewText3, layoutParamsLp4);
        return linearLayoutCard;
    }

    private View linkRow(String str, String str2, final String str3) {
        LinearLayout linearLayoutHorizontal = horizontal();
        linearLayoutHorizontal.setPadding(0, dp(12.0f), 0, dp(12.0f));
        clickable(linearLayoutHorizontal);
        ripple(linearLayoutHorizontal, this.p.overlay2, 10.0f, rounded(0, 10.0f));
        linearLayoutHorizontal.addView(image(str, this.p.blue), lp(dp(22.0f), dp(22.0f)));
        View viewText = text(str2, 13.5f, this.p.blue, false);
        LinearLayout.LayoutParams layoutParamsLp = lp(0, -2, 1.0f);
        layoutParamsLp.leftMargin = dp(8.0f);
        linearLayoutHorizontal.addView(viewText, layoutParamsLp);
        linearLayoutHorizontal.addView(image("ic_open_in_new", c("colorOverlay1")), lp(dp(18.0f), dp(18.0f)));
        linearLayoutHorizontal.setOnClickListener(new View.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    SettingsActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str3)));
                } catch (Exception e) {
                }
            }
        });
        return linearLayoutHorizontal;
    }

    private View innerDivider() {
        View view = new View(this);
        view.setBackgroundColor(this.p.surface1);
        LinearLayout.LayoutParams layoutParamsLp = lp(-1, Math.max(1, dp(0.7f)));
        layoutParamsLp.leftMargin = dp(10.0f);
        layoutParamsLp.rightMargin = dp(10.0f);
        view.setLayoutParams(layoutParamsLp);
        return view;
    }

    private TextView pill(String str, int i, int i2) {
        TextView textViewText = text(str, 11.5f, i2, true);
        textViewText.setBackground(rounded(i, 999.0f));
        textViewText.setPadding(dp(9.0f), dp(3.0f), dp(9.0f), dp(3.0f));
        return textViewText;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDirDialog() {
        final EditText editText = new EditText(this);
        editText.setText(this.prefs.getString(K_DIR, DEFAULT_DIR));
        editText.setSingleLine(true);
        editText.setTextSize(2, 14.0f);
        editText.setPadding(dp(16.0f), dp(10.0f), dp(16.0f), dp(10.0f));
        editText.setBackground(roundedStroke(c("colorSurface1"), c("colorSurface2"), 1.0f, 10.0f));
        new AlertDialog.Builder(this).setTitle("Backup directory").setView(editText).setPositiveButton("Save", new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String strTrim = editText.getText().toString().trim();
                if (strTrim.length() == 0) {
                    strTrim = SettingsActivity.DEFAULT_DIR;
                }
                SettingsActivity.this.prefs.edit().putString(SettingsActivity.K_DIR, strTrim).apply();
                SettingsActivity.this.updateRowValue(SettingsActivity.K_DIR, strTrim);
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSortDialog() {
        String string = this.prefs.getString(K_SORT, "0");
        int i = 0;
        for (int i2 = 0; i2 < SORT_VALUES.length; i2++) {
            if (SORT_VALUES[i2].equals(string)) {
                i = i2;
                break;
            }
        }
        new AlertDialog.Builder(this).setTitle("Select sort type").setSingleChoiceItems(SORT_NAMES, i, new DialogInterface.OnClickListener() { // from class: app.arifnurrohim.applistbackup.SettingsActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                SettingsActivity.this.prefs.edit().putString(SettingsActivity.K_SORT, SettingsActivity.SORT_VALUES[i3]).apply();
                SettingsActivity.this.updateRowValue(SettingsActivity.K_SORT, SettingsActivity.SORT_NAMES[i3]);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRowValue(String str, String str2) {
        for (int i = 0; i < this.content.getChildCount(); i++) {
            View childAt = this.content.getChildAt(i);
            if (childAt instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) childAt;
                for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
                    View childAt2 = linearLayout.getChildAt(i2);
                    if (childAt2 instanceof LinearLayout) {
                        LinearLayout linearLayout2 = (LinearLayout) childAt2;
                        if (linearLayout2.getChildCount() < 3) {
                            continue;
                        } else {
                            View childAt3 = linearLayout2.getChildAt(linearLayout2.getChildCount() - 1);
                            if (childAt3 instanceof TextView) {
                                TextView textView = (TextView) childAt3;
                                CharSequence text = ((TextView) linearLayout2.getChildAt(1)).getText();
                                if ((str.equals(K_DIR) && "Backup directory".contentEquals(text)) || (str.equals(K_SORT) && "Default sort type".contentEquals(text))) {
                                    textView.setText(str2);
                                    return;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    private String sortName(String str) {
        for (int i = 0; i < SORT_VALUES.length; i++) {
            if (SORT_VALUES[i].equals(str)) {
                return SORT_NAMES[i];
            }
        }
        return SORT_NAMES[0];
    }

    private void tintSwitch(Switch r6) {
        int[][] iArr = {new int[]{android.R.attr.state_checked}, new int[0]};
        int[] iArr2 = {this.accent, c("colorOverlay0")};
        int[] iArr3 = {withAlpha(this.accent, 0.45f), c("colorSurface2")};
        r6.setThumbTintList(new ColorStateList(iArr, iArr2));
        r6.setTrackTintList(new ColorStateList(iArr, iArr3));
        r6.setShowText(false);
    }

    private TextView text(CharSequence charSequence, float f, int i, boolean z) {
        TextView textView = new TextView(this);
        textView.setText(charSequence);
        textView.setTextSize(2, f);
        textView.setTextColor(i);
        textView.setTypeface(Typeface.create(z ? "sans-serif-medium" : "sans-serif", 0));
        textView.setIncludeFontPadding(false);
        return textView;
    }

    private ImageView image(String str, int i) {
        ImageView imageView = new ImageView(this);
        int iResId = resId("drawable", str);
        if (iResId != 0) {
            imageView.setImageResource(iResId);
            imageView.setColorFilter(i);
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    private LinearLayout vertical() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        return linearLayout;
    }

    private LinearLayout horizontal() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(0);
        linearLayout.setGravity(16);
        return linearLayout;
    }

    private GradientDrawable rounded(int i, float f) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadius(dp(f));
        return gradientDrawable;
    }

    private GradientDrawable roundedStroke(int i, int i2, float f, float f2) {
        GradientDrawable gradientDrawableRounded = rounded(i, f2);
        gradientDrawableRounded.setStroke(dp(f), i2);
        return gradientDrawableRounded;
    }

    private GradientDrawable oval(int i) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(i);
        return gradientDrawable;
    }

    private void ripple(View view, int i, float f, Drawable drawable) {
        view.setBackground(new RippleDrawable(ColorStateList.valueOf(withAlpha(i, 0.3f)), drawable, rounded(-1, f)));
    }

    private void clickable(View view) {
        view.setClickable(true);
        view.setFocusable(true);
    }

    private LinearLayout.LayoutParams lp(int i, int i2) {
        return new LinearLayout.LayoutParams(i, i2);
    }

    private LinearLayout.LayoutParams lp(int i, int i2, float f) {
        return new LinearLayout.LayoutParams(i, i2, f);
    }

    private int dp(float f) {
        return Math.round(f * getResources().getDisplayMetrics().density);
    }

    private int resId(String str, String str2) {
        return getResources().getIdentifier(str2, str, getPackageName());
    }

    private int c(String str) {
        TypedValue typedValue = new TypedValue();
        int iResId = resId("attr", str);
        if (iResId != 0 && getTheme().resolveAttribute(iResId, typedValue, true)) {
            return typedValue.data;
        }
        return -16777216;
    }

    private int onColor(int i) {
        double dRed = (((((double) Color.red(i)) * 0.299d) + (((double) Color.green(i)) * 0.587d)) + (((double) Color.blue(i)) * 0.114d)) / 255.0d;
        Palette palette = this.p;
        return dRed > 0.58d ? palette.crust : palette.base;
    }

    private static int withAlpha(int i, float f) {
        return (i & 16777215) | (Math.round(Math.max(0.0f, Math.min(1.0f, f)) * 255.0f) << 24);
    }

    static final class Palette {
        final int base;
        final int blue;
        final int crust;
        final boolean dark;
        final int flamingo;
        final int green;
        final String key;
        final int lavender;
        final int maroon;
        final int mauve;
        final String name;
        final int overlay1;
        final int overlay2;
        final int peach;
        final int pink;
        final int red;
        final int rosewater;
        final int sapphire;
        final int sky;
        final int subtext0;
        final int surface0;
        final int surface1;
        final int surface2;
        final int teal;
        final int text;
        final int yellow;
        static final String[] KEYS = {"latte", "frappe", "macchiato", "mocha"};
        static final String[] ACCENT_KEYS = {"rosewater", "flamingo", "pink", "mauve", "red", "maroon", "peach", "yellow", "green", "teal", "sky", "sapphire", "blue", "lavender"};
        static final String[] ACCENT_NAMES = {"Rosewater", "Flamingo", "Pink", "Mauve", "Red", "Maroon", "Peach", "Yellow", "Green", "Teal", "Sky", "Sapphire", "Blue", "Lavender"};
        private static final Palette LATTE = new Palette("latte", "Latte", false, -2323848, -2262920, -1411381, -7849489, -3010759, -1686189, -105461, -2126307, -12541909, -15232359, -16472603, -14639179, -14784779, -9271299, -11776151, -9670779, -7565407, -8618093, -3354406, -4407092, -5459778, -1052171, -2301720);
        private static final Palette FRAPPE = new Palette("frappe", "Frappe", true, -862769, -1130818, -739100, -3498266, -1604988, -1402468, -1073290, -1718128, -5844599, -8271682, -6696485, -8011300, -7558418, -4539407, -3747595, -5919282, -8156249, -7037765, -12499623, -11446419, -10327936, -13618106, -14473676);
        private static final Palette MACCHIATO = new Palette("macchiato", "Macchiato", true, -730154, -997690, -672282, -3759882, -1210474, -1140320, -677505, -1125217, -5842283, -7612982, -7219229, -8534812, -7688716, -4735496, -3484683, -5919285, -8353886, -7103817, -13223345, -11973276, -10788744, -14407878, -15197914);
        private static final Palette MOCHA = new Palette("mocha", "Mocha", true, -663332, -864819, -671001, -3430665, -816216, -1335124, -347257, -400721, -5839967, -7019819, -7742229, -9123860, -7752454, -4931842, -3287308, -5853752, -8420196, -7104078, -13553084, -12236966, -10986640, -14803410, -15658725);

        Palette(String str, String str2, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23) {
            this.key = str;
            this.name = str2;
            this.dark = z;
            this.rosewater = i;
            this.flamingo = i2;
            this.pink = i3;
            this.mauve = i4;
            this.red = i5;
            this.maroon = i6;
            this.peach = i7;
            this.yellow = i8;
            this.green = i9;
            this.teal = i10;
            this.sky = i11;
            this.sapphire = i12;
            this.blue = i13;
            this.lavender = i14;
            this.text = i15;
            this.subtext0 = i16;
            this.overlay1 = i17;
            this.overlay2 = i18;
            this.surface0 = i19;
            this.surface1 = i20;
            this.surface2 = i21;
            this.base = i22;
            this.crust = i23;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:47:0x009f  */
        int accent(String str) {
            switch (str) {
                case "rosewater":
                    return this.rosewater;
                case "flamingo":
                    return this.flamingo;
                case "pink":
                    return this.pink;
                case "red":
                    return this.red;
                case "maroon":
                    return this.maroon;
                case "peach":
                    return this.peach;
                case "yellow":
                    return this.yellow;
                case "green":
                    return this.green;
                case "teal":
                    return this.teal;
                case "sky":
                    return this.sky;
                case "sapphire":
                    return this.sapphire;
                case "blue":
                    return this.blue;
                case "lavender":
                    return this.lavender;
                case "mauve":
                default:
                    return this.mauve;
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code duplicated, block: B:20:0x0035  */
        static Palette forKey(String str) {
            if (str == null) {
                return MOCHA;
            }
            switch (str) {
                case "latte":
                    return LATTE;
                case "frappe":
                    return FRAPPE;
                case "macchiato":
                    return MACCHIATO;
                case "mocha":
                default:
                    return MOCHA;
            }
        }

        static String accentName(String str) {
            for (int i = 0; i < ACCENT_KEYS.length; i++) {
                if (ACCENT_KEYS[i].equals(str)) {
                    return ACCENT_NAMES[i];
                }
            }
            return "Mauve";
        }
    }
}
