package com.wuwind.undercover.activity.edit;

import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.db.Game;

public class EditView extends ViewDelegate<EditView.Listener> implements View.OnClickListener {

    private EditText etCount;
    private EditText etNormal;
    private EditText etUndercover;
    private EditText etBlank;
    private EditText etAudience;
    private AppCompatSpinner spWord;
    private Button btnStart;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    public void initWidget() {
        initViews(getRootView());
    }

    private void initViews(View view) {
        etCount = (EditText) view.findViewById(R.id.et_count);
        etNormal = (EditText) view.findViewById(R.id.et_normal);
        etUndercover = (EditText) view.findViewById(R.id.et_undercover);
        etBlank = (EditText) view.findViewById(R.id.et_blank);
        etAudience = (EditText) view.findViewById(R.id.et_audience);
        spWord = view.findViewById(R.id.sp_word);
        btnStart = (Button) view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Game game = new Game();
//        game.setFinish(0);
        game.setCount(getCount(etCount));
        game.setNormal(getCount(etNormal));
        game.setUndercover(getCount(etUndercover));
        game.setBlank(getCount(etBlank));
        game.setAudience(getCount(etAudience));
        if (listener != null) {
            listener.getGame(game);
        }
    }

    private int getCount(EditText editText) {
        String text = editText.getText().toString();
        if (TextUtils.isEmpty(text))
            return 0;
        return Integer.parseInt(text);
    }

    public interface Listener extends OnClick {
        void getGame(Game game);
    }

    public AppCompatSpinner getSpiner() {
        return spWord;
    }

    public void initByGame(Game game) {
        if(null == game)
            return;
        etCount.setText(game.getCount().toString());
        etNormal.setText(game.getNormal().toString());
        etUndercover.setText(game.getUndercover().toString());
        etBlank.setText(game.getBlank().toString());
        etAudience.setText(game.getAudience().toString());
    }
}