package com.sharkgulf.soloera.cards.activity.alert

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mAuthentication
import com.sharkgulf.soloera.cards.activity.history.RideTrackActivity
import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.home.cars.CarsAdapter
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.mvpview.message.MessagerView
import com.sharkgulf.soloera.presenter.messager.MessagerPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.TextDrawable
import com.sharkgulf.soloera.tool.view.datepicker.CustomDatePicker
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_alert_list.*
import kotlinx.android.synthetic.main.tite_layout.*
import razerdp.basepopup.BasePopupWindow
import java.sql.Date

class AlertListActivity : TrustMVPActivtiy<MessagerView,MessagerPresenter>(),MessagerView {
    private var showDialog: DialogFragment? = null
    private val carsAdapter = CarsAdapter()
    private var mCustomDatePicker : CustomDatePicker? = null
    private var mTime:Long = System.currentTimeMillis()
    private var mAlertAdapter:AlertAdapter? = null
    private var mDay:String = TrustTools.getTime(System.currentTimeMillis())
    private var mToday:String = TrustTools.getTime(System.currentTimeMillis())
    private var mToadayLong:Long = System.currentTimeMillis()
    private val TAG = AlertListActivity::class.java.canonicalName
    private var popupGravity: BasePopupWindow? = null
    private var mTodayTime = System.currentTimeMillis()
    override fun getLayoutId(): Int { return R.layout.activity_alert_list }
    private var mBikeId = bikeId
    private var mShowDay:String = getTime(System.currentTimeMillis())
    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(alert_list_calendar_btn)
        baseSetOnClick(title_back_btn)
//        baseSetOnClick(title_spinner)
        baseSetOnClick(alert_list_yesterday_btn,500)
        baseSetOnClick(alert_list_tomorrow_btn,500)

        val lp = LinearLayoutManager(this)
        lp.orientation = LinearLayout.VERTICAL
        mAlertAdapter = AlertAdapter(this)
        mAlertAdapter?.setListener(adapterListener)
        mAlertAdapter!!.setEmptyView(R.layout.item_empty_view_layout)
        alert_list_rv.adapter = mAlertAdapter
        alert_list_rv.layoutManager = lp
        title_tx.text = getString(R.string.alertlist_title_tx)

        changeDay(System.currentTimeMillis() + 10)

        requestList(mBikeId)
        val userCarList = mAuthentication.getUserCarList()
        if (userCarList != null) {
            carsAdapter.setList(userCarList)
            val intExtra = intent.getIntExtra(CHOOSE_POS_KEY, -1)
            if (intExtra != -1) {
                carsAdapter.changeItem(intExtra)
            }

        }

        carsAdapter.setListener(object : CarsAdapter.CarsAdapterItemListener{
            override fun startaCarInfo(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {}

            override fun delCars(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {}

            override fun toAddCars() {}

            override fun chooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
                popupGravity?.dismiss()
                mBikeId = bsCarsBean.bike_id
                requestList(mBikeId)
            }
        })
    }

    private fun requestList(bikeId:Int) {
        if (USE_TYPE == USE_DEMO) {
            getPresenter()?.requestAlertList(RequestConfig.alertList(demoBikeList, mToday))
        }else{
            val userCarList = getAuthentication().getUserCarList()
            if (userCarList != null) {
                val bidList = IntArray(userCarList.bikes.size)
                userCarList.bikes.forEachIndexed { index, bikesBean ->
                    bidList[index] = bikesBean.bike_id
                }
                getPresenter()?.requestAlertList(RequestConfig.alertList(bidList, mToday))
            }
        }
    }

