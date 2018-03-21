package com.njau.agricultural_assistant;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by lenovo on 2018/3/21.
 */

public class TabarActivity extends AppCompatActivity {
    private BottomTabBar mBottomBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tabbar);
        mBottomBar = (BottomTabBar) findViewById(R.id.bottom_bar);
        mBottomBar.init(getSupportFragmentManager())
                .setImgSize(90, 90)
                .setFontSize(12)
                .setTabPadding(4, 6, 10)
                .setChangeColor(Color.GREEN, Color.RED)
                .addTabItem("meun", R.mipmap.ic_launcher, MainActivity.class)
                .addTabItem("我的", R.mipmap.ic_launcher, UserinfoActivity.class)
                .isShowDivider(false);
    }
}
