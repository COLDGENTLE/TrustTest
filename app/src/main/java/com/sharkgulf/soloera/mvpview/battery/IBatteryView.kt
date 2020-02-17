package com.sharkgulf.soloera.mvpview.battery

import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/3/5
 */
interface IBatteryView :TrustView {
    fun resultBatteryInfo(bean: BsBatteryInfoBean?)
    fun resultBattryInfo(isDoublePower:Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?)
}