//package com.wuwind.netlibrary;
//
//import com.google.gson.Gson;
//import com.google.gson.internal.$Gson$Types;
//
//import java.io.File;
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
//
///**
// * Created by hongfengwu on 2017/3/25.
// */
//
//public abstract class FileRequest2<T extends Response> {
//
//
//    public abstract String url();
//
//    public transient File file;
//    public transient Map<String, String> map;
//
//    static final int modifiers = Modifier.TRANSIENT | Modifier.STATIC;
//    private static Gson mGson = new Gson();
//
//    public Observable<T> requset() {
//        if (null == map) {
//            map = new HashMap<>();
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
//                e.onNext(dealResult(OkHttpClientManager.post(getUrl(), file, "file", map)));
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.newThread());
//
//    }
//
//    private T dealResult(String result) {
//        if (null == result)
//            return null;
//        T o = mGson.fromJson(result, getSuperclassTypeParameter(getClass()));
//        return o;
//    }
//
//    private String getUrl() {
//        return UrlConst.URL + url();
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
