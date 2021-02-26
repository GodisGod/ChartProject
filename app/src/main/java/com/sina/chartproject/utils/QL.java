package com.sina.chartproject.utils;

import android.util.Log;

import com.sina.chartproject.BuildConfig;

/**
 * QL：quick log
 * 快速日志打印工具类
 */
public class QL {

    private static boolean isOpenLog = BuildConfig.DEBUG;

    public static void i(String msg) {
        if (isOpenLog) {
            Log.i("QL", msg);
        }
    }

    public static void d(String msg) {
        if (isOpenLog) {
            Log.d("QL", msg);
        }
    }

    public static void e(String msg) {
        if (isOpenLog) {
            Log.e("QL", msg);
        }
    }

    public static void w(String msg) {
        if (isOpenLog) {
            Log.w("QL", msg);
        }
    }

    public static void array(String[] strings){
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s).append(",");
        }
        Log.d("QL", sb.toString());
    }

}
