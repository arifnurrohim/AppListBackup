package net.zemna.android.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import net.zemna.android.graphics.BitmapUtil;

/* JADX INFO: loaded from: classes.dex */
public class IconCache {
    private static final String CACHE_PATH = "Android/data/app.arifnurrohim.applistbackup/cache/AppIcons";

    public static void saveIcon(AppItem item) throws IOException {
        File sdRoot = Environment.getExternalStorageDirectory();
        File cacheRoot = new File(sdRoot, CACHE_PATH);
        cacheRoot.mkdirs();
        File file = new File(cacheRoot, String.valueOf(item.getPackageName()) + "_" + String.valueOf(item.getVersionCode()) + ".zic");
        if (!file.exists()) {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            BitmapUtil.getBitmap(item.getIcon(), 48, 48).compress(Bitmap.CompressFormat.PNG, 50, bos);
            bos.flush();
            bos.close();
        }
    }

    public static BitmapDrawable readIcon(String pkgName, int versionCode) {
        File sdRoot = Environment.getExternalStorageDirectory();
        File cacheRoot = new File(sdRoot, CACHE_PATH);
        File file = new File(cacheRoot, String.valueOf(pkgName) + "_" + String.valueOf(versionCode) + ".zic");
        if (file.exists()) {
            return new BitmapDrawable(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }
        return null;
    }

    public static void deleteIcon(final String pkgName) {
        File sdRoot = Environment.getExternalStorageDirectory();
        File cacheRoot = new File(sdRoot, CACHE_PATH);
        File[] files = cacheRoot.listFiles(new FilenameFilter() { // from class: net.zemna.android.app.IconCache.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return filename.startsWith(pkgName);
            }
        });
        for (File file : files) {
            file.delete();
        }
    }
}
