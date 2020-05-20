package com.wuwind.undercover.activity.play;


import android.view.View;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.play.adapter.CardAdapter;
import com.wuwind.undercover.activity.play.dialog.FinishDialog;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.base.Constant;
import com.wuwind.undercover.bean.PlayerBean;
import com.wuwind.undercover.db.Game;
import com.wuwind.undercover.db.User;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.net.request.UserRequest;
import com.wuwind.undercover.net.response.UserResponse;
import com.wuwind.undercover.utils.StrConverter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseActivity<PlayView, PlayModel> {

    private Game game;
    private List<Integer> out = new ArrayList<>();
    private CardAdapter cardAdapter;
    private int undercoverNum, normalNum;
    private List<PlayerBean> datas;
    private FinishDialog dialog;

    @Override
    protected void bindEventListener() {
        final long gameId = getIntent().getLongExtra("gameId", 0);
        game = modelDelegate.getGame(gameId);
        datas = new ArrayList<>();
        byte[] sequence = StrConverter.toByteArray(game.getSequence());
        if (null == sequence)
            return;
        for (byte b : sequence) {
            PlayerBean bean = new PlayerBean();
            bean.setType(b);
            datas.add(bean);
        }
        cardAdapter = new CardAdapter(datas, out);
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
                cardAdapter.notifyItemChanged(position);
                check();
                toast(data.getWord());
            }

        });
        dialog = new FinishDialog(this, new FinishDialog.Listener() {
            @Override
            public void finish() {
                char[] outs = new char[out.size()];
                for (int i = 0; i < out.size(); i++) {
                    outs[i] = (char) (int) out.get(i);
                }
                game.setOut(new String(outs));
                modelDelegate.updateGame(game);
                finishMyself();
            }
        });
        viewDelegate.setListener(new PlayView.Listener() {
            @Override
            public void refresh() {
                new UserRequest().requset();
            }
        });
        new UserRequest().requset();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUsersResponse(UserResponse response) {
        List<User> users = response.data;
        if (null == users || users.isEmpty()) {
            return;
        }
        for (User user : users) {
            if (user.getGameId().longValue() == game.getId().longValue()) {
                String users1 = user.getUsers();
                String[] split = users1.split(",");
                String wordIS = user.getWordIS();
                String wordsStr = user.getWords();
                String[] index = wordIS.split(",");
                String[] words = wordsStr.split(",");
                for (int i = 0; i < index.length; i++) {
                    int j = Integer.parseInt(index[i]);
                    PlayerBean bean = datas.get(j);
                    bean.setIndex(j);
                    bean.setName(split[i]);
                    bean.setPhoto(user.getWxPhoto());
                    bean.setWord(words[i]);
                }
            }
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
            game.setFinish(1);

        }
        if (normalNum == game.getNormal()) {
            showFinishDialog("卧底获胜");
            game.setFinish(2);
        }
    }

    private void showFinishDialog(String success) {
        Word word = modelDelegate.getWord(game.getWordId());
        toast(success);
        dialog.setSuccess(success);
        dialog.setNormal("平民词：" + word.getW1());
        dialog.setUndercover("卧底词：" + word.getW2());
        dialog.show();
    }
}
