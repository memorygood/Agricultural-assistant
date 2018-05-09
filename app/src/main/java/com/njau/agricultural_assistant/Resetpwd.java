package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.HashMap;
import java.util.Map;

import static com.njau.agricultural_assistant.LoginActivity.userid;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class Resetpwd extends Activity {
    TextView pwd;
    TextView checkpwd;
    TextView tv_pwd;
    TextView tv_checkpwd;
    HttpURLConnection connection;
    BufferedReader reader;
    Handler handler = new Handler();
    private Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.resetpwd);
        Typeface font = Typeface.createFromAsset(getAssets(), "font/fontawesome-webfont.ttf");
        pwd = (TextView)findViewById(R.id.et_password);
        checkpwd = (TextView)findViewById(R.id.et_checkpassword);
        tv_pwd= (TextView)findViewById(R.id.iv_userIconPwd);
        tv_checkpwd= (TextView)findViewById(R.id.iv_checkIconPwd);
        Button tj = (Button)findViewById(R.id.btn_tj);
        Topbar topbar = (Topbar) findViewById(R.id.topbar);
        topbar.setRightButtonVisibility(false);
        tv_pwd.setTypeface(font);
        tv_checkpwd.setTypeface(font);
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                //左边按钮实现的功能逻辑
                finish();
                Intent intent = new Intent(Resetpwd.this, LoginActivity.class);
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
                new Thread(new Resetpwd.RegisterThread()).start();
            }
        });
    }
    //提交修改密码
    public class RegisterThread implements Runnable{
        String code;
        public void run() {
            String pwd1 =String.valueOf(pwd.getText());
            String pwd2 =String.valueOf(checkpwd.getText());
            String address = "http://192.168.43.64:8080/springmvc_mybatis/password?oldpwd="+pwd1+"&newpwd="+pwd2+
                    "&userid="+userid;
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
                code.trim();
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
                        Toast.makeText(Resetpwd.this,"修改成功！",Toast.LENGTH_LONG).show();
                        finish();
                        intent = new Intent(Resetpwd.this, MainActivity.class);
                        startActivity(intent);
                    }else if(code.equals("300")){
                        Toast.makeText(Resetpwd.this,"原密码错误！",Toast.LENGTH_LONG).show();
                    }
                }
            });
        };
    };
}
