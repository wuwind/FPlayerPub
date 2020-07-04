package com.wuwind.undercover.activity.punish;

import com.wuwind.ui.base.ModelDelegate;
import com.wuwind.undercover.db.litepal.Advise;
import com.wuwind.undercover.net.request.AdviseRequest;

import org.litepal.LitePal;

import java.util.List;

public class PunishModel extends ModelDelegate {

    public void getAllAdviseNet() {
        new AdviseRequest().requset();
    }

    public List<Advise> getAllAdviseLocal() {
        return LitePal.findAll(Advise.class);
    }
}