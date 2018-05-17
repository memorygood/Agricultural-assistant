package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class NywkActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tylistview);
        Topbar topbar = (Topbar) findViewById(R.id.topbar);
        topbar.setRightButtonVisibility(false);
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                //左边按钮实现的功能逻辑
                finish();
                Intent intent = new Intent(NywkActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void OnRightButtonClick() {
//右边按钮实现的功能逻辑\
            }
        });
        Toast.makeText(NywkActivity.this, "暂无相关视频", Toast.LENGTH_SHORT).show();
    }
}
