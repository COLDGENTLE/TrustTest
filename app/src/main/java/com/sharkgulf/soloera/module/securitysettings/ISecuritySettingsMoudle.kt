package com.sharkgulf.soloera.module.securitysettings

import com.sharkgulf.soloera.TrustAppConfig.URL_CHANGE_SECURITY
import com.sharkgulf.soloera.TrustAppConfig.URL_TEST_WARING_NOTIFY
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/8/19
 */
class ISecuritySettingsMoudle: HttpModel() ,ISecuritySettingsMoudleListener{


    override fun changeSecurity(map: HashMap<String, Any>, moduleResultInterface: ModuleResultInterface<BsSecuritySettingsBean>) {
        sendRrequest(URL_CHANGE_SECURITY,map,BsSecuritySettingsBean::class.java,moduleResultInterface)
    }

    override fun testNotify(map: HashMap<String, Any>, moduleResultInterface: ModuleResultInterface<BsTestNotifyBean>) {
        sendRrequest(URL_TEST_WARING_NOTIFY,map,BsTestNotifyBean::class.java,moduleResultInterface)
    }
}