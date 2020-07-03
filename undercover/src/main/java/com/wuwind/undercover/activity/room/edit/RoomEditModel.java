package com.wuwind.undercover.activity.room.edit;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.request.RoomUpdateRequest;

public class RoomEditModel extends ModelDelegate {

    public void save(Room room) {
        new RoomUpdateRequest(room).requset();
    }

    public void delete(Room room) {
        room.setDel(1);
        new RoomUpdateRequest(room).requset();
    }
}