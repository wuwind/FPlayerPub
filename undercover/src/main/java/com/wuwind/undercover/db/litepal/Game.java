package com.wuwind.undercover.db.litepal;


/**
 * Entity mapped to table "GAME".
 */
public class Game extends DbSupport implements Cloneable {

    private Integer wordId;
    private Integer count;
    private Integer normal;
    private Integer undercover;
    private Integer blank;
    private Integer audience;
    private String sequence;
    private String out;
    private Integer finish;
    private Integer roomId;
    private Integer win;//1开局2平民获胜3卧底获胜

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Game() {
    }

    @Override
    public Game clone() {
        try {
            return (Game) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getUndercover() {
        return undercover;
    }

    public void setUndercover(Integer undercover) {
        this.undercover = undercover;
    }

    public Integer getBlank() {
        return blank;
    }

    public void setBlank(Integer blank) {
        this.blank = blank;
    }

    public Integer getAudience() {
        return audience;
    }

    public void setAudience(Integer audience) {
        this.audience = audience;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getFinish() {
        return finish == null ? 0 : finish;
    }

    public void setFinish(Integer finish) {
        this.finish = finish;
    }

}
