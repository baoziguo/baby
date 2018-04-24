package com.baozi.baby.base;

import android.webkit.WebView;

/**
 * 和js通讯的基类
 *
 * @author jiahua.huang 2018/04/11
 */
public class BaseWebActor {

    private WebView webView;

    public BaseWebActor(WebView webView) {
        this.webView = webView;
    }

}
