package com.sina.chartproject.data;

/**
 * @author hongda5
 * @date 2021/2/22
 * 绘图所需要的的数据
 */
public class ElementData {
    private Object value;

    public ElementData(Object value) {
        this.value = value;
    }

    public float getValue() {
        if (value instanceof Float) {
            return (float) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).floatValue();
        } else {
            try {
                return Float.parseFloat(String.valueOf(value));
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public String getStrValue() {
        if (value instanceof Float) {
            return value + "";
        } else if (value instanceof String) {
            try {
                return (String) value;
            } catch (Exception e) {
                return "0";
            }
        } else {
            return "0";
        }
    }

    public void setValue(float value) {
        this.value = value;
    }


}
