package com.sharkgulf.soloera.module.controllcar

import android.view.View
import com.sharkgulf.soloera.*
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.dataanalysis.DataQueueManger
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.config.dataAnalyCenter
import com.sharkgulf.soloera.tool.config.getWebSocketStatus
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustAnalysis
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlin.collections.HashMap

/**
 *  Created by user on 2019/1/24
 */
class  ControllCarInternetMode : HttpModel(),ControllCarModeListener{

    override fun requestDelBle(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>?) {}
    override fun requestLoseBike(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {}
    private val mDataCenter = DataAnalysisCenter.getInstance()
    private val TAG = ControllCarInternetMode::class.java.canonicalName

    init {
        if (!isFirst) {
            isFirst = true
            register()
        }
    }

    //设防撤防
    private val CAR_OPEN_LOCK = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTON
    private val CAR_CLOSE_LOCK = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF
    private val CAR_NO_MUSIC_OPEN_LOCK = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ALERTOFF

    //一键启动
    private val CAR_START = WEB_SOKECT_SEND+WEB_SOKECT_CAR_START
    //坐桶
    private val CAR_BUCKET = WEB_SOKECT_SEND+WEB_SOKECT_CAR_BUCKET_OPEN
    //电门
    private val CAR_OPEN_POWER = WEB_SOKECT_SEND+WEB_SOKECT_CAR_ACCON
    private val CAR_CLOSE_POWER = WEB_SOKECT_SEND+ WEB_SOKECT_CAR_ACCOFF
    //寻车
    private val CAR_FIND = WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND


    companion object{
        private val controllList = arrayListOf<String>()
        private var isFirst = false
        private val controllMap = hashMapOf<String,ModuleResultInterface<Int>>()
    }


    override fun requestCarLock(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        val url = when (actionType) {
            ACTION_OPEN -> {//设防
                CAR_OPEN_LOCK
            }

            ACTION_CLOSE -> {//撤防
                CAR_CLOSE_LOCK

            }

            ACTION_NO_MUSIC_CLOSE -> {
                CAR_NO_MUSIC_OPEN_LOCK
            }
            else -> {
                CAR_CLOSE_LOCK
            }
        }

        TrustLogUtils.d(TAG,"url : ${url}")
        sendData(url, hashMap!!["bike_id"] as Int,resultInterface)
    }


    override fun requestStartCar(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        val url = when (actionType) {
            ACTION_OPEN -> {
                CAR_START
            }

            ACTION_CLOSE -> {
                CAR_START
            }else -> { CAR_START }
        }
        sendData(url, hashMap!!["bike_id"] as Int,resultInterface)
    }

    override fun requestOpenBucket(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
//        sendRequest(hashMap,String::class.java,resultInterface)
        val url = when (actionType) {
            ACTION_OPEN -> {
                CAR_BUCKET
            }

            ACTION_CLOSE -> {
                CAR_BUCKET
            }else -> { CAR_BUCKET }
        }

        sendData(url,hashMap!!["bike_id"] as Int,resultInterface)
    }

    override fun requestOpenOrCloseEle(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
//        sendRequest(hashMap,String::class.java,resultInterface)

        val url = when (actionType) {
            ACTION_OPEN -> {
                CAR_OPEN_POWER
            }

            ACTION_CLOSE -> {
                CAR_CLOSE_POWER
            }else -> { "" }
        }
        sendData(url,hashMap!!["bike_id"] as Int,resultInterface)

    }

    override fun requestFindCar(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
//        sendRequest(hashMap,String::class.java,resultInterface)

        val url = when (actionType) {
            ACTION_OPEN -> {
                CAR_FIND
            }

            ACTION_CLOSE -> {
                CAR_FIND
            }else -> { "" }
        }

        dataAnalyCenter().registerCallBack(WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_CAR_FIND,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                delTopic(url)
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {
                DataQueueManger.getInstance().initInstructionMap(timeOutTopic!!)
                resultData(ACTION_TIME_OUT,controllMap[timeOutTopic])
                val i = controllList.indexOf(timeOutTopic)
                if (i != -1) { controllList.removeAt(i) }
            }

        },TAG)

        sendData(url,hashMap!!["bike_id"] as Int,resultInterface)
    }


    private fun <T>  sendRequest(hashMap: HashMap<String, Any>?,clasz :Class<T>,resultInterface: ModuleResultInterface<T>){
        sendRrequest(TrustAppConfig.URL_CONTROLL_CAR,hashMap!!, clasz,resultInterface)
    }

    private fun timeOut(msg:String,resultInterface: ModuleResultInterface<String>?){
        resultInterface?.resultError(msg)
    }

    private fun resultData(msg:Int,resultInterface: ModuleResultInterface<Int>?){
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            resultInterface?.resultData(msg)
            emitter.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

    }


