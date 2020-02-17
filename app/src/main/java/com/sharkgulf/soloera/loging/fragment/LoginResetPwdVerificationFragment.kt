package com.sharkgulf.soloera.loging.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.login.ILoginVerificationCodeView
import com.sharkgulf.soloera.presenter.login.LoginVerificationCodePresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.alert.AlertTool
import com.sharkgulf.soloera.tool.config.finshActivity
import com.sharkgulf.soloera.tool.config.getAlerTool
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustMD5Utils
import com.trust.retrofit.config.ProjectInit
import com.tuo.customview.VerificationCodeView
import kotlinx.android.synthetic.main.fragment_loging_reset_pwd_verification_caode.*

/*
 *Created by Trust on 2018/12/17
 * 修改密码之输入验证码页面
 */
class LoginResetPwdVerificationFragment :TrustMVPFragment<ILoginVerificationCodeView, LoginVerificationCodePresenter>(), ILoginVerificationCodeView {
    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {}

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {
        if (bean?.getState().equals("00")) {//没有注册
            getUserRegisterKey()
        }else if(bean?.getState().equals("1103")){//已经注册了
            toFragment()
        }else{
            showToast(bean?.getState_info())
        }
    }


    private val trustTools = TrustTools<View>()
    private var phoneStr:String = userPhone
    private var verofocatopmCodeStr:String = ""
    private var showDialog: DialogFragment? = null

    private var mType:Int =  SEND_SMS_SVC_TYPE_SMS

    private var mIntentType:Int = INTENT_FRAGMENT
    fun setIntentType(intentType:Int){ mIntentType = intentType }

    fun setPhone(phone:String){ phoneStr = phone }

    fun setType(type:Int){ mType =  type }

