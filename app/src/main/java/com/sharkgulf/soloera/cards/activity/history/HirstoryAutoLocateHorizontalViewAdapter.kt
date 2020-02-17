package com.sharkgulf.soloera.cards.activity.history

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.cards.activity.AutoLocateHorizontalView

/**
 *  Created by user on 2019/1/22
 */
class HirstoryAutoLocateHorizontalViewAdapter : RecyclerView.Adapter<HirstoryAutoLocateHorizontalViewAdapter.HirstoryViewHodle>() , AutoLocateHorizontalView.IAutoLocateHorizontalView {
    private var mSources: ArrayList<Int>? = null
    private var context: Context? = null
    private var maxValue = -1
    private val maxHeight = 400
    private var v:View? = null
    override fun getItemView(): View? {
        return v
    }

    override fun onViewSelected(isSelected: Boolean, pos: Int, holder: RecyclerView.ViewHolder?, itemWidth: Int) {
        val holder1 = holder as HirstoryViewHodle
        val params = holder1.view!!.layoutParams
        params.height = (mSources!![pos] * 1f / maxValue * maxHeight).toInt()
        params.width = 5 * itemWidth / 6
        holder1.itemView.layoutParams = params
        if (isSelected) {
            holder1.view?.setBackgroundColor(Color.parseColor("#b71f16"))
//            holder1.tvAge.setTextColor(Color.parseColor("#A0000000"))
        } else {
            holder1.view?.setBackgroundColor(Color.parseColor("#2e2d30"))
//            holder1.tvAge.setTextColor(Color.parseColor("#30000000"))
        }
    }

    private var mList:ArrayList<Int> = arrayListOf()
    fun setList(context:Context,list:ArrayList<Int>){
        mList = list
        this.mSources = list
        this.context = context
        for (item in list) {
            if (item > maxValue) {
                maxValue = item
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HirstoryViewHodle {
        v = LayoutInflater.from(p0.context).inflate(R.layout.item_hirstory,p0,false)
        val hirstoryViewHodle = HirstoryViewHodle(v!!)
        hirstoryViewHodle.itemView.setOnClickListener {
        }
        return hirstoryViewHodle
    }

    override fun getItemCount(): Int {
        return mSources!!.size
    }

    override fun onBindViewHolder(p0: HirstoryViewHodle, p1: Int) {
//        p0.tx?.text = mList[ if (p1 % 2 === 1) 1 else 0]
        p0.tx?.text = " 我是$p1"
    }


    class HirstoryViewHodle(v:View) : RecyclerView.ViewHolder(v){
        var tx:TextView? = null
        var view:View? = null
        init {
            tx = v.findViewById(R.id.item_hirstory_tx)
            view = v.findViewById(R.id.item_hirstory_view)
        }
    }




}