package com.sharkgulf.soloera.home.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.SEND_SMS_SVC_TYPE_SMS
import com.sharkgulf.soloera.TrustAppConfig.userPhone
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.setEditText
import kotlinx.android.synthetic.main.activity_user_change_phone.*
import kotlinx.android.synthetic.main.tite_layout.*

class UserChangePhoneActivity : TrustMVPActivtiy<IUser, UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    private var showDialog: DialogFragment? = null

    private var mPhoneEd:EditText? = null
    private var mClearBtn:ImageView? = null
    private var mSmmsCodeEd:EditText? = null
    private var mSmmsSendCode:TextView? = null
    private var mNewPhone:String? = null
    private var mVc:String? = null
    private var trustTools = TrustTools<View>()
    private var mIsVc = false
    override fun getLayoutId(): Int { return R.layout.activity_user_change_phone }

    override fun initView(savedInstanceState: Bundle?) {
        mPhoneEd = change_phone_layout.findViewById(R.id.bs_edit_text_ed)
        mPhoneEd?.hint = getBsString(R.string.please_enter_new_pwd_tx)
        mPhoneEd?.setHintTextColor(getBsColor(R.color.gray999))
        mPhoneEd?.inputType = InputType.TYPE_CLASS_NUMBER
        mPhoneEd?.setTextColor(getBsColor(R.color.colorBlack))
        change_phone_layout.findViewById<ImageView>(R.id.bs_edit_text_type_ic).visibility = View.GONE
        mClearBtn = change_phone_layout.findViewById(R.id.bs_edit_text_ic)
//        mClearBtn?.setOnClickListener { mPhoneEd?.setText("") }
        mClearBtn?.visibility = View.GONE
        val leftTv = change_phone_layout.findViewById<TextView>(R.id.bs_edit_text_type_tv)
        leftTv.visibility = View.VISIBLE
        getBsString(leftTv,R.string.new_phone)

        mSmmsCodeEd = change_phone_smms_layout.findViewById(R.id.bs_edit_text_ed)
        mSmmsCodeEd?.hint = getBsString(R.string.please_enter_smms_code_tx)
        mSmmsCodeEd?.setHintTextColor(getBsColor(R.color.gray999))
        mSmmsCodeEd?.inputType = InputType.TYPE_CLASS_NUMBER
        mSmmsCodeEd?.setTextColor(getBsColor(R.color.colorBlack))
        change_phone_smms_layout.findViewById<ImageView>(R.id.bs_edit_text_type_ic).visibility = View.GONE
        change_phone_smms_layout.findViewById<ImageView>(R.id.bs_edit_text_ic).visibility = View.GONE
        mSmmsSendCode = change_phone_smms_layout.findViewById(R.id.bs_edit_right_tv)
        mSmmsSendCode?.visibility = View.VISIBLE

        mSmmsSendCode?.setTextColor(getBsColor(R.color.blue0ff))
        val leftSmmsTv = change_phone_smms_layout.findViewById<TextView>(R.id.bs_edit_text_type_tv)
        leftSmmsTv.visibility = View.VISIBLE
        getBsString(leftSmmsTv,R.string.smms_code_tx)
        getBsString(title_tx,R.string.change_phone_tx)

        baseSetOnClick(title_back_btn)
        baseSetOnClick(change_phone_submint_btn)
        baseSetOnClick(mSmmsSendCode!!)

        setEditText(mPhoneEd!!,11)
        setEditText(mSmmsCodeEd!!,6)

        getBsString(change_phone_msg1_tv,R.string.change_pwd_msg1_tx, formatPhone(userPhone))

    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.title_back_btn -> { finish() }
            R.id.change_phone_submint_btn -> { submint() }
            R.id.bs_edit_right_tv -> { sendSmms() }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(supportFragmentManager)
        }
    }

    override fun diassDialog() {showDialog?.dismiss()}

    override fun showToast(msg: String?) {
        showBsToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showBsToast(msg) }

    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {
        if (BeanUtils.checkSuccess(bean!!.getState()!!,bean.getState_info()!!,this)) {
            if (mIsVc) {
                getPresenter()!!.phoneLogin(RequestConfig.getPhoneLogin(mNewPhone!!,SEND_SMS_SVC_TYPE_SMS,true))
            }else{
                getPresenter()?.updatePhone(RequestConfig.updatePhone(TrustAppConfig.userPhone,mNewPhone!!,mVc!!))
            }
        }
    }

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun createPresenter(): UserPresenter { return UserPresenter() }


    private fun startCountdown(){
        trustTools.disposable?.dispose()
        trustTools.Countdown(this,mSmmsSendCode!!,60, getBsString(R.string.resend_countdown_tx),
                object : TrustTools.IsFinshTimeListener{
                    override fun isFinshTimeListener() {
                        getBsString(mSmmsSendCode!!,R.string.send_smms)
                    }
                    override fun showVoiceVerificationCode() {}
                }
        )
    }

    override fun resultUpdatePhone(bean: BsHttpBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean?.getState_info()!!,this)) {
            showToast(getBsString(R.string.replace_new_phone_tx,mPhoneEd!!.text.toString()))
            getAuthentication().setUserPhone(mNewPhone!!)
            userPhone = mNewPhone
            TrustTools<View>().setCountdown(3,object :TrustTools.CountdownCallBack{
                override fun callBackCountDown() {
                    finish()
                }

            })
        }
    }


    private fun submint(){
        mIsVc = false
        val phone = mPhoneEd?.text?.trim().toString()
        if (checkStringIsNullAndShowMsg(phone, getBsString(R.string.phone_is_null_tx)) || !checkPhone(phone, getString(R.string.login_fragment_phone_error))) {
            return
        }

        mVc = mSmmsCodeEd?.text?.trim().toString()
        if (checkStringIsNullAndShowMsg(mVc, getString(R.string.smms_code_is_null_tx)) || checkIsVc(mVc) ) {
            return
        }

        getPresenter()?.checkUserRegister(RequestConfig.checkUserIsRegister(phone))

    }

    private fun  checkIsVc(vc:String?):Boolean{
        if (vc == null) {
            return false
        }else if(vc.length ==6){
            return true
        }else{
            showToast(getBsString(R.string.smms_code_is_error_tx))
            return false
        }
    }

    private fun sendSmms(){
        mIsVc = true
        mNewPhone = mPhoneEd?.text?.trim().toString()
        if (checkStringIsNullAndShowMsg(mNewPhone, getString(R.string.phone_is_null_tx)) || !checkPhone(mNewPhone, getString(R.string.login_fragment_phone_error))) {
            return
        }
        getPresenter()!!.checkUserRegister(RequestConfig.checkUserIsRegister(mNewPhone!!))
    }

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {
        if (BeanUtils.checkSuccess(bean!!.getState()!!,bean?.getState_info()!!,this)) {
            startCountdown()
        }
    }
}