    override fun getLayoutId(): Int { return R.layout.fragment_loging_reset_pwd_verification_caode }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        fragment_login_reset_pwd_ed.setInputCompleteListener(inputCompleteListener)
        baseSetOnClick(fragment_login_reset_pwd_reacquire_btn)
        baseSetOnClick(fragment_login_reset_pwd_voice_txt_btn)
        baseSetOnClick(fragment_login_reset_pwd_reacquire_tx)
        fragment_login_reset_pwd_tx.text =  if(mType == SEND_SMS_SVC_TYPE_SMS){
            String.format(getString(R.string.verification_code_tx),phoneStr)
        }else{
            String.format(getString(R.string.verification_code_voice_tx),phoneStr)
        }
    }

    override fun initData() {
        reacquireVerificationCode()
        startCountdown()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(childFragmentManager)
        }
    }

    override fun diassDialog() { showDialog?.dismiss() }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.fragment_login_reset_pwd_reacquire_btn) {
            reacquireVerificationCode()
        }else if (v.id == R.id.fragment_login_reset_pwd_voice_txt_btn){
            if (mIntentType == INTENT_FRAGMENT) {//进语音
                val loginResetPwdVerificationFragment = LoginResetPwdVerificationFragment()
                loginResetPwdVerificationFragment.setIntentType(INTENT_FRAGMENT)
                loginResetPwdVerificationFragment.setPhone(phoneStr)
                if (mType == SEND_SMS_SVC_TYPE_VOICE) {
                    loginResetPwdVerificationFragment.setType(SEND_SMS_SVC_TYPE_SMS)
                    chooseFragment(loginResetPwdVerificationFragment)
                }else{
                    getAlerTool().showIsToVoiceVerification(object :AlertTool.AlertToolListener.isToVoiceListener{
                        override fun onToListener(isTo: Boolean) {
                            if (isTo) {
                                loginResetPwdVerificationFragment.setType(SEND_SMS_SVC_TYPE_VOICE)
                                chooseFragment(loginResetPwdVerificationFragment)
                            }
                        }
                    })

                }

            }else{//返回手机号短信ui
                mActivity?.finish()
            }
        }else if (v.id == R.id.fragment_login_reset_pwd_reacquire_tx){
            reacquireVerificationCode()
        }
    }

    /**
     * 检查 是 注册 还是忘记密码 逻辑不同
     * true 注册
     * false 忘记密码
     */
    private fun checkUserRegister():Boolean{ return uiTypeStatus == PWD_REGISTER }


    /**
     * 验证短信验证码
     */
    private fun  checkSmsvc(){
        val map = hashMapOf<String,Any>()
        map["phone_num"] = phoneStr
        map["sms_vc"] = verofocatopmCodeStr
        getPresenter()!!.getCheckSmsvc(map)
    }


    /**
     * 防刷接口  申请图形验证码
     */
    fun requestImgVeriFicationCode(){
        val map = HashMap<String, Any>()
        map["token"] = TrustMD5Utils.getMD5("BLUE+ts+SHARK")
        map["type"] = TrustAppConfig.ANTI_BRUSH_TYPE_NUM_IMG
        getPresenter()!!.getCapcha(map)
    }

    /**
     * 重新申请验证码
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun reacquireVerificationCode(){
        getPresenter()!!.phoneLogin(RequestConfig.getPhoneLogin(phoneStr!!,mType,true))
    }




    override fun createPresenter(): LoginVerificationCodePresenter {
        return LoginVerificationCodePresenter()
    }

    @SuppressLint("NewApi")
    fun chooseFragment(fragment: Fragment){

//        val changeBounds = ChangeBounds()
//        changeBounds.duration = 1000
//        loginResetPwdChangeFragment.sharedElementEnterTransition = changeBounds
        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.change_pwd_framelayout, fragment)
                .addToBackStack(null)
                .commit()
    }



    override fun resultReacquireVerificationCodeCallBack(bean: BsLoginBean?) { showToast(bean.toString()) }

    override fun resultSubmintVerificationCodeCallBack(bean: BsLoginBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            BsApplication.bsDbManger!!.setUserLoginStatus(phoneStr,true,false,bean.getData()?.token)
            token = bean.getData()?.token
            ProjectInit.addToken(token)
            val map  = hashMapOf<String,Any>()
            map["token"] = token
            getPresenter()?.getUserInfo(map)
        }else{
            showToast(bean.getState_info())
        }

    }

    override fun resultCapcha(bean: BsGetCapchaBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            startCountdown()
        }
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
            BsApplication.bsDbManger!!.setUserLoginStatus(phoneStr,true,false, token,null, bean?.getData()?.user?.user_id!!)
            if (mIntentType ==INTENT_FRAGMENT) {
                toFragment()
            }else{
                startActivity(Intent(activity!!, MainHomeActivity::class.java))
                finshActivity()
            }
        }
    }

    fun toFragment() {
        val loginResetPwdChangeFragment = LoginResetPwdChangeFragment()
        loginResetPwdChangeFragment.setPhone(phoneStr)
        chooseFragment(loginResetPwdChangeFragment)
    }


    private fun startCountdown(){
        fragment_login_reset_pwd_reacquire_btn.visibility = View.GONE
        fragment_login_reset_pwd_reacquire_tx.visibility = View.VISIBLE
        trustTools.Countdown(activity,fragment_login_reset_pwd_reacquire_tx,60,"秒后可重新获取",
                object :TrustTools.IsFinshTimeListener{
                    override fun isFinshTimeListener() {
                        if (mActivity != null && !mActivity!!.isFinishing) {
                            if (fragment_login_reset_pwd_reacquire_tx != null) {
                                fragment_login_reset_pwd_reacquire_tx.visibility = View.GONE
                                fragment_login_reset_pwd_reacquire_btn.visibility = View.VISIBLE
                                val tx = if (mType == SEND_SMS_SVC_TYPE_VOICE) {
                                    getString(R.string.verification_voice_code_request)
                                }else{
                                    getString(R.string.verification_code_request)
                                }
                                fragment_login_reset_pwd_reacquire_btn.text = tx
                            }
                        }
                    }
                    override fun showVoiceVerificationCode() {
//                        fragment_login_reset_pwd_reacquire_btn.visibility = View.VISIBLE
                        if (fragment_login_reset_pwd_voice_txt_btn != null) {
//                            if (mType == SEND_SMS_SVC_TYPE_VOICE) {
//                                fragment_login_reset_pwd_voice_txt_btn.text = "使用短信验证码"
//                            }else{
//                                fragment_login_reset_pwd_voice_txt_btn.text = "使用语音验证码"
//                            }
                            fragment_login_reset_pwd_voice_txt_btn.text = getString(R.string.verification_code_txt)

                            fragment_login_reset_pwd_voice_txt_btn.visibility = View.VISIBLE
                        }
                    }
                }
        )
    }




    private val inputCompleteListener =  object : VerificationCodeView.InputCompleteListener{
        override fun inputComplete() {
            if (trustTools.checkVerificationCode(fragment_login_reset_pwd_ed.inputContent)) {
                verofocatopmCodeStr = fragment_login_reset_pwd_ed.inputContent

                if (mType == SEND_SMS_SVC_TYPE_VOICE) {
                    submintSmsVc()
                }else{
                    checkSmsvc()
                }
//
//                if (checkUserRegister()) {
//                    submintSmsVc()
//                }else{
//
//                }
            }
        }

        override fun deleteContent() {
            unShowSubmintBtn()
        }

    }

    private fun showSubmintBtn(){
        submintSmsVc()
//        trustTools.changeSubmintBtn(fragment_login_verification_code_reacquire_submint_btn,true,R.drawable.login_register_btn_bg)
    }

    private fun unShowSubmintBtn(){
//        trustTools.changeSubmintBtn(fragment_login_verification_code_reacquire_submint_btn,false,R.drawable.login_register_btn_gray_bg)
    }



    /**
     * 提交验证码
     */
    fun submintSmsVc() {
        getPresenter()!!.submintVerificationCode( RequestConfig.
                getSubmintVerificationCode(LOGIN_TYPE_SMS,phoneStr,verofocatopmCodeStr,null))
    }




    override fun resultSetPwd(bean: BsSetPwdBean?) {}


    override fun resultUserKey(bean: BsGetUserKeyBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
            var userBean: DbUserLoginStatusBean? = BsApplication.bsDbManger!!.checkUserLoginStatus(phoneStr)
            if (userBean != null) {
                userBean.userSalt = bean?.getData()?.salt
            }else{
                userBean = DbUserLoginStatusBean()
                userBean.userPhone = phoneStr
                userBean.userSalt =  bean?.getData()?.salt
            }
            BsApplication.bsDbManger?.setUserLoginStatus(userBean)
//            showSubmintBtn()
        }
    }

    override fun resultUserRegisterKey(bean: BsUserRegisterKeyBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){

            toFragment()
//            val map = HashMap<String, Any>()
//            map["phone_num"] = phoneStr
//            getPresenter()!!.getUserKey(map)
        }
    }

    override fun resultCheckSmsvc(bean: BsCheckSmsvcBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
            if (checkUserRegister()) {//注册页面
                toFragment()
            }else{//忘记密码ui
                val map = HashMap<String, Any>()
                map["type"] = USER_REGISTER_PHONE
                map["phone_num"] = phoneStr
                getPresenter()!!.checkUserRegister(map)
            }
        }
    }

    fun getUserRegisterKey() {
        val map = HashMap<String, Any>()
        map["type"] = USER_REGISTER_PHONE
        map["phone_num"] = phoneStr
        getPresenter()!!.getUserRegisterKey(map)
    }

    override fun onDestroy() {
        trustTools.disposable.dispose()
        super.onDestroy()
    }

    override fun resultPwdLogin(bean: BsLoginBean?) {}



}