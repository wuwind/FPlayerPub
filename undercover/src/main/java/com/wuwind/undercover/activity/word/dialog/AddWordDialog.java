package com.wuwind.undercover.activity.word.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wuwind.undercover.R;
import com.wuwind.undercover.db.Word;

public class AddWordDialog extends Dialog {
    private Listener listener;

    public AddWordDialog(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_word);
        final EditText etWord1 = findViewById(R.id.et_word1);
        final EditText etWord2 = findViewById(R.id.et_word2);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                String word1 = etWord1.getText().toString();
                String word2 = etWord2.getText().toString();
                if (TextUtils.isEmpty(word1) || TextUtils.isEmpty(word2)) {
                    return;
                }
                Word word = new Word();
                word.setW1(word1);
                word.setW2(word2);
                listener.add(word);
                etWord1.setText(null);
                etWord2.setText(null);
            }
        });
    }

    public interface Listener {
        void add(Word word);
    }
}
