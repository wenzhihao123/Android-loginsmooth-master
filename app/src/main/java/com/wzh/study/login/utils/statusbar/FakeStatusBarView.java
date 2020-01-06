package com.wzh.study.login.utils.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 和状态栏高度一样的View
 */
public class FakeStatusBarView extends View {

    private static int statusBarHeight;

    public FakeStatusBarView(Context context) {
        super(context);
        init();
    }

    public FakeStatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FakeStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (statusBarHeight == 0) {
            statusBarHeight = getStatusHeight(getContext());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = statusBarHeight;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 获得状态栏的高度
     * @param context
     * @return
     */
    public int getStatusHeight(Context context) {
        int statusHeight = 0;
        if (statusHeight <= 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusHeight;
    }
}