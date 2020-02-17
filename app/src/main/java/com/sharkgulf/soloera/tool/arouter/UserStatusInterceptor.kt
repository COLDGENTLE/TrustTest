package com.sharkgulf.soloera.tool.arouter

import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 *  Created by user on 2019/7/12
 *  路由检查用户是否登陆未登陆跳转页面
 */
@Interceptor(priority = 8, name = "UserStatusInterceptor")
class UserStatusInterceptor : IInterceptor {
    private var mContext:Context? = null
    private var mStatus = false
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        Log.d("UserStatusInterceptor","process   ${postcard?.path}")


        callback?.onContinue(postcard)
    }

    override fun init(context: Context?) {
        mContext = context
        Log.d("UserStatusInterceptor","init")
    }
}