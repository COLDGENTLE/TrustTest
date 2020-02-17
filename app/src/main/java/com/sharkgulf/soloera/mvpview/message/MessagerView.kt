package com.sharkgulf.soloera.mvpview.message

import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/9/9
 */
interface MessagerView:TrustView {
    fun resultAlertList(bean:BsAlertBean?)
    fun resultBsAlertList(bean: DbAlertBean?)
}