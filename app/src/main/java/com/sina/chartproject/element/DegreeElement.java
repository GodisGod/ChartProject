package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.sina.chartproject.utils.DisplayUtils;
import com.sina.chartproject.view.CapitalChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 * 左侧和右侧的刻度元素
 */
public class DegreeElement extends ElementView {
    private Context context;

    //纵坐标左侧刻度值
    private List<Point> leftPoints;
    //右侧刻度值
    private List<Point> rightPoints;

    private List<String> leftList;
    private List<String> rightList;

    //纵坐标分为几段
    private int divide = 3;
    //整体绘制区域的高度
    private float height;
    //每一段的高度
    public float perPxHeight;

    public DegreeElement(Context context) {
        super(context);
        this.context = context;
        leftPoints = new ArrayList<>();
        rightPoints = new ArrayList<>();
        leftList = new ArrayList<>();
        rightList = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas, Rect mContentRect) {
        drawY(canvas);
    }

    private void resetList() {
        leftPoints.clear();
    }

    /**
     * 计算Y轴坐标位置和坐标值
     *
     * @param contentRect
     */
    private void countYValue(Rect contentRect) {
        height = contentRect.height();
        perPxHeight = height / divide;
        for (int i = 0; i < divide + 1; i++) {
            //1、计算左侧刻度的坐标值
            Point leftPoint = new Point();
            leftPoint.x = (int) contentRect.left;
            leftPoint.y = (int) (contentRect.top + perPxHeight * i);
            leftPoints.add(leftPoint);
            //2、计算右侧刻度的坐标值
            Point rightPoint = new Point();
            rightPoint.x = (int) contentRect.right;
            rightPoint.y = (int) (contentRect.top + perPxHeight * i);
            rightPoints.add(rightPoint);
        }
    }

    /**
     * 绘制y轴相关信息
     *
     * @param canvas
     */
    private void drawY(Canvas canvas) {
        mDateLinePaint.setStrokeWidth(dip1);
        drawYText(canvas, leftPoints, leftList);
    }

    /**
     * 绘制文本---todo 左右绘制应该分开，待后续优化
     * @param canvas
     * @param yPointsList
     * @param yValueList
     */
    private void drawYText(Canvas canvas, List<Point> yPointsList, List<String> yValueList) {
        mDatePaint.setTextSize(sp10);
        Rect rect = new Rect();

        if (yPointsList != null && !yPointsList.isEmpty()) {
            for (int i = 0; i < yPointsList.size(); i++) {

                Point point = yPointsList.get(i);
                Point pointRight = rightPoints.get(i);

                mDateLinePaint.setStrokeWidth(DisplayUtils.dip2px(context, 0.5f));

                String yValueStr = yValueList.get(i);
                String yValueStr2 = rightList.get(i);

                drawText(true, yPointsList.size(), i, yValueStr, rect, point, canvas);
                drawText(false, rightPoints.size(), i, yValueStr2, rect, pointRight, canvas);

            }
        }
    }

    private void drawText(boolean isLeft, int size, int i, String str, Rect rect, Point point, Canvas canvas) {
        mDatePaint.getTextBounds(str, 0, str.length(), rect);
        mDatePaint.setTextAlign(isLeft ? Paint.Align.RIGHT : Paint.Align.LEFT);
        Paint.FontMetrics fm = mDatePaint.getFontMetrics();
        float textHeight = (fm.descent - fm.ascent) / 2 - fm.bottom;
        int y;
        if (i == size - 1) {
            y = point.y;
        } else if (i == 0) {
            y = (int) (point.y + textHeight * 2);
        } else {
            y = (int) (point.y + textHeight);
        }
        if (isLeft) {
            canvas.drawText(str, point.x - dip3, y, mDatePaint);
        } else {
            canvas.drawText(str, point.x + dip3, y, mDatePaint);
        }
    }

    @Override
    public void contentRect(CapitalChartView capitalView, Rect mContentRect, Rect mDateRect) {
        resetList();
        countYValue(mContentRect);
    }

    /**
     * @param leftList  左侧刻度
     * @param rightList 右侧刻度
     */
    public void setyValueList(List<String> leftList, List<String> rightList) {
        this.leftList = leftList;
        this.rightList = rightList;
    }

    @Override
    public void changeSkin() {

    }

}
