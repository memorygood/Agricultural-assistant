package com.njau.agricultural_assistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by lenovo on 2018/3/19.
 */

public class RegisterActivity extends AppCompatActivity {
    private BottomTabBar mBottomTabBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .addTabItem("meun", R.mipmap.ic_launcher, MainActivity.class)
                .addTabItem("我的", R.mipmap.ic_launcher, UserinfoActivity.class);
//                .addTabItem("第三项", R.mipmap.ic_launcher, ThreeFragment.class);
    }
}
