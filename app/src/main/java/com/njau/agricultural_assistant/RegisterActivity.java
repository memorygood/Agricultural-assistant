package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2018/3/19.
 */

public class RegisterActivity extends Activity {
    TextView tv_usrname;
    TextView tv_pwd;
    TextView tv_checkpwd;
    TextView tv_email;
    TextView tv_phone;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
         Topbar topbar = (Topbar) findViewById(R.id.topbar);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        tv_usrname = (TextView)findViewById(R.id.iv_userIconName);
        tv_pwd = (TextView)findViewById(R.id.iv_userIconPwd);
        tv_checkpwd = (TextView)findViewById(R.id.iv_checkIconPwd);
        tv_email = (TextView)findViewById(R.id.iv_email);
        tv_phone = (TextView)findViewById(R.id.iv_phone);
        Button tj = (Button)findViewById(R.id.btn_register);
        tv_usrname.setTypeface(font);
        tv_pwd.setTypeface(font);
        tv_checkpwd.setTypeface(font);
        tv_phone.setTypeface(font);
        tv_email.setTypeface(font);
        topbar.setRightButtonVisibility(false);
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                //左边按钮实现的功能逻辑
                finish();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void OnRightButtonClick() {
//右边按钮实现的功能逻辑\
            }
        });
//        tj.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String account = tv_usrname.getText().toString().trim();
//                String passwd = tv_pwd.getText().toString().trim();
//            }
//            });
        tj.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(RegisterActivity.this,"111",Toast.LENGTH_LONG).show();
                    String address="http://192.168.43.200:8089/springmvc_mybatis/nyjs";
                    HttpURLConnection connection = null;

                    URL url = new URL(address);
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //提交注册信息
    public static void regist() throws MalformedURLException {
        String address="http://192.168.43.200:8089/springmvc_mybatis/nyjs";
        HttpURLConnection connection = null;

        URL url = new URL(address);
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
