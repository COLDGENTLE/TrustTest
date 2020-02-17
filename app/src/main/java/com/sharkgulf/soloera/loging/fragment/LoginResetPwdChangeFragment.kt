package com.sharkgulf.soloera.loging.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.PWD_REGISTER
import com.sharkgulf.soloera.TrustAppConfig.uiTypeStatus
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.loging.ChangePwdActivity.Companion.mStatus
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.login.ILoginVerificationCodeView
import com.sharkgulf.soloera.presenter.login.LoginVerificationCodePresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.getGPPopup
import com.sharkgulf.soloera.tool.config.setBtnIsclick
import com.sharkgulf.soloera.tool.config.setColor
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustMD5Utils
import kotlinx.android.synthetic.main.fragment_loging_reset_pwd_change.*
import razerdp.basepopup.BasePopupWindow

/*
 *Created by Trust on 2018/12/17
 * 修改密码页面
 */
class LoginResetPwdChangeFragment :TrustMVPFragment<ILoginVerificationCodeView, LoginVerificationCodePresenter>(), ILoginVerificationCodeView{

    private var phoneStr:String = ""
    private var firstPwd:String? = ""
    private var secondPwd:String? = ""
    private var salt:String = ""

    private var mPwd1:EditText? = null
    private var mPwd2:EditText? = null
    private var mPwd1Ic:ImageView? = null
    private var mPwd1ClearIc:ImageView? = null
    private var mPwd1ChangeTypeIc:ImageView? = null
    private var mPwd2Ic:ImageView? = null
    private var mPwd2ClearIc:ImageView? = null
    private var mPwd2ChangeTypeIc:ImageView? = null

