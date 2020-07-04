package com.wuwind.undercover.activity.punish;

import com.wuwind.ui.base.ActivityPresenter;
import com.wuwind.undercover.activity.punish.adapter.AdviseAdapter;
import com.wuwind.undercover.base.BaseActivity;
import com.wuwind.undercover.db.litepal.Advise;
import com.wuwind.undercover.net.response.AdviseResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PunishActivity extends BaseActivity<PunishView, PunishModel> {

    private AdviseAdapter adviseAdapter;
    @Override
    protected void bindEventListener() {
        adviseAdapter = new AdviseAdapter(null);
        viewDelegate.getRvList().setAdapter(adviseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        modelDelegate.getAllAdviseNet();
    }

    @Override
    protected void refreshData() {
        super.refreshData();
        adviseAdapter.setDatas(modelDelegate.getAllAdviseLocal());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAdviseResponse(AdviseResponse response) {
        if(response.code == 1) {
            for (Advise advise : response.data) {
                advise.saveFromService();
            }
            refreshData();
        }
    }
}
