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
    }
}
