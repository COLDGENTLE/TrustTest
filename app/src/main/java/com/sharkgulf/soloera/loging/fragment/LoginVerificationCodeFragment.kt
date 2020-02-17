package com.sharkgulf.soloera.loging.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.mvpview.login.ILoginVerificationCodeView
import com.sharkgulf.soloera.presenter.login.LoginVerificationCodePresenter
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import com.tuo.customview.VerificationCodeView
import kotlinx.android.synthetic.main.fragment_verification_code_loging.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.tool.alert.AlertTool
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_FRAGMENT_VERIFICATION_CODE
import com.sharkgulf.soloera.tool.config.getAlerTool
import com.sharkgulf.soloera.tool.config.getAppConfig
import com.sharkgulf.soloera.tool.config.setTextStrings
import com.trust.retrofit.config.ProjectInit


/*
 *Created by Trust on 2018/12/17
 */
@Route(path = ROUTE_PATH_FRAGMENT_VERIFICATION_CODE)
class LoginVerificationCodeFragment :TrustMVPFragment<ILoginVerificationCodeView,LoginVerificationCodePresenter>(), ILoginVerificationCodeView {
    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private var isHaveCar:Boolean = false
    private val TAG = LoginVerificationCodeFragment::class.java.canonicalName

    private var status = SEND_SMS_SVC_TYPE_SMS
    private var mChangeFragmentListener: LogingActivity.changeFragmentListener? = null
    fun setChangeFragmentListener(listener:LogingActivity.changeFragmentListener){
        mChangeFragmentListener = listener
    }

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!, bean.state_info!!,this)) {
                val checkUserLoginStatus = bsDbManger?.checkUserLoginStatus()
                checkUserLoginStatus?.userBikeList = bean.data
                bsDbManger!!.setUserLoginStatus(checkUserLoginStatus!!)

                isHaveCar = if (bean.data?.bikes != null) {
                    !bean.data?.bikes?.isEmpty()!!
                }else{
                    false
                }
                val map  = hashMapOf<String,Any>()
                getPresenter()?.getUserInfo(map)
        }
    }

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {}

    override fun resultPwdLogin(bean: BsLoginBean?) {}

    override fun resultCheckSmsvc(bean: BsCheckSmsvcBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
                getPresenter()?.requestUserRegister(RequestConfig.requestUserRegister(3,mPhone!!, mdata!!))
        }
    }


    private var showDialog: DialogFragment? = null
    private var mPhone:String? = null
    private var verofocatopmCodeStr:String = ""
    private val trustTools = TrustTools<View>()
    private var mType = -1
    private var mdata:HashMap<String,String>? = null
    override fun getLayoutId(): Int { return R.layout.fragment_verification_code_loging }

    fun setPhone(phone:String){ mPhone = phone }

    fun setData(data:HashMap<String,String>){ mdata = data }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(fragment_login_verification_code_reacquire_btn)
        baseSetOnClick(fragment_login_voice_txt_btn)
        fragment_berification_code_ed.setInputCompleteListener(inputCompleteListener)
        ver_code_tx.text = String.format(getString(R.string.verification_code_tx),mPhone)

    }


    override fun initData() { reacquireVerificationCode(status) }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            TrustDialog.getInstant().showDialog(childFragmentManager)
        }
    }

    override fun diassDialog() {
        mActivity!!.runOnUiThread {
            TrustDialog.getInstant().closeDialog()
        }
    }

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    override fun createPresenter(): LoginVerificationCodePresenter { return LoginVerificationCodePresenter() }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun baseResultOnClick(v: View) {

        when(v.id){
            R.id.fragment_login_verification_code_reacquire_submint_btn ->{ }
            R.id.fragment_login_voice_txt_btn -> {
                if (status == SEND_SMS_SVC_TYPE_VOICE) {//使用短信验证码
                    changeTextCode()
                }else{//使用语音验证码
                    getAlerTool().showIsToVoiceVerification(object : AlertTool.AlertToolListener.isToVoiceListener{
                        override fun onToListener(isTo: Boolean) {
                            if (isTo) {

                            changeVoiceCode()
                            }
                        }
                    },getString(R.string.receive_tx))

                }



            }
            R.id.fragment_login_verification_code_reacquire_btn->{
                changeTextCode()
            }
        }
    }

    /**
     * 防刷接口  申请图形验证码
     */
    fun requestImgVeriFicationCode(){
        getPresenter()!!.getCapcha(RequestConfig.getCapcha(ANTI_BRUSH_TYPE_NUM_IMG))
    }

    fun setType(type:Int){ mType = type }

    /**
     * 重新申请验证码
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun reacquireVerificationCode(type:Int){
        getPresenter()!!.phoneLogin(RequestConfig.getPhoneLogin(mPhone!!,type,true))
    }

    /**
     * 提交验证码
     */
    fun submintSmsVc(type:Int) {
        getPresenter()!!.submintVerificationCode(RequestConfig.
                getSubmintVerificationCode(type,mPhone,verofocatopmCodeStr,mdata))
    }

    /**
     * 验证短信验证码
     */
    private fun  checkSmsvc(){
        getPresenter()!!.getCheckSmsvc(RequestConfig.getCheckSmsvc(mPhone,verofocatopmCodeStr))
    }



    private val inputCompleteListener =  object :VerificationCodeView.InputCompleteListener{
        override fun inputComplete() {
            if (trustTools.checkVerificationCode(fragment_berification_code_ed.inputContent)) {
                verofocatopmCodeStr = fragment_berification_code_ed.inputContent
                showSubmintBtn()
            }
        }

        override fun deleteContent() { unShowSubmintBtn() }

    }


    private fun showSubmintBtn(){
        if (mType == LOGIN_TYPE_THERE) { checkSmsvc() }else{ submintSmsVc(LOGIN_TYPE_SMS) }
//        trustTools.changeSubmintBtn(fragment_login_verification_code_reacquire_submint_btn,true,R.drawable.login_register_btn_bg)
    }

    private fun unShowSubmintBtn(){
//        trustTools.changeSubmintBtn(fragment_login_verification_code_reacquire_submint_btn,false,R.drawable.login_register_btn_gray_bg)
    }


    private fun startCountdown(){
        trustTools.disposable?.dispose()
        fragment_login_verification_code_reacquire_btn.visibility = View.GONE
        fragment_login_verification_code_reacquire_tx.visibility = View.VISIBLE
        trustTools.Countdown(activity,fragment_login_verification_code_reacquire_tx,60,getString(R.string.countdown_tx),
                object :TrustTools.IsFinshTimeListener{
                    override fun isFinshTimeListener() {
                        if (mActivity != null && !mActivity!!.isFinishing) {
                            fragment_login_verification_code_reacquire_tx?.visibility = View.GONE
                            fragment_login_verification_code_reacquire_btn?.visibility = View.VISIBLE
                        }
                    }
                    override fun showVoiceVerificationCode() {
                        fragment_login_voice_txt_btn?.visibility = View.VISIBLE
                    }
                }
        )
    }

    override fun resultCapcha(bean: BsGetCapchaBean?) {
    }



    override fun resultReacquireVerificationCodeCallBack(bean: BsLoginBean?) {
        showToast(bean.toString())
    }

    override fun resultSubmintVerificationCodeCallBack(bean: BsLoginBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            bsDbManger!!.setUserLoginStatus(mPhone!!,true,false,bean?.getData()?.token)
            token = bean.getData()?.token
            ProjectInit.addToken(token)
            getPresenter()!!.getCarInfo(RequestConfig.requestUserOrCarInfo())

        }
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            bsDbManger!!.setUserLoginStatus(mPhone!!,true,isHaveCar,token,null, bean?.getData()?.user?.user_id!!, bean.getData()!!.user)

            val intent = Intent(activity!!, MainHomeActivity::class.java)
            intent.putExtra(HOME_UPDATE_KEY,true)
            startActivity(intent)
            mActivity!!.finish()
            getAppConfig().setLoginType(mType)
        }
    }



    override fun resultPhoneLogin(bean: BsGetSmsBean?) {
        diassDialog()
        showToast(bean?.getState_info()!!)
        startCountdown()
    }


    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultUserRegisterKey(bean: BsUserRegisterKeyBean?) {}

    override fun resultSetPwd(bean: BsSetPwdBean?) {}
    override fun onDestroy() {
        trustTools.disposable?.dispose()
        super.onDestroy()
    }



    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            submintSmsVc(LOGIN_TYPE_THERE)
        }
    }


    private fun changeVoiceCode(){
        reacquireVerificationCode(SEND_SMS_SVC_TYPE_VOICE)
        chengeUi(getString(R.string.input_verification_code_voice),null,
                getString(R.string.verification_code_request),getString(R.string.verification_code_voice), setTextStrings(R.string.verification_code_voice_tx,mPhone!!))
    }

    private fun changeTextCode(){
        status = SEND_SMS_SVC_TYPE_SMS
        reacquireVerificationCode(status)
        chengeUi(getString(R.string.input_verification_code_tx),R.string.verification_code_tx,
                getString(R.string.verification_code_request),getString(R.string.verification_code_voice))
    }

    private fun chengeUi(verificationCodeTitleTxt:String,verCodeMsg:Int?,reSendTxt:String,changeTxt:String,verCodemsg:String? = null){
//        verification_code_tv.text = verificationCodeTitleTxt
        ver_code_tx.text = if(verCodeMsg == null) verCodemsg!! else String.format(getString(verCodeMsg),mPhone)
        fragment_login_verification_code_reacquire_btn.text = reSendTxt
        fragment_login_voice_txt_btn.text = changeTxt
    }
}