package com.sharkgulf.soloera.module.bind

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface

/*
 *Created by Trust on 2019/1/5
 */
interface BindCarModeListener {
    fun requestBindCar(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsBindCarBean>)

    fun requestGetBleSign(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsBleSignBean>)

    fun requestUpdateBindStatus(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsUpdateBindStatusBean>)

    fun requestUpdateCarInfo(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsUpdateCarInfoBean>)

    fun requestGetBindInfo(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsGetBindInfoBean>)


}