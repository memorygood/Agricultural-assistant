package com.njau.agricultural_assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njau.agricultural_assistant.utils.UrlContancs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NyywActivity extends Activity implements AbsListView.OnScrollListener {
    Handler handler = new Handler();
    public View loadmoreView;
    public LayoutInflater inflater;
    public ListView listView;
    public int last_index;
    public int total_index;
    public List<String> firstList = new ArrayList<String>();//表示首次加载的list
    public List<String> nextList = new ArrayList<String>();//表示出现刷新之后需要显示的list
    public boolean isLoading = false;//表示是否正处于加载状态
    public ListViewAdapter adapter;
    HttpURLConnection  connection;
    BufferedReader reader;
    List<Map<String, Object>> list1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        inflater = LayoutInflater.from(this);
        loadmoreView = inflater.inflate(R.layout.load_more, null);//获得刷新视图
        loadmoreView.setVisibility(View.VISIBLE);//设置刷新视图默认情况下是不可见的
        listView = (ListView) findViewById(R.id.listView);
        initList(10, 30);
        new Thread(new NyywActivity.NyywListThread()).start();
//        adapter = new ListViewAdapter(this, firstList);
        listView.setOnScrollListener(this);
//        listView.addFooterView(loadmoreView,null,false);
//        listView.setAdapter(adapter);
}
    /**
     * 初始化我们需要加载的数据
     * @param firstCount
     * @param nextCount
     */
    public void initList(int firstCount,int nextCount)
    {
        for(int i = 0;i < firstCount;i++)
        {
            firstList.add("第"+(i+1)+"个开始加载");
        }
        for(int i = 0;i < firstCount;i++)
        {
            nextList.add("第"+(i+1)+"个开始加载");
        }
        for(int i = 0;i < nextCount;i++)
        {
            nextList.add("刷新之后第"+(i+1)+"个开始加载");
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        last_index = firstVisibleItem+visibleItemCount;
        total_index = totalItemCount;
        Log.i("last:  ", String.valueOf(last_index));
        Log.i("total:  ", String.valueOf(total_index));
    }


    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
        {
            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
            if(!isLoading)
            {
                //不处于加载状态的话对其进行加载
                isLoading = true;
                //设置刷新界面可见
                loadmoreView.setVisibility(View.VISIBLE);
                onLoad();
            }
        }
    }

    /**
     * 刷新加载
     */
    public void onLoad()
    {
        try {
            //模拟耗时操作
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(adapter == null)
        {
           // adapter = new ListViewAdapter(this, firstList);
            //listView.setAdapter(adapter);
        }else
        {
            //adapter.updateView(nextList);
        }
        loadComplete();//刷新结束
    }
    public class NyywListThread implements Runnable {

        public void run() {
            try {
                URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/nyyw");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(8000); // 设置超时时间
                connection.setReadTimeout(8000);
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
                Map<String, Object> map = new  HashMap<String, Object>();
                map = gson.fromJson(jsonString, map.getClass());
                list1 = ( List<Map<String, Object>>) map.get("result");
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
                    SimpleAdapter simpleAdapter = new SimpleAdapter(NyywActivity.this,list1,
                            R.layout.news_list_item,
                            new String[]{"c_title","c_content"},
                            new int[]{R.id.news_listitem_title,R.id.news_listitem_date});
                    listView.setAdapter(adapter);
                }
            });
        }
    };
    /**
     * 加载完成
     */
    public void loadComplete()
    {
        loadmoreView.setVisibility(View.GONE);//设置刷新界面不可见
        isLoading = false;//设置正在刷新标志位false
        NyywActivity.this.invalidateOptionsMenu();
        listView.removeFooterView(loadmoreView);//如果是最后一页的话，则将其从ListView中移出
    }
}