    override fun initData() {
        initTimerPicker()
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.alert_list_calendar_btn -> {
                showPop()
            }
            R.id.title_spinner -> {
                popupGravity?.dismiss()
                popupGravity = TrustBasePopuwindow.getPopuwindow(mContext!!, R.layout.popu_cars_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
                    override fun resultPopuwindowViewListener(v: View?): View {
                        val carsRecycle = v!!.findViewById<RecyclerView>(R.id.pupo_recycler)
                        val lp = LinearLayoutManager(mActivity)
                        lp.orientation = LinearLayout.VERTICAL
                        carsRecycle.layoutManager = lp
                        carsRecycle.adapter = carsAdapter
                        return v
                    }
                }).setPopupGravity(Gravity.BOTTOM).setBackgroundColor(Color.TRANSPARENT)
                val sas = popupGravity as TrustBasePopuwindow
                popupGravity?.showAnimator = sas.createAnimator(true)
                popupGravity?.dismissAnimator = sas.createAnimator(false)
                popupGravity?.showPopupWindow(title_spinner)
            }
            R.id.title_back_btn -> {
                finish()
            }

            R.id.alert_list_yesterday_btn -> {
                if (check()) {
                    mTodayTime -= DAY_TIME
                    mShowDay = getTime(mTodayTime)
                    mDay = TrustTools.getTime(mTodayTime)
                    changeDay(mTodayTime)
                    requestAlertList(mDay)
                }

            }

