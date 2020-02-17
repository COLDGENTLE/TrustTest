package com.sharkgulf.soloera.jpushlibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import cn.jpush.android.api.JPushInterface

/**
 *  Created by user on 2019/5/23
 */
//private  var pHuaweiPushInterface: JPluginPlatformInterface? = null
fun initJPush(context: Context,actionMap: HashMap<String,String>){
    JPushInterface.setDebugMode(true)
    JPushInterface.init(context)
//    pHuaweiPushInterface = JPluginPlatformInterface(context)
    PushMessageReceiver.setmActionMap(actionMap)
}

fun getRegistrationId(context: Context):String?{
    return JPushInterface.getRegistrationID(context)
}


fun JpushHuaweiOnStart(activity: Activity){
    //适配华为HMS SDK增加的调用
//    pHuaweiPushInterface?.onStart(activity)
}

fun JpushHuaweiOnStop(activity: Activity){
    //适配华为HMS SDK增加的调用
//    pHuaweiPushInterface?.onStop(activity)
}

fun JpushHuaweiOnActivityResult(activity: Activity, requestCode:Int, resultCode:Int, data:Intent?){
//    //适配华为HMS SDK增加的调用
//    if (requestCode == JPluginPlatformInterface.JPLUGIN_REQUEST_CODE) {
//        pHuaweiPushInterface?.onActivityResult(activity, requestCode, resultCode, data)
//    }
}






