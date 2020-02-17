package com.sharkgulf.soloera.loging

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.UserProtocolActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.login.ILoginView
import com.sharkgulf.soloera.presenter.login.LoginPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.checkPhone
import com.sharkgulf.soloera.tool.config.setBtnIsclick
import com.sharkgulf.soloera.tool.config.userPrivateStart
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_three_login.*
import kotlinx.android.synthetic.main.tite_layout.*

class ThreeLoginActivity : TrustMVPActivtiy<ILoginView,LoginPresenter>(),ILoginView {


    private var mPhone:String? = null
    private val TAG = ThreeLoginActivity::class.java.canonicalName
    companion object {
        var mdata:HashMap<String, String>? = null
        fun startActivity(context: Context,data:HashMap<String, String>?){
            if (data != null) {
                mdata = data
                context.startActivity(Intent(context,ThreeLoginActivity::class.java))
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_three_login
    }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(activity_threeligin_submint_btn)
        baseSetOnClick(title_back_btn)
        title_tx.text =  getString(R.string.bind_phone_tx)


        userPrivateStart(activity_threeligin_protocol_tx,R.color.colorRead,this,activity_threeligin_protocol_checkbox)
        setBtnIsclick(activity_threeligin_submint_btn,false)
        activity_threeligin_phone_ed.addTextChangedListener(editclick)
    }

    /**
     * 跳转协议页面
     */
    private fun intentProtocol(type:String){
        val intent = Intent(mActivity, UserProtocolActivity::class.java)
        intent.putExtra(WEB_INTENT_USER_TYPE_KEY,type)
        mActivity?.startActivity(intent)
    }

    override fun initData() {
        TrustLogUtils.d("获取到的第三方信息 ${mdata.toString()}")
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.activity_threeligin_submint_btn -> {
                requestBindPhone()
            }
            R.id.title_back_btn -> {
                finish()
            }
            else -> {
            }
        }
    }

    private fun requestBindPhone() {
        mPhone = activity_threeligin_phone_ed.text.toString()
        if (getCheckBox()) {
            if (checkPhone(mPhone,getString(R.string.login_fragment_phone_error))) {
                getPresenter()?.getCheckUserThree(RequestConfig.requestCheckUserThree(mPhone!!,mdata!!))
            }
        }else{
            showToast(getString(R.string.please_agress_tx))
        }


    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        mdata = null
    }


    override fun resultPhoneLoginCallBack(bean: BsGetSmsBean?) {}

    override fun resultPwdLoginCallBack(bean: BsLoginBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {}

    override fun resultCheckUserThree(bean: BsUserThressBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            userPhone = mPhone
            //三方跟手机没有绑定过
           LogingActivity.startActivity(this,CHECK_THERE, mdata,mPhone!!)
        }else{

        }
    }

    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {

        }
    }

    private fun getCheckBox():Boolean{
        return activity_threeligin_protocol_checkbox.isChecked
    }


    private val editclick  = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length == 11) {
                setBtnIsclick(activity_threeligin_submint_btn,true)
            }else{
                setBtnIsclick(activity_threeligin_submint_btn,false)
            }
        }

    }

}
