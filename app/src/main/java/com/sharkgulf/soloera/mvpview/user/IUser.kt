package com.sharkgulf.soloera.mvpview.user

import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/2/23
 */
interface IUser :TrustView{
    fun resultUploadfile(bean: BsUploadFileBean?)
    fun resultDeleteCar(bean: BsDeleteCarBean?)
    fun resultUserExt(bean:BsUserExtBean?)
    fun resultEditUserInfo(bean: BsEditUserInfoBean?)
    fun resultEditPwd(bean:BsSetPwdBean?)
    fun resultUserKey(bean:BsGetUserKeyBean?)
    fun resultCheckUserIsResiger(bean:BsCheckUserRegisterBean?)
    fun resultUserResiger(bean:BsUserRegisterKeyBean?)
    fun resultUserInfo(bean:BsGetUserInfoBean?)
    fun resultCheckinStatus(bean:BsCheckinStatusBean?)
    fun resultPointInFo(bean:BsPointinfoBean?)
    fun resultPointDetail(bean:BsPointDetailBean?)
    fun resultCheckinDaily(bean:BsCheckinDailyBean?)
    fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?)
    fun resultCarInfo(bean: CarInfoBean?)
    fun resultUpdatePhone(bean:BsHttpBean?)
    fun resultPhoneLogin(bean: BsGetSmsBean?)
    fun resultBleStatus(isconnection:Boolean)
}