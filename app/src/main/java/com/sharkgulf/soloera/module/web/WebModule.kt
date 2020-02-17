package com.sharkgulf.soloera.module.web

import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsOrderInfoBean
import com.sharkgulf.soloera.module.bean.BsOrderStatusBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/5/28
 */
class WebModule: HttpModel(),WebModuleListener {


    override fun getOrderInfo(map: HashMap<String, Any>, moduleResultInterface: ModuleResultInterface<BsOrderInfoBean>) {
        sendRrequest(URL_GET_ORDER_INFO,map, BsOrderInfoBean::class.java,moduleResultInterface)
    }

    override fun getTicket(map: HashMap<String, Any>, moduleResultInterface: ModuleResultInterface<BsTicketBean>) {
        sendRrequest(URL_GET_TICKET,map, BsTicketBean::class.java,moduleResultInterface)
    }

    override fun getOrderStatus(map: HashMap<String, Any>, moduleResultInterface: ModuleResultInterface<BsOrderStatusBean>) {
        sendRrequest(URL_GET_PAY_STATUS,map, BsOrderStatusBean::class.java,moduleResultInterface)
    }


}