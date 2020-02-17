package com.sharkgulf.soloera.tool.animation

import android.graphics.Camera
import android.util.Log
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.Transformation


/**
 *  Created by user on 2019/3/16
 */
class YRotateAnimation: Animation() {
    var centerX: Int = 0
    var centerY:Int = 0
    var camera = Camera()

    override fun initialize(width: Int, height: Int, parentWidth: Int,
                            parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)
        //获取中心点坐标
        centerX = width / 2
        centerY = height / 2
        //动画执行时间  自行定义
        duration = 1200
        interpolator = DecelerateInterpolator() as Interpolator?
    }

     override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val matrix = t.getMatrix()
        camera.save()
         Log.d("interpolatedTime","interpolatedTime:"+interpolatedTime)
        //中心是绕Y轴旋转  这里可以自行设置X轴 Y轴 Z轴
        camera.rotateY(360 * interpolatedTime)
        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix)
        //设置翻转中心点
        matrix.preTranslate((-centerX).toFloat(), (-centerY).toFloat())
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())
        camera.restore()
    }

}