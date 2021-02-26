package com.sina.chartproject.view;

/**
 * @author hongda5
 * @date 2021/2/23
 */
public interface LineOuterGestureCallback {

    // 点击
    void onSingleClick();

    // 长按
    void onLongPress(float x, float y);

    // 滑动
    void onScrollAfterLongPress(float x, float y);

    // 惯性滑动
    void onFling(float x, float y);

    // 抬起
    void onUp();

}
