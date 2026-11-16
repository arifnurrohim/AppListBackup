package app.arifnurrohim.applistbackup.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import app.arifnurrohim.applistbackup.DashboardActivity;
import app.arifnurrohim.applistbackup.R;

/* JADX INFO: loaded from: classes.dex */
public final class UIUtils {
    public static void setTitle(Activity activity) {
        ((TextView) activity.findViewById(R.id.title_text)).setText(activity.getTitle());
    }

    public static void goHome(Context context) {
        Intent intent = new Intent(context, (Class<?>) DashboardActivity.class);
        intent.setFlags(67108864);
        context.startActivity(intent);
    }
}
