package com.wuwind.undercover.activity.play.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuwind.undercover.R;

public class FinishDialog extends Dialog {
    private Listener listener;
    private TextView tvSuccess;
    private TextView tvNormal;
    private TextView tvUndercover;
    private String success;
    private String normal;
    private String undercover;

    public FinishDialog(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_finish_game);
        tvSuccess = findViewById(R.id.tv_success);
        tvNormal = findViewById(R.id.tv_normal);
        tvUndercover = findViewById(R.id.tv_undercover);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.finish();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        tvSuccess.setText(success);
        tvNormal.setText(normal);
        tvUndercover.setText(undercover);
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public void setUndercover(String undercover) {
        this.undercover = undercover;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public interface Listener {
        void finish();
    }
}
