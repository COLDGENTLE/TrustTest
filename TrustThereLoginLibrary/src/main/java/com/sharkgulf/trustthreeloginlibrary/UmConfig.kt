package com.sharkgulf.trustthreeloginlibrary

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI


/**
 *  Created by user on 2019/4/25
 */

//友盟初始化接口
fun UmConfigInit( key:String, application: Application, channelKey:String){
    /**
     * 设置组件化的Log开关
     * 参数: boolean 默认为false，如需查看LOG设置为true
     */
    UMConfigure.setLogEnabled(true)
    val channelStr =application.packageManager
            .getApplicationInfo(application.packageName,
                    PackageManager.GET_META_DATA).metaData.getString(channelKey)
    UMConfigure.init(application.applicationContext,key , channelStr, UMConfigure.DEVICE_TYPE_PHONE, null)
}


fun threeLogin(context: Context
                           ,wxAppID:String,wxAppSecret:String,
                           qqAppId:String,qqAppSecret:String,
                           sinaAppId:String,sinaAppSecret:String,sinaCallBack:String){
    PlatformConfig.setWeixin(wxAppID, wxAppSecret)//微信APPID和AppSecret
    PlatformConfig.setQQZone(qqAppId,qqAppSecret)//QQAPPID和AppSecret
    PlatformConfig.setSinaWeibo(sinaAppId, sinaAppSecret,sinaCallBack)//微博
    UmThreeLoginConfig.getConfig(context)
}

fun umThreeLogin(context: Context):UmThreeLoginConfig{
    return  UmThreeLoginConfig.getConfig(context)
}

fun MobclickAgentOnResume(context: Context){
    MobclickAgent.onResume(context)
}

fun MobclickAgentOnPause(context: Context){
    MobclickAgent.onPause(context)
}

fun UmOnActivityResult(context:Context,requestCode: Int, resultCode: Int, data: Intent?){
    UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data)
}