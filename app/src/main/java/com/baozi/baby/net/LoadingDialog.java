package com.baozi.baby.net;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.baozi.baby.R;

/**
 * 网络请求加载对话框
 */
public class LoadingDialog {
    private Dialog loadingDialog;
    private ImageView iv_dialog;
    private Animation animation;

    public LoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        iv_dialog = (ImageView) v.findViewById(R.id.iv_loading);
        animation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(v);
    }

    public LoadingDialog(Context context, boolean isCancelable) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        iv_dialog = v.findViewById(R.id.iv_loading);
        animation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setCanceledOnTouchOutside(isCancelable);
        loadingDialog.setCancelable(isCancelable);
        loadingDialog.setContentView(v);
    }

    public void show() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            iv_dialog.startAnimation(animation);
            loadingDialog.show();
        }
    }

    public void dismiss() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            iv_dialog.clearAnimation();
            loadingDialog.dismiss();
        }
    }

}
