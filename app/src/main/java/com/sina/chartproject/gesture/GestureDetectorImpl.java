package com.sina.chartproject.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * @author: fulibo
 * 单点、滑动、滑动释放、长按、长按滑动、长按滑动释放、缩放
 */
public class GestureDetectorImpl implements View.OnTouchListener {

    private enum GestureType {
        UNDEFINED, LONG_PRESS, SCROLL
    }

    private GestureType mGestureType = GestureType.UNDEFINED;

    private float mX;
    private float mY;
    private float mDownX;
    private float mDownY;

    private final int mLongPressTimeout;
    private final int mTapTimeout;
    private final int mScaledTouchSlop;
    private final int mScaledMaximumFlingVelocity;

    private VelocityTracker mVelocityTracker;

    private SimpleOnGestureListener mCallback;

    private final Runnable mLongPressRunnable = new Runnable() {

        @Override
        public void run() {
            mGestureType = GestureType.LONG_PRESS;

            if (mCallback != null) {
                mCallback.onLongPress(mDownX, mDownY);
            }
        }
    };

    private final Runnable mTapRunnable = new Runnable() {

        @Override
        public void run() {
            mGestureType = GestureType.UNDEFINED;

            if (mCallback != null) {
                mCallback.onSingleTap(mDownX, mDownY);
            }
        }
    };

    public GestureDetectorImpl(Context context, SimpleOnGestureListener l) {
        mLongPressTimeout = ViewConfiguration.getLongPressTimeout();
        mTapTimeout       = ViewConfiguration.getTapTimeout();
        mScaledTouchSlop  = ViewConfiguration.get(context).getScaledTouchSlop();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        mCallback = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        final int action = ev.getAction();
        final float x    = ev.getX();
        final float y    = ev.getY();

        initVelocityTracker();
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (mCallback != null) {
                    mCallback.onDown(ev);
                }

                recycleVelocityTracker();

                v.postDelayed(mLongPressRunnable, mLongPressTimeout);
                mX = mDownX = x;
                mY = mDownY = y;

                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                final float dx = (mX - x);
                final float dy = (mY - y);

                switch (mGestureType) {
                    case SCROLL:
                    case LONG_PRESS: {
                        boolean consumed = false;
                        if (mCallback != null) {
                            consumed = mCallback.onScroll(mDownX, mDownY, ev, dx, dy);
                        }

                        mX = x;
                        mY = y;
                        return consumed;
                    }

                    case UNDEFINED: {
                        final float scrollX = mDownX - x;
                        final float scrollY = mDownY - y;

                        final float distance = (float) Math.sqrt(scrollX * scrollX + scrollY * scrollY);

                        if (distance >= mScaledTouchSlop) {
                            v.removeCallbacks(mLongPressRunnable);
                            mGestureType = GestureType.SCROLL;
                        }
                    }
                    break;
                }

                mX = x;
                mY = y;
            }
            break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                switch (mGestureType) {
                    case SCROLL: {
                        if (mCallback != null) {
                            mCallback.onUp(ev);
                        }

                        mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);

                        if (mCallback != null) {
                            mCallback.onFling(mDownX, mDownY, ev, mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                        }

                        mGestureType = GestureType.UNDEFINED;
                    }
                    break;

                    case LONG_PRESS: {
                        if (mCallback != null) {
                            mCallback.onUp(ev);
                        }

                        mGestureType = GestureType.UNDEFINED;
                    }
                    break;

                    default:
                        v.postDelayed(mTapRunnable, mTapTimeout);
                        break;
                }

                recycleVelocityTracker();
                v.removeCallbacks(mLongPressRunnable);
            }
            break;

            default: {
                recycleVelocityTracker();

                v.removeCallbacks(mLongPressRunnable);
                v.removeCallbacks(mTapRunnable);

                mGestureType = GestureType.UNDEFINED;
            }
            break;
        }

        return true;
    }

    private void initVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}
