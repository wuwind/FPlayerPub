package com.wuwind.undercover.activity.room.adapter;

import android.view.View;
import android.widget.TextView;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.db.litepal.Room;

import java.util.List;

public class RoomAdapter extends RecyclerBaseAdapter<Room> {

    public RoomAdapter(List<Room> datas) {
        super(datas);
    }

    @Override
    public int itemLayout() {
        return R.layout.item_word;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        final TextView tvWord = itemView.findViewById(R.id.tv_word);
        return new RecyclerBaseHolder<Room>(itemView) {

            @Override
            public void refreshView(Room data, int position) {
                tvWord.setText(data.getName() + "\n" + data.getId());
            }
        };
    }

}
