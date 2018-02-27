package com.mine.saint.swebview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by c8923 on 2018/2/27.
 */

public class BaseWebViewClient extends WebViewClient{
    /**
     * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
