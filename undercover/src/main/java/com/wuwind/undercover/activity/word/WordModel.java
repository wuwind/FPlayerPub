package com.wuwind.undercover.activity.word;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.bean.WordBean;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.db.WordDao;
import com.wuwind.undercover.net.request.WordAddRequest;
import com.wuwind.undercover.net.request.WordRequest;
import com.wuwind.undercover.utils.db.DBService;
import com.wuwind.undercover.utils.db.DbUtils;

import org.greenrobot.greendao.Property;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class WordModel extends ModelDelegate {

    public List<Word> getWords() {
        return LitePal.findAll(Word.class);
    }

//    public long addWords(Word word) {
//        List<Property> properties = new ArrayList<Property>();
//        properties.add(WordDao.Properties.W1);
//        properties.add(WordDao.Properties.W2);
//        List<Object> values = new ArrayList<>();
//        values.add(word.getW1());
//        values.add(word.getW2());
//        List<Word> list = wordService.findList(properties, values);
//        if (!list.isEmpty())
//            return -2;
//        values.clear();
//        values.add(word.getW2());
//        values.add(word.getW1());
//        list = wordService.findList(properties, values);
//        if (!list.isEmpty())
//            return -2;
//        return wordService.insert(word);
//    }

    public void delete(Word word) {
//        DbUtils.getWordService().deleteById(word.getId());
    }

    public void addWordsNet(Word word) {
        WordAddRequest request = new WordAddRequest();
        request.w1 = word.getW1();
        request.w2 = word.getW2();
        request.requset();
    }

    public void getWordsNet() {
        WordRequest request = new WordRequest();
        request.requset();
    }
}