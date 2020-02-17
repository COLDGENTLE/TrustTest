package com.sharkgulf.soloera.presenter.controllcar

import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.controllcar.ControllCarBleMode
import com.sharkgulf.soloera.module.controllcar.ControllCarInternetMode
import com.sharkgulf.soloera.module.controllcar.ControllCarModeListener
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.tool.config.getBattryInfoData
import com.sharkgulf.soloera.tool.config.getString
import com.sharkgulf.soloera.tool.config.registerBattryInfo
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2019/1/4
 */
class ControllCarPresenter :TrustPresenters<IControllCarView>() ,IControllCarPresenterListener{

    private var TAG = ""
    private var controllBle: ControllCarModeListener? = null
    private var controllInternet:ControllCarModeListener? = null
    init {
        controllBle = ControllCarBleMode()
        controllInternet = ControllCarInternetMode()
    }

    override fun setTAG(tag: String) {
        TAG = tag
        registerBattryStatus()
    }

    private fun registerBattryStatus(){
        val listener = object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getBikeInfo()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }

        registerBattryInfo(listener,TAG)
    }

     fun getBikeInfo() {
        val battryInfoData = getBattryInfoData()
        var battry1: BattInfoBean.BodyBean.BattBean? = null
        var battry2: BattInfoBean.BodyBean.BattBean? = null
        var emerBattBean: BattInfoBean.BodyBean.EmerBattBean? = null

        battryInfoData?.batt?.forEach {
            if (it.info.position == BATTERY_ONE && battry1 == null) {
                battry1 = it
            } else {
                battry2 = it
            }
        }
        TrustLogUtils.d(TAG, "battry1 :${battry1 == null}")
        view.resultBattryInfo(isDouble, battry1, battry2, emerBattBean)
    }

    override fun requestCarLock(actionType: Int, hashMap: HashMap<String, Any>?) {

        view.showWaitDialog("",true,"")
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultCarLock(actionType,bean,checkAction(actionType,bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        }

        if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestCarLock(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestCarLock(actionType,initMap(),resultListener)
        }
    }

    override fun requestStartCar(actionType: Int, hashMap: HashMap<String, Any>?) {
        view.showWaitDialog("",true,"")
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultStartCar(actionType,bean,checkAction(CAR_START,bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultStartCar(actionType, DEFUTE)
            }

        }

        if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestStartCar(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestStartCar(actionType,initMap(),resultListener)
        }
    }

    override fun requestOpenBucket(actionType: Int, hashMap: HashMap<String, Any>?) {
        view.showWaitDialog("",true,"")
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultOpenBucket(actionType,bean,checkAction(CAR_BUCKET_OPEN,bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultOpenBucket(actionType, DEFUTE)
            }

        }
        if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestOpenBucket(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestOpenBucket(actionType,initMap(),resultListener)
        }
    }

    override fun requestOpenOrCloseEle(actionType: Int, hashMap: HashMap<String, Any>?) {
        view.showWaitDialog("",true,"")
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultOpenOrCloseEle(actionType,bean,checkAction(if (actionType == ACTION_OPEN) CAR_ACC_ON else CAR_ACC_OFF, bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultOpenOrCloseEle(actionType, DEFUTE)
            }

        }
        if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestOpenOrCloseEle(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestOpenOrCloseEle(actionType,initMap(),resultListener)
        }
    }

    override fun requestFindCar(actionType: Int, hashMap: HashMap<String, Any>?) {
        view.showWaitDialog("",true,"")
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultFindCar(actionType,bean,checkAction(CAR_FIND,bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultFindCar(actionType, DEFUTE)
            }

        }
        if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestFindCar(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestFindCar(actionType,initMap(),resultListener)
        }
    }





    private fun initMap():HashMap<String,Any>{
        return  hashMapOf<String,Any>(Pair<String,Any>("bike_id",bikeId))
    }

    private fun checkAction(controllACtion:Int,action:Int?):String?{
        val context = TrustAppUtils.getContext()
       return  when (controllACtion) {
            CAR_LOCK -> {
                getStr(action,getString(R.string.controll_car_lock_success_tx),getString(R.string.controll_car_lock_time_out_tx), getString(R.string.controll_car_lock_error_tx))
            }
            CAR_UN_LOCK -> {
                getStr(action,getString(R.string.controll_car_unlock_success_tx),getString(R.string.controll_car_unlock_time_out_tx),getString(R.string.controll_car_unlock_error_tx))
            }
            CAR_NO_VIDE_UN_LOCK -> {
                getStr(action,"车辆已静音撤防","车辆静音撤防超时，请重试",getString(R.string.controll_car_unlock_error_tx))
            }
            CAR_BUCKET_OPEN -> {
                getStr(action,getString(R.string.controll_car_bucket_success_tx),getString(R.string.controll_car_bucket_time_out_tx),getString(R.string.controll_car_bucket_error_tx))
            }
            CAR_ACC_ON -> {
                getStr(action,"车辆已开电门","车辆开电门超时，请重试","开电门失败")
            }
            CAR_ACC_OFF -> {
                getStr(action,"车辆已关电门","车辆关电门超时，请重试","关电门失败")
            }
            CAR_START -> {
                getStr(action,getString(R.string.controll_car_start_success_tx),getString(R.string.controll_car_start_time_out_tx),getString(R.string.controll_car_start_error_tx))
            }
            CAR_FIND -> {
                getStr(action,getString(R.string.controll_car_find_car_success_tx),getString(R.string.controll_car_find_car_time_out_tx),getString(R.string.controll_car_find_car_error_tx))
            }
            else -> {
                getStr(action,"未知指令","未知超时，请重试","未知指令失败")
            }
        }

    }

    private fun getStr(action: Int?,successStr:String,timeOutStr:String,errorStr:String):String{
        return when (action) {
            ACTION_SUCCESS -> {
                successStr
            }
            ACTION_TIME_OUT -> {
                timeOutStr
            }
            ACTION_ERROR -> {
                errorStr
            }
            INTERNET_ERROR -> {
                getString(R.string.http_error_tx)
            }
            WEBSOCKET_ERROR -> {

               getString(R.string.web_socket_error)
               getString(R.string.web_socket_error)
            }
            BIKE_IS_OFF -> {
                "车辆已离线"
            }
            else -> {
                "错误码 $action"
            }
        }
    }

    private fun checkIsSuccess(action:Int?):Boolean{
        return action == ACTION_SUCCESS
    }

    //设防
    private val CAR_LOCK = 2
    //撤防
    private val CAR_UN_LOCK = 3
    //静音设防
    private val CAR_NO_VIDE_UN_LOCK = 4
    //开坐桶
    private val CAR_BUCKET_OPEN = 5
    //开电门
    private val CAR_ACC_ON = 6
    //关电门
    private val CAR_ACC_OFF = 7
    //一键启动
    private val CAR_START = 8
    //寻车
    private val CAR_FIND = 9
}