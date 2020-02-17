package com.sharkgulf.soloera.module.map

import com.sharkgulf.soloera.TrustAppConfig.URL_GET_CAR_LOCATION
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.trust.demo.basis.base.ModuleResultInterface

/*
 *Created by Trust on 2018/12/26
 */
class MapHttpMode :HttpModel() ,MapModelListener{

    override fun requestCarInfo(map:HashMap<String,Any>,moduleResultInterface:ModuleResultInterface<BsGetCarLocationBean>) {
        sendRrequest(URL_GET_CAR_LOCATION,map,BsGetCarLocationBean::class.java,moduleResultInterface)
    }
}