package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sina.chartproject.utils.ElementUtils;
import com.sina.chartproject.view.CapitalChartView;

/**
 * @author hongda5
 * @date 2021/2/22
 * <p>
 * 各个元素的基础类
 */
public abstract class ElementView extends PaintView {
    protected ElementUtils elementUtils;
    protected CapitalChartView chartView;
    protected Context context;

    public ElementView(Context context) {
        super(context);
        this.context = context;
        elementUtils = new ElementUtils();
    }

    /**
     * 元素绘制方法
     *
     * @param canvas
     * @param mContentRect
     */
    public abstract void draw(Canvas canvas, Rect mContentRect);

    public void contentRect(CapitalChartView capitalView, Rect mContentRect, Rect mDateRect) {
        this.chartView = capitalView;
    }

    public abstract void changeSkin();

}
