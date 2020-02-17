package com.sharkgulf.soloera.home.user

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.Gravity
import android.view.View
import com.dn.tim.lib_permission.annotation.Permission
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsActivityLifecycleCallbacks
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mTrustWebSocket
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.tite_layout.*
import com.sharkgulf.soloera.tool.view.datepicker.DateFormatUtils
import com.sharkgulf.soloera.tool.view.datepicker.CustomDatePicker
import com.sharkgulf.soloera.tool.view.dialog.TrustBitmapChoosePopuwindow
import com.sharkgulf.soloera.tool.view.dialog.TrustChooseSexPopuwindow
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.lang.Exception
import java.sql.Date


class UserInfoActivity : TrustMVPActivtiy<IUser,UserPresenter>(),IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    private var imgPath:String = ""
    private var mTrustTools:TrustTools<View> = TrustTools.create(getApplicetion().packageName)


    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}
    private val TAG = UserInfoActivity::class.java.canonicalName
    private val sexs = arrayListOf("", getBsString(R.string.sex_man_tx), getBsString(R.string.sex_woman_tx))
    private val pwdStatus = arrayListOf(getBsString(R.string.not_required),getBsString(R.string.setting_yes),getBsString(R.string.setting_no))
    private var showDialog: DialogFragment? = null
    private var mBean:DbUserLoginStatusBean? = null
    private var mCustomDatePicker : CustomDatePicker? = null
    private var mTime:Long = System.currentTimeMillis()
    override fun getLayoutId(): Int { return R.layout.activity_user_info }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(user_info_ic_btn)
        baseSetOnClick(user_ext_btn)
        baseSetOnClick(title_back_btn)
        baseSetOnClick(user_info_nick_name_btn)
        baseSetOnClick(user_info_phone_btn)
        baseSetOnClick(user_info_phone_real_name_btn)
        baseSetOnClick(user_info_per_sign_btn)
        baseSetOnClick(user_info_phone_birthday_btn)
        baseSetOnClick(user_info_sex_btn)
        baseSetOnClick(user_info_pwd_status_btn)
        getBsString(title_tx,R.string.user_info_tx)
    }

    override fun initData() {
        initTimerPicker()
    }

    override fun baseResultOnClick(v: View) {
        when(v.id){
            R.id.user_info_ic_btn->{
                TrustBitmapChoosePopuwindow(this)
                        .setPopupWindowBitmapListener(object : TrustBitmapChoosePopuwindow.PopupWindowBitmapListener{
                            override fun BitMApListener(isCamera: Boolean) {
                                if (isCamera) {//色相头
                                    openCamera()
                                }else{//小相册
                                    openAlbum()
                                }
                            }
                        })
                        .setPopupGravity(Gravity.BOTTOM)
                        .showPopupWindow()
//                UserIcActivity.startActivity(this,mBean!!)
            }
            R.id.user_ext_btn->{
                showExtPopu()

            }
            R.id.title_back_btn->{
                finish()
            }
            R.id.user_info_nick_name_btn ->{
                start(UPDATE_USER_NICK_NAME)
            }
            R.id.user_info_phone_btn ->{
                startActivity(Intent(this,UserChangePhoneActivity::class.java))
            }
            R.id.user_info_phone_real_name_btn ->{
                start(UPDATE_USER_NAME)
            }
            R.id.user_info_per_sign_btn ->{
                start(UPDATE_USER_SIGNE)
            }
            R.id.user_info_phone_birthday_btn -> {
                mCustomDatePicker?.show("yyyy-MM-dd")
                mCustomDatePicker?.setSelectedTime(mTime,true)
            }
            R.id.user_info_sex_btn->{
                TrustChooseSexPopuwindow(this).setPopupWindowSexListener(object :TrustChooseSexPopuwindow.PopupWindowSexListener{
                    override fun SexListener(sex: Int) {
                        submintSex(sex)
                    }
                }) .setPopupGravity(Gravity.BOTTOM)
                        .showPopupWindow()
            }
            R.id.user_info_pwd_status_btn->{
                EditPwdActivity.startActivity(this, mBean?.userBean?.pwd_status!!, mBean?.userPhone!!)
            }
        }
    }

    private fun showExtPopu() {
        getGPPopup().showDoubleBtnPopu(msgTx = getString(R.string.ext_login),btn1Tx = getString(R.string.cancel),
                btn2Tx = getString(R.string.confirm),doubleBtnOnclickListener = listener)
    }

    private val listener= object :TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener{
        override fun onClickListener(isBtn1: Boolean) {
            if (!isBtn1) {
                getPresenter()?.userExt(hashMapOf())
            }
        }
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(supportFragmentManager)
        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    override fun createPresenter(): UserPresenter { return UserPresenter() }


    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            try {
                maxBindTime = DEFUTE.toLong()
                val checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus()
                checkUserLoginStatus!!.userLoginStatus = false
                BsApplication.bsDbManger!!.setUserLoginStatus(checkUserLoginStatus)
                BsApplication.mBsApplication?.setUserLoginStatus(false)
                bleDisConnection()
                mTrustWebSocket?.disConnect()
                getAppConfig().startInitLogin()
                val intent = Intent()
                intent.putExtra(HOME_UPDATE_KEY,true)
                BsActivityLifecycleCallbacks.getInstance().getMainHomeActivity().updateFragment(intent)
                TrustAppConfig.bikeId = DEFUTE
                DataAnalysisCenter.getInstance().setBikeId(DEFUTE)
            }catch (e:Exception){
                TrustLogUtils.d(TAG,"e : ${e.printStackTrace()}")
            }

//            startActivity(Intent(this, LogingActivity::class.java))
//            arouterStartActivity(ROUTE_PATH_LOGING)

        }
    }


    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            BsApplication.bsDbManger!!.setUserLoginStatus(mBean!!)
            showToast(bean.getState_info())
            changeUi()
        }
    }


    fun start(type:Int){
        UserEditActivity.startActivity(this,type, mBean!!)
    }

    override fun onResume() {
        changeUi()
        super.onResume()
    }


    private fun changeUi(){
        mBean  = BsDbManger.mDbUserLoginStatusBean
        TrustLogUtils.d(TAG,"name:  ${mBean?.userBean?.real_name}")
        if (mBean != null) {
            val userBean = mBean!!.userBean
            if (userBean != null) {
                user_info_nick_name_tx?.text = userBean.nick_name
                adjustTvTextSize(user_info_nick_name_tx,user_info_nick_name_tx.width,user_info_nick_name_tx.text.toString())
                user_info_phone_tx?.text = formatPhone(userBean.phone_num!!)
                user_info_phone_real_name_tx?.text = userBean.real_name
                user_info_phone_birthday_tx?.text = userBean.birthday
                user_info_per_sign_tx?.text = userBean.per_sign
                user_info_sex_tx?.text = sexs[userBean.gender]
                user_info_pwd_status_tx?.text = pwdStatus[userBean.pwd_status]
                user_info_ic_img?.glideUserIc(userBean.icon!!,true)
            }
        }
    }


    private fun initTimerPicker() {
        val beginTime = "1970-01-01 18:00:00"
        val endTime = DateFormatUtils.long2Str(mTime, true)
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mCustomDatePicker = CustomDatePicker(this, CustomDatePicker.Callback { timestamp ->
            submintInfo(timestamp)
        }, beginTime, endTime)
        // 允许点击屏幕或物理返回键关闭
        mCustomDatePicker?.setCancelable(true)
        // 显示时和分
        mCustomDatePicker?.setCanShowPreciseTime(false)
        // 允许循环滚动
        mCustomDatePicker?.setScrollLoop(true)
        // 允许滚动动画
        mCustomDatePicker?.setCanShowAnim(true)

    }

    private fun submintInfo(timestamp: Long) {
        val time = TrustTools.getTime(Date(timestamp), "yyyy-MM-dd")
        mBean?.userBean?.birthday = time
        getPresenter()?.editUserInfo(RequestConfig.requestEditUserInfo(mBean!!))
    }

    private fun submintSex(sex:Int){
        mBean?.userBean?.gender = sex
        getPresenter()?.editUserInfo(RequestConfig.requestEditUserInfo(mBean!!))
    }

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    @Permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    private fun openAlbum(){
        mTrustTools.openAlbum(this, 1)
    }
    @Permission(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun openCamera(){
        imgPath = mTrustTools.openCamera(this, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==1) {
            if (resultCode == RESULT_OK) {
//                val bitmap = TrustTools<View>().getImages(data, this)
                glideBitmap(this,data!!.data,object :onGetGlideBitmapListener{
                    override fun getGlideBitmapListener(bitmap: Bitmap?) {
                        if (bitmap != null) {
                        startUserIcActivity(bitmap)
                        }
                    }
                },isCircleCrop = false)

            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                mTrustTools.setInputStream(contentResolver
                        .openInputStream(mTrustTools.imageUri))
                val bitmapCompressionRotate = mTrustTools.bitmapCompressionRotate(imgPath)
                startUserIcActivity(bitmapCompressionRotate)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun startUserIcActivity(bitmap:Bitmap){
        if (!isFinishing) {
            UserIcActivity.mBitmap = bitmap
            UserIcActivity.startActivity(this,mBean!!)
        }
    }
}
