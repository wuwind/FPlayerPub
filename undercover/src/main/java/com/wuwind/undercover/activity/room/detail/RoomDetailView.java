package com.wuwind.undercover.activity.room.detail;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;

public class RoomDetailView extends ViewDelegate<RoomDetailView.Listener> implements View.OnClickListener {


    private Button btnEdit;

    public RecyclerView getRvList() {
        return rvList;
    }

    private android.support.v7.widget.RecyclerView rvList;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_room_detail;
    }

    @Override
    public void initWidget() {
        initViews(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                break;
            case R.id.btn_edit:
                listener.edit();
                break;
        }
    }

    private void initViews(View view) {
        btnEdit = (Button) view.findViewById(R.id.btn_edit);
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        btnEdit.setOnClickListener(this);
    }


    public interface Listener extends OnClick {

        void edit();
    }
}