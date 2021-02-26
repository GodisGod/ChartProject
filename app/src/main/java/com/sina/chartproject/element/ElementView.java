package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sina.chartproject.view.CapitalChartView;

/**
 * @author hongda5
 * @date 2021/2/22
 * <p>
 * 各个元素的基础类
 */
public abstract class ElementView extends PaintView {

    public ElementView(Context context) {
        super(context);
    }

    /**
     * 元素绘制方法
     *
     * @param canvas
     * @param mContentRect
     */
    public abstract void draw(Canvas canvas, Rect mContentRect);

    public abstract void contentRect(CapitalChartView capitalView, Rect mContentRect, Rect mDateRect);

    public abstract void changeSkin();

}
