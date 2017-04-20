package com.yunbao.vi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yunbao.vi.view.ViewPagerIndicator;

public class MainActivity extends AppCompatActivity {

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

    }

}
