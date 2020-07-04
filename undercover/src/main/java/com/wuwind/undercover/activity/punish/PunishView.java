package com.wuwind.undercover.activity.punish;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.punish.PunishView.Listener;

public class PunishView extends ViewDelegate<Listener> implements View.OnClickListener {

    private android.support.v7.widget.RecyclerView rvList;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_punish;

    }

    @Override
    public void initWidget() {
        initViews(rootView);
    }

    @Override
    public void onClick(View v) {

    }

    public RecyclerView getRvList() {
        return rvList;
    }

    private void initViews(View view) {
        rvList = (android.support.v7.widget.RecyclerView) view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public interface Listener extends OnClick {

    }
}