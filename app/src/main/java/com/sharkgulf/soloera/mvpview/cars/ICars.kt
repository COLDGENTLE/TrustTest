package com.sharkgulf.soloera.mvpview.cars

import com.sharkgulf.soloera.module.bean.BsDeleteCarBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/3/7
 */
interface ICars :TrustView{
    fun resultDeleteCar(bean: BsDeleteCarBean?)
    fun resultCarInfo(bean: BsGetCarInfoBean?)
}