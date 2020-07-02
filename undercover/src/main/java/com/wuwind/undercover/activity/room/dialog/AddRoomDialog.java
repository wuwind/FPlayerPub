package com.wuwind.undercover.activity.room.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wuwind.undercover.R;
import com.wuwind.undercover.db.Word;
import com.wuwind.undercover.db.litepal.Room;

public class AddRoomDialog extends Dialog {
    private Listener listener;

    public AddRoomDialog(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_room);
        final EditText etRoom = findViewById(R.id.et_room);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                String roomName = etRoom.getText().toString();
                if (TextUtils.isEmpty(roomName) || TextUtils.isEmpty(roomName)) {
                    return;
                }
                Room room = new Room();
                room.setName(roomName);
                room.setOpen(1);
                listener.add(room);
                etRoom.setText(null);
            }
        });
    }

    public interface Listener {
        void add(Room room);
    }
}
