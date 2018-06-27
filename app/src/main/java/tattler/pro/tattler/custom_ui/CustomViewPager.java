package tattler.pro.tattler.custom_ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean isPagingEnabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);

    }

    public void enablePaging() {
        isPagingEnabled = true;
    }

    public void disablePaging() {
        isPagingEnabled = false;
    }
}