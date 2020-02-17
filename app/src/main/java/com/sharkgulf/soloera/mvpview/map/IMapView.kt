package com.sharkgulf.soloera.mvpview.map

import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2018/12/26
 */
interface IMapView :TrustView{
    fun resultCarInfo(bean: BsGetCarLocationBean?)
    fun resultCarModule(str:String,module:Int?)
    fun resultChangeSecurity(bean: BsSecuritySettingsBean?)
    /**
     * 寻车
     */
    fun resultFindCar(actionType:Int,msg:Int?,str:String? = null,isSuccess:Boolean?=null)

    fun resultLcoation(bean: CarLoctionBean?)
}