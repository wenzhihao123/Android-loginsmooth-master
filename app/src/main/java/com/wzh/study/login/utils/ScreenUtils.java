package com.wzh.study.login.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * @Author: wenzhihao
 * @Time: 2020-01-06
 * @Description: ScreenUtils
 */
public class ScreenUtils {
    //get real height of screen
    public static int getRealScreenHeight(Context mContext) {
        if (mContext == null){
            return 0;
        }
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Gets the size of the display,
            display.getRealSize(outPoint);
        } else {
            //Gets the size of the display except virtual navigation's height
            display.getSize(outPoint);
        }
        int mRealSizeHeight;
        mRealSizeHeight = outPoint.y;
        return mRealSizeHeight;
    }

}
