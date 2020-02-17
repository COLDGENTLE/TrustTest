package com.sharkgulf.soloera.home.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.home.fragment.FragmentHomeEmergencyBatteryCard.Companion.changeUiListener
import com.sharkgulf.soloera.main.FragmentMyCar.Companion.mainRecyclerAdapter
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.battery.IBatteryView
import com.sharkgulf.soloera.presenter.battery.BatteryPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.registerUpdateCarInfo
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.card_battery_double.*
import kotlinx.android.synthetic.main.card_battery_only.*
import kotlinx.android.synthetic.main.fragment_home_battery_card.*
import kotlin.concurrent.thread

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeBatteryCard : TrustMVPFragment<IBatteryView,BatteryPresenter>(),IBatteryView {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private var mBsBatteryInfoBean:BattInfoBean.BodyBean? = null
    private val TAG = "FragmentHomeBatteryCard"
    private val REGIST_TOPIC = WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_POWER
    private val SEND_TOPIC = WEB_SOKECT_SEND+WEB_SOKECT_CAR_POWER
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_battery_card
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        DataAnalysisCenter.getInstance().registerCallBack(REGIST_TOPIC,object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String,timeOutTopic:String?) {}

            override fun onNoticeCallBack(msg: String?) {
                getData()
            }
        },TAG)

        registerUpdateCarInfo()
    }

    override fun initData() {
        DataAnalysisCenter.getInstance().sendData(SEND_TOPIC)
        thread {
            Thread.sleep(3000)
            getData()
        }
    }

    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): BatteryPresenter{
        return BatteryPresenter()
    }

    override fun resultBatteryInfo(bean: BsBatteryInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            mActivity?.runOnUiThread {
                changeUi()
            }
        }
    }

    @Synchronized
    private fun changeUi(){
        var batt1:BattInfoBean.BodyBean.BattBean? = null
        var batt2:BattInfoBean.BodyBean.BattBean? = null
        mBsBatteryInfoBean?.batt?.forEach {
            if (it.info.position == BATTERY_ONE && batt1 == null) {
                batt1 = it
            }else{
                batt2 = it
            }
        }
        hideOrShowLayout(batt1, batt2)
    }

    private fun hideOrShowLayout(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?) {

        val isOne = !(batt1 == null || batt1.status!!.in_use == 0)
        val isTwo = !(batt2 == null || batt2.status!!.in_use == 0)
//        val isOne = false
//        val isTwo = false
        var mileEs = 0
        if (isOne && isTwo) {//双电池
            changBatteryUi(false,false)
            isShowOnlyLayout(false)
            mileEs = setDoubleLayout(batt1!!,batt2!!)
        }else if (!isOne && !isTwo){//无电池
            changBatteryUi(true,true)
        }else{
            //单电池
            changBatteryUi(false,false)
            isShowOnlyLayout(true)
            mileEs = if (isOne) {
                setOnlyLayout(batt1!!)
            }else if (isTwo){
                setOnlyLayout(batt2!!)
            }else{
                0
            }

        }

        card_battery_recharge_mileage_tv?.text = mileEs.toString()
    }


    private fun changeBatteryNum(v: ImageView?, num:Int?,textView: TextView?){
        var textColor = ""
        val capacity :Int= if(num in 0..10){
            textColor = COLOR_READ
            1
        }
        else if(num in 11..20){
            textColor = COLOR_READ
            2
        }
        else if(num in 21..30){
            textColor = COLOR_GREEN
            3
        }

        else if(num in 31..40){
            textColor = COLOR_GREEN
            4
        }
        else if(num in 41..50){
            textColor = COLOR_GREEN
            5
        }
        else if(num in 51..60){
            textColor = COLOR_GREEN
            6
        }
        else if(num in 61..70){
            textColor = COLOR_GREEN
            7
        }
        else if(num in 71..80){
            textColor = COLOR_GREEN
            8
        }
        else if(num in 81..90){
            textColor = COLOR_GREEN
            9
        }
        else if(num in 91..100){
            textColor = COLOR_GREEN
            10
        }else{
            textColor = COLOR_GREEN
            10
        }
        v?.setImageResource(getBatteryIc(capacity))
        textView?.setTextColor(Color.parseColor(textColor))
    }



    /**
     * 动态显示应急电池 及电池未使用
     */
    private fun changBatteryUi(isShowEmer: Boolean = false,isShowNothingBattery:Boolean){
        TrustLogUtils.d(TAG,"isShowEmer: $isShowEmer   isShowNothingBattery : $isShowNothingBattery  carIsOk: $carIsOk")
        if (!isShowNothingBattery) {
            card_battery_layout?.visibility = View.VISIBLE
            include_battery_nothing_layout?.visibility = View.GONE
        }else{
            card_battery_layout?.visibility = View.GONE
            include_battery_nothing_layout?.visibility = View.VISIBLE
        }

        if (isShowEmer) {
            if (carIsOk) {
                FragmentHomeEmergencyBatteryCard.mBean = mBsBatteryInfoBean?.emer_batt
                TrustLogUtils.d(TAG,"changeUiListener : ${changeUiListener == null}")
                mainRecyclerAdapter?.addData(1, BS_EMERGENCY_BATTERY_CARD)
                changeUiListener?.changeUiListener()
            }
        }else{
            mainRecyclerAdapter?.removeData(BS_EMERGENCY_BATTERY_CARD)
        }
        TrustLogUtils.d(TAG,"!(isShowEmer && isShowNothingBattery) : ${!(isShowEmer && isShowNothingBattery)}")
        isPowerOk = !(isShowEmer && isShowNothingBattery)
    }


    private fun getData(){
        mActivity!!.runOnUiThread {
            val data = DataAnalysisCenter.getInstance().getData<BattInfoBean>("${WEB_SOKECT_RECEIVE}${WEB_SOKECT_CAR_POWER}")
            mBsBatteryInfoBean = data?.body
            if (mBsBatteryInfoBean != null && !mBsBatteryInfoBean?.batt!!.isEmpty()) {
                changeUi()
            }else{
                changBatteryUi(true,true)
            }
        }
    }

    private fun isShowOnlyLayout(isOnlyLayout:Boolean){
        card_battery_only_layout?.visibility = if(isOnlyLayout) View.VISIBLE else View.GONE
        card_battery_double_layout?.visibility = if(!isOnlyLayout) View.VISIBLE else View.GONE
    }

    private fun setDoubleLayout(batt1: BattInfoBean.BodyBean.BattBean,batt2: BattInfoBean.BodyBean.BattBean):Int{

        card_double_ba1_capacity_txt.text = setTextSpanneds(R.string.battery_num_tx,batt1.status.capacity.toString(),15)
        changeTmep(batt1.status.temp_ind,card_double_ba1_tem_txt)
        card_double_ba1_tem_txt.text = setTextSpanneds(R.string.battery_temperature_tx,batt1.status.temp.toString(),15)
        changeBatteryNum(card_double_ba1_capacity_img, batt1.status.capacity,card_double_ba1_capacity_txt)


        card_double_ba2_capacity_txt.text = setTextSpanneds(R.string.battery_num_tx,batt2.status.capacity.toString(),15)
        card_double_ba2_tem_txt.text = setTextSpanneds(R.string.battery_temperature_tx,batt2.status.temp.toString(),15)
        changeTmep(batt2.status.temp_ind,card_double_ba2_tem_txt)
        changeBatteryNum(card_double_ba2_capacity_img, batt2.status.capacity,card_double_ba2_capacity_txt)
        card_double_ba1_charge_img.visibility = if(batt1.status.charge ==
                BATTERY_CHARGE_STATUS_OPEN) View.VISIBLE else View.INVISIBLE
        card_double_ba2_charge_img.visibility = if(batt2.status.charge ==
                BATTERY_CHARGE_STATUS_OPEN) View.VISIBLE else View.INVISIBLE
        return batt1.status.mile_es + batt2.status.mile_es
    }

    private fun setOnlyLayout(batt: BattInfoBean.BodyBean.BattBean):Int{
        card_only_ba_capacity_txt?.text = setTextSpanneds(R.string.battery_num_tx,batt.status.capacity.toString(),15)
        card_only_ba_tem_txt?.text = setTextSpanneds(R.string.battery_temperature_tx,batt.status.temp.toString(),15)
        changeTmep(batt.status.temp_ind,card_only_ba_tem_txt)
        changeBatteryNum(card_only_ba_capacity_img, batt.status.capacity,card_only_ba_capacity_txt)
        ic_battery_first_img?.setImageResource(if(batt.info.position == BATTERY_ONE)
            R.drawable.ic_battery_first_normal else R.drawable.ic_battery_second_normal)
        ic_battery_first_img?.visibility = if (isDouble) View.VISIBLE else View.INVISIBLE
        card_only_ba_in_use_img?.visibility =
                if(batt.status.charge == BATTERY_CHARGE_STATUS_OPEN)View.VISIBLE else View.INVISIBLE
        return batt.status.mile_es
    }

    override fun onDestroy() {
        DataAnalysisCenter.getInstance().unRegisterCallBack(REGIST_TOPIC,TAG)
        super.onDestroy()
    }


    private fun registerUpdateCarInfo(){
        registerUpdateCarInfo(TAG, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                if (msg != null) {
                    DataAnalysisCenter.getInstance().sendData(SEND_TOPIC)
                    getData()
                }
            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }


    private fun changeTmep(status:Int,v:TextView?){
        val textColor = when (status) {
            BATTRY_ADD_LOW_TEMP,BATTRY_PUT_LOW_TEMP,BATTRY_ADD_HIGH_TEMP,BATTRY_PUT_HIGH_TEMP -> {
                COLOR_READ
            }
            else -> {
                COLOR_GREEN
            }
        }
        v?.setTextColor(Color.parseColor(textColor))
    }
}