    private fun register(){
        registerBikeStatus(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                TrustLogUtils.d(TAG,"onNoticeCallBack  $msg")
                checkBikeStatus(msg!!)
//                resultData(msg,)
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {
                TrustLogUtils.d(TAG,"onErrorCallBack  $msg  timeOutMsg $timeOutTopic  controllList.Size ${controllList.size}")
                val i = controllList.indexOf(timeOutTopic)
                if (i != -1) { controllList.removeAt(i) }
                resultData(ACTION_TIME_OUT,controllMap[timeOutTopic])
            }

        })
    }

    private fun checkBikeStatus(msg:String){
    //{"path":"/push/bike/bikestatus",
        // "header":{"to":"4","uuid":"5987b32f-16a8-4e74-853c-0ce3c20ee1a9","ts":1567130920,"ack":1},
        // "body":{"acc":1,"bike_id":5,"defence":1,"lstatus":0,"rstatus":0,"sstatus":0,"ts":1567130889000000000}}

        val bikeStatusBean = TrustAnalysis.resultTrustBean<BikeStatusBean>(msg,BikeStatusBean::class.java)
        if (controllList.isNotEmpty()) {
            controllList.forEach {
                checkControllList(it,bikeStatusBean.getBody()!!)
            }
        }
        mBikeStatusBean = bikeStatusBean
    }



    private fun checkControllList(topic :String,bean:BikeStatusBean.BodyBean){
        if (bean.getIsOl()) {
            when(topic){
                CAR_OPEN_LOCK -> {
                    if (bean.defence == CONTROLL_CAR_LOCK) {
                        delTopic(CAR_OPEN_LOCK)
                    }
                }

                CAR_CLOSE_LOCK -> {
                    if (bean.defence == CONTROLL_CAR_UNLOCK) {
                        delTopic(CAR_CLOSE_LOCK)
                    }
                }

                CAR_NO_MUSIC_OPEN_LOCK -> {
                    if (bean.defence == CONTROLL_CAR_NO_MUSIC_LOCK) {
                        delTopic(CAR_NO_MUSIC_OPEN_LOCK)
                    }
                }

                CAR_START -> {
                    if (bean.defence == CONTROLL_CAR_UNLOCK && CONTROLL_CARS_STATUS_ACC_ON == bean.acc) {
                        delTopic(CAR_START)
                    }
                }

                CAR_BUCKET -> {
                    if (bean.sstatus == CONTROLL_CAR_BUCKET_OPEN) {
                        delTopic(CAR_BUCKET)
                    }
                }

                CAR_OPEN_POWER -> {
                    if (bean.acc == CONTROLL_CARS_STATUS_ACC_ON) {
                        delTopic(CAR_OPEN_POWER)
                    }
                }

                CAR_CLOSE_POWER -> {
                    if (bean.acc == CONTROLL_CARS_STATUS_ACC_OFF) {
                        delTopic(CAR_CLOSE_POWER)
                    }
                }

            }
        }else{
            resultData(BIKE_IS_OFF,controllMap[topic])
        }

    }

    private fun delTopic(topic:String){
        DataQueueManger.getInstance().initInstructionMap(topic)
        val i = controllList.indexOf(topic)
        if (i != -1) { controllList.removeAt(i) }
        resultData(ACTION_SUCCESS,controllMap[topic])
    }

    override fun requestControllBikeLock(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        val url = when (actionType) {
            ACTION_LOCK_BIKE -> {
                WEB_SOKECT_CAR_LOCK
            }
            ACTION_LOCK_BIKE_CANCEL -> {
                WEB_SOKECT_CAR_LOCK_CANCEL
            }
            ACTION_UNLOCK_BIKE -> {
                WEB_SOKECT_CAR_UNLOCK
            }
            ACTION_UNLOCK_BIKE_CANCEL -> {
                WEB_SOKECT_CAR_UNLOCK_CANCEL
            }
            else -> {
                WEB_SOKECT_CAR_LOCK
            }
        }
        sendData(url, hashMap!!["bike_id"] as Int,resultInterface)
    }



    private fun sendData(url:String,bikeId:Int,resultInterface: ModuleResultInterface<Int>){
        if (isDemoStatus()) {
            TrustTools<View>().setCountdown(3,object :TrustTools.CountdownCallBack{
                override fun callBackCountDown() { resultInterface.resultData(ACTION_SUCCESS) }
            })
        }else{
            if (TrustHttpUtils.getSingleton(TrustAppUtils.getContext()).isNetworkAvailable()) {
//            if (BIKE_OL_STATUS != BIKE_OFF) {
                if (getChannel("UMENG_CHANNEL") == "BS_DEV" || BuildConfig.DEBUG) {
                    if (getWebSocketStatus()) {
                        controllList.add(url)
                        controllMap[url] = resultInterface
                        mDataCenter.sendData(url, bikeId)
                    }else{
                        resultInterface.resultData(WEBSOCKET_ERROR)
                    }
                }else{
                    controllList.add(url)
                    controllMap[url] = resultInterface
                    mDataCenter.sendData(url, bikeId)
                }
//            }else{
//                resultInterface.resultData(BIKE_IS_OFF)
//            }

            }else{
                resultInterface.resultData(INTERNET_ERROR)
            }
        }

    }
}