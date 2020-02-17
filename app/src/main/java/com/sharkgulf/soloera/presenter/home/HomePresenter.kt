package com.sharkgulf.soloera.presenter.home

import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.module.DbModel
import com.sharkgulf.soloera.module.DbModelListener
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.HttpModelListener
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bind.BindCarMode
import com.sharkgulf.soloera.module.bind.BindCarModeListener
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.mvpview.home.IHomeView
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2019/1/2
 */
class HomePresenter :TrustPresenters<IHomeView>() ,IHomePresenterListener{

    private val TAG = HomePresenter::class.java.canonicalName!!
    private var bleMode: BindCarModeListener? = null
    private var homeMode: HomeModeListener? = null
    private var dbModel:DbModelListener? = null
    private var mHttpModel: HttpModelListener? = null
    init {
        homeMode = HttpModel()
        bleMode = BindCarMode()
        dbModel = DbModel()
        mHttpModel = HttpModel()
        registerBleStatus()
        registerBikeStatusInfo()
        registerBikesInfo()
    }

    private fun registerBleStatus(){
        val listener = object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                TrustLogUtils.d(BLE_TAG,"TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE :${TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE}")
                    view.resultBleStatus(TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE,TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE)
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        }
        registerBleCheckPassWordSuccess(listener,TAG)
        registerBleDissConnection(listener,TAG)
    }

    private fun registerBikeStatusInfo(){
        registerBikeStatus(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getBikeStatusInfo()
            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }

    private fun registerBikesInfo(){
        registerUpdateBikeList(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                view.resultUpdateBikesInfo()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        })
    }

    override fun getAlertList(map: HashMap<String, Any>) {
        mHttpModel?.requestAlertList(map,object :ModuleResultInterface<BsAlertBean>{
            override fun resultData(bean: BsAlertBean?,pos:Int?) {
                if (map["date"].toString() == TrustTools.getTime(System.currentTimeMillis())) {
                    if (bean != null && bean.data.list != null) {
                        dbModel?.putAlertBean(DEFULT,map["date"].toString(),bean)
                        val alertMap = RequestConfig.getAlertList(map["date"].toString(), TrustAppConfig.bikeId,false)
                        dbModel?.getAlertList(alertMap["bike_id"] as Int,alertMap["date"].toString(),alertMap["is_read"] as Boolean,object :ModuleResultInterface<DbAlertBean>{
                            override fun resultData(bean: DbAlertBean?,pos:Int?) {
                                if (bean != null) {
                                    val list = bean.alerBean?.data?.list
                                    if (list != null && list.isNotEmpty()) {
                                        view.resultIsRead(false)
                                    }else{
                                        view.resultIsRead(true)
                                    }
                                }else{
                                    view.resultIsRead(true)
                                }
                            }

                            override fun resultError(msg: String) {
                                view.diassDialog()
                                view.resultError(msg)
                            }
                        })
                    }else{
                        view.resultIsRead(true)
                    }

                }
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
//        dbModel?.getAlertList(map["bike_id"] as Int,map["data"].toString(),map["is_read"] as Boolean,object :ModuleResultInterface<DbAlertBean>{
//            override fun resultData(bean: DbAlertBean?) {
//
//            }
//
//            override fun resultError(msg: String) {}
//        })
    }


    override fun getUserInfo(map: HashMap<String, Any>) {
        homeMode!!.getUserInfo(map,object :ModuleResultInterface<BsGetUserInfoBean>{
            override fun resultData(bean: BsGetUserInfoBean?,pos:Int?) {
                view.resultUserInfo(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun getCarInfo(map: HashMap<String, Any>) {
        homeMode!!.getCarInfo(map,object:ModuleResultInterface<BsGetCarInfoBean>{
            override fun resultData(bean: BsGetCarInfoBean?,pos:Int?) {
                view.resultCarInfo(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun userExt(map: HashMap<String, Any>) {
        homeMode!!.userExt(map,object :ModuleResultInterface<BsUserExtBean>{
            override fun resultData(bean: BsUserExtBean?,pos:Int?) {
                view.resultExt(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun delCar(map: HashMap<String, Any>) {
        homeMode!!.delCar(map,object :ModuleResultInterface<BsDelCarBean>{
            override fun resultData(bean: BsDelCarBean?,pos:Int?) {
                view.diassDialog()
                view.resultDelCar(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun getBleInfo(map: HashMap<String, Any>) {
        bleMode?.requestGetBleSign(map,object :ModuleResultInterface<BsBleSignBean>{
            override fun resultData(bean: BsBleSignBean?,pos:Int?) {
                view.diassDialog()
                view.resultBleSign(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun updatePushId(map: HashMap<String, Any>) {
        homeMode?.updatePushId(map,object :ModuleResultInterface<BsPushIdBean>{
            override fun resultData(bean: BsPushIdBean?,pos:Int?) {
                view.diassDialog()
                view.resultPushId(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    fun getBikeStatusInfo(){
        view?.resultBikeStatus(getBikeStatus())
    }



    fun requestDemoData(){
        TrustLogUtils.d(TAG,"demo model")
        val findDemoUser = getAuthentication().findDemoUser()
        if (findDemoUser != null) {
            view.diassDialog()
            view.resultDemoBikeInfo(null,findDemoUser)
        }else{
            mHttpModel?.requestDemoData(hashMapOf(Pair<String,Boolean>("isUseDemoToken",true)),object :ModuleResultInterface<BsGetCarInfoBean>{
                override fun resultError(msg: String) {
                    view.diassDialog()
                    view.resultError(msg)
                }

                override fun resultData(bean: BsGetCarInfoBean?, pos: Int?) {
                    view.diassDialog()
                    view.resultDemoBikeInfo(bean,null)
                }

            })
        }

    }

}