package com.sharkgulf.soloera.bindcar.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharkgulf.soloera.R;
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean;
import com.sharkgulf.soloera.tool.config.PublicMangerKt;
import com.sharkgulf.soloera.tool.view.recycler.BannerLayout;
import com.trust.demo.basis.trust.utils.TrustLogUtils;

import java.util.ArrayList;

/**
 * Created by test on 2017/11/22.
 */


public class WebBannerAdapter extends RecyclerView.Adapter<WebBannerAdapter.MzViewHolder> {
    private String TAG = "WebBannerAdapter";
    private Context context;
    private ArrayList<BsGetBindInfoBean> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public WebBannerAdapter(Context context, ArrayList<BsGetBindInfoBean> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setUrlList(ArrayList<BsGetBindInfoBean> list) {
        TrustLogUtils.d(TAG,"当前list " + urlList.size() + " | list:  " + list.size());
        this.urlList = list;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public WebBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(WebBannerAdapter.MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();
//        String url = urlList.get(P);
        ImageView img =  holder.imageView;
        BsGetBindInfoBean.DataBean.BindInfoBean.BikeInfoBean bike_info = urlList.get(P).getData().getBind_info().get(0).getBike_info();
        holder.macTx.setText(PublicMangerKt.setTextStrings(R.string.cars_model_name_tx,bike_info.getModel().getModel_name()));
        holder.carNumTx.setText(PublicMangerKt.setTextStrings(R.string.cars_vin_tx,bike_info.getVin()));

       PublicMangerKt.glide(img,bike_info.getPic_b(),false,R.drawable.car_ic,false);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(P);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (urlList != null) {
            return urlList.size();
        }
        return 0;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView macTx;
        TextView carNumTx;
        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            macTx = (TextView) itemView.findViewById(R.id.item_mac_txt);
            carNumTx = (TextView) itemView.findViewById(R.id.item_car_num_txt);
        }
    }


}
