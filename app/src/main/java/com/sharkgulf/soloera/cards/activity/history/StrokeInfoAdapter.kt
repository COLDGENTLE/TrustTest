package com.sharkgulf.soloera.cards.activity.history

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.baseSetOnclickListener
import com.trust.demo.basis.trust.TrustTools
import kotlin.collections.ArrayList

/**
 *  Created by user on 2019/2/14
 */
class StrokeInfoAdapter(contexts:Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var inflater: LayoutInflater? = LayoutInflater.from(contexts)
    private var mList = arrayListOf<StrokeInfoAndMalfunctionBean>()
    private var mEmptyView:Int? = null
    private var mNoMoreView:Int? = null
    private var mStroketInfoListener:StroketInfoListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (mList.isEmpty()) {
            -1
        }else{
            if (position == mList.size) {
                999999
            }else{
                mList[position].type
            }
        }

    }

    fun setStroketInfoListener(listener:StroketInfoListener){
        mStroketInfoListener = listener
    }

    fun setList(list :ArrayList<StrokeInfoAndMalfunctionBean>){
        this.mList = list

        mList!!.sortByDescending {
            it.ts
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val hodler = when (p1) {
            //行程item
            ITEM_TYPE_STROKE -> {
                val strokeHodler = StrokeInfoViewHodler(inflater!!.inflate(R.layout.item_stroke, parent, false))
                baseSetOnclickListener( strokeHodler.itemView,listener = object :View.OnClickListener{
                    override fun onClick(v: View?) {
                        mStroketInfoListener?.onStroketInfoListener(mList[strokeHodler.adapterPosition])
                    }
                })
                strokeHodler
            }
            //故障item
            ITEM_TYPE_MALFUNCTION->{
                val malfunction = MalfunctionViewHodler(inflater!!.inflate(R.layout.item_malfunction, parent, false))
                malfunction
            }

            -1 -> {
                EmptyViewHolder(inflater!!.inflate(mEmptyView!!, parent, false))
            }
            999999 -> {
                EmptyViewHolder(inflater!!.inflate(mNoMoreView!!, parent, false))
            }
            else -> {
                val malfunction = MalfunctionViewHodler(inflater!!.inflate(R.layout.item_malfunction, parent, false))
                malfunction
            }
        }
        return hodler
    }

    override fun getItemCount(): Int {
        return if (mList.isEmpty()) {
            1
        }else{
            mList.size+1
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (holder) {
            is  StrokeInfoViewHodler-> {
                val ridesBean = mList[pos].ridesBean

                holder.timeTx?.text = TrustTools.getTimes(TrustTools.getTimes(ridesBean!!.begin_time)) + "~" +  TrustTools.getTimes(TrustTools.getTimes(ridesBean!!.end_time))
                holder.startTx?.text = ridesBean?.begin_pos
                holder.endTx?.text = ridesBean?.end_pos
                if (ridesBean != null) {
                    holder.miles?.text = setTextSpanneds(R.string.item_driven_distance_tx, getMileageDouble(ridesBean.miles))
                    holder.times?.text = setTextSpanneds(R.string.item_riding_time_tx, getHMS(ridesBean.times))
                }
                holder.avg_speed?.text = setTextSpanneds(R.string.item_avg_speed_tx, getSpeed(ridesBean!!.avg_speed))
            }
            is MalfunctionViewHodler->{ }
        }
    }



    inner class StrokeInfoViewHodler(v:View): RecyclerView.ViewHolder(v){
        var timeTx:TextView? = null
        var startTx:TextView? = null
        var endTx:TextView? = null
        var miles:TextView? = null
        var avg_speed:TextView? = null

        var times:TextView? = null
        init {
            timeTx = v.findViewById(R.id.item_stroke_time_tx)
            startTx = v.findViewById(R.id.item_stroke_start_tx)
            endTx = v.findViewById(R.id.item_stroke_end_tx)
            miles = v.findViewById(R.id.item_storke_miles_tx)
            avg_speed = v.findViewById(R.id.item_storke_avg_spee_tx)
            times = v.findViewById(R.id.item_storke_time_tx)

        }
    }
    inner class MalfunctionViewHodler(v:View): RecyclerView.ViewHolder(v){
        var timeTx:TextView? = null
        var msgTx:TextView? = null
        var viewLine:View? = null
        var endViewLine:View? = null
        init {
            timeTx = v.findViewById(R.id.item_malfunction_time_tx)
            msgTx = v.findViewById(R.id.item_malfunction_msg_tx)
            viewLine = v.findViewById(R.id.item_malfunction_view_layout)
            endViewLine = v.findViewById(R.id.item_malfunction_end_layout)
        }
    }

     class StrokeInfoAndMalfunctionBean(
                                        var ridesBean: BsRideReportBean.DataBean.RidesBean?,
                                        var type:Int = 0 ,var ts :Long = 0L)

    class EmptyViewHolder(v:View): RecyclerView.ViewHolder(v)

    fun setEmptyView(layoutId:Int){
        mEmptyView = layoutId
    }



    fun setNoMoreView(layoutId: Int){
        mNoMoreView = layoutId
    }


    interface StroketInfoListener{
        fun onStroketInfoListener(bean:StrokeInfoAndMalfunctionBean)
    }
}