package com.sina.chartproject.gesture;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * @author: fulibo
 * 内部回调
 */
public interface InnerChartCallback {

    boolean onDown(MotionEvent ev);

    void onSingleClick(float x, float y);

    boolean onLongPress(float x, float y);

    boolean onScroll(float x, float y, MotionEvent ev, float dx, float dy);

    void onUp(MotionEvent ev);

    boolean onFling(float x, float y, MotionEvent ev, float dx, float dy);

}
