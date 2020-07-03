package com.wuwind.undercover.activity.room.detail;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.request.GameRequest;
import com.wuwind.undercover.net.request.RoomDeleteRequest;
import com.wuwind.undercover.net.request.RoomUpdateRequest;

import org.litepal.LitePal;
import org.litepal.Operator;

import java.util.List;

public class RoomDetailModel extends ModelDelegate {

    public void getGamesByRoomNet(Room room) {
        GameRequest gameRequest = new GameRequest();
        gameRequest.roomId = room.getServiceId();
        gameRequest.requset();
    }

    public List<Game> getGamesByRoomLocal(Room room) {
        List<Game> games = Operator.where("roomId=?", "" + room.getServiceId()).find(Game.class);
        return games;
    }

}