package com.sharkgulf.soloera.presenter.bind

import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean

/*
 *Created by Trust on 2019/1/5
 */
interface IBindCarPresenterListener {
    fun requestBindCar(hashMap: HashMap<String,Any>?)

    fun requestGetBleSign(hashMap: HashMap<String,Any>?)

    fun requestUpdateBindStatus(hashMap: HashMap<String,Any>?)

    fun requestUpdateCarInfo(hashMap: HashMap<String,Any>?,carInfoBean: CarInfoBean? = null)

    fun requestGetBindInfo(hashMap: HashMap<String,Any>?)

    fun startTiming()

    fun stopTiming()

    fun updatePhone(hashMap: HashMap<String,Any>)

    fun phoneLogin(map :HashMap<String, Any>)
}