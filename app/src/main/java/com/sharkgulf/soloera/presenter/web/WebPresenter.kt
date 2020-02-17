package com.sharkgulf.soloera.presenter.web

import com.sharkgulf.soloera.module.bean.BsOrderInfoBean
import com.sharkgulf.soloera.module.bean.BsOrderStatusBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.sharkgulf.soloera.module.web.WebModule
import com.sharkgulf.soloera.module.web.WebModuleListener
import com.sharkgulf.soloera.mvpview.web.WebView
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters

/**
 *  Created by user on 2019/5/28
 */
class WebPresenter:TrustPresenters<WebView>(),WebPresenterListener {



    private var moudle:WebModuleListener? = null

    init {
        moudle = WebModule()
    }
    override fun getTicket(hashMap: HashMap<String, Any>?) {
        moudle?.getTicket(hashMap!!,object :ModuleResultInterface<BsTicketBean>{
            override fun resultData(bean: BsTicketBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultTicket(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view?.resultError(msg)
            }

        })
    }



    override fun getOrderInfo(hashMap: HashMap<String, Any>?) {
        moudle?.getOrderInfo(hashMap!!,object :ModuleResultInterface<BsOrderInfoBean>{
            override fun resultData(bean: BsOrderInfoBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultOrderInfo(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view?.resultError(msg)
            }
        })
    }


    override fun getOrderStatus(hashMap: HashMap<String, Any>?) {
        moudle?.getOrderStatus(hashMap!!,object :ModuleResultInterface<BsOrderStatusBean>{
            override fun resultData(bean: BsOrderStatusBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultOrderStatus(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view?.resultError(msg)
            }

        })
    }
}