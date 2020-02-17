package com.sharkgulf.soloera.appliction

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.multidex.MultiDex
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.server.BsServer
import com.sharkgulf.soloera.server.TrustWebSocket
import com.sharkgulf.soloera.tool.ReadWriteUtils
import com.sharkgulf.soloera.MyObjectBox
import com.sharkgulf.soloera.TrustAppConfig.userPhone
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.tool.config.*

import com.trust.demo.basis.base.TrustApplication
import io.objectbox.BoxStore


/*
 *Created by Trust on 2018/12/11
 */
class BsApplication:TrustApplication() {
    private val mBsSc = BsServiceConnection()
    private var mServer:BsServer? = null
    private var mUserLoginStatus:Boolean = false
    private val TAG = BsApplication::class.java.canonicalName
    //初始化数据库
     companion object {
        lateinit var boxStore: BoxStore
        var applicationContext:Context? = null
        @SuppressLint("StaticFieldLeak")
        var bsDbManger: BsDbManger? = null
        var mTrustWebSocket:TrustWebSocket? = null
        @SuppressLint("StaticFieldLeak")
        var mBsApplication: BsApplication? = null


        @SuppressLint("StaticFieldLeak")
        private var mReadWriteUtils:ReadWriteUtils? = null
        val mAuthentication: Authentication = Authentication.getAuthentication()

        fun macString(mac:String): String {
            val replace = mac.replace("(.{2})".toRegex(), "$1:")//加入：
            val substring = replace.substring(0, replace.length - 1)
            return substring
        }

        fun removeString(mac:String):String{
            return mac.replace(":", "")
        }


    }



    override fun onCreate() {
        super.onCreate()
        checkUser()
        BsBleTool.getInstance()
        mBsApplication = this
        mReadWriteUtils = ReadWriteUtils.getReadWriteTool(getContexts())
        Companion.applicationContext = this


        initObjectBox()
        InitThirdParty(this)

        bindServer()

        initDefultBikeId()

//        getDbCarInfoManger().clearCarInfo(8)
//        testSend()

//        val time = TrustTools.getTime("2019/10/27","yyyy/MM/dd")
//        TrustLogUtils.d(TAG,"TrustTools.getTime(Date(time),\"time\") ： ${time}")
//        TrustLogUtils.d(TAG,"TrustTools.getTime(Date(time),\"dd\") ： ${TrustTools.getTime(Date(time),"MM")}")

    }



    private fun initObjectBox() {
        boxStore =  MyObjectBox.builder().androidContext(this).build()
        bsDbManger = BsDbManger(boxStore)
    }

