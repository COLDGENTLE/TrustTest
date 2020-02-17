package com.sharkgulf.pingpaylib

import android.app.Activity
import android.content.Intent
import com.pingplusplus.android.Pingpp
import android.R.attr.data
import androidx.fragment.app.Fragment
import androidx.core.app.NotificationCompat.getExtras
import android.util.Log


/**
 *  Created by user on 2019/4/26
 */

fun Activity.TestPay(activity: Activity){
    //开启调试模式
    Pingpp.DEBUG = true
    val ZFB = "{    \"id\": \"ch_XPWrLGfjXn54P8yXrP5SWDS4\",    \"object\": \"charge\",    \"created\": 1556261264,    \"livemode\": false,    \"paid\": false,    \"refunded\": false,    \"reversed\": false,    \"app\": \"app_1azb18LK84u5iDSW\",    \"channel\": \"alipay\",    \"order_no\": \"e0d86332ec653ae7\",    \"client_ip\": \"210.22.78.6\",    \"amount\": 1,    \"amount_settle\": 1,    \"currency\": \"cny\",    \"subject\": \"测试订单 by demo 20190426-144743\",    \"body\": \"Your Body - 订单详情\",    \"extra\": {},    \"time_paid\": null,    \"time_expire\": 1556261563,    \"time_settle\": null,    \"transaction_no\": null,    \"refunds\": {        \"object\": \"list\",        \"url\": \"/v1/charges/ch_XPWrLGfjXn54P8yXrP5SWDS4/refunds\",        \"has_more\": false,        \"data\": []    },    \"amount_refunded\": 0,    \"failure_code\": null,    \"failure_msg\": null,    \"metadata\": {        \"ori_channel\": \"alipay\"    },    \"credential\": {        \"object\": \"credential\",        \"alipay\": {            \"orderInfo\": \"app_id=2008336127720428&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA&timestamp=2019-04-26%2014%3A47%3A44&version=1.0&biz_content=%7B%22body%22%3A%22Your%20Body%20-%20%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85%22%2C%22subject%22%3A%22%E6%B5%8B%E8%AF%95%E8%AE%A2%E5%8D%95%20by%20demo%2020190426-144743%22%2C%22out_trade_no%22%3A%22e0d86332ec653ae7%22%2C%22total_amount%22%3A0.01%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%224m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_XPWrLGfjXn54P8yXrP5SWDS4\"        }    },    \"description\": null}"
    val WX = "{    \"id\": \"ch_zjXf9OPabbL0arTqjPzzjDmP\",    \"object\": \"charge\",    \"created\": 1557391104,    \"livemode\": false,    \"paid\": false,    \"refunded\": false,    \"reversed\": false,    \"app\": \"app_1azb18LK84u5iDSW\",    \"channel\": \"wx\",    \"order_no\": \"d32a93fd44674b28\",    \"client_ip\": \"210.22.78.6\",    \"amount\": 1,    \"amount_settle\": 1,    \"currency\": \"cny\",    \"subject\": \"测试订单 by demo 20190509-163824\",    \"body\": \"Your Body - 订单详情\",    \"extra\": {},    \"time_paid\": null,    \"time_expire\": 1557391404,    \"time_settle\": null,    \"transaction_no\": null,    \"refunds\": {        \"object\": \"list\",        \"url\": \"/v1/charges/ch_zjXf9OPabbL0arTqjPzzjDmP/refunds\",        \"has_more\": false,        \"data\": []    },    \"amount_refunded\": 0,    \"failure_code\": null,    \"failure_msg\": null,    \"metadata\": {        \"ori_channel\": \"wx\"    },    \"credential\": {        \"object\": \"credential\",        \"wx\": {            \"appId\": \"wxgirtok9ertsoqla1\",            \"partnerId\": \"9313071534\",            \"prepayId\": \"1101000000190509rh08mlvv94ogt4wd\",            \"nonceStr\": \"df59246921821a963bca7432ff5426a7\",            \"timeStamp\": 1557391104,            \"packageValue\": \"Sign=WXPay\",            \"sign\": \"EC1E94F6AAFFDE18712BDDFDEB81F136\"        }    },    \"description\": null}"
    Pingpp.createPayment(activity, WX)
}

fun Activity.PingPay(activity: Activity,payData:String){
    Pingpp.createPayment(activity, payData)
}

fun Fragment.PingPay(activity: Activity, payData:String){
    Pingpp.createPayment(activity, payData)
}


fun Activity.resultActivity(requestCode: Int, resultCode: Int, data: Intent?){
    //支付页面返回处理
    if (requestCode === Pingpp.REQUEST_CODE_PAYMENT) {
        if (resultCode === Activity.RESULT_OK) {
            val result = data?.extras?.getString("pay_result")
            /* 处理返回值
             * "success" - 支付
             * 成功
             * "fail"    - 支付失败
             * "cancel"  - 取消支付
             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
             * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
             */
            val errorMsg = data?.extras?.getString("error_msg") // 错误信息
            val extraMsg = data?.extras?.getString("extra_msg") // 错误信息
            mPayCallBack?.callBack(result,errorMsg,extraMsg,result.equals("success"))
            Log.d("pay","errorMsg $errorMsg  extraMsg $extraMsg  result $result" )
        }
    }
}

private var mPayCallBack :PayCallBack? = null
interface PayCallBack{
    fun callBack(result:String?,errorMsg:String?,extraMsg:String?,payStatus:Boolean)
}

fun Activity.addPayCallBack(callBack:PayCallBack){
    mPayCallBack =callBack
}

fun Fragment.addPayCallBack(callBack:PayCallBack){
    mPayCallBack =callBack
}
