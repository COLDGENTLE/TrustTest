package com.sharkgulf.soloera.mvpview.home

import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2019/1/2
 */
interface IHomeView : TrustView {

    /**
     * 获取 用户信息
     */

    fun resultUserInfo(bean: BsGetUserInfoBean?)

    /**
     * 获取车辆信息
     */
    fun resultCarInfo(bean: BsGetCarInfoBean?)

    /**
     * 退出登录
     */
    fun resultExt(bean: BsUserExtBean?)

    fun resultDelCar(bean:BsDelCarBean?)

    fun resultBleSign(bean:BsBleSignBean?)

    fun resultPushId(bean:BsPushIdBean?)

    fun resultIsRead(isRead:Boolean)

    fun resultBleStatus(isConnection:Boolean,isCheckPassWordSuccess:Boolean)

    fun resultBikeStatus(bean: BikeStatusBean.BodyBean?)

    fun resultDemoBikeInfo(bean: BsGetCarInfoBean?,dbBean:DbUserLoginStatusBean?)

    fun resultUpdateBikesInfo()
}