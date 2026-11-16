package net.zemna.android.app;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class AppItemComparable implements Comparator<AppItem> {
    @Override // java.util.Comparator
    public int compare(AppItem o1, AppItem o2) {
        if (o1.getAppName().compareToIgnoreCase(o2.getAppName()) < 0) {
            return -1;
        }
        return o1.getAppName().equals(o2.getAppName()) ? 0 : 1;
    }
}
