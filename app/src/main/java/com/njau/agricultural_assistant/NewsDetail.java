package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

/**
 * 新闻详细界面
 *
 */
public class NewsDetail extends Activity {
	Handler handler = new Handler();
	// 基本属性
	private TextView news_detail_title;
	private TextView news_detail_author;
	private TextView news_detail_date;
	private TextView news_detail_commentcount;
	private TextView news_detail_body;
	HttpURLConnection  connection;
	BufferedReader reader;
	private String cid;
	private String josnstring;
	private NyywNetUtils nyywNetUtils;
	Map<String, Object> nyywxx;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		initView();
		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		cid=data.getString("cid");
		new Thread(new NewsDetail.NyywxxThread()).start();
		Log.i("接收到的数据", String.valueOf(cid));
		ImageView img = findViewById(R.id.news_detail_home);
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				Intent intent = new Intent(NewsDetail.this, NyywActivity.class);
				startActivity(intent);
			}
		});
	}


	/**
	 * 初始化新闻详细界面
	 */
	private void initView() {
		news_detail_title = (TextView) findViewById(R.id.news_detail_title);
		news_detail_author = (TextView) findViewById(R.id.news_detail_author);
		news_detail_commentcount = (TextView) findViewById(R.id.news_detail_commentcount);
		news_detail_date = (TextView) findViewById(R.id.news_detail_date);
		news_detail_body = (TextView) findViewById(R.id.news_detail_body);
	}

	public class NyywxxThread implements Runnable {

		public void run() {
			try {
				URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/nyywxx?cid="+cid);
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
				nyywxx = (Map<String, Object>) map.get("result");
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
					news_detail_title.setText(nyywxx.get("c_title").toString());
					news_detail_date.setText(nyywxx.get("c_fbsj").toString());
					news_detail_body.setText(nyywxx.get("c_content").toString());
				}
			});
		}
	};
}
