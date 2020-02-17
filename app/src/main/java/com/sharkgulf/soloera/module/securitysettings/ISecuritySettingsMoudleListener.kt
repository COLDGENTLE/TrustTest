package com.sharkgulf.soloera.module.securitysettings

import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/8/19
 */
interface ISecuritySettingsMoudleListener {
    fun changeSecurity(map:HashMap<String,Any>,moduleResultInterface: ModuleResultInterface<BsSecuritySettingsBean>)
    fun testNotify(map:HashMap<String,Any>,moduleResultInterface: ModuleResultInterface<BsTestNotifyBean>)
}