package com.sharkgulf.soloera.mvpview.login

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.veiw.TrustView

/*
 *Created by Trust on 2018/12/10
 * 返回回调
 */
interface ILoginView : TrustView {
    fun resultPhoneLoginCallBack(bean: BsGetSmsBean?)
    fun resultPwdLoginCallBack(bean: BsLoginBean?)
    fun resultUserKey(bean:BsGetUserKeyBean?)
    fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?)
    fun resultUserInfo(bean: BsGetUserInfoBean?)
    fun resultCarInfo(bean: BsGetCarInfoBean?)
    fun resultCheckUserThree(bean:BsUserThressBean?)
    fun resultUserregister(bean:BsUserRegisterKeyBean?)
}