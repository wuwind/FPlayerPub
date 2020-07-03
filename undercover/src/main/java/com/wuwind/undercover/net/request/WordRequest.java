package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.WordResponse;

/**
 * Created by wuhf on 2020/5/8.
 * Description ：
 */
public class WordRequest extends Request<WordResponse> {

    @Override
    public String url() {
        return "/getAllWords";
    }
}
