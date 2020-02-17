package com.sharkgulf.soloera.cards.activity.alert

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.DEFUTE
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.tool.config.alertInfoMap
import com.sharkgulf.soloera.tool.config.getAuthentication
import com.trust.demo.basis.trust.TrustTools

/**
 *  Created by user on 2019/9/9
 */
class AlertAdapter (contexts: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mList = listOf<BsAlertBean.DataBean.ListBean>()
    private var inflater: LayoutInflater? = LayoutInflater.from(contexts)
    private var mEmptyView:Int? = null
    private val TAG = AlertAdapter::class.java.canonicalName.toString()
    private var mAlertOnclickListener:AlertOnclickListener? = null
    private var userCarList:BsGetCarInfoBean.DataBean? = null
    override fun getItemViewType(position: Int): Int {
        return if (mList.isEmpty()) {
            return DEFUTE
        }else{ position }
    }

    fun setListener(listener:AlertOnclickListener){ mAlertOnclickListener = listener }


    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): RecyclerView.ViewHolder {
        mAlertOnclickListener
        return if (pos == DEFUTE) {
            EmptyViewHolder(inflater!!.inflate(mEmptyView!!, null, false))
        }else{
            val alertViewHodler = AlertViewHodler(LayoutInflater.from(parent.context).inflate(R.layout.item_malfunction, parent, false))
            alertViewHodler.itemView.setOnClickListener {
                val listBean = mList[alertViewHodler.adapterPosition]

                mAlertOnclickListener?.onClickListener(listBean.event,listBean.result,listBean.exts)
            }
            alertViewHodler
        }

    }

    override fun getItemCount(): Int {
        return if (mList.isEmpty()) {
            1
        }else{
            mList.size
        }

    }

    fun setAlertList(list: List<BsAlertBean.DataBean.ListBean>?){
        if (list != null && list.isNotEmpty()) {
            mList = list
        }else{
            mList = arrayListOf()
        }
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (holder is AlertViewHodler) {

            if (userCarList == null) {
                userCarList = getAuthentication().getUserCarList()
            }

            val listBean = mList[pos]
            holder?.eventTx?.text = listBean.name
            holder?.msgTx?.text = "${TrustTools.getTimeInfo(listBean.ts * 1000L) } ${listBean.name}"
            val alertInfoBean = alertInfoMap[listBean.event]
            if (alertInfoBean != null) {
                holder.typeIc?.setImageResource(alertInfoBean.ic)
            }

            val bikes = userCarList?.bikes

            var find = bikes?.find { it.bike_id == listBean.bid }
            if (find != null) { holder.bikeNameTx?.text = find.bike_name }
            holder.timeTx?.text = TrustTools.getTime(listBean.ts * 1000L,"HH:mm")
//            TrustLogUtils.d(TAG,"${listBean.exts}")
//            holder.timeTx?.text = TrustTools.getTimeInfo(listBean.ts * 1000L)
//            holder.msgTx?.text = listBean.name
//            holder.numTx?.text = listBean.count.toString() + "次"
//            val alertInfoBean = alertInfoMap[listBean.event]
//            if (alertInfoBean != null) {
//                holder.typeIc?.setImageResource(alertInfoBean.ic)
//            }
//            if(pos == mList.size-1){//显示end
//                holder.viewLine?.visibility = View.GONE
//                holder.endViewLine?.visibility = View.VISIBLE
//            }else{//显示正常
//                holder.viewLine?.visibility = View.VISIBLE
//                holder.endViewLine?.visibility = View.GONE
//            }


        }

    }


    class AlertViewHodler(v:View): RecyclerView.ViewHolder(v){
        var timeTx: TextView? = null
        var msgTx: TextView? = null
        var typeIc:ImageView? = null
        var bikeNameTx:TextView? = null
        var eventTx:TextView? = null
        init {
            timeTx = v.findViewById(R.id.bike_alert_list_time_tv)
            msgTx = v.findViewById(R.id.bike_alert_list_msg_tvr)
            typeIc = v.findViewById(R.id.bike_alert_list_error_ic)
            bikeNameTx = v.findViewById(R.id.bike_alert_list_bike_name_tv)
            eventTx = v.findViewById(R.id.bike_alert_list_event_tv)

        }
    }

    fun setEmptyView(layoutId:Int){
        mEmptyView = layoutId
    }

    class EmptyViewHolder(v:View): RecyclerView.ViewHolder(v)


    interface AlertOnclickListener{
        fun onClickListener(event:Int,url:String?,exts:String?)
    }
}