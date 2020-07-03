package com.wuwind.undercover.activity.room.detail;

import android.content.Intent;
import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.undercover.activity.play.PlayActivity;
import com.wuwind.undercover.activity.room.adapter.GameAdapter;
import com.wuwind.undercover.activity.room.edit.RoomEditActivity;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.response.GameResponse;
import com.wuwind.undercover.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

public class RoomDetailActivity extends BaseActivity<RoomDetailView, RoomDetailModel> {

    private Room room;
    private GameAdapter gameAdapter;
    @Override
    protected void bindEventListener() {
        final int roomId = getIntent().getIntExtra("roomId", 0);
        room = LitePal.find(Room.class, roomId);
        LogUtil.e(room.toString());
        viewDelegate.setListener(new RoomDetailView.Listener() {
            @Override
            public void edit() {
                Intent intent = new Intent(RoomDetailActivity.this, RoomEditActivity.class);
                intent.putExtra("roomId", room.getId());
                startActivity(intent);
            }
        });
        gameAdapter = new GameAdapter(null);
        viewDelegate.getRvList().setAdapter(gameAdapter);
        gameAdapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<Game>() {
            @Override
            public void onItemClick(View view, Game data, int position) {
                Intent intent = new Intent(RoomDetailActivity.this, PlayActivity.class);
                intent.putExtra("gameId", data.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDelegate.getGamesByRoomNet(room);
        refresh();
    }

    private void refresh() {
        gameAdapter.setDatas(modelDelegate.getGamesByRoomLocal(room));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGameResponse(GameResponse response) {
        List<Game> games = response.data;
        for (Game game : games) {
            game.saveFromService();
        }
        refresh();
    }

}
