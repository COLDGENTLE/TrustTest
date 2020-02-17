package com.sharkgulf.soloera.module.bind

import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface

/*
 *Created by Trust on 2019/1/5
 */
class BindCarMode :HttpModel(), BindCarModeListener  {
    override fun requestGetBindInfo(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsGetBindInfoBean>) {
        sendRrequest(TrustAppConfig.URL_GET_BIND_INFO,hashMap!!, BsGetBindInfoBean::class.java,resultInterface)
    }

    override fun requestBindCar(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsBindCarBean>) {
        sendRrequest(TrustAppConfig.URL_BIND_CAR,hashMap!!, BsBindCarBean::class.java,resultInterface)
    }

    override fun requestGetBleSign(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsBleSignBean>) {
        sendRrequest(TrustAppConfig.URL_GET_BLE_SIGN,hashMap!!, BsBleSignBean::class.java,resultInterface)
    }

    override fun requestUpdateBindStatus(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsUpdateBindStatusBean>) {
        sendRrequest(TrustAppConfig.URL_UPDATE_BIND_INRO,hashMap!!, BsUpdateBindStatusBean::class.java,resultInterface)
    }

    override fun requestUpdateCarInfo(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsUpdateCarInfoBean>) {
        sendRrequest(TrustAppConfig.URL_UPDATE_CAR_INFO,hashMap!!, BsUpdateCarInfoBean::class.java,resultInterface)
    }
}