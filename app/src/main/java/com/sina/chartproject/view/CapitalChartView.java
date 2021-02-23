package com.sina.chartproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

import com.sina.chartproject.R;
import com.sina.chartproject.chart.BaseRenderView;
import com.sina.chartproject.utils.DisplayUtils;

/**
 * @author hongda5
 * @date 2021/2/22
 * 确定基本绘制区域和长按事件
 */
public class CapitalChartView extends View {

    //整个view的宽度
    private int mWidth;
    //整个view的高度
    private int mHeight;
    //view的左边距
    protected int mPaddingLeft;
    //view的右边距
    protected int mPaddingRight;
    //view的上边距
    protected int mPaddingTop;
    //view的下边距
    protected int mPaddingBottom;
    //主体内容绘制区域
    protected Rect mContentRect;
    //底部横坐标刻度线区域
    protected Rect mDateRect;
    //当前显示的View
    private BaseViewEngine mCurrentView;

    //按下的X的坐标
    private float mPressX;
    private LongPressRunnable mLongPressRunnable;

    //开关
    /**
     * 是否需要绘制日期
     */
    private boolean needDate = true;

    public CapitalChartView(Context context) {
        this(context, null);
    }

    public CapitalChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CapitalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CapitalChartView, 0, defStyleAttr);
        mPaddingLeft = (int) typedArray.getDimension(R.styleable.CapitalChartView_capitalChartView_padding_left, DisplayUtils.dip2px(context, 15));
        mPaddingRight = (int) typedArray.getDimension(R.styleable.CapitalChartView_capitalChartView_padding_right, DisplayUtils.dip2px(context, 10));
        mPaddingTop = (int) typedArray.getDimension(R.styleable.CapitalChartView_capitalChartView_padding_top, DisplayUtils.dip2px(context, 10));
        mPaddingBottom = (int) typedArray.getDimension(R.styleable.CapitalChartView_capitalChartView_padding_bottom, DisplayUtils.dip2px(context, 5));
        typedArray.recycle();
        //利润表，资产负债表，现金流量表区域
        mContentRect = new Rect();
        //日期区域
        mDateRect = new Rect();
        mLongPressRunnable = new LongPressRunnable();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            initSize(right - left, bottom - top);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initSize(w, h);
    }

    /**
     * 初始化宽高以及具体的图标区域大小
     *
     * @param width
     * @param height
     */
    public void initSize(int width, int height) {
        if (mWidth != width || mHeight != height) {
            this.mWidth = width;
            this.mHeight = height;
        }
        if (needDate) {
            int contentHeight = (int) (mHeight * 0.88);
            mContentRect.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mPaddingTop + contentHeight);
            mDateRect.set(mPaddingLeft, mContentRect.bottom, mWidth - mPaddingRight, mHeight - mPaddingBottom);
            Log.d("LHD", "mDateRect = " + mDateRect.toShortString());
        } else {
            int contentHeight = mHeight;
            mContentRect.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mPaddingTop + contentHeight);
            mDateRect.set(mPaddingLeft, mContentRect.bottom, mWidth - mPaddingRight, mContentRect.bottom);
        }
    }

    /**
     * 改变整个页面布局的大小
     *
     * @param percent
     */
    public void changeRectSize(float percent) {
        int contentHeight = (int) (mHeight * percent);
        mContentRect.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mPaddingTop + contentHeight);
        mDateRect.set(mPaddingLeft, mContentRect.bottom, mWidth - mPaddingRight, mHeight - mPaddingBottom);
    }

    /**
     * 获取绘制内容区域
     *
     * @return
     */
    public Rect getContentRect() {
        return mContentRect != null ? mContentRect : null;
    }

    /**
     * 获取日期区域
     *
     * @return
     */
    public Rect getDateRect() {
        return mDateRect != null ? mDateRect : null;
    }

    /**
     * 设置当前显示的View
     */
    public void setCurrentView(BaseViewEngine baseViewEngine) {
        this.mCurrentView = baseViewEngine;
        mCurrentView.setDrawRect(this);
        invalidate();   //刷新页面进行绘制
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentView != null) {
            mCurrentView.draw(canvas, this);
        }
    }

    private boolean isLongPress = false;

    /**
     * 长按
     */
    private class LongPressRunnable implements Runnable {
        @Override
        public void run() {
            getParent().requestDisallowInterceptTouchEvent(true);
            isLongPress = true;
            if (mFocusChangedListener != null) {
                mFocusChangedListener.showPopBoxView();
            }
        }
    }

    long downTime = 0;
    float pressX = 0, pressY = 0, moveX = 0, moveY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = event.getDownTime();
                mPressX = event.getX();
                pressX = event.getX();
                pressY = event.getY();
                isLongPress = false;
                if (mFocusChangedListener != null) {
                    mFocusChangedListener.focusChanged(mPressX);
                }
                postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_MOVE:
                mPressX = event.getX();
                if (mFocusChangedListener != null) {
                    mFocusChangedListener.focusChanged(mPressX);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                removeCallbacks(mLongPressRunnable);
                if (mFocusChangedListener != null) {
                    mFocusChangedListener.hidePopBoxView();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                removeCallbacks(mLongPressRunnable);
                if (mFocusChangedListener != null) {
                    mFocusChangedListener.hidePopBoxView();
                }
                break;
        }
        return true;
    }

    private OnFocusChangedListener mFocusChangedListener;

    //当触摸发生变化的时候调用
    public void setOnFocusChanged(OnFocusChangedListener listener) {
        this.mFocusChangedListener = listener;
    }

    public void setNeedDate(boolean needDate) {
        this.needDate = needDate;
    }

    public interface OnFocusChangedListener {
        void focusChanged(float x);

        void showPopBoxView();

        void hidePopBoxView();
    }


}
