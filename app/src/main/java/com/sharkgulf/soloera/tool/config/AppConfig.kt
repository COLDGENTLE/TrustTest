package com.sharkgulf.soloera.tool.config

import android.app.Activity
import android.content.Intent
import com.dn.tim.lib_permission.annotation.PermissionCanceled
import com.dn.tim.lib_permission.annotation.PermissionDenied
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.db.bean.DbAppBean
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.sharkgulf.soloera.tool.config.AppConfig.Companion.TAG_AppConfig
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.updateapp.BaseDownLoad
import com.trust.demo.basis.updateapp.IBaseDownLoad
import razerdp.basepopup.BasePopupWindow
import java.io.File

/**
 *  Created by user on 2019/9/24
 */
class AppConfig {
    companion object{
         val TAG_AppConfig = "AppConfig"
        private var mAppConfig:AppConfig? = null
        fun getInstance():AppConfig{
            if (mAppConfig == null) {
                mAppConfig = AppConfig()
            }
            return mAppConfig!!
        }
    }
    private val mDbManger: BsDbManger = getDbManger()



    //获取是否同意过隐私协议
    fun getAppPrivacyPolicyStatus():Boolean{
        return getDbAppBean().appPrivacyPolicy
    }

private  var mDbAppBean:DbAppBean = getDbAppBean()
    fun setAppPrivacyPilicyStatus(status:Boolean){
        mDbAppBean.appPrivacyPolicy = status
        mDbManger.setDbAppBean(mDbAppBean)
    }

    private fun getDbAppBean():DbAppBean{
        return mDbManger.getDbAppBean()
    }

    fun getIsDebug():Boolean{
        return getDbAppBean().isDebug
    }

    fun getisDevFirstInit():Boolean{
        return getDbAppBean().isDevFirstInit
    }

    fun getDbDeviceId():String?{
        return mDbAppBean.deviceId
    }

    fun setDbDeviceId(deviceId:String){
        mDbAppBean.deviceId = deviceId
        mDbManger.setDbAppBean(mDbAppBean)
    }

    fun setDevFirstInit(){
        mDbAppBean.isDevFirstInit = true
        mDbManger.setDbAppBean(mDbAppBean)
    }

    fun setAppModule(isDebug:Boolean){
        mDbAppBean.isDebug = isDebug
        mDbManger.setDbAppBean(mDbAppBean)
    }

    fun getLoginType():Int{
        return mDbAppBean.loginType
    }

    fun setLoginType(loginType:Int){
        mDbAppBean.loginType = loginType
        mDbManger.setDbAppBean(mDbAppBean)
    }

    fun setThreeLoginType(type:Int){
        mDbAppBean.threeLoginType = type
        mDbManger.setDbAppBean(mDbAppBean)
    }

    fun getThreeLoginType():Int{
        return mDbAppBean.threeLoginType
    }

    //根据上一次登录的类型跳转对应的ui

    fun startInitLogin(){
        val context = TrustAppUtils.getContext()
        val loginType = getLoginType()
        val intent = Intent(context,LogingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        when (loginType) {
            LOGIN_TYPE_SMS,LOGIN_TYPE_PWD -> {
//                intent.setClass(context,LogingActivity::class.java)
                intent.putExtra(LOGIN_TYPE_KEY,loginType)
            }
            LOGIN_TYPE_THERE -> {
//                intent.setClass(context,LogingActivity::class.java)
                intent.putExtra(LOGIN_TYPE_KEY,loginType)
//                intent.setClass(context,ThreeLoginChooseActivity::class.java)
            }
        }
        context.startActivity(intent)
    }

}


private var mAppInfo:BsAppVersionInfoBean.DataBean? = null
private var mActivity:Activity? = null
private var mIsMust = false
fun appVersionUpdate(activity: Activity,bean: BsAppVersionInfoBean,listener:TrustGeneralPurposePopupwindow.PopupOnclickListener.DownLoadBtnListener){
    mActivity = activity
    mAppInfo = bean?.getData()
    if(mAppInfo!!.is_update == APP_UPDATE){
        mIsMust = mAppInfo!!.upgrade_type == APP_MUST_UPDATE
        getGPPopup().showDownLoadPopu("", mAppInfo!!.ver!!,mAppInfo!!.des!!, listener,isMustLayout = mIsMust)
    }

}


fun downLoad(basePopupWindow: BasePopupWindow){
    val baseDownLoad = BaseDownLoad(mActivity!!, mAppInfo!!.url!!, object : IBaseDownLoad {
        override fun loadingDownLoad(maxLength: Int, progress: Int) {
            getGPPopup().changeDownLoadPopuLoading(maxLength, progress)
        }

        override fun endDownLoadView() {
            basePopupWindow.dismiss()
        }

        override fun loadingSuccess(file: File) {
            TrustLogUtils.d(TAG_AppConfig, "loadingSuccess ")
            BaseDownLoad.installApk(mActivity!!, file)
            basePopupWindow.dismiss()
        }

        override fun errorDownLoad(t: Exception) {
            getGPPopup().changDownLoadPopuError(t.toString(), object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DownLoadBtnListener {
                override fun onClickListener(isBtn1: Boolean, v: BasePopupWindow) {
                    if (isBtn1) {
                        v.dismiss()
                    } else {
                        startDownLoad(BaseDownLoad.getInstance()!!)
                    }
                }
            },mIsMust)
            TrustLogUtils.d(TAG_AppConfig, "errorDownLoad ${t.toString()} ")
        }

    })

    startDownLoad(baseDownLoad)
}

private fun startDownLoad(baseDownLoad:BaseDownLoad){
    baseDownLoad.downLoadApk()
}

@PermissionCanceled //点击取消执行这个函数
private fun cancel() {}

@PermissionDenied//点击取消和不在提醒 执行这个函数 注意 这个函数执行后 会自动跳转到手机系统设置权限得页面
private fun denied() {}



val BLE_TAG = "BLE_TAG"