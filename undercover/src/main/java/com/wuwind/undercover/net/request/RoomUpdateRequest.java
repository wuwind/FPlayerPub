package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.response.RoomAddResponse;
import com.wuwind.undercover.net.response.RoomUpdateResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class RoomUpdateRequest extends Request<RoomUpdateResponse> {

    public Room room;
    public int id;

    public RoomUpdateRequest(Room room) {
        room.setId(room.getServiceId());
        this.room = room;
        this.id = room.getServiceId();
    }

    @Override
    public String url() {
        return "/updateRoom";
    }
}
