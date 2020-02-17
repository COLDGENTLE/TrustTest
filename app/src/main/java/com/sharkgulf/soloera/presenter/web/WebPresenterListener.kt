package com.sharkgulf.soloera.presenter.web

/**
 *  Created by user on 2019/5/28
 */
interface WebPresenterListener {

    fun getTicket(hashMap: HashMap<String,Any>? = null)
    fun getOrderInfo(hashMap: HashMap<String, Any>? = null)
    fun getOrderStatus(hashMap: HashMap<String, Any>? = null)
}