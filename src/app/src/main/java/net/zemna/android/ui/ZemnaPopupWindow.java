package net.zemna.android.ui;

import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/* JADX INFO: loaded from: classes.dex */
public class ZemnaPopupWindow {
    protected final View anchor;
    private Drawable background = null;
    private View root;
    private final PopupWindow window;
    private final WindowManager windowManager;

    public ZemnaPopupWindow(View anchor) {
        this.anchor = anchor;
        this.window = new PopupWindow(anchor.getContext());
        this.window.setTouchInterceptor(new View.OnTouchListener() { // from class: net.zemna.android.ui.ZemnaPopupWindow.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != 4) {
                    return false;
                }
                ZemnaPopupWindow.this.window.dismiss();
                return true;
            }
        });
        this.windowManager = (WindowManager) this.anchor.getContext().getSystemService("window");
        onCreate();
    }

    protected void onCreate() {
    }

    protected void onShow() {
    }

    private void preShow() {
        if (this.root == null) {
            throw new IllegalStateException("setContentView was not called with a view to display.");
        }
        onShow();
        if (this.background == null) {
            this.window.setBackgroundDrawable(new BitmapDrawable());
        } else {
            this.window.setBackgroundDrawable(this.background);
        }
        this.window.setWidth(-2);
        this.window.setHeight(-2);
        this.window.setTouchable(true);
        this.window.setFocusable(true);
        this.window.setOutsideTouchable(true);
        this.window.setContentView(this.root);
    }

    public void setBackgroundDrawable(Drawable background) {
        this.background = background;
    }

    public void setContentView(View root) {
        this.root = root;
        this.window.setContentView(root);
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflator = (LayoutInflater) this.anchor.getContext().getSystemService("layout_inflater");
        setContentView(inflator.inflate(layoutResID, (ViewGroup) null));
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.window.setOnDismissListener(listener);
    }

    public void showLikePopDownMenu() {
        showLikePopDownMenu(0, 0);
    }

    public void showLikePopDownMenu(int xOffset, int yOffset) {
        preShow();
        int[] location = new int[2];
        this.anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + this.anchor.getWidth(), location[1] + this.anchor.getHeight());
        this.root.measure(-2, -2);
        int screenHeight = this.windowManager.getDefaultDisplay().getHeight();
        if (anchorRect.bottom + this.root.getMeasuredHeight() > screenHeight) {
            yOffset = (yOffset - anchorRect.height()) - this.root.getMeasuredHeight();
            this.window.setAnimationStyle(2131165189);
        } else {
            this.window.setAnimationStyle(2131165188);
        }
        this.window.showAsDropDown(this.anchor, xOffset, yOffset);
    }

    public void showLikeQuickAction() {
        showLikeQuickAction(0, 0);
    }

    public void showLikeQuickAction(int xOffset, int yOffset) {
        preShow();
        int[] location = new int[2];
        this.anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + this.anchor.getWidth(), location[1] + this.anchor.getHeight());
        this.root.measure(-2, -2);
        int rootWidth = this.root.getMeasuredWidth();
        int rootHeight = this.root.getMeasuredHeight();
        int screenWidth = this.windowManager.getDefaultDisplay().getWidth();
        int xPos = ((screenWidth - rootWidth) / 2) + xOffset;
        int yPos = (anchorRect.top - rootHeight) + yOffset;
        if (rootHeight > anchorRect.top) {
            yPos = anchorRect.bottom + yOffset;
            this.window.setAnimationStyle(2131165187);
        }
        this.window.showAtLocation(this.anchor, 0, xPos, yPos);
    }

    public void dismiss() {
        this.window.dismiss();
    }
}
