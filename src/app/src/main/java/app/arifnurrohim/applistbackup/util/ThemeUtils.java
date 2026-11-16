package app.arifnurrohim.applistbackup.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import app.arifnurrohim.applistbackup.R;
import java.util.Locale;
import java.util.WeakHashMap;

/* JADX INFO: loaded from: classes.dex */
public final class ThemeUtils {
    private static final WeakHashMap sAppliedThemes = new WeakHashMap();

    private ThemeUtils() {
    }

    private static void applyOverlays(Activity activity) {
        String flavor = getFlavor(activity);
        String accent = getAccent(activity);
        Resources resources = activity.getResources();
        String packageName = activity.getPackageName();
        int identifier = resources.getIdentifier("ThemeOverlay_Flavor_" + flavor, "style", packageName);
        int identifier2 = resources.getIdentifier("ThemeOverlay_Accent_" + flavor + "_" + accent, "style", packageName);
        Resources.Theme theme = activity.getTheme();
        theme.applyStyle(identifier, true);
        theme.applyStyle(identifier2, true);
        sAppliedThemes.put(activity, getThemeKey(activity));
    }

    public static void applySettingsTheme(Activity activity) {
        setBaseTheme(activity, "SettingsTheme");
        applyOverlays(activity);
    }

    public static void applyTheme(Activity activity) {
        setBaseTheme(activity, "Theme");
        applyOverlays(activity);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1);
    }

    public static String getAccent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("PREF_KEY_THEME_ACCENT", "mauve");
    }

    public static int getColorAttr(Context context, String str) {
        int identifier = context.getResources().getIdentifier(str, "attr", context.getPackageName());
        if (identifier != 0) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(identifier, typedValue, true)) {
            return typedValue.data;
        }
        return 0;
    }

    public static int getColorAttrRes(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            return typedValue.data;
        }
        return 0;
    }

    public static String getFlavor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("PREF_KEY_THEME_FLAVOR", "mocha");
    }

    public static String getThemeKey(Context context) {
        return getFlavor(context) + ":" + getAccent(context);
    }

    public static Drawable newSwatch(int i, int i2) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(i);
        gradientDrawable.setStroke(2, i2);
        gradientDrawable.setSize(48, 48);
        return gradientDrawable;
    }

    private static void setBaseTheme(Activity activity, String str) {
        activity.setTheme(activity.getResources().getIdentifier(str, "style", activity.getPackageName()));
    }

    public static boolean shouldRecreate(Activity activity) {
        String themeKey = getThemeKey(activity);
        String str = (String) sAppliedThemes.get(activity);
        return (str == null || str.equals(themeKey)) ? false : true;
    }

    public static Drawable tint(Context context, Drawable drawable, String str) {
        if (drawable != null) {
            drawable.setTint(getColorAttrRes(context, R.attr.colorSubtext0));
        }
        return drawable;
    }
}
