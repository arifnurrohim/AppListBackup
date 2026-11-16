package app.arifnurrohim.applistbackup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import net.zemna.android.app.AppItem;

/* JADX INFO: loaded from: classes.dex */
class ViewListAdapter extends ArrayAdapter<AppItem> {
    Context _context;
    LayoutInflater _inflater;
    int _layout;

    public ViewListAdapter(Context context, int resourceId, List<AppItem> appItems) {
        super(context, 0, appItems);
        this._context = context;
        this._inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this._layout = resourceId;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Filterable
    public Filter getFilter() {
        Filter filter = new Filter() { // from class: app.arifnurrohim.applistbackup.ViewListAdapter.1
            @Override // android.widget.Filter
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            }

            @Override // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults fr = new Filter.FilterResults();
                return fr;
            }
        };
        return filter;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this._inflater.inflate(this._layout, parent, false);
        }
        AppItem item = getItem(position);
        ImageView img = (ImageView) convertView.findViewById(R.id.imgIcon);
        img.setImageDrawable(item.getIcon());
        TextView txt = (TextView) convertView.findViewById(R.id.textName);
        txt.setText(item.getAppName());
        txt.setTextColor(item.isInstalled() ? -3287308 : -816216);
        TextView txt2 = (TextView) convertView.findViewById(R.id.textPkg);
        txt2.setText(item.isInstalled() ? R.string.installed : R.string.not_installed);
        txt2.setTextColor(item.isInstalled() ? -5839967 : -816216);
        return convertView;
    }
}
