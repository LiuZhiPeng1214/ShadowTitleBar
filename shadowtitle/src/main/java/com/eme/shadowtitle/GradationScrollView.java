package com.eme.shadowtitle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 带滚动监听的scrollview
 * Created by eme on 2018/2/28.
 */

public class GradationScrollView extends ScrollView {
    private ScrollViewListener mScrollViewListener = null;
    public GradationScrollView(Context context) {
        super(context);
    }

    public GradationScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface  ScrollViewListener {
        void onScrollChanged(GradationScrollView view, int x, int y ,int oldx, int oldy);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    protected void onScrollChanged(int x, int y,int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (mScrollViewListener != null) {
            mScrollViewListener.onScrollChanged(this,x,y,oldx,oldy);
        }
    }
}
