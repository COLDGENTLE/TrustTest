package com.sharkgulf.soloera.presenter.battery

import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.battery.BatteryMode
import com.sharkgulf.soloera.module.battery.IBatteryModeListener
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.battery.IBatteryView
import com.sharkgulf.soloera.tool.config.getBattryInfoData
import com.sharkgulf.soloera.tool.config.registerBattryInfo
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters

/**
 *  Created by user on 2019/3/5
 */
class BatteryPresenter :TrustPresenters<IBatteryView>(),IBatteryPresenterListener{
    private var mIBatteryModeListener: IBatteryModeListener? = null
    private var TAG  = ""
    init {
        mIBatteryModeListener = BatteryMode()
    }

    override fun setTAG(tag: String) {
        TAG = tag
        registerBattryStatus()
    }


    fun getBattery(){
        val battryInfoData = getBattryInfoData()
        var battry1: BattInfoBean.BodyBean.BattBean? = null
        var battry2: BattInfoBean.BodyBean.BattBean? = null

        battryInfoData?.batt?.forEach {
            if (it.info.position == TrustAppConfig.BATTERY_ONE && battry1 == null) {
                battry1 = it
            }else{
                battry2 = it
            }
        }
        view.resultBattryInfo(TrustAppConfig.isDouble,battry1,battry2,battryInfoData?.emer_batt)

    }

    private fun registerBattryStatus(){
        val listener = object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getBattery()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }

        registerBattryInfo(listener,TAG)
    }


    override fun requestBatteryInfo(map: HashMap<String, Any>?) {
        mIBatteryModeListener?.requestBatteryInfo(map!!,object : ModuleResultInterface<BsBatteryInfoBean>{
            override fun resultData(bean: BsBatteryInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultBatteryInfo(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }
}