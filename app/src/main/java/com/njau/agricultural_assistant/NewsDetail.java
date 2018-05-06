package com.njau.agricultural_assistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.njau.agricultural_assistant.utils.NyywNetUtils;

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
 * 新闻详细界面
 *
 */
public class NewsDetail extends Activity {
	Handler handler = new Handler();
	// 基本属性
	private TextView news_detail_title;
	private TextView news_detail_ly;
	private TextView news_detail_date;
	private TextView news_detail_body;
	HttpURLConnection  connection;
	BufferedReader reader;
	private String cid;
	private String path;
	private String xxlx;
	private String activityname;
	private String josnstring;
	Map<String, Object> xxlist;
	// 创建等待框
	private ProgressDialog dialog;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
		dialog.setTitle("提示");
		dialog.setMessage("加载中...");
		dialog.setCancelable(false);
		dialog.show();
		initView();
		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		cid=data.getString("cid");
		path=data.getString("path");
		xxlx=data.getString("xxlx");
		activityname=data.getString("activity");
		Topbar topbar = (Topbar) findViewById(R.id.topbar);
		topbar.setRightButtonVisibility(true);
		topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
			@Override
			public void OnLeftButtonClick() {
				//左边按钮实现的功能逻辑
				finish();
				if(activityname.equals("NyywActivity")) {
					Intent intent1  = new Intent(NewsDetail.this, NyywActivity.class);
					startActivity(intent1);
				}else if(activityname.equals("NyzcActivity")){
					Intent intent2 = new Intent(NewsDetail.this, NyzcActivity.class);
					startActivity(intent2);
				}else if(activityname.equals("SczxActivity")){
					Intent intent3 = new Intent(NewsDetail.this, SczxActivity.class);
					startActivity(intent3);
				}
			}
			@Override
			public void OnRightButtonClick() {
				//右边按钮实现的功能逻辑\
				new Thread(new NewsDetail.ScxxThread()).start();
			}
		});
		new Thread(new NewsDetail.xxThread()).start();
		Log.i("接收到的数据", String.valueOf(cid));
//		ImageView img = findViewById(R.id.news_detail_home);
//		img.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				finish();
//				Intent intent = new Intent(NewsDetail.this, NyywActivity.class);
//				startActivity(intent);
//			}
//		});

	}


	/**
	 * 初始化新闻详细界面
	 */
	private void initView() {
		news_detail_title = (TextView) findViewById(R.id.news_detail_title);
		news_detail_ly = (TextView) findViewById(R.id.news_detail_ly);
		news_detail_date = (TextView) findViewById(R.id.news_detail_date);
		news_detail_body = (TextView) findViewById(R.id.news_detail_body);
	}

	public class xxThread implements Runnable {

		public void run() {
			try {
				URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/"+path+"?cid="+cid);
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
				xxlist = (Map<String, Object>) map.get("result");
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
					news_detail_title.setText(xxlist.get("c_title").toString());
					news_detail_date.setText(xxlist.get("c_fbsj").toString());
					news_detail_body.setText(xxlist.get("c_content").toString());
					if(xxlist.get("c_ly")!=null) {
						news_detail_ly.setText(xxlist.get("c_ly").toString());
					}else
						news_detail_ly.setText("未知");
					dialog.dismiss();
				}
			});
		}
	};

	public class ScxxThread implements Runnable {
		String code;
		public void run() {
			try {
				URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/scxx?userid="+userid+"&xxid="+cid+"&xxlx="+xxlx);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				InputStream in = connection.getInputStream();				//下面对获取到的输入流进行读取
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
					Toast.makeText(NewsDetail.this,"收藏成功",Toast.LENGTH_LONG).show();
					}else if(code.equals("300")){
						Toast.makeText(NewsDetail.this,"您已收藏过该信息",Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(NewsDetail.this,"收藏失败",Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	};
}
