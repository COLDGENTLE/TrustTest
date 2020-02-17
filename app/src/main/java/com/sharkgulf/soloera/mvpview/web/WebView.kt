package com.sharkgulf.soloera.mvpview.web

import com.sharkgulf.soloera.module.bean.BsOrderInfoBean
import com.sharkgulf.soloera.module.bean.BsOrderStatusBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/5/28
 */
interface WebView :TrustView{
    fun resultTicket(bean: BsTicketBean?)
    fun resultOrderInfo(bean: BsOrderInfoBean?)
    fun resultOrderStatus(bean: BsOrderStatusBean?)
}