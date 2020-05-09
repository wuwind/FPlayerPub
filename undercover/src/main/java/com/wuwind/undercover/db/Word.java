package com.wuwind.undercover.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "WORD".
 */
@Entity
public class Word {

    @Id(autoincrement = true)
    private Long id;
    private String w1;
    private String w2;

    @Generated
    public Word() {
    }

    public Word(Long id) {
        this.id = id;
    }

    @Generated
    public Word(Long id, String w1, String w2) {
        this.id = id;
        this.w1 = w1;
        this.w2 = w2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getW1() {
        return w1;
    }

    public void setW1(String w1) {
        this.w1 = w1;
    }

    public String getW2() {
        return w2;
    }

    public void setW2(String w2) {
        this.w2 = w2;
    }

}
