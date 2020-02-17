package com.sharkgulf.soloera.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.shark.sharkbleclient.*
import com.shark.sharkbleclient.SharkBleCommands.Electronic.*
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.ObservableOnSubscribe
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.tool.BleTool
import com.sharkgulf.soloera.tool.config.*


/**
 *  Created by user on 2019/8/20
 */
class BsBleTool {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mBsBleTool: BsBleTool? = null

        fun getInstance(): BsBleTool {
            if (mBsBleTool == null) {
                mBsBleTool = BsBleTool()
            }
            return mBsBleTool!!
        }

        private val bleStatusListener = object : TrustAppUtils.BleStatus.BleStatusListener {
            override fun BleOpening() {
                TrustLogUtils.d(TAG, "ble 开启中")
            }

            override fun BleOpen() {
                TrustLogUtils.d(TAG, "ble 开启")
                dataAnalyCenter().sendLocalData(BLE_STATUS, BLE_STATUS_OPEN.toString())
            }

            override fun BleClose() {
                TrustLogUtils.d(TAG, "ble 关闭")
                dataAnalyCenter().sendLocalData(BLE_STATUS, BLE_STATUS_CLOSE.toString())
            }

            override fun BleCloseing() {
                TrustLogUtils.d(TAG, "ble 关闭中")
            }

        }

    }

    private val TAG = BsBleTool::class.java.canonicalName
    private val mContext = TrustAppUtils.getContext()
    private val mDevices = hashMapOf<String, SharkBleDeviceInfo>()
    private var mBsBleScannerCallBack: BsBleCallBack? = null
    private val NUM = 100
    private val NO_NUM = 2
    private var mTimes = NUM
    private val ON_RECONNECTION = 0
    private val RECONNECTION = 8000
    private var mElectironic :SharkBleCommands.Electronic? = null
    private var mCushionInduction :SharkBleCommands.CushionInduction? = null
    //是否重连
    private var mIsReConnection = true
    private var mIsConnection = false
    //是否可以进行控制ble所有操作
    private var mIsControllBle = true

    init {
        TrustAppUtils.initBleStatus(bleStatusListener)
        DataAnalysisCenter.getInstance().registerCallBack(BLE_STATUS, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                if (msg!!.toInt() == BLE_STATUS_OPEN) {
                    TrustLogUtils.d(TAG, "$mAddress $mId $mPassword")
                    if (mAddress != null && mId != null && mPassword != null) {
                        bleConnec(true, mAddress!!, mPassword!!)
//                        connection(mAddress!!, mId!!)
                    }
                } else {
                    TrustLogUtils.d(TAG, "ble close")
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }, TAG)
    }


    fun setBleConfig(address: String, id: String, passWord: String) {
        mAddress = address
        mId = id
        mPassword = passWord
    }


    private val mClient = object : SharkBleClient(mContext) {
        override fun onConnStateChanged(connected: Boolean) {
            mIsConnection = connected
            mBsBleScannerCallBack?.resultConnectionStatus(connected)
            super.onConnStateChanged(connected)
        }

        override fun onConnecting(times: Int): Int {
            mTimes = if (mIsReConnection) {
                NUM
            } else {
                NO_NUM
            }
            TrustLogUtils.d(TAG, "times $times MTIME:$mTimes")

            if (times > mTimes) {
                return ON_RECONNECTION
            }
            return RECONNECTION
        }

        override fun onConnFailed() {
            mIsConnection = false
            mBsBleScannerCallBack?.resultConnectionStatus(false)
            super.onConnFailed()
        }
    }

    fun setIsReConnection(connectionStatus: Boolean) {
        mIsReConnection = connectionStatus
    }

    fun getCliecnt(): SharkBleClient {
        return mClient
    }

    private val mCollection = object : SharkBleDeviceCollection() {
        override fun onDeviceUpdated(info: SharkBleDeviceInfo) {
            super.onDeviceUpdated(info)
            if (mDevices[info.device.address] == null) {
                mDevices[info.device.address] = info
                mBsBleScannerCallBack?.resultScannerCallBack(info)
            }
        }
    }


    fun disConnection() {
        isActivate = false
        mClient.disconnect()
        mAddress = null
        mId = null
    }

    private var mAddress: String? = null
    private var mId: String? = null
    private var mPassword: String? = null
    fun connection(address: String? = null, id: String) {
        mAddress = address
        mId = id
        val connect = mClient.connect(mAddress, mId)
        TrustLogUtils.d(TAG,"connect $connect")
    }

    fun getBleId(): String? {
        return mId
    }



    fun readDeviceStatus(): Int? {
        return BleTool.bytesToInt(mClient.readBindState(), 0)
    }

    private val mScanner = object : SharkBleScanner() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onScanResult(collection: SharkBleDeviceCollection?) {
            super.onScanResult(collection)
        }
    }

    fun startScanner() {
        mDevices.clear()
        mScanner.start(mContext, mCollection)
    }

    fun stopScanner() {
        val bluetoothAdapter =
                BluetoothAdapter.getDefaultAdapter()
        TrustLogUtils.d(TAG, "ble  log  ${bluetoothAdapter.state}")
        if (bluetoothAdapter.isEnabled && bluetoothAdapter.state == BluetoothAdapter.STATE_ON) {
            mScanner.stop()
        }
    }

    fun sendPing(ping: String, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {

            if (mIsConnection) {

                object : SharkBleCommands.CheckPinCode(ping) {
                    override fun onSuccess() {
                        super.onSuccess()
                        TrustLogUtils.d(TAG, "ble sendPing success")
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        TrustLogUtils.d(TAG, "ble sendPing error")
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        TrustLogUtils.d(TAG, "ble sendPing time out")
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)

            } else {
                conrollCallBack.resultControllTimeOutCallBack()
            }

        } else {
            conrollCallBack.resultControllTimeOutCallBack()
        }

    }

    fun writePassword(password: String, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {


            if (mIsConnection) {

                object : SharkBleCommands.WritePassword(password) {
                    override fun onSuccess() {
                        super.onSuccess()
                        TrustLogUtils.d(TAG, "ble writePassword success")
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        TrustLogUtils.d(TAG, "ble writePassword error")
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        TrustLogUtils.d(TAG, "ble writePassword time out")
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)


            }

        }
    }


    fun checkPassword(password: String, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        mPassword = password
        if (mIsControllBle) {

            if (mIsConnection) {
                object : SharkBleCommands.CheckPassword(password) {
                    override fun onSuccess() {
                        super.onSuccess()
                        showDebugToast("checkPassWord success")
                        TrustLogUtils.d(TAG, "ble checkPassWord success")
                        CONTROLL_STATUS = CONTROLL_CAR_BLE
                        dataAnalyCenter().sendLocalData(BLE_CHECK_PASS_WORD_SUCCESS)
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        showDebugToast("checkPassWord error")
                        TrustLogUtils.d(TAG, "ble checkPassWord error ")
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        showDebugToast("checkPassWord time out")
                        TrustLogUtils.d(TAG, "ble checkPassWord Time Out ")
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)
            }

        }
    }

    fun setPower(enable: Boolean, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {

        if (mIsControllBle) {


            if (mIsConnection) {

                object : SharkBleCommands.SetPower(enable) {
                    override fun onSuccess() {
                        super.onSuccess()
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)


            }


        }
    }

    fun findCar(conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {


            if (mIsConnection) {

                object : SharkBleCommands.FindCar() {
                    override fun onSuccess() {
                        super.onSuccess()
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)


            }

        }
    }

    fun setGuard(enable: Byte, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {
            if (mIsConnection) {
                object : SharkBleCommands.SetGuard(enable) {
                    override fun onSuccess() {
                        super.onSuccess()
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)


            }

        }
    }


    fun oneKeyStart(conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {


            if (mIsConnection) {

                object : SharkBleCommands.OneKeyStart() {
                    override fun onSuccess() {
                        super.onSuccess()
                        conrollCallBack.resultControllCallBack(true)
                    }

                    override fun onError() {
                        super.onError()
                        conrollCallBack.resultControllCallBack(false)
                    }

                    override fun onTimeout() {
                        super.onTimeout()
                        conrollCallBack.resultControllTimeOutCallBack()
                    }
                }.sendAsync(mClient)
            }
        }
    }


    fun setSeatLock(enable: Boolean, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        if (mIsControllBle) {


            object : SharkBleCommands.SetSeatLock(enable) {
                override fun onSuccess() {
                    super.onSuccess()
                    conrollCallBack.resultControllCallBack(true)
                }

                override fun onError() {
                    super.onError()
                    conrollCallBack.resultControllCallBack(false)
                }

                override fun onTimeout() {
                    super.onTimeout()
                    conrollCallBack.resultControllTimeOutCallBack()
                }
            }.sendAsync(mClient)


        }
    }


    fun setBikeLock(enable: Boolean, conrollCallBack: BsBleCallBack.BsBleConrollCallBack) {
        object : SharkBleCommands.SetBikeLock(enable) {
            override fun onSuccess() {
                super.onSuccess()
                conrollCallBack.resultControllCallBack(true)
            }

            override fun onError() {
                super.onError()
                conrollCallBack.resultControllCallBack(false)
            }

            override fun onTimeout() {
                super.onTimeout()
                conrollCallBack.resultControllTimeOutCallBack()
            }
        }.sendAsync(mClient)
    }


    private var controll: SharkBleCommand? = null
    private var isActivate = false
    fun gprsActivate(conrollCallBack: BsBleCallBack.BsBleConrollCallBack? = null, dissConnction: BsBleCallBack.BsBleConrollCallBack.BsBleDissConnction?) {
        TrustLogUtils.d(TAG, "gprsActivate mIsControllBle $mIsControllBle  mIsConnection:$mIsConnection  isActivate:$isActivate ")
        if (mIsControllBle) {

            if (mIsConnection) {

                if (!isActivate) {
                    isActivate = true
                    controll = object : SharkBleCommands.GprsActivate() {
                        override fun onSuccess() {
                            super.onSuccess()
                            isActivate = false
                            conrollCallBack?.resultControllCallBack(true)
                            TrustLogUtils.d(TAG, "gprsActivate Success")
                        }

                        override fun onError() {
                            super.onError()
                            isActivate = false
                            conrollCallBack?.resultControllCallBack(false)
                            TrustLogUtils.d(TAG, "gprsActivate onError  conrollCallBack：$conrollCallBack")
                        }

                        override fun onTimeout() {
                            super.onTimeout()
                            isActivate = false
                            if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {
                                conrollCallBack?.resultControllTimeOutCallBack()
                            } else {
                                dissConnction?.resultDissConnection()
                            }
                            TrustLogUtils.d(TAG, "gprsActivate onTimeout")
                        }
                    }
                    controll?.sendAsync(mClient)
                }

            } else {
                isActivate = false
                conrollCallBack?.resultControllTimeOutCallBack()
            }

        }
    }

    fun getGprsActivateStatus(): Int? {
        return controll?.response?.get(1)?.toInt()
    }

    fun unBind() {
        object : SharkBleCommands.Unbind() {
            override fun onSuccess() {
                super.onSuccess()
                Observable.create(ObservableOnSubscribe<String> { emitter ->
                    //                    Toast.makeText(TrustAppUtils.getContext(),"BLE 设备 解绑 成功！",Toast.LENGTH_LONG).show()
                    emitter.onComplete()
                }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                        .subscribeOn(AndroidSchedulers.mainThread())//执行在io线程
                        .subscribe()

                TrustLogUtils.d(TAG, "Unbind Success")
            }

            override fun onError() {
                super.onError()
                Observable.create(ObservableOnSubscribe<String> { emitter ->
                    //                    Toast.makeText(TrustAppUtils.getContext(),"BLE 设备 解绑 失败！",Toast.LENGTH_LONG).show()
                    emitter.onComplete()
                }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                        .subscribeOn(AndroidSchedulers.mainThread())//执行在io线程
                        .subscribe()
                TrustLogUtils.d(TAG, "Unbind onError")
            }

            override fun onTimeout() {
                super.onTimeout()
                Observable.create(ObservableOnSubscribe<String> { emitter ->
                    //                    Toast.makeText(TrustAppUtils.getContext(),"BLE 设备 解绑 超时！",Toast.LENGTH_LONG).show()
                    emitter.onComplete()
                }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                        .subscribeOn(AndroidSchedulers.mainThread())//执行在io线程
                        .subscribe()
                TrustLogUtils.d(TAG, "Unbind onTimeout")
            }
        }.sendAsync(mClient)
    }


    interface BsBleCallBack {
        fun resultScannerCallBack(device: SharkBleDeviceInfo)
        fun resultConnectionStatus(status: Boolean)
        interface BsBleConrollCallBack {
            fun resultControllCallBack(isSuccess: Boolean, errorString: String? = null)
            fun resultControllTimeOutCallBack()
            interface BsBleDissConnction {
                fun resultDissConnection()
            }
        }
    }

    fun setBsBleScannerCallBack(callBack: BsBleCallBack): BsBleTool {
        mBsBleScannerCallBack = callBack
        return this
    }

    fun setIsControllBle(isControllBle: Boolean) {
        mIsControllBle = isControllBle
    }


    fun readElectironic(listener: BsBleCallBack.BsBleConrollCallBack){
        setElectironic(byteArrayOf(READ_STATUS),object :BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                dataAnalyCenter().sendLocalData(BLE_CONTROLL_BIKE_INFO)
                readElectironicCheck(listener,errorString)
            }

            override fun resultControllTimeOutCallBack() {
                listener.resultControllTimeOutCallBack()
            }
        })
    }

    fun controllElectironic(isOpen:Boolean,listener: BsBleCallBack.BsBleConrollCallBack){
        val byteArray = ByteArray(2)
        byteArray[0] = SETTING_STATUS
        byteArray[1] = if(isOpen) OPEN else CLOSE
        object : SharkBleCommands.Electronic(byteArray){
            override fun onSuccess() {
                super.onSuccess()
                listener.resultControllCallBack(true)
            }

            override fun onError() {
                super.onError()
                listener.resultControllCallBack(false)
            }

            override fun onTimeout() {
                super.onTimeout()
                listener.resultControllTimeOutCallBack()
            }
        }.sendAsync(mClient)
    }


    private fun readElectironicCheck(listener: BsBleCallBack.BsBleConrollCallBack,data:String?){
        if (mElectironic != null) {
            val byteArray = mElectironic?.response
            if (byteArray != null) {
                val b = byteArray[2]
                val result = b.toInt()
                getDbBleManger().setBleInfoElectironic(bikeId,result == OPEN.toInt())
                listener.resultControllCallBack(result == OPEN.toInt())
            }
        }

    }

    private fun setElectironic(byteArray: ByteArray,listener: BsBleCallBack.BsBleConrollCallBack){
        mElectironic = object : SharkBleCommands.Electronic(byteArray){
            override fun onSuccess() {
                super.onSuccess()
                listener.resultControllCallBack(true)
            }

            override fun onError() {
                super.onError()
                listener.resultControllCallBack(false)
            }

            override fun onTimeout() {
                super.onTimeout()
                listener.resultControllTimeOutCallBack()
            }
        }
        mElectironic?.sendAsync(mClient)
    }



    fun readCushionInduction(listener: BsBleCallBack.BsBleConrollCallBack){
        setCushionInduction(byteArrayOf(READ_STATUS),object :BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                readCushionInductionCheck(listener)
                dataAnalyCenter().sendLocalData(BLE_CONTROLL_BIKE_INFO)
            }

            override fun resultControllTimeOutCallBack() {
                listener.resultControllTimeOutCallBack()
            }
        })
    }

    fun controllCushionInduction(isOpen: Boolean,listener: BsBleCallBack.BsBleConrollCallBack){
        val byteArray = ByteArray(2)
        byteArray[0] = SETTING_STATUS
        byteArray[1] = if(isOpen) OPEN else CLOSE
        object : SharkBleCommands.CushionInduction(byteArray){
            override fun onSuccess() {
                super.onSuccess()
                listener.resultControllCallBack(true)
            }

            override fun onError() {
                super.onError()
                listener.resultControllCallBack(false)
            }

            override fun onTimeout() {
                super.onTimeout()
                listener.resultControllTimeOutCallBack()
            }
        }.sendAsync(mClient)
    }

    private fun readCushionInductionCheck(listener: BsBleCallBack.BsBleConrollCallBack){
        if (mCushionInduction != null) {
            val byteArray = mCushionInduction?.response
            if (byteArray != null) {
                val b = byteArray[2]
                val result = b.toInt()
                getDbBleManger().setBleInfoCushionInduction(bikeId,result == OPEN.toInt())
                listener.resultControllCallBack(result == OPEN.toInt())
            }
        }

    }

    private fun setCushionInduction(byteArray: ByteArray,listener: BsBleCallBack.BsBleConrollCallBack){
        mCushionInduction = object : SharkBleCommands.CushionInduction(byteArray){
            override fun onSuccess() {
                super.onSuccess()
                listener.resultControllCallBack(true)
            }

            override fun onError() {
                super.onError()
                listener.resultControllCallBack(false)
            }

            override fun onTimeout() {
                super.onTimeout()
                listener.resultControllTimeOutCallBack()
            }
        }
        mCushionInduction?.sendAsync(mClient)
    }
}