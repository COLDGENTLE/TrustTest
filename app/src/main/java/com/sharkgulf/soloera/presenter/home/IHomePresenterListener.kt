package com.sharkgulf.soloera.presenter.home

/*
 *Created by Trust on 2019/1/2
 */
interface IHomePresenterListener {
    /**
     * 获取 用户信息
     */

    fun getUserInfo(map:HashMap<String,Any>)

    /**
     * 获取车辆信息
     */
    fun getCarInfo(map:HashMap<String,Any>)

    /**
     * 当前用户退出
     */
    fun userExt(map:HashMap<String,Any>)

    /**
     * 删除车辆
     */
    fun delCar(map:HashMap<String,Any>)

    /**
     * 获取车辆对应的蓝牙信息
     */
    fun getBleInfo(map: HashMap<String, Any>)

    //上传推送id
    fun updatePushId(map: HashMap<String, Any>)

    //
    fun getAlertList(map: HashMap<String,Any>)
}