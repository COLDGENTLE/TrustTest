package com.sharkgulf.soloera.cards.activity;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharkgulf.soloera.R;
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean;
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean;
import com.sharkgulf.soloera.tool.config.PublicMangerKt;
import com.trust.demo.basis.trust.utils.TrustLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 龙衣 on 17-2-20.
 */

public class BarAdapter extends RecyclerView.Adapter implements AutoLocateHorizontalView.IAutoLocateHorizontalView {
    List<HistoryBean> mSources = new ArrayList<>();
    public final static int DAY = 0;
    public final static int WEEK = 1;
    public final static int MOTH = 2;
    private Context context;
    private int maxValue = -1;
    private View itemView;
    private int maxHeight = 300;
    private String TAG = "BarAdapter";
    private int adapterType = DAY;
    public BarAdapter(Context context){
     this.context = context;
    }

    public void setAdapterTypr(int type){
        adapterType = type;
    }

    public int getMaxValue(){
        return maxValue;
    }

    public void setList(List<HistoryBean> list, int height){
        maxValue = -1;
        this.maxHeight = height;
        this.mSources = list;
        TrustLogUtils.d(TAG,"max value:--------------");
        for(HistoryBean item:list){
            if(item.rideSummaryBean.getMiles() > maxValue){
                maxValue = item.rideSummaryBean.getMiles();
            }
            TrustLogUtils.d(TAG,"max value:"+item.rideSummaryBean.getMiles() +"｜"+item.rideSummaryBean.getDate());
        }
        TrustLogUtils.d(TAG,"max value:"+maxValue+"---------");
        notifyDataSetChanged();
    }




    public void setAllList(List<HistoryBean> list){
        if (this.mSources.isEmpty()) {
            this.mSources = list;
        }else{
            this.mSources.addAll(list);
        }
        for(HistoryBean item:list){
            if(item.rideSummaryBean.getMiles() > maxValue){
                maxValue = item.rideSummaryBean.getMiles();
            }
        }
    }

