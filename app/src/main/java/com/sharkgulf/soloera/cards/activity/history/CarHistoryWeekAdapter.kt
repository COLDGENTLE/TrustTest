package com.sharkgulf.soloera.cards.activity.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.util.Calendar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.BsHistoryItemWeelBean
import com.trust.demo.basis.trust.TrustTools
import java.text.SimpleDateFormat
import java.util.*

/**
 *  Created by user on 2019/3/22
 */
class CarHistoryWeekAdapter : RecyclerView.Adapter<CarHistoryWeekAdapter.WeekViewHolder>() {
    private var mList = arrayListOf<BsHistoryItemWeelBean>()
    private var mButton:ArrayList<TextView> = arrayListOf()
    private var weekList = arrayListOf<String>()
    private var mWeekAdapterListener:WeekAdapterListener? = null


    fun setWeekAdapterListener(listener:WeekAdapterListener){
        mWeekAdapterListener = listener
    }
    /**
     * week
     */
    private var today = ""
    private var dayStr = ""
    private var thisWeek  = false
    @SuppressLint("NewApi")
      fun setTime (time:Long,day:String){
        weekList = arrayListOf<String>()
        mList.clear()
        var calendatTime:Long = 0
        val calendar = Calendar.getInstance(Locale.CHINA) //获取China区Calendar实例，实际是GregorianCalendar的一个实例
        calendar.timeInMillis = time
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 获取本周一的日期
        val timeInMillis = calendar.timeInMillis

        val df = SimpleDateFormat("MM/dd")
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        today = df1.format(System.currentTimeMillis())
        dayStr = day
        for (i in 0..6){
            calendatTime = timeInMillis + i* TrustAppConfig.DAY_TIME
            df.format(calendatTime)
            mList.add(BsHistoryItemWeelBean(calendatTime, TrustAppConfig.weeks[i],df.format(calendatTime)))
//            if (today == df1.format(calendatTime)) {
//                thisWeek = true
//                today = TrustAppConfig.weeks[i]
//            }
            val format = df1.format(calendatTime)
            if(dayStr ==  format){
                thisWeek = true
                dayStr = TrustAppConfig.weeks[i]
            }
            weekList.add(format)

        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WeekViewHolder {
        val width = parent.measuredWidth / itemCount
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_week_layout, parent, false)
        val params = itemView.layoutParams
        if (params != null) {
            params.width = width
            itemView.layoutParams = params
        }
        val weekViewHolder = WeekViewHolder(itemView)
        weekViewHolder.mButton?.setOnClickListener {
            it as TextView
            setView(it,Color.parseColor("#ffffff"),R.drawable.red_bg,weekViewHolder.adapterPosition)
        }
        return weekViewHolder
    }

    private fun setView(v:TextView,textColor:Int,background:Int,pos:Int){
        if (mButton.isEmpty()) {
            mButton.add(v)
        }else{
            mButton.forEach {
                it.setBackgroundResource(R.drawable.history_week_radio_bg)
                it.setTextColor(Color.parseColor("#ffffff"))
            }
            mButton.clear()
            mButton.add(v)
        }
        v.setBackgroundResource(background)
        v.setTextColor(textColor)
        mWeekAdapterListener?.weekListener(TrustTools.getTime(weekList[pos]),weekList[pos])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(hodler: WeekViewHolder, pos: Int) {
        hodler.mButton?.text = "${mList[pos].week} \n ${mList[pos].date}"
        if (thisWeek) {
            if (dayStr==mList[pos].week) {
                setView(hodler.mButton!!,Color.parseColor("#ffffff"),R.drawable.red_bg,pos)
            }
        }else{
            if (pos==0) {
                setView(hodler.mButton!!,Color.parseColor("#ffffff"),R.drawable.red_bg,pos)
            }
        }
    }
    class WeekViewHolder(v:View): RecyclerView.ViewHolder(v){
        var mButton:TextView? = null
        init {
            mButton = v.findViewById(R.id.item_week_btn)
        }
    }

    interface WeekAdapterListener{
        fun weekListener(data:Long,dataStr:String)
    }
}