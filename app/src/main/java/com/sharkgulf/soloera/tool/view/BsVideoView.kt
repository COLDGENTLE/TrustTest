package com.sharkgulf.soloera.tool.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView

class BsVideoView@JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.getDefaultSize(0,widthMeasureSpec)
        val height = View.getDefaultSize(0,heightMeasureSpec)
        setMeasuredDimension(width,height)
    }
}