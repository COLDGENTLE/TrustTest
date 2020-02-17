package com.sharkgulf.soloera.presenter.securitysettings

import android.view.View
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.module.securitysettings.ISecuritySettingsMoudle
import com.sharkgulf.soloera.module.securitysettings.ISecuritySettingsMoudleListener
import com.sharkgulf.soloera.mvpview.securitysettings.ISecuritySettingsView
import com.sharkgulf.soloera.tool.config.presenterGetCarInfo
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.TrustTools
import io.reactivex.disposables.Disposable

/**
 *  Created by user on 2019/8/19
 */
class ISecuritySettingsPresenters:TrustPresenters<ISecuritySettingsView>(),ISecuritySettingsPresentersListener{

    private var TAG = ""
    private var mListener : ISecuritySettingsMoudleListener? = null
    private var mHttpModel:HttpModel? = null
    init {
        mListener = ISecuritySettingsMoudle()
        mHttpModel = HttpModel()
    }


    fun setTAG(tag:String){
        TAG = tag
        registerBikeInfo()
    }

    private var mDisposable: Disposable? = null
    fun startFilter(){
        stop()
        mDisposable = TrustTools<View>().setCountdown(3) {
            view.resultFilter()
        }
    }

    private fun stop(){
        mDisposable?.dispose()
    }



    private fun registerBikeInfo(){
        com.sharkgulf.soloera.tool.config.registerBikeInfo(TAG, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                getCarInfo()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }

    override fun changeSecurity(map: HashMap<String, Any>) {
        view.showWaitDialog(null,true,null)
        mListener?.changeSecurity(map,object :ModuleResultInterface<BsSecuritySettingsBean>{
            override fun resultData(bean: BsSecuritySettingsBean?,pos:Int?) {
                view.diassDialog()
                view.resultChangeSecurity(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun testNotify(map: HashMap<String, Any>) {
        view.showWaitDialog(null,true,null)
        mListener?.testNotify(map,object :ModuleResultInterface<BsTestNotifyBean>{
            override fun resultData(bean: BsTestNotifyBean?,pos:Int?) {
                view.diassDialog()
                view.resultTestNotify(bean)
            }
            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun lostModu(map: HashMap<String, Any>) {
        mHttpModel?.requestLostMode(map,object :ModuleResultInterface<BsLostModeBean>{
            override fun resultData(bean: BsLostModeBean?,pos:Int?) {
                view.diassDialog()
                view.resultLostModu(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

fun getCarInfo(){
    val bean = presenterGetCarInfo()
    view.resultCarModule(bean.str,bean.mode,bean.carInfo)
}



}