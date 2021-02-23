package com.sina.chartproject.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lifei16
 * @version 4.4.9
 * @date 2019-07-31 15:40
 */
public class DisplayUtils {

    private static DisplayMetrics dMetrics = null;

    private static int StatusBarHight = 0;

    private static int DisplayWidth = 0;
    private static int DisplayMaxWidth = 0;

    public static DisplayMetrics getDisplayMetrics(Context activity) {
        if (dMetrics == null && activity != null) {
            //modified by junfeng@ 会产生内存泄露？
//			dMetrics = new DisplayMetrics();
//			activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
            dMetrics = activity.getApplicationContext().getResources().getDisplayMetrics();
        }
        return dMetrics;
    }


    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static int getStatusBarHeight(Activity activity) {
        if (StatusBarHight == 0) {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            StatusBarHight = frame.top;
        }

        if (StatusBarHight == 0) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            try {
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                StatusBarHight = activity.getResources().getDimensionPixelSize(x);
            } catch (Exception e1) {
//				e1.printStackTrace();
                return 75;
            }
        }
        return StatusBarHight;
    }

    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dp2pxF(Context context, float dpValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float sp2pxF(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5F;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//    	Log.e("", "fontScale=" + fontScale);
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getWidthNoDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        wm.getDefaultDisplay().getMetrics(dm);
        int val = (int) Math.ceil(((float) dm.widthPixels) / dm.density);
        return val;
    }

    public static int getDisPlayWidth(Activity activity) {
        if (DisplayWidth <= 0) {
            DisplayMetrics dMetrics = getDisplayMetrics(activity);
            if (dMetrics != null) {
                if (dMetrics.widthPixels < dMetrics.heightPixels) {
                    DisplayWidth = dMetrics.widthPixels;
                } else {
                    DisplayWidth = dMetrics.heightPixels;
                }
            }
        }

        if (DisplayWidth <= 0) {
            return 480;
        } else {
            return DisplayWidth;
        }
    }

    public static int getDisPlayMaxWidth(Activity activity) {
        if (DisplayMaxWidth <= 0) {
            DisplayMetrics dMetrics = getDisplayMetrics(activity);
            if (dMetrics != null) {
                if (dMetrics.widthPixels < dMetrics.heightPixels) {
                    DisplayMaxWidth = dMetrics.heightPixels;
                } else {
                    DisplayMaxWidth = dMetrics.widthPixels;
                }
            }
        }

        if (DisplayMaxWidth <= 0) {
            return 800;
        } else {
            return DisplayMaxWidth;
        }
    }

    public static int getScreenWidth(Context activity) {
        DisplayMetrics dMetrics = getDisplayMetrics(activity);
        if (dMetrics != null) {
            return dMetrics.widthPixels;
        } else {
            return 720;
        }
    }

    public static int getScreenHeight(Context activity) {
        DisplayMetrics dMetrics = getDisplayMetrics(activity);
        if (dMetrics != null) {
            return dMetrics.heightPixels;
        } else {
            return 1280;
        }
    }

    public static int getScreenWidth(Context context, Configuration newConfig) {
        int width = 0;
        if (newConfig != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            width = newConfig.densityDpi / 160 * newConfig.screenWidthDp;
        } else {
            width = context.getResources().getDisplayMetrics().widthPixels;
        }
        return width;
    }

    //取屏幕分辨率
    public static String getScreenResolution(Context context) {
        float[] data = new float[2];
        data[0] = context.getResources().getDisplayMetrics().widthPixels;
        data[1] = context.getResources().getDisplayMetrics().heightPixels;

        StringBuilder sb = new StringBuilder();
        sb.append(data[0]).append('x').append(data[1]);
        return sb.toString();
    }

    /**
     * 比较2个日期的先后
     * 当T1:2018-08-27 T2:2018-08-24，返回true
     *
     * @param time1 T1
     * @param time2 T2
     * @return T1 AFTER T2
     */
    public static boolean compare(String time1, String time2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date a = sdf.parse(time1);
            Date b = sdf.parse(time2);
            return a.after(b);
        } catch (ParseException e) {
            ;
        }
        return false;
    }

    public static int getUnitSize(Context mcontext, int unit, int size) {
        return (int) TypedValue.applyDimension(unit, size, mcontext.getResources().getDisplayMetrics());

    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public static int getResource(Context context, String imageName) {
        int resId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        return resId;
    }
}
