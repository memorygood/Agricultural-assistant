package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 新闻详细界面
 *
 */
public class NewsDetail extends Activity {

	// 基本属性
	private TextView news_detail_title;
	private TextView news_detail_author;
	private TextView news_detail_date;
	private TextView news_detail_commentcount;
	private TextView news_detail_body;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		this.initView();
		this.initData();
		this.testNewsData();
	}

	/**
	 * 读取新闻数据并显示在新闻详细界面中
	 */
	private void initData() {
		// 接收position位置(被点击的列表项位置news_id)
		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		Log.i("接收到的数据", String.valueOf(position));

		// 依position读取对应的新闻
		News news = NyywActivity.newsDataList.get(position-1);
		news_detail_title.setText(news.getTitle());
		news_detail_author.setText(news.getAuthor());
		news_detail_date.setText(news.getPubDate());
		news_detail_commentcount.setText(String.valueOf(news.getCommentCount()));
		news_detail_body.setText(news.getBody());

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

	/**
	 * 测试读取新闻列表项中的数据
	 */
	private void testNewsData() {
		// 接收position位置(被点击的列表项位置news_id)
		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		Log.i("测试接点击列表项位置", String.valueOf(position));
		News news = NyywActivity.newsDataList.get(position-1);
		Log.w("标题", news.getTitle());
		Log.w("作者", news.getAuthor());
		Log.w("发表日期", news.getPubDate());
		Log.w("评论数", String.valueOf(news.getCommentCount()));
		Log.w("内容", news.getBody());

	}
}
