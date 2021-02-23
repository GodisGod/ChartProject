package com.sina.chartproject.chart;

import android.content.Context;
import androidx.core.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * @author: fulibo
 */
public class ChartGestureDetector implements View.OnTouchListener {

    private OnChartGestureListener mOnChartGestureListener;

    // 标记，用来长按之后，补发CANCEL
    private boolean mIsLongPress;
    // 标记，用来长按之后，捕获UP操作
    private boolean mIsLongPressProgress;
    private boolean mIsScrollProgress;

    private GestureDetectorCompat mGestureDetector;

    private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            mIsLongPress = false;
            mIsLongPressProgress = false;
            mIsScrollProgress = false;

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mIsLongPress = true;
            mIsLongPressProgress = true;

            mIsScrollProgress = false;

            if (mOnChartGestureListener != null) {
                mOnChartGestureListener.onLongPress(e);
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mIsLongPressProgress) {
                if (mOnChartGestureListener != null) {
                    mOnChartGestureListener.onScrollAfterLongPress(e2);
                }
                return true;
            } else {
                mIsScrollProgress = true;


                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };

    public ChartGestureDetector(View target, OnChartGestureListener listener) {
        target.setOnTouchListener(this);

        Context context = target.getContext();
        mGestureDetector = new GestureDetectorCompat(context, mOnGestureListener);
        mOnChartGestureListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            if (mIsLongPressProgress) {
                mIsLongPressProgress = false;


                if (mOnChartGestureListener != null) {
                    mOnChartGestureListener.onReleaseAfterLongPress();
                }
            } else if (mIsScrollProgress) {
                mIsScrollProgress = false;

            }
        }

        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (mGestureDetector.onTouchEvent(ev)) {
            return true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (mIsLongPress) {
                    mIsLongPress = false;
                    MotionEvent cancel = MotionEvent.obtain(ev);
                    cancel.setAction(MotionEvent.ACTION_CANCEL);
                    mGestureDetector.onTouchEvent(cancel);
                }
                break;

            default:
                break;
        }

        return false;
    }

}