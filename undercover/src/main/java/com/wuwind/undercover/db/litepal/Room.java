package com.wuwind.undercover.db.litepal;

import org.litepal.crud.LitePalSupport;

/**
 * Created by wuhf on 2020/6/30.
 * Description ：
 */
public class Room extends LitePalSupport {
    private String name;
    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
