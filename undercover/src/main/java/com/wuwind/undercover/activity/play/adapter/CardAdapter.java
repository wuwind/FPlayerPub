package com.wuwind.undercover.activity.play.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.base.Constant;

import java.util.List;

public class CardAdapter extends RecyclerBaseAdapter<Byte> {

    private List<Integer> out;

    public CardAdapter(List<Byte> datas, List<Integer> out) {
        super(datas);
        this.out = out;
    }

    @Override
    public int itemLayout() {
        return R.layout.item_word;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        return new RecyclerBaseHolder<Byte>(itemView) {
            TextView tvWord = itemView.findViewById(R.id.tv_word);

            @Override
            public void refreshView(Byte data, int position) {
                String des = "";
                itemView.setBackgroundColor(Color.WHITE);
                for (Integer aByte : out) {
                    if(aByte == position) {
                        itemView.setBackgroundColor(Color.GRAY);
                        switch (data) {
                            case Constant.PersonType.NORMAL:
                                des = "平民";
                                break;
                            case Constant.PersonType.UNDERCOVER:
                                des = "卧底";
                                break;
                            case Constant.PersonType.BLANK:
                                des = "白板";
                                break;
                            case Constant.PersonType.AUDIENCE:
                                des = "观众";
                                break;
                        }
                        break;
                    }
                }
                tvWord.setText(position + 1 + "号牌\n" + des);
            }
        };
    }
}
