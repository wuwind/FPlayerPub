package com.wuwind.undercover.activity.room.edit;

import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.response.RoomDeleteResponse;
import com.wuwind.undercover.net.response.RoomUpdateResponse;
import com.wuwind.undercover.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

public class RoomEditActivity extends BaseActivity<RoomEditView, RoomEditModel> {
    private Room room;
    @Override
    protected void bindEventListener() {
        final int roomId = getIntent().getIntExtra("roomId", 0);
        room = LitePal.find(Room.class, roomId);
        LogUtil.e(room.toString());
        viewDelegate.setLock(room.getmLock());
        viewDelegate.setOpen(room.getOpen());
        viewDelegate.setName(room.getName());
        viewDelegate.setListener(new RoomEditView.Listener() {

            @Override
            public void save(String name, int opened, int locked, int punish) {
                room.setOpen(opened);
                room.setmLock(locked);
                room.setName(name);
                room.setPunish(punish);
                modelDelegate.save(room);
            }

            @Override
            public void delete() {
                modelDelegate.delete(room);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateResponse(RoomUpdateResponse response) {
        if(response.data.getDel() == 1) {
            room.delete();
        }
        finish();
    }

}
