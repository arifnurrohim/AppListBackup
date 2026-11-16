package app.arifnurrohim.applistbackup.backupitem;

import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class BackupItem {
    private String backupName;
    private Date createdDate;
    private String fullPath;

    public String getBackupName() {
        return this.backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public String getFullPath() {
        return this.fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
