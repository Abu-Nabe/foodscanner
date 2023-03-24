package com.example.unifood.Main.Extension;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NonSwipableViewPager extends ViewPager {

    private boolean canScroll = false;
    public NonSwipableViewPager(Context context) {
        super(context);
    }
    public NonSwipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll && super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll && super.onInterceptTouchEvent(ev);
    }
}
