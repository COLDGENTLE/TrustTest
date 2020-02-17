package com.sharkgulf.soloera.cards.activity.history

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_car_history.*
import kotlinx.android.synthetic.main.tite_layout.*
import com.sharkgulf.soloera.cards.activity.AutoLocateHorizontalView
import com.sharkgulf.soloera.cards.activity.BarAdapter
import com.sharkgulf.soloera.cards.activity.BarAdapter.*
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.history.HirstoryView
import com.sharkgulf.soloera.presenter.history.HirstoryPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackLineChart
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.util.*
import kotlin.collections.ArrayList


class CarHistoryActivity : TrustMVPActivtiy<HirstoryView, HirstoryPresenters>(),HirstoryView  {
    override fun resultTrackLineData(list: List<TrackLineChart.Data>) {}

    override fun resultCarinfo(totalMiles: Int, bindDays: Int, maxMils: Int) {
        hirstory_mileage_tv.text = getMileageDouble(totalMiles)
        hirstory_speed_tv.text = getMileageDouble(maxMils)
        hirstory_time_tv.text = bindDays.toString()
    }

    override fun resultBattUseNum(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?) {}

    override fun resultAddressList(addressList: ArrayList<String>?) {}


    private val TAG = CarHistoryActivity::class.java.canonicalName
    private var mHirstoryAdapter: BarAdapter? = null
    private val mWeekAdapter = CarHistoryWeekAdapter()
    private val mMonthAdapter = CarHistoryMonthAdapter()
    private var mStrokeInfoAdapter:StrokeInfoAdapter? = null
    private var mTime = 0L //选中的day 的时间
    private var mWeek = "" //选中那天对应的日周月日期
    private var mMonth = "" //选中那天对应的日周月日期
    private var mDay = "" //选中那天对应的日周月日期
    private var mBindTime:Long = 0L
    private var mBindDay:String? = null
    override fun getLayoutId(): Int { return R.layout.activity_car_history }

