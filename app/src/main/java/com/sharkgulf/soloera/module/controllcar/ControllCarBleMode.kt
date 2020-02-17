package com.sharkgulf.soloera.module.controllcar

import com.google.gson.Gson
import com.shark.sharkbleclient.SharkBleCommands
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2019/1/4
 */
class ControllCarBleMode :ControllCarModeListener{
    override fun requestLoseBike(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
    }

    override fun requestControllBikeLock(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
    }

    private val TAG = ControllCarBleMode::class.java.canonicalName
    override fun requestCarLock(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        val byte = when (actionType) {
            ACTION_OPEN -> {
                SharkBleCommands.SetGuard.ENABLE
            }

            ACTION_CLOSE -> {
                SharkBleCommands.SetGuard.DISABLE
            }

            ACTION_NO_MUSIC_CLOSE -> {
                SharkBleCommands.SetGuard.MUTE_ENABLE
            }
            else -> {
                SharkBleCommands.SetGuard.DISABLE
            }
        }
        setGuard(byte, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {

            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                var bikeStatusBody:BikeStatusBean.BodyBean? = null
                val msg = if (isSuccess) {
                    bikeStatusBody = getBikeStatus()
                    when (actionType) {
                        ACTION_OPEN -> {
                             changeLock(checkBean(bikeStatusBody),CONTROLL_CAR_LOCK)
                            ACTION_SUCCESS
                        }
                        ACTION_CLOSE -> {
                             changeLock(checkBean(bikeStatusBody),CONTROLL_CAR_UNLOCK)

                            ACTION_SUCCESS
                        }
                        ACTION_NO_MUSIC_CLOSE -> {
                            changeLock(checkBean(bikeStatusBody),CONTROLL_CAR_NO_MUSIC_LOCK)
                            ACTION_SUCCESS
                        }
                        else -> {
                            "未知指令"
                            ACTION_SUCCESS
                        }
                    }

                } else {
                    when (actionType) {
                        ACTION_OPEN -> {
                            "车辆设防失败"
                            ACTION_ERROR
                        }
                        ACTION_CLOSE -> {
                            "车辆撤防失败"
                            ACTION_ERROR
                        }
                        ACTION_NO_MUSIC_CLOSE -> {
                            "静音设防失败"
                            ACTION_ERROR
                        }
                        else -> {
                            "未知指令"
                            ACTION_ERROR
                        }
                    }
                }
                resultInterface.resultData(msg,null)
            }

            override fun resultControllTimeOutCallBack() {
                val str = when (actionType) {
                    ACTION_OPEN -> {
                        "车辆设防超时，请重试"
                        ACTION_TIME_OUT
                    }
                    ACTION_CLOSE -> {
                        "车辆撤防超时，请重试"
                        ACTION_TIME_OUT
                    }
                    ACTION_NO_MUSIC_CLOSE -> {
                        "静音设防超时"
                        ACTION_TIME_OUT
                    }
                    else -> {
                        "未知指令"
                        ACTION_TIME_OUT
                    }
                }
                resultInterface.resultData(str)
            }
        })



//        mResultInterface = resultInterface
//        mHelper.requestLock(object : BsBleEVBHelper.ActionCallBack{
//            override fun actionSuccessCallBack(data: ByteArray?) {
//                resultInterface.resultData("操作成功")
//            }
//
//            override fun actionErrorCallBack() {
//                resultInterface.resultError("操作失败")
//            }
//        })


    }

    private fun changeLock(bikeStatusBody: BikeStatusBean.BodyBean,status:Int) {
        if ( bikeStatusBody != null) {
            bikeStatusBody.defence = status
            send(bikeStatusBody)
        }
    }

    private fun checkBean(bikeStatusBody: BikeStatusBean.BodyBean?):BikeStatusBean.BodyBean{
        return if (bikeStatusBody == null) {
            val bodyBean = BikeStatusBean.BodyBean()
            bodyBean.bike_id = bikeId
            bodyBean
        }else{
            bikeStatusBody
        }

    }

    private fun changeStartCar(bikeStatusBody: BikeStatusBean.BodyBean?) {
        if ( bikeStatusBody != null) {
            bikeStatusBody.defence = CONTROLL_CAR_UNLOCK
            bikeStatusBody.acc = CONTROLL_CARS_STATUS_ACC_ON
            send(bikeStatusBody)
        }
    }

    private fun changeBucket(bikeStatusBody: BikeStatusBean.BodyBean?){
        if ( bikeStatusBody != null) {
            bikeStatusBody.sstatus = CONTROLL_CAR_BUCKET_OPEN
            send(bikeStatusBody)
        }
    }

    override fun requestStartCar(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        oneKeyStart(object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                var bikeStatusBody:BikeStatusBean.BodyBean? = null

                val msg = if (isSuccess) {
                    bikeStatusBody = getBikeStatus()
                    changeStartCar(checkBean(bikeStatusBody))
                    "车辆启动成功，电门已打开"
                    ACTION_SUCCESS
                } else {
                    "车辆启动失败"
                    ACTION_ERROR
                }
                resultInterface.resultData(msg)
            }

            override fun resultControllTimeOutCallBack() {
                resultInterface.resultData(ACTION_TIME_OUT)
            }
        })
    }

    override fun requestOpenBucket(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        setSeatLock(true, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                val msg = if (isSuccess) {
                    changeBucket(getBikeStatus())
                    "车辆座桶已打开"
                    ACTION_SUCCESS
                } else {
                    "车辆座桶打开失败"
                    ACTION_ERROR
                }
                resultInterface.resultData(msg)
            }

            override fun resultControllTimeOutCallBack() {
                resultInterface.resultData(ACTION_TIME_OUT)
            }
        })

