package com.sina.chartproject.utils;

import android.text.TextUtils;

import com.sina.chartproject.data.ElementData;

import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 */
public class ElementUtils {

    public float[] getMaxData(List<ElementData> list) {
        if (list == null || list.size() <= 0) {
            return new float[]{0, 0};
        }
        float[] maxAndMin = new float[2];
        float max = list.get(0).getValue();
        float min = max;
        for (ElementData f : list) {
            Float cur = f.getValue();
            if (cur >= max) {
                max = cur;
            }
            if (cur < min) {
                min = cur;
            }
        }

        max = getMaxMultipleFiveData(max, 10);
        min = getMinMultipleFiveData(min, 10, false);

        maxAndMin[0] = max;
        maxAndMin[1] = min;
        return maxAndMin;
    }

    /**
     * 取离一个数最近的比它大的5的倍数，如93最近的比它大的5的倍数就是95
     *
     * @param value
     * @param multi 倍数
     * @return
     */
    public int getMaxMultipleFiveData(float value, int multi) {

        //商
        int merchant = (int) (value / multi);

        int max = merchant * multi + multi;

        return max;
    }

    public int getMinMultipleFiveData(float value, int multi, boolean canBeNegative) {

        //商
        int merchant = (int) (value / multi);

        //余
        float remainder = value % 5;

        int min = merchant * multi - multi;
        if (min <= 0 && !canBeNegative) {
            min = 0;
        }

        return min;
    }


}
