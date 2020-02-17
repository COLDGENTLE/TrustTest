package com.sharkgulf.soloera.tool.view

import android.view.Gravity
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.sharkgulf.soloera.R


/**
 *  Created by user on 2019/9/5
 */
class HoloCircularProgressBar
/**
 * Instantiates a new holo circular progress bar.
 *
 * @param context  the context
 * @param attrs    the attrs
 * @param defStyle the def style
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                          defStyle: Int = R.attr.circularProgressBarStyle) : View(context, attrs, defStyle) {

    /**
     * The rectangle enclosing the circle.
     */
    private val mCircleBounds = RectF()

    /**
     * the rect for the thumb square
     */
    private val mSquareRect = RectF()

    /**
     * the paint for the background.
     */
    private var mBackgroundColorPaint = Paint()

    /**
     * The stroke width used to paint the circle.
     */
    var circleStrokeWidth = 10
        private set

    /**
     * The gravity of the view. Where should the Circle be drawn within the given bounds
     *
     * [.computeInsets]
     */
    private var mGravity = Gravity.CENTER

    /**
     * The Horizontal inset calcualted in [.computeInsets] depends on [ ][.mGravity].
     */
    private var mHorizontalInset = 0

    /**
     * true if not all properties are set. then the view isn't drawn and there are no errors in the
     * LayoutEditor
     */
    private var mIsInitializing = true

    /**
     * flag if the marker should be visible
     */
    /**
     * @return true if the marker is visible
     */
    /**
     * Sets the marker enabled.
     *
     * @param enabled the new marker enabled
     */
    var isMarkerEnabled = false

    /**
     * indicates if the thumb is visible
     */
    /**
     * @return true if the marker is visible
     */
    /**
     * shows or hides the thumb of the progress bar
     *
     * @param enabled true to show the thumb
     */
    var isThumbEnabled = true

    /**
     * The Marker color paint.
     */
    private var mMarkerColorPaint: Paint? = null

    /**
     * The Marker progress.
     */
    /**
     * similar to [.getProgress]
     */
    /**
     * Sets the marker progress.
     *
     * @param progress the new marker progress
     */
    var markerProgress = 0.0f
        set(progress) {
            isMarkerEnabled = true
            field = progress
        }

    /**
     * the overdraw is true if the progress is over 1.0.
     */
    private var mOverrdraw = false

    /**
     * The current progress.
     */
    /**
     * gives the current progress of the ProgressBar. Value between 0..1 if you set the progress to
     * >1 you'll get progress % 1 as return value
     *
     * @return the progress
     */
    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    var progress = 0.3f
        set(progress) {
            if (progress == this.progress) {
                return
            }

            if (progress == 1f) {
                mOverrdraw = false
                field = 1f
            } else {

                if (progress >= 1) {
                    mOverrdraw = true
                } else {
                    mOverrdraw = false
                }

                field = progress % 1.0f
            }

            if (!mIsInitializing) {
                invalidate()
            }
        }

    /**
     * The color of the progress background.
     */
    private var mProgressBackgroundColor: Int = 0

    /**
     * the color of the progress.
     */
    private var mProgressColor: Int = 0

    /**
     * paint for the progress.
     */
    private var mProgressColorPaint: Paint? = null

    /**
     * Radius of the circle
     *
     *
     *  Note: (Re)calculated in [.onMeasure].
     */
    private var mRadius: Float = 0.toFloat()

    /**
     * The Thumb color paint.
     */
    private var mThumbColorPaint = Paint()

    /**
     * The Thumb pos x.
     *
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in [.onMeasure]
     */
    private var mThumbPosX: Float = 0.toFloat()

    /**
     * The Thumb pos y.
     *
     * Care. the position is not the position of the rotated thumb. The position is only calculated
     * in [.onMeasure]
     */
    private var mThumbPosY: Float = 0.toFloat()

    /**
     * The pointer width (in pixels).
     */
    private var mThumbRadius = 20

    /**
     * The Translation offset x which gives us the ability to use our own coordinates system.
     */
    private var mTranslationOffsetX: Float = 0.toFloat()

    /**
     * The Translation offset y which gives us the ability to use our own coordinates system.
     */
    private var mTranslationOffsetY: Float = 0.toFloat()

    /**
     * The Vertical inset calcualted in [.computeInsets] depends on [ ][.mGravity]..
     */
    private var mVerticalInset = 0

    /**
     * Gets the progress color.
     *
     * @return the progress color
     */
    /**
     * Sets the progress color.
     *
     * @param color the new progress color
     */
    var progressColor: Int
        get() = mProgressColor
        set(color) {
            mProgressColor = color

            updateProgressColor()
        }

    /**
     * Gets the current rotation.
     *
     * @return the current rotation
     */
    private val currentRotation: Float
        get() = 360 * progress

    /**
     * Gets the marker rotation.
     *
     * @return the marker rotation
     */
    private val markerRotation: Float
        get() = 360 * markerProgress

    init {

        // load the styled attributes and set their properties
        val attributes = context
                .obtainStyledAttributes(attrs, R.styleable.HoloCircularProgressBar,
                        defStyle, 0)
        if (attributes != null) {
            try {
                progressColor = attributes!!
                        .getColor(R.styleable.HoloCircularProgressBar_progress_color, Color.CYAN)
                setProgressBackgroundColor(attributes!!
                        .getColor(R.styleable.HoloCircularProgressBar_progress_background_color,
                                Color.GREEN))
                progress = attributes!!.getFloat(R.styleable.HoloCircularProgressBar_progress, 0.0f)
                markerProgress = attributes!!.getFloat(R.styleable.HoloCircularProgressBar_marker_progress,
                        0.0f)
                setWheelSize(attributes!!
                        .getDimension(R.styleable.HoloCircularProgressBar_stroke_width, 10f).toInt())
                isThumbEnabled = attributes!!
                        .getBoolean(R.styleable.HoloCircularProgressBar_thumb_visible, true)
                isMarkerEnabled = attributes!!
                        .getBoolean(R.styleable.HoloCircularProgressBar_marker_visible, true)

                mGravity = attributes!!
                        .getInt(R.styleable.HoloCircularProgressBar_android_gravity,
                                Gravity.CENTER)
            } finally {
                // make sure recycle is always called.
                attributes!!.recycle()
            }
        }

        mThumbRadius = circleStrokeWidth * 2

        updateBackgroundColor()

        updateMarkerColor()

        updateProgressColor()

        // the view has now all properties and can be drawn
        mIsInitializing = false

    }

    protected override fun onDraw(canvas: Canvas) {

        // All of our positions are using our internal coordinate system.
        // Instead of translating
        // them we let Canvas do the work for us.
        canvas.translate(mTranslationOffsetX, mTranslationOffsetY)

        val progressRotation = currentRotation

        // draw the background
        if (!mOverrdraw) {
            canvas.drawArc(mCircleBounds, 270f, -(360 - progressRotation), false,
                    mBackgroundColorPaint)
        }

        // draw the progress or a full circle if overdraw is true
        canvas.drawArc(mCircleBounds, 270f, if (mOverrdraw) 360f else progressRotation, false,
                mProgressColorPaint)

        // draw the marker at the correct rotated position
        if (isMarkerEnabled) {
            val markerRotation = markerRotation

            canvas.save()
            canvas.rotate(markerRotation - 90)
            canvas.drawLine((mThumbPosX + mThumbRadius / 2 * 1.4).toFloat(), mThumbPosY,
                    (mThumbPosX - mThumbRadius / 2 * 1.4).toFloat(), mThumbPosY, mMarkerColorPaint)
            canvas.restore()
        }

        if (isThumbEnabled) {
            // draw the thumb square at the correct rotated position
            canvas.save()
            canvas.rotate(progressRotation - 90)
            // rotate the square by 45 degrees
            canvas.rotate(45F, mThumbPosX, mThumbPosY)
            mSquareRect.left = mThumbPosX - mThumbRadius / 3
            mSquareRect.right = mThumbPosX + mThumbRadius / 3
            mSquareRect.top = mThumbPosY - mThumbRadius / 3
            mSquareRect.bottom = mThumbPosY + mThumbRadius / 3
            canvas.drawRect(mSquareRect, mThumbColorPaint)
            canvas.restore()
        }
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(
                getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(),
                heightMeasureSpec)
        val width = getDefaultSize(
                getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight(),
                widthMeasureSpec)

        val diameter: Int
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // ScrollView
            diameter = width
            computeInsets(0, 0)
        } else if (widthMeasureSpec == MeasureSpec.UNSPECIFIED) {
            // HorizontalScrollView
            diameter = height
            computeInsets(0, 0)
        } else {
            // Default
            diameter = Math.min(width, height)
            computeInsets(width - diameter, height - diameter)
        }

        setMeasuredDimension(diameter, diameter)

        val halfWidth = diameter * 0.5f

        // width of the drawed circle (+ the drawedThumb)
        val drawedWith: Float
        if (isThumbEnabled) {
            drawedWith = mThumbRadius * (5f / 6f)
        } else if (isMarkerEnabled) {
            drawedWith = circleStrokeWidth * 1.4f
        } else {
            drawedWith = circleStrokeWidth / 2f
        }

        // -0.5f for pixel perfect fit inside the viewbounds
        mRadius = halfWidth - drawedWith - 0.5f

        mCircleBounds.set(-mRadius, -mRadius, mRadius, mRadius)

        mThumbPosX = (mRadius * Math.cos(0.0)).toFloat()
        mThumbPosY = (mRadius * Math.sin(0.0)).toFloat()

        mTranslationOffsetX = halfWidth + mHorizontalInset
        mTranslationOffsetY = halfWidth + mVerticalInset

    }

    protected override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            progress = state.getFloat(INSTANCE_STATE_PROGRESS)
            markerProgress = state.getFloat(INSTANCE_STATE_MARKER_PROGRESS)

            val progressColor = state.getInt(INSTANCE_STATE_PROGRESS_COLOR)
            if (progressColor != mProgressColor) {
                mProgressColor = progressColor
                updateProgressColor()
            }

            val progressBackgroundColor = state
                    .getInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR)
            if (progressBackgroundColor != mProgressBackgroundColor) {
                mProgressBackgroundColor = progressBackgroundColor
                updateBackgroundColor()
            }

            isThumbEnabled = state.getBoolean(INSTANCE_STATE_THUMB_VISIBLE)

            isMarkerEnabled = state.getBoolean(INSTANCE_STATE_MARKER_VISIBLE)

            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE_SAVEDSTATE))
            return
        }

        super.onRestoreInstanceState(state)
    }

    protected override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE_SAVEDSTATE, super.onSaveInstanceState())
        bundle.putFloat(INSTANCE_STATE_PROGRESS, progress)
        bundle.putFloat(INSTANCE_STATE_MARKER_PROGRESS, markerProgress)
        bundle.putInt(INSTANCE_STATE_PROGRESS_COLOR, mProgressColor)
        bundle.putInt(INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR, mProgressBackgroundColor)
        bundle.putBoolean(INSTANCE_STATE_THUMB_VISIBLE, isThumbEnabled)
        bundle.putBoolean(INSTANCE_STATE_MARKER_VISIBLE, isMarkerEnabled)
        return bundle
    }

    /**
     * Sets the progress background color.
     *
     * @param color the new progress background color
     */
    fun setProgressBackgroundColor(color: Int) {
        mProgressBackgroundColor = color

        updateMarkerColor()
        updateBackgroundColor()
    }

    /**
     * Sets the wheel size.
     *
     * @param dimension the new wheel size
     */
    fun setWheelSize(dimension: Int) {
        circleStrokeWidth = dimension

        // update the paints
        updateBackgroundColor()
        updateMarkerColor()
        updateProgressColor()
    }

    /**
     * Compute insets.
     *
     * <pre>
     * ______________________
     * |_________dx/2_________|
     * |......| /'''''\|......|
     * |-dx/2-|| View ||-dx/2-|
     * |______| \_____/|______|
     * |________ dx/2_________|
    </pre> *
     *
     * @param dx the dx the horizontal unfilled space
     * @param dy the dy the horizontal unfilled space
     */
    @SuppressLint("NewApi")
    private fun computeInsets(dx: Int, dy: Int) {
        var absoluteGravity = mGravity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            absoluteGravity = Gravity.getAbsoluteGravity(mGravity, getLayoutDirection())
        }

        when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> mHorizontalInset = 0
            Gravity.RIGHT -> mHorizontalInset = dx
            Gravity.CENTER_HORIZONTAL -> mHorizontalInset = dx / 2
            else -> mHorizontalInset = dx / 2
        }
        when (absoluteGravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> mVerticalInset = 0
            Gravity.BOTTOM -> mVerticalInset = dy
            Gravity.CENTER_VERTICAL -> mVerticalInset = dy / 2
            else -> mVerticalInset = dy / 2
        }
    }

    /**
     * updates the paint of the background
     */
    private fun updateBackgroundColor() {
        mBackgroundColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundColorPaint.setColor(mProgressBackgroundColor)
        mBackgroundColorPaint.setStyle(Paint.Style.STROKE)
        mBackgroundColorPaint.setStrokeWidth(circleStrokeWidth.toFloat())

        invalidate()
    }

    /**
     * updates the paint of the marker
     */
    private fun updateMarkerColor() {
        mMarkerColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMarkerColorPaint!!.setColor(mProgressBackgroundColor)
        mMarkerColorPaint!!.setStyle(Paint.Style.STROKE)
        mMarkerColorPaint!!.setStrokeWidth((circleStrokeWidth / 2).toFloat())

        invalidate()
    }

    /**
     * updates the paint of the progress and the thumb to give them a new visual style
     */
    private fun updateProgressColor() {
        mProgressColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mProgressColorPaint!!.setColor(mProgressColor)
        mProgressColorPaint!!.setStyle(Paint.Style.STROKE)
        mProgressColorPaint!!.setStrokeWidth(circleStrokeWidth.toFloat())

        mThumbColorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mThumbColorPaint.setColor(mProgressColor)
        mThumbColorPaint.setStyle(Paint.Style.FILL_AND_STROKE)
        mThumbColorPaint.setStrokeWidth(circleStrokeWidth.toFloat())

        invalidate()
    }

    companion object {

        /**
         * TAG constant for logging
         */
        private val TAG = HoloCircularProgressBar::class.java.simpleName

        /**
         * used to save the super state on configuration change
         */
        private val INSTANCE_STATE_SAVEDSTATE = "saved_state"

        /**
         * used to save the progress on configuration changes
         */
        private val INSTANCE_STATE_PROGRESS = "progress"

        /**
         * used to save the marker progress on configuration changes
         */
        private val INSTANCE_STATE_MARKER_PROGRESS = "marker_progress"

        /**
         * used to save the background color of the progress
         */
        private val INSTANCE_STATE_PROGRESS_BACKGROUND_COLOR = "progress_background_color"

        /**
         * used to save the color of the progress
         */
        private val INSTANCE_STATE_PROGRESS_COLOR = "progress_color"

        /**
         * used to save and restore the visibility of the thumb in this instance
         */
        private val INSTANCE_STATE_THUMB_VISIBLE = "thumb_visible"

        /**
         * used to save and restore the visibility of the marker in this instance
         */
        private val INSTANCE_STATE_MARKER_VISIBLE = "marker_visible"
    }

}
/**
 * Instantiates a new holo circular progress bar.
 *
 * @param context the context
 */
/**
 * Instantiates a new holo circular progress bar.
 *
 * @param context the context
 * @param attrs   the attrs
 */
