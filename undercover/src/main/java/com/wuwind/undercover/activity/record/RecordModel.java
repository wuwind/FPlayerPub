package com.wuwind.undercover.activity.record;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.net.request.GameAddRequest;
import com.wuwind.undercover.net.request.GameRequest;
import com.wuwind.undercover.utils.db.DbUtils;

import org.litepal.LitePal;

import java.util.List;

public class RecordModel extends ModelDelegate {

    public List<Game> getGamesLocal() {
        return LitePal.findAll(Game.class);
    }

    public Game newGame(Game game) {
        Game mGame = game.clone();
        mGame.setFinish(0);
        mGame.setSequence(null);
        mGame.setWin(0);
        mGame.save();
        return mGame;
    }

    public void clear() {
        DbUtils.getGameService().deleteAll();
    }

    public void getGamesNet() {
        new GameRequest().requset();
    }

    public void updateGame(Game game) {
//        DbUtils.getGameService().update(game);
    }

    public void updateGame(List<Game> game) {
//        DbUtils.getGameService().update(game);
    }

    public void inserteGame(Game game) {
//        DbUtils.getGameService().insert(game);
    }
}