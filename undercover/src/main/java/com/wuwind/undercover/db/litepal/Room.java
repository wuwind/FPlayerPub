package com.wuwind.undercover.db.litepal;

import org.litepal.crud.LitePalSupport;

/**
 * Created by wuhf on 2020/6/30.
 * Description ：
 */
public class Room extends DbSupport {
    private String name;
    private int num;
    private int gameCount;
    private String createTime;
    private int open;
    private String password;
    private int mLock;
    private int del;
    private int punish;

    public int getPunish() {
        return punish;
    }

    public void setPunish(int punish) {
        this.punish = punish;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public int getmLock() {
        return mLock;
    }

    public void setmLock(int mLock) {
        this.mLock = mLock;
    }

    @Override
    public String toString() {
        return "Room{" +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", gameCount=" + gameCount +
                ", createTime='" + createTime + '\'' +
                ", open=" + open +
                ", password='" + password + '\'' +
                ", mLock=" + mLock +
                ", del=" + del +
                '}';
    }
}
