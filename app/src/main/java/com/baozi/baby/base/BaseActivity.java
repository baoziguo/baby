package com.baozi.baby.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baozi.baby.R;
import com.baozi.baby.modle.Event;
import com.baozi.baby.util.ActivityManager;
import com.baozi.baby.util.EventBusUtil;
import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Activity by:baozi
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;

    /**
     * @return activity布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //禁止横屏
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_54B4E3), 0);

        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }

        initView();
        initData();
        ActivityManager activitymanager = ActivityManager.getInstance();
        activitymanager.addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //界面不可见，取消图片加载
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
        bind.unbind();

        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 左侧返回按钮 by：baozi
     *
     * @param listener
     */
    protected void showLeftButton(View.OnClickListener listener) {//
        ImageView head_back_bt = (ImageView) findViewById(R.id.head_back_bt);
        head_back_bt.setOnClickListener(listener);
    }

    /**
     * 左侧图标替换按钮 by：baozi
     *
     * @param resourceId
     */
    protected void showLeftButton(int resourceId) {
        ImageView head_back_bt = (ImageView) findViewById(R.id.head_back_bt);
        head_back_bt.setBackgroundResource(resourceId);
    }

    /**
     * 左侧图标替换按钮 by：baozi
     *
     * @param resourceId
     * @param listener
     */
    protected void showLeftButton(int resourceId, View.OnClickListener listener) {
        ImageView head_back_bt = (ImageView) findViewById(R.id.head_back_bt);
        head_back_bt.setOnClickListener(listener);
        head_back_bt.setBackgroundResource(resourceId);
    }

    /**
     * 右侧图标替换按钮 by：baozi
     *
     * @param resourceId
     * @param listener
     */
    protected void showRightButton(int resourceId, View.OnClickListener listener) {
        ImageView iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(listener);
        iv_right.setBackgroundResource(resourceId);
    }

    /**
     * 右侧文字按键 by：baozi
     *
     * @param content
     * @param listener
     */
    protected TextView showRightText(String content, View.OnClickListener listener) {
        TextView rightTv = (TextView) findViewById(R.id.tv_right_text);
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText(content);
        rightTv.setOnClickListener(listener);
        return rightTv;
    }

    /**
     * 设置title by:baozi
     *
     * @param title
     */
    protected void setTitle(String title) {//设置title
        TextView head_center_tv = (TextView) findViewById(R.id.head_center_tv);
        head_center_tv.setText(title);
    }

    /**
     * 设置普通的title（默认包含左侧返回键）by：baozi
     *
     * @param title
     */
    protected void setNormalTitle(String title) {
        setTitle(title);
        showLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        if (event != null) {
            onReceiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyMessageEvent(Event event) {
        if (event != null) {
            onReceiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void onReceiveEvent(Event event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void onReceiveStickyEvent(Event event) {

    }

}
