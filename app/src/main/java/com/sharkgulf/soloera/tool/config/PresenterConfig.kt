package com.sharkgulf.soloera.tool.config

import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean

/**
 *  Created by user on 2019/11/21
 */

fun presenterGetCarInfo():PresenterGetCarInfoBean {
    val carInfoData = getCarInfoData(TrustAppConfig.bikeId)
    var mode:Int? =  null
    val pair = getStr(carInfoData?.security?.mode)
    val str = pair.first
    mode = pair.second
    return PresenterGetCarInfoBean(carInfoData,str,mode)
}

fun getStr(mode: Int?): Pair<String, Int?> {
    var mode1 = mode
    val str =
        if (mode1 == SECUTITY_ALERT) {
            "警戒模式已开启"
        } else if (mode1 == SECUTITY_DO_NOT_DISTURB) {
            "勿扰模式已开启"
        } else {
            "未知模式$mode1"
        }
    return Pair(str, mode1)
}

class PresenterGetCarInfoBean(var carInfo:CarInfoBean?,var str:String,var mode:Int?)