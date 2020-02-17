package com.sharkgulf.soloera.home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.trust.demo.basis.base.baseSetOnclickListener
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2018/12/18
 */
class MainRecyclerAdapter (contexts: Context?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var inflater: LayoutInflater? = LayoutInflater.from(contexts)
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mList:ArrayList<Int>? = null
    private val TAG = "MainRecyclerAdapter"
    override fun getItemCount(): Int {
        return  if(mList != null){
            mList!!.size
        }else{
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(mList == null) 0 else mList!![position]
    }

    fun updateList(){
        mList = mainRvControlOrder
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TrustLogUtils.d("mainRvControlOrder size ${mainRvControlOrder.size}")
        val viewHolder = when (viewType) {
            //地图卡片
            BS_MAP_CARD -> {
                var i = -1
                for(x in 0 until  parent.childCount){
                    if(parent.getChildAt(x).tag != null && parent.getChildAt(x).tag == "mapcard"){
                        i = x
                    }
                }
                val view = if (i !=-1) {
//                    parent.removeViewAt(i)
                    parent.getChildAt(i)
                }else{
                    inflater!!.inflate(R.layout.item_home_map_card, parent, false)
                }

                view.tag = "mapcard"
                val cardViewHolder = CardViewHolder(view)

                baseSetOnclickListener(cardViewHolder.itemView.findViewById<View>(R.id.item_map_layout),listener = object :View.OnClickListener{
                    override fun onClick(v: View?) {
                        TrustLogUtils.d(TAG,"recyclerView Map onClickListener")
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener!!.onItemClickListener(mList!![cardViewHolder.adapterPosition])
                        }
                    }

                })

                val tag = parent.getTag(1)
                val s = if (tag != null) {
                    parent.getTag(R.id.tag_first)
                }else{
                    parent.setTag(R.id.tag_first,cardViewHolder)
                    cardViewHolder
                }

                s as CardViewHolder
            }

            //车辆状态卡片
            BS_CAR_CARD->{
                onclick(CardViewHolder(inflater!!.inflate(R.layout.item_home_car_card,parent,false)))
            }

            //车辆电池卡片
            BS_BATTERY_CARD->{
                onclick(CardViewHolder(inflater!!.inflate(R.layout.item_home_battery_card,parent,false)))
            }

            //车辆控制卡片
            BS_CONTROL_CARD->{
//                onclick( CardViewHolder(inflater!!.inflate(R.layout.item_home_control_card,parent,false)))
                onclick( CardViewHolder(inflater!!.inflate(R.layout.item_home_control_card,parent,false)))
            }

            //车辆状态卡片
            BS_CARD_INTERNET_STATUS_CARD->{
                onclick(CardViewHolder(inflater!!.inflate(R.layout.item_home_car_internet_card,parent,false)))
            }

            //车辆广告卡片
            BS_AD_CARD->{
                onclick(CardAdViewHolder(inflater!!.inflate(R.layout.item_home_ad_card,parent,false)))
            }

            //用车报告卡片
            BS_CAR_HISTORY_CARD->{
                onclick(CardAdViewHolder(inflater!!.inflate(R.layout.item_home_car__hirstory_card,parent,false)))
            }

            //车辆云平台注册卡片
            BS_CAR_REGIST_SECTION_BLE_CARD->{
                CardAdViewHolder(inflater!!.inflate(R.layout.item_home_section_card,parent,false))
            }

            //车辆应急卡片
            BS_EMERGENCY_BATTERY_CARD -> {
               CardAdViewHolder(inflater!!.inflate(R.layout.item_home_emergency_battery_card,parent,false))
            }

            //安防设置
            BS_SECURITY_SETTINGS_CARD -> {
                onclick(CardAdViewHolder(inflater!!.inflate(R.layout.item_home_securuty_settings_card,parent,false)))
            }
            else -> {
                onclick( CardAdViewHolder(inflater!!.inflate(R.layout.item_home_ad_card,parent,false)))
            }
        }


        return  viewHolder

    }

    fun onclick(viewHolder: RecyclerView.ViewHolder): RecyclerView.ViewHolder {
        baseSetOnclickListener(viewHolder.itemView,listener = View.OnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClickListener(mList!![viewHolder.adapterPosition])
            }
        })
        return viewHolder
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

    }


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class CardAdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 增加数据
     */
    fun addData(position: Int,typeCard:Int) {
        if (mList != null) {
            val indexOf = mList!!.indexOf(typeCard)
            if ( indexOf == -1 && mList!!.isNotEmpty()) {
                mList!!.add(position,typeCard)
//            mainRvControlOrder = mList
//            notifyItemInserted(position)
                notifyDataSetChanged()
                TrustLogUtils.d(TAG,"刷新item")
            }
        }
    }

    /**
     * 移除数据
     */
    fun removeData(typeCard: Int) {
        val indexOf = mList?.indexOf(typeCard)
        if (indexOf != null) {
            if (indexOf != -1) {
                mList!!.remove(typeCard)
//            mainRvControlOrder = mList
//            notifyItemRemoved(indexOf)
                notifyDataSetChanged()
            }
        }
    }

    fun setOnItemClickListener(onClickItemLiserner: OnItemClickListener){
        mOnItemClickListener = onClickItemLiserner
    }

    interface OnItemClickListener{ fun onItemClickListener(cardType:Int)}
}