package app.arifnurrohim.applistbackup.util;

import android.content.Context;
import android.preference.PreferenceManager;
import app.arifnurrohim.applistbackup.DomAppItemParser;
import app.arifnurrohim.applistbackup.backupitem.BackupItem;
import app.arifnurrohim.applistbackup.backupitem.BackupItemComparable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import net.zemna.android.app.AppItem;

/* JADX INFO: loaded from: classes.dex */
public final class BackupItemUtil {
    static String BACKUP_PATH = "Zemna AppList Backup";

    public static String getBackupDir(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("PREF_KEY_BACKUP_DIR", "/storage/emulated/0/Documents/AppList Backup");
    }

    public static List<BackupItem> loadBackupList(Context context) {
        File appDirectory = new File(getBackupDir(context));
        appDirectory.mkdirs();
        File[] files = appDirectory.listFiles(new FilenameFilter() { // from class: app.arifnurrohim.applistbackup.util.BackupItemUtil.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".xml");
            }
        });
        List<BackupItem> list = new ArrayList<>();
        for (File file : files) {
            BackupItem item = new BackupItem();
            item.setBackupName(file.getName());
            item.setFullPath(file.getAbsolutePath());
            item.setCreatedDate(new Date(file.lastModified()));
            list.add(item);
        }
        BackupItemComparable comparator = new BackupItemComparable();
        Collections.sort(list, comparator);
        return list;
    }

    public static List<AppItem> loadBackupFile(String fileFullPath) {
        List<AppItem> list = DomAppItemParser.parse(fileFullPath);
        return list;
    }

    public static void deleteBackupFile(BackupItem backupItem) {
        File file = new File(backupItem.getFullPath());
        file.delete();
    }

    public static String makeBackupFile(Context context, InputStream inputStream) throws IOException {
        File appDir = new File(getBackupDir(context));
        appDir.mkdirs();
        Date date = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd_'at'_HH-mm-ss");
        String df = simpleDate.format(date);
        File file = new File(appDir, "AppList_" + df + ".xml");
        FileOutputStream fos = new FileOutputStream(file);
        while (true) {
            int value = inputStream.read();
            if (value >= 0) {
                fos.write(value);
            } else {
                fos.close();
                return file.getAbsolutePath();
            }
        }
    }
}
