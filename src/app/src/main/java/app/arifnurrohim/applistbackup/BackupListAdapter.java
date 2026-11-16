package app.arifnurrohim.applistbackup;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.arifnurrohim.applistbackup.backupitem.BackupItem;
import java.text.SimpleDateFormat;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class BackupListAdapter extends BaseAdapter {
    List<BackupItem> _arSrc;
    Context _context;
    LayoutInflater _inflater;
    int _layout;

    public BackupListAdapter(Context context, int layout, List<BackupItem> arSrc) {
        this._context = context;
        this._inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this._arSrc = arSrc;
        this._layout = layout;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this._arSrc == null) {
            return 0;
        }
        return this._arSrc.size();
    }

    @Override // android.widget.Adapter
    public BackupItem getItem(int position) {
        return this._arSrc.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this._inflater.inflate(this._layout, parent, false);
        }
        BackupItem item = this._arSrc.get(position);
        Resources res = this._context.getResources();
        TextView txt = (TextView) convertView.findViewById(R.id.textName);
        txt.setText(item.getBackupName());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String df = String.valueOf(res.getString(R.string.creation_date)) + " : " + simpleDate.format(item.getCreatedDate());
        TextView txt2 = (TextView) convertView.findViewById(R.id.textDate);
        txt2.setText(df);
        return convertView;
    }
}
