package com.njau.agricultural_assistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.njau.agricultural_assistant.LoginActivity.userid;

/**
 * Created by admin on 2018/5/16.
 */

public class SearchActivity extends Activity {
    private SearchView mSearchView;
    public ListView listView;
    Handler handler = new Handler();
    HttpURLConnection connection;
    BufferedReader reader;
    List<Map<String, Object>> list1;
    // 创建等待框
    private ProgressDialog dialog;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Topbar topbar = (Topbar) findViewById(R.id.topbar);
        mSearchView = (SearchView) findViewById(R.id.searchEdit);
        listView = findViewById(R.id.td_searchlistview);


        topbar.setRightButtonVisibility(false);
        topbar.setOnLeftAndRightClickListener(new Topbar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                //左边按钮实现的功能逻辑
                finish();
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void OnRightButtonClick() {
            //右边按钮实现的功能逻辑\
            }
        });
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                str = query;
                new Thread(new SearchActivity.SearchListThread()).start();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
           }
        });
    }
    public class SearchListThread implements Runnable {

        public void run() {
            try {
                String urlstr = "http://192.168.43.64:8080/springmvc_mybatis/ssxxlist";
                String params = "str="+str;
                URL url = new URL(urlstr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5*1000);
                connection.setReadTimeout(5*1000);
                //设置是否向httpURLConnection写入内容
                //post请求必须设置为true,因为post请求参数是否写在http正文中
                connection.setDoOutput(true);
                //设置是否从HttpURLConnection读入内容，默认为true
                connection.setDoInput(true);
                //设置是否使用缓存，post请求不使用缓存
                connection.setUseCaches(false);
                //设置请求方法  注意大小写!
                connection.setRequestMethod("POST");
                //设置字符集
                connection.setRequestProperty("Charset","utf-8");
                OutputStream os = connection.getOutputStream();
                os.write(params.getBytes());
                os.flush();
                os.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    byte[] bytes = new byte[1024];
                    int i = 0;
                    while ((i = is.read(bytes)) != -1) {
                        sb.append(new String(bytes, 0, i, "utf-8"));
                    }
                    is.close();
                    String json = sb.toString();
                    Gson gson = new Gson();
                    list1 = getListMaps("result", json);
                }
                } catch (MalformedURLException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                 }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(list1.size()!=0) {
                        SimpleAdapter simpleAdapter = new SimpleAdapter(SearchActivity.this, list1,
                                R.layout.news_list_item,
                                new String[]{"c_title", "c_fbsj"},
                                new int[]{R.id.news_listitem_title, R.id.news_listitem_datetime});
                        listView.setAdapter(simpleAdapter);
                        //  dialog.dismiss();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Map<String, Object> xx = list1.get(position);
                                String cid = xx.get("c_id").toString();
                                String xxlx = xx.get("c_xxlx").toString();
                                Intent intent = new Intent();
                                intent.setClass(SearchActivity.this, NewsDetail.class);
                                //利用bundle来存取数据
                                Bundle bundle = new Bundle();
                                bundle.putString("cid", cid);
                                bundle.putString("path", xxlx);
                                bundle.putString("activity", "SearchActivity");
                                //再把bundle中的数据传给intent，以传输过去
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                            }
                        });
                    }
                    else{
                        Toast.makeText(SearchActivity.this, "无相关信息", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
    public  List<Map<String, Object>> getListMaps(String key, String jsonString) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                // 通过org.json中的迭代器来取Map中的值。
                Iterator<String> iterator = jsonObject2.keys();
                while (iterator.hasNext()) {
                    String jsonKey = iterator.next();
                    Object jsonValue = jsonObject2.get(jsonKey);
                    //JSON的值是可以为空的，所以我们也需要对JSON的空值可能性进行判断。
                    if (jsonValue == null) {
                        jsonValue = "";
                    }
                    map.put(jsonKey, jsonValue);
                }
                listMap.add(map);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return listMap;
    }

}
