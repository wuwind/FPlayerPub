package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.net.response.GameFinishResponse;

/**
 * Created by wuhf on 2020/5/27.
 * Description ：
 */
public class GameFinishRequest extends Request<GameFinishResponse> {

    public int gameId;
    public int finish;
    public int win;

    public GameFinishRequest(int serviceId, int win) {
        this.gameId = serviceId;
        this.finish = 1;
        this.win = win;
    }

    @Override
    public String url() {
        return "/finish";
    }
}
