package com.wuwind.undercover.activity.play;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.net.request.GameFinishRequest;
import com.wuwind.undercover.utils.db.DbUtils;

public class PlayModel extends ModelDelegate {

    public Game getGame(long id) {
        return DbUtils.getGameService().findById(id);
    }

    public void updateGame(Game game) {
        DbUtils.getGameService().update(game);
    }

    public Word getWord(long wordId) {
        return DbUtils.getWordService().findById(wordId);
    }

    public void finishGameNet(long gameId) {
        new GameFinishRequest(gameId).requset();
    }

//    public List<>
}