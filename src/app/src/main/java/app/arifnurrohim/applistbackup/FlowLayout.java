package app.arifnurrohim.applistbackup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class FlowLayout extends ViewGroup {
    private int horizontalSpacing;
    private int verticalSpacing;

    public FlowLayout(Context context) {
        super(context);
    }

    public void setSpacing(int i, int i2) {
        this.horizontalSpacing = i;
        this.verticalSpacing = i2;
        requestLayout();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        int paddingLeft = mode == 0 ? Integer.MAX_VALUE : (size - getPaddingLeft()) - getPaddingRight();
        int i3 = 0;
        int iMax = 0;
        int iMax2 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i, i2);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (i4 > 0 && this.horizontalSpacing + i4 + measuredWidth > paddingLeft) {
                    i3 += iMax + this.verticalSpacing;
                    iMax2 = Math.max(iMax2, i4);
                    iMax = measuredHeight;
                    i4 = measuredWidth;
                } else {
                    if (i4 > 0) {
                        i4 += this.horizontalSpacing;
                    }
                    i4 += measuredWidth;
                    iMax = Math.max(iMax, measuredHeight);
                }
            }
        }
        int i6 = i3 + iMax;
        int iMax3 = Math.max(iMax2, i4);
        if (mode == 0) {
            size = iMax3;
        }
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), i6 + getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingLeft = ((i3 - i) - getPaddingLeft()) - getPaddingRight();
        int paddingLeft2 = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int iMax = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (paddingLeft2 > getPaddingLeft() && paddingLeft2 + measuredWidth > getPaddingLeft() + paddingLeft) {
                    paddingLeft2 = getPaddingLeft();
                    paddingTop += iMax + this.verticalSpacing;
                    iMax = 0;
                }
                childAt.layout(paddingLeft2, paddingTop, paddingLeft2 + measuredWidth, paddingTop + measuredHeight);
                paddingLeft2 += measuredWidth + this.horizontalSpacing;
                iMax = Math.max(iMax, measuredHeight);
            }
        }
    }
}
