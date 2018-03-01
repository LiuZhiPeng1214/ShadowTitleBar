package com.eme.shadowtitlebar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class QQActivity extends AppCompatActivity implements GradationScrollView.ScrollViewListener {

    private android.widget.ImageView                ivbanner;
    private NoScrollListView                        listview;
    private com.eme.shadowtitle.GradationScrollView scrollView;
    private android.widget.TextView                 textview;
    public  int                                     mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtil.setImgTransparent(this);
        setContentView(R.layout.activity_qq);

        textview = (TextView) findViewById(R.id.textview);
        scrollView = (GradationScrollView) findViewById(R.id.scrollView);
        listview =  findViewById(R.id.listview);
        ivbanner = (ImageView) findViewById(R.id.iv_banner);

        ivbanner.setFocusable(true);
        ivbanner.setFocusableInTouchMode(true);
        ivbanner.requestFocus();

        initListener();
        initdata();
    }

    private void initdata() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(QQActivity.this, android.R.layout.simple_list_item_1, getdata());
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
        ViewTreeObserver vto = ivbanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                textview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mHeight = (int) (ivbanner.getMeasuredHeight() / 1.5);
                Log.e("QQActivity", "mHeight:" + mHeight);
                scrollView.setScrollViewListener(QQActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(GradationScrollView view, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            textview.setBackgroundColor(Color.argb(0, 133, 151, 166));
        } else if (y > 0 && y <= mHeight) {
            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mHeight;
            float alpha = 255 * scale;
            textview.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            textview.setBackgroundColor(Color.argb((int) alpha, 133, 151, 166));
        } else {
            textview.setBackgroundColor(Color.argb(255, 133, 151, 166));
        }
    }
}

