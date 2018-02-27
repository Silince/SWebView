package com.mine.saint.swebview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by c8923 on 2018/2/27.
 */

public class SWebView extends WebView {
    private Context mContext;

    public SWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private SWebView getThis() {
        return this;
    }

    private void init() {
        //支持获取手势焦点
        this.requestFocusFromTouch();
        this.addJavascriptInterface(new InsertObj(), "jsObj");
        //设置长按保存图片

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取type
                /*
                type有这几种类型：
                WebView.HitTestResult.UNKNOWN_TYPE 未知类型
                WebView.HitTestResult.PHONE_TYPE 电话类型
                WebView.HitTestResult.EMAIL_TYPE 电子邮件类型
                WebView.HitTestResult.GEO_TYPE 地图类型
                WebView.HitTestResult.SRC_ANCHOR_TYPE 超链接类型
                WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE 带有链接的图片类型
                WebView.HitTestResult.IMAGE_TYPE 单纯的图片类型
                WebView.HitTestResult.EDIT_TEXT_TYPE 选中的文字类型
                 */
                WebView.HitTestResult result = getThis().getHitTestResult();
                int type = result.getType();
                //
                return true;
            }
        });


        WebSettings mWebSettings = getSettings();

        //支持JS
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        //设置自适应屏幕
        mWebSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //支持缩放
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);//设置可以缩放
        mWebSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        mWebSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
        //支持内容重新布局???
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //支持多窗口
        mWebSettings.setSupportMultipleWindows(true);
        //关闭webview中缓存
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置可以访问文件
        mWebSettings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        mWebSettings.setNeedInitialFocus(true);
        //支持自动加载图片
        mWebSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        mWebSettings.setDefaultTextEncodingName("utf-8");
        //设置字体
        mWebSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        mWebSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        mWebSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8


        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);


        //缓存数据
        saveData(mWebSettings);
        setWebChromeClient(new BaseWebChromeClient());
        setWebViewClient(new BaseWebViewClient());

        //解决https不能正常显示的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        if (NetStatusUtil.isConnected(mContext)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        File cacheDir = mContext.getCacheDir();
        if (cacheDir != null) {
            String appCachePath = cacheDir.getAbsolutePath();
            mWebSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
            mWebSettings.setDatabaseEnabled(true);//开启 database storage API 功能
            mWebSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

            mWebSettings.setAppCachePath(appCachePath); //设置  Application Caches 缓存目录
        }
    }

    @Override
    public void destroy() {
        //LayoutInflater.from(mContext)这里需要获取父容器然后移除webview

        super.destroy();
    }

    //是否到达底端
    boolean isTop() {
        if (this.getContentHeight() * this.getScaleY() == (this.getHeight() + this.getScrollY())) {
            return true;
        }
        return false;
    }

    //是否到达顶端
    boolean isBottom() {
        if (0 == this.getScaleY()) return true;
        return false;
    }

    //返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.canGoBack()) {
            this.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    /*
    webview的一些方法
    goBack()//后退
    goForward()//前进
    goBackOrForward(intsteps) //以当前的index为起始点前进或者后退到历史记录中指定的steps，
                              如果steps为负数则为后退，正数则为前进
    canGoForward()//是否可以前进
    canGoBack() //是否可以后退
    clearCache(true);//清除网页访问留下的缓存，由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
    clearHistory()//清除当前webview访问的历史记录，只会webview访问历史记录里的所有记录除了当前访问记录.
    clearFormData()//这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据。
     */

    /*
    webview的状态
    onResume() //激活WebView为活跃状态，能正常执行网页的响应
    onPause()//当页面被失去焦点被切换到后台不可见状态，需要执行onPause动过， onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
    pauseTimers()//当应用程序被切换到后台我们使用了webview， 这个方法不仅仅针对当前的webview而是全局的全应用程序的webview，它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
    resumeTimers()//恢复pauseTimers时的动作。
    destroy()//销毁，关闭了Activity时，音乐或视频，还在播放。就必须销毁。
    注意：webview调用destory时,webview仍绑定在Activity上.这是由于自定义webview构
    建时传入了该Activity的context对象,因此需要先从父容器中移除webview,然后再销毁webview:
    rootLayout.removeView(webView);
    webView.destroy();
     */

    /**
     * 将cookie设置到 WebView
     *
     * @param url    要加载的 url
     * @param cookie 要同步的 cookie
     */
    public void syncCookie(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(mContext);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
    }

    /**
     * 获取指定 url 的cookie
     */
    public String syncCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    /**
     * 清除COOKIE
     */
    public boolean clearCookie() {
        final Boolean[] result = new Boolean[1];
        //
        CookieManager.getInstance().removeSessionCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                result[0] = value;
                //清除结果
            }
        });// 移除所有过期 cookie
        //CookieManager.getInstance().removeAllCookies(); // 移除所有的 cookie
        return result[0];
    }


}