    public List<HistoryBean> getList(){
        return mSources;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bar,parent,false);
        this.itemView = itemView;
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryBean historyBean = mSources.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        switch (adapterType) {
            case DAY:
                viewHolder.tvAge.setText(historyBean.dayStr);
                break;
            case WEEK:
                viewHolder.tvAge.setText(historyBean.weekStr);
                break;
            case MOTH:
                viewHolder.tvAge.setText(historyBean.mothStr);
                break;
        }


//        if (position == 1) {
//            ((ViewHolder)holder).bar.setBackgroundResource(R.drawable.item_hirstory_histogram_no_selected_bg);
//        }else if (position == 2){
//            ((ViewHolder)holder).bar.setBackgroundResource(R.drawable.item_hirstory_histogram_no_data_bg);
//        }else if (position == 3){
//            ((ViewHolder)holder).bar.setBackgroundResource(R.drawable.item_hirstory_histogram_no_selected_bg);
//        }

    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    @Override
    public View getItemView() {
        return itemView;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder,int itemWidth) {
        ViewHolder holder1 = (ViewHolder) holder;
        TrustLogUtils.d(TAG,"mSources : "+mSources.size() + "|我是 pos :"+pos);
        if (pos<= mSources.size()-1) {
            HistoryBean historyBean = mSources.get(pos);
            ViewGroup.LayoutParams params = holder1.bar.getLayoutParams();
            ViewGroup.LayoutParams bgParams = holder1.barBg.getLayoutParams();
            ViewGroup.LayoutParams tvParams = holder1.tvAge.getLayoutParams();

            TrustLogUtils.d(TAG,"真是pos ："+pos);
            if ( historyBean.rideSummaryBean.getMiles() ==0) {
                TrustLogUtils.d(TAG,"一滴都没有了 :"+pos+"|"+historyBean.rideSummaryBean.getMiles() +" |"+historyBean.rideSummaryBean.getDate());
                params.height = maxHeight /2;
            }else {
                Integer height = mSources.get(pos).rideSummaryBean.getMiles();
                if (height<10 && height != 0) {
                    int i =height+10;
                    mSources.get(pos).height = i;
                }else{
                    mSources.get(pos).height = height;
                }
                float v = mSources.get(pos).height * 1f;
                float value = 0f;
                value =  v/ maxValue;
                TrustLogUtils.d(TAG,"v:"+ (mSources.get(pos).height * 1f) + "|"+maxValue + "|"+params.height+"|"+value+"|"+(value * maxHeight)+"|"+mSources.get(pos).dayStr+"|pos:"+pos);
                params.height = (int) (value * maxHeight);
            }


            params.width = (int) (5*itemWidth/6.5);
            bgParams.width = (int) (5*itemWidth/6.5 / 14);
            tvParams.width = (int) (5*itemWidth/6.5);
            holder1.bar.setLayoutParams(params);
            holder1.barBg.setLayoutParams(bgParams);

            holder1.barBg.setX(holder1.barBg.getX() * 10);
//            holder1.tvAge.setLayoutParams(tvParams);
        }
        HistoryBean historyBean = mSources.get(pos);
        if(isSelected){
            if (historyBean.rideSummaryBean.getMiles()!=0) {
                holder1.bar.setBackgroundResource(R.drawable.item_hirstory_histogram_bg);
                holder1.tvAge.setTextColor(PublicMangerKt.getBsColor(R.color.color_515561));
//                holder1.tvAge.setBackgroundResource(R.drawable.red_bg);
            }else{
                holder1.bar.setBackgroundColor(PublicMangerKt.getBsColor(R.color.transparent));
                holder1.tvAge.setTextColor(PublicMangerKt.getBsColor(R.color.gray092));
//                holder1.tvAge.setBackgroundResource(R.drawable.red_bg);
            }

        }else{
            if (historyBean.rideSummaryBean.getMiles() == 0) {
                holder1.bar.setBackgroundColor(PublicMangerKt.getBsColor(R.color.transparent));
                holder1.tvAge.setTextColor(PublicMangerKt.getBsColor(R.color.gray092));
                holder1.tvAge.setBackgroundResource(R.drawable.history_week_radio_bg);
            }else {
                holder1.bar.setBackgroundResource(R.drawable.item_hirstory_histogram_no_selected_bg);
                holder1.tvAge.setTextColor(PublicMangerKt.getBsColor(R.color.gray092));
                holder1.tvAge.setBackgroundResource(R.drawable.history_week_radio_bg);
            }
        }
        holder1.barBg.setBackgroundColor(PublicMangerKt.getBsColor(R.color.color_eaeaea));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public View bar;
        public View barBg;
        public TextView tvAge;

        public ViewHolder(View itemView) {
            super(itemView);
            bar = (View)itemView.findViewById(R.id.view_bar);
            barBg = (View)itemView.findViewById(R.id.view_bg);
            tvAge = (TextView) itemView.findViewById(R.id.tv_bar);
        }
    }

    public static class HistoryBean{
        public Integer height = 0;//控件高度
        public String  itemName = "";

        public BsRideSummaryBean.DataBean.RideSummaryBean rideSummaryBean ;
        public BsTimeLevelBean.DataBean.TimeBean timeBean ;
        public String dayStr;
        public String weekStr;
        public String mothStr;;
        public HistoryBean(Integer height, String itemName ,
                           BsRideSummaryBean.DataBean.RideSummaryBean bean ,
                           BsTimeLevelBean.DataBean.TimeBean timeBean
        ,String dayStr,String weekStr,String mothStr) {
            this.dayStr = dayStr;
            this.weekStr = weekStr;
            this.mothStr = mothStr;
            this.height = height;
            this.itemName = itemName;
            if (bean != null) {
                this.rideSummaryBean = bean;
            }else{
                this.rideSummaryBean = new BsRideSummaryBean.DataBean.RideSummaryBean();
            }

            if(timeBean != null){
                this.timeBean = timeBean;
            }else{
                this.timeBean = new BsTimeLevelBean.DataBean.TimeBean();
            }
        }
    }
}
