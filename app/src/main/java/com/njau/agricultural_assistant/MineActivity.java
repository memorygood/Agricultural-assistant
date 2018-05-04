package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

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
 * Created by Administrator on 2018/4/9 0009.
 */

public class MineActivity extends Activity {
    HttpURLConnection  connection;
    BufferedReader reader;
    Handler handler = new Handler();
    private TextView mine_loginname;
    Map<String, Object> minexx;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mine);
        Topbar topbar = (Topbar) findViewById(R.id.topbar);
        View user = (View) findViewById(R.id.userinfo);
        mine_loginname = findViewById(R.id.mine_loginname);
        topbar.setRightButtonVisibility(false);
        Map<String, Object> minexx;
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                //左边按钮实现的功能逻辑
                finish();
                Intent intent = new Intent(MineActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void OnRightButtonClick() {
//右边按钮实现的功能逻辑\
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MineActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        new Thread(new MineActivity.mineThread()).start();
    }
    public class mineThread implements Runnable {

        public void run() {
            try {
                URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/mine?userid="+userid);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream in = connection.getInputStream();
                //下面对获取到的输入流进行读取
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                String userinfo;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String jsonString = response.toString();
                Gson gson = new Gson();
                Map<String, Object> map = new HashMap<String, Object>();
                map = gson.fromJson(jsonString, map.getClass());
                minexx = (Map<String, Object>) map.get("result");
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
                    mine_loginname.setText(minexx.get("c_logname").toString());
                }
            });
        }
    };
}
