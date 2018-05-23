package com.njau.agricultural_assistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class Video extends Activity {
    private WebView webView;
    private ProgressDialog dialog;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.video_view);
        Intent intent = getIntent();
        intent.getExtras();
        Bundle data = intent.getExtras();
        url=data.getString("url");
        init();

//        Uri uri = Uri.parse("http://www.jmzsjy.com/UploadFile/微课/地方风味小吃——宫廷香酥牛肉饼.mp4");
//        VideoView videoView = (VideoView)this.findViewById(R.id.video_view);
//        videoView.setMediaController(new MediaController(this));
//        videoView.setVideoURI(uri);
//        videoView.start();
//        videoView.requestFocus();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
        //String type = "video/* ";

//        Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        //intent.setDataAndType(uri, type);
//        startActivity(intent);
    }
    private void init() {
        webView = (WebView) findViewById(R.id.webView);
        //WebView加载本地资源
//        webView.loadUrl("file:///android_asset/example.html");
        //WebView加载web资源
        webView.loadUrl(url);
        //覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
                view.loadUrl(url);
                return true;
            }
            //WebViewClient帮助WebView去处理一些页面控制和请求通知
        });

        WebSettings settings = webView.getSettings();
        //WebView加载页面优先使用缓存加载
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true); // 关键点
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSupportZoom(true); // 支持缩放
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
        //页面加载

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress   1-100之间的整数
                if (newProgress == 100) {
                    //页面加载完成，关闭ProgressDialog
                    closeDialog();
                } else {
                    //网页正在加载，打开ProgressDialog
                    openDialog(newProgress);
                }
            }

            private void openDialog(int newProgress) {
                if (dialog == null) {
                    dialog = new ProgressDialog(Video.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }

            private void closeDialog() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();   //返回上一页面
                return true;
            } else {
                System.exit(0);     //退出程序
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
