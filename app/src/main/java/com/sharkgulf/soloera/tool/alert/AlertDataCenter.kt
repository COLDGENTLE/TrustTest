package com.sharkgulf.soloera.tool.alert

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Message
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.cards.activity.alert.AlertListActivity
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.loging.StartUpActivity
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.module.bean.socketbean.WebAlertBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.trust.utils.TrustAnalysis
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/9/12
 */

class AlertDataCenter {

    companion object{
        private var mAlertDataCenter:AlertDataCenter? = null

        fun getInstance():AlertDataCenter{
            if (mAlertDataCenter == null) {
                mAlertDataCenter = AlertDataCenter()
            }
            return mAlertDataCenter!!
        }


    }


    private val handler = @SuppressLint("HandlerLeak")
    object :android.os.Handler(){
        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                val obj = msg.obj as String
                notificationCheckData(obj)
            }
        }
    }

    fun sendMsgForHander(msg:String){
        thread {
            Thread.sleep(1000)
            val obtain = Message.obtain()
            obtain.obj = msg
            obtain.what = DEFULT
            handler.sendMessage(obtain)
        }

    }



    private val mAlertTools:AlertTool = AlertTool.getInstance()
    private val TAG = "AlertDataCenter"
    private val mContext = TrustAppUtils.getContext()

    fun checkData(msg:String){
        val bean = TrustAnalysis.resultTrustBean<WebAlertBean>(msg, WebAlertBean::class.java).body
            if (bean.type == ALERT_TYPE_CAR_INFO) {
                checkMsgType(bean)
            }else{

            }
    }



    private fun checkMsgType(bean:WebAlertBean.BodyBean){
        when (bean.pop) {
            POPU_TYPE_NO -> {
                mAlertTools.showAlertNoUi(bean)
            }
            POPU_TYPE_FULL -> {
                mAlertTools.showAlertWebData(bean)
            }
            POPU_TYPE_POPU -> {
                mAlertTools.showAlertPopu(bean)
            }
            POPU_TYPE_TOAST -> {
                mAlertTools.showAlertToast(bean)
            }
        }
        if (getBsActivityLifecycleCallbacks().isAlertListActivity()) {
            val activity = getBsActivityLifecycleCallbacks().getActivity() as AlertListActivity
            activity.requestList()
        }
    }


    fun notificationCheckData(msg:String){
        val checkUser = checkUser()
        TrustLogUtils.d(TAG,"checkUser:${checkUser}")
        if (checkUser != null && checkUser) {
            val bikes = getUser()?.userBikeList?.bikes
            if (bikes != null && bikes.isNotEmpty() && demoBikeList == null) {
                demoBikeList = IntArray(bikes.size)
                bikes.forEachIndexed { index, bikesBean ->
                    demoBikeList[index] = bikesBean.bike_id
                }
                TrustAppConfig.bikeId = getAuthentication().getChooseBid() ?: bikes[0].bike_id
            }
            startAvtivity(msg,true)
            }else{
            if (!TrustAppConfig.appIsInit) {
                TrustLogUtils.d(TAG,"user is no login")
                val intent = Intent(mContext, StartUpActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val mainIntent = Intent(mContext, MainHomeActivity::class.java)
//              mainIntent.putExtra(ALERT_NOTIFICATIION_KEY,msg)
                mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                val login = Intent(mContext,LogingActivity::class.java)
                val intents =  arrayOf(mainIntent,login,
                        intent.putExtra("isStartMain",false))
                mContext.startActivities(intents)
            }

            }
//            if (bean.type == ALERT_TYPE_CAR_INFO) {
//                mAlertTools.showAlertWebData(bean)
//            }else{
//
//            }
    }


    private fun startAvtivity(msg:String,isNotification:Boolean){
        val bean = TrustAnalysis.resultTrustBean<WebAlertBean>(msg, WebAlertBean::class.java).body

        if (checkIsShowAlert(bean.bid)) {

            if (bean.event == TEST_ALERT) {
                mAlertTools.showAlertWebData(bean)
            }else{
                if (TrustAppConfig.appIsInit && !isNotification) {
                }else if (TrustAppConfig.appIsInit && isNotification){
//                val alertList = Intent(mContext,AlertListActivity::class.java)
//                alertList.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                mContext.startActivity(alertList)
                    checkData(msg)
                } else{
                    val intent = Intent(mContext, StartUpActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val mainIntent = Intent(mContext, MainHomeActivity::class.java)
                    mainIntent.putExtra(ALERT_NOTIFICATIION_KEY,msg)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                val alertList = Intent(mContext,AlertListActivity::class.java)
                    val intents =  arrayOf(mainIntent,
                            intent.putExtra("isStartMain",false))
                    mContext.startActivities(intents)
                }

            }
        }
        TrustAppConfig.appIsInit = true
    }

    private fun checkIsShowAlert(bid:Int):Boolean{
//        return if (!appIsInit && demoBikeList.indexOf(bid) != DEFULT) {
//            return true
//        }else return appIsInit && bid == bikeId

            return demoBikeList.indexOf(bid) != DEFULT
    }

}
