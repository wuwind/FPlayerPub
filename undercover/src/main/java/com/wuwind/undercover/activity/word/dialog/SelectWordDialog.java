package com.wuwind.undercover.activity.word.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wuwind.undercover.R;

public class SelectWordDialog extends Dialog implements View.OnClickListener {
    private Listener listener;

    public SelectWordDialog(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_word);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_new).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.btn_delete) {
            listener.delete();
        } else {
            listener.newGame();
        }
    }

    public interface Listener {
        void delete();

        void newGame();
    }
}
