package com.trust.demo.basis.trust.utils

import android.widget.EditText

/**
 *  Created by user on 2019/11/25
 */

fun setEditText(ed:EditText,maxNum:Int){
    ed.filters = arrayOf(EditTextInputFilter(maxNum))
}