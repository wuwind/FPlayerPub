package com.wuwind.undercover.activity.edit;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.net.request.GameAddRequest;
import com.wuwind.undercover.utils.db.DbUtils;

import java.util.List;

public class EditModel extends ModelDelegate {

    public long saveGame(Game game) {
        getSequence(game);
        long gameId = DbUtils.getGameService().insert(game);
        return gameId;
    }

    public void saveGameNet(Game game) {
        getSequence(game);
        GameAddRequest request = new GameAddRequest();
        request.wordId = game.getWord_id();
        request.count= game.getCount();
        request.normal= game.getNormal();
        request.undercover= game.getUndercover();
        request.blank= game.getBlank();
        request.audience= game.getAudience();
        request.sequence= game.getSequence();
        request.requset();
    }


    public Game getLastGame() {
        List<Game> all = DbUtils.getGameService().findAll();
        if (all.isEmpty())
            return null;
        return all.get(all.size() - 1);
    }

    public List<Word> getWords() {
        List<Word> listBy = DbUtils.getWordService().findAll();
        return listBy;
    }

    private void getSequence(Game game) {
        char[] sequence = new char[game.getCount()];
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
        game.setSequence(new String(sequence));
    }

    public boolean checkCount(Game game) {
        return game.getCount() == game.getNormal() + game.getUndercover() + game.getBlank() + game.getAudience();
    }

    /**
     * @return 洗牌算法
     */
    private void shuffle(char[] sequence) {
        int i = sequence.length, j;
        char t;
        while (i > 0) {
            j = (int) Math.floor(Math.random() * i--);
            t = sequence[i];
            sequence[i] = sequence[j];
            sequence[j] = t;
        }
    }

}