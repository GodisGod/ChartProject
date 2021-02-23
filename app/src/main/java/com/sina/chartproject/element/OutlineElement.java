package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.sina.chartproject.R;
import com.sina.chartproject.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * @author hongda5
 * @date 2021/2/22
 * 轮廓线和背景色：包括外部轮廓线和X/Y轴刻度线和背景色
 */
public class OutlineElement extends ElementView {
    //轮廓线画笔
    private Paint outLinePaint;
    //背景画笔
    private Paint bgPaint;
    //X轴最多分几段--纵向刻度线数量
    private int yDividerNum = 0;
    //Y轴最多分几段--横向刻度线数量
    private int xDividerNum = 2;
    //是否有横向刻度线
    private boolean hasXDivider = true;
    //是否有纵向刻度线
    private boolean hasYDivider = true;
    //绘制区域总高度
    private int height;
    //绘制区域总宽度
    private int width;
    //纵向-单位刻度高度
    public int perPxHeight;
    //横向-单位刻度宽度
    public int perPxWidth;

    //横向刻度线坐标
    private ArrayList<Point> xPointsList = new ArrayList<>();
    //纵向刻度线坐标
    private ArrayList<Point> yPointsList = new ArrayList<>();

    public OutlineElement(Context context) {
        super(context);
        outLinePaint = new Paint();
        outLinePaint.setAntiAlias(true);
        outLinePaint.setStrokeWidth(DisplayUtils.dip2px(context, 1));
        outLinePaint.setStyle(Paint.Style.STROKE);
        outLinePaint.setColor(ContextCompat.getColor(context, R.color.color_2f323a));
    }

    @Override
    public void draw(Canvas canvas, Rect contentRect) {

        //绘制横向刻度线 ==
        if (hasXDivider) {
            canvas.drawLine(contentRect.left, contentRect.top, contentRect.right, contentRect.top, outLinePaint);
            canvas.drawLine(contentRect.left, contentRect.bottom, contentRect.right, contentRect.bottom, outLinePaint);
            for (Point p : xPointsList) {
                canvas.drawLine(contentRect.left, p.y, contentRect.right, p.y, outLinePaint);
            }
        }
        //绘制纵向刻度线 ||||
        if (hasYDivider) {
            canvas.drawLine(contentRect.left, contentRect.top, contentRect.left, contentRect.bottom, outLinePaint);
            canvas.drawLine(contentRect.right, contentRect.top, contentRect.right, contentRect.bottom, outLinePaint);
            for (Point p : yPointsList) {
                canvas.drawLine(p.x, contentRect.top, p.x, contentRect.bottom, outLinePaint);
            }
        }

    }

    @Override
    public void contentRect(Rect mContentRect, Rect mDateRect) {
        initData(mContentRect);
    }

    private void initData(Rect contentRect) {
        height = contentRect.height();
        width = contentRect.width();
        Log.d("LHD", "绘图区域 = " + contentRect.toShortString());
        perPxHeight = height / (xDividerNum + 1);
        perPxWidth = width / (yDividerNum + 1);

        for (int i = 1; i <= xDividerNum; i++) {
            Point point = new Point();
            point.y = contentRect.top + i * perPxHeight;
            xPointsList.add(point);
        }

        for (int i = 1; i <= yDividerNum; i++) {
            Point point = new Point();
            point.x = contentRect.left + i * perPxWidth;
            yPointsList.add(point);
        }

    }

    @Override
    public void changeSkin() {

    }

    /**
     * @param hasXDivider 是否有横向刻度线
     * @param xDividerNum 刻度线数量
     */
    public void setHasXDivider(boolean hasXDivider, int xDividerNum) {
        this.hasXDivider = hasXDivider;
        this.xDividerNum = xDividerNum;
    }

    /**
     * @param hasYDivider 是否有纵向刻度线
     * @param yDividerNum 刻度线数量
     */
    public void setHasYDivider(boolean hasYDivider, int yDividerNum) {
        this.hasYDivider = hasYDivider;
        this.yDividerNum = yDividerNum;
    }

}