    private var isShowPwd1 = false
    private var isShowPwd2 = false
    override fun getLayoutId(): Int {
        return R.layout.fragment_loging_reset_pwd_change
    }


    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(fragment_loging_reset_pwd_change_submint_btn)
        mPwd1  = fragment_loging_reset_pwd_change_first_pwd_ed.findViewById(R.id.bs_edit_text_ed)
        mPwd1?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        mPwd1?.addTextChangedListener(pwd1Listener)
        mPwd1Ic  = fragment_loging_reset_pwd_change_first_pwd_ed.findViewById(R.id.bs_edit_text_type_ic)
        mPwd1ClearIc  = fragment_loging_reset_pwd_change_first_pwd_ed.findViewById(R.id.bs_edit_text2_ic)
        mPwd1ClearIc?.setOnClickListener(on1ClickListener)
        mPwd1ClearIc?.visibility = View.VISIBLE
        mPwd1ChangeTypeIc  = fragment_loging_reset_pwd_change_first_pwd_ed.findViewById(R.id.bs_edit_text_ic)
        mPwd1ChangeTypeIc?.setColor(R.color.colorWhiteGay)
        mPwd1ChangeTypeIc?.setOnClickListener(on1ClickListener)
        mPwd1ChangeTypeIc?.setImageResource(R.drawable.ic_hint_pw)
        mPwd2  = fragment_loging_reset_pwd_change_second_pwd_ed.findViewById(R.id.bs_edit_text_ed)
        mPwd2?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        mPwd2?.addTextChangedListener(pwd2Listener)
        mPwd2Ic  = fragment_loging_reset_pwd_change_second_pwd_ed.findViewById(R.id.bs_edit_text_type_ic)
        mPwd2ClearIc  = fragment_loging_reset_pwd_change_second_pwd_ed.findViewById(R.id.bs_edit_text2_ic)
        mPwd2ClearIc?.setOnClickListener(on2ClickListener)
        mPwd2ClearIc?.visibility = View.VISIBLE
        mPwd2ChangeTypeIc  = fragment_loging_reset_pwd_change_second_pwd_ed.findViewById(R.id.bs_edit_text_ic)
        mPwd2ChangeTypeIc?.setColor(R.color.colorWhiteGay)
        mPwd2ChangeTypeIc?.setOnClickListener(on2ClickListener)
        mPwd2ChangeTypeIc?.setImageResource(R.drawable.ic_hint_pw)
        mPwd1?.hint = getString(R.string.pwd_insufficient_tx)
        mPwd2?.hint = getString(R.string.pwd_reenter_tx)
        mPwd1Ic?.setImageResource(R.drawable.pwd_ic)
        mPwd2Ic?.setImageResource(R.drawable.repwd_ic)
        setBtnIsclick(fragment_loging_reset_pwd_change_submint_btn, false)
    }

    private val on1ClickListener = View.OnClickListener { v ->
        if (v.id == R.id.bs_edit_text_ic) {
            mPwd1ChangeTypeIc?.setImageResource(if (isShowPwd1) {
                R.drawable.ic_hint_pw
            }else{
                R.drawable.ic_show_pw
            })
            isShowPwd1 = !isShowPwd1
            val type = if (isShowPwd1) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
            mPwd1?.inputType = InputType.TYPE_CLASS_TEXT or type
            mPwd1?.typeface = mPwd1?.typeface
            mPwd1?.setSelection(if(mPwd1?.text == null) 0 else mPwd1?.text!!.length)

        }else{//切换
            mPwd1?.setText("")

        }
    }

    private val on2ClickListener = View.OnClickListener {v->
        if (v.id == R.id.bs_edit_text_ic) {//
            mPwd2ChangeTypeIc?.setImageResource(if (isShowPwd2) {
                R.drawable.ic_hint_pw
            }else{
                R.drawable.ic_show_pw
            })
            isShowPwd2 = !isShowPwd2
            val type = if (isShowPwd2) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
            mPwd2?.inputType = InputType.TYPE_CLASS_TEXT or type
            mPwd2?.typeface = mPwd1?.typeface
            mPwd2?.setSelection(if(mPwd1?.text == null) 0 else mPwd2?.text!!.length)

        }else{//
            mPwd2?.setText("")

        }
    }

    private val pwd1Listener = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            firstPwd = s.toString()
            checkUi()
        }
    }
    private val pwd2Listener = object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            secondPwd = s.toString()
            checkUi()
        }
    }

    private fun checkUi(){
        val firstStatus = firstPwd != null && firstPwd!!.length >=6
        val secondStatus = secondPwd != null && secondPwd!!.length >= 6
        setBtnIsclick(fragment_loging_reset_pwd_change_submint_btn, firstStatus && secondStatus)
    }


    override fun initData() { mStatus = 0 }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg)
    }



    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.fragment_loging_reset_pwd_change_submint_btn) { submintPwd() }
    }

    fun setPhone(phone:String){
        phoneStr = phone
    }


    /**
     * 获取用户密码盐
     */
    fun getUserKey(){

        getPresenter()!!.getUserKey(RequestConfig.requestUserKey(phoneStr,true))
    }

    /**
     * 提交新密码
     */
    fun requestPwd(){
        val map  = hashMapOf<String,Any>()
        map["phone_num"] = phoneStr
        map["password"] = TrustMD5Utils.getMD5(salt,TrustMD5Utils.getMD5(secondPwd),"BS")
        getPresenter()?.setPwd(map)
    }

    /**
     * 提交
     */
    fun submintPwd(){
        firstPwd = mPwd1?.text?.trim().toString()
        if (checkStringIsNullAndShowMsg(firstPwd,getString(R.string.pwd_enter_error_tx))||checkPwdIsError(firstPwd,getString(R.string.pwd_insufficient_tx))) {
            return
        }
        secondPwd = mPwd2?.text?.trim().toString()

        if (checkStringIsNullAndShowMsg(secondPwd,getString(R.string.pwd_enter_error_tx)) || checkPwdIsError(secondPwd,getString(R.string.pwd_insufficient_tx))) {
            return
        }

        if(secondPwd != firstPwd){
            showDoublePwdDiffrentPo()
//            showToast("两次密码输入不一致!")
            return
        }

        if (checkUserRegister()) {
            val map = HashMap<String, Any>()
            map["type"] = TrustAppConfig.USER_REGISTER_PHONE
            map["phone_num"] = phoneStr
            map["password"] = TrustMD5Utils.getMD5(phoneStr,TrustMD5Utils.getMD5(secondPwd),"BS")
            getPresenter()!!.getUserRegisterKey(map)
        }else{
            getUserKey()
        }





//        mActivity?.startActivity(Intent(mActivity,MainActivity::class.java))

    }


    fun getCarInfo(){
        val map1  = hashMapOf<String,Any>()
        map1["user_id"] = TrustAppConfig.userId
        getPresenter()!!.getCarInfo(map1)
    }
    override fun createPresenter(): LoginVerificationCodePresenter {
        return LoginVerificationCodePresenter()
    }

    @SuppressLint("NewApi")
    fun chooseFragment(fragment: Fragment){
        val changeBounds = ChangeBounds()
        changeBounds.duration = 1000
        fragment.sharedElementEnterTransition = changeBounds
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.activity_login_fragment_layout, fragment)
                .addToBackStack(null)
