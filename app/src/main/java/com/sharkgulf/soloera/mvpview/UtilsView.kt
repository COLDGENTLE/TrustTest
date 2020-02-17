package com.sharkgulf.soloera.mvpview

import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/3/13
 */
interface UtilsView :TrustView {

    fun resultAppVersionInfo(bean:BsAppVersionInfoBean?)
}