    private inner class  BsServiceConnection :ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service != null) {
                mServer = (service as BsServer.BsBinder).getServer()
                mTrustWebSocket = mServer!!.getWebSocket()
            }
        }
    }

    fun getService():BsServer?{
        if (mServer == null) {
            bindServer()
        }
        return mServer
    }

    private fun bindServer(){
        val serviceIntent = Intent(this, BsServer::class.java)
        bindService(serviceIntent,mBsSc, Context.BIND_AUTO_CREATE)
    }

    private fun unbindServer(){
        unbindService(mBsSc)
    }

    override fun onTerminate() {
        super.onTerminate()
        unbindServer()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    fun setUserLoginStatus(userLoginStatus:Boolean){
        mUserLoginStatus = userLoginStatus
    }

    fun getUserLoginStatus():Boolean{
        return mUserLoginStatus
    }



//    private fun testSend(){
//
//        thread {
//            while (true){
//
//                Thread.sleep(10000)
//
//                dianChi()
//            }
//
//
//        }
//
//
//    }
//    var i = 0
////    private fun testi():Int{
////        if(i>=4){
////            i = 1
////        }
////        return i++
////    }

//    private fun testCarinfo(){
//        dataAnalyCenter().sendLocalData("/push/bike/bikeinfo",
//                "{\"path\":\"/push/bike/bikeinfo\",\"header\":{\"to\":\"210\",\"uuid\":\"39fc272f-5674-4812-9f7c-1c2d8d218bc7\",\"ts\":1570702523,\"ack\":1},\"body\":{\"activated_time\":\"2019-10-10 09:27:15\",\"base\":{\"imei\":\"866274030039193\",\"imsi\":\"460040548606281\",\"mac\":\"C375A5D61E64\",\"sn\":\"G510BQA17072800218\"},\"batt_support\":2,\"bike_class\":0,\"bike_id\":4,\"bike_name\":\"蓝鲨Robor lite1E64\",\"bind_days\":2,\"binded_time\":\"2019-10-09 16:45:02\",\"brand\":{\"brand_id\":1,\"brand_name\":\"蓝鲨\",\"logo\":\"\"},\"cc_id\":10003,\"color\":\"黄\",\"completion\":100,\"ctrl_tmpl\":3,\"func\":{\"e_sidestand\":0,\"saddle_sensor\":0},\"model\":{\"ctrl_tmpl\":3,\"model_id\":1,\"model_name\":\"Robor lite\",\"pic_b\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png\",\"pic_s\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png\",\"pic_side\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png\"},\"order\":1014,\"owner_id\":210,\"pic_b\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png\",\"pic_s\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png\",\"pic_side\":\"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png\",\"plate_num\":\"plate_num\",\"security\":{\"custom\":{\"batt_out\":true,\"move\":true,\"vibr_severe\":true},\"mode\":1},\"status\":${testi()},\"total_miles\":87,\"vin\":\"vin-C375A5D61E64\"}}")
//    }

    private fun dianChi(){
        val string = when (3) {
            1 -> {
               "{\"path\":\"/push/bike/battinfo\",\"header\":{\"to\":\"210\",\"uuid\":\"25aaa411-0d58-4b34-a442-9870ce11173d\",\"ts\":1570704103,\"ack\":1},\"body\":{\"batt\":[],\"bike_id\":4,\"emer_batt\":{\"left_days\":4,\"time_desc\":\"刚刚\",\"vol\":417}}}"
            }
            2 -> { "{\"path\":\"/push/bike/battinfo\",\"header\":{\"to\":\"210\",\"uuid\":\"fc8d1d98-d4ae-4276-a934-7316c86aea9a\",\"ts\":1570705996,\"ack\":1},\"body\":{\"batt\":[{\"info\":{\"cycle\":0,\"loss\":0,\"position\":0,\"prod_date\":\"2019-07-16 17:01:12\",\"rated_c\":30,\"rated_v\":48,\"sn\":\"BAT_A1234567890\",\"type\":0,\"version\":\"BMS1.1.0\"},\"status\":{\"capacity\":88,\"charge\":0,\"charge_es\":0,\"in_use\":1,\"mile_es\":188,\"temp\":66,\"vol\":48000}},{\"info\":{\"cycle\":0,\"loss\":0,\"position\":1,\"prod_date\":\"2019-07-16 17:01:12\",\"rated_c\":30,\"rated_v\":48,\"sn\":\"BAT_A1234567890\",\"type\":0,\"version\":\"BMS1.1.1\"},\"status\":{\"capacity\":88,\"charge\":0,\"charge_es\":0,\"in_use\":1,\"mile_es\":188,\"temp\":66,\"vol\":48000}}],\"bike_id\":4,\"emer_batt\":{\"left_days\":0,\"time_desc\":\"\",\"vol\":0}}}" }
            3 -> { "{\"path\":\"/push/bike/battinfo\",\"header\":{\"to\":\"210\",\"uuid\":\"fc8d1d98-d4ae-4276-a934-7316c86aea9a\",\"ts\":1570705996,\"ack\":1},\"body\":{\"batt\":[{\"info\":{\"cycle\":0,\"loss\":0,\"position\":0,\"prod_date\":\"2019-07-16 17:01:12\",\"rated_c\":30,\"rated_v\":48,\"sn\":\"BAT_A1234567890\",\"type\":0,\"version\":\"BMS1.1.0\"},\"status\":{\"capacity\":88,\"charge\":0,\"charge_es\":0,\"in_use\":1,\"mile_es\":188,\"temp\":66,\"vol\":48000}}],\"bike_id\":4,\"emer_batt\":{\"left_days\":0,\"time_desc\":\"\",\"vol\":0}}}" }
            else -> {
                "{\"path\":\"/push/bike/battinfo\",\"header\":{\"to\":\"210\",\"uuid\":\"25aaa411-0d58-4b34-a442-9870ce11173d\",\"ts\":1570704103,\"ack\":1},\"body\":{\"batt\":[],\"bike_id\":4,\"emer_batt\":{\"left_days\":4,\"time_desc\":\"刚刚\",\"vol\":417}}}}"
            }
        }

        dataAnalyCenter().sendLocalData("/push/bike/battinfo",string)
    }


    private fun initDefultBikeId(){
        val user = getAuthentication().getUser()
        if (user != null) {
            userPhone = user.userPhone
            val userBikeList = user.userBikeList
            if (userBikeList != null ) {
                val bikes = userBikeList.bikes
                if (bikes != null && bikes.isNotEmpty()) {
                    dataAnalyCenter().setBikeId(bikes[0].bike_id)
                }
            }
        }
    }

     fun getWebSocket():TrustWebSocket{
        return mTrustWebSocket!!
    }
}