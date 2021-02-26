package com.sina.chartproject.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.sina.chartproject.element.ElementView;
import com.sina.chartproject.gesture.InnerChartCallback;
import com.sina.chartproject.gesture.LineGestureDetector;
import com.sina.chartproject.utils.QL;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda5
 * @date 2021/2/23
 */
public abstract class GestureView extends View implements InnerChartCallback {

    private ViewGroup needBlockViewGroup;

    private LineGestureDetector mDetector;
    private LineOuterGestureCallback gestureCallback;

    private List<LineOuterGestureCallback> callbacks;

    private boolean mCrossPress;

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDetector = new LineGestureDetector(this, this);
        callbacks = new ArrayList<>();
    }

    public abstract Rect getMainView();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            boolean consumed = super.onTouchEvent(event);
            if (needBlockViewGroup != null) {
                needBlockViewGroup.requestDisallowInterceptTouchEvent(consumed);
            }
            getParent().requestDisallowInterceptTouchEvent(consumed);
            return consumed;
        }
    }

    @Override
    public boolean onDown(MotionEvent ev) {
        return false;
    }

    @Override
    public void onSingleClick(float x, float y) {
        if (gestureCallback != null) {
            gestureCallback.onSingleClick();
        }
        if (callbacks != null && callbacks.size() > 0) {
            for (LineOuterGestureCallback c : callbacks) {
                c.onSingleClick();
            }
        }
    }

    @Override
    public boolean onLongPress(float x, float y) {
        if (gestureCallback != null) {
            gestureCallback.onLongPress(x, y);
            mCrossPress = true;
            return true;
        }

        if (callbacks != null && callbacks.size() > 0) {
            for (LineOuterGestureCallback c : callbacks) {
                c.onLongPress(x, y);
            }
            mCrossPress = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean onScroll(float x, float y, MotionEvent ev, float dx, float dy) {
//        QL.d("LHD x = " + x + " y = " + y + " ev.x = " + ev.getX() + " dx = " + dx + " dy = " + dy);
        if (gestureCallback != null && mCrossPress) {
            gestureCallback.onScrollAfterLongPress(getMainView().left + ev.getX(), getMainView().top + ev.getY());
            return true;
        }
        if (callbacks != null && callbacks.size() > 0) {
            for (LineOuterGestureCallback c : callbacks) {
                c.onScrollAfterLongPress(dx, dy);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onUp(MotionEvent ev) {
        mCrossPress = false;
        if (gestureCallback != null) {
            gestureCallback.onUp();
        }
    }

    @Override
    public boolean onFling(float x, float y, MotionEvent ev, float dx, float dy) {
        if (gestureCallback != null) {
            gestureCallback.onFling(x, y);
        }
        return false;
    }

    public void setNeedBlockViewGroup(ViewGroup needBlockViewGroup) {
        this.needBlockViewGroup = needBlockViewGroup;
    }

    public void setGestureCallback(LineOuterGestureCallback callback) {
        this.gestureCallback = callback;
    }


    public void addElementViewsGesture(LineOuterGestureCallback callback) {
        callbacks.add(callback);
    }


}
