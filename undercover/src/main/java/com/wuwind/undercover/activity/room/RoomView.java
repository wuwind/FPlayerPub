package com.wuwind.undercover.activity.room;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.room.RoomView.Listener;

public class RoomView extends ViewDelegate<Listener> implements View.OnClickListener {

    public RecyclerView getRvList() {
        return rvList;
    }

    private RecyclerView rvList;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_room;
    }

    @Override
    public void initWidget() {
        rvList = rootView.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rootView.findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.add();
    }

    public interface Listener extends OnClick {
        void add();
    }
}