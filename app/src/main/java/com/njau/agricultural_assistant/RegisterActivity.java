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

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2018/3/19.
 */

public class RegisterActivity extends Activity {
    TextView tv_usrname;
    TextView tv_pwd;
    TextView tv_checkpwd;
    TextView tv_email;
    TextView tv_phone;
    BufferedReader reader;
    Handler handler = new Handler();
    private Intent intent;
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
                new Thread(new RegisterActivity.RegisterThread()).start();
            }
        });
    }

    //提交注册信息
    public class RegisterThread implements Runnable{
        String code;
        public void run() {
                String address = "http://192.168.43.64:8080/springmvc_mybatis/registe?username="+tv_usrname.getText()+
                        "&pwd="+tv_pwd.getText()+"&phone="+tv_phone.getText()+"&email="+tv_email.getText();
                String message = "";
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    String userinfo;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    String json = response.toString();
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = gson.fromJson(json, map.getClass());
                    code = (String) map.get("code");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(code.equals("200")){
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                        finish();
                        intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else if(code.equals("500")){
                        Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                        finish();
                        intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
            };
    };

}
