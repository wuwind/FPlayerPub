package com.wuwind.undercover.activity.main;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.GameDao;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.utils.LogUtil;
import com.wuwind.undercover.utils.StrConverter;
import com.wuwind.undercover.utils.db.DbUtils;

import org.litepal.LitePal;

import java.util.List;

public class MainModel extends ModelDelegate {

    public void updateGame(Game game) {
        game.update(game.getId());
    }

//    public void getUnfinishGame() {
//        List<Game> listBy = DbUtils.getGameService().findListBy(GameDao.Properties.Finish, 0);
////        List<Game> listBy = DbUtils.getGameService().findAll();
//        for (Game game : listBy) {
//            byte[] sequence = StrConverter.toByteArray(game.getSequence());
//            if (null != sequence) {
//                for (byte b : sequence) {
//                    LogUtil.e(b);
//                }
//            }
//        }
//    }

    public Game getGame(int id) {
        return LitePal.find(Game.class, id);
    }

//    private void getSequence(Game game) {
//        byte[] sequence = new byte[game.getCount()];
//        for (int i = 0; i < sequence.length; i++) {
//            if (i < game.getNormal()) {
//                sequence[i] = 0;
//            } else if (i < game.getNormal() + game.getUndercover()) {
//                sequence[i] = 1;
//            } else if (i < game.getNormal() + game.getUndercover() + game.getBlank()) {
//                sequence[i] = 2;
//            } else {
//                sequence[i] = 3;
//            }
//        }
//        shuffle(sequence);
//        game.setSequence(sequence);
//    }

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

    public Word getWord(int id) {
        return LitePal.find(Word.class, id);
    }
}