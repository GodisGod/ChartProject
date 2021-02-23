package com.sina.chartproject.chart;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * @author: fulibo
 */
public interface OnChartGestureListener {


    void onLongPress(MotionEvent e);

    void onScrollAfterLongPress(MotionEvent e);

    void onReleaseAfterLongPress();

}