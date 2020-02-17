package com.sharkgulf.soloera.tool.alert

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.socketbean.WebAlertBean
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import razerdp.basepopup.BasePopupWindow
import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.UserProtocolActivity
import com.sharkgulf.soloera.cards.activity.AlertPopupwindowPrivacyPolicyActivity
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.tool.config.*


/**
 *  Created by user on 2019/9/9
 */
class AlertTool {
    companion object{
        private var mAlertTool:AlertTool? = null
        fun getInstance():AlertTool{
            if (mAlertTool == null) {
                mAlertTool = AlertTool()
            }
            return mAlertTool!!
        }
    }
    private val TAG = "AlertTool"
    private val mShowAlertPopuMagner:ShowAlertPopuMagner = ShowAlertPopuMagner.getInstance()

    //显示全屏 弹框
    @Synchronized
    fun showAlertWebData(bean:WebAlertBean.BodyBean){
        if (bean.event == TEST_ALERT) {
            mShowAlertPopuMagner.showAlertTestPopu(bean,System.currentTimeMillis())
        }else{

            mShowAlertPopuMagner.showAlertPopu(bean.title,bean.content, alertInfoMap[bean.event] ,bean,System.currentTimeMillis())
        }
    }

    //显示 notification 及时悬浮提醒
    @Synchronized
    fun showAlertToast(bean:WebAlertBean.BodyBean){
        mShowAlertPopuMagner.showAlertToast(bean)
    }

    //收到不提示
    @Synchronized
    fun showAlertNoUi(bean:WebAlertBean.BodyBean){
        mShowAlertPopuMagner.showAlertNoUi(bean)
    }

    //显示 popu
    @Synchronized
    fun showAlertPopu(bean: WebAlertBean.BodyBean){
        mShowAlertPopuMagner.showAlertPopu(bean)
    }






    fun showNotifiction(data:String){

    }



    fun showPrivacyPolicy(){
        val intent = Intent(getContext(), AlertPopupwindowPrivacyPolicyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        getContext().startActivity(intent)
        var mPopuwindow:BasePopupWindow? = null
            mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.popupwindow_first_privacy_policy, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                val text = v!!.findViewById<TextView>(R.id.popupwindow_privacy_policy_tx)
                v.findViewById<TextView>(R.id.privacy_policy_disagree_btn).setOnClickListener {
                    showDisAgreePrivacyPolicy()
                    mPopuwindow?.dismiss()
                }

                v.findViewById<TextView>(R.id.privacy_policy_agree_btn).setOnClickListener {
                    setAppPrivacyPilicyStatus(true)
                    mPopuwindow?.dismiss()
                }

                setHtmlSpanneds(text,R.string.popupwindow_first_privacy_policy_tx,color = R.color.colorBlue4ff,callBack = SizeLabel.HtmlOnClickListener {
                    UserProtocolActivity.startActivity(getBsActivity(), TrustAppConfig.URL_PRIVACY_POLICY)
                })
                return v
            }
        })
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackPressEnable(false)
        mPopuwindow.showPopupWindow()
    }


    fun showDisAgreePrivacyPolicy(){
        var mPopuwindow:BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.popupwindow_first_disagree_privacy_policy, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                val text = v!!.findViewById<TextView>(R.id.popupwindow_disagree_privacy_policy_tx)

                v.findViewById<TextView>(R.id.privacy_policy_disagree_btn).setOnClickListener {
                    mPopuwindow?.dismiss()
                    showPrivacyPolicy()
                }

                 setHtmlSpanneds(text,R.string.popupwindow_disagree_privacy_policy_tx,color = R.color.colorBlue4ff,callBack = SizeLabel.HtmlOnClickListener {
                    UserProtocolActivity.startActivity(getBsActivity(), TrustAppConfig.URL_PRIVACY_POLICY)
                })

                return v
            }
        })
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackPressEnable(false)
        mPopuwindow.showPopupWindow()
    }

    class AlertPopuBean( var time:Long ,var popu:BasePopupWindow)


    fun showIsToVoiceVerification(listener: AlertToolListener.isToVoiceListener,doubleBtmTx:String? = null){
        var mPopuwindow:BasePopupWindow? = null
        mPopuwindow = TrustBasePopuwindow.getPopuwindow(getContext(), R.layout.popupwindow_is_to_voice, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            override fun resultPopuwindowViewListener(v: View?): View {
                val text = v!!.findViewById<TextView>(R.id.voice_tx)
                text.text = setTextSpanneds(R.string.is_to_voice_ui_tx)
                val doubleBtn = v.findViewById<TextView>(R.id.voice_agree_btn)
                if (doubleBtmTx != null) {
                    doubleBtn.text = doubleBtmTx
                }

                doubleBtn.setOnClickListener {
                    mPopuwindow?.dismiss()
                    listener.onToListener(true)
                }

                v.findViewById<View>(R.id.voice_disagree_btn).setOnClickListener {
                    mPopuwindow?.dismiss()
                    listener.onToListener(false)
                }

                text.movementMethod = LinkMovementMethod.getInstance()
                text.highlightColor = Color.parseColor("#00000000")
                return v
            }
        })
        mPopuwindow.setBackgroundColor(Color.TRANSPARENT)
        mPopuwindow.setBackPressEnable(false)
        mPopuwindow.showPopupWindow()
    }

    interface AlertToolListener{
        interface isToVoiceListener{
            fun onToListener(isTo:Boolean)
        }
    }


}