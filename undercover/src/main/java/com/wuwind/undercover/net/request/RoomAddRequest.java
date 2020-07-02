package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.RoomAddResponse;
import com.wuwind.undercover.net.response.WordAddResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class RoomAddRequest extends Request<RoomAddResponse> {

    public String name;
    public int open;

    public RoomAddRequest(String name, int open) {
        this.name = name;
        this.open = open;
    }

    @Override
    public String url() {
        return "/addRoom";
    }
}
