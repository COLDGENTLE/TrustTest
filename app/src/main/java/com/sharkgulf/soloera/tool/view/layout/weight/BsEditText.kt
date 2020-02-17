package com.sharkgulf.soloera.tool.view.layout.weight

import android.content.Context
import android.graphics.Canvas
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.sharkgulf.soloera.R

/**
 *  Created by user on 2019/10/25
 */
class BsEditText@JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mEditText:EditText? = null
    private var mInputTypeBtn:ImageView? = null
    private var mClearBtn:ImageView? = null

    private var mPwdIsShow = true
    init {
        this.orientation = LinearLayout.HORIZONTAL

        mEditText = EditText(context)
        mInputTypeBtn = ImageView(context)
        mClearBtn = ImageView(context)


        mEditText?.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        mEditText?.background = null
        mInputTypeBtn?.setPadding(20,20,20,20)
        mClearBtn?.setPadding(20,20,20,20)
        mInputTypeBtn?.setImageResource(R.drawable.del_ic)
        mClearBtn?.setImageResource(R.drawable.del_ic)

        mInputTypeBtn?.setOnClickListener { changeEditTextType() }
        mClearBtn?.setOnClickListener { clearEditText() }

        addView(mEditText)
        addView(mInputTypeBtn)
        addView(mClearBtn)

    }

    private fun clearEditText(){
        val childAt = getChildAt(0) as EditText
        childAt.setText("")
    }
    private fun changeEditTextType(){
        val childAt = getChildAt(0) as EditText
        val status = if (mPwdIsShow) {
            mPwdIsShow = false
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }else{
            mPwdIsShow = true
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        }
        childAt.inputType =status
    }

    fun getEditTextString():String?{
        return getEditText().text.toString()
    }

    fun showPwdChangeBtn(){
        getPwdBtn().visibility = View.VISIBLE
    }

    fun setEditText(listener: TextWatcher){
        getEditText().addTextChangedListener(listener)
    }

    private fun getEditText():EditText{
        return getChildAt(0) as EditText
    }

    private fun getPwdBtn():View{
        return getChildAt(1)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mEditText?.layoutParams = LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,5.0f)
        mInputTypeBtn?.layoutParams =  LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f)
        mClearBtn?.layoutParams =  LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f)
    }
}