package com.sharkgulf.soloera.mvpview.login

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2018/12/10
 * 返回回调
 */
interface ILoginVerificationCodeView : TrustView {
    fun resultReacquireVerificationCodeCallBack(bean:BsLoginBean?)
    fun resultSubmintVerificationCodeCallBack(bean: BsLoginBean?)

    fun resultCapcha(bean:BsGetCapchaBean?)

    fun resultPhoneLogin(bean:BsGetSmsBean?)

    fun resultUserInfo(bean:BsGetUserInfoBean?)

     fun resultSetPwd(bean:BsSetPwdBean?)

    fun resultUserKey(bean:BsGetUserKeyBean?)

    fun resultUserRegisterKey(bean:BsUserRegisterKeyBean?)

    fun resultCheckSmsvc(bean: BsCheckSmsvcBean?)

    fun resultPwdLogin(bean: BsLoginBean?)

    fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?)

    /**
     * 获取车辆信息
     */
    fun resultCarInfo(bean: BsGetCarInfoBean?)

    fun resultUserregister(bean:BsUserRegisterKeyBean?)
}