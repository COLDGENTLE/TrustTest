package com.sharkgulf.soloera.jpushlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.jpush.android.api.JPushInterface
import org.json.JSONObject

/**
 *  Created by user on 2019/6/17
 */
open abstract class TrustJPushCallBackActivity: AppCompatActivity() {
    companion object {
        val PUSH_TYPE = "PUSH_TYPE"
        val PUSH_TYPE_OPEN = 0
    }
    private var mPushType:Int = PUSH_TYPE_OPEN
    abstract fun getLaiyoutId():Int
    //点击通知后回调
    abstract fun openedPushCallBack(title:String,msg:String,extras:String?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLaiyoutId())
        initData()
    }


    private fun initData(){

        if (checkHw()) {
            return
        }

        if(checkFcmOrOppo()){
            return
        }


        val title = intent.getStringExtra(JPushInterface.EXTRA_NOTIFICATION_TITLE)
        val msg = intent.getStringExtra(JPushInterface.EXTRA_ALERT)
        val extras:String? = intent.getStringExtra("extras")
        openedPushCallBack(title,msg,extras)
    }

    private fun checkHw():Boolean{
        val data =  intent.data
          if (data != null) {
              val dataString = data.toString()
              val jsonObject = JSONObject(dataString)
              val title = jsonObject.getString("n_title")
              val msg = jsonObject.getString("n_content")
              val extras = jsonObject.getString("n_extras")

              openedPushCallBack(title,msg,extras)
              return true
          }
        return false
    }

    private fun checkFcmOrOppo():Boolean{
        val mdata = intent.extras?.getString("JMessageExtra")
        if (mdata != null) {
            val jsonObject = JSONObject(mdata)
            val title:String? = jsonObject.optString("n_title")
            val msg:String? = jsonObject.optString("n_content")
            val extras:String? = jsonObject.optString("n_extras")


            if (title != null && msg != null) {
                openedPushCallBack(title,msg,extras)
                return true
            }
        }

        return false
    }

}