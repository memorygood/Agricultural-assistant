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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
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
    TextView usrname;
    TextView pwd;
    TextView checkpwd;
    TextView email;
    TextView phone;
    TextView tv_radioGroup;
    RadioButton tv_radiobutton_boy;
    String xb;
    BufferedReader reader;
    Handler handler = new Handler();
    private Intent intent;
    HttpURLConnection connection;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
         Topbar topbar = (Topbar) findViewById(R.id.topbar);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        usrname = (TextView)findViewById(R.id.iv_userIconName);
        pwd = (TextView)findViewById(R.id.iv_userIconPwd);
        checkpwd = (TextView)findViewById(R.id.iv_checkIconPwd);
        email = (TextView)findViewById(R.id.iv_email);
        phone = (TextView)findViewById(R.id.iv_phone);
        tv_radioGroup = (TextView)findViewById(R.id.iv_xb);
        tv_radiobutton_boy = (RadioButton)findViewById(R.id.radio_boy);
        Button tj = (Button)findViewById(R.id.btn_register);
        usrname.setTypeface(font);
        pwd.setTypeface(font);
        checkpwd.setTypeface(font);
        phone.setTypeface(font);
        email.setTypeface(font);
        tv_radioGroup.setTypeface(font);
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
                if(tv_radiobutton_boy.isChecked()){
                    xb="1";
                }else{
                    xb="2";
                }
                tv_usrname = (TextView)findViewById(R.id.et_userName);
                tv_pwd = (TextView)findViewById(R.id.et_password);
                tv_checkpwd = (TextView)findViewById(R.id.et_checkpassword);
                tv_email = (TextView)findViewById(R.id.et_email);
                tv_phone = (TextView)findViewById(R.id.et_phone);
                String usrname =String.valueOf(tv_usrname.getText());
                String email =String.valueOf(tv_email.getText());
                String phone =String.valueOf(tv_phone.getText());
                String pwd =String.valueOf(tv_pwd.getText());
                String checkpwd = String.valueOf(tv_checkpwd.getText());

                if(usrname==null||usrname.equals("")){
                    Toast.makeText(RegisterActivity.this,"请输入用户名！",Toast.LENGTH_LONG).show();
                }else if(pwd==null||pwd.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_LONG).show();
                }else if(checkpwd==null||checkpwd.equals("")){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码！", Toast.LENGTH_LONG).show();
                }else if(phone==null||phone.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入电话号码！", Toast.LENGTH_LONG).show();
                }else if(email==null||email.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入邮箱！", Toast.LENGTH_LONG).show();
                }else if(!pwd.equals(checkpwd) ){
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不一致！",Toast.LENGTH_LONG).show();
                }else{
                    new Thread(new RegisterActivity.RegisterThread()).start();
                }
            }
        });
    }
    //提交注册信息
    public class RegisterThread implements Runnable{
        String code;
        public void run() {
            String usrname =String.valueOf(tv_usrname.getText());
            String email =String.valueOf(tv_email.getText());
            String phone =String.valueOf(tv_phone.getText());
            String pwd =String.valueOf(tv_pwd.getText());
            String address = "http://192.168.43.64:8080/springmvc_mybatis/registe?username="+java.net.URLEncoder.encode(usrname)+
                    "&pwd="+pwd+"&phone="+phone+"&email="+email+"&xb="+java.net.URLEncoder.encode(xb);
            try {
                URL url = new URL(address);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(8000); // 设置超时时间
                connection.setReadTimeout(8000);
                connection.setRequestProperty("Charset","UTF-8");
                connection.setRequestMethod("GET");
                InputStream in = connection.getInputStream();
                //下面对获取到的输入流进行读取
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
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
