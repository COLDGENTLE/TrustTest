package com.sharkgulf.soloera.cards.activity.history

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.util.Calendar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.cards.activity.BarAdapter
import com.sharkgulf.soloera.tool.config.setTextStrings
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 *  Created by user on 2019/4/10
 */
class CarHistoryMonthAdapter : RecyclerView.Adapter<CarHistoryMonthAdapter.MonthViewHolder>(){
    private val TAG = "CarHistoryMonthAdapter"
    private var mList = arrayListOf<ArrayList<MonthBean>>()
    private var mButton:ArrayList<ButtonBean> = arrayListOf()
    private var mMonthList = arrayListOf<MonthBean>()
    private var mMonthListerner:MonthListener? = null
    private var mIsclick = false
    private var mLastTimeOfDay :Long = 0

    fun setList(list: ArrayList<ArrayList<MonthBean>>){
        mList = list
    }

    fun setMonthListener(listener:MonthListener){
        mMonthListerner = listener
    }
    private var dayStr = 0L
    private var mTimes = 0L
    private var isDay = false
    private val LAST_MONTH = 0
    private val NEXT_MONTH = 1
    private val THE_MONTH = 2
    private var mBindTime:Long = 0
    private var mToday:String? = null
    @SuppressLint("NewApi")
    @Synchronized
    fun setTime(bindTime:Long,times:Long,day:String,beanList:ArrayList<BarAdapter.HistoryBean>?){
        mBindTime = bindTime
        mLastTimeOfDay = getLastTimeOfTheDay()
        mToday = TrustTools.getTime(mLastTimeOfDay)
        isDay = false
        mTimes = times
        mIsclick = false
        mList = arrayListOf()
        mMonthList.clear()
        val monthStart = getMonthStart(times)
        val monthEnd =  getMonthStart(times,false)
        val days = (monthEnd - monthStart) / TrustAppConfig.DAY_TIME + 1
        var time :Long
        dayStr = TrustTools.getTime(day)

        //获取本月每天的时间
        for(i in 0 until days){
            time = monthStart + TrustAppConfig.DAY_TIME *i
            val toInt = i.toInt()
            if (beanList != null && toInt<= beanList.size-1) {
                mMonthList.add(MonthBean(time,THE_MONTH, beanList[toInt]))
            }else{
                mMonthList.add(MonthBean(time,THE_MONTH,null))
            }
            if (dayStr == time) {
                isDay = true
            }
        }
        mMonthList[mMonthList.size-1].isLastDay = true
        time = monthStart
        //获取第一天星期
        val instance = Calendar.getInstance()
        instance.timeInMillis = monthStart
        val weekIndex = instance.get(Calendar.DAY_OF_WEEK) - 1

        //获取上个月的天数集合
        var beforTime :Long
        TrustLogUtils.d(TAG,"上月:weekIndex $weekIndex")
        val beforList = arrayListOf<MonthBean>()
        for (i in 1 until weekIndex) {
            beforTime =  time - TrustAppConfig.DAY_TIME *i
            beforList.add(MonthBean(beforTime,LAST_MONTH,null))
            TrustLogUtils.d(TAG,"上个月日期: ${TrustTools.getTime(beforTime)}  type:$LAST_MONTH")
        }
        beforList.reverse()

        mMonthList.addAll(0,beforList)

        //获取下个月的天数集合
        var afterTime :Long
        val afterList = arrayListOf<MonthBean>()
        for (i in 1 .. 42 - mMonthList.size) {
            afterTime =  monthEnd + TrustAppConfig.DAY_TIME *i
            afterList.add(MonthBean(afterTime,NEXT_MONTH,null))
            TrustLogUtils.d(TAG,"下个月日期: ${TrustTools.getTime(afterTime)}  type:$NEXT_MONTH")
        }
        mMonthList.addAll(afterList)

        mList = arrayListOf()
        var list = arrayListOf<MonthBean>()
        var i = 0
        for (l in 0 until mMonthList.size) {

            list.add(mMonthList[l])
            if(i == 6 ){
                i = 0
                mList.add(list)
                list = arrayListOf()

            }else{
                i++
            }
        }


        mList.forEach {
            TrustLogUtils.d(TAG,"月总和 ${Gson().toJson(it)}")
        }
        notifyDataSetChanged()
    }
    /**
     * 获取指定月份的第一天或最后一天时间
     * @return
     */
    @SuppressLint("NewApi")
    fun getMonthStart(time:Long,isFirstDay:Boolean = true):Long{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.set(Calendar.DATE, 1)
        if (!isFirstDay) {
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.DATE, -1)
        }
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0)
        //将秒至0
        calendar.set(Calendar.SECOND,0)
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis

    }



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_month_layout, parent, false)
        val monthViewHolder = MonthViewHolder(itemView)
        val mOnClickListener = View.OnClickListener {
            it as TextView
            TrustLogUtils.d("点击月 ${monthViewHolder.adapterPosition}")
            checkPos(it,monthViewHolder.adapterPosition)
        }

        monthViewHolder.m1?.setOnClickListener(mOnClickListener)
        monthViewHolder.m2?.setOnClickListener(mOnClickListener)
        monthViewHolder.m3?.setOnClickListener(mOnClickListener)
        monthViewHolder.m4?.setOnClickListener(mOnClickListener)
        monthViewHolder.m5?.setOnClickListener(mOnClickListener)
        monthViewHolder.m6?.setOnClickListener(mOnClickListener)
        monthViewHolder.m7?.setOnClickListener(mOnClickListener)

        return monthViewHolder
    }

    private fun checkPos(it: TextView, pos: Int) {
        val arrayList = mList[pos]
        val time = when (it.id) {
            R.id.item_month_1 -> {
                arrayList[0]
            }
            R.id.item_month_2 -> {
                arrayList[1]
            }
            R.id.item_month_3 -> {
                arrayList[2]
            }
            R.id.item_month_4 -> {
                arrayList[3]
            }
            R.id.item_month_5 -> {
                arrayList[4]
            }
            R.id.item_month_6 -> {
                arrayList[5]
            }
            R.id.item_month_7 -> {
                arrayList[6]
            }
            else->{
                null
            }
        }
        if (time != null) {
            if (time.type == THE_MONTH) {
                    setView(it,Color.parseColor("#ffffff"),R.drawable.red_bg,pos)
                    mMonthListerner?.monthListener(time.time,TrustTools.getTime(time.time))
//                }
            }else{
            }
        }

    }


    private fun setView(v:TextView,textColor:Int,background:Int,pos: Int){

        if (mButton.isEmpty()) {
            mButton.add(ButtonBean(v,pos))
        }else{
            mButton.forEachIndexed { _, it ->
                val viewPos = returnTextViewPos(it.v)
                if(it.pos < mList.size){

                val monthBean = mList[it.pos][viewPos]
                if(monthBean != null) {
                    val bean = monthBean.bean
                    if (bean != null) {
                        if (bean.height ==0) {
                            it.v.setTextColor(Color.parseColor(graryC4))
                        }else {
                            it.v.setTextColor(Color.parseColor(black))
                        }

                    }else {
                        it.v.setTextColor(Color.parseColor(graryC4))
                    }
                    it.v.setBackgroundResource(R.drawable.history_week_radio_bg)
                }else {//空数据
                    it.v.setTextColor(Color.parseColor(graryC4))
                    it.v.setBackgroundResource(R.drawable.history_week_radio_bg)
                }

                }

            }
            mButton.clear()
            mButton.add(ButtonBean(v,pos))
        }
        v.setBackgroundResource(background)
        v.setTextColor(textColor)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MonthViewHolder, pos: Int) {
        val arrayList = mList[pos]
        if (pos ==0) {
            holder.mTitle?.visibility = View.VISIBLE
        }

        val dayList = arrayListOf<DayBean>()
        val botton = arrayListOf( holder.m1!!,
                holder.m2!!, holder.m3!!, holder.m4!!, holder.m5!!, holder.m6!!, holder.m7!!)
        changeUi(arrayList, botton,dayList,pos)
        botton.forEachIndexed { index, textView ->
            textView.text = dayList[index].day
        }

    }

    val black = "#ffffff"
    val graryC4 = "#ffffff"
    private fun changeUi(arrayList: ArrayList<MonthBean>, botton: ArrayList<TextView> ,dayList:ArrayList<DayBean> ,pos :Int):ArrayList<DayBean> {
        arrayList.forEachIndexed { index, monthBean ->
            if (monthBean.type == THE_MONTH) {
                val bean = monthBean.bean
                if (bean != null) {
                    monthBean.isClick = mBindTime <= monthBean.time
                    monthBean.isEffective = bean.height != 0
                } else {
                    monthBean.isClick = !(mLastTimeOfDay < monthBean.time || mBindTime > monthBean.time)
                }
                monthBean.isToday = mToday.equals(TrustTools.getTime(monthBean.time))

            }

            val str = if (monthBean.isToday) {
                setView(botton[index],R.color.colorWhiteGay,R.drawable.hirstory_week_bg,index)
                mMonthListerner?.monthListener(monthBean.time,TrustTools.getTime(monthBean.time))
                setTextStrings(R.string.car_history_today_tx)
            }else{
                TrustTools.getTime(Date(monthBean.time),"d")
            }
            dayList.add(DayBean(str,monthBean.type,monthBean.time))

            botton[index].isClickable = monthBean.isClick
            botton[index].setTextColor(Color.parseColor(if(monthBean.isEffective) black else graryC4))

            if(monthBean.isLastDay && !isDay){
                setView(botton[index],R.color.colorWhiteGay,R.drawable.red_bg,pos)
                mMonthListerner?.monthListener(monthBean.time,TrustTools.getTime(monthBean.time))
            }
        }
        return dayList
    }


    class MonthViewHolder(v: View): RecyclerView.ViewHolder(v){
        var m1: TextView? = null
        var m2: TextView? = null
        var m3: TextView? = null
        var m4: TextView? = null
        var m5: TextView? = null
        var m6: TextView? = null
        var m7: TextView? = null
        var mTitle: LinearLayout? = null
        init {
            m1 = v.findViewById(R.id.item_month_1)
            m2 = v.findViewById(R.id.item_month_2)
            m3 = v.findViewById(R.id.item_month_3)
            m4 = v.findViewById(R.id.item_month_4)
            m5 = v.findViewById(R.id.item_month_5)
            m6 = v.findViewById(R.id.item_month_6)
            m7 = v.findViewById(R.id.item_month_7)
            mTitle = v.findViewById(R.id.item_month_title_layout)
        }
    }


    interface MonthListener{
        fun monthListener(data:Long,dataStr:String)
    }


    class MonthBean(var time:Long,var type:Int,var bean: BarAdapter.HistoryBean?,var isToday:Boolean = false,var isEffective:Boolean = false,var isClick:Boolean = false,var isLastDay:Boolean = false)
    class DayBean(var day:String,type:Int,var time:Long)
    class ButtonBean(var v:TextView,var pos:Int)
    private fun returnTextViewPos(v:View):Int{
        return when (v.id) {
            R.id.item_month_1 -> {
                 0
            }
            R.id.item_month_2 -> {
                 1
            }
            R.id.item_month_3 -> {
                 2
            }
            R.id.item_month_4 -> {
                 3
            }
            R.id.item_month_5 -> {
                 4
            }
            R.id.item_month_6 -> {
                 5
            }
            R.id.item_month_7 -> {
                 6
            }else ->{
            -1
            }
        }

        }


    private fun getLastTimeOfTheDay():Long{
        val current = System.currentTimeMillis()
        val zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().rawOffset +TrustAppConfig.DAY_TIME -1
        return zero
    }



    }


