package com.wuwind.undercover.activity.room.detail;

import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.utils.LogUtil;

import org.litepal.LitePal;

public class RoomDetailActivity extends ActivityPresenter<RoomDetailView, RoomDetailModel> {
    private Room room;
    @Override
    protected void bindEventListener() {
        final int roomId = getIntent().getIntExtra("roomId", 0);
        room = LitePal.find(Room.class, roomId);
        LogUtil.e(room.toString());
        viewDelegate.setLock(room.getmLock());
        viewDelegate.setOpen(room.getOpen());
        viewDelegate.setName(room.getName());
        viewDelegate.setListener(new RoomDetailView.Listener() {
            @Override
            public void save(String name, int opened, int locked) {
                room.setOpen(opened);
                room.setmLock(locked);
                room.setName(name);
                modelDelegate.save(room);
//                finish();
            }

            @Override
            public void delete() {
                modelDelegate.delete(room);
//                finish();
            }
        });
    }
}
