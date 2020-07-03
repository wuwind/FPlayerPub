package com.wuwind.undercover.activity.room;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.undercover.activity.room.adapter.RoomAdapter;
import com.wuwind.undercover.activity.room.detail.RoomDetailActivity;
import com.wuwind.undercover.activity.room.edit.RoomEditActivity;
import com.wuwind.undercover.activity.room.dialog.AddRoomDialog;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.response.RoomAddResponse;
import com.wuwind.undercover.net.response.RoomResponse;
import com.wuwind.undercover.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

public class RoomActivity extends BaseActivity<RoomView, RoomModel> {

    private RoomAdapter roomAdapter;
    private List<Room> all;

    @Override
    protected void bindEventListener() {
        viewDelegate.setListener(new RoomView.Listener() {
            @Override
            public void add() {
                new AddRoomDialog(RoomActivity.this, new AddRoomDialog.Listener() {
                    @Override
                    public void add(Room room) {
                        modelDelegate.addRoom(room);
                    }
                }).show();
            }
        });
        final RecyclerView rvList = viewDelegate.getRvList();
        roomAdapter = new RoomAdapter(all);
        rvList.setAdapter(roomAdapter);
        roomAdapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<Room>() {
            @Override
            public void onItemClick(View view, Room data, int position) {
                Intent intent = new Intent(RoomActivity.this, RoomDetailActivity.class);
                intent.putExtra("roomId", data.getId());
                startActivity(intent);
            }
        });
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDelegate.getAllRooms();
    }

    private void refresh() {
        all = LitePal.findAll(Room.class);
        roomAdapter.setDatas(all);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addRoomNet(RoomAddResponse response) {
        modelDelegate.getAllRooms();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRoomsNet(RoomResponse response) {
        LogUtil.e(response.data.toString());
        boolean inset;
//        for (Room room : response.data) {
//            inset = true;
//            room.setServiceId(room.getId());
//            for (Room room1 : all) {
//                if (room.getServiceId() == room1.getServiceId()) {
//                    inset = false;
//                    if (room.getDel() == 1) {
//                        room1.delete();
//                    } else {
//                        room.update(room1.getId());
//                    }
//                    break;
//                }
//            }
//            if (inset && room.getDel() != 1) {
//                boolean save = room.save();
//                LogUtil.e("save:" + save);
//            }
//        }
        for (Room room : response.data) {
            if(room.getDel() == 1) {
                room.delFromService();
            } else {
                room.saveFromService();
            }
        }
        refresh();
    }

}
