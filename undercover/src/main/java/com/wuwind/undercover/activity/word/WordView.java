package com.wuwind.undercover.activity.word;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.word.WordView.Listener;

public class WordView extends ViewDelegate<Listener> implements View.OnClickListener {

    private RecyclerView rvWords;


    @Override
    public int getRootLayoutId() {
        return R.layout.activity_word;
    }

    @Override
    public void initWidget() {
        rvWords = rootView.findViewById(R.id.rv_words);
        rvWords.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rootView.findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.addWord();
    }

    public RecyclerView getRvWords() {
        return rvWords;
    }

    public interface Listener extends OnClick {
        void addWord();
    }

    public void showAddDialog() {

    }
}