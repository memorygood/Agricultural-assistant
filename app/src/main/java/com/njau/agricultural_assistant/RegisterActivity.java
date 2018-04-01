package com.njau.agricultural_assistant;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.hjm.bottomtabbar.BottomTabBar;

/**
 * Created by lenovo on 2018/3/19.
 */

public class RegisterActivity extends Activity {
    private BottomTabBar mBottomTabBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
         Topbar topbar = (Topbar) findViewById(R.id.topbar);
        topbar.setRightButtonVisibility(false);
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();//左边按钮实现的功能逻辑
            }

            @Override
            public void OnRightButtonClick() {
//右边按钮实现的功能逻辑\
            }
        });
    }
}
