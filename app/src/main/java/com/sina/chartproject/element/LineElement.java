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

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/22
 * 画折线图
 */
public class LineElement extends ElementView {

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
    private float[] maxAndMinSuccessNum;

    /**
     * 全局变量
     */
    private int height;
    public float perPxWidth;

    public LineElement(Context context, List<ElementData> list) {
        super(context);
        this.context = context;
        this.list = list;
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

    @Override
    public void draw(Canvas canvas, Rect mContentRect) {
        //绘制折线
        drawLine(canvas, mContentRect);

        //绘制渐变色背景

    }

    @Override
    public void contentRect(Rect mContentRect, Rect mDateRect) {
        //收集绘制数据
        initDrawData(mContentRect);
    }

    private void initDrawData(Rect contentRect) {
        linePoints.clear();
        linePath.reset();
        height = contentRect.height();
        Log.d("LHD", "绘制区域 = " + contentRect.toShortString());

        perPxWidth = contentRect.width() / ( list.size() - 1);

        maxAndMinSuccessNum = elementUtils.getMaxData(list);

        float max = maxAndMinSuccessNum[0];
        float min = maxAndMinSuccessNum[1];

        for (int i = 0; i < list.size(); i++) {
            Point point = new Point();
            point.x = (int) (contentRect.left + i * perPxWidth);

            //获取当前值
            float value = list.get(i).getValue();
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

}
