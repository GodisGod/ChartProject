package com.sina.chartproject.gesture;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author: fulibo
 */
public class LineGestureDetector {

    private GestureDetectorImpl mDetector;
    private InnerChartCallback  mCallback;
    private View mView;

    private SimpleOnGestureListener mOnGestureCallback = new SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent ev) {
            if (mCallback != null) {
                return mCallback.onDown(ev);
            }

            return super.onDown(ev);
        }

        @Override
        public void onSingleTap(float x, float y) {
            if (mCallback != null) {
                mCallback.onSingleClick(x, y);
            }
        }

        @Override
        public boolean onLongPress(float x, float y) {
            if (mCallback != null) {
                return mCallback.onLongPress(x, y);
            }

            return super.onLongPress(x, y);
        }

        @Override
        public boolean onScroll(float x, float y, MotionEvent ev, float dx, float dy) {
            if (mCallback != null) {
                return mCallback.onScroll(x, y, ev, dx, dy);
            }

            return super.onScroll(x, y, ev, dx, dy);
        }

        @Override
        public boolean onFling(float x, float y, MotionEvent ev, float vx, float vy) {
            if (mCallback != null) {
                return mCallback.onFling(x, y, ev, vx, vy);
            }

            return super.onFling(x, y, ev, vx, vy);
        }

        @Override
        public void onUp(MotionEvent ev) {
            if (mCallback != null) {
                mCallback.onUp(ev);
            }
        }
    };

    public LineGestureDetector(View v, InnerChartCallback c) {
        mView     = v;
        mCallback = c;
        mDetector = new GestureDetectorImpl(v.getContext(), mOnGestureCallback);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (mDetector.onTouch(mView, ev)) {
            return true;
        }

        return false;
    }
}
