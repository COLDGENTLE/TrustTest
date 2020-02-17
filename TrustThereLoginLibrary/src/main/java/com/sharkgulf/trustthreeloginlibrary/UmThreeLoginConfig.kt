package com.sharkgulf.trustthreeloginlibrary

import android.app.Activity
import android.content.Context
import android.util.Log
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA


/**
 *  Created by user on 2019/4/25
 */
class UmThreeLoginConfig(context: Context) {
    companion object {
        private var mUmThereLoginConfig:UmThreeLoginConfig? = null
        fun getConfig(context: Context):UmThreeLoginConfig{
            if (mUmThereLoginConfig == null) {
                mUmThereLoginConfig = UmThreeLoginConfig(context)
            }
            return mUmThereLoginConfig!!
        }
    }
    private var umShareAPI: UMShareAPI? = null
    private var mSHARE_MEDIA :SHARE_MEDIA? = null
    private val WX = "wx"
    private val QQ = "qq"
    private val SINA = "sina"
    private var mType = QQ
    private var mActivity:Activity? = null
    private var mThreeListener:ThreeListener? = null
    private var umAuthListener: UMAuthListener = object :UMAuthListener{
        override fun onComplete(platform: SHARE_MEDIA?, action: Int, data: MutableMap<String, String>?) {
            Log.d("lhh","onComplete $mType")
            if (data != null) {
                mThreeListener?.resultUserinfo(mType, data)
                deleteThreeLogin()
            }
        }

        override fun onCancel(platform: SHARE_MEDIA?, action: Int) {
            Log.d("lhh","onCancel")
        }

        override fun onError(platform: SHARE_MEDIA?, action: Int, t: Throwable?) {
            val error = when (platform) {
                SHARE_MEDIA.QQ -> {
                    "qq"+t.toString()
                }
                SHARE_MEDIA.WEIXIN -> {
                    if (action == 0) {
                        "请先安装微信"
                    }else{
                        t.toString()
                    }
                }
                SHARE_MEDIA.SINA -> {
                    "授权失败"+t.toString()
                }
                else -> {
                    t.toString()
                }
            }


            mThreeListener?.reusltError(error)
            Log.d("lhh","onError $platform action $action t  $t")
        }

        override fun onStart(platform: SHARE_MEDIA?) {
            Log.d("lhh","onStart")
        }

    }

    init {
        umShareAPI = UMShareAPI.get(context)
    }

    fun startQQLogin(activity: Activity,listener:ThreeListener){
        mSHARE_MEDIA = SHARE_MEDIA.QQ
        mThreeListener = listener
        mType = QQ
        mActivity = activity
        umShareAPI?.getPlatformInfo(activity, mSHARE_MEDIA, umAuthListener);//QQ登录
    }

    fun startSinaLogin(activity: Activity,listener:ThreeListener){
        mSHARE_MEDIA = SHARE_MEDIA.SINA
        mThreeListener = listener
        mType = SINA
        mActivity = activity
        umShareAPI?.getPlatformInfo(activity, mSHARE_MEDIA, umAuthListener)
    }

    fun startWxLogin(activity: Activity,listener: ThreeListener){
        mSHARE_MEDIA = SHARE_MEDIA.WEIXIN
        mThreeListener = listener
        mType = WX
        mActivity = activity
        umShareAPI?.getPlatformInfo(activity, mSHARE_MEDIA, umAuthListener)
    }

    fun deleteThreeLogin(){
        if (mActivity != null) {
            umShareAPI?.deleteOauth(mActivity, mSHARE_MEDIA, umAuthListener);
        }
    }

    interface ThreeListener{
        fun resultUserinfo(partner:String,data :MutableMap<String, String>)
        fun reusltError( error: String)
        fun reusltImport(msg:String)
    }
}