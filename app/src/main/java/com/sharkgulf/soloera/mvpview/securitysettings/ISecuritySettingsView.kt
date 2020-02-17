package com.sharkgulf.soloera.mvpview.securitysettings

import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/8/19
 */
interface ISecuritySettingsView :TrustView {
    fun resultChangeSecurity(bean: BsSecuritySettingsBean?)
    fun resultTestNotify(bean: BsTestNotifyBean?)
    fun resultLostModu(bean:BsLostModeBean?)
    fun resultCarModule(str:String,module:Int?,bean: CarInfoBean?)
    fun resultFilter()
}