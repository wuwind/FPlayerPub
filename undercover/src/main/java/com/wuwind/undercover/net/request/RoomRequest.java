package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.netlibrary.Response;
import com.wuwind.undercover.net.response.RoomResponse;

/**
 * Created by wuhf on 2020/7/2.
 * Description ：
 */
public class RoomRequest extends Request<RoomResponse> {

    @Override
    public String url() {
        return "/getAllRooms";
    }
}
