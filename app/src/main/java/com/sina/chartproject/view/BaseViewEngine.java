package com.sina.chartproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.sina.chartproject.element.DateElement;
import com.sina.chartproject.element.ElementView;

import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 * 流程控制层
 */
public abstract class BaseViewEngine {

    //绘图的view
    protected CapitalChartView mCapitalChartView;

    //具体绘制的内容区域
    protected Rect mContentRect;

    //需要绘制的元素集合
    protected List<ElementView> elementViews;

    //日期区域
    protected Rect mDateRect;

    public BaseViewEngine(Context context) {
    }

    public void setDrawRect(CapitalChartView capitalView) {
        //确定绘制区域和日期区域
        if (capitalView != null) {
            this.mCapitalChartView = capitalView;
            mContentRect = capitalView.getContentRect();
            mDateRect = capitalView.getDateRect();
        }

        //绘制每一个元素
        if (elementViews != null && elementViews.size() > 0) {
            for (ElementView element : elementViews) {
                element.contentRect(mContentRect, mDateRect);
            }
        }

    }

    public void draw(Canvas canvas, CapitalChartView capitalView) {
        //绘制每一个元素
        if (elementViews != null && elementViews.size() > 0) {
            for (ElementView element : elementViews) {
                element.draw(canvas, mContentRect);
            }
        }
    }

    public void setElementViews(List<ElementView> elementViews) {
        this.elementViews = elementViews;
    }

}
