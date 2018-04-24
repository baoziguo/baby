package com.baozi.baby.app;

import android.app.Application;
import android.content.SharedPreferences;
import com.baozi.baby.net.MyCookieStore;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    public static SharedPreferences userPreferences;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initOkGo();
    }

    /**
     * 初始化okgo网络框架
     */
    private void initOkGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志

        //超时时间设置，默认30秒
        int DEFAULT_MILLISECONDS = 30000;
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie
        builder.cookieJar(new CookieJarImpl(new MyCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE);
    }

}
