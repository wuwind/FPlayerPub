package com.wuwind.undercover.activity.edit;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.net.request.GameAddRequest;
import com.wuwind.undercover.utils.db.DbUtils;

import org.litepal.LitePal;

import java.util.List;

public class EditModel extends ModelDelegate {


    public void saveGame(Game game) {
        game.save();
    }

    public void saveGameNet(Game game) {
        getSequence(game);
        GameAddRequest request = new GameAddRequest();
        request.wordId = game.getWordId();
        request.count = game.getCount();
        request.normal = game.getNormal();
        request.undercover = game.getUndercover();
        request.blank = game.getBlank();
        request.audience = game.getAudience();
        request.sequence = game.getSequence();
        request.roomId = game.getRoomId();
        request.requset();
    }


    public Game getLastGame() {
        return LitePal.findLast(Game.class);
    }

    public List<Word> getWords() {
        return LitePal.findAll(Word.class);
    }

    public List<Room> getRooms() {
        return LitePal.findAll(Room.class);
    }

    private void getSequence(Game game) {
        byte[] sequence = new byte[game.getCount()];
        for (int i = 0; i < sequence.length; i++) {
            if (i < game.getNormal()) {
                sequence[i] = Constant.PersonType.NORMAL;
            } else if (i < game.getNormal() + game.getUndercover()) {
                sequence[i] = Constant.PersonType.UNDERCOVER;
            } else if (i < game.getNormal() + game.getUndercover() + game.getBlank()) {
                sequence[i] = Constant.PersonType.BLANK;
            } else {
                sequence[i] = Constant.PersonType.AUDIENCE;
            }
        }
        shuffle(sequence);
        StringBuffer sb = new StringBuffer();
        for (byte b : sequence) {
            sb.append(b);
        }
        game.setSequence(sb.toString());
    }

    public boolean checkCount(Game game) {
        return game.getCount() == game.getNormal() + game.getUndercover() + game.getBlank() + game.getAudience();
    }

    /**
     * @return 洗牌算法
     */
    private void shuffle(byte[] sequence) {
        int i = sequence.length, j;
        byte t;
        while (i > 0) {
            j = (int) Math.floor(Math.random() * i--);
            t = sequence[i];
            sequence[i] = sequence[j];
            sequence[j] = t;
        }
    }

}