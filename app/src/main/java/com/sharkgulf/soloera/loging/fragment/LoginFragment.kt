package com.sharkgulf.soloera.loging.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.loging.ChangePwdActivity
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.mvpview.login.ILoginView
import com.sharkgulf.soloera.presenter.login.LoginPresenter
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPFragment
import kotlinx.android.synthetic.main.fragment_loging.*
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.*
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mAuthentication
import com.sharkgulf.soloera.loging.ThreeLoginActivity
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.tool.config.getAppConfig
import com.sharkgulf.soloera.tool.config.setBtnIsclick
import com.sharkgulf.soloera.tool.config.setColor
import com.sharkgulf.soloera.tool.config.setHtmlSpanneds
import com.sharkgulf.trustthreeloginlibrary.UmThreeLoginConfig
import com.sharkgulf.trustthreeloginlibrary.umThreeLogin
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.retrofit.config.ProjectInit
import kotlin.collections.HashMap


/*
 *Created by Trust on 2018/12/17
 */
class LoginFragment : TrustMVPFragment<ILoginView, LoginPresenter>(), ILoginView {

    private var showDialog: DialogFragment? = null

    //默认登陆方式 手机号
    private var loginType = LOGIN_TYPE_SMS

    private var mPhone: String? = null

    private var mPwd: String? = null

    private var mSalt: String? = null

    private var checkUserLoginStatus: DbUserLoginStatusBean? = null

    private val TAG = LoginFragment::class.java.canonicalName

    private var mdata: HashMap<String, String>? = null
    override fun getLayoutId(): Int { return R.layout.fragment_loging }

    private var fragment_loging_pwd: EditText? = null
    private var isShow = false

    override fun initView(view: View?, savedInstanceState: Bundle?) {

        fragment_loging_pwd = bs_edit_text_pwd.findViewById(R.id.bs_edit_text_ed)
        fragment_loging_pwd?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        fragment_loging_pwd?.addTextChangedListener(pwdEditclick)
        bs_edit_text_pwd.findViewById<ImageView>(R.id.bs_edit_text_type_ic).setImageResource(R.drawable.pwd_ic)
        val pwdIc = bs_edit_text_pwd.findViewById<ImageView>(R.id.bs_edit_text_ic)
        pwdIc.setColor(R.color.colorWhiteGay)
        pwdIc.setImageResource(R.drawable.ic_hint_pw)
        pwdIc.setOnClickListener {
            pwdIc.setImageResource(if (isShow) R.drawable.ic_hint_pw else R.drawable.ic_show_pw)
            isShow = !isShow
            val type = if (isShow) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            fragment_loging_pwd?.inputType = type
            fragment_loging_pwd?.typeface = fragment_loging_pwd?.typeface
            fragment_loging_pwd?.setSelection(fragment_loging_pwd?.text!!.length)
        }


        baseSetOnClick(fragment_login_submint_btn)
        baseSetOnClick(fragment_login_register_btn)
        baseSetOnClick(fragment_login_change_pwd_btn)
        baseSetOnClick(login_wb_btn)
        baseSetOnClick(login_wx_btn)
        baseSetOnClick(login_qq_btn)

        setBtnIsclick(fragment_login_submint_btn, false)
        fragment_loging_phone.addTextChangedListener(editclick)
        checkUserLoginStatus = mAuthentication.getRecentLoginUser()
        if (checkUserLoginStatus != null) {
            val demo = checkUserLoginStatus!!.isDemo
            val userPhone = checkUserLoginStatus?.userPhone
            if (!demo) {
              fragment_loging_phone.setText(userPhone!!.toCharArray(), 0, userPhone.length)
            }
            if (!checkUserLoginStatus?.userBean?.icon.equals("")) {
//                fragment_loging_logo_img.glideUserIc(checkUserLoginStatus?.userBean?.icon, true)
            }
        }



        setHtmlSpanneds(fragment_login_protocol_tx,R.string.login_fragment_layout_protocol,"",R.color.colorRead, SizeLabel.HtmlOnClickListener { action ->
            intentProtocol(if(action == "0")URL_USER_SERVICES_AGREEMENT else URL_PRIVACY_POLICY )
        })
    }

    override fun initData() {
        token = null
    }


