package com.sharkgulf.soloera.tool.view.bsedittext

import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R

/**
 *  Created by user on 2019/11/6
 */
class BsEditText {

    private var editText:EditText? = null
    private var ediTextTypeIc:ImageView? = null
    private var ediTextIc:ImageView? = null
    private var ediTextTypetv:TextView? = null
    private var isShow = false

    private var ediTextIcIsShowIc:Int? = null
    private var ediTextIcNoShowIc:Int? = null

    //输入框 右侧图标作用  false  是清除内容  true 是切换pwd 状态
    private var isTypePwd = false

    fun initView(v:View,isTypePwd:Boolean = false,editTextTypeIc:Int? = null,hint:String,isShowIc:Int,noShowIc:Int? = null):BsEditText{
        this.isTypePwd = isTypePwd
        editText = v.findViewById(R.id.bs_edit_text_ed)
        editText?.hint = hint
        ediTextTypeIc = v.findViewById(R.id.bs_edit_text_type_ic)
        ediTextIc = v.findViewById(R.id.bs_edit_text_ic)
        ediTextTypetv = v.findViewById(R.id.bs_edit_text_type_tv)
        ediTextIc?.setOnClickListener(ediTextIcListener)
        changeEd()

        ediTextIcIsShowIc = isShowIc
        ediTextIcNoShowIc = noShowIc
        ediTextIc?.setImageResource(ediTextIcIsShowIc!!)
        if (editTextTypeIc != null) {
            ediTextTypeIc?.visibility = View.VISIBLE
            ediTextTypeIc?.setImageResource(editTextTypeIc)
        }else{
            ediTextTypeIc?.visibility = View.GONE
        }
        return this
    }



    private val ediTextIcListener = View.OnClickListener {
        if (isTypePwd) {
            ediTextIc?.setImageResource(if (isShow) ediTextIcIsShowIc!! else ediTextIcNoShowIc!!)
            isShow = !isShow
            changeEd()
        }else{
            editText?.setText("")
        }

    }

    private fun changeEd() {
        val type = if (isShow) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD
        editText?.inputType = InputType.TYPE_CLASS_TEXT or type
    }
}