package com.wuwind.netlibrary;

import android.text.TextUtils;
import android.widget.Toast;


import com.google.gson.internal.$Gson$Types;
import com.wuwind.netlibrary.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hongfengwu on 2017/3/25.
 */

public abstract class Request<T extends Response> {

    public transient boolean unPostLoading;
    public transient Object tag;
    public transient Map<String, String> map;
    public transient boolean isShowToast = true;

    public abstract String url();

    static final int modifiers = Modifier.TRANSIENT | Modifier.STATIC;

    public Request setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Request setIsShowToast(boolean isShowToast) {
        this.isShowToast = isShowToast;
        return this;
    }

    public void requset() {
//        if(!NetUtil.isConnect(context)){
//            ToastUtil.show("请检查网络");
//            return;
//        }
        if (null == map) {
            map = new HashMap<>();

//            Field[] fields = getClass().getFields();
//            for (Field field : fields) {
//                String name = field.getName();
//                if ((modifiers & field.getModifiers()) != 0) {
//                    continue;
//                }
//                try {
//                    String o = String.valueOf(field.get(this));
//                    if ("null".equals(o))
//                        continue;
//                    map.put(name, o);
//
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
            getValue(this, map);
        }
        for (String s : map.keySet()) {
            LogUtil.e(s + ":" + map.get(s));
        }
        OkHttpClientManager.postAsyn(getUrl(), new Callback(getSuperclassTypeParameter(getClass()), unPostLoading, isShowToast), map, tag);
    }

    /**
     * 递归获取对象里的基本数据类型值，重复覆盖
     * @param mObj
     */
    private void getValue(Object mObj, Map map) {
        if(null == mObj)
            return;
        Field[] fields = mObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if ((modifiers & field.getModifiers()) != 0) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object obj = field.get(mObj);
                if(obj == null)
                    continue;
                if(!isPrimitive(obj)) {
                    getValue(obj, map);
                    continue;
                }
                String o = String.valueOf(field.get(mObj));
                if ("null".equals(o))
                    continue;
                map.put(name, o);
//                System.out.println(name +" "+o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isPrimitive(Object obj) {
//		System.out.println(obj);
        try {
            Class<?> c = ((Class<?>)obj.getClass().getField("TYPE").get(null));
            //System.out.println(c);
            return c.isPrimitive();
        } catch (Exception e) {
            if(obj instanceof String)
                return true;
            return false;
        }
    }

    protected String getUrl() {
        String url = UrlConst.URL + url();
        LogUtil.e("url:" + url);
        return url;
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    static class Callback extends OkHttpClientManager.ResultCallback {

        private boolean unPostResponse;
        private boolean isShowToast;

        public Callback(Type mType) {
            super(mType);
        }

        public Callback(Type mType, boolean unPostResponse, boolean isShowToast) {
            super(mType);
            this.unPostResponse = unPostResponse;
            this.isShowToast = isShowToast;
        }

        @Override
        public void onError(okhttp3.Request request, Exception e) {

            e.printStackTrace();
            LogUtil.e("error--");
            if (!unPostResponse) {
//                LoadingResponse response = new LoadingResponse(false);
//                response.isError = true;
//                onFinish(response);
            }
        }

        @Override
        public void onResponse(Object response) {
            LogUtil.e("onResponse--" + response.getClass());
            if (!unPostResponse) {
//                onFinish(new LoadingResponse(false));
                if (response instanceof Response) {
                    Response r = (Response) response;
                    if (r.code != 1 && !TextUtils.isEmpty(r.msg) && isShowToast) {
//                        ToastUtil.show(r.msg);
                        LogUtil.e("r.msg " + r.msg);
                    }
                }
                //TODO
                EventBus.getDefault().post(response);
            }
        }

//        private void onFinish(LoadingResponse response) {
//            EventBus.getDefault().post(response);
//        }
    }


}
