package com.sina.chartproject.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/***
 * 浮点数工具类
 * @Company SINA
 *
 * @Copyright 2015-2016
 *
 * @author junfeng
 *
 * @date 2015-11-9 下午3:40:40
 *
 * @Version 3.0
 */
public class FloatUtils {
    /** +0 */
    public static float EPSILON = 0.0000001f;
    /** -0 */
    public static float EPSILONNGT = -0.0000001f;

    /**
     * 浮点零值判定
     *
     * @param val
     * @return
     */
    public static boolean isZero(float val) {
        return (val > EPSILONNGT && val < EPSILON);
    }

    public static float getFloatValue(String data) {
        float result = 0f;
        try {
            result = Float.parseFloat(data);
        } catch (Exception e) {
            result = 0f;
        }
        return result;
    }

    /**
     * 格式化，0或null，返回--
     *
     * @param data data
     * @return format string
     */
    public static String getFloatValueZeroDefault(String data) {
        if (TextUtils.isEmpty(data)) {
            return "--";
        }
        float result = getFloatValue(data, 2);
        if (isZero(result)) {
            return "--";
        } else {
            return String.valueOf(result);
        }
    }
    public static String getFloatValueZeroDefault(String data, int digit) {
        if (TextUtils.isEmpty(data)) {
            return "--";
        }
        float result = getFloatValue(data, digit);
        if (isZero(result)) {
            return "--";
        } else {
            return String.valueOf(result);
        }
    }

    /**
     * string 转换float，保留小数位，不补0
     *
     * @param data   string value
     * @param digits digital
     * @return float value
     */
    public static float getFloatValue(String data, int digits) {
        try {
            BigDecimal b = new BigDecimal(data);
            b = b.setScale(digits, BigDecimal.ROUND_HALF_UP);
            return b.floatValue();
        } catch (Exception e) {
            return 0f;
        }
    }

    /**
     * string 转换float，保留小数位，补0
     *
     * @param data   string value
     * @param digits digital
     * @return float value
     */
    public static String getFloatValue2(String data, int digits) {
        try {
            if (isZero(getFloatValue(data))){
                return "--";
            }
            BigDecimal b = new BigDecimal(Float.toString(getFloatValue(data)));
            StringBuilder sb = new StringBuilder(b.setScale(digits, BigDecimal.ROUND_HALF_UP).toString());
            return sb.toString();
        } catch (Exception e) {
            return "--";
        }
    }

    public static float parseFloatSafe(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0.0f;
        } else {
            try {
                return Float.valueOf(value);
            } catch (NumberFormatException e) {
                return 0.0f;
            }
        }
    }

    public static float parseFloatSafe(String value, int digital) {
        if (TextUtils.isEmpty(value)) {
            return 0.0f;
        } else {
            try {
                BigDecimal b = new BigDecimal(value);
                b.setScale(digital, BigDecimal.ROUND_HALF_UP);
                return b.floatValue();
            } catch (NumberFormatException e) {
                return 0.0f;
            }
        }
    }

    public static float parseFloatSafe2(String value, float defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    /**
     * 小数后自动补0
     *
     * @param value
     * @param d
     * @return
     */
    public static String format(float value, int d) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(d);
        formatter.setMaximumFractionDigits(d);
        return formatter.format(value);
    }

    /**
     * 小数后自动补0
     *
     * @param value
     * @param d
     * @return
     */
    public static String format(String value, int d) {
        float val = parseFloatSafe(value);
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(d);
        formatter.setMaximumFractionDigits(d);
        return formatter.format(val);
    }

    public static String format2(float value, int d) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(d);
        return df.format(value);
    }
}
