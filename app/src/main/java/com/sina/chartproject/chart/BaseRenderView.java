package com.sina.chartproject.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.sina.chartproject.R;
import com.sina.chartproject.utils.DisplayUtils;

/**
 * @author guannan11
 * @date on 2019/10/25 17:58
 * @des 绘图框架的基类（该类主要对绘制区域进行分区操作，便于子类根据区域边界进行绘制操作）
 */
public class BaseRenderView extends ChartView {
    // 视图的左边距
    private int mPaddingLeft;
    // 视图的右边距
    private int mPaddingRight;
    // 视图的上边距
    private int mPaddingTop;
    // 视图的下边距
    private int mPaddingBottom;
    // 整个view的宽度
    private int mWidth;
    // 整个view的高度
    private int mHeight;
    // 主图区域
    protected Rect mContentRect = new Rect();
    // 日期区域
    protected Rect mDateRect = new Rect();
    private LongPressRunnable mLongPressRunnable;
    private float mPressX;
    // 长按监听
    protected OnLongClickListener mOnLongClickListener;

    public BaseRenderView(Context context) {
        this(context, null);
    }

    public BaseRenderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChartView, 0, defStyleAttr);
        mPaddingLeft = (int) typedArray.getDimension(R.styleable.ChartView_chart_padding_left, DisplayUtils.dip2px(context, 12));
        mPaddingRight = (int) typedArray.getDimension(R.styleable.ChartView_chart_padding_right, DisplayUtils.dip2px(context, 12));
        mPaddingTop = (int) typedArray.getDimension(R.styleable.ChartView_chart_padding_top, DisplayUtils.dip2px(context, 10));
        mPaddingBottom = (int) typedArray.getDimension(R.styleable.ChartView_chart_padding_bottom, DisplayUtils.dip2px(context, 5));
        typedArray.recycle();
        mLongPressRunnable = new LongPressRunnable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
        int contentHeight = (int) (mHeight * 0.88);
        mContentRect.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mPaddingTop + contentHeight);
        mDateRect.set(mPaddingLeft, mContentRect.bottom, mWidth - mPaddingRight, mHeight - mPaddingBottom);
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
     * 长按
     */
    private class LongPressRunnable implements Runnable {
        @Override
        public void run() {
            isLongPress = true;
            getParent().requestDisallowInterceptTouchEvent(true);
            showPopBoxView(true);
        }
    }

    long downTime = 0;
    float pressX = 0, pressY = 0, moveX = 0, moveY = 0;
    private boolean isLongPress = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = event.getDownTime();
                mPressX = event.getX();
                pressX = event.getX();
                pressY = event.getY();
                isLongPress = false;
                focusChanged(mPressX);
                postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_MOVE:
                mPressX = event.getX();
                focusChanged(mPressX);
                moveX = event.getX();
                moveY = event.getY();
                if (Math.abs(moveX - pressX) >= Math.abs(moveY - pressY)) {
                    if (event.getEventTime() - downTime > ViewConfiguration.getLongPressTimeout()) {   //长按
                        isLongPress = true;
                    }
                }
                if (isLongPress) {
                    showPopBoxView(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isLongPress){
                    onLongClick();
                }else{
                    performClick();
                }
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                removeCallbacks(mLongPressRunnable);
                showPopBoxView(false);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 按下的位置的x轴的坐标
     *
     * @param x
     */
    protected void focusChanged(float x) {

    }

    /**
     * 是否显示弹框  true：显示
     *
     * @param isShow
     */
    protected void showPopBoxView(boolean isShow) {

    }

    /**
     * 长按手指抬起取消
     */
    private void onLongClick() {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onLongClick();
        }
    }

    public void setOnLongClickListener2(OnLongClickListener listener) {
        this.mOnLongClickListener = listener;
    }

    public interface OnLongClickListener {
        void onLongClick();
    }

}
