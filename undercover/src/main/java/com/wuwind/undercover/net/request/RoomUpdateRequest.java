package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.RoomAddResponse;
import com.wuwind.undercover.net.response.RoomUpdateResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class RoomUpdateRequest extends Request<RoomUpdateResponse> {

    public String name;
    public int open;
    public int mLock;
    public int id;

    public RoomUpdateRequest(int id,String name, int open,int mLock) {
        this.id = id;
        this.name = name;
        this.open = open;
        this.mLock = mLock;
    }

    @Override
    public String url() {
        return "/updateRoom";
    }
}
