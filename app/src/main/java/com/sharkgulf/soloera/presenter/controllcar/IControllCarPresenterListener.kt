package com.sharkgulf.soloera.presenter.controllcar

/*
 *Created by Trust on 2019/1/4
 */
interface IControllCarPresenterListener {

    /**
     *解防/设防
     */
    fun requestCarLock(actionType:Int,hashMap: HashMap<String,Any>? = null)

    /**
     * 一键启动
     */
    fun requestStartCar(actionType:Int,hashMap: HashMap<String,Any>? = null)

    /**
     * 开坐桶
     */
    fun requestOpenBucket(actionType:Int,hashMap: HashMap<String,Any>? = null)

    /**
     *开关电门
     */
    fun requestOpenOrCloseEle(actionType:Int,hashMap: HashMap<String,Any>? = null)

    /**
     * 寻车
     */
    fun requestFindCar(actionType:Int,hashMap: HashMap<String,Any>? = null)

    fun setTAG(tag:String)
}