package com.sharkgulf.soloera.module.battery

import com.sharkgulf.soloera.TrustAppConfig.URL_GET_POWER
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/3/5
 */
class BatteryMode :HttpModel() ,IBatteryModeListener{
    override fun requestBatteryInfo(hashMap: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsBatteryInfoBean>) {
        sendRrequest(URL_GET_POWER,hashMap,BsBatteryInfoBean::class.java,resultInterface)
    }
}