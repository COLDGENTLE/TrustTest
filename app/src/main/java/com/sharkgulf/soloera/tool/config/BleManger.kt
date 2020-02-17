package com.sharkgulf.soloera.tool.config

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/8/20
 */

@SuppressLint("StaticFieldLeak")
val mBsBleTool = BsBleTool.getInstance()
val TAG = "BleManger"
fun bleStartScanner() {
    mBsBleTool.startScanner()
}

fun bleReadDeviceStatus(): Int? {
    return mBsBleTool.readDeviceStatus()
}

fun bleStopScanner() {
    mBsBleTool.stopScanner()
}

//只连接
fun bleConnection(address: String? = null, id: String) {
    mBsBleTool.connection(address, id)
}

fun bleDisConnection() {
    mBsBleTool.disConnection()
}

fun getBleId(): String? {
    return mBsBleTool.getBleId()
}

fun bleSetBsBleScannerCallBack(callBack: BsBleTool.BsBleCallBack) {
    mBsBleTool.setBsBleScannerCallBack(callBack)
}

fun blesetIsControllBle(isControllBle: Boolean) {
    mBsBleTool.setIsControllBle(isControllBle)
}

fun bleSetIsReConnection(isReconnection: Boolean) {
    mBsBleTool.setIsReConnection(isReconnection)
}

fun bleSendPing(ping: String, password: String, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    TrustLogUtils.d(TAG, "Ping $ping PassWord $password")
    blesetIsControllBle(true)
    mBsBleTool.sendPing(ping, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
        override fun resultControllTimeOutCallBack() {
            conrollCallBack.resultControllCallBack(false, "Ping码超时!")
        }

        override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
            if (isSuccess) {
                writePassword(password, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
                    override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                        if (isSuccess) {
                            checkPassword(password, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
                                override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                                    conrollCallBack.resultControllCallBack(isSuccess)
                                }

                                override fun resultControllTimeOutCallBack() {
                                    conrollCallBack.resultControllTimeOutCallBack()
                                }

                            })
                        } else {
                            conrollCallBack.resultControllCallBack(false, "鉴权失败")
                        }
                    }

                    override fun resultControllTimeOutCallBack() {
                        conrollCallBack.resultControllCallBack(false, "鉴权超时")
                    }

                })
            } else {
                conrollCallBack.resultControllCallBack(false, "PIN码校验失败\n" +
                        "此车可能已经超时退出待绑定状态，试试重新搜索添加此车!")
            }
        }

    })
}

private fun writePassword(password: String, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.writePassword(password, conrollCallBack)
}

fun checkPassword(password: String, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.checkPassword(password, conrollCallBack)
}

fun setBleConfig(address: String, id: String, passWord: String) {
    mBleId = id
    mBsBleTool.setBleConfig(address, id, passWord)
}

private var mBleId: String? = null


fun setPower(enable: Boolean, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.setPower(enable, conrollCallBack)
}

fun findCar(conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.findCar(conrollCallBack)
}

fun setGuard(enable: Byte, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.setGuard(enable, conrollCallBack)
}

fun oneKeyStart(conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.oneKeyStart(conrollCallBack)
}

fun setSeatLock(enable: Boolean, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.setSeatLock(enable, conrollCallBack)
}

fun setBikeLock(enable: Boolean, conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack) {
    mBsBleTool.setBikeLock(enable, conrollCallBack)
}

fun setUnBind() {
    mBsBleTool.unBind()
}

fun gprsActivate(conrollCallBack: BsBleTool.BsBleCallBack.BsBleConrollCallBack? = null, dissConnction: BsBleTool.BsBleCallBack.BsBleConrollCallBack.BsBleDissConnction? = null) {
    mBsBleTool.gprsActivate(conrollCallBack, dissConnction)
}

fun getGprsActivateStatus(): Int? {
    return mBsBleTool.getGprsActivateStatus()
}

private var mfilterStatus = false

private var mIsReGprsActivate = true

fun setIsReGprsActivate(isReGprsActivate: Boolean) {
    mIsReGprsActivate = isReGprsActivate
}


