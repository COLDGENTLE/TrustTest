package com.sharkgulf.soloera.home.user.cars

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.config.setTextStrings

/**
 *  Created by user on 2019/4/16
 */
class CarDeleteAdapter: RecyclerView.Adapter<CarDeleteAdapter.CarDeleteHodler>() {
    private var mList = arrayListOf<ProtocolBean>()
    private var mDeleteAdapterListener:DeleteAdapterListener? = null
    fun init(deleteAdapterListener:DeleteAdapterListener):CarDeleteAdapter{
        mDeleteAdapterListener = deleteAdapterListener
        mList = arrayListOf()
        mList.add(ProtocolBean(setTextStrings(R.string.del_car_1),false))
        mList.add(ProtocolBean(setTextStrings(R.string.del_car_2),false))
        mList.add(ProtocolBean(setTextStrings(R.string.del_car_3),false))
        mList.add(ProtocolBean(setTextStrings(R.string.del_car_4),false))
        return this
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CarDeleteHodler {
        val carDeleteHodler = CarDeleteHodler(LayoutInflater.from(parent.context).inflate(R.layout.item_car_delete_layout, parent, false))
        carDeleteHodler.itemView.setOnClickListener {
            val adapterPosition = carDeleteHodler.adapterPosition
            mList[adapterPosition].status = !mList[adapterPosition].status
            carDeleteHodler.img?.setImageResource(if( mList[adapterPosition].status) R.drawable.del_bike_protocol_ic else R.drawable.del_bike_step_no_start_ic)
            checkAllAception()
        }
        return carDeleteHodler
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CarDeleteHodler, pos: Int) {
        holder.tx?.text = mList[pos].msg
    }


    class CarDeleteHodler(v:View): RecyclerView.ViewHolder(v){
        var img : ImageView? = null
        var tx:TextView? = null
        init {
            img = v.findViewById(R.id.item_car_deleter_img)
            tx = v.findViewById(R.id.item_car_deleter_tx)
        }
    }
    private fun checkAllAception(){
        var status = true
        mList.forEach {
            if (!it.status) {
              status = false
                return@forEach
            }
        }
        mDeleteAdapterListener!!.isAceiptionListener(status)
    }

    public interface DeleteAdapterListener{
        fun isAceiptionListener(boolean: Boolean)
    }

    class ProtocolBean(var msg :String = "",var status :Boolean = false)
}