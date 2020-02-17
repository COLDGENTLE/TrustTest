package com.sharkgulf.soloera.home.fragment.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.DATA_TYPE_DAY
import com.sharkgulf.soloera.cards.SosServiceActivity
import com.sharkgulf.soloera.cards.activity.FindBsActivity
import com.sharkgulf.soloera.cards.activity.battery.BatteryActivity
import com.sharkgulf.soloera.cards.activity.history.CarHistoryActivity
import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.history.HirstoryView
import com.sharkgulf.soloera.presenter.history.HirstoryPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackLineChart
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home_car_hirstory_card.*

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeHistoryCard : TrustMVPFragment<HirstoryView, HirstoryPresenters>(), HirstoryView {
    override fun resultTrackLineData(list: List<TrackLineChart.Data>) {}


    override fun resultAddressList(addressList: ArrayList<String>?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private val TAG = FragmentHomeHistoryCard::class.java.canonicalName!!
    private var mCountdown: Disposable? = null
    override fun getLayoutId(): Int { return R.layout.fragment_home_car_hirstory_card }

    override fun initView(view: View?, savedInstanceState: Bundle?) {

        baseSetOnClick(home_mileage_btn)
        baseSetOnClick(home_battery_manger_btn)
        baseSetOnClick(home_find_bs_btn)
        baseSetOnClick(home_battery_sos_btn)

    }

    @SuppressLint("NewApi")
    override fun initData() {
        fragment_card_hirstory_mileage_tv.text =  setTextSpanneds(R.string.home_hirstory_tx,"0")
        fragment_card_hirstory_speed_tv.text =  setTextSpanneds(R.string.home_hirstory_speed_tx,"0")
        fragment_card_hirstory_time_tv.text =  setTextSpanneds(R.string.home_hirstory_time_tx,"0")
        updateFragmentUi()
        changeBatteryLayout(null,null)
//        fragment_card_hirstory_speed_tv.text = Html.fromHtml(getString(R.string.home_hirstory_speed_tx,"0"),0,null,SizeLabel(30))
//        fragment_card_hirstory_time_tv.text = Html.fromHtml(getString(R.string.home_hirstory_time_tx,"0"),0,null,SizeLabel(30))

    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.home_mileage_btn -> {
                startActivity(Intent(mActivity!!,CarHistoryActivity::class.java))
            }
            R.id.home_battery_manger_btn -> {

                    startActivity(Intent(mActivity!!,BatteryActivity::class.java))

            }
            R.id.home_find_bs_btn -> {
                startActivity(Intent(mActivity!!, FindBsActivity::class.java))
            }
            else -> {
                if (!isDemoStatus()) {
                    startActivity(Intent(mActivity!!, SosServiceActivity::class.java))
                }else{
                    showToast(getBsString(R.string.no_login_battery))
                }
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { showBsToast(msg)}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): HirstoryPresenters {
        val hirstoryPresenters = HirstoryPresenters()
        hirstoryPresenters.setTAG(TAG)
        return hirstoryPresenters
    }


    @SuppressLint("NewApi")
    private  fun changeUi(bean: BsRideSummaryBean.DataBean.RideSummaryBean?){
        if (bean != null) {
            fragment_card_hirstory_mileage_tv.text = Html.fromHtml(getString(R.string.home_hirstory_tx,bean.miles.toString()),0,null,SizeLabel(30))
            fragment_card_hirstory_speed_tv.text = Html.fromHtml(getString(R.string.home_hirstory_speed_tx,bean.avg_speed.toString()),0,null,SizeLabel(30))
            fragment_card_hirstory_time_tv.text = Html.fromHtml(getString(R.string.home_hirstory_time_tx,(bean.times/3600.0).toString()),0,null,SizeLabel(30))
        }
    }


    override fun resultTimeLevel(bean: BsTimeLevelBean?) {}

    override fun resultRideSummary(bean: BsRideSummaryBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            val rideSummaryBean = bean.getData()?.ride_summary?.get(0)
            changeUi(rideSummaryBean)
        }
    }

    override fun resultRideReport(bean: BsRideReportBean?) {}

    override fun resultRideTrack(bean: BsRideTrackBean?) {}


    fun forRequest(){
        mCountdown = TrustTools<View>().setCountdown(15) {
//            requestHirstory()
            forRequest()
        }
    }


    fun requestHirstory(){
        TrustAppConfig.DATA_TYPE = DATA_TYPE_DAY
        getPresenter()?.requestRideSummary(RequestConfig.requestRideSummary( TrustTools.getTime(System.currentTimeMillis()), TrustTools.getTime(System.currentTimeMillis())))
    }

    override fun onResume() {
        super.onResume()
//        requestHirstory()
//        forRequest()
    }

    override fun onPause() {
        super.onPause()
        mCountdown?.dispose()
    }

    override fun onDestroy() {
        mCountdown?.dispose()
        super.onDestroy()
    }

    override fun resultDrawTrajectory(status: Boolean) {}
    override fun resultMoveListener(isEnd: Boolean,pos:Int?) {}

    override fun resultBattUseNum(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?) {
        changeBatteryLayout(batt1,batt2) }

    override fun resultCarinfo(totalMiles: Int, bindDays: Int, maxMils: Int) {
        home_mileage_num_tv.text = getMileageDouble(totalMiles) + " km"
        home_bike_accompany_days_tv.text = getBsString(R.string.bike_accompany_days_tx,bindDays.toString())
    }


    private fun changeBatteryLayout(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?){
        if (batt1 == null && batt2 == null) {//都无
            showOnlyLayout(batt1,batt2)
        }else if(batt1 != null && batt2 != null){//都有
            val status1 = batt1.status
            val status2 = batt2.status
            when {
                status1.faults?.isEmpty() == status2.faults?.isEmpty() -> {//都无
                    if (status1.charge == TrustAppConfig.BATTERY_CHARGE_STATUS_OPEN && status2.charge == TrustAppConfig.BATTERY_CHARGE_STATUS_OPEN) { //都充电
                        showOnlyLayout(batt1,batt2)
                       }else if(status1.charge != TrustAppConfig.BATTERY_CHARGE_STATUS_OPEN && status2.charge != TrustAppConfig.BATTERY_CHARGE_STATUS_OPEN){ //都不充电
                        showOnlyLayout(batt1,batt2)
                       }else{//不同状态
                        showDoubleLayout(batt1,batt2)
                       }
                }
                status1.faults?.isNotEmpty() == status2.faults?.isNotEmpty() -> { //都有
                    showOnlyLayout(batt1,batt2) }
                else -> {//不同状态
                    showDoubleLayout(batt1,batt2) }
            }
        }else{//不同状态
            showDoubleLayout(batt1,batt2)
        }
    }


    private fun showOnlyLayout(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?){
        home_battery_double_status_layout.visibility = View.GONE
        home_battery_only_status_layout.visibility = View.VISIBLE

        changeBattryStatus(statusIm = home_battery_only_status_ic,bean = batt1,
                nothingIc = nothingIc, errorIc = errorIc,addPower = addPower,nomorl = nomorl,pos = 1,battinfoStr = home_battry_info_msg_tv,isShowTxInfo = false)
//        changeBattryStatus(statusIm = home_battery_only_status_ic,bean = batt2,
//                nothingIc = nothingIc, errorIc = errorIc,addPower = addPower,nomorl = nomorl,pos = 2,battinfoStr = home_battry_info_msg_tv,isShowTxInfo = false)
    }
    private val nothingIc = R.drawable.battery_nothing_bg
    private val errorIc = R.drawable.battery_nothing_bg
    private val addPower = R.drawable.battery_nothing_bg
    private val nomorl = R.drawable.battery_nothing_bg
    private fun showDoubleLayout(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?){
        home_battery_double_status_layout.visibility = View.VISIBLE
        home_battery_only_status_layout.visibility = View.GONE

        changeBattryStatus(statusIm = home_battery_double_status_batt1_ic,bean = batt1,
                nothingIc = nothingIc, errorIc = errorIc,addPower = addPower,nomorl = nomorl,pos = 1,battinfoStr = home_battry_info_msg_tv,isShowTxInfo = false)
//        changeBattryStatus(statusIm = home_battery_double_status_batt2_ic,bean = batt2,
//                nothingIc = nothingIc, errorIc = errorIc,addPower = addPower,nomorl = nomorl,pos = 2,battinfoStr = home_battry_info_msg_tv,isShowTxInfo = false)

    }



   fun updateFragmentUi() {
        getPresenter()?.getHirstoryData()
    }
}