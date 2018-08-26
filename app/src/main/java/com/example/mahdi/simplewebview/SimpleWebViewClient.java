package com.example.mahdi.simplewebview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * description：WebViewClient主要帮助WebView处理各种通知、请求事件的   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/8/13 下午9:25<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class SimpleWebViewClient extends WebViewClient {

    public SimpleWebViewClient() {
        super();
    }

    //拦截页面中的url加载,21以下的
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Log.i("ansen","拦截url:"+url);
//        if(url.equals("http://www.google.com/")){
//            Toast.makeText(MainActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
//            return true;//表示我已经处理过了
//        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    //拦截页面中的url加载,21以上的
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 页面开始加载
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.e("wjc", "onPageStarted---->url:" + url);
    }

    /**
     * 页面请求完成
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.e("wjc", "onPageFinished---->url:" + url + ",title:" + view.getTitle());
    }

    /**
     * 作用：在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
     *
     * @param view
     * @param url
     */
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
    }

    //监听WebView发出的请求并做相应的处理
    //浏览器的渲染以及资源加载都是在一个线程中，如果在shouldInterceptRequest处理时间过长，WebView界面就会阻塞
    //21以下的
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }

    //监听WebView发出的请求并做相应的处理
    //浏览器的渲染以及资源加载都是在一个线程中，如果在shouldInterceptRequest处理时间过长，WebView界面就会阻塞
    //21以上的
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    /**
     * //页面加载出现错误,23以下的 ,.访问错误时回调，例如访问网页时报错404，在这个方法回调的时候可以加载错误页面。
     *
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("wjc", "onReceivedError---->errorCode:" + errorCode + ",description:" + description+",failingUrl:"+failingUrl);
    }


    //页面加载出现错误,23以上的
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.e("wjc", "onReceivedError---->request:" + request + ",error:" + error);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    /**
     * 处理https请求
     *
     * @param view
     * @param handler
     * @param error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); //接受所有网站的证书
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        super.onReceivedClientCertRequest(view, request);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        return super.onRenderProcessGone(view, detail);
    }
}
