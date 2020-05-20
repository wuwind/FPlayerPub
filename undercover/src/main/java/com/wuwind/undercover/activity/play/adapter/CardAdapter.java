package com.wuwind.undercover.activity.play.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.bean.PlayerBean;

import java.util.List;

public class CardAdapter extends RecyclerBaseAdapter<PlayerBean> {

    private List<Integer> out;

    public CardAdapter(List<PlayerBean> datas, List<Integer> out) {
        super(datas);
        this.out = out;
    }

    @Override
    public int itemLayout() {
        return R.layout.item_word;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        return new RecyclerBaseHolder<PlayerBean>(itemView) {
            TextView tvWord = itemView.findViewById(R.id.tv_word);
            ImageView ivPhoto = itemView.findViewById(R.id.iv_photo);

            @Override
            public void refreshView(PlayerBean data, int position) {
                String des = "";
                itemView.setBackgroundColor(Color.WHITE);
                for (Integer aByte : out) {
                    if (aByte == position) {
                        itemView.setBackgroundColor(Color.GRAY);
                        switch (data.getType()) {
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
                Glide.with(itemView.getContext()).load(data.getPhoto()).into(ivPhoto);
                tvWord.setText((position + 1) + data.getName() + "\n" + des);
            }
        };
    }
}
