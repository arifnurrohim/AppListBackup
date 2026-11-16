package net.zemna.android.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class PackageThreadLoader {
    private static final String TAG = "PackageThreadLoader";
    private final ArrayList<QueueItem> Queue = new ArrayList<>();
    private final Handler handler = new Handler();
    private QueueRunner runner = new QueueRunner(this, null);
    private Thread thread = new Thread(this.runner);

    public interface PackageLoadedListener {
        void packageLoaded(AppItem appItem);
    }

    private final class QueueItem {
        public AppItem appItem;
        public Context context;
        public PackageLoadedListener listener;

        private QueueItem() {
        }

        /* synthetic */ QueueItem(PackageThreadLoader packageThreadLoader, QueueItem queueItem) {
            this();
        }
    }

    private class QueueRunner implements Runnable {
        private QueueRunner() {
        }

        /* synthetic */ QueueRunner(PackageThreadLoader packageThreadLoader, QueueRunner queueRunner) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                while (PackageThreadLoader.this.Queue.size() > 0) {
                    final QueueItem item = (QueueItem) PackageThreadLoader.this.Queue.remove(0);
                    Log.d(PackageThreadLoader.TAG, "Queue Size : " + String.valueOf(PackageThreadLoader.this.Queue.size()));
                    Log.d(PackageThreadLoader.TAG, "Queue Item : " + item.appItem.getPackageName());
                    if (item.appItem.isLoadcompleted()) {
                        PackageThreadLoader.this.handler.post(new Runnable() { // from class: net.zemna.android.app.PackageThreadLoader.QueueRunner.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (item.listener != null) {
                                    item.listener.packageLoaded(item.appItem);
                                }
                            }
                        });
                    } else {
                        PackageManager pm = item.context.getPackageManager();
                        PackageInfo pi = null;
                        try {
                            pi = pm.getPackageInfo(item.appItem.getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        item.appItem.setAppName(pm.getApplicationLabel(pi.applicationInfo).toString());
                        BitmapDrawable bd = (BitmapDrawable) pm.getApplicationIcon(pi.applicationInfo);
                        item.appItem.setIcon(new BitmapDrawable(Bitmap.createScaledBitmap(bd.getBitmap(), 48, 48, false)));
                        item.appItem.setLoadCompleted(true);
                        PackageThreadLoader.this.handler.post(new Runnable() { // from class: net.zemna.android.app.PackageThreadLoader.QueueRunner.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (item.listener != null) {
                                    item.listener.packageLoaded(item.appItem);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public AppItem loadPackage(Context context, AppItem appItem, PackageLoadedListener listener) {
        QueueItem queueItem = null;
        if (appItem.isLoadcompleted()) {
            return appItem;
        }
        QueueItem item = new QueueItem(this, queueItem);
        item.context = context;
        item.appItem = appItem;
        item.listener = listener;
        this.Queue.add(item);
        if (this.thread.getState() == Thread.State.NEW) {
            this.thread.start();
        } else if (this.thread.getState() == Thread.State.TERMINATED) {
            this.thread = new Thread(this.runner);
            this.thread.start();
        }
        return null;
    }
}
