package com.sharkgulf.soloera.module.home

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/*
 *Created by Trust on 2019/1/2
 */
interface HomeModeListener {
    /**
     * 获取 用户信息
     */

    fun getUserInfo(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetUserInfoBean>)

    /**
     * 获取车辆信息
     */
    fun getCarInfo(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsGetCarInfoBean>)

    /**
     * 当前用户退出
     */
    fun userExt(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsUserExtBean>)

    /**
     * 删除车辆
     */
    fun delCar(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsDelCarBean>)

    fun updatePushId(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsPushIdBean>)

}