package com.sharkgulf.soloera.module.battery

import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/3/5
 */
interface IBatteryModeListener {
    fun requestBatteryInfo(hashMap: HashMap<String,Any>,resultInterface: ModuleResultInterface<BsBatteryInfoBean>)
}