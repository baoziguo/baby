package com.baozi.baby.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.baozi.baby.R;
import com.baozi.baby.jsbridgewebview.BridgeHandler;
import com.baozi.baby.jsbridgewebview.BridgeWebView;
import com.baozi.baby.jsbridgewebview.CallBackFunction;
import com.baozi.baby.jsbridgewebview.WebViewClientCallback;
import com.baozi.baby.view.NumberProgressBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public abstract class BaseWebFragment extends BaseFragment {

    protected BridgeWebView mWebView;
    private NumberProgressBar mProgressBar;
    protected String url;

    public static final String objectName = "android";
    private BaseWebActor webActor;

    private static final String HANDLER_NAME = "BaseWebHandler";
    private SmartRefreshLayout refreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    protected void initView() {

        url = getWebUrl();

        mWebView = (BridgeWebView) getConvertView().findViewById(R.id.web_view);
        mProgressBar = (NumberProgressBar) getConvertView().findViewById(R.id.progressbar);

        getConvertView().findViewById(R.id.title_bar).setVisibility(View.GONE);
        refreshLayout = getConvertView().findViewById(R.id.refreshLayout);

        refreshLayout.setEnableAutoLoadmore(false);
        String ua = mWebView.getSettings().getUserAgentString();
//        mWebView.getSettings().setUserAgentString(ua + " baozi v" + DeviceUtil.getVersionName());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        //打开js存储功能
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 20);
        String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
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
        webActor = new BaseWebActor(mWebView);
        mWebView.addJavascriptInterface(this, objectName);
        mWebView.registerHandler(HANDLER_NAME, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
            synCookies(getActivity(), url);
        }

    }

    public abstract String getWebUrl();

    private void setWebViewClient() {
        mWebView.setWebViewClientCallback(mClientCallback);
    }

    private WebViewClientCallback mClientCallback = new WebViewClientCallback() {
        @Override
        public void pageFinishedCallBack(WebView view, String url) {
            refreshLayout.finishRefresh(true);
        }

        @Override
        public void pageStartedCallBack(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void receivedSslErrorCallBack(WebView view, SslErrorHandler handler, SslError error) {
        }

        @Override
        public void receivedErrorCallBack(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void shouldOverrideUrlLoadingCallBack(WebView view, String url) {
            if (url.startsWith("http") && url.contains("childQlandPage")) {
                Intent intent = new Intent(getActivity(), CommonWebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                mWebView.pageUp(true);
            }
        }
    };

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
//        String cookie = PreferencesUtil.getPreferences("cookie", "");
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, "");
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.clearCache(true);
            mWebView.destroy();
            mWebView = null;
        }
    }

}