//        mHelper.requestOpenSeat(object : BsBleEVBHelper.ActionCallBack{
//            override fun actionSuccessCallBack(data: ByteArray?) {
//                resultInterface.resultData("操作成功")
//            }
//
//            override fun actionErrorCallBack() {
//                resultInterface.resultError("操作失败")
//            }
//        })
    }

    override fun requestOpenOrCloseEle(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        setPower(actionType == ACTION_OPEN, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                val msg = if (isSuccess) {
                    if (actionType == ACTION_OPEN) {
                        "车辆电门已打开"
                        ACTION_SUCCESS
                    } else {
                        "车辆电门已关闭"
                        ACTION_SUCCESS
                    }
                } else {
                    if (actionType == ACTION_OPEN) {
                        "车辆电门打开失败"
                        ACTION_ERROR
                    } else {
                        "车辆电门关闭失败"
                        ACTION_ERROR
                    }
                }
                resultInterface.resultData(msg)
            }

            override fun resultControllTimeOutCallBack() {
                val str = if (actionType == ACTION_OPEN) {
                    "车辆打开电门超时，请重试"
                } else {
                    "车辆电门关闭超时"
                }
                resultInterface.resultData(ACTION_TIME_OUT)
            }
        })

//        if(actionType==ACTION_OPEN){
//            mHelper.requestOpenAcc(object : BsBleEVBHelper.ActionCallBack{
//                override fun actionSuccessCallBack(data: ByteArray?) {
//                    resultInterface.resultData("操作成功")
//                }
//
//                override fun actionErrorCallBack() {
//                    resultInterface.resultError("操作失败")
//                }
//            })
//        }else{
//            mHelper.requestCloseAcc(object : BsBleEVBHelper.ActionCallBack{
//                override fun actionSuccessCallBack(data: ByteArray?) {
//                    resultInterface.resultData("操作成功")
//                }
//
//                override fun actionErrorCallBack() {
//                    resultInterface.resultError("操作失败")
//                }
//            })
//        }

    }

    override fun requestFindCar(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>) {
        findCar(object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                val msg = if (isSuccess) {
                    "寻车指令已下发"
                    ACTION_SUCCESS
                } else {
                    "寻车指令下发失败"
                    ACTION_ERROR
                }
                resultInterface.resultData(msg)
            }

            override fun resultControllTimeOutCallBack() {
                resultInterface.resultData(ACTION_TIME_OUT)
            }
        })
    }

    override fun requestDelBle(actionType: Int, hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<Int>?) {
        setUnBind()
    }

    private fun send(bikeStatusBeanBody: BikeStatusBean.BodyBean?){
        TrustLogUtils.d(TAG,"bikeStatusBean :${bikeStatusBeanBody?.defence}   id:${bikeStatusBeanBody?.bike_id}")
        if (bikeStatusBeanBody != null) {
            val bikeStatusBean = BikeStatusBean()
            bikeStatusBean.setBody(bikeStatusBeanBody)
            dataAnalyCenter().sendLocalData(WEB_SOKECT_RECEIVE+WEB_CAR_STATUS,Gson().toJson(bikeStatusBean))
        }
    }


    fun readElectironic(){
        readElectironic(object :BsBleTool.BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                getDbBleManger().setBleInfoElectironic(bikeId,isSuccess)
                TrustLogUtils.d(TAG,"读取 电子侧撑结果 $isSuccess")
            }

            override fun resultControllTimeOutCallBack() {}

        })
    }

    fun controllElectironic(bid:Int,mac:String,isOpen:Boolean){
        controllElectironic(mac,isOpen,object :BsBleTool.BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                val result = if (isSuccess) {
                    isOpen
                }else{
                    !isOpen
                }
                getDbBleManger().setBleInfoElectironic(bid,result)
                sendLocationBleEletrion(errorString)
            }

            override fun resultControllTimeOutCallBack() {}

        })
    }

    fun readCushionInduction(){
        readCushionInduction(object :BsBleTool.BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                getDbBleManger().setBleInfoCushionInduction(bikeId,isSuccess)
                TrustLogUtils.d(TAG,"读取 坐垫感应 $isSuccess")
            }

            override fun resultControllTimeOutCallBack() {}

        })
    }

    fun controllCushionInduction(bid:Int,mac:String,isOpen: Boolean){
        controllCushionInduction(isOpen,object :BsBleTool.BsBleCallBack.BsBleConrollCallBack{
            override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                TrustLogUtils.d(TAG,"设置 坐垫感应 $isSuccess")
                val result = if (isSuccess) {
                    isOpen
                }else{
                    !isOpen
                }
                getDbBleManger().setBleInfoCushionInduction(bikeId,result)
                sendLocationBleCushionInduction(errorString)
            }

            override fun resultControllTimeOutCallBack() {

            }

        })
    }
}