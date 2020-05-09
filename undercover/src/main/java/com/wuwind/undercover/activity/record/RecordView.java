package com.wuwind.undercover.activity.record;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.record.RecordView.Listener;

public class RecordView extends ViewDelegate<Listener> implements View.OnClickListener {

    public RecyclerView getRvRecord() {
        return rvRecord;
    }

    private RecyclerView rvRecord;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public void initWidget() {
        rvRecord = rootView.findViewById(R.id.rv_record);
        rvRecord.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rootView.findViewById(R.id.btn_clear).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.clear();
    }

    public interface Listener extends OnClick {
        void clear();
    }
}