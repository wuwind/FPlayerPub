package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.GameResponse;
import com.wuwind.undercover.net.response.WordResponse;

/**
 * Created by wuhf on 2020/5/8.
 * Description ：
 */
public class GameRequest extends Request<GameResponse> {

    public Integer roomId;

    @Override
    public String url() {
        return "/games";
    }
}
