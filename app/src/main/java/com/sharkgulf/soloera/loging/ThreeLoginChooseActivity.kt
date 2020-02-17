package com.sharkgulf.soloera.loging

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.tool.config.DEFULT
import com.sharkgulf.soloera.tool.config.getAppConfig
import com.sharkgulf.trustthreeloginlibrary.UmThreeLoginConfig
import com.sharkgulf.trustthreeloginlibrary.umThreeLogin
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView

import kotlinx.android.synthetic.main.activity_three_login_choose.*

class ThreeLoginChooseActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_three_login_choose
    }
    private val mThreeLoginType = getAppConfig().getThreeLoginType()
    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(three_login_submint_btn)
        baseSetOnClick(three_login_other_submint_btn)
    }

    override fun initData() {
        if (mThreeLoginType != DEFULT) {

        }
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.three_login_submint_btn -> {
                 threeLogin()
            }
            R.id.three_login_other_submint_btn -> {

            }
            else -> {
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
    }


    private val ThreeListener = object : UmThreeLoginConfig.ThreeListener{
        override fun reusltError(error: String) {
            showToast(error)
        }


        override fun reusltImport(msg: String) {

        }

        override fun resultUserinfo(partner: String, data: MutableMap<String, String>) {
            data["partner"] = partner
//            getPresenter()?.checkUserRegister(RequestConfig.requestCheckUserRegister(3,mdata!!))
        }

    }

    private fun threeLogin(){
        if (mThreeLoginType != DEFULT) {
            when (mThreeLoginType) {
                LOGIN_WX -> { umThreeLogin(this).startWxLogin(mActivity!!,ThreeListener) }
                LOGIN_WB -> { umThreeLogin(this).startSinaLogin(mActivity!!,ThreeListener) }
                LOGIN_QQ -> { umThreeLogin(this).startQQLogin(mActivity!!,ThreeListener) }
            }
        }
    }
}
