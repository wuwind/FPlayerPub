package com.wuwind.undercover.activity.record;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.main.MainActivity;
import com.wuwind.undercover.activity.play.PlayActivity;
import com.wuwind.undercover.activity.record.adapter.RecordAdapter;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.net.response.GameResponse;
import com.wuwind.undercover.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class RecordActivity extends BaseActivity<RecordView, RecordModel> {

    private AlertDialog.Builder newGameDialog;
    private Game clickGame;
    private RecordAdapter recordAdapter;
    private List<Game> datas;

    @Override
    protected void bindEventListener() {
        RecyclerView rvRecord = viewDelegate.getRvRecord();
        datas = modelDelegate.getGames();
        recordAdapter = new RecordAdapter(datas);
        rvRecord.setAdapter(recordAdapter);

        recordAdapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<Game>() {
            @Override
            public void onItemClick(View view, Game game, int position) {
                clickGame = game;
                if (game.getFinish() == null) {
                    Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                    intent.putExtra("gameId", game.getId());
                    startActivity(intent);
                    finishMyself();
                } else if (game.getFinish() == 1) {
                    Intent intent = new Intent(RecordActivity.this, PlayActivity.class);
                    intent.putExtra("gameId", game.getId());
                    startActivity(intent);
                    finishMyself();
                } else {
                    showNewGameDialog();
                }
            }
        });
        viewDelegate.setListener(new RecordView.Listener() {
            @Override
            public void clear() {
                modelDelegate.clear();
                finishMyself();
            }
        });
        modelDelegate.getGamesNet();
    }


    private void showNewGameDialog() {
        if (null == newGameDialog) {
            newGameDialog = new AlertDialog.Builder(this)
                    .setMessage("再来一局？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            long newGameId = modelDelegate.newGame(clickGame);
                            Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                            intent.putExtra("gameId", newGameId);
                            startActivity(intent);
                            finishMyself();
                        }
                    });
//                    .setNegativeButton("取消")
//            newGameDialog = new AlertDialog()
        }
        newGameDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGamesResponse(GameResponse response) {
        List<Game> data = response.data;
        if(null == data || data.isEmpty())
            return;
        boolean isUpdate;
        for (Game datum : data) {
            isUpdate = false;
            for (Game game : datas) {
                if(datum.getId().equals(game.getId())) {
                    modelDelegate.updateGame(datum);
                    isUpdate =true;
                    break;
                }
            }
            if(!isUpdate)
                modelDelegate.inserteGame(datum);
        }
        datas = modelDelegate.getGames();
        recordAdapter.setDatas(datas);
    }
}
