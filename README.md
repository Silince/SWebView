# SWebView
比较好的博客推荐：https://www.jianshu.com/p/3fcf8ba18d7f
内存泄漏的处理：https://my.oschina.net/zhibuji/blog/100580



1.多窗口问题：问题描述：html中的_bank标签是新建窗口打开，有时会打不开，需要设置setSupportMultipleWindows(false)和setJavaScriptCanOpenWindowsAutomatically(true)，然后重写WebChromeClient的onCreateWindow方法。
2.html5数据缓存的问题：有时候网页需要保存一些关键数据，Android WebView需要自己设置
3.
