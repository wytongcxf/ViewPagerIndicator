package com.yunbao.vi;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunbao.vi.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<View> mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ViewPagerIndicator indicator= (ViewPagerIndicator) findViewById(R.id.indicator);
        String[] titles={"关注","热门","最近","附近","设置","帮助"};
        indicator.setTitles(titles);
        indicator.setVisibleChildCount(4);
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        mViews=new ArrayList<>();
        LayoutInflater inflater= LayoutInflater.from(this);
        for(int i=0;i<titles.length;i++){
            View view=inflater.inflate(R.layout.view_main_viewpager_item,mViewPager,false);
            TextView tv= (TextView) view.findViewById(R.id.textView);
            tv.setText(titles[i]);
            mViews.add(view);
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view=mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }
        });
        indicator.setViewPager(mViewPager);
    }

}
