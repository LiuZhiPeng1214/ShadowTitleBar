package com.eme.shadowtitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eme on 2018/2/28.
 */

public class MaterialIndicator extends View implements ViewPager.OnPageChangeListener{
    private static final int UNDEFINED_PADDING = -1;
    private Paint mSelectedIndicatorPaint;
    private Paint mIndicatorPaint;
    private float deselectedAlpha = 0.2f;
    private final RectF mSelectorRect;
    private int count;
    private final float indicatorRadius;
    private final float indicatorPadding;
    private int selectedPage = 0;
    private float offset;


    public MaterialIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaterialIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(Color.BLACK);
        mIndicatorPaint.setAlpha((int) (deselectedAlpha*255));
        mSelectorRect = new RectF();
        if (isInEditMode()) {
            count = 3;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.MaterialIndicator,0,R.style.MaterialIndicator);

        try {
            indicatorRadius = ta.getDimension(R.styleable.MaterialIndicator_mi_indicatorRadius,0);
            indicatorPadding = ta.getDimension(R.styleable.MaterialIndicator_mi_indicatorPadding,UNDEFINED_PADDING);
            mSelectedIndicatorPaint.setColor(ta.getColor(R.styleable.MaterialIndicator_mi_indicatorColor,0));
        }finally {
            ta.recycle();
        }
    }
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectedPage = position;
        offset = positionOffset;
        invalidate();
    }

    public void onPageSelected(int position) {
        selectedPage = position;
        offset = 0;
        invalidate();
    }

    public void onPageScrollStateChanged(int state) {

    }

    public void setAdapter(PagerAdapter adapter) {
        this.count = adapter.getCount();
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        if (getLayoutParams().width == ViewPager.LayoutParams.WRAP_CONTENT) {
            width = getSuggestedMinimumWidth();
        }
        setMeasuredDimension(width, getSuggestedMinimumHeight());

    }

    protected int getSuggestedMinimumWidth() {
        return (int) (indicatorDiameter() * count + getInternalPadding());
    }

    protected int getSuggestedMinimumHeight() {
        return (int) (getPaddingTop() + getPaddingBottom() + indicatorDiameter());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float gap = getGapBetweenIndicators();
        for (int i = 0; i < count; i++) {
            float position = indicatorStartX(gap,i);
            canvas.drawCircle(position + indicatorRadius, midY(), indicatorRadius , mIndicatorPaint);
        }
        float extenderStart = indicatorStartX(gap, selectedPage) + Math.min(gap * interpolatedOffset() * 2,gap);
        float extenderEnd = indicatorStartX(gap,selectedPage) + indicatorDiameter() + Math.min(gap * interpolatedOffset() * 2,gap);
        mSelectorRect.set(extenderStart,midY() - indicatorRadius, extenderEnd,midY() + indicatorRadius);
        canvas.drawRoundRect(mSelectorRect,indicatorRadius, indicatorRadius,mSelectedIndicatorPaint);
    }

    private float interpolatedOffset() {
        return 0;
    }

    private float midY() {
        return getHeight() / 2f;
    }

    private float indicatorStartX(float gap, int page) {
        return ViewCompat.getPaddingStart(this) + gap * page + indicatorRadius;
    }

    private float getGapBetweenIndicators() {
        if (indicatorPadding == UNDEFINED_PADDING) {
            return (getWidth() - indicatorDiameter()) / (count + 1);
        } else {
            return  indicatorPadding;
        }
    }

    private float indicatorDiameter() {
        return indicatorRadius * 2;
    }

    private float getInternalPadding() {
        if (indicatorPadding == UNDEFINED_PADDING || indicatorPadding == 0 || count == 0) {
            return 0;
        }
        return indicatorPadding * (count - 1);
    }
}
