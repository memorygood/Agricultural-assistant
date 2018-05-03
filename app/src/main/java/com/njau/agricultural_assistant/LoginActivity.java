package com.njau.agricultural_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.njau.agricultural_assistant.utils.UrlContancs;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jy on 2018/3/19.
 */

public class LoginActivity extends AppCompatActivity{
    private static final String APPID = "1106790452";
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;
    private IUiListener mListener;
    boolean isServerSideLogin = false;
    private static final String TAG = "LoginActivity";
    public static Tencent mTencent;
    private UserInfo mUserInfo;
    private BaseUiListener mIUiListener;
    EditText userName,password;
    BufferedReader reader;
    HttpURLConnection  connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mTencent = Tencent.createInstance(APPID,LoginActivity.this.getApplicationContext());
        Button btn1 = (Button) findViewById(R.id.btn_login);
        Button btn2 = (Button) findViewById(R.id.btn_loginbyqq);
        TextView t1 = (TextView) findViewById(R.id.textview_register);
        userName = (EditText) findViewById(R.id.et_userName);
        password = (EditText) findViewById(R.id.et_password);
        btn1.setOnClickListener(new onClickListener());
        btn2.setOnClickListener(new onClickListener());
        t1.setOnClickListener(new onClickListener());
        }


    private class onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textview_register:
                    enterRegister();
                    break;
                case R.id.btn_loginbyqq:
                    tencentlogin();
                    break;
                case R.id.btn_login:
                    String username = userName.getText().toString().trim();
                    String passwd = password.getText().toString().trim();
                    if (username.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    } else if (passwd.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {

                        new Thread(loginThread).start();
                        break;
                    }
            }
        }

        private void enterRegister() {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        public Thread loginThread = new Thread() {
            public void run() {
                String username = userName.getText().toString().trim();
                String passwd = password.getText().toString().trim();
                try {
                    URL url = new URL(UrlContancs.loginurl + "?username="+ username+ "&pwd=" + passwd);
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
                    Map<String, Object> user = new HashMap<String, Object>();
                    user = (Map<String, Object>) map.get("result");
                    String password = String.valueOf(user.get("c_password"));
                    if(passwd.equals(password)){
                        Message msg = Message.obtain();
                        msg.what = 0;
                        successHandler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what= 1;
                        successHandler.sendMessage(msg);
                    }
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
            }
        };
    }
    private void tencentlogin() {
        mIUiListener = new BaseUiListener();
        //all表示获取所有权限
        mTencent.login(LoginActivity.this,"all", mIUiListener);
    }
    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener{
        @Override
        public void onComplete(Object response) {
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG,"登录成功"+response.toString());
//                        finish();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
                        Message msg = Message.obtain();
                        msg.what = 0;
                        successHandler.sendMessage(msg);
                    }
                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler successHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        };
    };
}

