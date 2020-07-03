package com.wuwind.undercover.activity.word;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.undercover.activity.edit.EditActivity;
import com.wuwind.undercover.activity.word.adapter.WordAdapter;
import com.wuwind.undercover.activity.word.dialog.AddWordDialog;
import com.wuwind.undercover.activity.word.dialog.SelectWordDialog;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.net.response.WordAddResponse;
import com.wuwind.undercover.net.response.WordResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class WordActivity extends BaseActivity<WordView, WordModel> {

    private WordAdapter adapter;
    private AddWordDialog addWordDialog;
    private SelectWordDialog selectWordDialog;

    @Override
    protected void bindEventListener() {
        viewDelegate.setListener(new WordView.Listener() {
            @Override
            public void addWord() {
                addToDb();
            }
        });
        adapter = new WordAdapter(modelDelegate.getWords());
        viewDelegate.getRvWords().setAdapter(adapter);
        adapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<Word>() {
            @Override
            public void onItemClick(View view, final Word data, int position) {
                if (null == selectWordDialog) {
                    selectWordDialog = new SelectWordDialog(WordActivity.this, new SelectWordDialog.Listener() {
                        @Override
                        public void delete() {
                            adapter.deleteData(data);
                            modelDelegate.delete(data);
                        }

                        @Override
                        public void newGame() {
                            Intent intent = new Intent(WordActivity.this, EditActivity.class);
                            intent.putExtra("word_id", data.getId());
                            startActivity(intent);
                            finishMyself();
                        }
                    });
                }
                selectWordDialog.show();
            }
        });
        modelDelegate.getWordsNet();
    }

    private void addToDb() {
        if (null == addWordDialog)
            addWordDialog = new AddWordDialog(this, new AddWordDialog.Listener() {
                @Override
                public void add(Word word) {
                    modelDelegate.addWordsNet(word);
//                    long code = modelDelegate.addWords(word);
//                    if (code == -2) {
//                        toast("重复了");
//                        return;
//                    }
//                    toast("添加成功");
//                    adapter.addData(word);
                }
            });
        addWordDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getResponse(WordAddResponse response) {
        toast("添加成功");
        modelDelegate.getWordsNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWordsResponse(WordResponse response) {
        if(response.code == 1) {
            List<Word> words = response.data;
            if(null != words) {
                for (Word word : words) {
                    word.saveFromService();
                }
            }
            adapter.setDatas(modelDelegate.getWords());
        }
    }
}