    fun getCarInfo() {
        val map1 = hashMapOf<String, Any>()
        map1["user_id"] = userId
        getPresenter()!!.getCarInfo(map1)
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(childFragmentManager)
        }
    }

    override fun diassDialog() {
        mActivity!!.runOnUiThread {
            showDialog?.dismiss()
        }
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        TrustLogUtils.d("error: $msg")
        diassDialog()
        showToast(msg)
    }


    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.fragment_login_submint_btn -> {
                requestLogin()
            }
            R.id.fragment_login_register_btn -> {
                startChangePwdActivtiy(R.string.layout_register_tx, PWD_REGISTER)
            }
            R.id.fragment_login_change_pwd_btn -> {
                startChangePwdActivtiy(R.string.layout_reset_pwd_tx, PWD_RESET)
            }
            R.id.login_wb_btn -> {
                umThreeLogin(mActivity!!).startSinaLogin(mActivity!!, ThreeListener)
            }
            R.id.login_qq_btn -> {
                umThreeLogin(mActivity!!).startQQLogin(mActivity!!, ThreeListener)
            }
            R.id.login_wx_btn -> {
                umThreeLogin(mActivity!!).startWxLogin(mActivity!!, ThreeListener)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startChangePwdActivtiy(title: Int, titleType: Int) {
        val intent = Intent(mActivity, ChangePwdActivity::class.java)
        intent.putExtra("title", getString(title))
        intent.putExtra("titleType", titleType)
        mActivity!!.startActivity(intent)
    }

    /**
     * 切换登录类型
     */
    private fun chooseLoginType() {
        loginType = when (loginType) {
            LOGIN_TYPE_SMS -> {
                login_type_tv.visibility = View.VISIBLE
                LOGIN_TYPE_PWD
            }
            LOGIN_TYPE_PWD -> {
                login_type_tv.visibility = View.INVISIBLE
                LOGIN_TYPE_SMS
            }
            else -> {
                login_type_tv.visibility = View.INVISIBLE
                LOGIN_TYPE_SMS
            }
        }

        if (loginType == LOGIN_TYPE_SMS) {
            //手机号直接登录
            changeLoginType(getString(R.string.login_fragment_layout_pwd_login), View.VISIBLE, View.GONE)
            fragment_login_other_layout.visibility = View.GONE
            bs_edit_text_pwd_line.visibility = View.GONE
            fragment_loging_help_tx.visibility = View.VISIBLE
        } else {
            //密码登陆
            changeLoginType(getString(R.string.login_fragment_layout_phone_login), View.GONE, View.VISIBLE)
            fragment_login_other_layout.visibility = View.VISIBLE
            fragment_loging_help_tx.visibility = View.GONE
            bs_edit_text_pwd_line.visibility = View.VISIBLE

        }
        fragment_loging_pwd?.setText("")
        checkLoginType()
    }

    private fun changeLoginType(msg: String, helpTxStatus: Int, pwdBtnStatus: Int) {
        fragment_loging_help_tx.visibility = helpTxStatus
        fragment_loging_pwd?.visibility = pwdBtnStatus
        bs_edit_text_pwd?.visibility = pwdBtnStatus
    }




    /**
     * 跳转协议页面
     */
    private fun intentProtocol(type: String) {
        val intent = Intent(mActivity, UserProtocolActivity::class.java)
        intent.putExtra(WEB_INTENT_USER_TYPE_KEY, type)
        mActivity?.startActivity(intent)
    }


    /**
     * 登陆请求
     */
    private fun requestLogin() {
        mPhone = fragment_loging_phone.text.trim().toString()
        if (checkStringIsNullAndShowMsg(mPhone, getString(R.string.login_fragment_phone_error)) || !checkPhone(mPhone, getString(R.string.login_fragment_phone_error))) {
            return
        }

        checkUserLoginStatus = bsDbManger!!.checkUserLoginStatus(mPhone!!)
        toRequest()
    }

    fun toRequest() {
        if (loginType == LOGIN_TYPE_SMS) {
            userPhone = mPhone!!
            if (TrustHttpUtils.getSingleton(mActivity!!).isNetworkAvailable()) {
                chooseFragment()
            } else {
                showHttpError()
            }
        } else {
            mPwd = fragment_loging_pwd?.text?.trim().toString()
            if (checkStringIsNullAndShowMsg(mPwd, getString(R.string.login_fragment_pwd_error))) {
                return
            }


            if (TrustHttpUtils.getSingleton(mActivity!!).isNetworkAvailable()) {
                val userBean = bsDbManger!!.checkUserLoginStatus(mPhone!!)
                if (userBean != null) {
                    if (userBean.userSalt != null) {
                        mSalt = userBean.userSalt
                        userPwdLogin()
                    } else {
                        getUserKey()
                    }
                } else {//没有保存用户salt
                    getUserKey()
                }
            } else {
                showHttpError()
            }

        }
        getAppConfig().setLoginType(loginType)
    }

    /**
     * 获取用户密码盐
     */
    fun getUserKey() {
        getPresenter()!!.getUserKey(RequestConfig.requestUserKey(mPhone!!,true))
    }


    fun userPwdLogin() {
        mPwd = fragment_loging_pwd?.text?.trim().toString()
        userPhone = mPhone!!
        getPresenter()!!.pwdLogin(RequestConfig.pwdLogin(mPhone!!, mPwd!!,true))
    }


    override fun resultPhoneLoginCallBack(bean: BsGetSmsBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            userPhone = mPhone!!
            chooseFragment()
        }
    }

    override fun resultPwdLoginCallBack(bean: BsLoginBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            userPhone = mPhone
            token = bean.getData()!!.token
            ProjectInit.addToken(token)
            val map = hashMapOf<String, Any>()
            getPresenter()?.getUserInfo(map)
        }
    }


    override fun createPresenter(): LoginPresenter { return LoginPresenter() }

    @SuppressLint("NewApi")
    fun chooseFragment() { mChangeFragmentListener?.startVerificationCodeFragment(mPhone!!) }


    override fun resultUserKey(bean: BsGetUserKeyBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            var userBean: DbUserLoginStatusBean? = bsDbManger!!.checkUserLoginStatus(mPhone!!)
            if (userBean != null) {
                userBean.userSalt = bean.getData()?.salt
            } else {
                userBean = DbUserLoginStatusBean()
                userBean.userPhone = mPhone!!
                userBean.userSalt = bean.getData()?.salt
            }
            mSalt = userBean.userSalt
            bsDbManger?.setUserLoginStatus(userBean)
            userPwdLogin()
        } else {
            showToast(bean.getState_info())
        }

    }


    override fun onResume() {
        if (userPhone != "") {
            fragment_loging_phone.setText(userPhone!!.toCharArray(), 0, userPhone.length)
        }
        super.onResume()
    }

    private val ThreeListener = object : UmThreeLoginConfig.ThreeListener {
        override fun reusltError(error: String) {
            showToast(error)
        }


        override fun reusltImport(msg: String) {}

        override fun resultUserinfo(partner: String, data: MutableMap<String, String>) {
            data["partner"] = partner
            mdata = data as HashMap<String, String>
            getPresenter()?.checkUserRegister(RequestConfig.requestCheckUserRegister(CHECK_THERE, mdata!!))
        }

    }


    override fun resultCheckUserThree(bean: BsUserThressBean?) {}


    private val editclick = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mPhone =s?.toString()
            checkLoginType()
        }
    }

    private val pwdEditclick = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                mPwd = s?.toString()
                checkLoginType()
            }

        }
    }



    private val mChangeTitleStatus= object : LogingActivity.changeTitleStatus {
        override fun callBack(isInit: Boolean) {
            chooseLoginType()
        }
    }


    fun getChangeTitleListener():LogingActivity.changeTitleStatus{
        return mChangeTitleStatus
    }

    private fun showHttpError(){
        showToast(getString(R.string.http_error_tx))
    }



    private fun checkLoginType(){

        val phoneStatus = if (mPhone != null) {
            mPhone!!.length == 11
        }else{
            false
        }

        val pwdStatus = if(mPwd != null){
            mPwd!!.length >=6
        }else{ false }

        if (loginType == LOGIN_TYPE_SMS  ) {
            setBtnIsclick(fragment_login_submint_btn, phoneStatus)
        }else {
            setBtnIsclick(fragment_login_submint_btn, phoneStatus && pwdStatus)
        }

    }




    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultUserregister(bean: BsUserRegisterKeyBean?) {}

    private var mChangeFragmentListener: LogingActivity.changeFragmentListener? = null
    fun setChangeFragmentListener(listener:LogingActivity.changeFragmentListener){
        mChangeFragmentListener = listener
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            userPhone = bean.getData()?.user?.phone_num
            mPhone = bean.getData()?.user?.phone_num
            bsDbManger!!.setUserLoginStatus(userPhone!!, true, false, token, null, bean.getData()?.user?.user_id!!, bean.getData()!!.user)
            getCarInfo()
        }
    }

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!, bean.state_info!!, this)) {
            val checkUserLoginStatus: DbUserLoginStatusBean? = bsDbManger!!.checkUserLoginStatus(mPhone!!)
            checkUserLoginStatus?.userIsBindCar = if (bean.data?.bikes != null) {
                bikeId = bean.data?.bikes!![0].bike_id
                !bean.data?.bikes?.isEmpty()!!
            } else {
                false
            }
            bsDbManger!!.setUserLoginStatus(checkUserLoginStatus!!)

            mAuthentication.setCarInfo(bean.data)
//            arouterStartActivity(ROUTE_PATH_MAIN_HOME)
            val intent = Intent(mActivity!!, MainHomeActivity::class.java)
            intent.putExtra(HOME_UPDATE_KEY,true)
            mActivity!!.startActivity(intent)

            mActivity!!.finish()
        }
    }

    override fun resultCheckUserRegister(bean: BsCheckUserRegisterBean?) {

        when (BeanUtils.checkResultData(bean?.getState()!!, bean.getState_info()!!, this)) {
            BeanUtils.CODE_DATA_SUCCESS -> {
                //三方没有绑定过
                startThreeLogin()
            }
            BeanUtils.CODE_DATA_USER_IS_REGISTER -> {
                getAppConfig().setLoginType(LOGIN_TYPE_THERE)
                getPresenter()?.pwdLogin(RequestConfig.userThreeLogin(mActivity!!, mdata!!))
            }
            else -> {
            }
        }

    }


    fun startThreeLogin() {
        ThreeLoginActivity.startActivity(mContext!!, mdata)
    }

}