package net.zemna.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
public class BitmapUtil {
    public static Bitmap getBitmap(Drawable d, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, 48, 48);
        d.draw(canvas);
        canvas.save();
        return bitmap;
    }
}
