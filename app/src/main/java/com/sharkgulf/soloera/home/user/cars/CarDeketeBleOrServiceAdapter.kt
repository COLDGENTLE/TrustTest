package com.sharkgulf.soloera.home.user.cars

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*

/**
 *  Created by user on 2019/4/16
 */
class CarDeketeBleOrServiceAdapter: RecyclerView.Adapter<CarDeketeBleOrServiceAdapter.BleOrServiceViewHolder>() {
    private val mList = arrayListOf<BleOrServiceBean>()
    private var mBleOrServiceListener:BleOrServiceListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): BleOrServiceViewHolder {
        val bleOrServiceViewHolder = BleOrServiceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ble_or_service_layout, parent, false))
        bleOrServiceViewHolder.retryBtn?.setOnClickListener {
            mBleOrServiceListener?.bleOrServiceListener(bleOrServiceViewHolder.adapterPosition)
        }
        return bleOrServiceViewHolder
    }

    fun init():CarDeketeBleOrServiceAdapter{
        mList.add(BleOrServiceBean("STEP 1","连接车辆","",DELETE_CAR_NO_START))
        mList.add(BleOrServiceBean("STEP 2","解除车辆绑定状态","",DELETE_CAR_NO_START))
        mList.add(BleOrServiceBean("STEP 3","从服务器删除该车辆  ","",DELETE_CAR_NO_START))
        return this
    }

    fun setmBleOrServiceListener(bleOrServiceListener:BleOrServiceListener):CarDeketeBleOrServiceAdapter{
        mBleOrServiceListener = bleOrServiceListener
        return this
    }


    fun getStemp(pos:Int,statusMsgTx: String,status: Int){
        mList[pos].status = status
        mList[pos].statusMsgTx = statusMsgTx
        notifyItemChanged(pos)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BleOrServiceViewHolder, pos: Int) {

        val bleOrServiceBean = mList[pos]
        when (bleOrServiceBean.status) {
            DELETE_CAR_NO_START -> {
                changeUi(holder, bleOrServiceBean,R.drawable.del_bike_step_no_start_ic,
                        View.VISIBLE,View.GONE,"#969696","#969696",
                        R.drawable.line_bg,"0")
            }
            DELETE_CAR_STARTING -> {
                changeUi(holder, bleOrServiceBean,R.drawable.del_bike_step_success_ic,
                        View.GONE,View.VISIBLE,"#0099ff","#000000",
                        R.drawable.line_bg,"0")
            }
            DELETE_CAR_SUCCESS -> {
                changeUi(holder, bleOrServiceBean,R.drawable.del_bike_step_success_ic,
                        View.VISIBLE,View.GONE,"#0099ff","#000000",
                        0,"#0099FF")
            }
            DELETE_CAR_ERROR ->{
                changeUi(holder, bleOrServiceBean,R.drawable.del_bike_step_error_ic,
                        View.VISIBLE,View.GONE,"#ff0000","#000000",
                        R.drawable.line_bg)
            }

        }

        if (pos == mList.size-1) {
            holder.bgView?.visibility = View.INVISIBLE

            if (bleOrServiceBean.status == DELETE_CAR_ERROR) {
                holder.retryBtn?.visibility = View.VISIBLE
            }else{
                holder.retryBtn?.visibility = View.GONE
            }
        }

    }

    fun changeUi(holder: BleOrServiceViewHolder, bleOrServiceBean: BleOrServiceBean,
                 statusIcId:Int,statusIcVisibility:Int,statusProgressBarVisibility:Int,
                 stepTxTextColor:String,stepMsgTxTextColor:String,
                 bgViewBackgroundId:Int = 0,bgViewBackgroundColor:String = "0") {
        holder.statusIc?.setImageResource(statusIcId)
        holder.statusIc?.visibility = statusIcVisibility
        holder.statusProgressBar?.visibility = statusProgressBarVisibility
        holder.stepTx?.setTextColor(Color.parseColor(stepTxTextColor))
        holder.stepMsgTx?.setTextColor(Color.parseColor(stepMsgTxTextColor))
        if (bgViewBackgroundId != 0) {
            holder.bgView?.setBackgroundResource(bgViewBackgroundId)
        }
        if (bgViewBackgroundColor != "0") {
            holder.bgView?.setBackgroundColor(Color.parseColor(bgViewBackgroundColor))
        }
        holder.stepTx?.text = bleOrServiceBean.stepTx
        holder.stepMsgTx?.text = bleOrServiceBean.stepMsgTx
        holder.statusMsgTx?.text = bleOrServiceBean.statusMsgTx
    }


    class BleOrServiceViewHolder(v:View): RecyclerView.ViewHolder(v){
        var bgView:View? = null
        var stepTx:TextView? = null
        var stepMsgTx:TextView? = null
        var statusMsgTx:TextView? = null
        var statusIc:ImageView? = null
        var statusProgressBar:ProgressBar? = null
        var retryBtn:Button? = null
        init {
            bgView = v.findViewById(R.id.ble_or_service_bg_view)
            stepTx = v.findViewById(R.id.ble_or_service_step_tx)
            stepMsgTx = v.findViewById(R.id.ble_or_service_step_msg_tx)
            statusMsgTx = v.findViewById(R.id.ble_or_service_status_msg_tx)
            statusIc = v.findViewById(R.id.item_ble_or_service_status_ic)
            statusProgressBar = v.findViewById(R.id.item_ble_or_service_status_progressbar)
            retryBtn = v.findViewById(R.id.ble_or_service_retry_btn)
        }
    }

    interface BleOrServiceListener{
        fun bleOrServiceListener(pos:Int)
    }

    class BleOrServiceBean(var stepTx:String,var stepMsgTx:String,var statusMsgTx:String,var status:Int)
}