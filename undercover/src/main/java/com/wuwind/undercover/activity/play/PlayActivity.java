package com.wuwind.undercover.activity.play;


import android.util.Log;
import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.undercover.activity.play.adapter.CardAdapter;
import com.wuwind.undercover.activity.play.dialog.FinishDialog;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.bean.PlayerBean;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.User;
import com.wuwind.undercover.net.response.GameFinishResponse;
import com.wuwind.undercover.net.response.UserResponse;
import com.wuwind.undercover.net.response.WordByIdResponse;
import com.wuwind.undercover.utils.StrConverter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayActivity extends BaseActivity<PlayView, PlayModel> {

    private Game game;
    private Word word;
    private List<Integer> out = new ArrayList<>();
    private CardAdapter cardAdapter;
    private int undercoverNum, normalNum;
    private List<PlayerBean> datas;
    private FinishDialog dialog;

    @Override
    protected void bindEventListener() {
        int gameId = getIntent().getIntExtra("gameId", 0);
        this.game = modelDelegate.getGameLocal(gameId);
        if (null == game) {
            toast("没有找到游戏");
            finish();
            return;
        }
        datas = new ArrayList<>();
        byte[] sequence = StrConverter.toByteArray(this.game.getSequence());
        if (null == sequence)
            return;
        for (byte b : sequence) {
            PlayerBean bean = new PlayerBean();
            bean.setType(b);
            datas.add(bean);
        }
        cardAdapter = new CardAdapter(datas, out);
        word = modelDelegate.getWordLocal(game.getWordId());
        cardAdapter.setWord(word);
        viewDelegate.getRvCards().setAdapter(cardAdapter);
        cardAdapter.setClickListener(new RecyclerBaseAdapter.OnItemClickListener<PlayerBean>() {
            @Override
            public void onItemClick(View view, PlayerBean data, int position) {
                for (int i = 0; i < out.size(); i++) {
                    if (out.get(i) == position) {
                        out.remove(i);
                        cardAdapter.notifyItemChanged(position);
                        check();
                        return;
                    }
                }
                out.add(position);
                Log.e("palyactivity", " out " + Arrays.toString(out.toArray()));
                cardAdapter.notifyItemChanged(position);
                check();
            }

        });
        dialog = new FinishDialog(this, new FinishDialog.Listener() {
            @Override
            public void finish() {
                finishGame();
            }
        });
        viewDelegate.setListener(new PlayView.Listener() {
            @Override
            public void refresh() {
                modelDelegate.getUserFromNet(game.getRoomId());
            }

            @Override
            public void showType(boolean isChecked) {
                cardAdapter.showType(isChecked);
            }
        });
        modelDelegate.getUserFromNet(game.getRoomId());
        modelDelegate.getWordFromNet(game.getWordId());
    }

    private void finishGame() {
        game.setFinish(1);
        modelDelegate.finishGameNet(game.getServiceId(), game.getWin());
        modelDelegate.updateGame(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.setOut(Arrays.toString(out.toArray()));
        modelDelegate.updateGame(game);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGameFinishResponse(GameFinishResponse response) {
        if (response.code != 1) {
            toast("提交失败");
        }
        finishMyself();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWordByIdResponse(WordByIdResponse response) {
        if (response.code == 1) {
            word = response.data;
            cardAdapter.setWord(word);
            word.saveFromService();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUsersResponse(UserResponse response) {
        List<User> users = response.data;
        if (null == users || users.isEmpty()) {
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            PlayerBean bean = datas.get(i);
            User u = users.get(i);
            bean.setName(u.getUsers());
            bean.setPhoto(u.getWxPhoto());
        }
        cardAdapter.notifyDataSetChanged();
    }

    private void check() {
        undercoverNum = 0;
        normalNum = 0;
        for (int position : out) {
            byte data = datas.get(position).getType();
            if (data == Constant.PersonType.UNDERCOVER) {
                undercoverNum++;
            } else if (data == Constant.PersonType.NORMAL) {
                normalNum++;
            }
        }
        if (undercoverNum == game.getUndercover()) {
            showFinishDialog("平民获胜");
            game.setWin(1);

        }
        if (normalNum == game.getNormal()) {
            showFinishDialog("卧底获胜");
            game.setWin(2);
        }
    }

    private void showFinishDialog(String success) {
        toast(success);
        dialog.setSuccess(success);
        if (null != word) {
            dialog.setNormal("平民词：" + word.getW1());
            dialog.setUndercover("卧底词：" + word.getW2());
        }
        dialog.show();
    }
}
