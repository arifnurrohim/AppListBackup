package app.arifnurrohim.applistbackup;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import app.arifnurrohim.applistbackup.util.ThemeUtils;

/* JADX INFO: loaded from: classes.dex */
public class Preferences extends PreferenceActivity {
    private void applyIcon(Preference preference, String str) {
        Drawable icon = preference.getIcon();
        if (icon == null) {
            preference.setIcon(ThemeUtils.tint(this, icon, str));
        }
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        if (ThemeUtils.shouldRecreate(this)) {
            recreate();
        }
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySettingsTheme(this);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("PREF_KEY_BACKUP_DIR");
        editTextPreference.setSummary(editTextPreference.getText());
        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: app.arifnurrohim.applistbackup.Preferences.1
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((CharSequence) newValue);
                return true;
            }
        });
        ListPreference listPreference = (ListPreference) findPreference("PREF_KEY_DEFAULT_SORT_TYPE");
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: app.arifnurrohim.applistbackup.Preferences.4
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference2 = (ListPreference) preference;
                int iFindIndexOfValue = listPreference2.findIndexOfValue((String) newValue);
                if (iFindIndexOfValue < 0) {
                    return true;
                }
                listPreference2.setSummary(listPreference2.getEntries()[iFindIndexOfValue]);
                return true;
            }
        });
        findPreference("PREF_KEY_SHOW_SYSTEM_APPS");
        ListPreference listPreference2 = (ListPreference) findPreference("PREF_KEY_THEME_FLAVOR");
        listPreference2.setSummary(ThemeUtils.capitalize(listPreference2.getValue()));
        listPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: app.arifnurrohim.applistbackup.Preferences.2
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String str = (String) newValue;
                preference.setSummary(ThemeUtils.capitalize(str));
                SharedPreferences.Editor editor = preference.getEditor();
                editor.putString(preference.getKey(), str);
                editor.apply();
                Preferences.this.recreate();
                return true;
            }
        });
        listPreference2.setIcon(ThemeUtils.newSwatch(ThemeUtils.getColorAttrRes(this, R.attr.colorBase), ThemeUtils.getColorAttrRes(this, R.attr.colorMauve)));
        ListPreference listPreference3 = (ListPreference) findPreference("PREF_KEY_THEME_ACCENT");
        listPreference3.setSummary(ThemeUtils.capitalize(listPreference3.getValue()));
        listPreference3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: app.arifnurrohim.applistbackup.Preferences.2
            @Override // android.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String str = (String) newValue;
                preference.setSummary(ThemeUtils.capitalize(str));
                SharedPreferences.Editor editor = preference.getEditor();
                editor.putString(preference.getKey(), str);
                editor.apply();
                Preferences.this.recreate();
                return true;
            }
        });
        listPreference3.setIcon(ThemeUtils.newSwatch(ThemeUtils.getColorAttrRes(this, R.attr.colorAccent), ThemeUtils.getColorAttrRes(this, R.attr.colorBase)));
    }
}
