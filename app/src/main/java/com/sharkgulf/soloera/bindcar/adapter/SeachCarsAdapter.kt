package com.sharkgulf.soloera.bindcar.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsGetBindInfoBean
import com.sharkgulf.soloera.tool.config.adjustTvTextSize
import com.sharkgulf.soloera.tool.config.glide
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import java.util.ArrayList

/**
 *  Created by user on 2019/11/12
 */
class SeachCarsAdapter : RecyclerView.Adapter<SeachCarsAdapter.SeachCarsHodler>() {
    private var urlList: ArrayList<BsGetBindInfoBean>? = null
    private val TAG = SeachCarsAdapter::class.java.canonicalName
    private var mOnSeachCarItemOnClickListener:onSeachCarItemOnClickListener? = null

    fun setItemOnClickListener(listener:onSeachCarItemOnClickListener){
        mOnSeachCarItemOnClickListener = listener
    }


    fun setUrlList(list: ArrayList<BsGetBindInfoBean>) {
        this.urlList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SeachCarsHodler {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        val seachCarsHodler = SeachCarsHodler(inflate)
        seachCarsHodler.itemView?.setOnClickListener { mOnSeachCarItemOnClickListener?.onClickListener(seachCarsHodler.adapterPosition) }
        return seachCarsHodler
    }

    override fun getItemCount(): Int {
        return if (urlList != null) { urlList!!.size }else{0}
    }

    override fun onBindViewHolder(holder: SeachCarsHodler, pos: Int) {
        val bike_info = urlList!!.get(pos).getData()!!.bind_info!![0].bike_info
//        holder.macTx?.text = setTextSpanneds(R.string.cars_model_name_tx, bike_info!!.getModel()!!.model_name!!)
        holder.imageView?.glide(bike_info?.getPic_b(), false, R.drawable.car_ic, false)
        val carNumTx = setTextSpanneds(R.string.cars_vin_tx, bike_info?.getVin()!!)
        holder.carNumTx?.text = carNumTx
        val macTx = setTextSpanneds(R.string.cars_model_name_tx, bike_info!!.getModel()!!.model_name!!)
        holder.macTx?.text = macTx
        adjustTvTextSize(holder.carNumTx,holder.carNumTx!!.maxWidth!!,textSpanned = carNumTx)
        adjustTvTextSize(holder.macTx,holder.macTx!!.maxWidth!!,textSpanned = macTx)

        if(pos > urlList!!.size-1){
            holder.line?.visibility = View.GONE
        }
    }



    interface onSeachCarItemOnClickListener{
        fun onClickListener(pos:Int)
    }

    class SeachCarsHodler(v:View): RecyclerView.ViewHolder(v){
        var imageView: ImageView? = null
        var macTx: TextView? = null
        var carNumTx: TextView? = null
        var line:View? = null

        init {
            line = v.findViewById(R.id.item_car_line)
            imageView = v.findViewById(R.id.image)
            macTx = v.findViewById(R.id.item_mac_txt)
            carNumTx = v.findViewById(R.id.item_car_num_txt)
        }

    }
}