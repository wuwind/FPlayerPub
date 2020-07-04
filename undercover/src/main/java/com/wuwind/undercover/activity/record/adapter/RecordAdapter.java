package com.wuwind.undercover.activity.record.adapter;

import android.view.View;
import android.widget.TextView;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Word;

import org.litepal.LitePal;
import org.litepal.Operator;

import java.util.List;

public class RecordAdapter extends RecyclerBaseAdapter<Game> {

    public RecordAdapter(List<Game> datas) {
        super(datas);
    }

    @Override
    public int itemLayout() {
        return R.layout.item_game_record;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        return new RecyclerBaseHolder<Game>(itemView) {
            TextView tvNo = itemView.findViewById(R.id.tv_no);
            TextView tvCount = itemView.findViewById(R.id.tv_count);
            TextView tvNormal = itemView.findViewById(R.id.tv_normal);
            TextView tvUndercover = itemView.findViewById(R.id.tv_undercover);
            TextView tvBlank = itemView.findViewById(R.id.tv_blank);
            TextView tvAudience = itemView.findViewById(R.id.tv_audience);
            TextView tvWords = itemView.findViewById(R.id.tv_words);
            TextView tvWinner = itemView.findViewById(R.id.tv_winner);

            @Override
            public void refreshView(Game data, int position) {
                tvNo.setText(position + 1 +"-"+data.getServiceId());
                tvCount.setText(data.getCount().toString());
                tvNormal.setText(data.getNormal().toString());
                tvUndercover.setText(data.getUndercover().toString());
                tvBlank.setText(data.getBlank().toString());
                tvAudience.setText(data.getAudience().toString());
                tvWords.setText(getWords(data.getWordId()));
                tvWinner.setText(getWinner(data));
            }
        };
    }

    private String getWords(int id) {
        Word word = LitePal.where("serviceId=?", id+"").findFirst(Word.class);
        if(null == word)
            return "";
        return word.getW1() + "\n" + word.getW2();
    }

    private String getWinner(Game game) {
        Integer finish = game.getFinish();
        if(null == finish) {
            return "未结束";
        }
        Integer win = game.getWin();
        if(null == win) {
            return "未知";
        }
        switch (win) {
            case 1:
                return "平民";
            case 2:
                return "卧底";
        }
        return "未知";
    }
}
