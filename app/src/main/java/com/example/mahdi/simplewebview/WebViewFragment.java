package com.example.mahdi.simplewebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * description： https://blog.csdn.net/carson_ho/article/details/52693322
 * https://blog.csdn.net/yk377657321/article/details/71668608 <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/8/13 下午9:02<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class WebViewFragment extends Fragment implements View.OnLongClickListener {

    private static final String WEB_API = "web_api";
    private SimpleWebView mWebView;
    private String webApi;
    private SmoothProgressBar progress;

    public static WebViewFragment getInstance(String api) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WEB_API, api);
        fragment.setArguments(bundle);
        return fragment;
    }

    public String getCurWebApi() {
        return webApi;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            webApi = bundle.getString(WEB_API);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        progress = rootView.findViewById(R.id.progress);
//        progress.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity()).interpolator(new AccelerateInterpolator()).build());
        mWebView = rootView.findViewById(R.id.webview);
        initWebView();
        return rootView;
    }

    private void initWebView() {
        mWebView.setOnLongClickListener(this);

        mWebView.setWebChromeClient(new SimpleWebChromeClient());
        mWebView.setWebViewClient(new SimpleWebViewClient());

        setWebView();

        loadUrl();
    }

    /**
     * 配置webView
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    void setWebView() {
        //声明WebSettings子类
        WebSettings webSettings = mWebView.getSettings();

//        在Android 5.0之后，WebView默认不允许Https + Http的混合使用方式，所以当Url是Https的，图片资源是Http时，导致页面加载失败。需要设置 MixedContentMode属性允许Https+Http混用。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //支持Javascript交互
        webSettings.setJavaScriptEnabled(true);
        //增加js交互接口
        mWebView.addJavascriptInterface(new JsCallAndroidInterface(), "JSCallBackInterface");//添加js监听 这样html就能调用客户端


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        if (NetworkUtil.isNetworkConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件

        //对于不需要使用 file 协议的应用，禁用 file 协议；防止文件泄密，file协议即是file://
        //webSettings.setAllowFileAccess(false);
        //webSettings.setAllowFileAccessFromFileURLs(false);
        //webSettings.setAllowUniversalAccessFromFileURLs(false);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        /**
         * 咱Android这边儿为了提升网页加载速度，在手机系统是Android8以及以上的时候，会关闭网络图片自动加载，在页面加载完毕之后再加载img标签等指向的图片资源，
         * 但是那个链接在浏览器打开可以发现，直接就是一张图片，没有对应的html代码，所以webview加载完毕之后，找不到要重新加载的img标签，导致图片加载不出来白屏
         */
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //网页中触发下载动作
            }
        });


        //DOM storage 是HTML5提供的一种标准接口，主要将键值对存储在本地，在页面加载完毕后可以通过JavaScript来操作这些数据，但是Android 默认是没有开启这个功能的，则导致H5页面加载失败
        // todo .如果不开启，则Api.android_interview不显示
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
//
        String innerCacheDir = SimpleApp.getInstance().getCacheDir().getPath();
        Log.e("wjc", "setAppCachePath--->path:" + innerCacheDir);
        webSettings.setAppCachePath(innerCacheDir); //设置  Application Caches 缓存目录
    }

    private void loadUrl() {
        mWebView.loadUrl("https://pic1.zhuanstatic.com/zhuanzh/n_v20691f0260bef455b84dc2ede6e01e802.jpg?tt=1B73A2C2C8ABC2DD580B91175EC63EDD1571412555505&zzv=9.7.2&webview=zzn");
        // 格式规定为:file:///android_asset/文件名.html
//        mWebView.loadUrl("file:///android_asset/localHtml.html");
        //方式1. 加载远程网页：
        //mWebView.loadUrl("http://www.google.com/");
        //方式2：加载asset的html页面
        //mWebView.loadUrl("file:///android_asset/localHtml.html");
        //方式3：加载手机SD的html页面
        //mWebView.loadUrl("file:///mnt/sdcard/database/taobao.html");
    }



    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public boolean canGoBack() {
        if (mWebView.canGoBack()) {//点击返回按钮的时候判断有没有上一页
            mWebView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }

    //https://www.jianshu.com/p/16713361bbd3
    @Override
    public boolean onLongClick(View v) {
        WebView.HitTestResult result = null;
        try {
            result = ((WebView) v).getHitTestResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            return false;
        }
        final String extra = result.getExtra();
        switch (result.getType()) {
            case WebView.HitTestResult.ANCHOR_TYPE://api 14废弃  1
                break;
            case WebView.HitTestResult.PHONE_TYPE: //打开一个电话号码   2
                break;
            case WebView.HitTestResult.GEO_TYPE: //打开一个map地址   3
                break;
            case WebView.HitTestResult.EMAIL_TYPE://打开一个邮件地址   4
                break;
            case WebView.HitTestResult.IMAGE_TYPE: //打开一个html img标签   5
                //type:5,extra:https://developers.weixin.qq.com/miniprogram/dev/image/quickstart/basic/register.png?t=18081317
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = SaveImageUtil.downloadImage(extra);
                        final String mapSgr = map.toString();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), mapSgr, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                break;
            case WebView.HitTestResult.IMAGE_ANCHOR_TYPE: //api14废弃    6
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE: //打开一个html a标签，内容是一个http网址    7
                // type:7,extra:https://mp.weixin.qq.com/wxopen/waregister?action=step1
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE://打开一个html a标签，内容是由http及img标签组成    8
                //type:8,extra:https://github.com/Piasy/BigImageViewer/raw/master/art/fresco_big_image_viewer_demo.gif
                //type:8,extra:https://github.com/Piasy/BigImageViewer/raw/master/art/android_studio_memory_monitor.png

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = SaveImageUtil.downloadImage(extra);
                        final String mapSgr = map.toString();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), mapSgr, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                break;
            case WebView.HitTestResult.EDIT_TEXT_TYPE: //打开一个可编辑的区域    9
                break;
            case WebView.HitTestResult.UNKNOWN_TYPE: //打开的内容未知    0
                break;
        }
        Log.e("wjc", "onLongClick--->type:" + result.getType() + ",extra:" + result.getExtra());
        return false;
    }

    public void retryLoad() {
        mWebView.reload();
    }
}
