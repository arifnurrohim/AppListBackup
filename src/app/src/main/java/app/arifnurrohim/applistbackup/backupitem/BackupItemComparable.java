package app.arifnurrohim.applistbackup.backupitem;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class BackupItemComparable implements Comparator<BackupItem> {
    @Override // java.util.Comparator
    public int compare(BackupItem o1, BackupItem o2) {
        if (o1.getCreatedDate().compareTo(o2.getCreatedDate()) < 0) {
            return -1;
        }
        return o1.getCreatedDate().equals(o2.getCreatedDate()) ? 0 : 1;
    }
}
