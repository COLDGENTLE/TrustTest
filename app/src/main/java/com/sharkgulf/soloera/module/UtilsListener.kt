package com.sharkgulf.soloera.module

import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/**
 *  Created by user on 2019/3/13
 */
interface UtilsListener {
    fun requestAppVersion(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsAppVersionInfoBean>)
    fun requestLostMode(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsLostModeBean>)
}