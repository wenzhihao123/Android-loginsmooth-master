package com.wzh.study.login.utils.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.wzh.study.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: wenzhihao
 * @Time: 2019-12-25
 * @Description: 沉浸式状态栏处理工具
 */
public class StatusBarUtil{

    /**
     * 透明状态栏且布局可插入
     */
    public static void transparentStatusbarAndLayoutInsert(Activity activity, boolean light) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //纯白色状态栏配黑色文本
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
            statusBarLightMode(activity, light);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //适配21-22之间的
            try {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } catch (Exception e) {
            }
        } else {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View fakeStatusBarView = contentView.findViewById(R.id.statusbarutil_fake_status_bar_view);
            fakeStatusBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置状态栏字体
     * 是否选取高亮模式(即状态栏浅色背景黑色字体)
     *
     * @param activity activity
     * @param light 是否高亮 true是状态栏黑色字体， false是状态栏白色字体
     */
    public static void statusBarLightMode(Activity activity, boolean light) {
        try {
            if (Build.MANUFACTURER.equalsIgnoreCase("XIAOMI")) {
                setXMStatusBarDarkMode(activity, light);
            } else if (Build.MANUFACTURER.equalsIgnoreCase("MEIZU")){
                setMZStatusBarDarkIcon(activity, light);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
                if (light) {
                    systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //按位或，加标识
                } else {
                    systemUiVisibility = systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //按位取反再与，意在clear掉之前的标识
                }
                window.getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 适配小米低版本的，设置状态栏黑色字符
     * url: https://dev.mi.com/console/doc/detail?pId=1159
     *
     * @param darkIcon
     * @param activity
     */
    private static void setXMStatusBarDarkMode(Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     * http://open-wiki.flyme.cn/doc-wiki/index#id?79
     */
    private static void setMZStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}