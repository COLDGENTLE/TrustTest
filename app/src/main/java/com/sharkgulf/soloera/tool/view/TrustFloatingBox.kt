package com.sharkgulf.soloera.tool.view

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.config.getGPPopup
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustAppUtils

class TrustFloatingBox(private val context: Context) {

    companion object{
        private var mTrustFloatingBox:TrustFloatingBox? = null

        fun getInstance():TrustFloatingBox{
            if (mTrustFloatingBox == null) {
                mTrustFloatingBox = TrustFloatingBox(TrustAppUtils.getContext())
            }
            return mTrustFloatingBox!!
        }
    }


    private var mWindowManger: WindowManager? = null

    private var dataList = arrayListOf<FlocationBoxViewHodler>()


     fun showFloatingBox(title:String,isError:Boolean){
        if (checkPerssion()) {
            dataList.add(FlocationBoxViewHodler(title,isError))
        } else {
            show(title,isError)
        }
    }

    private fun checkPerssion(): Boolean {
        val b = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !Settings.canDrawOverlays(context)
        } else {
            false
        }
        if (b) {
            getGPPopup().showTrustDoubleBtnPopu("请允许应用获取悬浮权限，便于及时通知车辆消息!",listener = object :TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener{
                override fun onClickListener(isBtn1: Boolean) {
                    if (!isBtn1) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                        intent.flags = FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                }
            })
        }
        return b
    }

    fun checkListFloatingBox(){
        if (!checkPerssion()) {
            if (dataList.isNotEmpty()) {

                val hodler = dataList[0]
                show(hodler.title,hodler.isError)
                dataList.remove(hodler)
                checkListFloatingBox()
            }
        }
    }



    private fun show(title:String,isError:Boolean){
        mWindowManger = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        // 新建悬浮窗控件
        val view = LayoutInflater.from(context).inflate(R.layout.notification_nomol_layout, null, false)
        val layout = view.findViewById<RelativeLayout>(R.id.notification_layout)
        val textView = view.findViewById<TextView>(R.id.notification_layout_tv)
        if (isError) {
            layout.setBackgroundColor(Color.RED)
        }else{
            layout.setBackgroundColor(Color.parseColor("#00c6ff"))
        }
        textView.text = title

        // 设置LayoutParam
        val layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }


//        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.TOP
        layoutParams.windowAnimations=android.R.style.Animation_Translucent
        // 将悬浮窗控件添加到WindowManager
        mWindowManger?.addView(view, layoutParams)

        TrustTools<View>().setCountdown(3,object :TrustTools.CountdownCallBack{
            override fun callBackCountDown() {
                mWindowManger?.removeView(view)
            }
        })

    }

    class FlocationBoxViewHodler(var title:String,var isError:Boolean)

}