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
import com.wuwind.undercover.db.Game;

public class RecordActivity extends ActivityPresenter<RecordView, RecordModel> {

    private AlertDialog.Builder newGameDialog;
    private Game clickGame;
    private RecordAdapter recordAdapter;

    @Override
    protected void bindEventListener() {
        RecyclerView rvRecord = viewDelegate.getRvRecord();
        recordAdapter = new RecordAdapter(modelDelegate.getGames());
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
}
