package com.sina.chartproject.gesture;

import android.view.MotionEvent;

/**
 * @author: fulibo
 * @version:
 */
public class SimpleOnGestureListener {

    public boolean onDown(MotionEvent ev) {
        return false;
    }

    public void onSingleTap(float x, float y) {

    }

    public boolean onLongPress(float x, float y) {
        return false;
    }

    public boolean onScroll(float x, float y, MotionEvent ev, float dx, float dy) {
        return false;
    }

    public void onUp(MotionEvent ev) {

    }

    public boolean onFling(float x, float y, MotionEvent ev, float vx, float vy) {
        return false;
    }
}
