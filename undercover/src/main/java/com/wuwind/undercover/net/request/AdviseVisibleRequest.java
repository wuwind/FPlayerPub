package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.AdviseResponse;
import com.wuwind.undercover.net.response.AdviseVisibleResponse;

/**
 * Created by wuhf on 2020/5/8.
 * Description ：
 */
public class AdviseVisibleRequest extends Request<AdviseVisibleResponse> {

    public int visible;
    public int id;

    @Override
    public String url() {
        return "/setVisibleAdvises";
    }
}
