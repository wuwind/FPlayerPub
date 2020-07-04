package com.wuwind.undercover.activity.play;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.net.request.GameFinishRequest;
import com.wuwind.undercover.net.request.UserRequest;
import com.wuwind.undercover.net.request.WordByIdRequest;
import com.wuwind.undercover.utils.db.DbUtils;

import org.litepal.LitePal;

public class PlayModel extends ModelDelegate {

    public Game getGameLocal(long id) {
        return LitePal.find(Game.class, id);
    }

    public void updateGame(Game game) {
        game.update(game.getId());
    }

    public Word getWordLocal(int wordId) {
        return LitePal.find(Word.class, wordId);
    }

    public void finishGameNet(int serviceId, int win) {
        new GameFinishRequest(serviceId, win).requset();
    }

    public void updateGameNet(Game game) {
//        new GameFinishRequest(gameId, finish).requset();
    }

    public void getUserFromNet(int roomId) {
        UserRequest userRequest = new UserRequest();
        userRequest.roomId = roomId;
        userRequest.requset();
    }

    public void getWordFromNet(int wordId) {
        new WordByIdRequest(wordId).requset();
    }
}