package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;

import com.sina.chartproject.R;
import com.sina.chartproject.data.ElementData;
import com.sina.chartproject.utils.DisplayUtils;
import com.sina.chartproject.utils.ElementUtils;
import com.sina.chartproject.utils.FloatUtils;
import com.sina.chartproject.utils.QL;
import com.sina.chartproject.view.CapitalChartView;
import com.sina.chartproject.view.LineOuterGestureCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 * 画折线图
 */
public class LineElement extends ElementView implements LineOuterGestureCallback {

    private CapitalChartView chartView;

    private ElementUtils elementUtils;
    private Context context;
    //折线图画笔
    private Paint linePaint;
    //数据点
    public final ArrayList<Point> linePoints;
    //折线路径
    private Path linePath;

    //原始数据
    private List<ElementData> list;
    private List<ElementData> drawList;
    private float[] maxAndMinSuccessNum;

    /**
     * 全局变量
     */
    private float height;
    public float perPxWidth;

    //滑动相关
    //一页最多显示多少条数据
    private int maxDataNum = 20;
    private int startIndex = 0;
    private int endIndex = 0;
    //超过一屏数据则开启滑动
    private boolean needScroll = false;

    public LineElement(Context context, List<ElementData> list) {
        super(context);
        this.context = context;
        this.list = list;
        drawList = new ArrayList<>();

        if (list != null) {
            if (list.size() > maxDataNum) {
                drawList.addAll(list.subList(0, maxDataNum));
                startIndex = 0;
                endIndex = maxDataNum;
                needScroll = true;
            } else {
                drawList.addAll(list);
                startIndex = 0;
                endIndex = list.size();
                needScroll = false;
            }
        }
        QL.d("LHD  添加drawList = "+drawList.size());

        //成功率画笔
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(ContextCompat.getColor(context, R.color.color_fa2b4c));
        linePaint.setStrokeWidth(DisplayUtils.dip2px(context, 1));

        linePoints = new ArrayList<>();
        linePath = new Path();

        elementUtils = new ElementUtils();

    }

    private Canvas canvas;

    @Override
    public void draw(Canvas canvas, Rect mContentRect) {
        this.canvas = canvas;
        //绘制折线
        drawLine(canvas, mContentRect);
        //绘制节点
        for (int i = 0; i < linePoints.size(); i++) {
            Point p = linePoints.get(i);
            canvas.drawCircle(p.x, p.y, dip3, linePaint);
            //绘制辅助数字
            linePaint.setTextSize(dip8);
            canvas.drawText("" + i, p.x, p.y, linePaint);
        }


        //绘制渐变色背景

    }

    @Override
    public void contentRect(CapitalChartView capitalView, Rect mContentRect, Rect mDateRect) {
        this.chartView = capitalView;
        //收集绘制数据
        initDrawData(mContentRect);
    }

    private Rect contentRect;

    private void initDrawData(Rect contentRect) {
        this.contentRect = contentRect;
        linePoints.clear();
        linePath.reset();
        height = contentRect.height();

        if (drawList.size() < maxDataNum) {
            perPxWidth = contentRect.width() / (drawList.size() - 1);
        } else {
            perPxWidth = contentRect.width() / (maxDataNum - 1);
        }

        Log.d("LHD", "绘制区域 = " + contentRect.toShortString() + "  drawList = " + drawList.size() + "  perPxWidth = " + perPxWidth);

        maxAndMinSuccessNum = elementUtils.getMaxData(drawList);

        float max = maxAndMinSuccessNum[0];
        float min = maxAndMinSuccessNum[1];

        for (int i = 0; i < drawList.size(); i++) {
            Point point = new Point();
            point.x = (int) (contentRect.left + i * perPxWidth);

            //获取当前值
            float value = drawList.get(i).getValue();
            //与最大值比较得出当前值的y轴坐标
            float diffValue;
            if (FloatUtils.isZero(max - min)) {
                diffValue = 0f;
            } else {
                diffValue = (max - value) / (max - min);
            }
            point.y = (int) (contentRect.top + diffValue * height);
//            Log.d("LHD", "x坐标" + point.x + " y坐标" + point.y);
            linePoints.add(point);
        }

        initPath(linePoints, linePath);

    }

    private void initPath(ArrayList<Point> points, Path path) {
        if (points != null && !points.isEmpty()) {
            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                if (i == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            }
        }
    }

    private void drawLine(Canvas canvas, Rect mContentRect) {
        canvas.drawPath(linePath, linePaint);
    }

    @Override
    public void changeSkin() {

    }

    public void setLineConfig(@IdRes int colorId) {
        linePaint.setColor(ContextCompat.getColor(context, colorId));
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    @Override
    public void onSingleClick() {

    }

    @Override
    public void onLongPress(float x, float y) {

    }

    private int lastNewNum = 0;

    @Override
    public void onScrollAfterLongPress(float dx, float dy) {
        Log.d("LHD", "折线图滑动 dx = " + dx + "  dy = " + dy + "  perPxWidth = " + perPxWidth);
        float absDx = Math.abs(dx);
        if (absDx > perPxWidth) {
            QL.d("LHD 重新计算");
            int newNum = (int) (dx / perPxWidth);
            if (lastNewNum == newNum && endIndex < list.size() - 1) {
                return;
            }
            lastNewNum = newNum;
            int maxNum = list.size();
            int diff = maxNum - endIndex - 1;
            if (newNum <= diff) {
                startIndex += newNum;
                endIndex += newNum;
            } else {
                startIndex += diff;
                endIndex += diff;
            }
            QL.d("LHD 新的start = " + startIndex + "  end = " + endIndex);
            if (startIndex <= 0) {
                startIndex = 0;
            }
            if (endIndex <= 20) {
                startIndex = 0;
                endIndex = 20;
            }

            QL.d("LHD  新的条目数量 newNum = " + newNum + " diff = " + diff + "  start = " + startIndex + "  end = " + endIndex);
            if (needScroll) {
                if (startIndex < endIndex && endIndex < list.size()) {
                    drawList.clear();
                    drawList.addAll(list.subList(startIndex, endIndex));
                    initDrawData(contentRect);
//                    draw(canvas, contentRect);
                    chartView.invalidate();
                    QL.d("LHD 重新绘制图表");
                }
            }
        }

    }

    @Override
    public void onFling(float x, float y) {

    }

    @Override
    public void onUp() {

    }


}
