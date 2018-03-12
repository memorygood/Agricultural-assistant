package com.njau.agricultural_assistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity {
    private SliderLayout mDemoSlider;
    //定义图标数组
    private int[] imageRes = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};

    //定义图标下方的名称数组
    private String[] name = {
            "当前农事",
            "农业要闻",
            "农业论坛",
            "市场资讯",
            "农业科技",
            "农业微课"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> urlMaps = new HashMap<>();
        urlMaps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        urlMaps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        urlMaps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");

        for(String name : urlMaps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)//描述
                    .image(urlMaps.get(name))//image方法可以传入图片url、资源id、File
                    .setScaleType(BaseSliderView.ScaleType.Fit)//图片缩放类型
                    .setOnSliderClickListener(onSliderClickListener);//图片点击
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);//传入参数
            mDemoSlider.addSlider(textSliderView);//添加一个滑动页面
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);//滑动动画
//        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认指示器样式
        mDemoSlider.setCustomIndicator((PagerIndicator)findViewById(R.id.custom_indicator2));//自定义指示器
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());//设置图片描述显示动画
        mDemoSlider.setDuration(4000);//设置滚动时间，也是计时器时间
        mDemoSlider.addOnPageChangeListener(onPageChangeListener);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        int length = imageRes.length;

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,//数据来源
                R.layout.menuitems,//item的XML实现

                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.img_dqns, R.id.txt_dqns});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,name[position],Toast.LENGTH_LONG).show();
            }
        });
    }

    private BaseSliderView.OnSliderClickListener onSliderClickListener=new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(MainActivity.this,slider.getBundle().get("extra") + "",
                    Toast.LENGTH_SHORT).show();
        }


    };

    //页面改变监听
    private ViewPagerEx.OnPageChangeListener onPageChangeListener=new ViewPagerEx.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            Log.d("ansen", "Page Changed: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };
}
