package net.zemna.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/* JADX INFO: loaded from: classes.dex */
public class ZemnaProgressDialog extends Dialog {
    private static ZemnaProgressDialog _progDlg = null;

    public static ZemnaProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static ZemnaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static ZemnaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static ZemnaProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        if (_progDlg == null) {
            _progDlg = new ZemnaProgressDialog(context);
        }
        _progDlg.setTitle(title);
        _progDlg.setCancelable(cancelable);
        _progDlg.setOnCancelListener(cancelListener);
        _progDlg.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(-2, -2));
        _progDlg.show();
        return _progDlg;
    }

    public static void dismissDialog() {
        if (_progDlg != null) {
            _progDlg.dismiss();
            _progDlg = null;
        }
    }

    public ZemnaProgressDialog(Context context) {
        super(context, app.arifnurrohim.applistbackup.R.style.ZemnaProgressDialog);
    }
}
