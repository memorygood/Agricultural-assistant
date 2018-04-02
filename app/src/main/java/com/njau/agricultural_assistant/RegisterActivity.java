package com.njau.agricultural_assistant;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;
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
        Typeface font = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        TextView tv_usrname = (TextView)findViewById(R.id.iv_userIconName);
        TextView tv_pwd = (TextView)findViewById(R.id.iv_userIconPwd);
        TextView tv_checkpwd = (TextView)findViewById(R.id.iv_checkIconPwd);
        TextView tv_email = (TextView)findViewById(R.id.iv_email);
        TextView tv_phone = (TextView)findViewById(R.id.iv_phone);
        tv_usrname.setTypeface(font);
        tv_pwd.setTypeface(font);
        tv_checkpwd.setTypeface(font);
        tv_phone.setTypeface(font);
        tv_email.setTypeface(font);
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
