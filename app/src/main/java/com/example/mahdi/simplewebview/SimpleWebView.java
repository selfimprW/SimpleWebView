package com.example.mahdi.simplewebview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.webkit.WebView;

/**
 * description： https://github.com/CarGuo/CustomActionWebView.git  <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/8/13 下午10:11<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class SimpleWebView extends WebView {
    public SimpleWebView(Context context) {
        super(context);
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
