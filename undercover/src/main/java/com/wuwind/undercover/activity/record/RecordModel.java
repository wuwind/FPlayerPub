package com.wuwind.undercover.activity.record;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.utils.db.DbUtils;

import java.util.List;

public class RecordModel extends ModelDelegate {

    public List<Game> getGames() {
        return DbUtils.getGameService().findAll();
    }

    public long newGame(Game game) {
        Game mGame = game.clone();
        mGame.setFinish(null);
        mGame.setId(null);
        mGame.setSequence(null);
        return DbUtils.getGameService().insert(mGame);
    }

    public void clear() {
        DbUtils.getGameService().deleteAll();
    }
}