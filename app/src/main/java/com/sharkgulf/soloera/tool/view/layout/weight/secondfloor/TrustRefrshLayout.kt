package com.sharkgulf.soloera.tool.view.layout.weight.secondfloor

import android.animation.ValueAnimator.*
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Scroller
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.trust.demo.basis.trust.utils.TrustLogUtils
import java.text.DecimalFormat


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
/*
 *Created by Trust on 2019/1/8
 */
class TrustRefrshLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mContext:Context = context
    private val mAttrs = attrs
    private var mScroller: Scroller? = null

    private var mVelocityTracker: VelocityTracker? = null

    private val mViewTypeProcessing = ViewTypeProcessing()

    private var mSecondFloorView: View?  = null

    private var mHeadView: View? = null

    private var mHeadViewWidth =  0
    private var mHeadViewHight =  0

    private var mSecondFloorViewWidth = 0
    private var mSecondFloorViewHeight = 0

    //    分别记录上次滑动的坐标(onInterceptTouchEnvent)
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    //状态
    //默认状态
    private val DEFAULT = 0
    //车库显示不全
    private val PULL_DOWN_REFRESH = 1
    //车库显示全
    private val RELEASE_REFRESH = 2
    //车库展示
    private val REFRESHING = 3
    //加载更多
    private val LOAD_MORE = 4
    //二楼显示不全
    private val SECOND_FLOOR_PULL_DOWN_REFRESH = 5
    //二楼显示全
    private val SECOND_FLOOR_RELEASE_REFRESH = 6
    //二楼展示
    private val SECOND_FLOOR_REFRESHING = 7

    //布局状态更新回调
    private var mTrustRefrshViewStatusCallBack: TrustRefrshViewStatusCallBack? = null


    //状态标记
    private var STATE = DEFAULT

    private var mToolbar:View? = null


    private var mToolbarHeight:Int = 0

    //记录上一次滑动的坐标
    private var mLastY:Int = 0

    private var secondFloorNum = 0.7F

    //headview + secondFloor 状态
    private var mTrustRefrshListener: TrustRefrshListener? = null

    private var isClose:Boolean = false

    fun setToolbar(v : View): TrustRefrshLayout {
        mToolbar = v
        return this
    }

    fun setSecondFloorNum(num:Float):TrustRefrshLayout{
        secondFloorNum = num
        return this
    }

    fun setHeadView(headViewLayoutId:Int): TrustRefrshLayout {
        mHeadView =  LayoutInflater.from(mContext).inflate(headViewLayoutId,this,false)
        return this
    }

    fun setSecondFloorView(secondFloorViewLayoutId:Int): TrustRefrshLayout {
        mSecondFloorView = LayoutInflater.from(mContext).inflate(secondFloorViewLayoutId,this,false)
        return this
    }

    fun setTrustRefrshListener(trustRefrshListener: TrustRefrshListener): TrustRefrshLayout {
        mTrustRefrshListener = trustRefrshListener
        return this
    }


    private var isChildOnclick:Boolean = true

    fun iniView(){
        initView()
    }


    fun setChildOnClick(bool:Boolean):TrustRefrshLayout{
        isChildOnclick = bool
        return this
    }


 @SuppressLint("CustomViewStyleable", "Recycle")
 private fun initView(){

     orientation = LinearLayout.VERTICAL
     mScroller  = Scroller(mContext)
     mVelocityTracker =VelocityTracker.obtain()

     //添加车库
     if (mSecondFloorView != null) {

         //不隐藏的话  一开始会显示 这些view
         mSecondFloorView?.visibility = View.INVISIBLE
         mHeadView?.visibility = View.INVISIBLE
         addView(mSecondFloorView,0)
         if (mHeadView != null) {
             addView(mHeadView,1)
         }
     }else{
         //不隐藏的话  一开始会显示 这些view
         mSecondFloorView?.visibility = View.INVISIBLE
         mHeadView!!.visibility = View.INVISIBLE
         addView(mHeadView,0)
     }

     // 以下代码主要是为了设置头布局的marginTop值为-headerviewHeight
     // 注意必须等到一小会才会得到正确的头布局宽高，滑动时差

     postDelayed({
         if (mToolbar != null) {
             mToolbarHeight  = mToolbar!!.height
         }
         if (mHeadView != null) {
             mHeadViewWidth = mHeadView!!.width
             mHeadViewHight = mHeadView!!.height
         }
         val lp = LinearLayout.LayoutParams(mHeadViewWidth, mHeadViewHight)
         lp.setMargins(0, -mHeadViewHight + mToolbarHeight, 0, 0)
         mHeadView?.layoutParams = lp

         if (mSecondFloorView != null) {
             mSecondFloorViewWidth = mSecondFloorView!!.width
             mSecondFloorViewHeight = mSecondFloorView!!.height

             val lp1 = LinearLayout.LayoutParams(mSecondFloorViewWidth, mSecondFloorViewHeight)
             lp1.setMargins(0, -(mSecondFloorViewHeight), 0, 0)
             mSecondFloorView!!.layoutParams = lp1
             mSecondFloorView!!.visibility = View.VISIBLE
         }
         mHeadView?.visibility = View.VISIBLE
     },5)

     if (mTrustRefrshViewStatusCallBack == null) {
         mTrustRefrshViewStatusCallBack = TrustRefrshViewStatusCallBack(this)
     }
 }

    private var upDirection = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mVelocityTracker!!.addMovement(event)
        val y = event!!.y.toInt()

        when(event.action){
            MotionEvent.ACTION_DOWN->{
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }}

            MotionEvent.ACTION_MOVE->{
                val deltay = y - mLastY
                if(deltay >0){
                    upDirection = false
                    TrustLogUtils.d("滑动方向 下")
                }else{
                    upDirection = true
                    TrustLogUtils.d("滑动方向 上")
                }

                if (scrollY > 0) {

                }
                else if(scrollY <= -mSecondFloorViewHeight * 1){ }
                else if (scrollY <= 0 && scrollY > -mHeadViewHight * 1) {
                    scrollBy(0, -deltay / 2)
                }
                else if (scrollY <= -mHeadViewHight * 1 ){
                    scrollBy(0, -deltay / 2)
                }else if(scrollY <=0){
                    closeRefish()
                }else if ( mViewTypeProcessing.typeRecyclerView() == -1){
                    scrollBy(0, -deltay / 2)
                }else{
                    scrollBy(0, -deltay / 2)
                }

                log("滑动 距离 $scrollY  STATE $STATE  ")
                val fl = -((mSecondFloorViewHeight + mHeadViewHight) * secondFloorNum)
                //车库显示不全时，
                 if(scrollY > -mHeadViewHight && STATE != REFRESHING &&  STATE != SECOND_FLOOR_REFRESHING){
                    STATE  = PULL_DOWN_REFRESH
                     mTrustRefrshViewStatusCallBack?.onPullDownHeadViewRefreshState(scrollY,mHeadViewHight,deltay)
                 }

                //车库显示完全时，为释放刷新状态(表示可以刷新了)
                else if(scrollY < -mHeadViewHight && STATE != REFRESHING && STATE != RELEASE_REFRESH  && STATE != SECOND_FLOOR_REFRESHING ){
                    STATE = RELEASE_REFRESH
                     mTrustRefrshViewStatusCallBack?.onReleaseHeadViewRefreshState(scrollY,deltay)
                }



                //二楼显示不全时
                else if(scrollY > fl && STATE != SECOND_FLOOR_REFRESHING && mHeadView !=null){
                    STATE = SECOND_FLOOR_PULL_DOWN_REFRESH
                     mTrustRefrshViewStatusCallBack?.onPullDownmSecondFloorViewRefreshState(scrollY,mSecondFloorViewHeight,deltay)
                }

                //二楼显示完全
                else if(scrollY <= fl && STATE != SECOND_FLOOR_REFRESHING && mHeadView !=null){
                    STATE = SECOND_FLOOR_REFRESHING
                     mTrustRefrshViewStatusCallBack?.onReleaseSecondFloorRefreshState(scrollY,deltay)
                }
                else {
                     //二楼显示不全时 没有一楼
                      if(scrollY > fl && mHeadView == null){
                          STATE = SECOND_FLOOR_PULL_DOWN_REFRESH
                          mTrustRefrshViewStatusCallBack?.onPullDownmSecondFloorViewRefreshState(scrollY,mSecondFloorViewHeight,deltay)
                     }else if(scrollY <= fl && mHeadView == null){
                          STATE = SECOND_FLOOR_REFRESHING
                          mTrustRefrshViewStatusCallBack?.onReleaseSecondFloorRefreshState(scrollY,deltay)
                      }
                 }

            }

            MotionEvent.ACTION_UP->{
                log("--------------ACTION_UP")
                val scrollY = scrollY

                when (STATE) {
                    PULL_DOWN_REFRESH -> {
                        STATE = DEFAULT
                        log("车库显示不全")
                        closeRefish()
                        mTrustRefrshListener?.onDefaultState()
                    }
                    RELEASE_REFRESH -> {
                        STATE = REFRESHING
                        smoothScroollBy(0, -mHeadViewHight - scrollY)
                        mTrustRefrshViewStatusCallBack?.onRefreshingHeadViewState()
                        mTrustRefrshListener?.onRefreshingHeadViewState()
                    }
                    REFRESHING -> if (getScrollY() < -mHeadViewHight) {
                        smoothScroollBy(0, -mHeadViewHight - scrollY)
                    }
                    SECOND_FLOOR_PULL_DOWN_REFRESH ->{
//                        STATE = REFRESHING
                        smoothScroollBy(0, -(scrollY+mHeadViewHight))
                    }
                    SECOND_FLOOR_RELEASE_REFRESH ->{
                        STATE = SECOND_FLOOR_REFRESHING
                        smoothScroollBy(0,  - scrollY - mSecondFloorViewHeight -mHeadViewHight)
                    }
                    SECOND_FLOOR_REFRESHING->{
                        TrustAnimation
                                .getTrustAnimation()
                                .addAlphaAnimation(1.0f,0.0f,100)
                                .startAnimation(mSecondFloorView!!.findViewById(R.id.second_floor_tx))
                        if (mHeadView != null) {
                            TrustAnimation
                                    .getTrustAnimation()
                                    .addAlphaAnimation(1.0f,0.0f,500)
                                    .startAnimation(mHeadView!!)
                        }

                        isClose = true
                        smoothScroollBy(0,  - scrollY - mSecondFloorViewHeight)

                        mSecondFloorView?.postDelayed({ mTrustRefrshListener?.onRefreshingSecondFloorViewState()},500)
                        mTrustRefrshViewStatusCallBack?.onRefreshingSecondFloorViewState()
                    }
                    else -> {
                        smoothScroollBy(0, -scrollY)
                    }
                }
                mVelocityTracker!!.clear()
            }
        }
        mLastY = y
        return true
    }

    private var mY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false



        val x = ev!!.x.toInt()
        val y = ev.y.toInt()


    TrustLogUtils.d("onInterceptTouchEvent 正在实行")


        when(ev.action){
            MotionEvent.ACTION_DOWN->{

                intercepted = false
                if (!mScroller!!.isFinished) {
                    mScroller!!.abortAnimation()
                }

                if(STATE == DEFAULT){
                    false
                }else{
                    isChildOnclick
                }

            }


            MotionEvent.ACTION_MOVE->{
                val deltax = x - mLastXIntercept
                val deltay = y - mLastYIntercept
                val firstCompletelyVisibleItemPosition = mViewTypeProcessing.typeRecyclerView()

                intercepted = if (firstCompletelyVisibleItemPosition == 0 && deltay > 0 && Math.abs(deltay) > Math.abs(deltax)) {
                    true
                }
                else if (scrollY < 0 && Math.abs(deltay) > Math.abs(deltax)) {
                    true
                }
                else if(firstCompletelyVisibleItemPosition == -1){
                    if(upDirection == false && STATE == DEFAULT){
                        false
                    }else{
                        isChildOnclick
                    }
                }
                else {
                    false
                }

            }
            MotionEvent.ACTION_UP->{
                intercepted = false}
        }

        mLastY = y
        mLastXIntercept = x
        mLastYIntercept = y

        return intercepted
    }


    private fun smoothScroollBy(dx: Int, dy: Int) {
        mScroller?.startScroll(dx, scrollY, 0, dy, 500)
        invalidate()
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        setToolBarHide(t)
        super.onScrollChanged(l, t, oldl, oldt)
    }

    /**
     * 设置 toolbar  根据设置的滑动距离 隐藏和显示
     */
    private fun setToolBarHide(t: Int) {
        if (scrollY <= t) {
            val s = 150
            val conversionType = conversionType(-((scrollY.toFloat() + mHeadViewHight) / s))
            val toFloat = conversionType.toFloat()
            mToolbar?.alpha = 1 - toFloat
        }
    }

    /**
     * 复位
     */
    fun closeRefish(){
        smoothScroollBy(0, 0 - scrollY)
        mLastY = 0
        mLastXIntercept = 0
        mLastYIntercept = 0
        STATE = DEFAULT
        mTrustRefrshViewStatusCallBack?.onDefaultState()

        if (isClose) {
            val lp = LinearLayout.LayoutParams(mHeadViewWidth, mHeadViewHight)
            lp.setMargins(0,-mHeadViewHight + mToolbarHeight!!, 0, 0)
            mHeadView?.layoutParams = lp
            isClose =false
        }
        if (mSecondFloorView != null) {
            TrustAnimation
                    .getTrustAnimation()
                    .addAlphaAnimation(0.0f,1.0f,100)
                    .startAnimation(mSecondFloorView!!.findViewById(R.id.second_floor_tx))
        }

        if (mHeadView != null) {
            TrustAnimation
                    .getTrustAnimation()
                    .addAlphaAnimation(0.0f,1.0f,100)
                    .startAnimation(mHeadView!!)
        }
    }


    override fun computeScroll() {
        if (mScroller != null) {
            if (mScroller!!.computeScrollOffset()) {
                scrollTo(mScroller!!.currX, mScroller!!.currY)
                postInvalidate()
            }
        }
    }


    private fun log(msg:String){
        Log.d("lhh",msg)
    }


    fun getHeadView():View?{
        return mHeadView
    }

    fun getSecondFloorView():View?{
        return mSecondFloorView
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val childCount = childCount
        for (i in 0..(childCount-1)){
                mViewTypeProcessing.checkView(getChildAt(i))
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }


    private fun conversionType(num: Float): String {
        val decimalFormat = DecimalFormat("0.00")//构造方法的字符格式这里如果小数不足1位,会以0补足.
        return decimalFormat.format(num.toDouble())
    }





    /**
     * 由于是线性布局 所以要把headview magin 恢复
     */
    private fun headViewRecoveryanimation(){
        // 步骤1：设置动画属性的初始值 & 结束值
        val anim = ofInt(-mHeadViewHight + mToolbarHeight!!, mHeadViewHight)
        // ofInt（）作用有两个
        // 1. 创建动画实例
        // 2. 将传入的多个Int参数进行平滑过渡:此处传入0和1,表示将值从0平滑过渡到1
        // 如果传入了3个Int参数 a,b,c ,则是先从a平滑过渡到b,再从b平滑过渡到C，以此类推
        // ValueAnimator.ofInt()内置了整型估值器,直接采用默认的.不需要设置，即默认设置了如何从初始值 过渡到 结束值
        // 关于自定义插值器我将在下节进行讲解
        // 下面看看ofInt()的源码分析 ->>关注1

        // 步骤2：设置动画的播放各种属性
        anim.duration = 10
        // 设置动画运行的时长

        anim.repeatCount = 0
        // 设置动画重复播放次数 = 重放次数+1
        // 动画播放次数 = infinite时,动画无限重复

        anim.repeatMode = RESTART
        // 设置重复播放动画模式
        // ValueAnimator.RESTART(默认):正序重放
        // ValueAnimator.REVERSE:倒序回放

        // 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
        // 设置 值的更新监听器
        // 即：值每次改变、变化一次,该方法就会被调用一次
        anim.addUpdateListener { animation ->
            val currentValue = animation!!.animatedValue as Int
            // 获得改变后的值

            System.out.println(currentValue)
            // 输出改变后的值

            // 步骤4：将改变后的值赋给对象的属性值，下面会详细说明

            val lp = LinearLayout.LayoutParams(mHeadViewWidth, mHeadViewHight)
            lp.setMargins(0, currentValue, 0, 0)
            mHeadView!!.layoutParams = lp
            // 步骤5：刷新视图，即重新绘制，从而实现动画效果
            mHeadView!!.requestLayout()
        }
        anim.start()
        // 启动动画

    }

}