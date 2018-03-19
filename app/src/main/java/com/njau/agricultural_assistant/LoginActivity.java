package com.njau.agricultural_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by jy on 2018/3/19.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动图片
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        }
    @Override
    public void onClick(View v){
        Intent intent = null;
        switch(v.getId()){
            case R.id.textview_register:
                enterRegister();

        }
    }
    private void enterRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}