    override fun initView(savedInstanceState: Bundle?) {
        DATA_TYPE = DATA_TYPE_DAY
        mTime = System.currentTimeMillis()
        baseSetOnClick(title_back_btn)
        baseSetOnClick(car_history_choose_btn)
        setTextStrings(title_tx, R.string.car_history_title_tx)

        mStrokeInfoAdapter = StrokeInfoAdapter(this)
        mStrokeInfoAdapter?.setEmptyView(R.layout.item_list_empty_view_layout)
        mStrokeInfoAdapter?.setNoMoreView(R.layout.item_list_no_data_layout)
        mStrokeInfoAdapter?.setStroketInfoListener(mStroketInfoListener)


        hirstory_recycler_info.adapter = mStrokeInfoAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        hirstory_recycler_info.layoutManager = layoutManager

        mMonthAdapter.setMonthListener(mMonthListener)
        history_tablayout.getTabAt(0)!!.select()

        history_tablayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(p0: TabLayout.Tab?) { changeRecyclerType(p0!!.position) }

        })
    }
    @SuppressLint("NewApi")
    override fun initData() {
        val lllls = LinearLayoutManager(this)
        lllls.orientation = LinearLayoutManager.VERTICAL
        car_history_month_recycler.layoutManager = lllls
        car_history_month_recycler.adapter = mMonthAdapter

        mWeekAdapter.setWeekAdapterListener(mWeekListener)
        car_history_week_recycler.adapter = mWeekAdapter
        val llllsss = LinearLayoutManager(this)
        llllsss.orientation = LinearLayoutManager.HORIZONTAL
        car_history_week_recycler.layoutManager = llllsss

        changeUi()
        val carinfoBean = getCarInfoData()
        mBindTime = bindTime
        if (carinfoBean != null) {
            mBindTime = TrustTools.getTime(carinfoBean.binded_time)
        }
        mBindDay = TrustTools.getTime(mBindTime)
        requestTimeLevel()
        getPresenter()?.getHirstoryData()
    }

    fun requestTimeLevel() {
        TrustLogUtils.d(TAG,"requestTimeLevel : $bindTime")
        if (TrustHttpUtils.getSingleton(this).isNetworkAvailable()) {
            if (mBindTime != 0L) {
                getPresenter()?.requestTimeLevel(RequestConfig.requestTimeLevel(TrustTools.getTime(mBindTime),TrustTools.getTime(System.currentTimeMillis())))
            }
        }else{
            resultError(getString(R.string.http_error_tx))
        }
    }

    fun requestRideSummary() {
        if (mBindDay != null) {
        getPresenter()?.requestRideSummary(RequestConfig.requestRideSummary(checkData(mBindDay!!), checkData(TrustTools.getTime(System.currentTimeMillis()))))
        }
    }

    private fun checkData(chooseDay:String):String{

        val day = if (DATA_TYPE == DATA_TYPE_WEEK) {
            val find = weekList.find {
                it.dayStr ==  chooseDay
            }
            find?.weekStr
        }else if(DATA_TYPE == DATA_TYPE_MONTH){
            val find = dayList.find {
                it.dayStr ==  chooseDay
            }
            find?.mothStr
        }else{
            chooseDay
        }
        var timeDay = ""
        if (day == null) {
            timeDay = chooseDay
        }else{
            timeDay = day
        }
        return timeDay
    }

    fun requestRideReport(data:String?) {
        if (data != null) {
            getPresenter()?.requestRideReport(RequestConfig.requestRideReport(data))
        }
    }


    override fun baseResultOnClick(v: View) {
        when(v.id){
            R.id.car_history_choose_btn->{
                val popupGravity = TrustBasePopuwindow.getPopuwindow(this,R.layout.popupwindow_choose_history_type,object :TrustBasePopuwindow.TrustPopuwindowOnclickListener{
                    override fun resultPopuwindowViewListener(v: View?): View {
                        v!!.findViewById<View>(R.id.popu_only_warn_btn).setOnClickListener {
                            mStrokeInfoAdapter?.setList(mAlertList) }
                        v.findViewById<View>(R.id.popu_only_location_btn).setOnClickListener {
                            mStrokeInfoAdapter?.setList(mRideList)
                        }
                        v.findViewById<View>(R.id.popu_all_btn).setOnClickListener {
                            mStrokeInfoAdapter?.setList(mAllList)
                        }
                        return v
                    }
                }).setPopupGravity(Gravity.BOTTOM or Gravity.RIGHT).setBackgroundColor(Color.TRANSPARENT)
                val sas = popupGravity as TrustBasePopuwindow
                popupGravity.showAnimator = sas.createAnimator(true)
                popupGravity.dismissAnimator = sas.createAnimator(false)
                popupGravity.showPopupWindow(title_layout)

            }
            else ->{
                finish()
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        TrustLogUtils.d(TAG,"msg: $msg")
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        TrustLogUtils.d(TAG,"msg: $msg")
        baseShowToast(msg)
    }

    override fun createPresenter(): HirstoryPresenters {
        return HirstoryPresenters()
    }



    override fun resultTimeLevel(bean: BsTimeLevelBean?) {
        try {
            if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
                val time = bean.getData()?.time!!
                val beanList = arrayListOf<HistoryBean>()
                time.forEach {
//                    beanList.add(BarAdapter.HistoryBean(0,TrustTools.getTime(Date( TrustTools.getTime(it.display)),"dd"),null,it))
                    TrustLogUtils.d(TAG,"it.display : ${it.display}")
                    beanList.add(HistoryBean(0,it.display,null,
                            it,getDayStr(it.display!!)
                            ,getWeekStr(it.display!!),getMothStr(it.display!!)))
                }
                if (mHirstoryAdapter == null) {
                    mHirstoryAdapter = BarAdapter(this)
                    mHirstoryAdapter!!.setList(setList(beanList),test_height_layout.height)
                    changeMiles()
                    car_hirstory_recycler_view.setItemCount(7)
                    car_hirstory_recycler_view.setInitPos(beanList.size-1)
                    car_hirstory_recycler_view.adapter = mHirstoryAdapter
                    car_hirstory_recycler_view.y = 0F
                    car_hirstory_recycler_view.setOnSelectedPositionChangedListener (mOnSelectedPositionChangedListener)
                }else{
                    mHirstoryAdapter!!.setList(setList(beanList),test_height_layout.height)
                    changeMiles()
                }
                requestRideSummary()
            }
        }catch (e:Exception){
            TrustLogUtils.e(TAG,e)
        }

    }



    override fun resultRideSummary(bean: BsRideSummaryBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {

            try {
                val rideSummary = bean.getData()?.ride_summary

                if (rideSummary != null) {
                    val list = mHirstoryAdapter!!.list
                    val size = if(list.size > rideSummary.size){
                        rideSummary.size
                    }else if (list.size < rideSummary.size){
                        list.size
                    }else{
                        list.size
                    }
                    for (x in 0 until size){
                        list[x].height = rideSummary[x].miles
                        list[x].rideSummaryBean = rideSummary[x]
                    }
                    mHirstoryAdapter!!.setList(setList(list),test_height_layout.height)
                    changeMiles()
                }
            }catch (e:java.lang.Exception){
                TrustLogUtils.d(TAG,"error:${e.printStackTrace().toString()}")
            }

        }


    }

    override fun resultRideReport(bean: BsRideReportBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            filterData(bean)
            mRideList.forEach {
                val s= it.ridesBean
                TrustLogUtils.d(TAG,"${s?.begin_time}  ${s?.end_time} ${s?.avg_speed}   ${s?.max_speed}")
                 }

            if (mRideList.isEmpty()) {
                hirstory_list_empty_layout.visibility = View.VISIBLE
                hirstory_recycler_info.visibility = View.GONE
            }else{
                hirstory_list_empty_layout.visibility = View.GONE
                hirstory_recycler_info.visibility = View.VISIBLE
                mStrokeInfoAdapter?.setList(mRideList)
            }
        }
    }
    private var mAllList = arrayListOf<StrokeInfoAdapter.StrokeInfoAndMalfunctionBean>()
    private var mAlertList = arrayListOf<StrokeInfoAdapter.StrokeInfoAndMalfunctionBean>()
    private var mRideList = arrayListOf<StrokeInfoAdapter.StrokeInfoAndMalfunctionBean>()
    private fun filterData(bean: BsRideReportBean) {
        mAllList.clear()
        mAlertList.clear()
        mRideList.clear()
        val rides = bean.getData()?.rides
        rides?.forEach {
            mAllList.add(StrokeInfoAdapter.StrokeInfoAndMalfunctionBean( it, ITEM_TYPE_STROKE, it.ts))
            mRideList.add(StrokeInfoAdapter.StrokeInfoAndMalfunctionBean( it, ITEM_TYPE_STROKE, it.ts))
            it.max_speed
        }

        mAllList.sortWith(Comparator { o1, o2 -> o1!!.ts.compareTo(o2!!.ts) })
    }

    override fun resultRideTrack(bean: BsRideTrackBean?) {}


    @SuppressLint("NewApi")
    private val mOnSelectedPositionChangedListener = AutoLocateHorizontalView.OnSelectedPositionChangedListener { pos ->
        TrustLogUtils.d(TAG,"我是选中的 ： $pos")
        val list = mHirstoryAdapter!!.list
        if(pos <=list.size-1 && pos != -1){
            val historyBean = mHirstoryAdapter!!.list[pos]


            val rideSummaryBean = historyBean.rideSummaryBean


            car_history_max_speed_tx.text = "${rideSummaryBean.max_speed}km/h11111"
            car_history_drive_num_tx.text = "骑行${rideSummaryBean.ride_count}次"
            car_history_error_tx.text = "报警${rideSummaryBean.alert_count}次"


            mTime = TrustTools.getTime(historyBean.timeBean.begin_date)

            when (DATA_TYPE ) {
                DATA_TYPE_DAY ->{
                    if(historyBean.timeBean.begin_date != null){
                        mDay = historyBean.timeBean.begin_date!!
                    }
                    if (historyBean.timeBean.week != null) {
                        mWeek = historyBean.timeBean.week!!
                    }
                    if (historyBean.timeBean.month != null) {
                        mMonth = historyBean.timeBean.month!!
                    }

                    requestRideReport(historyBean.itemName)

                    changeUi(rideSummaryBean,historyBean.timeBean.display)
                    changeMiles(rideSummaryBean)
                }
                DATA_TYPE_WEEK -> {
                    mWeek = TrustTools.getTime(mTime)
                    mMonth = TrustTools.getTime(mTime)
                    mWeekAdapter.setTime(mTime,mDay)
                    changeMiles(rideSummaryBean)
                    changeUi(rideSummaryBean,historyBean.timeBean.display)
//                    changeUi(rideSummaryBean,historyBean.timeBean.display)
                }
                DATA_TYPE_MONTH -> {
                    mWeek = TrustTools.getTime(mTime)
                    mMonth = TrustTools.getTime(mTime)
                    TrustLogUtils.d(TAG,"月份的 日期 $mMonth")
                    changeMiles(rideSummaryBean)
                    changeUi(rideSummaryBean,historyBean.timeBean.display)
                    mMonthAdapter.setTime(mBindTime,mTime,mDay, dayMonthMap[mMonth])
                }
            }
        }
    }

    fun changeUi(rideSummaryBean: BsRideSummaryBean.DataBean.RideSummaryBean? = null,day:String? = "") {
//        hirstory_mileage_tv.text = setTextSpanneds(R.string.home_hirstory_tx,
//                rideSummaryBean?.miles?.toString() ?: getString(R.string.nothing_tx))

//        hirstory_speed_tv.text = setTextSpanneds(R.string.home_hirstory_speed_tx,
//                rideSummaryBean?.avg_speed?.toString() ?: getString(R.string.nothing_tx))

//        hirstory_time_tv.text = setTextSpanneds(R.string.home_hirstory_time_tx,
//                rideSummaryBean?.times?.toString() ?: getString(R.string.nothing_tx))

        car_history_max_speed_tx.text = setTextSpanneds(R.string.popu_top_speed_tx, rideSummaryBean?.max_speed?.toString()
                ?: getString(R.string.nothing_tx))
        car_history_drive_num_tx.text = setTextSpanneds(R.string.popu_riding_tx, rideSummaryBean?.ride_count?.toString()
                ?: getString(R.string.nothing_tx))
        car_history_error_tx.text = setTextSpanneds(R.string.popu_warn_num_tx, rideSummaryBean?.alert_count?.toString()
                ?: getString(R.string.nothing_tx))
        car_history_day_tx.text = day
        title_time_tx.text = day


        if (DATA_TYPE == DATA_TYPE_DAY) {
            hirstory_choose_day_tv.visibility = View.VISIBLE
            hirstory_choose_week_tv.visibility = View.INVISIBLE
            hirstory_choose_month_tv.visibility = View.INVISIBLE

        }else if(DATA_TYPE == DATA_TYPE_WEEK){
            hirstory_choose_day_tv.visibility = View.INVISIBLE
            hirstory_choose_week_tv.visibility = View.VISIBLE
            hirstory_choose_month_tv.visibility = View.INVISIBLE
        }else{
            hirstory_choose_day_tv.visibility = View.INVISIBLE
            hirstory_choose_week_tv.visibility = View.INVISIBLE
            hirstory_choose_month_tv.visibility = View.VISIBLE
        }
        hirstory_choose_day_tv.text = day
        hirstory_choose_week_tv.text = day
        hirstory_choose_month_tv.text = day
    }




    private fun setList(list:List<HistoryBean>):List<HistoryBean>{
        return when (DATA_TYPE) {
            DATA_TYPE_DAY -> {
                dayList = list
                var i = 0
                dayList.forEachIndexed { index, historyBean ->
                    if (historyBean.timeBean.begin_date.equals(mDay)) {
                        i = index
                    }
                    TrustLogUtils.d(TAG,"day 显示:${historyBean.dayStr}| ${historyBean.rideSummaryBean.miles}")
                }
                addDayMonthList()
                car_hirstory_recycler_view.setStartPos(i)
                dayList

            }
            DATA_TYPE_WEEK -> {
                TrustLogUtils.d(TAG,"周梳理数据源")
                weekList = list
                var i = 0
                weekList.forEachIndexed { index, historyBean ->
                    if (historyBean.timeBean.begin_date.equals(mWeek)) {
                        i = index
                    }
                    TrustLogUtils.d(TAG,"WEEK 显示:${historyBean.dayStr}| ${historyBean.rideSummaryBean.miles}")
                }
                car_hirstory_recycler_view.setStartPos(i)
                weekList
            }
            DATA_TYPE_MONTH -> {
                monthList = list
                var i = 0
                monthList.forEachIndexed { index, historyBean ->
                    if (historyBean.timeBean.begin_date.equals(mMonth)) {
                        i = index
                    }
                }
                car_hirstory_recycler_view.setStartPos(i)
                monthList
            }
            else -> {
                list
            }

        }

    }

    private fun addDayMonthList() {
        var list = arrayListOf<BarAdapter.HistoryBean>()
        var firstDay = ""
        dayList?.forEachIndexed { index, it ->
            TrustLogUtils.d(TAG,"索引  $index")
            if (firstDay == "") {
                firstDay = it.timeBean.month!!
                list.add(it)
            }else{
                if (firstDay != it.timeBean.month!!) {
                    if (dayMonthMap.get(firstDay) == null) {
                        dayMonthMap.set(firstDay,list)
                    }
                    list = arrayListOf()
                    firstDay = ""
                }
                    list.add(it)
            }

        }
        if (dayMonthMap.get(firstDay) == null) {
            dayMonthMap.set(firstDay,list)
        }
        dayMonthMap.forEach {
            TrustLogUtils.d(TAG,"我是map  key ${it.key}   value is null ${it.value == null}")
        }
        TrustLogUtils.d(TAG,"结束")
    }


    private val mWeekListener = object :CarHistoryWeekAdapter.WeekAdapterListener{
        override fun weekListener(data: Long, dataStr: String) {
            mDay = dataStr
            if (dayList != null) {
                dayList.forEach {
                    if (it.timeBean.display.equals(dataStr)) {
                        mMonth = it.timeBean.month!!
                    }
                }
                requestRideReport(dataStr)

                val bean = dayList.find {
                    it.timeBean.display.equals(mDay)
                }
//                changeUi(bean?.rideSummaryBean,bean?.timeBean?.display)
            }
        }
    }

    private val mMonthListener = object : CarHistoryMonthAdapter.MonthListener{
        override fun monthListener(data: Long, dataStr: String) {
            mDay = dataStr

            val bean = dayList.find {
                it.timeBean.display.equals(mDay)
            }
//            changeUi(bean?.rideSummaryBean,bean?.timeBean?.display)
            mWeekAdapter.setTime(mTime,mDay)
            dayList.forEach {
                if (it.timeBean.display.equals(dataStr)) {
                    mWeek = it.timeBean.week!!
                }
            }
            requestRideReport(dataStr)
        }

    }



    private val mStroketInfoListener = object : StrokeInfoAdapter.StroketInfoListener{
        override fun onStroketInfoListener(bean: StrokeInfoAdapter.StrokeInfoAndMalfunctionBean) {
            RideTrackActivity.routeStartActivity(bikeId,bean.ridesBean!!)
        }

    }
    override fun resultDrawTrajectory(status: Boolean) {}
    override fun resultMoveListener(isEnd: Boolean,pos:Int?) {}


    private fun changeRecyclerType(pos:Int){
        when (pos) {
            //日
            0 -> {
                mHirstoryAdapter?.setAdapterTypr(DAY)
                car_history_week_recycler.visibility = View.GONE
                car_history_month_recycler.visibility = View.GONE
                DATA_TYPE = DATA_TYPE_DAY
            }
            //周
            1 -> {
                mHirstoryAdapter?.setAdapterTypr(WEEK)
                mWeekAdapter.setTime(mTime,mDay)
                car_history_week_recycler.visibility = View.VISIBLE
                car_history_month_recycler.visibility = View.GONE
                DATA_TYPE = DATA_TYPE_WEEK
            }
            //月
            else -> {
                mHirstoryAdapter?.setAdapterTypr(MOTH)
                car_history_week_recycler.visibility = View.GONE
                car_history_month_recycler.visibility = View.VISIBLE
                DATA_TYPE = DATA_TYPE_MONTH
            }
        }
        requestTimeLevel()
    }


    private fun getDayStr(date:String):String{
        return TrustTools.getTime(Date( TrustTools.getTime(date)),"dd")
    }

    private fun getMothStr(date:String):String{
        return date.substring(5) + "月"
    }

    private fun getWeekStr(date:String):String{
//        2019/11/04~2019/11/10
        val split = date.split("~")
        var time = StringBuffer()
        split.forEach {
            time.append(TrustTools.getTime(Date( TrustTools.getTime(it,"yyyy/MM/dd")),"dd"))
            time.append("~")
        }
        time.deleteCharAt(time.length-1)
        return time.toString()
    }

    private fun changeMiles(bean: BsRideSummaryBean.DataBean.RideSummaryBean? = null){
        var maxValue = mHirstoryAdapter?.maxValue!!
        if (maxValue != null) {
            if (maxValue == 0) {
                history_max_mileage_tv.visibility = View.INVISIBLE
                history_half_mileage_tv.visibility = View.INVISIBLE
                history_item_line.visibility = View.INVISIBLE
                history_max_line.visibility = View.INVISIBLE
            }else{
                history_max_mileage_tv.visibility = View.VISIBLE
                history_half_mileage_tv.visibility = View.VISIBLE
                history_item_line.visibility = View.INVISIBLE
                history_max_line.visibility = View.VISIBLE
                TrustLogUtils.d(TAG,"最大的值:$maxValue")

                history_max_mileage_tv.text = "${Math.ceil(maxValue / 1000.0).toInt()}km"
                history_half_mileage_tv.text = "${Math.ceil(maxValue  /2.0 / 1000.0).toInt()}km"
            }
        }
        if (bean != null) {
            hirstory_choose_type_mileage_tv.text = getMileageDouble(bean.miles)
            hirstory_choose_type_max_speed_tv.text = getSpeed(bean.max_speed)
            hirstory_choose_type_average_speed_tv.text = getSpeed(bean.avg_speed)
            hirstory_choose_type_use_num_tv.text = bean.ride_count.toString()
        }
    }
}
