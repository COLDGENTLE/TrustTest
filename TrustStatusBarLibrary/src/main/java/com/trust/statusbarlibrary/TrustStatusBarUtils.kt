package com.trust.statusbarlibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.trust.statusbarlibrary.view.TrustStatusBarView
import android.view.Window







/**
 * Created by Trust on 2018/5/25.
 * 状态栏封装类
 */
class TrustStatusBarUtils (private val context: Context){

    companion object {
      @SuppressLint("StaticFieldLeak")
      private var trustStatusBarUtils:TrustStatusBarUtils?=null
        fun getSingleton(context: Context) :TrustStatusBarUtils{
            if (trustStatusBarUtils == null) {
                trustStatusBarUtils = TrustStatusBarUtils(context)
            }
            return trustStatusBarUtils!!
        }
    }


    /**
     * 设置状态栏颜色
     */
    fun setStatusBarColor(activity: Activity ,color:Int){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity.window.statusBarColor = color
        }else{
            val decorView:ViewGroup = activity.window.decorView as ViewGroup
            val statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight())
            statusBarView.setBackgroundColor(color)
            decorView.addView(statusBarView,lp)
        }
    }


    /**
     * 状态栏透明   7.0有灰色遮罩
     * @param   . 与目标控件 同级view
     */
    fun setStatusBarTransparentIsMask(activity: Activity,view:View){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           val window = activity.window
           window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
           val statusBarHeight = getStatusBarHeight()
           view.setPadding(0, statusBarHeight, 0, 0)
       }
   }

    fun setStatusBarTansparentIsMask(activity: Activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            var params = activity.window.attributes
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            activity.window.attributes = params
        }
    }

    /**
     * 状态栏透明   7.0无灰色遮罩
     */
    fun setStatusBarTransparent(activity: Activity){
        setBarColor(activity,Color.TRANSPARENT)
    }

    /**
     * 设置状态栏渐变色
     * @param  colorArray 渐变色 数组   第一个color  是开始  最后一个是结束
     * @param  angle 旋转角度 目前支持 0  90  180
     *
     * */
    fun setStatusBarGradient(activity: Activity,colorArray:IntArray?,angle:Int){
        setStatusBarColor(activity,Color.TRANSPARENT)
        val decorView:ViewGroup = activity.window.decorView as ViewGroup
        val statusBarView = TrustStatusBarView(activity,null)
        statusBarView.setAngle(angle)
        statusBarView.setGradients(colorArray)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight())
        decorView.addView(statusBarView,lp)
    }


    /**
     * 隐藏状态栏
     */
    fun hideStatusBar(activity: Activity){
        val window = activity.window
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag,flag)
    }




    /**
     * 通过反射获取状态栏高度
     */
    private fun getStatusBarHeight() :Int{
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if(resourceId >0){
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 修改状态栏颜色  透明不会有灰色遮罩
     */
    private fun setBarColor(activity: Activity,color: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val win = activity.window
            val decorView = win.decorView
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)//沉浸式状态栏(4.4-5.0透明，5
            // .0以上半透明)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//android5.0以上设置透明效果
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //清除flag，为了android5.0以上也全透明效果
                //让应用的主体内容占用系统状态栏的空间
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = decorView.systemUiVisibility or option
                win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                win.statusBarColor = color//设置状态栏背景色
            }
        }
    }


    /**
     * 设置状态栏文字颜色  true 为黑色  false 为白色（XML跟布局配置  android:fitsSystemWindows="true"，否则布局会上移）
     */
    fun setStatusBarLightMode(activity: Activity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (dark) {  //
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity.window, dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //其他
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity.window, dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window!!.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window!!.setAttributes(lp)
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun MIUISetStatusBarLightMode(activity: Activity, dark: Boolean): Boolean {
        var result = false
        val window = activity.window
        if (window != null) {

            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
            }

        }
        return result
    }

}