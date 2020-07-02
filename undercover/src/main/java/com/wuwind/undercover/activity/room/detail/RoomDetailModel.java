package com.wuwind.undercover.activity.room.detail;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.request.RoomDeleteRequest;
import com.wuwind.undercover.net.request.RoomUpdateRequest;

public class RoomDetailModel extends ModelDelegate {

    public void save(Room room) {
        new RoomUpdateRequest(room.getId(), room.getName(), room.getOpen(), room.getmLock()).requset();;
    }

    public void delete(Room room) {
        new RoomDeleteRequest(room.getId()).requset();
    }
}