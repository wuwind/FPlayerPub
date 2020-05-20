package com.wuwind.undercover.activity.edit;

import android.content.Intent;
import android.widget.ArrayAdapter;

import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.main.MainActivity;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.Word;

import java.util.List;
import java.util.Random;

public class EditActivity extends ActivityPresenter<EditView, EditModel> {

    private List<Word> words;

    @Override
    protected void bindEventListener() {
        viewDelegate.setListener(new EditView.Listener() {
            @Override
            public void getGame(Game game) {
                if(words.isEmpty())
                    return;
                int wordIndex = viewDelegate.getSpiner().getSelectedItemPosition();
                if (wordIndex == 0) {
                    wordIndex = new Random().nextInt(words.size());
                } else {
                    wordIndex--;
                }
                game.setWordId(words.get(wordIndex).getId());
                long newGameId = modelDelegate.saveGame(game);
                modelDelegate.saveGameNet(game);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                intent.putExtra("gameId", newGameId);
                startActivity(intent);
                finishMyself();
            }
        });
        words = modelDelegate.getWords();
        if (words.size() > 0) {
            long wordId = getIntent().getLongExtra("word_id", 0);
            int selectPosition = 0;
            String[] strings = new String[words.size() + 1];
            strings[0] = "随机";
            for (int i = 0; i < words.size(); i++) {
                strings[i + 1] = words.get(i).getW1() + "-" + words.get(i).getW2();
                if(wordId == words.get(i).getId()) {
                    selectPosition = i+1;
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, strings);
            viewDelegate.getSpiner().setAdapter(adapter);
            viewDelegate.getSpiner().setSelection(selectPosition);
        }
        viewDelegate.initByGame(modelDelegate.getLastGame());

    }

}
