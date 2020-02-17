package com.sharkgulf.soloera.appliction

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.appliction.BsApplication.Companion.mAuthentication
import com.sharkgulf.soloera.cards.activity.alert.AlertListActivity
import com.sharkgulf.soloera.cards.activity.securitysettings.WaitTestNotifyActivity
import com.sharkgulf.soloera.loging.ThreeLoginActivity
import com.sharkgulf.soloera.main.MainHomeActivity
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/8/5
 */
class BsActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val TAG = BsActivityLifecycleCallbacks::class.java.canonicalName
    private var activityAount = 0
    private var mActivity:Activity? = null
    private var mThreeLoginActivity:ThreeLoginActivity? = null
    private var mMainHomeActivity: MainHomeActivity? = null
    private var mWaitTestNotifyActivity: WaitTestNotifyActivity? = null
    companion object{
        private var mBsActivityLifecycleCallbacks:BsActivityLifecycleCallbacks? = null
        fun getInstance():BsActivityLifecycleCallbacks{
            if (mBsActivityLifecycleCallbacks == null) {
                mBsActivityLifecycleCallbacks = BsActivityLifecycleCallbacks()
            }
            return mBsActivityLifecycleCallbacks!!
        }
    }

    fun finishTestActivity(){
        if (mWaitTestNotifyActivity != null && !mWaitTestNotifyActivity!!.isFinishing) {
            mWaitTestNotifyActivity?.finish()
        }
    }

    fun getActivity():Activity{
        return  mActivity!!
    }

    fun getThreeLoginActivity():ThreeLoginActivity?{
        return  mThreeLoginActivity
    }

    fun getBsActivity():TrustMVPActivtiy<*, *>?{
        if (mActivity != null) {
            if (mActivity is TrustMVPActivtiy<*, *>) {
                return mActivity!! as TrustMVPActivtiy<*, *>
            }
        }
        return null
    }

    fun getMainHomeActivity():MainHomeActivity{
        return mMainHomeActivity!!
    }
    override fun onActivityPaused(activity: Activity?) {}

    override fun onActivityResumed(activity: Activity?) {
        mActivity = activity
        if (activity is MainHomeActivity) {
            mMainHomeActivity = activity
        }

        else if (activity is WaitTestNotifyActivity){
            mWaitTestNotifyActivity = activity
        }

        else if (activity is ThreeLoginActivity) {
            mThreeLoginActivity =activity
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        activityAount++
        if (activityAount == 1) {
            frontDesk()
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        activityAount--
        if (activityAount == 0) {
            backgroundProcess()
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        mActivity = activity
    }


    private fun backgroundProcess(){
        BsApplication.mTrustWebSocket?.disConnect()
        TrustLogUtils.d(TAG,"后台")
    }


    fun isAlertListActivity():Boolean{
        return mActivity is AlertListActivity
    }

    private fun frontDesk(){
        if (mAuthentication.getUserLoginStatus()) {
            if (!TrustAppConfig.isFirstStartHomeMain) {
                val user = mAuthentication.getUser()
                if ( user!= null && mAuthentication.getUserLoginStatus()) {
                    BsApplication.mTrustWebSocket?.connect(user.userToken!!)
//                    getApplicetion().getService()?.checkListFloatingBox()

                }
            }
        }
        TrustLogUtils.d(TAG,"前台")
        mMainHomeActivity?.updateFragment(Intent())
    }
}