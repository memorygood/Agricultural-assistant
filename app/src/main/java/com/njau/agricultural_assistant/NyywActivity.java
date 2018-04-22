package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/22 0022.
 */

public class NyywActivity extends Activity {
    // 下拉刷新列表
    private PullToRefreshListView pullToRefreshListView;
    private News news;
    // 构造一个存储列表项内容的ArrayList<>结构
    public static List<News> newsDataList = new ArrayList<News>();
    private NewsListViewAdapter newsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_list);
        this.initNewsData();
        this.testLoadNewsData();
        newsListViewAdapter = new NewsListViewAdapter(this, newsDataList,
                R.layout.news_list_item);
        // 加载新闻列表(下拉刷新列表)
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.frame_listview_news);
        // 绑定列表项适配器(为列表适配各项)
        pullToRefreshListView.setAdapter(newsListViewAdapter);
        // 绑定点击列表项事件监听器(依据点击的列表项启动新活动显示对应新闻详情)
        pullToRefreshListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(view.getContext(),
                                NewsDetail.class);
                        intent.putExtra("news_id", position);
                        view.getContext().startActivity(intent);
                    }
                });
    }

    /**
     * 初始化新闻内容(插入20条新闻测试数据)
     */
    private void initNewsData() {
        for (int i = 1; i <= 20; i++) {
            news = new News("简单的新闻列表事例标题" + i, "Freedom", "2014-1-" + i,
                    12 + i, "新闻内容" + i);
            newsDataList.add(news);
        }
    }

    /**
     * 测试读取新闻列表项中的数据
     */
    private void testLoadNewsData() {
        Log.w("当前newsDataList中新闻数量为", String.valueOf(newsDataList.size()));
        for (int i = 1; i <= newsDataList.size(); i++) {
            News news = newsDataList.get(i - 1);
            Log.i("第" + i + "条新闻标题", news.getTitle());
            Log.i("第" + i + "条新闻作者", news.getAuthor());
            Log.i("第" + i + "条新闻发表日期", news.getPubDate());
            Log.i("第" + i + "条新闻评论数", String.valueOf(news.getCommentCount()));
            Log.i("第" + i + "条新闻内容", news.getBody());
        }
    }
}
