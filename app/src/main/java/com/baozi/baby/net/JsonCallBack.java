package com.baozi.baby.net;

import android.text.TextUtils;
import com.baozi.baby.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.json.JSONObject;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;

public abstract class JsonCallBack<T> extends AbsCallback<T> {

    private Class<T> clazz;
    private Type type;

    public JsonCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallBack(Type type) {
        this.type = type;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        //可添加头
    }


    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        JSONObject jsonObject = new JSONObject(body.string());
//        if(!jsonObject.getString("code").equals("0")){
//            throw new HttpException(jsonObject.getString("code"),jsonObject.getString("message"));
//        }

        if (jsonObject.getString("code").equals("80001")) {
//            SPUtils.getInstance().put("cookie", "");
            new MyCookieStore(OkGo.getInstance().getContext()).removeAllCookie();
        }

        if(!jsonObject.getString("code").equals("0")){
            throw new IllegalAccessError(jsonObject.toString());
        }

        if (clazz == String.class) {
            //只需要String数据
            return (T) (jsonObject.toString());
        }
        if (clazz == null) {
            //直接返回JSONObject
            return (T) jsonObject;
        }
        if (TextUtils.isEmpty(jsonObject.getString("data"))) {
            //请求成功， 只是没有数据
            return null;
        }

        T data = null;
        Gson gson = new Gson();
        if (clazz != null) data = gson.fromJson(jsonObject.getString("data"), clazz);
        if (type != null) data = gson.fromJson(jsonObject.getString("data"), type);

        return data;

        //以下代码是通过泛型解析实际参数,泛型必须传
//        Type genType = getClass().getGenericSuperclass();
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
//        Type type = params[0];
//        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
//
//        Gson gson = new Gson();
//        BaseModel baseModel = gson.fromJson(response.body().toString(),BaseModel.class);
//        if(baseModel.getErr_code()==0){
//            return (T)baseModel;
//        }else {
//            throw new IllegalStateException("基类错误无法解析!");
//        }

//        JsonReader jsonReader = new JsonReader(response.body().charStream());
//        Type rawType = ((ParameterizedType) type).getRawType();
//        if (rawType == BaseModel.class) {
//            BaseModel baseModel = Convert.fromJson(jsonReader, type);
//            if (baseModel.getErr_code()==200) {
//                response.close();
//                //noinspection unchecked
//                return (T) baseModel;
//            } else {
//                response.close();
//                throw new IllegalStateException("服务端接口错误");
//            }
//        } else {
//            response.close();
//            throw new IllegalStateException("基类错误无法解析!");
//        }
    }


    @Override
    public void onError(Response<T> response) {
        super.onError(response);
//        if (response.getRawResponse() == null || response.body() == null) {
//            ToastUtils.showShort("没有网络");
//        }
        if (response.getRawResponse() != null && response.getRawResponse().code() != 200) {
            ToastUtils.showShortToast("网络错误" + response.getRawResponse().code());
        }
        if (response.getException() instanceof HttpException) {
            ToastUtils.showShortToast(((HttpException) response.getException()).getMsg());
        }
        if (response.getException() instanceof IllegalAccessError) {

        }

    }
}
