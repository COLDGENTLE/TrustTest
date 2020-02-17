package com.sharkgulf.soloera.dataanalysis

import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.socketbean.*
import com.sharkgulf.soloera.tool.config.showToast
import com.trust.demo.basis.trust.utils.TrustAnalysis
import java.lang.Exception

/**
 *  Created by user on 2019/8/8
 *  所有数据保存中心
 */
class DataAnalysisBean {
    companion object{
        private var mDataAnalysisBean:DataAnalysisBean? = null

        fun getInstanse():DataAnalysisBean{
            if (mDataAnalysisBean == null) {
                mDataAnalysisBean = DataAnalysisBean()
            }
            return mDataAnalysisBean!!
        }
    }

    private val TAG = DataAnalysisBean::class.java.canonicalName
    private var mSocketCarBean = SocketCarBean()
    private var mBleStatusBean = BleStatusBean()
    //

    fun updateData(topic:String,data:String?){
        try {
            if (data != null) {
                checkType(topic,data)
            }
        }catch (e:Exception){
            showToast(e.message!!)
        }

    }

    private fun checkType(topic:String,data:String){

        when(topic){
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_LOCTION" -> {
                setDb(topic,analysis(data,CarLoctionBean::class.java))
//                mSocketCarBean.setCarLoctionBean(analysis(data,CarLoctionBean::class.java))
            }
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_POWER" -> {
                setDb(topic,analysis(data,BattInfoBean::class.java))
//                mSocketCarBean.setBattInfoBean(analysis(data,BattInfoBean::class.java))
            }
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_INFO" -> {
                setDb(topic,analysis(data,CarInfoNullBean::class.java))
//                mSocketCarBean.setCarInfoBean(analysis(data,CarInfoBean::class.java))
            }

            WEB_SOKECT_RECEIVE+WEB_CAR_STATUS -> {
                setDb(topic,analysis(data,BikeStatusBean::class.java))
//                mSocketCarBean.setCarInfoBikeStatus(analysis(data,BikeStatusBean::class.java))
            }
            BLE_STATUS -> {
                setDb(topic,analysis(data,Int::class.java))
//                mBleStatusBean.bleStatus = data.toInt()
            }
        }
    }

    fun <T> setDbData(topic: String,data:T){
        setDb(topic,data)
    }

    private fun<T> setDb(topic:String,data:T){
        when(topic){
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_LOCTION" -> {
                data as CarLoctionBean
                if (data.body.gps.lat != 0.0) {
                mSocketCarBean.setCarLoctionBean(data)
                }
            }
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_POWER" -> {
                mSocketCarBean.setBattInfoBean(data as BattInfoBean)
            }
            "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_INFO" -> {
                if (data is CarInfoBean) {
                    data as CarInfoBean
                    mSocketCarBean.setCarInfoBean(data)
                }else{
                    data as CarInfoNullBean
                    mSocketCarBean.setCarInfoBean(data.body)
                }

            }

            WEB_SOKECT_RECEIVE+WEB_CAR_STATUS -> {
                mSocketCarBean.setCarInfoBikeStatus(data as BikeStatusBean)
            }
            BLE_STATUS -> {
                mBleStatusBean.bleStatus = data.toString().toInt()
            }
        }
    }

    private fun<T> analysis(data:String,clasz:Class<T>):T{
        return TrustAnalysis.resultTrustBean<T>(data,clasz)
    }

     fun<T> getData(topic: String,bikeId: Int):T?{
         return when(topic){
             "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_LOCTION" -> {
                mSocketCarBean.getCarLoctionBean(bikeId)
            }
             "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_POWER" ->{
                mSocketCarBean.getBattInfoBean(bikeId)
            }
             "$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_INFO" -> {
                 mSocketCarBean.getCarInfoBean(bikeId)
             }
             WEB_SOKECT_RECEIVE+WEB_CAR_STATUS -> {
                 mSocketCarBean.getCarStatus(bikeId)?.getBody()
             }
             BLE_STATUS -> {
                 mBleStatusBean.bleStatus
             }
            else -> {
                null
            }
        } as T
    }

}