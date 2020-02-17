package com.sharkgulf.soloera.home.user.checkininfo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsPointDetailBean
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import com.trust.demo.basis.trust.TrustTools
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by user on 2019/7/17
 */
class CheckinInfoAdapter: RecyclerView.Adapter<CheckinInfoAdapter.checkinInfoViewHolder>() {
    private var ml :ArrayList<BsPointDetailBean.DataBean.DetailsBean> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): checkinInfoViewHolder {
        return checkinInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_checin_info_item,parent,false))
    }

    override fun getItemCount(): Int {
        return ml.size
    }

    fun setData(isupdate:Boolean = false,list:ArrayList<BsPointDetailBean.DataBean.DetailsBean>){
        if (isupdate) {
            ml.addAll(list)
        }else{
            ml = list
        }
        notifyDataSetChanged()
    }

    fun getLostDataTime():Long{
        return if (ml.isEmpty()) {
            TrustTools.getSystemTimeDataMillisecond()
        }else{
            ml[ml.size-1].ts
        }
    }


    override fun onBindViewHolder(hodler: checkinInfoViewHolder, pos: Int) {
        val bean = ml[pos]
//        hodler.msgTv?.text = "${bean.name}  ${bean.channel}  ${bean.points}  ${bean.type}  ${TrustTools.getTime(bean.ts *1000)}"
        hodler.msgTv?.text = setTextSpanneds(R.string.integral_info_item_tx
                , bean.name, TrustTools.getTime(Date(bean.ts * 1000), "yyyy-MM-dd HH:mm:ss"), 20)
        hodler.numTv?.text = setTextSpanneds(R.string.integral_info_item_num_tx, "+", bean.points.toString(), 25)

    }


    class checkinInfoViewHolder(v:View) : RecyclerView.ViewHolder(v){
        var msgTv:TextView? = null
        var numTv:TextView? = null
        init {
            msgTv = itemView.findViewById(R.id.item_checin_info_msg_tx)
            numTv = itemView.findViewById(R.id.integral_info_item_num_tx)
        }
    }
}