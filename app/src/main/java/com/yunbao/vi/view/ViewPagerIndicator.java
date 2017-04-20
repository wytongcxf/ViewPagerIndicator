package com.yunbao.vi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunbao.vi.R;

/**
 * Created by weiliankeji on 2017/4/19.
 */

public class ViewPagerIndicator extends LinearLayout {
    private static final String TAG = "ViewPagerIndicator";
    private String[] mTitles;
    private int mVisibleChildCount;
    private int mWidth;
    private int mHeight;
    private float mTextSize;
    private int mTextColor;
    private float mLineWidth;
    private Paint mPaint;


    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTextSize = a.getDimension(R.styleable.ViewPagerIndicator_title_textSize, 0);
        mTextColor = a.getColor(R.styleable.ViewPagerIndicator_textColor, Color.BLACK);
        mLineWidth = a.getDimension(R.styleable.ViewPagerIndicator_lineWidth, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e(TAG, "onFinishInflate: onFinishInflate---->");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        addChild();
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public void setVisibleChildCount(int visibleChildCount) {
        mVisibleChildCount = visibleChildCount;
    }

    private void addChild() {
        if (mTitles == null || mVisibleChildCount == 0) {
            return;
        }
        for (int i = 0; i < mTitles.length; i++) {
            TextView textView = new TextView(getContext());
            LayoutParams params = new LayoutParams((int) mWidth / mVisibleChildCount, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setText(mTitles[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setTextColor(mTextColor);
            textView.setGravity(Gravity.CENTER);
            addView(textView);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(mTextColor);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
        canvas.save();
        canvas.translate(0, mHeight);
        canvas.drawRect(new Rect(0, (int) (-mLineWidth), (int) (mWidth / mVisibleChildCount), 0), mPaint);
        canvas.restore();
    }


}
