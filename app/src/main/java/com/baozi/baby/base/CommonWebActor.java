package com.baozi.baby.base;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * 统一的和js通讯的类
 * @author jiahua.huang 2018/04/11
 *
 */
public class CommonWebActor extends BaseWebActor {


	public CommonWebActor(WebView webView) {
		super(webView);
	}
	
	private boolean isCanBack;

	/**
	 * js控制返回到上一个网页还是结束本activity
	 */
	public boolean getIsCanBack() {
		return isCanBack;
	}

	@JavascriptInterface
	public void setIsCanBack(boolean isCanBack) {
		this.isCanBack = isCanBack;
	}
	
	/**
	 * 拨打电话
	 * 
	 * @param phone
	 */
	@JavascriptInterface
	public void call(String phone) {

	}

}
