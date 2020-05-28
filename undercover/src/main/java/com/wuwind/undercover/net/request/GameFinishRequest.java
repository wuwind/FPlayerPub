package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.net.response.GameFinishResponse;

/**
 * Created by wuhf on 2020/5/27.
 * Description ：
 */
public class GameFinishRequest extends Request<GameFinishResponse> {

    public Game game;

    public GameFinishRequest(Game game) {
        this.game = game;
    }


    @Override
    public String url() {
        return "/updateGame";
    }
}
