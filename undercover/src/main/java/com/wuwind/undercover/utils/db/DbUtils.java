package com.wuwind.undercover.utils.db;


import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.Word;

public class DbUtils {

    public static DBService<Word> getWordService() {
        return DBService.getInstance(Word.class);
    }

    public static DBService<Game> getGameService() {
        return DBService.getInstance(Game.class);
    }

}
