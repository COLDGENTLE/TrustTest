package com.sharkgulf.soloera.tool.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.TextView
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.view.View
import com.sharkgulf.soloera.R


/**
 *  Created by user on 2019/11/14
 */
class WaveProgressView@JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private val DEFAULT_WAVE_COLOR = "#FFCDE5FD"   // 默认的波纹颜色
    private val DEFAULT_WAVE_BG_COLOR = "#FFF1F7FF"    // 默认波纹背景色
    private val DEFAULT_TEXT_HINT_COLOR = "#FF878889" // 默认提示字体颜色
    private val DEFAULT_TEXT_COLOR = "#FF288DEB"    // 默认字体颜色
    private val DEFAULT_STROKE_COLOR = "#00000000" // 默认背景下，边框颜色
    private val DEFAULT_STROKE_RADIO = 0 // 默认背景下线宽占view宽比
    private val DEFAULT_SPACE_RADIO = 0   // 默认背景下线与波纹图片间距占view宽比

    private var mPath: Path? = null

    private var mIsAutoBack = false    // 用于判断是否需要一个默认的背景（默认为圆形背景）

    private var mBackground: Bitmap? = null // 用于保存与波浪进行图形计算的背景
    private var mTmpBackground: Drawable? = null  // 保存背景，因为要把背景清空

    private var mWidth: Int = 0 // 控件宽度，默认的话则认为是200
    private var mHeight: Int = 0    // 控件高度，默认的话则认为是200

    private var mWavePaint: Paint? = null   // 绘制波纹画笔
    private var mStrokePaint: Paint? = null // 绘制默认背景边框的画笔
    private var mTextPaint: Paint? = null   // 文字画笔

    private var mWaveCount = 0 // 波纹个数（计算得来）
    private var mWaveWidth: Float = 0.toFloat() // 波纹宽度（一个起伏）
    private var mHalfWaveWidth = mWaveWidth / 4  // 四分之一的波纹宽
    private var mWaveHeight: Float = 0.toFloat()   // 波纹高度
    private var mDistance = 0f    // 波纹总偏移量

    private var mSpeed: Float = 0.toFloat()    // 每帧偏移量

    private var mMaxProgress: Float = 0.toFloat() // 最大进度
    private var mProgress: Float = 0.toFloat() // 当前进度

    private var mStrokeWidth: Float = 0.toFloat()   // 默认背景时的线宽
    private var mWaveColor: Int = 0  // 波纹颜色
    private var mWaveBackgroundColor: Int = 0 // 波纹背景色
    private var mStrokeColor: Int = 0  // 边框颜色

    private var mHintColor = Color.parseColor(DEFAULT_TEXT_HINT_COLOR) // 提示文本颜色
    private var mTextColor: Int = 0  // 文本颜色
    private var mHint = "可用额度"   // 提示文本
    private var mText = "￥80,000.00"   // 文本
    private var mTextSize: Int = 0 // sp
    private var mHintSize: Int = 0 // sp
    private var mTextSpace = 10f
    private var mTextRect: Rect? = null

    init {
        inits(context,attrs)
    }

    private fun inits(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.WaveProgressViews)

        mMaxProgress = ta.getInt(R.styleable.WaveProgressViews_wave_progress_max, 100).toFloat()
        mProgress = ta.getInt(R.styleable.WaveProgressViews_wave_progress, 50).toFloat()
        mWaveWidth = ta.getFloat(R.styleable.WaveProgressViews_swave_width, 200f)
        mHalfWaveWidth = mWaveWidth / 4
        mWaveHeight = ta.getFloat(R.styleable.WaveProgressViews_swave_height, 20f)
        mSpeed = ta.getFloat(R.styleable.WaveProgressViews_wave_speed, mWaveWidth / 70)
        mWaveColor = ta.getColor(R.styleable.WaveProgressViews_swave_color,
                Color.parseColor(DEFAULT_WAVE_COLOR))
        mWaveBackgroundColor = ta.getColor(R.styleable.WaveProgressViews_swave_bg_color,
                Color.parseColor(DEFAULT_WAVE_BG_COLOR))
        mStrokeColor = ta.getColor(R.styleable.WaveProgressViews_stroke_color,
                Color.parseColor(DEFAULT_STROKE_COLOR))
        mText = ta.getString(R.styleable.WaveProgressViews_main_text)
        if (mText == null) {
            mText = ""
        }
        mTextColor = ta.getColor(R.styleable.WaveProgressViews_main_text_color,
                Color.parseColor(DEFAULT_TEXT_COLOR))
        mTextSize = ta.getDimensionPixelSize(R.styleable.WaveProgressViews_main_text_size, sp2px(16))
        mHint = ta.getString(R.styleable.WaveProgressViews_hint_text)
        if (mHint == null) {
            mHint = ""
        }
        mHintColor = ta.getColor(R.styleable.WaveProgressViews_hint_color,
                Color.parseColor(DEFAULT_TEXT_COLOR))
        mHintSize = ta.getDimensionPixelSize(R.styleable.WaveProgressViews_hint_size, sp2px(14))
        mTextSpace = ta.getDimension(R.styleable.WaveProgressViews_text_space, 10f)

        ta.recycle()

        mPath = Path()
        mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mWavePaint!!.style = Paint.Style.FILL
        // 设置波纹颜色
        mWavePaint!!.color = mWaveColor
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mTextRect = Rect()
        mTmpBackground = background
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = measureWidth(widthMeasureSpec)
        val measuredHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
        if (null == mTmpBackground) {
            mIsAutoBack = true
            val min = Math.min(measuredWidth, measuredHeight)
            mStrokeWidth = DEFAULT_STROKE_RADIO * min.toFloat()
            val spaceWidth = DEFAULT_SPACE_RADIO * min   // 默认背景时，线和波纹图片间距
            mWidth = (min - (mStrokeWidth + spaceWidth) * 2) .toInt()
            mHeight = (min - (mStrokeWidth + spaceWidth) * 2) .toInt()
            mBackground = autoCreateBitmap(mWidth / 2)
        } else {
            mIsAutoBack = false
            mBackground = getBitmapFromDrawable(mTmpBackground)
            if (mBackground != null && !mBackground!!.isRecycled) {
                mWidth = mBackground!!.width
                mHeight = mBackground!!.height
            }
        }
        mWaveCount = calWaveCount(mWidth, mWaveWidth)
    }

    /**
     * 创建默认是圆形的背景
     *
     * @param radius 半径
     * @return 背景图
     */
    private fun autoCreateBitmap(radius: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = mWaveBackgroundColor
        p.style = Paint.Style.FILL
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), p)
        return bitmap
    }

    /**
     * 从drawable中获取bitmap
     */
    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (null == drawable) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: OutOfMemoryError) {
            return null
        }

    }

    /**
     * 测量view高度，如果是wrap_content，则默认是200
     */
    private fun measureHeight(heightMeasureSpec: Int): Int {
        var height = 0
        val mode = View.MeasureSpec.getMode(heightMeasureSpec)
        val size = View.MeasureSpec.getSize(heightMeasureSpec)
        if (mode == View.MeasureSpec.EXACTLY) {
            height = size
        } else if (mode == View.MeasureSpec.AT_MOST) {
            if (null != mTmpBackground) {
                height = mTmpBackground!!.minimumHeight
            } else {
                height = 400
            }
        }
        return height
    }

    /**
     * 测量view宽度，如果是wrap_content，则默认是200
     */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        var width = 0
        val mode = View.MeasureSpec.getMode(widthMeasureSpec)
        val size = View.MeasureSpec.getSize(widthMeasureSpec)
        if (mode == View.MeasureSpec.EXACTLY) {
            width = size
        } else if (mode == View.MeasureSpec.AT_MOST) {
            if (null != mTmpBackground) {
                width = mTmpBackground!!.minimumWidth
            } else {
                width = 400
            }
        }
        return width
    }

    protected override fun onDraw(canvas: Canvas) {
        val bitmap = createWaveBitmap(mWidth, mHeight)
        if (mIsAutoBack) {  // 如果没有背景，就画默认背景
            if (null == mStrokePaint) {
                mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
                mStrokePaint!!.color = mStrokeColor
                mStrokePaint!!.strokeWidth = mStrokeWidth
                mStrokePaint!!.style = Paint.Style.STROKE
            }
            // 默认背景下先画个边框
            val radius = Math.min(measuredWidth / 2, measuredHeight / 2).toFloat()
            canvas.drawCircle((measuredWidth / 2).toFloat(), (measuredHeight / 2).toFloat(), radius - mStrokeWidth / 2, mStrokePaint)
            val left = measuredWidth / 2 - mWidth / 2
            val top = measuredHeight / 2 - mHeight / 2
            canvas.drawBitmap(bitmap, left.toFloat(), top.toFloat(), null)
        } else {
            canvas.drawBitmap(bitmap, 0.toFloat(), 0.toFloat(), null)
        }
        // 画文字
        if (!TextUtils.isEmpty(mText)) {
            mTextPaint!!.setColor(mTextColor)
            mTextPaint!!.setTextSize(mTextSize.toFloat())
            mTextPaint!!.getTextBounds(mText, 0, mText.length - 1, mTextRect)
            val textLength = mTextPaint!!.measureText(mText)
            //            canvas.drawText(mText, getMeasuredWidth() / 2 - mTextRect.width() / 2,
            //                    getMeasuredHeight() / 2 + mTextRect.height() / 2, mTextPaint);  引以为戒
            val metrics = mTextPaint!!.getFontMetrics()
            val baseLine = mTextRect!!.height() / 2 + (metrics.descent - metrics.ascent) / 2 - metrics.descent
            canvas.drawText(mText, measuredWidth / 2 - textLength / 2,
                    measuredHeight / 2 + baseLine, mTextPaint)
        }
        // 画提示
        if (!TextUtils.isEmpty(mHint)) {
            mTextPaint!!.setColor(mHintColor)
            mTextPaint!!.setTextSize(mHintSize.toFloat())
            val hintLength = mTextPaint!!.measureText(mHint)
            canvas.drawText(mHint, measuredWidth / 2 - hintLength / 2,
                    measuredHeight / 2 - mTextRect!!.height() - mTextSpace, mTextPaint)
        }
        postInvalidateDelayed(10)
    }

    /**
     * 绘制重叠的bitmap，注意：没有背景则默认是圆形的背景，有则是背景
     *
     * @param width  背景高
     * @param height 背景宽
     * @return 带波纹的图
     */
    private fun createWaveBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 计算波浪位置
        val mCurY = (height * (mMaxProgress - mProgress) / mMaxProgress).toInt()

        // 画path
        mPath!!.reset()
        mPath!!.moveTo(-mDistance, mCurY.toFloat())
        for (i in 0 until mWaveCount) {
            mPath!!.quadTo(i * mWaveWidth + mHalfWaveWidth - mDistance, mCurY - mWaveHeight,
                    i * mWaveWidth + mHalfWaveWidth * 2 - mDistance, mCurY.toFloat())    // 起
            mPath!!.quadTo(i * mWaveWidth + mHalfWaveWidth * 3 - mDistance, mCurY + mWaveHeight,
                    i * mWaveWidth + mHalfWaveWidth * 4 - mDistance, mCurY.toFloat())    // 伏
        }
        mPath!!.lineTo(width.toFloat(), height.toFloat())
        mPath!!.lineTo(0F, height.toFloat())
        mPath!!.close()
        canvas.drawPath(mPath, mWavePaint)

        mDistance += mSpeed
        mDistance %= mWaveWidth

        mWavePaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        canvas.drawBitmap(mBackground, 0.toFloat(), 0.toFloat(), mWavePaint)
        return bitmap
    }

    /**
     * 计算波纹数目
     *
     * @param width     波纹图宽度
     * @param waveWidth 每条波纹的宽度
     * @return 波纹数目
     */
    private fun calWaveCount(width: Int, waveWidth: Float): Int {
        val count: Int
        if (width % waveWidth == 0f) {
            count = (width / waveWidth + 1).toInt()
        } else {
            count = (width / waveWidth + 2).toInt()
        }
        return count
    }

    /**
     * 设置最大进度
     */
    fun setMax(max: Float) {
        mMaxProgress = max
    }

    /**
     * 设置当前进度
     */
    fun setProgress(progress: Float) {
        mProgress = progress
    }

    /**
     * 获取当前进度
     */
    fun getProgress(): Float {
        return mProgress
    }

    /**
     * 设置波纹速度
     */
    fun setSpeed(speed: Float) {
        mSpeed = speed
    }

    /**
     * 设置波纹尺寸
     *
     * @param waveWidth  波纹宽度,默认200
     * @param waveHeight 波纹高度,默认20
     */
    fun setWave(waveWidth: Float, waveHeight: Float) {
        mWaveWidth = waveWidth
        mWaveHeight = waveHeight
        mHalfWaveWidth = waveWidth / 4
        mWaveCount = calWaveCount(mWidth, mWaveWidth)
    }

    /**
     * 设置波纹颜色
     *
     * @param waveColor 颜色值，默认是淡蓝色
     */
    fun setWaveColor(waveColor: Int) {
        this.mWaveColor = waveColor
        mWavePaint!!.setColor(mWaveColor)
    }

    /**
     * 设置波纹背景色
     *
     * @param waveBackgroundColor 波纹背景色，必须是在无背景下才会有效果
     */
    fun setWaveBackgroundColor(waveBackgroundColor: Int) {
        this.mWaveBackgroundColor = waveBackgroundColor
        if (mIsAutoBack) {
            mBackground = autoCreateBitmap(mWidth / 2)
        }
    }

    /**
     * 设置边框颜色
     *
     * @param strokeColor 边框颜色，必须是在无背景下才会有效果
     */
    fun setStrokeColor(strokeColor: Int) {
        this.mStrokeColor = strokeColor
        if (mStrokePaint != null) {
            mStrokePaint!!.color = mStrokeColor
        }
    }

    /**
     * 设置文本字体大小
     *
     * @param textSize 文本字体大小,sp
     */
    fun setTextSize(textSize: Int) {
        this.mTextSize = sp2px(textSize)
    }

    /**
     * 设置文本字体颜色
     *
     * @param textColor 文本字体颜色
     */
    override fun setTextColor(textColor: Int) {
        this.mTextColor = textColor
    }

    /**
     * 设置文本
     *
     * @param text 文本
     */
    fun setText(text: String) {
        this.mText = text
    }

    /**
     * 设置提示文本字体大小
     *
     * @param hintSize 提示文本字体大小,sp
     */
    fun setHintSize(hintSize: Int) {
        this.mHintSize = sp2px(hintSize)
    }

    /**
     * 设置提示文本字体颜色
     *
     * @param hintColor 提示文本字体颜色
     */
    fun setHintColor(hintColor: Int) {
        this.mHintColor = hintColor
    }

    /**
     * 设置提示文本
     *
     * @param hint 提示文本
     */
    fun setHint(hint: String) {
        this.mHint = hint
    }

    /**
     * 设置提示与文本间距
     *
     * @param textSpace 提示与文本间距,dp
     */
    fun setTextSpace(textSpace: Int) {
        this.mTextSpace = dp2px(textSpace.toFloat())
    }

    /**
     * sp转换成px
     *
     * @param spVal sp
     * @return px
     */
    fun sp2px(spVal: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal.toFloat(), context.resources.displayMetrics).toInt()
    }

    /**
     * dp转换成px
     *
     * @param dpVal dp
     * @return px
     */
    fun dp2px(dpVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal.toFloat(), context.resources.displayMetrics).toInt().toFloat()
    }
}