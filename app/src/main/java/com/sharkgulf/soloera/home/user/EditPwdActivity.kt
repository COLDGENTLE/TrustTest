package com.sharkgulf.soloera.home.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.adjustTvTextSize
import com.sharkgulf.soloera.tool.config.getBsString
import com.sharkgulf.soloera.tool.config.getGPPopup
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_edit_pwd.*
import kotlinx.android.synthetic.main.tite_layout.*
import razerdp.basepopup.BasePopupWindow
import java.util.HashMap








class EditPwdActivity : TrustMVPActivtiy<IUser, UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}
    private val TAG = EditPwdActivity::class.java.canonicalName
    private val titles = arrayListOf("", getBsString(R.string.user_change_pwd_tx), getBsString(R.string.set_pwd_tx))
    private var mType:Int = 2
    private var mStr1:String? = null
    private var mStr2:String? = null
    private var mStr3:String? = null
    private var mPhone:String? = null
    private var edit_pwd_old_ed:EditText? = null
    private var edit_pwd_new_ed:EditText? = null
    private var edit_pwd_new2_ed:EditText? = null
    companion object {
        fun startActivity(context: Context,type:Int,phone:String){
            val intent = Intent(context,EditPwdActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("phone",phone)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_pwd
    }
    private var oldIsShow =false
    private var newIsShow =false
    private var new2IsShow =false
    @SuppressLint("NewApi")
    override fun initView(savedInstanceState: Bundle?) {
        mType = intent.getIntExtra("type",0)
        if (mType == 1) {
            edit_pwd_old_l.visibility = View.VISIBLE
        }else{
            edit_pwd_old_l.visibility = View.GONE
        }
        mPhone = intent.getStringExtra("phone")
        changUi()
        title_back_btn.visibility = View.GONE
        title_submint_btn.visibility = View.VISIBLE

        title_back_txt.text = getBsString(R.string.cancel)
        baseSetOnClick(title_submint_btn)
        baseSetOnClick(title_back_txt)
        edit_pwd_old_layout.findViewById<View>(R.id.bs_edit_text_type_ic).visibility = View.GONE
        edit_pwd_new_layout.findViewById<View>(R.id.bs_edit_text_type_ic).visibility = View.GONE
        edit_pwd_new2_layout.findViewById<View>(R.id.bs_edit_text_type_ic).visibility = View.GONE


        val oldText = edit_pwd_old_layout.findViewById<TextView>(R.id.bs_edit_text_type_tv)
        oldText.visibility = View.VISIBLE
        oldText.text = getString(R.string.old_pwd_tx)
        val newText = edit_pwd_new_layout.findViewById<TextView>(R.id.bs_edit_text_type_tv)
        newText.visibility = View.VISIBLE
        newText.text = getString(R.string.new_pwd_tx)
        val new2Text = edit_pwd_new2_layout.findViewById<TextView>(R.id.bs_edit_text_type_tv)
        new2Text.visibility = View.VISIBLE
        new2Text.text = getString(R.string.new2_pwd_tx)




        edit_pwd_old_ed = edit_pwd_old_layout.findViewById(R.id.bs_edit_text_ed)
        edit_pwd_old_ed?.setTextColor(resources.getColor(R.color.gray6d,null))
        edit_pwd_old_ed?.setHint(R.string.old_pwd_msg_tx)

        edit_pwd_new_ed = edit_pwd_new_layout.findViewById(R.id.bs_edit_text_ed)
        edit_pwd_new_ed?.setTextColor(resources.getColor(R.color.gray6d,null))
        edit_pwd_new_ed?.setHint(R.string.new_pwd_msg_tx)
        edit_pwd_new2_ed = edit_pwd_new2_layout.findViewById(R.id.bs_edit_text_ed)
        edit_pwd_new2_ed?.setTextColor(resources.getColor(R.color.gray6d,null))
        edit_pwd_new2_ed?.setHint(R.string.new2_pwd_msg_tx)


        edit_pwd_old_ed?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        edit_pwd_new_ed?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        edit_pwd_new2_ed?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT

        edit_pwd_old_ed?.addTextChangedListener(listener1)
        edit_pwd_new_ed?.addTextChangedListener(listener2)
        edit_pwd_new2_ed?.addTextChangedListener(listener3)

        val pwdIcOld = edit_pwd_old_layout.findViewById<ImageView>(R.id.bs_edit_text_ic)
        pwdIcOld.setImageResource(R.drawable.ic_hint_pw)
        pwdIcOld.setOnClickListener { oldIsShow = changeUi(pwdIcOld,oldIsShow,edit_pwd_old_ed) }

        val pwdIcNew = edit_pwd_new_layout.findViewById<ImageView>(R.id.bs_edit_text_ic)
        pwdIcNew.setImageResource(R.drawable.ic_hint_pw)
        pwdIcNew.setOnClickListener { newIsShow = changeUi(pwdIcNew,newIsShow,edit_pwd_new_ed) }

        val pwdIcNew2 = edit_pwd_new2_layout.findViewById<ImageView>(R.id.bs_edit_text_ic)
        pwdIcNew2.setImageResource(R.drawable.ic_hint_pw)
        pwdIcNew2.setOnClickListener { new2IsShow = changeUi(pwdIcNew2,new2IsShow,edit_pwd_new2_ed) }

        adjustTvTextSize(edit_pwd_msg2_tv,edit_pwd_msg2_tv.width,edit_pwd_msg2_tv.text.toString())

    }

    override fun initData() { checkStr()}

    private fun changeUi(v:ImageView,isShow: Boolean,v1:EditText?):Boolean{
        var show = isShow
        v.setImageResource(if (isShow) R.drawable.ic_hint_pw else R.drawable.ic_show_pw)
        show = !show
        val type = if (show) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
        v1?.inputType = InputType.TYPE_CLASS_TEXT or type
        v1?.typeface = v1?.typeface
        val text = v1?.text
        TrustLogUtils.d(TAG,"text.length ${text?.length}")
        if (text != null  && !TextUtils.isEmpty(text)) {
            v1?.setSelection(text.length) }
        return show
    }

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.title_submint_btn) {
            val userBean = BsApplication.bsDbManger!!.checkUserLoginStatus(mPhone!!)
            if (userBean != null) {
                submint()
            }else{
                getUserKey()
            }
        }else{
            finish()
        }
    }


    override fun createPresenter(): UserPresenter { return UserPresenter() }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            val checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus()
            checkUserLoginStatus!!.userLoginStatus = false
            BsApplication.bsDbManger!!.setUserLoginStatus(checkUserLoginStatus)
            startActivity(Intent(this, LogingActivity::class.java))
            finishActivityList()
        }
    }

    private fun changUi(){
        title_tx.text = titles[mType]
        edit_pwd_phone_tx.text = mPhone
    }

    /**
     * 获取用户密码盐
     */
    fun getUserKey(){
        val map = HashMap<String, Any>()
        map["phone_num"] = mPhone!!
        getPresenter()!!.getUserKey(map)
    }

    private fun submint() {
        val oldPwd = edit_pwd_old_ed!!.text.trim().toString()
        val newPwd = edit_pwd_new_ed!!.text.trim().toString()
        val new1Pwd = edit_pwd_new2_ed!!.text.trim().toString()


        if(mType == 1 && oldPwd == ""){
            showOnlyPopu(getBsString(R.string.pwd_error_tx), getBsString(R.string.old_pwd_error_tx))
        }else if (new1Pwd == "" || newPwd == "") {
            showOnlyPopu(getBsString(R.string.pwd_error_tx), getBsString(R.string.new_pwd_error_tx))
        }else if(new1Pwd != newPwd){
            showOnlyPopu("", getBsString(R.string.double_pwd_diffrent_tx))
        }else if(mType == 1 && newPwd == oldPwd){
            showToast(getBsString(R.string.old_pwd_different_new_pwd_tx))
//            showOnlyPopu("不能与旧密码相同，请重新输入新密码!")
        }else {
            getPresenter()?.editPwd( if (mType == 1) {//修改密码
                RequestConfig.setEditPwd(newPwd,oldPwd)
            }else{//设置密码
                RequestConfig.setEditPwd(newPwd)
            })
        }
    }


    override fun resultUserKey(bean: BsGetUserKeyBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            var userBean: DbUserLoginStatusBean? = BsApplication.bsDbManger!!.checkUserLoginStatus(mPhone!!)
            if (userBean != null) {
                userBean.userSalt =bean?.getData()?.salt
            }else{
                userBean = DbUserLoginStatusBean()
                userBean.userPhone = mPhone!!
                userBean.userSalt =  bean?.getData()?.salt
            }
            BsApplication.bsDbManger?.setUserLoginStatus(userBean)

            submint()
        }
    }


    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {
            if (bean?.getState().equals("00")) {//没有注册

            }else if(bean?.getState().equals("01")){//已经注册了

            }else{
                showToast(bean?.getState_info())
            }

    }
    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    private fun changeFocusable(v:EditText?){
        v?.setFocusable(false);
        v?.setFocusableInTouchMode(true);
    }


    private fun showOnlyPopu(title :String ? = null,msg:String){
        hideSoftKeyboard(this)
        getGPPopup().showOnlyBtnPopu(title ?: "",msg,listener = object :TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener{
            override fun onClickListener(view: BasePopupWindow) {
                view.dismiss()
            }
        },btnTx = getBsString(R.string.popupwindow_disagree_privacy_policy_cancel_tx))
    }

    @SuppressLint("NewApi")
    private fun changeBtn(color:Int,isClickable:Boolean){
        title_submint_btn.setTextColor(getColor(color))
        title_submint_btn.isClickable = isClickable
    }


    private val listener1 = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mStr1 = s.toString()
            checkStr(mStr1,mStr2,mStr3)
        }
    }

    private val listener2 = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mStr2 = s.toString()
            checkStr(mStr1,mStr2,mStr3)
        }
    }

    private val listener3 = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mStr3 = s.toString()
            checkStr(mStr1,mStr2,mStr3)
        }
    }

    private fun checkStr(str1:String? = null,str2:String? = null,str3:String? = null){
        if (mType == 1) {//设置过密码
            if (str1 != null && str2 != null && str3 != null && str1.length >= 6 && str2.length >=6 && str3.length >= 6 ) {
                changeBtn(R.color.colorWhiteGay,true)
            }else{
                changeBtn(R.color.gray6d,false)
            }
        }else{
            if ( str2 != null && str3 != null  && str2.length >=6 && str3.length >= 6) {
                changeBtn(R.color.colorWhiteGay,true)
            }else{
                changeBtn(R.color.gray6d,false)
            }
        }
    }


    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}
