package com.wuwind.undercover.activity.launcher;

import android.content.Intent;

import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.edit.EditActivity;
import com.wuwind.undercover.activity.record.RecordActivity;
import com.wuwind.undercover.activity.word.WordActivity;
import com.wuwind.undercover.db.litepal.Room;
import com.wuwind.undercover.net.request.WordRequest;
import com.wuwind.undercover.utils.LogUtil;

import org.litepal.LitePal;

import java.util.List;

public class LauncherActivity extends ActivityPresenter<LauncherView, LauncherModel> {

    @Override
    protected void bindEventListener() {
        viewDelegate.setListener(new LauncherView.Listener() {
            @Override
            public void wordClick() {
                startActivity(new Intent(LauncherActivity.this, WordActivity.class));
            }

            @Override
            public void recordClick() {
                startActivity(new Intent(LauncherActivity.this, RecordActivity.class));
            }

            @Override
            public void newClick() {
                startActivity(new Intent(LauncherActivity.this, EditActivity.class));
            }
        });
        Room room = new Room();
        room.setName("007");
        room.save();

    }
}
