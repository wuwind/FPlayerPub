package com.wuwind.undercover.activity.room.edit;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import com.wuwind.undercover.R;
import com.wuwind.undercover.activity.room.edit.RoomEditView.Listener;

public class RoomEditView extends ViewDelegate<Listener> implements View.OnClickListener {

    private EditText etName;
    private Switch open;
    private Switch lock;
    private Button btnDelete;
    private Button btnSave;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_room_edit;
    }

    @Override
    public void initWidget() {
        initViews(rootView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String name = etName.getText().toString();
                int locked = lock.isChecked() ? 1 : 0;
                int opened = open.isChecked() ? 1 : 0;
                listener.save(name, opened, locked);
                break;
            case R.id.btn_delete:
                listener.delete();
                break;
        }
    }

    private void initViews(View view) {
        etName = (EditText) view.findViewById(R.id.et_name);
        open = (Switch) view.findViewById(R.id.open);
        lock = (Switch) view.findViewById(R.id.lock);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    public void setName(String name) {
        etName.setText(name);
        etName.setSelection(name.length());
    }

    public void setOpen(int opened) {
        open.setChecked(opened == 1);
    }

    public void setLock(int locked) {
        lock.setChecked(locked == 1);
    }

    public interface Listener extends OnClick {
        void save(String name, int opened, int locked);

        void delete();
    }
}