package com.sharkgulf.soloera.main

import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import com.sharkgulf.soloera.TrustAppConfig
import com.trust.demo.basis.trust.utils.TrustLogUtils
import org.json.JSONObject

/**
 *  Created by user on 2019/5/27
 */
class FragmentShoppingJs {
    private var mFragmentShopping:FragmentShopping? = null
    private var mH5CallBackName:String? = null
     var mSuccessUrl:String? = null
     var mFailUrl:String? = null
    private val TAG = "FragmentShoppingJs"
    fun setFragment(fragment:FragmentShopping){
        mFragmentShopping = fragment
    }

    @JavascriptInterface
    fun getTicketWithCallback(msg:String){
//        if (mAuthentication.getUserLoginStatus()) {
            try {
                val json = JSONObject(msg)
                val parameters = json.getString("parameters")
                val parametersjsonObject = JSONObject(parameters)
                val callbackfunc = parametersjsonObject.getString("callbackfunc")
                mH5CallBackName = callbackfunc
                TrustLogUtils.d(TAG,"  $msg")
                TrustLogUtils.d(TAG,"callbackfunc :$callbackfunc")
                mFragmentShopping?.getTicket()
            }catch (e:Exception){
                TrustLogUtils.e(e.toString())
            }
//        }
    }


    fun setTicket(ticket:String){
        val map = hashMapOf<String,Any>()
        map["ticket"] = ticket
        map["user_id"] = TrustAppConfig.userId
        TrustLogUtils.d(TAG,"$mH5CallBackName('${Gson().toJson(map)}')")
        mFragmentShopping?.callJsTicket("$mH5CallBackName('${Gson().toJson(map)}')")
    }

    @JavascriptInterface
    fun paymentCommit(msg:String){
        Log.d(TAG,"paymentCommit  $msg")
        val parametersString = JSONObject(msg).getString("parameters")
        val paramsStr = JSONObject(parametersString).getString("params")
        val order_id = JSONObject(paramsStr).getString("order_id")
        mSuccessUrl = JSONObject(paramsStr).getString("return_url")
        mFailUrl = JSONObject(paramsStr).getString("failed_url")
        val amount = JSONObject(paramsStr).getInt("amount")
        val channel = JSONObject(paramsStr).getString("channel")
        mFragmentShopping?.getOrderInfo(channel,amount,order_id)
    }

    @JavascriptInterface
    fun test(msg:String){
        Log.d(TAG,msg)
        mFragmentShopping?.callJsTicket("getTicket('我是android 测试')")
    }
}