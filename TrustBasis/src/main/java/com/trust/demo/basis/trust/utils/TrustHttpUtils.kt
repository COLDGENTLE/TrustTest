package com.trust.demo.basis.trust.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.content.IntentFilter
import android.net.*
import android.util.Log
import android.widget.Toast


/**
 * Created by Trust on 2018/5/28.
 * 网络检查工具类
 */
class TrustHttpUtils(context: Context) {
    private var context: Context = context
    private val TAG = TrustHttpUtils::class.java.canonicalName
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var trustHttpUtils: TrustHttpUtils?=null
        fun getSingleton(context: Context) : TrustHttpUtils {
            if (trustHttpUtils == null) {
                trustHttpUtils = TrustHttpUtils(context)
            }
            return trustHttpUtils!!
        }
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable():Boolean{
        val connectivity  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null){
            val info = connectivity.activeNetworkInfo
            if (info != null ){
                    return  info.isConnected
            }
        }
        return false
    }

    @SuppressLint("MissingPermission")
            /**
     * 获取网络状态
     *
     * @param context
     * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
     * @permission android.permission.ACCESS_NETWORK_STATE
     */
    fun getNetWorkStatesType(): Int {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
            return -1000//没网
        }

        val type = activeNetworkInfo.type
        when (type) {
            ConnectivityManager.TYPE_MOBILE -> return TYPE_MOBILE//移动数据
            ConnectivityManager.TYPE_WIFI -> return TYPE_WIFI//WIFI
            else -> {
            }
        }
        return -1000
    }


    @SuppressLint("MissingPermission")
            /**
     * 获取网络状态
     *
     * @param context
     * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
     * @permission android.permission.ACCESS_NETWORK_STATE
     */
    fun getNetWorkStatesString(): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected) {
            return "当前网络不可以用，请检查网络设置"//没网
        }

        val type = activeNetworkInfo.type
        when (type) {
            ConnectivityManager.TYPE_MOBILE -> return "移动数据"//移动数据
            ConnectivityManager.TYPE_WIFI -> return "WIFI"//WIFI
            else -> {
            }
        }
        return "当前网络不可以用，请检查网络设置"
    }

    fun registerReceiver(listener:TrustHttpUtilsListener) {
       val connectivityManager = TrustAppUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork( NetworkRequest.Builder().build(),object : ConnectivityManager.NetworkCallback(){

            override fun onLost(network: Network?) {
                super.onLost(network)
                listener.onLost()
//                showToast("当前网络不可以用，请检查网络设置")
                TrustLogUtils.d(TAG,"onLost ")
            }

            override fun onAvailable(network: Network?) {
                super.onAvailable(network)
                listener.onAvailable()
//                showToast("网络正常")
                TrustLogUtils.d(TAG,"onAvailable")
            }
        })

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(TrustHttp(),intentFilter)
    }


    interface TrustHttpUtilsListener{
        fun onLost()
        fun onAvailable()
    }

    class TrustHttp: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("testLhh","onReceive")
        }
    }


    private fun showToast(msg:String){
        Toast.makeText(TrustAppUtils.getContext(),msg,Toast.LENGTH_LONG).show()
    }

}