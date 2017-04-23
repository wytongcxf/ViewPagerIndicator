package com.yunbao.vi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunbao.vi.R;

/**
 * Created by cxf on 2017/4/19.
 */

public class ViewPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ViewPagerIndicator";
    //所有的标题
    private String[] mTitles;
    //可见的item个数
    private int mVisibleItemCount;
    //宽高
    private int mWidth;
    private int mHeight;
    //字体大小
    private float mTextSize;
    //正常字体颜色
    private int mNormalColor;
    //高亮字体颜色
    private int mLightColor;
    private int[] mNormalColorArgb;
    private int[] mLightColorArgb;
    //下面指示条的宽度
    private int mIndicatorWidth;
    //下面指示条的高度
    private int mIndicatorHeight;
    //当前的第几个选项高亮
    private int mCurrentItem;
    //每次滚动偏移量
    private int mScrollX;
    //总偏移量
    private int mTotalScrollX;
    private Paint mPaint;
    private ViewPager mViewPager;
    private boolean isScrolling;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTextSize = a.getDimension(R.styleable.ViewPagerIndicator_title_textSize, 0);
        mIndicatorWidth = (int) a.getDimension(R.styleable.ViewPagerIndicator_indicatorWidth, 0);
        mIndicatorHeight = (int) a.getDimension(R.styleable.ViewPagerIndicator_indicatorHeight, 0);
        mNormalColor = a.getColor(R.styleable.ViewPagerIndicator_normalColor, Color.BLACK);
        mLightColor = a.getColor(R.styleable.ViewPagerIndicator_lightColor, Color.BLACK);
        mNormalColorArgb = getColorArgb(mNormalColor);
        mLightColorArgb = getColorArgb(mLightColor);
        mCurrentItem = a.getInteger(R.styleable.ViewPagerIndicator_currentItem, 0);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        addChild();
    }

    private void addChild() {
        if (mTitles == null || mVisibleItemCount == 0) {
            return;
        }
        mScrollX = mWidth / mVisibleItemCount;
        mTotalScrollX = mCurrentItem * mScrollX;
        for (int i = 0; i < mTitles.length; i++) {
            final TextView textView = new TextView(getContext());
            LayoutParams params = new LayoutParams(mScrollX, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setText(mTitles[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            if (mCurrentItem == i) {
                textView.setTextColor(mLightColor);
            } else {
                textView.setTextColor(mNormalColor);
            }
            textView.setGravity(Gravity.CENTER);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mViewPager!=null){
                        mViewPager.setCurrentItem(finalI,true);
                    }
                }
            });
            addView(textView);
        }
    }

    /**
     * 画出下面的横线
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(mNormalColor);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
        canvas.save();
        canvas.translate(0, mHeight);
        //实际绘制的指示条的宽度
        int w = Math.min(mIndicatorWidth, mScrollX);
        //求出右上角和左下角的坐标
        int x1 = (mScrollX - w) / 2 + mTotalScrollX;
        int y1 = -mIndicatorHeight;
        int x2 = x1 + w;
        int y2 = 0;
        //画出矩形
        canvas.drawRect(new Rect(x1, y1, x2, y2), mPaint);
        canvas.restore();
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
    }

    public void setVisibleChildCount(int visibleChildCount) {
        mVisibleItemCount = visibleChildCount;
    }


    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentItem);
    }

    private int[] getColorArgb(int color) {
        return new int[]{Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)};
    }

    /**
     * 文字变颜色
     * @param position
     * @param rate
     */
    private void textChangeColor(int position, float rate) {
        int a = (int)(mNormalColorArgb[0]*(1-rate)+ mLightColorArgb[0]  *rate);
        int r = (int)(mNormalColorArgb[1]*(1-rate)+ mLightColorArgb[1]  *rate);
        int g = (int)(mNormalColorArgb[2]*(1-rate)+ mLightColorArgb[2]  *rate);
        int b = (int)(mNormalColorArgb[3]*(1-rate)+ mLightColorArgb[3]  *rate);
        TextView textView=(TextView) getChildAt(position);
        if(textView!=null){
            textView.setTextColor(Color.argb(a, r, g, b));
        }
    }

    /**
     * 滚动的方法
     *
     * @param position
     * @param positionOffset
     */
    private void scroll(int position, float positionOffset) {
        mTotalScrollX = (int) ((position + positionOffset) * mScrollX);
        if (position >= mVisibleItemCount - 1) {
            scrollTo((position - mVisibleItemCount + 1) * mScrollX + (int) (positionOffset * mScrollX), 0);
        }
        invalidate();
        changeColor(position,positionOffset);
    }

    /**
     * 文字颜色变化
     */
    private void changeColor(int position,float positionOffset) {
        textChangeColor(position, 1-positionOffset);
        textChangeColor(position+1,  positionOffset);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        scroll(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state==0){
            for(int i=0;i<getChildCount();i++){
                if(i!=mViewPager.getCurrentItem()){
                    ((TextView)getChildAt(i)).setTextColor(mNormalColor);
                }
            }
        }
    }
}
