package com.wuwind.undercover.activity.room;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.request.RoomAddRequest;
import com.wuwind.undercover.net.request.RoomRequest;

public class RoomModel extends ModelDelegate {

    public void getAllRooms() {
        new RoomRequest().requset();
    }

    public void addRoom(Room room) {
        new RoomAddRequest(room.getName(), room.getOpen()).requset();
    }
}