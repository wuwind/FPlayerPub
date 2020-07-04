package com.wuwind.undercover.activity.punish.adapter;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.libwuwind.uilibrary.recyclerview.RecyclerBaseAdapter;
import com.libwuwind.uilibrary.recyclerview.RecyclerBaseHolder;
import com.wuwind.undercover.R;
import com.wuwind.undercover.db.litepal.Advise;
import com.wuwind.undercover.db.litepal.Game;
import com.wuwind.undercover.db.litepal.Word;
import com.wuwind.undercover.net.request.AdviseVisibleRequest;

import org.litepal.LitePal;

import java.util.List;

public class AdviseAdapter extends RecyclerBaseAdapter<Advise> {

    public AdviseAdapter(List<Advise> datas) {
        super(datas);
    }

    @Override
    public int itemLayout() {
        return R.layout.item_advise;
    }

    @Override
    public RecyclerBaseHolder getViewHolder(View itemView) {
        return new RecyclerBaseHolder<Advise>(itemView) {
            TextView tvNo = (TextView) itemView.findViewById(R.id.tv_no);
            TextView tvName = (TextView) itemView.findViewById(R.id.tv_name);
            TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            Switch visible = (Switch) itemView.findViewById(R.id.visible);

            @Override
            public void refreshView(final Advise data, int position) {
                tvNo.setText(position + 1 + "-" + data.getServiceId());
                tvName.setText(data.getName());
                tvContent.setText(data.getContent());
                visible.setChecked(data.getVisible() == 0);
                visible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        AdviseVisibleRequest adviseVisibleRequest = new AdviseVisibleRequest();
                        adviseVisibleRequest.id = data.getServiceId();
                        adviseVisibleRequest.visible = isChecked ? 0 : 1;
                        adviseVisibleRequest.requset();
                    }
                });
            }
        };
    }

}
