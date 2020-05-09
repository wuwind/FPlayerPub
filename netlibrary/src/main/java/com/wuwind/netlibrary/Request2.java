//package com.wuwind.netlibrary;
//
//import com.fg.baseapp.util.LogUtil;
//import com.google.gson.Gson;
//import com.google.gson.internal.$Gson$Types;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.Map;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.schedulers.Schedulers;
//
///**
// * Created by wuhf on 2017/9/19.
// */
//
//public abstract class Request2<T extends Response> {
//
//    public transient boolean unPostLoading;
//    public transient Object tag;
//    public transient Map<String, String> map;
//    private static Gson mGson = new Gson();
//
//    public abstract String url();
//
//    static final int modifiers = Modifier.TRANSIENT | Modifier.STATIC;
//
//    public Observable<T> requset() {
//        if (null == map) {
//            map = new HashMap<>();
//
//            Field[] fields = getClass().getFields();
//            for (Field field : fields) {
//                String name = field.getName();
//                if ((modifiers & field.getModifiers()) != 0) {
//                    continue;
//                }
//                try {
//                    String o = String.valueOf(field.get(this));
//                    if (null == o || "null".equals(o))
//                        continue;
//                    System.out.println("--" + name);
//                    System.out.println("--" + o);
//                    map.put(name, o);
//
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return Observable.create(new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
//                e.onNext(dealResult(OkHttpClientManager.post(getUrl(), map)));
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io());
//
//    }
//
//    private T dealResult(String result){
//        if(null == result)
//            return null;
//        T o = mGson.fromJson(result, getSuperclassTypeParameter(getClass()));
//        return o;
//    }
//
//    private String getUrl() {
//        String url = UrlConst.URL + url();
//        LogUtil.e("url:" + url);
//        return url;
//    }
//
//    static Type getSuperclassTypeParameter(Class<?> subclass) {
//        Type superclass = subclass.getGenericSuperclass();
//        if (superclass instanceof Class) {
//            throw new RuntimeException("Missing type parameter.");
//        }
//        ParameterizedType parameterized = (ParameterizedType) superclass;
//        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//    }
//
//}
