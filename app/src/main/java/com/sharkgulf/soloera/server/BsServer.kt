package com.sharkgulf.soloera.server

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.shark.sharkbleclient.SharkBleDeviceInfo
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.tool.alert.AlertDataCenter
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.trust.demo.basis.trust.utils.TrustLogUtils
import razerdp.basepopup.BasePopupWindow
import com.sharkgulf.soloera.module.controllcar.ControllCarBleMode
import com.sharkgulf.soloera.tool.view.TrustFloatingBox


/*
 *Created by Trust on 2018/12/25
 */
class BsServer :Service(){
    private val mContext:Context = this
    private val mBsBinder = BsBinder()
    private val TAG = "BsServer"
    private var mPopuwindow: BasePopupWindow? = null
    private var mBleMode:ControllCarBleMode = ControllCarBleMode()
    override fun onBind(intent: Intent?): IBinder? {
        return mBsBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        registerRobLogin()
        registerAlert()
        registerListener()
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TrustLogUtils.d("onStartCommand")

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        TrustLogUtils.d("onDestroy")
        dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_ROB_LOGIN,TAG)
        dataAnalyCenter().unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_PUSH_ALERT,TAG)
    }

    inner class BsBinder :Binder() {
        fun getServer():BsServer{
            return this@BsServer
        }
    }



    private val trustWebSocket:TrustWebSocket = TrustWebSocket()


    fun getWebSocket():TrustWebSocket{
        return trustWebSocket
    }

    fun setBleCallBack(){
        bleSetBsBleScannerCallBack(mBleScannerCallBack)
    }


    private val mBleScannerCallBack = object : BsBleTool.BsBleCallBack{
        override fun resultConnectionStatus(status: Boolean) {
            TrustLogUtils.d(BLE_TAG,"connection $status")
            if (status) {
                showDebugToast("ble connection success")
                dataAnalyCenter().sendData(BLE_CONNECT_SUCCESS)
            }else{
                dataAnalyCenter().sendData(BLE_CONNECT_CLOSE)
                CONTROLL_STATUS = CONTROLL_CAR_INTERNET
                showDebugToast("ble connection close")
            }
        }

        override fun resultScannerCallBack(device: SharkBleDeviceInfo) {
        }
    }

    private fun registerRobLogin(){
        dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_ROB_LOGIN,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                showPopi()
                extUser()
            }

            override fun onErrorCallBack(msg: String, timeOutMsg: String?) {}
        },TAG)
    }

    private fun registerAlert(){
        dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_PUSH_ALERT,object :DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutMsg: String?) {}
            override fun onNoticeCallBack(msg: String?) {
                AlertDataCenter.getInstance().checkData(msg!!)
                TrustLogUtils.d(TAG,"service  $msg") }
        },TAG)
    }


    private fun showPopi(){
        BsApplication.mAuthentication.allUserExt()
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.rob_login_layout,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
            override fun resultPopuwindowViewListener(v: View?): View {
                v!!.findViewById<ImageView>(R.id.rob_login_close_btn).setOnClickListener {
                    mPopuwindow?.dismiss()
                    arouterStartActivity("/activity/LoginActivity")
                }
                v.findViewById<TextView>(R.id.rob_login_to_login_btn).setOnClickListener {
                    mPopuwindow?.dismiss()
                    arouterStartActivity("/activity/LoginActivity")
                }
                return v
            }
        })
        mPopuwindow?.isAllowDismissWhenTouchOutside = false
        mPopuwindow?.setBackPressEnable(false)
        mPopuwindow?.showPopupWindow()
        getWebSocket().disConnect()
    }

    fun showFloatingBox(txt:String,isError:Boolean){
        TrustFloatingBox.getInstance().showFloatingBox(txt,isError)
    }

    fun checkListFloatingBox(){
        TrustFloatingBox.getInstance().checkListFloatingBox()
    }


    fun registerListener(){
        registerBleCheckPassWordSuccess(object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                    mBleMode.readElectironic()
                    mBleMode.readCushionInduction()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        },TAG)
    }
}