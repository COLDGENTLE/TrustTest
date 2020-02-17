package com.sharkgulf.soloera.tool.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.view.View
import android.view.animation.*

/*
 *Created by Trust on 2018/12/3
 * 属性动画工具类
 * 使用方式
 *
 * 单一动画
 * TrustAnimation
                .getTrustAnimation()
                .addAlphaAnimation(1.0f,0.0f,200)
                .startAnimation(v)
 *
 *
 * 组合动画
 *  TrustAnimation
                .getTrustAnimation()
                .addTranslateXAnimationItem(car_lock,0F,-50F,2000)
                .addTranslateYAnimationItem(car_status,0F,-50F,1500)
                .addAlphAnimationItem(car_lock,1.0f,0.0f,2000)
                .addAlphAnimationItem(car_status,1.0f,0.0f,2000)
                .setPlayType(TrustAnimation.PLAY_TOGETHER)
                .startAnimations()
 *
 *
 */
class TrustAnimation {

    /**
     * ---------------------------------------------------------------------------------------------
     */

    companion object {
        private var animation : TrustAnimation?= null

        //动画播放方式
        //串行
        const val PLAY_SEQUENTIALLY = 0
        //并行
        const val PLAY_TOGETHER = 1

        fun getTrustAnimation(): TrustAnimation {
            if (animation == null) {
                animation = TrustAnimation()
            }
            return animation!!
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------
     */

    private var valueAnimator:Animation?=null
    private val animatorSet = AnimatorSet()  //组合动画
    private val animationList = arrayListOf<Animator>()
    private var animationPlayType:Int = PLAY_TOGETHER
    /**
     * 透明度动画
     */
     fun addAlphaAnimation(start:Float,end:Float,duration:Long= 1500,noReset:Boolean=true,
                               animationListener:Animation.AnimationListener?=null): TrustAnimation {
      valueAnimator = AlphaAnimation(start,end)
      valueAnimator!!.duration = duration
      valueAnimator!!.fillAfter = noReset
      valueAnimator!!.setAnimationListener(animationListener)
        return this
  }

    /**
     * 位移动画
     */
    fun addTranslateAnimation(startX:Float,startY:Float,endX:Float,endY:Float,duration:Long = 1000,
                              noReset:Boolean=true,
                              animationListener:Animation.AnimationListener?=null): TrustAnimation {
        valueAnimator = TranslateAnimation(startX,endX,startY,endY)
        valueAnimator!!.duration = duration
        valueAnimator!!.fillAfter = noReset
        valueAnimator!!.setAnimationListener(animationListener)
        return this
    }

    /**
     * 缩放动画
     */

    fun addScaleAnimation(startX: Float,startY: Float,endX: Float,endY: Float,
                          duration:Long = 1500,noReset:Boolean=true,
                          animationListener:Animation.AnimationListener?=null): TrustAnimation {
        valueAnimator = ScaleAnimation(startX,endX,startY,endY)
        valueAnimator!!.duration = duration
        valueAnimator!!.fillAfter = noReset
        valueAnimator!!.setAnimationListener(animationListener)
        return this
    }

    /**
     * 旋转动画
     */
    fun addRotateAnimation(duration:Long = 1500,noReset:Boolean=true): TrustAnimation {
        valueAnimator = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        valueAnimator!!.duration = duration
        valueAnimator!!.fillAfter = noReset
        valueAnimator!!.repeatCount = -1
        return  this
//        valueAnimator!!.setAnimationListener(animationListener)
    }


    fun cancelAnimation(){
        valueAnimator?.cancel()
    }

    /**
     * 开始动画
     */

   fun startAnimation(v:View?){
      v?.startAnimation(valueAnimator)
  }


    /**
     * 设置组合动画播放机制  并行or串行
     */
    fun setPlayType(playType:Int): TrustAnimation {
        animationPlayType = playType
        return this
    }

    /**
     * 添加横向位移动画
     */
    fun addTranslateXAnimationItem(v:View,x:Float,y: Float,duration: Long=1500): TrustAnimation {
        addQueue(v,"translationX" ,x, y, duration)
        return this
    }

    /**
     * 添加缩放动画
     */
    fun addScaleAnimationItem(v:View,scaleSX:Float,scaleEX: Float,scaleSY:Float,scaleEY: Float,duration: Long=1500): TrustAnimation {
        addQueue(v,"scaleX" ,scaleSX, scaleEX, duration)
        addQueue(v,"scaleY" ,scaleSY, scaleEY, duration)
        return this
    }


    /**
     * 添加纵向位移动画
     */
    fun addTranslateYAnimationItem(v:View,x:Float,y: Float,duration: Long=1500): TrustAnimation {
        addQueue(v,"translationY" ,x, y, duration)
        return this
    }

    /**
     * 添加透明度动画
     */
    fun addAlphAnimationItem(v:View,start:Float,end: Float,duration: Long=1500): TrustAnimation {
        addQueue(v, "alpha",start, end, duration)
        return this
    }



    private fun addQueue(v: View,propertyName:String, start: Float, end: Float, duration: Long) {
        val animator = ObjectAnimator.ofFloat(v, propertyName, start, end)
        animator.duration = duration
        animationList.add(animator)
    }

    /**
     * 开始组合动画
     */
    fun startAnimations(){
        if (animationPlayType== PLAY_TOGETHER) {
            animatorSet.playTogether(animationList)
        }else{
            animatorSet.playSequentially(animationList)
        }
        animatorSet.start()
        clearAnimations()
    }

    /**
     * 清理组合动画item
     */
    fun clearAnimations(): TrustAnimation {
        animationList.clear()
        return this
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    /**
     * 补间动画
     */

    fun setAnimationDrawable(context: Context, name:String , idList:ArrayList<Int>, defType:String="drawable",
           duration:Int = 200):TrustViewAnimation.Companion{
        return TrustViewAnimation.setAnimatonDrawable(context,name,defType,duration,idList)
    }
}