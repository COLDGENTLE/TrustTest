package com.sharkgulf.soloera.home.user


import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.github.cropbitmap.CropViewUtils
import java.io.FileDescriptor
import java.io.InputStream
import android.graphics.Bitmap
import com.sharkgulf.soloera.R









class TestQQView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var centerX: Float = 0.toFloat()
    private var centerY: Float = 0.toFloat()

    init {
        initGesture()
        initAttr(attrs)
    }

    //view显示的图片
    private var showBitmap: Bitmap? = null
    private var showBitmapRectF: RectF? = null
    //控制图片绘制的矩阵
    private var showBitmapMatrix: Matrix? = null
    private var showBitmapPaint: Paint? = null
    //图片可放大的最大倍数
    private var maxScale = 3f
    //双击图片放大倍数
    var doubleClickScale:Float = 1.8f
    protected var doubleClickX: Float = 0.toFloat()
    protected var doubleClickY: Float = 0.toFloat()


    //随着圆形区域的变小而变小,用来限制圆形的缩小倍数
    private val minCircleScale = 1f
    //初始化图片缩放和平移，保证图片在view中心显示
    private var initScale = 1f
    private var initTranslateX: Float = 0.toFloat()
    private var initTranslateY: Float = 0.toFloat()


    //圆形所在矩阵
    private var circleRectF: RectF? = null
    //只是用来记录初始位置
    private var initCircleRectF: RectF? = null
    //控制圆形所在矩阵
    private var circleRectFMatrix: Matrix? = null

    //包裹圆形可触摸矩阵
    private var bigCircleRectF: RectF? = null

    //通过path在view中显示出圆形
    private var circlePath: Path? = null
    //给圆形path内部绘制一个边框(like qq)
    private var circleBorderPath: Path? = null
    private var circleBorderPaint: Paint? = null

    //圆形之外所有区域
    private var outsidePath: Path? = null

    private var paint: Paint? = null
    private var bgPaint: Paint? = null


    private var touchRegion: Region? = null//(暂时没用)
    //1:左上角，2右上角，3右下角，4左下角(暂时没用)
    private val touchArea: Int = 0
    //圆形外部可触摸宽度(暂时没用)
    private val touchLength = 10
    //用于放大圆形(暂时没用)
    private var bigCirclePath: Path? = null

    //是否可以放大圆形(暂时没用)
    private var canZoomCircle: Boolean = false

    //是否可以移动图片(点击图片内部区域才能移动位置)
    private var canMoveBitmap: Boolean = false

    private var gestureDetector: GestureDetector? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var valueAnimator: ValueAnimator? = null


    private var radius = -1f
    private var bgColor = Color.WHITE
    private var maskColor: Int = 0
    private var borderColor: Int = 0


    fun getRadius(): Float {
        return radius
    }

    fun getClipWidth(): Float {
        return getRectLength(circleRectF!!)
    }

    fun setRadius(radius: Float): TestQQView {
        this.radius = radius
        post(object : Runnable {
            override fun run() {
                refreshPath()
                invalidate()
            }
        })

        return this
    }

    fun getMaskColor(): Int {
        return maskColor
    }

    fun setMaskColor(@ColorInt maskColor: Int): TestQQView {
        this.maskColor = maskColor
        if (sizeChanged) {
            refreshPaint()
            invalidate()
        }
        return this
    }

    fun getBgColor(): Int {
        return bgColor
    }

    fun setBgColor(bgColor: Int): TestQQView {
        this.bgColor = bgColor
        return this
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(@ColorInt borderColor: Int): TestQQView {
        this.borderColor = borderColor
        if (sizeChanged) {
            refreshPaint()
            invalidate()
        }

        return this
    }

    fun getMaxScale(): Float {
        return maxScale
    }

    fun setMaxScale(maxScale: Float): TestQQView {
        var maxScale = maxScale
        if (maxScale < 1) {
            maxScale = 1f
        }
        if (doubleClickScale > maxScale) {
            doubleClickScale = maxScale
        }
        this.maxScale = maxScale
        return this
    }

    fun getDoubleClickScale(): Double {
        return doubleClickScale.toDouble()
    }

    fun setDoubleClickScale(doubleClickScale: Float): TestQQView {
        var doubleClickScale = doubleClickScale
        if (doubleClickScale < 1) {
            doubleClickScale = 1f
        }
        if (doubleClickScale > maxScale) {
            doubleClickScale = maxScale
        }
        this.doubleClickScale = doubleClickScale
        return this
    }

    private var sizeChanged: Boolean = false


    private fun initAttr(attrs: AttributeSet?) {
        maskColor = Color.parseColor("#60000000")
        borderColor = ContextCompat.getColor(getContext(), android.R.color.white)
        if (attrs == null) {
            return
        }

        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LikeQQCropView)
        maskColor = typedArray.getColor(R.styleable.LikeQQCropView_maskColor, Color.parseColor("#60000000"))
        bgColor = Color.BLUE
        borderColor = typedArray.getColor(R.styleable.LikeQQCropView_borderColor, ContextCompat.getColor(getContext(), android.R.color.white))
        radius = typedArray.getDimension(R.styleable.LikeQQCropView_radius, -1f)

        maxScale = typedArray.getFloat(R.styleable.LikeQQCropView_maxScale, 3f)
        doubleClickScale = typedArray.getFloat(R.styleable.LikeQQCropView_doubleClickScale, 1.8f)

        if (maxScale < 1) {
            maxScale = 1f
        }
        if (doubleClickScale < 1) {
            doubleClickScale = 1f
        }
        if (doubleClickScale > maxScale) {
            doubleClickScale = maxScale
        }
        typedArray.recycle()
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val width = getScreenWidth() / 2
        val height = getScreenWidth() / 2

        if (ViewGroup.LayoutParams.WRAP_CONTENT == getLayoutParams().width && ViewGroup.LayoutParams.WRAP_CONTENT == getLayoutParams().height) {
            setMeasuredDimension(width, height)
        } else if (ViewGroup.LayoutParams.WRAP_CONTENT == getLayoutParams().width) {
            setMeasuredDimension(width, heightSize)
        } else if (ViewGroup.LayoutParams.WRAP_CONTENT == getLayoutParams().height) {
            setMeasuredDimension(widthSize, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

    }

    private fun getScreenWidth(): Int {
        return getContext().getResources().getDisplayMetrics().widthPixels
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        touchRegion = Region()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        //        paint.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        paint!!.setStyle(Paint.Style.STROKE)
        paint!!.setStrokeWidth(2f)

        circleBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circleBorderPaint!!.setColor(borderColor)
        circleBorderPaint!!.setStyle(Paint.Style.STROKE)
        circleBorderPaint!!.setStrokeWidth(dip2px(getContext(), 1f).toFloat())

        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        bgPaint!!.setColor(maskColor)
        //        bgPaint.setStyle(Paint.Style.FILL);

        showBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //        showBitmapPaint.setColor(ContextCompat.getColor(getContext(),R.color.black));
        showBitmapPaint!!.setStyle(Paint.Style.STROKE)
        showBitmapPaint!!.setStrokeWidth(2f)
        init()
        sizeChanged = true
    }


    fun getNewBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高.
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例.
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }


    private fun init() {

        centerX = (getWidth() / 2).toFloat()
        centerY = (getHeight() / 2).toFloat()
        circleBorderPath = Path()
        circlePath = Path()
        outsidePath = Path()
        bigCirclePath = Path()
        if (showBitmap == null) {
            return
        }


        var width = showBitmap!!.width
        var height = showBitmap!!.height


//        if(width > height){
//            newGCD =  getOptimumGCD(ratioW);
//        }else {
//            newGCD = getOptimumGCD(ratioH);
//        }

//        showBitmap = getNewBitmap(showBitmap!!, getWidth()/3 *ratioW.toInt(),getHeight()/3 *ratioH.toInt())


        if (showBitmap!!.getHeight() < getHeight() && showBitmap!!.getWidth() < getWidth()) {
//            //如果图片宽高均小于屏幕宽高，只需要计算图片位移到中心的距离
//            initScale = 1f
//            initTranslateX = ((getWidth() - showBitmap!!.getWidth()) / 2).toFloat()
//            initTranslateY = ((getHeight() - showBitmap!!.getHeight()) / 2).toFloat()

            //如果图片宽(高)大于屏幕宽(高)，需要计算图片缩小倍数和位移到中心的距离
            if (showBitmap!!.getWidth() * 1.0f / showBitmap!!.getHeight() > getWidth() * 1.0f / getHeight()) {

                if(showBitmap!!.getWidth() < showBitmap!!.getHeight()){
                    initScale = 1f
                    initTranslateX = ((getWidth() - showBitmap!!.getWidth()) / 2).toFloat()
                    initTranslateY = ((getHeight() - showBitmap!!.getHeight()) / 2).toFloat()
                }


            } else {
                initScale = getHeight() * 1.0f / showBitmap!!.getHeight()
                initTranslateX = (getWidth() - showBitmap!!.getWidth() * initScale) / 2
                initTranslateY = 0f
            }

        } else {
            //如果图片宽(高)大于屏幕宽(高)，需要计算图片缩小倍数和位移到中心的距离
            if (showBitmap!!.getWidth() * 1.0f / showBitmap!!.getHeight() > getWidth() * 1.0f / getHeight()) {
                initScale = getWidth() * 1.0f / showBitmap!!.getWidth()
                initTranslateX = 0f
                initTranslateY = (getHeight() - showBitmap!!.getHeight() * initScale) / 2
            } else {
                initScale = getHeight() * 1.0f / showBitmap!!.getHeight()
                initTranslateX = (getWidth() - showBitmap!!.getWidth() * initScale) / 2
                initTranslateY = 0f
            }
        }


        circleRectFMatrix = Matrix()

        //图片未缩放的矩阵
        showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat() )
        showBitmapMatrix = Matrix()
        showBitmapMatrix!!.postScale(initScale, initScale)
        showBitmapMatrix!!.postTranslate(initTranslateX, initTranslateY)

        //图片缩放之后的矩阵
        showBitmapMatrix!!.mapRect(showBitmapRectF)

        //根据图片矩阵获取圆形矩阵
        circleRectF = getCircleRectFByBitmapRectF(RectF(0f, 0f, getWidth().toFloat(), getHeight().toFloat() ))
        //        circlePath.addCircle(centerX, centerY,circleRectFLength/2, Path.Direction.CW);
        //记录初始化的圆形矩阵
        initCircleRectF = circleRectF

        refreshPath()

    }


    private fun refreshPaint() {
        circleBorderPaint!!.setColor(borderColor)
        bgPaint!!.setColor(maskColor)
    }

    private fun refreshPath() {
        if (!outsidePath!!.isEmpty()) {
            outsidePath!!.reset()
        }
        //圆形之外所有区域
        outsidePath!!.addRect(RectF(0f, 0f, getWidth().toFloat(), getHeight().toFloat()), Path.Direction.CW)

        if (!circlePath!!.isEmpty()) {
            circlePath!!.reset()
        }
        if (radius > getRectLength(circleRectF!!) / 2 || radius < 0) {
            radius = getRectLength(circleRectF!!) / 2
        }
        //圆形之内所有区域
        circlePath!!.addRoundRect(circleRectF, radius, radius, Path.Direction.CW)

        if (!circleBorderPath!!.isEmpty()) {
            circleBorderPath!!.reset()
        }
        val circleBorderRectF = RectF(circleRectF!!.left + getPathInterval(), circleRectF!!.top + getPathInterval(), circleRectF!!.right - getPathInterval(), circleRectF!!.bottom - getPathInterval())
        circleBorderPath!!.addRoundRect(circleBorderRectF, radius * getRectLength(circleBorderRectF) / getRectLength(circleRectF!!), radius * getRectLength(circleBorderRectF) / getRectLength(circleRectF!!), Path.Direction.CW)

        //获取圆形之外所有区域
        outsidePath!!.op(circlePath, Path.Op.XOR)

        this.bigCircleRectF = getBigCircleRectF(circleRectF)

        if (!bigCirclePath!!.isEmpty()) {
            bigCirclePath!!.reset()
        }
        bigCirclePath!!.addRoundRect(this.bigCircleRectF, (this.bigCircleRectF!!.right - this.bigCircleRectF!!.left) / 2, (this.bigCircleRectF!!.right - this.bigCircleRectF!!.left) / 2, Path.Direction.CW)

        bigCirclePath!!.op(circlePath, Path.Op.XOR)

        //获取可以触摸放大的区域
        //        touchRegion.setPath(bigCirclePath,new Region(0,0,getWidth(),getHeight()));
    }

    /*裁剪*/
    fun clip(): Bitmap? {
        if (sizeChanged == false) {
            return null
        }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val matrix = Matrix()
        showBitmapMatrix!!.invert(matrix)

        val rectF = RectF()
        rectF.set(circleRectF)
        matrix.mapRect(rectF)

        var needCropBitmap: Bitmap? = Bitmap.createBitmap(showBitmap!!, rectF.left.toInt(), rectF.top.toInt(), (rectF.right - rectF.left).toInt(), (rectF.bottom - rectF.top).toInt())

        var newBitmap = Bitmap.createBitmap(getRectLength(circleRectF!!).toInt(), getRectLength(circleRectF!!).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //        if(bgColor!=-1){
        //        }
        canvas.drawColor(bgColor)

        val saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)

        val path = Path()
        path.addRoundRect(RectF(0f, 0f, getRectLength(circleRectF!!), getRectLength(circleRectF!!)), radius, radius, Path.Direction.CW)

        path.moveTo(0f, 0f)
        path.moveTo(getRectLength(circleRectF!!), getRectLength(circleRectF!!))

        canvas.drawPath(path, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))


        canvas.drawBitmap(needCropBitmap!!,
                Rect(0, 0, needCropBitmap!!.getWidth(), needCropBitmap!!.getHeight()),
                RectF(0f, 0f, getRectLength(circleRectF!!), getRectLength(circleRectF!!)), paint)
        //        canvas.drawBitmap(needCropBitmap,newMatrix,paint);
        paint.setXfermode(null)

        canvas.restoreToCount(saveCount)

        val temp = FloatArray(9)
        showBitmapMatrix!!.getValues(temp)

        if (temp[Matrix.MSCALE_X] < 0 && temp[Matrix.MSCALE_Y] < 0) {
            //如果裁剪时有翻转图片，则对图片做处理
            newBitmap = getFlipBitmap(newBitmap, -1, -1)
        } else if (temp[Matrix.MSCALE_X] < 0) {
            newBitmap = getFlipBitmap(newBitmap, -1, 1)
        } else if (temp[Matrix.MSCALE_Y] < 0) {
            newBitmap = getFlipBitmap(newBitmap, 1, -1)
        }
        needCropBitmap!!.recycle()
        needCropBitmap = null
        return newBitmap
    }

    private fun getFlipBitmap(newBitmap: Bitmap, MSCALE_X: Int, MSCALE_Y: Int): Bitmap {
        var newBitmap = newBitmap
        val flipMatrix = Matrix()
        flipMatrix.postScale(MSCALE_X.toFloat(), MSCALE_Y.toFloat(), (newBitmap.getWidth() / 2).toFloat(), (newBitmap.getWidth() / 2).toFloat())
        newBitmap = Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), flipMatrix, true)
        return newBitmap
    }

    /*水平翻转*/
    fun horizontalFlip() {
        post(object : Runnable {
            override fun run() {
                showBitmapMatrix!!.postScale(-1f, 1f, centerX, centerY)
                showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                showBitmapMatrix!!.mapRect(showBitmapRectF)
                invalidate()
            }
        })
    }

    /**垂直翻转 */
    fun verticalFlip() {
        post(object : Runnable {
            override fun run() {
                showBitmapMatrix!!.postScale(1f, -1f, centerX, centerY)
                showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                showBitmapMatrix!!.mapRect(showBitmapRectF)
                invalidate()
            }
        })

    }

    /**垂直+水平翻转 */
    fun verticalAndHorizontalFlip() {
        post(object : Runnable {
            override fun run() {
                showBitmapMatrix!!.postScale(-1f, -1f, centerX, centerY)
                showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                showBitmapMatrix!!.mapRect(showBitmapRectF)

                invalidate()
            }
        })
    }

    /*还原*/
    fun reset() {
        if (sizeChanged) {
            init()
            invalidate()
        } else {
            post(object : Runnable {
                override fun run() {
                    init()
                    invalidate()
                }
            })
        }
    }

    private fun getPathInterval(): Float {
        return dip2px(getContext(), 0.5f).toFloat()
    }

    //根据图片缩放之后的矩阵获取圆形裁剪框的矩阵
    private fun getCircleRectFByBitmapRectF(showBitmapRectF: RectF): RectF {
        val rectFW = showBitmapRectF.right - showBitmapRectF.left
        val rectFH = showBitmapRectF.bottom - showBitmapRectF.top
        //圆形所在矩阵边长
        val circleRectFLength = if (rectFW > rectFH) rectFH else rectFW
        //计算出圆形所在矩阵的left top
        val circleRectFLeft = centerX - circleRectFLength / 2
        val circleRectFTop = centerY - circleRectFLength / 2
        //圆形矩阵
        return RectF(circleRectFLeft, circleRectFTop, circleRectFLength + circleRectFLeft, circleRectFLength + circleRectFTop)
    }

    private fun getRectLength(rectF: RectF): Float {
        return Math.abs(rectF.right - rectF.left)
    }

    private fun getBigCircleRectF(circleRectF: RectF?): RectF {
        val rectF = RectF()
        rectF.set(circleRectF)
        rectF.left = rectF.left - getTouchAreaWidth()
        rectF.top = rectF.top - getTouchAreaWidth()
        rectF.right = rectF.right + getTouchAreaWidth()
        rectF.bottom = rectF.bottom + getTouchAreaWidth()
        return rectF
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (showBitmap == null) {
            return
        }
        canvas.drawBitmap(showBitmap!!, showBitmapMatrix!!, null)


        //圆形所在矩阵
        //        canvas.drawRect(circleRectF,paint);
        //        canvas.drawRect(bigCircleRectF,showBitmapPaint);
        //包含图片矩形
        //        canvas.drawRect(showBitmapRectF,showBitmapPaint);//
        canvas.drawPath(outsidePath!!, bgPaint!!)

        canvas.drawPath(circleBorderPath!!, circleBorderPaint!!)

        //bigpath
        //        canvas.drawPath(bigCirclePath,bgPaint);

    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.getResources().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }

    private fun getTouchAreaWidth(): Int {
        return dip2px(getContext(), 10f)
    }

    private fun getCurrentScale(): Float {
        val temp = FloatArray(9)
        showBitmapMatrix!!.getValues(temp)
        //加了水平翻转功能进去，所以这里取绝对值
        return Math.abs(temp[Matrix.MSCALE_X])
    }

    private fun initGesture() {
        gestureDetector = GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                var distanceX = distanceX
                var distanceY = distanceY
                //通过移动来缩放圆形裁剪框
                if (canZoomCircle) {
                    //&&getCurrentScale()<maxScale&&getCurrentScale()>minCircleScale
                    val distance = if (Math.abs(distanceX) > Math.abs(distanceY)) distanceX else distanceY
                    val rectHeight = circleRectF!!.bottom - circleRectF!!.top
                    val scaleFactory = (-distance * 2 + rectHeight) / rectHeight

                    //圆形裁剪框和bitmap同时缩放处理
                    //                    circleRectFMatrix.postScale(scaleFactory,scaleFactory,centerX,centerY);

                    showBitmapMatrix!!.postScale(scaleFactory, scaleFactory, centerX, centerY)

                    showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                    showBitmapMatrix!!.mapRect(showBitmapRectF)


                    circleRectF = RectF()
                    circleRectF!!.set(getCircleRectFByBitmapRectF(showBitmapRectF!!))
                    //                    circleRectFMatrix.mapRect(circleRectF);

                    bigCircleRectF = getBigCircleRectF(circleRectF)

                    refreshPath()


                    invalidate()
                }
                //移动图片
                if (canMoveBitmap && canZoomCircle == false) {
                    //从左往右滑动图片(防止图片滑出裁剪框外)
                    if (distanceX < 0) {
                        val rectDistance = circleRectF!!.left - showBitmapRectF!!.left
                        if (rectDistance < Math.abs(distanceX)) {
                            distanceX = -rectDistance
                        }
                    }
                    //从右往左滑动图片(防止图片滑出裁剪框外)
                    if (distanceX > 0) {
                        val rectDistance = showBitmapRectF!!.right - circleRectF!!.right
                        if (rectDistance < Math.abs(distanceX)) {
                            distanceX = rectDistance
                        }
                    }
                    //从上往下滑动图片(防止图片滑出裁剪框外)
                    if (distanceY < 0) {
                        val rectDistance = circleRectF!!.top - showBitmapRectF!!.top
                        if (rectDistance < Math.abs(distanceY)) {
                            distanceY = -rectDistance
                        }
                    }

                    //从下往上滑动图片(防止图片滑出裁剪框外)
                    if (distanceY > 0) {
                        val rectDistance = showBitmapRectF!!.bottom - circleRectF!!.bottom
                        if (rectDistance < Math.abs(distanceY)) {
                            distanceY = rectDistance
                        }
                    }

                    showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                    showBitmapMatrix!!.postTranslate(-distanceX, -distanceY)
                    showBitmapMatrix!!.mapRect(showBitmapRectF)


                    invalidate()
                }
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                when (e.getAction()) {
                    MotionEvent.ACTION_UP -> if (showBitmapRectF!!.contains(e.getX(), e.getY())) {
                        if (getCurrentScale() > initScale) {
                            //用于双击图片放大缩小,获取动画间隔缩放系数
                            val sparseArray = SparseArray<Float>()
                            sparseArray.put(0, -1f)
                            sparseArray.put(1, -1f)
                            valueAnimator = ValueAnimator.ofFloat(getCurrentScale(), initScale)
                            //                                Log(initScale+"==="+initScale);
                            valueAnimator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                                override fun onAnimationUpdate(animation: ValueAnimator) {
                                    val value = animation.getAnimatedValue() as Float
                                    var tempScale = 1f
                                    if (sparseArray.get(0) == -1f && sparseArray.get(1) == -1f) {
                                        sparseArray.put(0, value)
                                    } else if (sparseArray.get(1) == -1f) {
                                        sparseArray.put(1, value)
                                        tempScale = sparseArray.get(1) / sparseArray.get(0)
                                    } else {
                                        sparseArray.put(0, sparseArray.get(1))
                                        sparseArray.put(1, value)
                                        tempScale = sparseArray.get(1) / sparseArray.get(0)
                                    }
                                    zoomBitmap(tempScale, centerX, centerX)
                                    invalidate()
                                }
                            })
                            valueAnimator!!.setInterpolator(DecelerateInterpolator())
                            valueAnimator!!.setDuration(300)
                            valueAnimator!!.start()
                        } else {
                            doubleClickX = e.getX()
                            doubleClickY = e.getY()
                            //用于双击图片放大缩小,获取动画间隔缩放系数
                            val sparseArray = SparseArray<Float>()
                            sparseArray.put(0, -1f)
                            sparseArray.put(1, -1f)
                            valueAnimator = ValueAnimator.ofFloat(getCurrentScale(), doubleClickScale)
                            valueAnimator!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                                override fun onAnimationUpdate(animation: ValueAnimator) {
                                    val value = animation.getAnimatedValue() as Float
                                    var tempScale = 1f
                                    if (sparseArray.get(0) == -1f && sparseArray.get(1) == -1f) {
                                        sparseArray.put(0, value)
                                    } else if (sparseArray.get(1) == -1f) {
                                        sparseArray.put(1, value)
                                        tempScale = sparseArray.get(1) / sparseArray.get(0)
                                    } else {
                                        sparseArray.put(0, sparseArray.get(1))
                                        sparseArray.put(1, value)
                                        tempScale = sparseArray.get(1) / sparseArray.get(0)
                                    }
                                    showBitmapMatrix!!.postScale(tempScale, tempScale, doubleClickX, doubleClickY)
                                    showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
                                    showBitmapMatrix!!.mapRect(showBitmapRectF)
                                    invalidate()

                                }
                            })
                            valueAnimator!!.setInterpolator(DecelerateInterpolator())
                            valueAnimator!!.setDuration(300)
                            valueAnimator!!.start()
                        }
                    }
                }
                return super.onDoubleTapEvent(e)
            }
        })
        scaleGestureDetector = ScaleGestureDetector(getContext(), object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentScale = getCurrentScale()
                var scaleFactor = detector.getScaleFactor()

                //防止过度缩小
                if (currentScale * scaleFactor < initScale) {
                    scaleFactor = initScale / currentScale
                }

                /*showBitmapMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());

                showBitmapRectF = new RectF(0,0,showBitmap.getWidth(),showBitmap.getHeight());
                showBitmapMatrix.mapRect(showBitmapRectF);


                //如果缩小需要检查圆形框是否包含图片，如果不包含，缩小之后需要平移
                if(scaleFactor<1){
                    float leftLength = showBitmapRectF.left - circleRectF.left;
                    if(leftLength>0){
                        showBitmapMatrix.postTranslate(-leftLength,0);
                    }
                    float topLength = showBitmapRectF.top - circleRectF.top;
                    if(topLength>0){
                        showBitmapMatrix.postTranslate(0,-topLength);
                    }
                    float rightLength = circleRectF.right-showBitmapRectF.right ;
                    if(rightLength>0){
                        showBitmapMatrix.postTranslate(rightLength,0);
                    }
                    float bottomLength = circleRectF.bottom-showBitmapRectF.bottom ;
                    if(bottomLength>0){
                        showBitmapMatrix.postTranslate(0,bottomLength);
                    }
                    showBitmapRectF = new RectF(0,0,showBitmap.getWidth(),showBitmap.getHeight());
                    showBitmapMatrix.mapRect(showBitmapRectF);
                }*/
                zoomBitmap(scaleFactor, detector.getFocusX(), detector.getFocusY())
                invalidate()
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                //如果缩放中心在图片范围内就可以缩放
                return if (showBitmapRectF!!.contains(detector.getFocusX(), detector.getFocusY())) {
                    true
                } else {
                    false
                }
            }
        })
    }

    private fun zoomBitmap(scaleFactor: Float, focusX: Float, focusY: Float) {
        var scaleFactor = scaleFactor
        if (scaleFactor > 1 && getCurrentScale() * scaleFactor > maxScale) {
            scaleFactor = maxScale / getCurrentScale()
        }
        showBitmapMatrix!!.postScale(scaleFactor, scaleFactor, focusX, focusY)

        showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
        showBitmapMatrix!!.mapRect(showBitmapRectF)


        //如果缩小需要检查圆形框是否包含图片，如果不包含，缩小之后需要平移
        if (scaleFactor < 1) {//小于1缩小动作，大于1放大动作
            val leftLength = showBitmapRectF!!.left - circleRectF!!.left
            if (leftLength > 0) {
                showBitmapMatrix!!.postTranslate(-leftLength, 0f)
            }
            val topLength = showBitmapRectF!!.top - circleRectF!!.top
            if (topLength > 0) {
                showBitmapMatrix!!.postTranslate(0f, -topLength)
            }
            val rightLength = circleRectF!!.right - showBitmapRectF!!.right
            if (rightLength > 0) {
                showBitmapMatrix!!.postTranslate(rightLength, 0f)
            }
            val bottomLength = circleRectF!!.bottom - showBitmapRectF!!.bottom
            if (bottomLength > 0) {
                showBitmapMatrix!!.postTranslate(0f, bottomLength)
            }
            showBitmapRectF = RectF(0f, 0f, showBitmap!!.getWidth().toFloat(), showBitmap!!.getHeight().toFloat())
            showBitmapMatrix!!.mapRect(showBitmapRectF)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        gestureDetector!!.onTouchEvent(event)
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> if (showBitmapRectF!!.contains(event.getX(), event.getY())) {
                canMoveBitmap = true
            }
            MotionEvent.ACTION_UP -> {
                canMoveBitmap = false
                canZoomCircle = false
            }
        }/* if(touchRegion.contains((int)event.getX(),(int)event.getY())){
                    canZoomCircle=true;
                }else{
                }*/
        return true
    }

    /*    private void Log(String s) {
        Log.i("@@@===","==="+s);
    }*/
    private fun resetBitmap() {

        reset()
    }

    fun getBitmap(): Bitmap? {
        return showBitmap
    }

    @Deprecated("")
    fun setBitmap(bitmap: Bitmap): TestQQView {
        showBitmap = bitmap
        resetBitmap()
        return this
    }

    /** */
    fun setBitmap(resId: Int, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(getContext(), resId, reqWidth, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmap(pathName: String, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(pathName, reqWidth, reqHeight)
        resetBitmap()
        return this
    }

    @Deprecated("")
    fun setBitmapToRotate(pathName: String, reqWidth: Int, reqHeight: Int): TestQQView {
        //内部已做旋转处理
        showBitmap = CropViewUtils.compressBitmap(pathName, reqWidth, reqHeight)
        resetBitmap()
        //        int degree = CropViewUtils.readPictureDegree(pathName);
        //        if(degree>0){
        //            showBitmap=rotateBitmap(degree,showBitmap);
        //        }
        return this
    }

    fun setBitmap(data: ByteArray, offset: Int, length: Int, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(data, offset, length, reqWidth, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmap(fd: FileDescriptor, outPadding: Rect, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(fd, outPadding, reqWidth, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmap(res: Resources, value: TypedValue, `is`: InputStream, pad: Rect, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(res, value, `is`, pad, reqWidth, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmap(`is`: InputStream, outPadding: Rect, reqWidth: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmap(`is`, outPadding, reqWidth, reqHeight)
        resetBitmap()
        return this
    }
    /*******************************************************************************************************/


    /** */
    fun setBitmapForHeight(resId: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(getContext(), resId, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmapForHeight(pathName: String, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(pathName, reqHeight)
        resetBitmap()
        return this
    }

    @Deprecated("")
    fun setBitmapForHeightToRotate(pathName: String, reqHeight: Int): TestQQView {
        //内部已做旋转处理
        showBitmap = CropViewUtils.compressBitmapForHeight(pathName, reqHeight)
        resetBitmap()
        //        int degree = CropViewUtils.readPictureDegree(pathName);
        //        if(degree>0){
        //            showBitmap=rotateBitmap(degree,showBitmap);
        //        }
        return this
    }

    fun setBitmapForHeight(data: ByteArray, offset: Int, length: Int, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(data, offset, length, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmapForHeight(fd: FileDescriptor, outPadding: Rect, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(fd, outPadding, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmapForHeight(res: Resources, value: TypedValue, `is`: InputStream, pad: Rect, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(res, value, `is`, pad, reqHeight)
        resetBitmap()
        return this
    }

    fun setBitmapForHeight(`is`: InputStream, outPadding: Rect, reqHeight: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForHeight(`is`, outPadding, reqHeight)
        resetBitmap()
        return this
    }
    /*******************************************************************************************************/


    /** */
    fun setBitmapForWidth(resId: Int, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(getContext(), resId, reqWidth)
        resetBitmap()
        return this
    }

    fun setBitmapForWidth(pathName: String, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(pathName, reqWidth)
        resetBitmap()
        return this
    }

    @Deprecated("")
    fun setBitmapForWidthToRotate(pathName: String, reqWidth: Int): TestQQView {
        //内部已做旋转处理
        showBitmap = CropViewUtils.compressBitmapForWidth(pathName, reqWidth)
        resetBitmap()
        //        int degree = CropViewUtils.readPictureDegree(pathName);
        //        if(degree>0){
        //            showBitmap=rotateBitmap(degree,showBitmap);
        //        }
        return this
    }

    fun setBitmapForWidth(data: ByteArray, offset: Int, length: Int, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(data, offset, length, reqWidth)
        resetBitmap()
        return this
    }

    fun setBitmapForWidth(fd: FileDescriptor, outPadding: Rect, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(fd, outPadding, reqWidth)
        resetBitmap()
        return this
    }

    fun setBitmapForWidth(res: Resources, value: TypedValue, `is`: InputStream, pad: Rect, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(res, value, `is`, pad, reqWidth)
        resetBitmap()
        return this
    }

    fun setBitmapForWidth(`is`: InputStream, outPadding: Rect, reqWidth: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForWidth(`is`, outPadding, reqWidth)
        resetBitmap()
        return this
    }
    /*******************************************************************************************************/


    /** */
    fun setBitmapForScale(resId: Int, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(getContext(), resId, scaleSize)
        resetBitmap()
        return this
    }

    fun setBitmapForScale(pathName: String, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(pathName, scaleSize)
        resetBitmap()
        return this
    }

    @Deprecated("")
    fun setBitmapForScaleToRotate(pathName: String, scaleSize: Int): TestQQView {
        //内部已做旋转处理
        showBitmap = CropViewUtils.compressBitmapForScale(pathName, scaleSize)
        resetBitmap()
        //        int degree = CropViewUtils.readPictureDegree(pathName);
        //        if(degree>0){
        //            showBitmap=rotateBitmap(degree,showBitmap);
        //        }
        return this
    }

    fun setBitmapForScale(data: ByteArray, offset: Int, length: Int, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(data, offset, length, scaleSize)
        resetBitmap()
        return this
    }

    fun setBitmapForScale(fd: FileDescriptor, outPadding: Rect, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(fd, outPadding, scaleSize)
        resetBitmap()
        return this
    }

    fun setBitmapForScale(res: Resources, value: TypedValue, `is`: InputStream, pad: Rect, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(res, value, `is`, pad, scaleSize)
        resetBitmap()
        return this
    }

    fun setBitmapForScale(`is`: InputStream, outPadding: Rect, scaleSize: Int): TestQQView {
        showBitmap = CropViewUtils.compressBitmapForScale(`is`, outPadding, scaleSize)
        resetBitmap()
        return this
    }
    /*******************************************************************************************************/


    /**************************************************旋转图片*****************************************************/


    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    fun rotateBitmap(angle: Int, bitmap: Bitmap): Bitmap {
        // 旋转图片 动作
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        // 创建新的图片
        /*if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }*/
        return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true)
    }


}