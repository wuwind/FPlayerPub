package com.wuwind.netlibrary;

/**
 * Created by hongfengwu on 2017/3/25.
 */

public class Response<T> {

    public int code;
    public String msg;
    public T data;
    public String type;
    public Object tag;
}
