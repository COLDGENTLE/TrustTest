package com.trust.demo.basis.trust.utils

import com.google.gson.JsonSyntaxException
import com.trust.demo.basis.R
import com.trust.demo.basis.base.TrustApplication
import com.trust.demo.basis.trust.TrustTools
import java.lang.reflect.UndeclaredThrowableException
import java.net.UnknownHostException

/**
 *  Created by user on 2019/1/22
 */
class TrustHttpErrorUtils{
    companion object {
        fun checkHttpError(e:Throwable):String?{
            return when (e) {
                is JsonSyntaxException,
                is UndeclaredThrowableException-> {
                    getString(R.string.JsonSyntaxException)
                }
                is UnknownHostException -> {
                    getString(R.string.UnknownHostException)
                }
                else -> {
                    if (e.message != null) {
                        e.message

                    }else{
                        e.toString()
                    }
                }
            }
        }

        fun getString(id:Int) = TrustTools.getString(TrustApplication.getContexts(), id)
    }

}