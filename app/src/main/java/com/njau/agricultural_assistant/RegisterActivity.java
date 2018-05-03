package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

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
        tj.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this,"111",Toast.LENGTH_LONG).show();
                new Thread(registThread).start();
            }
        });
    }

    //提交注册信息
    public Thread  registThread = new Thread(){
            public void run() {
                String address = "http://192.168.43.64:8080/springmvc_mybatis/nyjs";
                String message = "";
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    byte[] data = new byte[1024];
                    StringBuffer sb = new StringBuffer();
                    int length = 0;
                    while ((length = inputStream.read(data)) != -1) {
                        String s = new String(data, Charset.forName("utf-8"));
                        sb.append(s);
                    }
                    message = sb.toString();
                    Message msg = Message.obtain();
                    msg.what = 0;
                    postHandler.sendMessage(msg);
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }
            };
    };

    private Handler postHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0){
                Toast.makeText(RegisterActivity.this,"222333",Toast.LENGTH_LONG).show();
            }
        };
    };
}