private var mMac: String = ""
//连接写 写密码
fun bleConnec(isConnection: Boolean, bleId: String, password: String) {
    mMac = bleId
    TrustLogUtils.d(TAG, " PassWord $password  bleId:$bleId  mBleId:$mBleId")
    mfilterStatus = false
    if (isConnection) {
        if (mBsBleTool.getCliecnt().isConnected) {
            bleDisConnection()
        }

        if (mBleId == null) {
            bleConnection(bleId, bleId)
        } else {
            bleConnection(mBleId, mBleId!!)
        }

        dataAnalyCenter().unRegisterCallBack(BLE_CONNECT_SUCCESS, TAG)
        dataAnalyCenter().registerCallBack(BLE_CONNECT_SUCCESS,
                object : DataAnalysisCenter.onDataAnalysisCallBack {
                    override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

                    override fun onNoticeCallBack(msg: String?) {
                        TrustLogUtils.d(TAG, "mfilterStatus : $mfilterStatus")
                        if (!mfilterStatus) {
                            TrustLogUtils.d(TAG, "check password  $password  bleId:$bleId   mMac :$mMac")
                            checkPassword(password, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
                                override fun resultControllTimeOutCallBack() {
                                    TrustLogUtils.d(TAG, "resultControllTimeOutCallBack password $password")
                                    TrustLogUtils.d(TAG, "resultControllTimeOutCallBack bleId $bleId")
                                    TrustLogUtils.d(TAG, "resultControllTimeOutCallBack mMac $mMac")
                                    if (mIsReGprsActivate) {
                                        mfilterStatus = false
                                        bleConnec(isConnection, bleId, password)
                                        bleConnection(bleId, bleId)
                                    }
                                    CONTROLL_STATUS = CONTROLL_CAR_INTERNET
                                }

                                override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                                    TrustLogUtils.d(TAG, "ble checkPassWord $isSuccess")
                                    CONTROLL_STATUS = if (isSuccess) {
                                        if (carStatus == CAR_STATUS_GPRS_NO_ACTIVATION) {
                                            showDebugToast("ble checkPassWord success")
                                            dataAnalyCenter().sendLocalData(BLE_CHECK_PASS_WORD_SUCCESS)
                                        }
                                        CONTROLL_CAR_BLE
                                    } else {
                                        dataAnalyCenter().sendLocalData(BLE_CONNECT_CLOSE)
                                        CONTROLL_CAR_INTERNET
                                    }

                                }
                            })
                            mfilterStatus = true
                        }

                    }
                }, TAG)
    }
}


fun setBleCallBack() {
    BsApplication.mBsApplication?.getService()?.setBleCallBack()
}


fun appStartBle(bikeId: Int? = null) {
    bleSetIsReConnection(true)
    if (bikeId != null) {
        val bleConfig = getDbManger().getBleConfig(bikeId)
        if (bleConfig != null) {
            setBleConfig(bleConfig.mac, bleConfig.mac, bleConfig.salt)
        }
        val hasSystemFeature = getContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        if (hasSystemFeature) {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter.state == BluetoothAdapter.STATE_OFF) {
                mBluetoothAdapter.enable() //开启
            } else {
                if (bleConfig != null) {
                    TrustLogUtils.d(TAG, "bleConfig != null: ${bleConfig.mac}")
                    bleDisConnection()
                    bleConnec(true, bleConfig.mac, bleConfig.salt)
                }
            }
        }

    }
}

fun readElectironic(listener: BsBleTool.BsBleCallBack.BsBleConrollCallBack){
    if (checkBleConnectionStatus()) {
        mBsBleTool.readElectironic(listener)
    }
}

fun controllElectironic(mac:String,isOpen:Boolean,listener: BsBleTool.BsBleCallBack.BsBleConrollCallBack){
    if (checkBleConnectionStatus()) {
        mBsBleTool.controllElectironic(isOpen,listener)
    }else{listener.resultControllCallBack(false,"请连接蓝牙!")}
}

fun readCushionInduction(listener: BsBleTool.BsBleCallBack.BsBleConrollCallBack){
    if (checkBleConnectionStatus()) {
        mBsBleTool.readCushionInduction(listener)
    }
}

fun controllCushionInduction(isOpen:Boolean,listener: BsBleTool.BsBleCallBack.BsBleConrollCallBack){
    if (checkBleConnectionStatus()) {
        mBsBleTool.controllCushionInduction(isOpen,listener)
    }else{listener.resultControllCallBack(false,"请连接蓝牙!")}
}

fun checkBleConnectionStatus():Boolean{
    return CONTROLL_STATUS == CONTROLL_CAR_BLE
}


