package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.sina.chartproject.R;
import com.sina.chartproject.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 * 横坐标元素
 */
public class DateElement extends ElementView {

    /**
     * 需要绘制的横坐标
     */
    private List<String> values;
    //横坐标分为几段
    private int totalCount = 5;
    //每一段的宽度
    public float perPxWidth;
    //横坐标刻度值
    private ArrayList<Point> xPointsList;

    public DateElement(Context context) {
        super(context);
        values = new ArrayList<>();
        xPointsList = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas, Rect mContentRect) {
        drawX(canvas, mContentRect);
    }

    @Override
    public void contentRect(Rect mContentRect, Rect mDateRect) {
        resetList();
        countXValue(mContentRect);
    }

    private void resetList() {
        xPointsList.clear();
    }

    /**
     * 计算x轴的坐标点位置和坐标值
     *
     * @param contentRect
     */
    private float countXValue(Rect contentRect) {
        perPxWidth = contentRect.width() / (totalCount - 1);
        for (int i = 0; i < totalCount; i++) {
            Point point = new Point();
            point.x = (int) (contentRect.left + perPxWidth * i);
            point.y = contentRect.bottom;
            xPointsList.add(point);
        }
        return perPxWidth;
    }

    /**
     * 绘制x轴相关信息
     *
     * @param canvas
     */
    private void drawX(Canvas canvas, Rect mDateRect) {
        for (int i = 0; i < xPointsList.size(); i++) {
            Point point = xPointsList.get(i);
            //绘制文字
            if (i < values.size()) {
                String str = values.get(i);
                if (!TextUtils.isEmpty(str)) {
                    Rect textRect = new Rect();
                    mDatePaint.getTextBounds(str, 0, str.length(), textRect);
                    mDatePaint.setTextAlign(Paint.Align.LEFT);
                    mDatePaint.setTextSize(sp10);
                    Paint.FontMetrics fm = mDatePaint.getFontMetrics();
                    float textHeight = (fm.descent - fm.ascent) / 2 - fm.bottom;
                    int x;
                    //计算第一个文字下标横坐标，只绘制第一天和最后一天
                    if (i == 0) {
                        x = point.x;
                        canvas.drawText(str, x, point.y + textHeight * 2 + dip6, mDatePaint);
                    } else if (i == xPointsList.size() - 1) {
                        //计算最后一个文字下标横坐标
                        x = point.x - textRect.width();
                        canvas.drawText(str, x, point.y + textHeight * 2 + dip6, mDatePaint);
                    } else {
                        //计算中间文字下标横坐标
                        x = point.x - textRect.width() / 2;
                        canvas.drawText(str, x, point.y + textHeight * 2 + dip6, mDatePaint);
                    }
                }
            }

        }
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public void changeSkin() {

    }

}