//                .addSharedElement(fragment_login_logo_img,"logo")
                .commit()
    }


    override fun resultReacquireVerificationCodeCallBack(bean: BsLoginBean?) {

    }

    override fun resultSubmintVerificationCodeCallBack(bean: BsLoginBean?) {
    }

    override fun resultCapcha(bean: BsGetCapchaBean?) {
    }

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            BsApplication.bsDbManger!!.setUserLoginStatus(bean)
            getCarInfo()
        }
    }

    override fun resultSetPwd(bean: BsSetPwdBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            showToast(bean.getState_info())
            mActivity?.finish()
//            val map = java.util.HashMap<String, Any>()
//            map["phone_num"] = phoneStr
//            map["password"] = TrustMD5Utils.getMD5(salt, TrustMD5Utils.getMD5(secondPwd), "BS")
//            map["type"] = TrustAppConfig.LOGIN_TYPE_PWD
//            map["channel"] = TrustAppConfig.appChannel
//            map["trigger"] = TrustAppConfig.TYPE_USER
//            map["dev_id"] = "Android,${TrustTools.getMac(mActivity)}"
//            getPresenter()!!.pwdLogin(map)
        }
    }

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
        salt = userBean.userSalt!!
        requestPwd()
    }

    override fun resultUserRegisterKey(bean: BsUserRegisterKeyBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            showToast(bean.getState_info())
            mActivity?.finish()
        }
    }

    override fun resultPwdLogin(bean: BsLoginBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) { }
    }

    private fun showDoublePwdDiffrentPo(){
        getGPPopup().showOnlyBtnPopu(msgTx = getString(R.string.double_pwd_diffrent_tx),
                btnTx = getString(R.string.popupwindow_disagree_privacy_policy_cancel_tx)
                ,listener =  object : TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener{
            override fun onClickListener(v:BasePopupWindow) {
                v.dismiss()
            }
        })
    }


    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if(BeanUtils.checkSuccess(bean?.state!!, bean.state_info!!,this)){
            var checkUserLoginStatus :DbUserLoginStatusBean?= BsApplication.bsDbManger!!.checkUserLoginStatus(phoneStr)
            checkUserLoginStatus?.userIsBindCar = !bean.data?.bikes?.isEmpty()!!
            BsApplication.bsDbManger!!.setUserLoginStatus(checkUserLoginStatus!!)
            checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus(phoneStr)
            if (checkUserLoginStatus!!.userIsBindCar) {
                activity!!.startActivity(Intent(activity!!,MainHomeActivity::class.java))
            }else{
                val intent = Intent(activity!!, BindCarActivity::class.java)
                intent.putExtra("phone",phoneStr)
                startActivity(intent)
            }
            mActivity!!.finish()
        }
    }

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {}

    override fun resultCheckSmsvc(bean: BsCheckSmsvcBean?) {}

    /**
     * 检查 是 注册 还是忘记密码 逻辑不同
     * true 注册
     * false 忘记密码
     */
    private fun checkUserRegister():Boolean{
        return uiTypeStatus == PWD_REGISTER
    }

}