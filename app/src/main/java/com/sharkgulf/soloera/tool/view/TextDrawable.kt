package com.sharkgulf.soloera.tool.view

import android.graphics.drawable.Drawable
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import android.content.res.ColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import com.sharkgulf.soloera.R


/**
 *  Created by user on 2019/11/8
 */
class TextDrawable @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private var drawableLeft: Drawable? = null
    private var drawableRight: Drawable? = null
    private var drawableTop: Drawable? = null
    private var leftWidth: Int = 0
    private var rightWidth: Int = 0
    private var topWidth: Int = 0
    private var leftHeight: Int = 0
    private var rightHeight: Int = 0
    private var topHeight: Int = 0
    private var mContext: Context? = context

    init { init(context,attrs) }



    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextDrawable)
        drawableLeft = typedArray.getDrawable(R.styleable.TextDrawable_leftDrawable)
        drawableRight = typedArray.getDrawable(R.styleable.TextDrawable_rightDrawable)
        drawableTop = typedArray.getDrawable(R.styleable.TextDrawable_topDrawable)
        if (drawableLeft != null) {
            leftWidth = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_leftDrawableWidth, dip2px(context, 20f))
            leftHeight = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_leftDrawableHeight, dip2px(context, 20f))
        }
        if (drawableRight != null) {
            rightWidth = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_rightDrawableWidth, dip2px(context, 20f))
            rightHeight = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_rightDrawableHeight, dip2px(context, 20f))
        }
        if (drawableTop != null) {
            topWidth = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_topDrawableWidth, dip2px(context, 20f))
            topHeight = typedArray.getDimensionPixelOffset(R.styleable.TextDrawable_topDrawableHeight, dip2px(context, 20f))
        }
    }


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (drawableLeft != null) {
            drawableLeft!!.setBounds(0, 0, leftWidth, leftHeight)
        }
        if (drawableRight != null) {
            drawableRight!!.setBounds(0, 0, rightWidth, rightHeight)
        }
        if (drawableTop != null) {
            drawableTop!!.setBounds(0, 0, topWidth, topHeight)
        }
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, null)
    }

    /**
     * 设置左侧图片并重绘
     */
    fun setDrawableLeft(drawableLeft: Drawable) {
        this.drawableLeft = drawableLeft
        invalidate()
    }

    fun setDrawableLeftColor(color:Int){
        if (this.drawableLeft != null) { tintDrawable(this.drawableLeft!!,ColorStateList.valueOf(color)) }
        invalidate()
    }

    /**
     * 设置左侧图片并重绘
     */
    fun setDrawableLeft(drawableLeftRes: Int) {
        this.drawableLeft = mContext!!.getResources().getDrawable(drawableLeftRes)
        invalidate()
    }

    /**
     * 设置右侧图片并重绘
     */
    fun setDrawableRight(drawableRight: Drawable?) {
        this.drawableRight = drawableRight
        invalidate()
    }

    /**
     * 设置右侧图片并重绘
     */
    fun setDrawableRight(drawableRightRes: Int) {
        this.drawableRight = mContext!!.getResources().getDrawable(drawableRightRes,null)
        invalidate()
    }

    fun setDrawableRightColor(color:Int){
        if (this.drawableRight != null) { tintDrawable(this.drawableRight!!,ColorStateList.valueOf(color)) }
        invalidate()
    }

    fun setDrawableLigtColor(color:Int){
        if (this.drawableLeft != null) { tintDrawable(this.drawableLeft!!,ColorStateList.valueOf(color)) }
        invalidate()
    }

    /**
     * 设置上部图片并重绘
     */
    fun setDrawableTop(drawableTop: Drawable) {
        this.drawableTop = drawableTop
        invalidate()
    }

    /**
     * 设置右侧图片并重绘
     */
    fun setDrawableTop(drawableTopRes: Int) {
        this.drawableTop = mContext!!.getResources().getDrawable(drawableTopRes)
        invalidate()
    }


    private fun tintDrawable(drawable: Drawable, colors: ColorStateList): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }
}