            R.id.alert_list_tomorrow_btn -> {
                if (check()) {
                    mTodayTime += DAY_TIME
                    mDay = TrustTools.getTime(mTodayTime)
                    mShowDay = getTime(mTodayTime)
                    changeDay(mTodayTime)
                    requestAlertList(mDay)
                }
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) { showDialog = TrustDialog().showDialog(supportFragmentManager) } }

    override fun diassDialog() {
        mActivity!!.runOnUiThread {
            showDialog?.dismiss()
        }
    }

    override fun showToast(msg: String?) { baseShowToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { baseShowToast(msg) }

    override fun resultAlertList(bean: BsAlertBean?) {
        if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
            if (mDay == mToday) {
                getPresenter()?.getAlertList(RequestConfig.getAlertList(mDay, DEFULT,true))
                dataAnalyCenter().sendLocalData(APP_MESSAGER_READ,true.toString())
            }else{
                mAlertAdapter!!.setAlertList(bean.data.list)
            }
//            changeDay()
//            mAlertAdapter!!.setAlertList(bean.data.list)
        }
    }

    override fun createPresenter(): MessagerPresenter { return MessagerPresenter() }


    private fun requestAlertList(day:String){
        mDay = day
        if (USE_TYPE == USE_DEMO) {
            getPresenter()?.requestAlertList(RequestConfig.alertList(demoBikeList, mToday))
        }else{
            val userCarList = getAuthentication().getUserCarList()
            if (userCarList != null) {
                val bidList = IntArray(userCarList.bikes.size)
                userCarList.bikes.forEachIndexed { index, bikesBean -> bidList[index] = bikesBean.bike_id }
                getPresenter()?.requestAlertList(RequestConfig.alertList(bidList, day))
            }
        }

    }

    override fun resultBsAlertList(bean: DbAlertBean?) {
        if (bean != null) {
            mAlertAdapter!!.setAlertList(bean.alerBean!!.data.list)
        }else{
            mAlertAdapter!!.setAlertList(arrayListOf())
        }
    }

    private fun showPop(){
        mCustomDatePicker?.show("yyyy-MM-dd")
        mCustomDatePicker?.setSelectedTime(mTime,true)
    }


    private fun initTimerPicker() {
        val beginTime = "1970-01-01 18:00:00"
//        val endTime = DateFormatUtils.long2Str(mTime, true)
        val endTime = "3019-01-01 00:00"
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mCustomDatePicker = CustomDatePicker(this, CustomDatePicker.Callback { timestamp ->
            val time = TrustTools.getTime(Date(timestamp), "yyyy-MM-dd")
            checkIsEffective(timestamp, time)
        }, beginTime, endTime)
        // 允许点击屏幕或物理返回键关闭
        mCustomDatePicker?.setCancelable(true)
        // 显示时和分
        mCustomDatePicker?.setCanShowPreciseTime(false)
        // 允许循环滚动
        mCustomDatePicker?.setScrollLoop(true)
        // 允许滚动动画
        mCustomDatePicker?.setCanShowAnim(true)
    }

    private fun checkIsEffective(timestamp: Long, time: String) {
        val currentTimeMillis = System.currentTimeMillis()
        if(currentTimeMillis < timestamp &&  timestamp> maxBindTime){
//            changeDay(timestamp)
            showToast(getBsString(R.string.is_exceed_bike_data_time_tx))
        }else if (maxBindTime  > timestamp && timestamp< currentTimeMillis){
            showToast(getBsString(R.string.is_exceed_bike_data_time_tx))
        }
        else{
            mShowDay = getTime(timestamp)
            mTodayTime = timestamp
            requestAlertList(time)
            changeDay(timestamp)
        }

    }

    private fun changeDay(time:Long){
        alert_list_submint_btn.text =  if(TrustTools.getTime(time) == TrustTools.getTime(maxBindTime)){
            changeBtnStatus(alert_list_tomorrow_btn,true)
            changeBtnStatus(alert_list_yesterday_btn,false)
            mShowDay
            }else if (time >= mToadayLong || mToday == mDay) {
            changeBtnStatus(alert_list_tomorrow_btn,false)
            changeBtnStatus(alert_list_yesterday_btn,true)
            "$mShowDay ${getString(R.string.alertlist_today_tx)}"
        }else if(time <= mToadayLong && time >= maxBindTime){
            changeBtnStatus(alert_list_tomorrow_btn,true)
            changeBtnStatus(alert_list_yesterday_btn,true)
            mShowDay
        }else if(time <= maxBindTime){
            changeBtnStatus(alert_list_tomorrow_btn,true)
            changeBtnStatus(alert_list_yesterday_btn,false)
            mShowDay
        }else{
            mShowDay
        }
        TrustLogUtils.d(TAG,"TrustTools.getTime(time) ：${TrustTools.getTime(time)}")
        TrustLogUtils.d(TAG,"TrustTools.getTime(maxBindTime) ：${TrustTools.getTime(maxBindTime)}")
        TrustLogUtils.d(TAG,"TrustTools.getTime(maxBindTime) ：$maxBindTime")
    }


    private fun changeBtnStatus(v: TextDrawable,isClick:Boolean){
        v.isClickable = isClick
        val color = if (isClick) { R.color.colorBlack }else{ R.color.gray6d }
        v.setDrawableRightColor(getBsColor(color))
        v.setDrawableLeftColor(getBsColor(color))
        v.setTextColor(getBsColor(color))
    }



    private val adapterListener = object : AlertAdapter.AlertOnclickListener{
        override fun onClickListener(event: Int, url: String?,exts:String?) {
            if (event == ALERT_MOVE){
                val intent  = Intent(this@AlertListActivity,RideTrackActivity::class.java)
                val bean = getExtsBean(exts)
                intent.putExtra(ALERT_KEY,bean)
                startActivity(intent)
            }else{
                val alertInfoBean = alertInfoMap[event]
                if (alertInfoBean != null) {
                    val alertIntent = Intent(this@AlertListActivity,BsFragmentActivity::class.java)
//                if (alertIntent != null) {
                    if (url != null && url != "") {
                        alertIntent.putExtra(ALERT_KEY,url)
                        startActivity(alertIntent)
                    }
//                }
                }
            }
        }
    }

    fun requestList(){
        requestList(bikeId)
    }

    private fun check():Boolean{
        if (TrustHttpUtils.getSingleton(this).isNetworkAvailable()) { return true }
        showToast(getString(R.string.http_error_tx))
        return false
    }

    private fun getTime(time:Long):String{
        return TrustTools.getTime(time,"MM月dd日")
    }
}
