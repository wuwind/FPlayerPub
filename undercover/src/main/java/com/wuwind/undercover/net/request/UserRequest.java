package com.wuwind.undercover.net.request;

import com.wuwind.netlibrary.Request;
import com.wuwind.undercover.net.response.UserResponse;
import com.wuwind.undercover.net.response.WordResponse;

/**
 * Created by wuhf on 2020/5/8.
 * Description ：
 */
public class UserRequest extends Request<UserResponse> {

    public Integer gameId;
    public Integer roomId;

    @Override
    public String url() {
        return "/users";
    }
}
