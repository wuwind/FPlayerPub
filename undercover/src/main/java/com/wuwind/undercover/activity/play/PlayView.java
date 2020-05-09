package com.wuwind.undercover.activity.play;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.play.PlayView.Listener;

public class PlayView extends ViewDelegate<Listener> implements View.OnClickListener {

    public RecyclerView getRvCards() {
        return rvCards;
    }

    private RecyclerView rvCards;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initWidget() {
        rvCards = rootView.findViewById(R.id.rv_cards);
        rvCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    @Override
    public void onClick(View v) {

    }

    public interface Listener extends OnClick {

    }
}