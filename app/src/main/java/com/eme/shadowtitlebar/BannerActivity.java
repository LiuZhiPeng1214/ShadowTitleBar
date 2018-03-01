package com.eme.shadowtitlebar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eme.shadowtitle.GradationScrollView;
import com.eme.shadowtitle.NoScrollListView;
import com.eme.shadowtitle.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity implements GradationScrollView.ScrollViewListener{

    private android.support.v4.view.ViewPager viewpager;
    private com.eme.shadowtitle.MaterialIndicator indicator;
    private com.eme.shadowtitle.NoScrollListView  listview;
    private android.widget.TextView               textview;
    public int mImageHeight;
    private com.eme.shadowtitle.GradationScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.activity_banner);
        this.scrollView = (GradationScrollView) findViewById(R.id.scrollView);
        this.textview = (TextView) findViewById(R.id.textview);
        this.listview = (NoScrollListView) findViewById(R.id.listview);
//        this.indicator = (MaterialIndicator) findViewById(R.id.indicator);
        this.viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.setFocusable(true);
        viewpager.setFocusableInTouchMode(true);
        viewpager.requestFocus();

        viewpager.setAdapter(new MyPagerAdapter());
//        viewpager.addOnPageChangeListener(indicator);
//        indicator.setAdapter(viewpager.getAdapter());
        initListener();
        initdata();
    }

    private void initdata() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BannerActivity.this, android.R.layout.simple_list_item_1, getdata());
        listview.setAdapter(adapter);
    }

    private List<String> getdata() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i<20;i++) {
            data.add("测试数据"+ i);
        }
        return data;
    }

    private void initListener() {
        ViewTreeObserver vto = viewpager.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                viewpager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mImageHeight = (int) (viewpager.getHeight() / 1.5);
                scrollView.setScrollViewListener(BannerActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(GradationScrollView view, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            textview.setBackgroundColor(Color.argb(0, 133, 151, 166));
        } else if (y > 0 && y <= mImageHeight) {
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mImageHeight;
            float alpha = 255 * scale;
            textview.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            textview.setBackgroundColor(Color.argb((int) alpha, 133, 151, 166));
        } else {
            textview.setBackgroundColor(Color.argb(255, 133, 151, 166));
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        public int [] drawables = {R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(drawables[position]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
