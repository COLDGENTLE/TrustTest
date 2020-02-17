package com.sharkgulf.soloera.loging.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.view.Gravity
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.login.ILoginView
import com.sharkgulf.soloera.presenter.login.LoginPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.BeanUtils.Companion.CODE_DATA_SUCCESS
import com.sharkgulf.soloera.tool.BeanUtils.Companion.CODE_DATA_USER_IS_REGISTER
import com.sharkgulf.soloera.tool.config.setBtnIsclick
import com.sharkgulf.soloera.tool.config.userPrivateStart
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.trust.utils.TrustStringUtils
import kotlinx.android.synthetic.main.fragment_loging_reset_pwd.*
import razerdp.basepopup.BasePopupWindow

/*
 *Created by Trust on 2018/12/17
 * 修改密码之手机号申请验证码
 */
class LoginResetPwdFragment :TrustMVPFragment<ILoginView,LoginPresenter>(), ILoginView {
    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {}

    override fun resultCheckUserThree(bean: BsUserThressBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {}


    private var phoneStr:String = ""
    private val TAG = LoginResetPwdFragment::class.java.canonicalName

    fun setType(type:Int){ uiTypeStatus = type }

    override fun getLayoutId(): Int { return R.layout.fragment_loging_reset_pwd }

    @SuppressLint("NewApi")
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        if (uiTypeStatus == PWD_REGISTER) {
            fragment_loging_reset_protocol_layout.visibility = View.VISIBLE
        }else{
            fragment_loging_reset_protocol_layout.visibility = View.GONE
        }
        baseSetOnClick(fragment_loging_reset_pwd_next_btn)

        userPrivateStart(fragment_loging_reset_protocol_tx,R.color.colorRead,mActivity!!,fragment_loging_reset_protocol_checkbox)
        TrustLogUtils.d(TAG,"fragment_loging_reset_protocol_checkbox.isChecked ${fragment_loging_reset_protocol_checkbox.isChecked}")
    }


    override fun initData() {
        setBtnIsclick(fragment_loging_reset_pwd_next_btn, false)
        fragment_loging_reset_pwd_phone_ed.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 11) {
                    setBtnIsclick(fragment_loging_reset_pwd_next_btn, true)
                }else{
                    setBtnIsclick(fragment_loging_reset_pwd_next_btn, false)
                }
            }
        })
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }



    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.fragment_loging_reset_pwd_next_btn) {
            phoneStr = fragment_loging_reset_pwd_phone_ed.text.trim().toString()

            if (uiTypeStatus == PWD_REGISTER) {
                if (!getCheckBox()) {
                    showToast(getString(R.string.please_agress_tx))
                    return
                }
            }


            if (  !checkStringIsNullAndShowMsg(phoneStr,getString(R.string.login_fragment_phone_error)) && TrustStringUtils.isPhoneNumberValid(phoneStr)) {
                val map = hashMapOf<String,Any>()
                if (checkUserRegister()) {
                    map["type"] = USER_REGISTER_PHONE
                    map["phone_num"] = phoneStr
                    getPresenter()!!.checkUserRegister(map)
                }else{//忘记密码
                    val loginResetPwdVerificationFragment = LoginResetPwdVerificationFragment()
                    loginResetPwdVerificationFragment.setPhone(phoneStr)
                    chooseFragment(loginResetPwdVerificationFragment)
                }
            }else{
                resultError(getString(R.string.login_fragment_phone_error))
            }
        }
    }




    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    @SuppressLint("NewApi")
    fun chooseFragment(fragment: Fragment){
        val changeBounds = ChangeBounds()
        changeBounds.duration = 1000
        fragment.sharedElementEnterTransition = changeBounds
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.change_pwd_framelayout, fragment)
                .addToBackStack(null)
//                .addSharedElement(fragment_login_logo_img,"logo")
                .commit()
    }


    override fun resultPhoneLoginCallBack(bean: BsGetSmsBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){ }
    }

    override fun resultPwdLoginCallBack(bean: BsLoginBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) { }
    }

    /**
     * 检查 是 注册 还是忘记密码 逻辑不同
     * true 注册
     * false 忘记密码
     */
    private fun checkUserRegister():Boolean{ return uiTypeStatus == PWD_REGISTER }

    override fun resultUserKey(bean: BsGetUserKeyBean?) {
        var userBean: DbUserLoginStatusBean? = BsApplication.bsDbManger!!.checkUserLoginStatus(phoneStr)
        if (userBean != null) {
            userBean.userSalt = bean?.getData()?.salt
        }else{
            userBean = DbUserLoginStatusBean()
            userBean.userPhone = phoneStr
            userBean.userSalt =  bean?.getData()?.salt
        }
        BsApplication.bsDbManger?.setUserLoginStatus(userBean)
    }

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {
        val checkResultData = BeanUtils.checkResultData(bean?.getState()!!, bean.getState_info()!!, this)
        if (checkResultData==CODE_DATA_USER_IS_REGISTER) {
            showPopuwindow()
        }else if(checkResultData == CODE_DATA_SUCCESS){
            val loginResetPwdVerificationFragment = LoginResetPwdVerificationFragment()
            loginResetPwdVerificationFragment.setPhone(phoneStr)
            chooseFragment(loginResetPwdVerificationFragment)
        }
    }

    private var mPopupGravity: BasePopupWindow? = null
    private fun showPopuwindow(){
        mPopupGravity = TrustBasePopuwindow.getPopuwindow(mContext!!, R.layout.popupwindow_choose_register_loging_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                v!!.findViewById<View>(R.id.popu_cancel_btn).setOnClickListener {
                    userPhone = phoneStr
                    mPopupGravity?.dismiss()
                    mActivity?.finish()
                }
                return v
            }
        }).setPopupGravity(Gravity.CENTER)
        mPopupGravity?.showPopupWindow()

    }


    private fun getCheckBox():Boolean{
        return fragment_loging_reset_protocol_checkbox.isChecked
    }
}