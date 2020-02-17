package com.sharkgulf.soloera.tool

import android.widget.Toast
import com.trust.demo.basis.trust.utils.TrustAppUtils

/**
 *  Created by user on 2019/11/6
 */
class ToastUtils {
    companion object {
        private var mToastUtils:ToastUtils? = null

        fun getiInstance():ToastUtils{
            if (mToastUtils == null) {
                mToastUtils = ToastUtils()
            }
            return mToastUtils!!
        }

    }



    fun showShortToast(msg:String) {
        val toast = Toast.makeText(TrustAppUtils.getContext(), "", Toast.LENGTH_SHORT)
        toast.setText(msg)
        toast.show()
    }

    fun showLongToast(msg:String) {
        val toast = Toast.makeText(TrustAppUtils.getContext(), null, Toast.LENGTH_LONG)
        toast.setText(msg)
        toast.show()
    }









}