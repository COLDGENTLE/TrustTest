package com.sharkgulf.soloera.mvpview.controllcar

import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2019/1/4
 */
interface IControllCarView :TrustView {

    /**
     *解防/设防
     */
    fun resultCarLock(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    /**
     * 一键启动
     */
    fun resultStartCar(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    /**
     * 开坐桶
     */
    fun resultOpenBucket(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    /**
     *开关电门
     */
    fun resultOpenOrCloseEle(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    /**
     * 寻车
     */
    fun resultFindCar(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    fun resultBattryInfo(isDoublePower:Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?)
}