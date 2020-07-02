package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.RoomDeleteResponse;
import com.wuwind.undercover.net.response.RoomUpdateResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class RoomDeleteRequest extends Request<RoomDeleteResponse> {

    public int id;

    public RoomDeleteRequest(int id) {
        this.id = id;
    }

    @Override
    public String url() {
        return "/deleteRoom";
    }
}
