package com.baozi.baby.net;

import android.app.Activity;

import com.lzy.okgo.request.base.Request;

import java.lang.reflect.Type;

public abstract class DialogCallBack<T> extends JsonCallBack<T>{
    private LoadingDialog loadingDialog;


    public DialogCallBack(Activity activity,Class<T> clazz) {
        super(clazz);
        initDialog(activity);
    }
    public DialogCallBack(Activity activity,Type type) {
        super(type);
        initDialog(activity);
    }

    protected void initDialog(Activity activity){
        loadingDialog = new LoadingDialog(activity);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        loadingDialog.show();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        loadingDialog.dismiss();
    }
}
