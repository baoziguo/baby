package com.baozi.baby.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baozi.baby.R;
import com.baozi.baby.app.MyApplication;

/**
 * author：baozi
 * time: 2018/3/20 16:58
 * email：baoziguoguo@foxmail.com
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showShortToast(String showString) {
        if (!TextUtils.isEmpty(showString)){
            showCustomToast(showString);
        }
    }

    /**
     * 自定义toast且防止重复toast
     * @param msg
     */
    @SuppressWarnings("unused")
    private static void showCustomToast(final String msg) {
        LayoutInflater inflate = LayoutInflater.from(MyApplication.getInstance().getApplicationContext());
        View view = inflate.inflate(R.layout.toast_rect_bg, null);
        TextView textView = (TextView) view.findViewById(R.id.toast_content);
        textView.setText(msg);
        if (mToast == null) {
            mToast = new Toast(MyApplication.getInstance().getApplicationContext());
            mToast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(MyApplication.getInstance().getApplicationContext(), 80));
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setView(view);
        mToast.show();
    }

    /**
     * 防止重复toast
     */
    private static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
