package com.sharkgulf.soloera.tool.view.layout.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.config.getBsColor

class BatteryBgView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mPaint:Paint = Paint()

    init {
        mPaint.color = getBsColor(R.color.blue0ff)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 8f
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)



        canvas?.drawCircle(width/2f,-800f,height + 800f,mPaint)
    }


    fun setBgColor(color: Int){
        mPaint.color = color
        invalidate()
    }
}