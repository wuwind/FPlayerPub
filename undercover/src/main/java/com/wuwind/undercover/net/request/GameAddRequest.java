package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.GameAddResponse;
import com.wuwind.undercover.net.response.WordAddResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class GameAddRequest extends Request<GameAddResponse> {

    public Long wordId;
    public Integer count;
    public Integer normal;
    public Integer undercover;
    public Integer blank;
    public Integer audience;
    public String sequence;
    public String outSequence;
    public String lookSequence;

    @Override
    public String url() {
        return "/addGame";
    }
}
