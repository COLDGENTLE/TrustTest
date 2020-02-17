package com.sharkgulf.soloera.home.user.cars

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean

/**
 *  Created by user on 2019/2/27
 */
class CarsAdapter(val mItemOnClikListener:ItemOnClikListener?= null) : RecyclerView.Adapter<CarsAdapter.UserCarsViewHolder>() {
    private var mList: BsGetCarInfoBean? = null

    fun setList(list:BsGetCarInfoBean?){
        mList = list
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CarsAdapter.UserCarsViewHolder {
        val holder = UserCarsViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_user_cars,p0,false))
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            mItemOnClikListener?.onClikListener(mList?.getData()?.bikes!![adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int {
        return if(mList?.getData() == null) 0 else mList?.getData()?.bikes!!.size
    }

    override fun onBindViewHolder(hodler: CarsAdapter.UserCarsViewHolder, pos: Int) {
        val bikes = mList?.getData()?.bikes!!
        hodler.carVersion?.text = bikes[pos].vin
        hodler.carName?.text = bikes[pos].bike_name
        hodler.bindTime?.text = bikes[pos].bind_days.toString()
//        hodler.mileage?.text = bikes[pos].completion.toString()
    }


    class UserCarsViewHolder(v:View): RecyclerView.ViewHolder(v){
        var carVersion:TextView? = null
        var carName:TextView? = null
        var carIc:ImageView? = null
        var bindTime:TextView? = null
        var mileage:TextView? = null
        init {
            carVersion = v.findViewById(R.id.item_car_version_tx)
            carName = v.findViewById(R.id.item_car_name_tx)
            carIc = v.findViewById(R.id.item_car_ic_img)
            bindTime = v.findViewById(R.id.item_car_bind_time_tx)
            mileage = v.findViewById(R.id.item_car_mileage_tx)
        }
    }

    interface ItemOnClikListener{
        fun onClikListener(bean: BsGetCarInfoBean.DataBean.BikesBean)
    }
}