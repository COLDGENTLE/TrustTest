package com.sharkgulf.soloera.home.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.battery.IBatteryView
import com.sharkgulf.soloera.presenter.battery.BatteryPresenter
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_emergency_battery.*

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeEmergencyBatteryCard : TrustMVPFragment<IBatteryView,BatteryPresenter>(),IBatteryView {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun onTrustFragmentFirstVisible() {
    }

    companion object {
        var changeUiListener :ChangeUiListener? = null
        var mBean: BattInfoBean.BodyBean.EmerBattBean? = null
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_emergency_battery
    }



    private val TAG = FragmentHomeEmergencyBatteryCard::class.java.canonicalName
    @SuppressLint("NewApi")
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        changeUiListener = mChangeUiListener
        changeUi()
    }


    @SuppressLint("NewApi")
    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
    }

    override fun createPresenter(): BatteryPresenter{
        return BatteryPresenter()
    }

    override fun resultBatteryInfo(bean: BsBatteryInfoBean?) {

    }

    interface ChangeUiListener{
        fun changeUiListener()
    }

    private val mChangeUiListener = object :ChangeUiListener{
        @SuppressLint("NewApi")
        override fun changeUiListener() {
            changeUi()
        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun changeUi(){
        TrustLogUtils.d(TAG,"mBean : $mBean")
        val days = mBean?.left_days
        fragment_emergency_msg_tx?.text = setTextSpanneds(R.string.emergency_tx, days?.toString()
                ?: "0","")
        fragment_emergency_time_tx?.text = mBean?.time_desc
    }



}