package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.WordByIdResponse;
import com.wuwind.undercover.net.response.WordResponse;

/**
 * Created by wuhf on 2020/5/8.
 * Description ：
 */
public class WordByIdRequest extends Request<WordByIdResponse> {

    private int wordId;

    public WordByIdRequest(int wordId) {
        this.wordId = wordId;
    }

    @Override
    public String url() {
        return "/getWordById";
    }
}
