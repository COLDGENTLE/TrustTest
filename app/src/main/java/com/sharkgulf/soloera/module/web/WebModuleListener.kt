package com.sharkgulf.soloera.module.web

import com.sharkgulf.soloera.module.bean.BsOrderInfoBean
import com.sharkgulf.soloera.module.bean.BsOrderStatusBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/5/28
 */
interface WebModuleListener {
    fun getTicket(map:HashMap<String,Any>,moduleResultInterface: ModuleResultInterface<BsTicketBean>)
    fun getOrderInfo(map:HashMap<String,Any>,moduleResultInterface: ModuleResultInterface<BsOrderInfoBean>)
    fun getOrderStatus(map:HashMap<String,Any>,moduleResultInterface: ModuleResultInterface<BsOrderStatusBean>)
}