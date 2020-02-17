package com.sharkgulf.soloera.home.cars

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.tool.config.*

/*
 *Created by Trust on 2019/1/10
 * 车库adapter
 */
class CarsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mList:ArrayList<CarsBean> = arrayListOf()
    private var mListener:CarsAdapterItemListener? = null
    private var mChoosePos:Int = -1
    private var mFirst = true
    private var mIsUserBikeList = true
    private val TAG = "CarsAdapter"
    fun getLister():Int{
        return if (mList != null) {
            mList!!.size
        }else{
            0
        }
    }

    fun clearList(){
        mList?.clear()
        notifyDataSetChanged()
    }

    fun setList(list: BsGetCarInfoBean.DataBean?){
        mFirst = true
        mChoosePos = -1
        mList = arrayListOf()
        list?.bikes?.forEach {
            mList!!.add(CarsBean(it))
        }

        val chooseBidPos =  if (isDemoStatus()) {
            null
        }else{
            getAuthentication().getChooseBidPos(false,list?.bikes)
        }

        if (chooseBidPos != null && chooseBidPos != DEFULT) {
            mChoosePos = chooseBidPos
            mListener?.chooseCar(mList!![mChoosePos].bikes)
        }else if (mList!!.size>0 && mFirst){
            mFirst = false
            mListener?.chooseCar(mList!![0].bikes)
        }


//        mList!!.sortBy {
//            it.bikes.bind_days
//        }

    }
    fun setListener(carsAdapterItemListener:CarsAdapterItemListener){
        mListener = carsAdapterItemListener
    }


    fun setAdapterType(isUserBikeList:Boolean){
        mIsUserBikeList = isUserBikeList
    }

    private val ITEM_BODY = 0
    private val ITEM_ADD = 1

    override fun getItemViewType(position: Int): Int {
        return  if(position >=0 &&  mList == null){
            ITEM_ADD
        }else if(position >=0 && position <= mList!!.size -1){
            ITEM_BODY
        }
        else{
            ITEM_ADD
        }

    }

    @SuppressLint("CheckResult")
    override fun onCreateViewHolder(p0: ViewGroup, type: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        viewHolder = if (type == ITEM_BODY) {
             val carHolder = if (mIsUserBikeList) {
                 CarHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_home_cars,p0,false))
            }else{
                 ChooseCarHodler(LayoutInflater.from(p0.context).inflate(R.layout.item_home_choose_bike_layout,p0,false))
             }

            if (!mIsUserBikeList) {
                carHolder.itemView.setOnClickListener {
                    val position = carHolder.adapterPosition
                    changeItem(position)
                }
            }else{
                carHolder.itemView.setOnClickListener {
                    mListener?.startaCarInfo(mList!![carHolder.adapterPosition].bikes)
                }
            }
            carHolder
        }else{
            val v =  LayoutInflater.from(p0.context).inflate(R.layout.item_home_cars_add_car,p0,false)
            v.findViewById<View>(R.id.item_add_cars_btn).setOnClickListener {  mListener?.toAddCars() }
            AddCarHodler(v)
        }
        return viewHolder!!
    }

    fun changeItem(position: Int) {
        if (mChoosePos != position) {
            if (mChoosePos != -1) {
                mList!![mChoosePos].status = false

            }
            mList!![position].status = true
            notifyItemChanged(mChoosePos)
            notifyItemChanged(position)
            mChoosePos = position
            mListener?.chooseCar(mList!![mChoosePos].bikes)
            getAuthentication().setChooseBid(bidPos = mList!![mChoosePos].bikes.bike_id)
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null && mList!!.size !=0) {
            if (mList!!.size == 5) {
                (mList!!.size)
            }else{
                (mList!!.size)
            }
        }else{
            1
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (mList.isNotEmpty()) {
            val carsBean = mList[pos]
            val bikes = carsBean?.bikes

            if (mChoosePos == pos) {
                carsBean.status = true
            }else if(mChoosePos == -1){
                if (pos==0) {
                    mChoosePos = 0
                    carsBean.status = true
                }
            }

            if (holder is CarHolder) {
                if (mIsUserBikeList) {
                    holder.mCarsNameTv?.text = bikes.bike_name
                     if (carsBean.status) {
                        holder.mChooseIm?.visibility = View.VISIBLE
                        holder.mCarsNameTv?.alpha = 1f
                         holder.mLayout?.alpha = 0.1f
                    }else{
                        holder.mChooseIm?.visibility = View.GONE
                        holder.mCarsNameTv?.alpha = 0.5f
                         holder.mLayout?.alpha = 0f
                    }

                }else{ carsBean.bikes.pic_side }

            }else if (holder is ChooseCarHodler){
                holder.mBikeName?.text = bikes.bike_name
                holder.mBikeVin?.text = setTextSpanneds(R.string.cars_vin_tx, bikes?.getVin()!!)
                holder.mBikeModul?.text = setTextSpanneds(R.string.cars_vin_tx, bikes?.getModel()!!.model_name!!)
                holder.mBikeIc?.glide(bikes?.pic_side, false, R.drawable.car_ic, false)

                adjustTvTextSize(holder.mBikeName,holder.mBikeName!!.maxWidth!!,bikes.bike_name!!)
                adjustTvTextSize(holder.mBikeVin,holder.mBikeVin!!.maxWidth!!,bikes.getVin()!!)
                adjustTvTextSize(holder.mBikeModul,holder.mBikeModul!!.maxWidth!!,bikes.getModel()!!.model_name!!)

                val icId = if (carsBean.status) {
                    holder.mLayoutBg?.setBackgroundResource(R.drawable.white_bg_blue_line_bg)
                    R.drawable.choose_bike_choose_ic
                }else{
                    holder.mLayoutBg?.setBackgroundResource(R.drawable.white_bg_white_line_bg)
                    R.drawable.choose_bike_no_choose_ic
                }

                holder.mBikeSelect?.setImageResource(icId)
            }
        }
    }


    class CarHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mCarsNameTv:TextView? = null
        var mChooseIm:TextView? = null
        var mLayout: View? = null
        init {
            mCarsNameTv = itemView.findViewById(R.id.item_cars_name)
            mChooseIm = itemView.findViewById(R.id.item_cars_choose_im)
            mLayout = itemView.findViewById(R.id.home_cars_layout)
        }
    }

    class AddCarHodler(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ChooseCarHodler(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mBikeName:TextView? = null
        var mBikeVin:TextView? = null
        var mBikeModul:TextView? = null
        var mBikeIc:ImageView? = null
        var mBikeSelect:ImageView? = null
        var mLayoutBg:View? = null
        init {
            mBikeName = itemView.findViewById(R.id.item_home_choose_bike_name_tv)
            mBikeVin = itemView.findViewById(R.id.item_home_choose_bike_vin_tv)
            mBikeModul = itemView.findViewById(R.id.item_home_choose_bike_modul_tv)
            mBikeIc = itemView.findViewById(R.id.item_home_choose_bike_bike_ic)
            mBikeSelect = itemView.findViewById(R.id.item_home_choose_bike_select_im)
            mLayoutBg = itemView.findViewById(R.id.item_home_choose_bike_layout_bg)

        }
    }

    interface CarsAdapterItemListener{
        fun toAddCars()
        fun chooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean)
        fun delCars(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean)
        fun startaCarInfo(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean)
    }

    class CarsBean(var bikes: BsGetCarInfoBean.DataBean.BikesBean,var status:Boolean = false)

    fun getItemPos():Int{ return mChoosePos }
}