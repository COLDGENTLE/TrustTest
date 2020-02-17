package com.trust.demo.basis.updateapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AutoOpenAppManger :BroadcastReceiver(){

    companion object{
        private var mManger:AutoOpenAppManger? = null

        fun getInstance():AutoOpenAppManger{
            if (mManger == null) {
                mManger = AutoOpenAppManger()
            }
            return mManger!!
        }
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val action = intent.action
            val packageName = context.packageName
            val data = intent.data
            val installedPkgName = data?.schemeSpecificPart
            if((action.equals(Intent.ACTION_PACKAGE_ADDED)
                            || action.equals(Intent.ACTION_PACKAGE_REPLACED)) && installedPkgName.equals(packageName)){
                val launchIntent =  Intent()
                launchIntent.action = "action_bs_main_activity"
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent);
            }
        }
    }

}