package com.baozi.baby.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.baozi.baby.R;
import com.baozi.baby.jsbridgewebview.BridgeHandler;
import com.baozi.baby.jsbridgewebview.BridgeWebView;
import com.baozi.baby.jsbridgewebview.CallBackFunction;
import com.baozi.baby.util.DeviceUtil;
import com.baozi.baby.util.PreferencesUtil;
import com.baozi.baby.view.NumberProgressBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public abstract class BaseWebActivity extends BaseActivity {

    protected BridgeWebView mWebView;
    private NumberProgressBar mProgressBar;
    protected String url;
    public static final String objectName = "android";
    private BaseWebActor webActor;
    private static final String HANDLER_NAME = "BaseWebHandler";
    public static final String URL = "url";
    private SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_web;
    }

    protected void initData() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            url = extra.getString(URL);
        }
    }

    @Override
    protected void initView() {

        initData();

        mWebView = (BridgeWebView) findViewById(R.id.web_view);
        mProgressBar = (NumberProgressBar) findViewById(R.id.progressbar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + " hufuapp v" + DeviceUtil.getVersionName());

        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        //打开js存储功能
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 20);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        // 设置H5的缓存
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        setWebViewClient();
        webActor = (BaseWebActor) getWebActor();
        mWebView.addJavascriptInterface(this, objectName);
        mWebView.registerHandler(HANDLER_NAME, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
            synCookies(this, url);
        }

        showLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        refreshLayout.setEnableRefresh(false);
    }

    public abstract Object getWebActor();

    public void setWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    /**
     * 后退
     */
    private void goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
        String cookie = PreferencesUtil.getPreferences("cookie", "");
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }

}
