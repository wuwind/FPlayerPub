package com.wuwind.undercover.activity.word.adapter;

import android.view.View;
import android.widget.TextView;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.db.litepal.Word;

import java.util.List;

public class WordAdapter extends RecyclerBaseAdapter<Word> {

    public WordAdapter(List<Word> datas) {
        super(datas);
    }

    @Override
    public int itemLayout() {
        return R.layout.item_word;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        final TextView tvWord = itemView.findViewById(R.id.tv_word);
        return new RecyclerBaseHolder<Word>(itemView) {

            @Override
            public void refreshView(Word data, int position) {
                tvWord.setText(data.getW1() + "\n" + data.getW2());
            }
        };
    }

}
