package com.wuwind.undercover.activity.play;


import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.play.adapter.CardAdapter;
import com.wuwind.undercover.activity.play.dialog.FinishDialog;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.utils.StrConverter;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends ActivityPresenter<PlayView, PlayModel> {

    private Game game;
    private List<Integer> out = new ArrayList<>();
    private CardAdapter cardAdapter;
    private int undercoverNum, normalNum;
    private List<Byte> datas;
    private FinishDialog dialog;

    @Override
    protected void bindEventListener() {
        final long gameId = getIntent().getLongExtra("gameId", 0);
        game = modelDelegate.getGame(gameId);
        datas = new ArrayList<>();
        byte[] sequence = StrConverter.toByteArray(game.getSequence());
        if(null == sequence)
            return;
        for (byte b : sequence) {
            datas.add(b);
        }
        cardAdapter = new CardAdapter(datas, out);
        viewDelegate.getRvCards().setAdapter(cardAdapter);
        cardAdapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<Byte>() {
            @Override
            public void onItemClick(View view, Byte data, int position) {
                for (int i = 0; i < out.size(); i++) {
                    if (out.get(i) == position) {
                        out.remove(i);
                        cardAdapter.notifyItemChanged(position);
                        check();
                        return;
                    }
                }
                out.add(position);
                cardAdapter.notifyItemChanged(position);
                check();
            }

        });
        dialog = new FinishDialog(this, new FinishDialog.Listener() {
            @Override
            public void finish() {
                char[] outs = new char[out.size()];
                for (int i = 0; i < out.size(); i++) {
                    outs[i] = (char)(int)out.get(i);
                }
                game.setOut(new String(outs));
                modelDelegate.updateGame(game);
                finishMyself();
            }
        });
    }

    private void check() {
        undercoverNum = 0;
        normalNum = 0;
        for (int position : out) {
            byte data = datas.get(position);
            if (data == Constant.PersonType.UNDERCOVER) {
                undercoverNum++;
            } else if (data == Constant.PersonType.NORMAL) {
                normalNum++;
            }
        }
        if (undercoverNum == game.getUndercover()) {
            showFinishDialog("平民获胜");
            game.setFinish(1);

        }
        if (normalNum == game.getNormal()) {
            showFinishDialog("卧底获胜");
            game.setFinish(2);
        }
    }

    private void showFinishDialog(String success) {
        Word word = modelDelegate.getWord(game.getWord_id());
        toast(success);
        dialog.setSuccess(success);
        dialog.setNormal("平民词：" + word.getW1());
        dialog.setUndercover("卧底词：" + word.getW2());
        dialog.show();
    }
}
