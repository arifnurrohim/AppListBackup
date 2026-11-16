package app.arifnurrohim.applistbackup;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;
import net.zemna.android.app.AppItem;

/* JADX INFO: loaded from: classes.dex */
class AppItemAdapter extends ArrayAdapter<AppItem> {
    private LayoutInflater inflater;
    private int resourceId;

    public AppItemAdapter(Context context, int resourceId, List<AppItem> appItems) {
        super(context, 0, appItems);
        this.resourceId = 0;
        this.resourceId = resourceId;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resourceId, parent, false);
        }
        Resources res = getContext().getResources();
        AppItem item = getItem(position);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String df = String.valueOf(res.getString(R.string.installation_date)) + " : " + simpleDate.format(Long.valueOf(item.getInstallationDate()));
        TextView txt = (TextView) convertView.findViewById(R.id.textDate);
        txt.setText(df);
        CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.chkItem);
        chkBox.setVisibility(0);
        chkBox.setChecked(((ListView) parent).isItemChecked(position));
        chkBox.setFocusable(false);
        chkBox.setClickable(false);
        TextView txt2 = (TextView) convertView.findViewById(R.id.textName);
        txt2.setText(item.getAppName());
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        imgIcon.setImageDrawable(item.getIcon());
        return convertView;
    }
}
