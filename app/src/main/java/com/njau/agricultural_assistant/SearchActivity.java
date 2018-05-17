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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
        listView = findViewById(R.id.td_listview);


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
                dialog = new ProgressDialog(SearchActivity.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                dialog.setTitle("提示");
                dialog.setMessage("加载中...");
                dialog.setCancelable(false);
                dialog.show();
                new Thread(new SearchActivity.SearchListThread()).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            // 当搜索内容改变时触发该方法 @Override
//            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    mListView.setFilterText(newText);
//                }else{
//                    mListView.clearTextFilter();
//                }
//                return false;
//            }
        });
    }
    public class SearchListThread implements Runnable {

        public void run() {
            try {
                URL url = new URL("http://192.168.43.64:8080/springmvc_mybatis/ssxxlist?str="+str);
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
                String json = response.toString();
                Gson gson = new Gson();
                list1 = getListMaps("result",json);;
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
                    SimpleAdapter simpleAdapter = new SimpleAdapter(SearchActivity.this,list1,
                            R.layout.news_list_item,
                            new String[]{"c_title","c_fbsj"},
                            new int[]{R.id.news_listitem_title,R.id.news_listitem_datetime});
                    listView.setAdapter(simpleAdapter);
                    dialog.dismiss();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Map<String,Object> xx = list1.get(position);
                            String cid = xx.get("c_id").toString();
                            String xxlx = xx.get("c_xxlx").toString();
                            Intent intent=new Intent();
                            intent.setClass(SearchActivity.this,NewsDetail.class);
                            //利用bundle来存取数据
                            Bundle bundle=new Bundle();
                            bundle.putString("cid",cid);
                            bundle.putString("path",xxlx);
                            bundle.putString("activity","SearchActivity");
                            //再把bundle中的数据传给intent，以传输过去
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);
                        }
                    });
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
