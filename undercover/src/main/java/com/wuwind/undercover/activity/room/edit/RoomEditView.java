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
    private EditText etNum;
    private EditText etTotal;
    private Switch open;
    private Switch lock;
    private Switch punish;
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
                String num = etNum.getText().toString();
                String total = etTotal.getText().toString();
                int locked = lock.isChecked() ? 1 : 0;
                int opened = open.isChecked() ? 1 : 0;
                int punished = open.isChecked() ? 0 : 1;
                listener.save(name, opened, locked, punished, num, total);
                break;
            case R.id.btn_delete:
                listener.delete();
                break;
        }
    }

    private void initViews(View view) {
        etName = (EditText) view.findViewById(R.id.et_name);
        etNum = (EditText) view.findViewById(R.id.et_num);
        etTotal = (EditText) view.findViewById(R.id.et_total);
        open = (Switch) view.findViewById(R.id.open);
        lock = (Switch) view.findViewById(R.id.lock);
        punish = (Switch) view.findViewById(R.id.punish);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    public void setName(String name) {
        etName.setText(name);
        etName.setSelection(name.length());
    }

    public void setNum(String num) {
        etNum.setText(num);
        etName.setSelection(num.length());
    }

    public void setTotal(String num) {
        etTotal.setText(num);
        etTotal.setSelection(num.length());
    }

    public void setOpen(int opened) {
        open.setChecked(opened == 1);
    }

    public void setLock(int locked) {
        lock.setChecked(locked == 1);
    }

    public interface Listener extends OnClick {
        void save(String name, int opened, int locked, int punish, String num, String total);

        void delete();
    }
}