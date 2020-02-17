package com.sharkgulf.soloera.home.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.trust.demo.basis.base.TrustMVPActivtiy

class EmailActivity : TrustMVPActivtiy<IUser, UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun getLayoutId(): Int { return  R.layout.activity_email }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initData() {}

    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun createPresenter(): UserPresenter {
        return UserPresenter()
    }
}
