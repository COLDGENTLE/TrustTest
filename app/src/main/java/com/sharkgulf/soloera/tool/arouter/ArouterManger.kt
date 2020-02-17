package com.sharkgulf.soloera.tool.arouter

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.View
import com.sharkgulf.soloera.loging.fragment.LoginVerificationCodeFragment


/**
 *  Created by user on 2019/7/12
 */

//路由路径
const val ROUTE_PATH_MAIN_HOME = "/activity/MainHomeActivity"
const val ROUTE_PATH_LOGING = "/activity/LoginActivity"
const val ROUTE_PATH_BATTERY = "/activity/BatteryActivity"
const val ROUTE_PATH_RIDE_TRACK = "/activity/RideTrackActivity"
const val ROUTE_PATH_ABOUT_CAR = "/activity/AboutCarActivity"
const val ROUTE_PATH_BS_WEV_VIEW = "/activity/BaWebViewActivity"


//fragment 验证码
const val ROUTE_PATH_FRAGMENT_VERIFICATION_CODE = "/fragment/LoginVerificationCodeFragment"




fun arouterStartActivity(path:String,parmas:Bundle = Bundle()){
    ARouter.getInstance().build(path).with(parmas).navigation()
}


fun arouterStartLoginVerificationCodeFragment(supportFragmentManager: FragmentManager, frameLayoutId:Int, path:String, v:View? = null, vStr:String? = null, phone:String){
    val fragment = ARouter.getInstance().build(path).navigation() as LoginVerificationCodeFragment
    fragment.setPhone(phone)
    arouterStartFragmentAnim(supportFragmentManager,fragment,frameLayoutId,v,vStr)
}



fun arouterStartFragmentAnim(supportFragmentManager: FragmentManager, fragment: Fragment, frameLayoutId:Int, v:View? = null, vStr:String? = null){
    val addToBackStack = supportFragmentManager!!.beginTransaction().replace(frameLayoutId, fragment)
            .addToBackStack(null)
    if (v != null && vStr != null) {
        addToBackStack.addSharedElement(v,vStr)
    }
    addToBackStack.commitAllowingStateLoss()
}


