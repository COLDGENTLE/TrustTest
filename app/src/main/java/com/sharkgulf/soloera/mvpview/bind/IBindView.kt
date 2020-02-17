package com.sharkgulf.soloera.mvpview.bind

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2019/1/5
 */
interface IBindView:TrustView {

    fun resultBindCar(bean:BsBindCarBean?)

    fun resultGetBleSign(bean:BsBleSignBean?)

    fun resultUpdateBindStatus(bean: BsUpdateBindStatusBean?)

    fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?)

    fun resultGetBindInfo(bean:BsGetBindInfoBean?)

    fun showSeachBtn()

    fun resultDeviceStatus(status:Boolean,msg:String?)
}