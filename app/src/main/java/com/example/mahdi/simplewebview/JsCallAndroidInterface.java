package com.example.mahdi.simplewebview;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2018/8/13 下午10:47<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class JsCallAndroidInterface {

    //@JavascriptInterface注解方法，js端调用，4.2以后安全
    //4.2以前，当JS拿到Android这个对象后，就可以调用这个Android对象中所有的方法，包括系统类（java.lang.Runtime 类），从而进行任意代码执行。
    @JavascriptInterface
    public void callback(String msg) {
        Toast.makeText(SimpleApp.getInstance(), "JS方法回调到web了 ：" + msg, Toast.LENGTH_SHORT).show();
    }
}
