package com.sharkgulf.soloera.module

import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.BsHttpBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.model.RequestResultListener

/**
 *  Created by user on 2019/9/9
 */
interface HttpModelListener {
    fun requestAlertList(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsAlertBean>)
    fun requestUpdate(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsHttpBean>)
    fun requestDownLoad(url:String,map: HashMap<String, Any>, range:Long,downLoadListener: RequestResultListener.DownLoadListener)

    fun requestDemoData(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsGetCarInfoBean>)

    fun requestUpdatePhoneAndThree(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsTicketBean>)
}