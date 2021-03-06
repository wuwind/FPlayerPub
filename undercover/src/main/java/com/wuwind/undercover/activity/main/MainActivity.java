package com.wuwind.undercover.activity.main;

import android.content.Intent;

import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.play.PlayActivity;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.utils.StrConverter;

public class MainActivity extends ActivityPresenter<MainView, MainModel> {

    private int count = 1;
    private Game game;
    private Word word;

    @Override
    protected void bindEventListener() {
        int gameId = getIntent().getIntExtra("gameId", 0);
        game = modelDelegate.getGame(gameId);
        if (null == game)
            return;
        word = modelDelegate.getWord(game.getWordId());
        viewDelegate.setListener(listener);
        viewDelegate.showBack(count);
    }

    MainView.Listener listener = new MainView.Listener() {
        @Override
        public void open() {
            showFront();
        }


        @Override
        public void next() {
            if (count >= game.getCount()) {
                toast("开始啦");
                startGame();
                return;
            }
            viewDelegate.showBack(++count);
        }

        @Override
        public void start() {
            startGame();
        }
    };

    private void startGame() {
        game.setFinish(1);
        modelDelegate.updateGame(game);
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra("gameId", game.getId());
        startActivity(intent);
        finishMyself();
    }

    private void showFront() {
        if (null == game)
            return;
        byte[] sequence = StrConverter.toByteArray(game.getSequence());
        if(null == sequence)
            return;
        int type = sequence[count - 1];
        switch (type) {
            case Constant.PersonType.NORMAL:
                viewDelegate.showFront(word.getW1());
                break;
            case Constant.PersonType.UNDERCOVER:
                viewDelegate.showFront(word.getW2());
                break;
            case Constant.PersonType.BLANK:
                viewDelegate.showFront("白板");
                break;
            case Constant.PersonType.AUDIENCE:
                viewDelegate.showFront("观众");
                break;
        }

    }
}
