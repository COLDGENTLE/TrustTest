package com.sharkgulf.soloera.tool.animation

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import android.widget.ImageView
import android.os.Handler


/*
 *Created by Trust on 2018/12/3
 *
 *
 *
 *
    val ls = arrayListOf<Int>()
    for (i in 1..10) {
         val id = resources.getIdentifier("status$i", "drawable", packageName)
         ls.add(id)
    }

    TrustAnimation
           .getTrustAnimation()
           .setAnimationDrawable(this,"status",ls)
           .startAnimation((v as ImageView),object :TrustViewAnimation.animationCallBack{
           override fun animationEnd() {
                    Log.d("lhh","动画结束了")
                    }
            })
 */class TrustViewAnimation {
    companion object {
        var anim:AnimationDrawable?= null
        var time:Long = 0
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setAnimatonDrawable(context: Context,name:String,defType:String="drawable",
                                duration:Int = 200,idList:ArrayList<Int>): Companion {
            time = 0
            anim = AnimationDrawable()
            for (i in 1..idList.size){
                val id = context.applicationContext.resources.getIdentifier(name+i,
                        defType , context.applicationContext.packageName)
                anim!!.addFrame(context.applicationContext.resources.getDrawable(id,null),duration)
                time +=duration
            }
            return  this
        }

        fun startAnimation(v: ImageView,callBack: animationCallBack? = null
                           ,isOneShot:Boolean = true): Companion {
            if (callBack != null) {
                Handler().postDelayed({
                    callBack.animationEnd()
                }, time)
            }

            anim!!.isOneShot = isOneShot
            v.setImageDrawable(anim)
            anim!!.start()
            return this
        }


        fun getAnimationTime():Long{
            return time
        }
    }

    interface animationCallBack{
        fun animationEnd()
    }
}