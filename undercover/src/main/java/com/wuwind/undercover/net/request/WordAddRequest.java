package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.WordAddResponse;

/**
 * Created by wuhf on 2020/5/9.
 * Description ：
 */
public class WordAddRequest extends Request<WordAddResponse> {

    public String w1;
    public String w2;

    @Override
    public String url() {
        return "/addWord";
    }
}
