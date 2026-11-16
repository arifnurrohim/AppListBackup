package net.zemna.android.app;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class AppItemDateComparable implements Comparator<AppItem> {
    @Override // java.util.Comparator
    public int compare(AppItem o1, AppItem o2) {
        long d1 = o1.getInstallationDate();
        long d2 = o2.getInstallationDate();
        if (d1 < d2) {
            return -1;
        }
        return d1 == d2 ? 0 : 1;
    }
}
