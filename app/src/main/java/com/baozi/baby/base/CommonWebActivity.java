package com.baozi.baby.base;

/**
 * 统一的web页面
 *
 * @author jiahua.huang 2018/04/11
 */
public class CommonWebActivity extends BaseWebActivity {

    public static final String URL = BaseWebActivity.URL;

    @Override
    public Object getWebActor() {
        return new CommonWebActor(mWebView);
    }
}
