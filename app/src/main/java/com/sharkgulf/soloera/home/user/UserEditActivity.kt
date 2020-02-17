package com.sharkgulf.soloera.home.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.text.Editable
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_user_edit.*
import kotlinx.android.synthetic.main.tite_layout.*
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.trust.utils.TrustAnalysis
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.trust.utils.setEditText


class UserEditActivity : TrustMVPActivtiy<IUser,UserPresenter>(),IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}
    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}
    override fun resultPointInFo(bean: BsPointinfoBean?) {}
    override fun resultPointDetail(bean: BsPointDetailBean?) {}
    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}
    private val TAG= UserEditActivity::class.java.canonicalName!!
    private var mDialog: DialogFragment? = null
    private val titles = arrayListOf("昵称","手机号","真实姓名","个性签名","车牌号","车辆名称")
    private var intExtra :Int = 0
    private var mBid:Int = DEFUTE
    private var mBean :DbUserLoginStatusBean? = null
    private var mCarInfo:CarInfoBean? = null
    private val USER= 0
    private val BIKE= 1
    private var mHttpType = USER
    companion object {
        fun startActivity(context:Context,type:Int,userBean: DbUserLoginStatusBean){
            val intent = Intent(context,UserEditActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("userBean",TrustAnalysis.resultString(userBean))
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int { return R.layout.activity_user_edit }

    override fun initView(savedInstanceState: Bundle?) {
        changeEdTv(false)
        title_submint_btn.visibility = View.VISIBLE
        title_back_btn.visibility = View.GONE
        title_back_txt.text = getString(R.string.cancel)
        baseSetOnClick(title_back_txt)
        baseSetOnClick(title_submint_btn)
        user_edit_ed.addTextChangedListener(listener)
        user_edit_ed.setOnEditorActionListener { textView, i, keyEvent -> (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) }
        intExtra = intent.getIntExtra("type", 0)
        mBid = intent.getIntExtra("bid", DEFULT)
//        mBean = TrustAnalysis.resultTrustBean(intent.getStringExtra("userBean"),DbUserLoginStatusBean::class.java)
        mBean = getAuthentication().getUser()
        title_tx.text = titles[intExtra]
        when (intExtra) {
            //昵称
            UPDATE_USER_NICK_NAME -> {
                changeEdlength(16)
                val name = if (mBean?.userBean != null && !mBean?.userNickName.equals("")) {
                    mBean?.userBean?.nick_name
                }else{
                    "sharks0001"
                }
                user_edit_ed.hint = name
                user_edit_ed.maxLines = 1
                user_edit_ed.ellipsize = TextUtils.TruncateAt.START
                user_edit_ed.setSingleLine(true)
            }
            //手机号
            UPDATE_USER_PHONE ->{
                user_edit_ed.inputType =  android.text.InputType.TYPE_CLASS_NUMBER
                changeEdlength(11)
                user_edit_ed.hint = mBean?.userPhone
            }
            //真实姓名
            UPDATE_USER_NAME -> {
                changeEdlength(16)
                user_edit_ed.hint = mBean?.userName
            }
            //个性签名
            UPDATE_USER_SIGNE -> {
                changeEdlength(32)
                user_edit_ed.setText(mBean?.userBean?.per_sign!!.toCharArray(),0, mBean?.userBean?.per_sign!!.length)
            }
            //车牌号
            4 -> {
                changeEdlength(16)
                checkData(4) }
            //车辆名称
            5 -> {
                changeEdlength(16)
                checkData(5) }
            else -> {
            }
        }

    }


    private fun  checkData(type:Int){
        mHttpType = BIKE
        mCarInfo = getCarInfoData(mBid)
        if (mCarInfo != null) {
            val result = if (type == 4){
                mCarInfo!!.plate_num
            }else{
                mCarInfo!!.bike_name
            }
            user_edit_ed.setText(result)
        }
    }


    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.title_back_txt -> {
                finish()
            }
            else -> {
                submintInfo()
            }
        }
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        mDialog =  TrustDialog().showDialog(supportFragmentManager)
    }

    override fun diassDialog() {
        mDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    override fun createPresenter(): UserPresenter {
        val userPresenter = UserPresenter()
        userPresenter.setTAG(TAG)
        return userPresenter
    }



    private fun changeEdlength(length:Int){
        setEditText(user_edit_ed,length)
    }

    private fun submintInfo() {
        val msg = user_edit_ed.text.trim().toString()
        if (mHttpType == USER) {
            when (intExtra) {
                //昵称
                0 -> {
                    if (mBean?.userBean?.nick_name != msg) {
                        mBean?.userBean?.nick_name = msg
                    }
                }
                //手机号
                1 ->{
                    if (mBean?.userBean?.phone_num != msg) {
                        mBean?.userBean?.phone_num = msg
                    }
                }
                //真实姓名
                2 -> {
                    if (mBean?.userBean?.real_name != msg) {
                        mBean!!.userName = msg
                        mBean!!.userBean!!.real_name = msg
                    }
                }
                //个性签名
                3 -> {
                    if (mBean?.userBean?.per_sign != msg) {
                        mBean?.userBean?.per_sign = msg
                    }
                }
                else -> {
                }
            }
            getPresenter()?.editUserInfo(RequestConfig.requestEditUserInfo(mBean!!))
        }else{
            if (intExtra ==4) {//车牌
                mCarInfo!!.plate_num = msg
            }else{
                mCarInfo!!.bike_name = msg
            }
            getPresenter()?.requestUpdateCarInfo(RequestConfig.updateCarProfile(mCarInfo!!),mCarInfo!!)
        }

    }



    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            TrustLogUtils.d(TAG,"mBean :${mBean?.userBean?.real_name}")
            getAuthentication().setUser(mBean!!)
//            BsApplication.bsDbManger!!.setUserLoginStatus(mBean!!)
//            getAuthentication().setUserInfoName(mBean!!.userBean!!.real_name!!)
//            BsApplication.bsDbManger!!.setUserLoginStatus(mBean!!)
            showToast(bean.getState_info())

            finish()
        }
    }
    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            sendBikeInfo(mBid)
            sendUpdateBikeList()
            finish()
        }
    }

    private val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        @SuppressLint("NewApi")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length!! > 0) {
                changeEdTv(true)
            }else{
                changeEdTv(false)
            }
        }

    }

    @SuppressLint("NewApi")
    private fun changeEdTv(isClickable:Boolean){
        title_submint_btn.isEnabled = isClickable
        title_submint_btn.isClickable = isClickable
        title_submint_btn.setTextColor(resources.getColor(if(isClickable)R.color.colorWhiteGay else R.color.gray6d,null))
    }
}
