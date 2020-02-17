package com.sharkgulf.soloera.module.controllcar

import com.trust.demo.basis.base.ModuleResultInterface

/*
 *Created by Trust on 2019/1/4
 */
interface ControllCarModeListener {
    /**
     *解防/设防
     */
    fun  requestCarLock(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     * 一键启动
     */
    fun requestStartCar(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     * 开坐桶
     */
    fun requestOpenBucket(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     *开关电门
     */
    fun requestOpenOrCloseEle(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     * 寻车
     */
    fun requestFindCar(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     * 解绑
     */
    fun requestDelBle(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>? = null)

    /**
     * 丢车
     */
    fun requestLoseBike(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

    /**
     * 锁、解车锁操作
     */
    fun requestControllBikeLock(actionType:Int,hashMap: HashMap<String,Any>? = null, resultInterface: ModuleResultInterface<Int>)

}