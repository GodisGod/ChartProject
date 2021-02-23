package com.sina.chartproject.element;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.sina.chartproject.R;
import com.sina.chartproject.utils.DisplayUtils;

/**
 * @author hongda5
 * @date 2021/2/22
 * 画笔层
 */
public class PaintView {

    //日期轴线画笔
    protected Paint mDateLinePaint;
    //日期文字画笔
    protected Paint mDatePaint;

    protected int dip1;
    protected int dip3;
    protected int dip6;
    protected int dip7;
    protected int dip8;
    protected int dip64;
    protected int dip117;
    protected int sp10;

    public PaintView(Context context) {
        dip1 = DisplayUtils.dip2px(context, 1);
        dip3 = DisplayUtils.dip2px(context, 3);
        dip6 = DisplayUtils.dip2px(context, 6);
        dip7 = DisplayUtils.dip2px(context, 7);
        dip8 = DisplayUtils.dip2px(context, 8);
        dip64 = DisplayUtils.dip2px(context, 64);
        dip117 = DisplayUtils.dip2px(context, 117);
        sp10 = DisplayUtils.sp2px(context, 12);
        initPaints(context);
    }


    /**
     * 初始化画笔
     *
     * @param context
     */
    public void initPaints(Context context) {
        //是否是夜间版
        boolean isBlack = true;
        //日期轴线画笔
        mDateLinePaint = new Paint();
        mDateLinePaint.setAntiAlias(true);
        mDateLinePaint.setStrokeWidth(DisplayUtils.dip2px(context, 1));
        mDateLinePaint.setStyle(Paint.Style.STROKE);
        mDateLinePaint.setColor(isBlack ? ContextCompat.getColor(context, R.color.color_2f323a)
                : ContextCompat.getColor(context, R.color.color_e5e6f2));
        //日期画笔
        mDatePaint = new Paint();
        mDatePaint.setAntiAlias(true);
        mDatePaint.setStyle(Paint.Style.FILL);
        mDatePaint.setTextSize(DisplayUtils.dip2px(context, 12));
        mDatePaint.setColor(isBlack ? ContextCompat.getColor(context, R.color.color_9a9ead)
                : ContextCompat.getColor(context, R.color.color_747985));
    }

}
