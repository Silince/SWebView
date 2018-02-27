package com.mine.saint.swebview;

import android.webkit.JavascriptInterface;

/**
 * Created by c8923 on 2018/2/27.
 */

class InsertObj {
    //给html提供的方法，js中可以通过：var str = window.jsObj.HtmlcallJava(); 获取到
    @JavascriptInterface
    public String HtmlcallJavaWithoutParams() {
        return "Html call Java";
    }

    //给html提供的有参函数 ： window.jsObj.HtmlcallJavaWithParams("IT-homer blog");
    @JavascriptInterface
    public String HtmlcallJavaWithParams(final String param) {
        return "Html call Java : " + param;
    }

    //Html给我们提供的函数
    @JavascriptInterface
    public void JavacallHtmlWithoutParams() {
    }

    //Html给我们提供的有参函数
    @JavascriptInterface
    public void JavacallHtmlWithParams(final String param) {

    }

}
