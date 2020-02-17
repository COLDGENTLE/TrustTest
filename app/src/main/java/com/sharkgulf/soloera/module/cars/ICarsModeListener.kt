package com.sharkgulf.soloera.module.cars

import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/3/7
 */
interface ICarsModeListener {
    fun  requestCarInfo(hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<BsGetCarInfoBean>)
}