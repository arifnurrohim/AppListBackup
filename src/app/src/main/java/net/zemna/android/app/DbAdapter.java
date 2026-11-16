package net.zemna.android.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class DbAdapter {
    private static final String DATABASE_CREATE = "CREATE TABLE apps (pkgName text not null, locale text not null,version integer not null, appName text not null,PRIMARY KEY (pkgName, locale));";
    private static final String DATABASE_NAME = "appList.db";
    private static final String DATABASE_TABLE = "apps";
    private static final int DATABASE_VERSION = 4;
    public static final String KEY_APPNAME = "appName";
    public static final String KEY_LOCALE = "locale";
    public static final String KEY_PKGNAME = "pkgName";
    public static final String KEY_VERSION = "version";
    private static final String TAG = "ZEMNA";
    private final Context _context;
    private SQLiteDatabase _db;
    private DatabaseHelper _dbHelper;

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DbAdapter.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 4);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DbAdapter.DATABASE_CREATE);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DbAdapter.TAG, "Upgrading db from version" + oldVersion + " to" + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS apps");
            onCreate(db);
        }
    }

    public DbAdapter(Context ctx) {
        this._context = ctx;
    }

    public DbAdapter open(boolean isWritable) throws SQLException {
        this._dbHelper = new DatabaseHelper(this._context);
        if (isWritable) {
            this._db = this._dbHelper.getWritableDatabase();
        } else {
            this._db = this._dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        this._dbHelper.close();
    }

    public SQLiteDatabase getDB() {
        return this._db;
    }

    public long insertApp(AppItem item) throws IOException {
        if (exists(item.getPackageName())) {
            updateApp(item);
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PKGNAME, item.getPackageName());
        initialValues.put(KEY_LOCALE, Locale.getDefault().toString());
        initialValues.put(KEY_VERSION, Integer.valueOf(item.getVersionCode()));
        initialValues.put(KEY_APPNAME, item.getAppName());
        IconCache.saveIcon(item);
        return this._db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteApp(String pkgName) {
        IconCache.deleteIcon(pkgName);
        return this._db.delete(DATABASE_TABLE, "pkgName=?", new String[]{pkgName}) > 0;
    }

    public ArrayList<AppItem> getAllApps() {
        ArrayList<AppItem> list = new ArrayList<>();
        Cursor cursor = this._db.query(DATABASE_TABLE, new String[]{KEY_PKGNAME, KEY_LOCALE, KEY_VERSION, KEY_APPNAME}, "locale=?", new String[]{Locale.getDefault().toString()}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                AppItem item = makeItem(cursor);
                if (item != null) {
                    list.add(item);
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    public AppItem getApp(String pkgName) throws SQLException {
        AppItem item = null;
        Cursor cursor = this._db.query(true, DATABASE_TABLE, new String[]{KEY_PKGNAME, KEY_LOCALE, KEY_VERSION, KEY_APPNAME}, "pkgName='" + pkgName + "'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            item = makeItem(cursor);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return item;
    }

    private AppItem makeItem(Cursor cursor) {
        AppItem item = new AppItem();
        item.setPackageName(cursor.getString(cursor.getColumnIndex(KEY_PKGNAME)));
        item.setVersionCode(cursor.getInt(cursor.getColumnIndex(KEY_VERSION)));
        item.setAppName(cursor.getString(cursor.getColumnIndex(KEY_APPNAME)));
        BitmapDrawable icon = IconCache.readIcon(item.getPackageName(), item.getVersionCode());
        if (icon == null) {
            return null;
        }
        item.setIcon(icon);
        item.setExistInDB(true);
        return item;
    }

    public boolean exists(String pkgName) {
        String locale = Locale.getDefault().toString();
        Cursor cursor = this._db.query(true, DATABASE_TABLE, new String[]{KEY_PKGNAME, KEY_VERSION}, "pkgName='" + pkgName + "' AND " + KEY_LOCALE + "='" + locale + "'", null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return false;
    }

    public AppItem getItem(String pkgName) throws SQLException {
        return null;
    }

    public boolean updateApp(AppItem item) throws IOException {
        ContentValues args = new ContentValues();
        args.put(KEY_VERSION, Integer.valueOf(item.getVersionCode()));
        args.put(KEY_APPNAME, item.getAppName());
        IconCache.saveIcon(item);
        return this._db.update(DATABASE_TABLE, args, "pkgName=? AND locale=?", new String[]{item.getPackageName(), Locale.getDefault().toString()}) > 0;
    }
}
