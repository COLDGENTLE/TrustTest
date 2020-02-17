package com.trust.demo.basis.base

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

/**
 *  Created by user on 2019/10/28
 */

fun baseSetOnclickListener(v:View,seconds:Long = 1,listener:View.OnClickListener){
    RxView
            .clicks(v)
            .throttleFirst(seconds, TimeUnit.SECONDS)
            .subscribe { listener.onClick(v) }
}