package com.sharkgulf.soloera.tool.alert

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES.N_MR1
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.appliction.BsActivityLifecycleCallbacks
import com.sharkgulf.soloera.cards.activity.alert.BsFragmentActivity
import com.sharkgulf.soloera.cards.activity.history.RideTrackActivity
import com.sharkgulf.soloera.module.bean.socketbean.WebAlertBean
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.retrofit.config.ProjectInit

/**
 *  Created by user on 2019/11/20
 */
class ShowAlertPopuMagner {
    private val popuMap:HashMap<Int,ArrayList<AlertTool.AlertPopuBean>> = hashMapOf()
    private var mIsStartVoice  = false
    private var mMediaPlayer:MediaPlayer? = null
    private val vibrator = TrustAppUtils.getContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val TAG = ShowAlertPopuMagner::class.java.canonicalName

    companion object{
        private var mShowAlertPopu:ShowAlertPopuMagner? = null
        fun getInstance():ShowAlertPopuMagner{
            if (mShowAlertPopu == null) {
                mShowAlertPopu = ShowAlertPopuMagner()
            }
            return mShowAlertPopu!!
        }
    }




     fun showAlertPopu(title:String, content:String, alertInfoBean: AlertInfoBean? = null, bean : WebAlertBean.BodyBean, time:Long){
         if (bean.actions.size ==1) {
            showAlertOnlyPOpu(title,content,alertInfoBean,bean,time)
         }else{
            showAlertDoublePopu(title,content,alertInfoBean,bean,time)
         }
    }

    private fun showAlertDoublePopu(title:String, content:String, alertInfoBean: AlertInfoBean? = null, bean : WebAlertBean.BodyBean, time:Long){
        val actionsBean1 = bean.actions[0]
        val actionsBean2 = bean.actions[1]
        val mPopuwindow = TrustBasePopuwindow.
                getPopuwindow(TrustAppUtils.getContext(),
                        R.layout.popuwindow_alert,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
                    override fun resultPopuwindowViewListener(v: View?): View {
                        if (alertInfoBean != null) {
                            v!!.findViewById<ImageView>(R.id.popu_alert_ic).setImageResource(alertInfoBean.ic)
                        }
                        v!!.findViewById<TextView>(R.id.popu_alert_title_tx).text = title
                        v.findViewById<TextView>(R.id.popu_alert_content_tx).text = content
                        val btn1 = v.findViewById<Button>(R.id.popu_alert_submint_btn)
                        btn1.text = actionsBean1.text
                        btn1.setOnClickListener { checkIsStart(bean,time,actionsBean1.result) }
                        val btn2 = v.findViewById<Button>(R.id.popu_alert_cancel_btn)
                        btn2.text = actionsBean2.text
                        btn2.setOnClickListener {
                            checkIsStart(bean,time,actionsBean2.result)
                            dataAnalyCenter().sendLocalData(TrustAppConfig.APP_MESSAGER_READ,false.toString())
//                    arouterStartActivity("/activity/LoginActivity")
                        }
                        return v
                    }
                }).setPopupGravity(Gravity.CENTER )
        mPopuwindow?.isAllowDismissWhenTouchOutside = false
        mPopuwindow?.setBackPressEnable(false)
        mPopuwindow?.showPopupWindow()
        var arrayList = popuMap[bean.event]
        if (arrayList != null && arrayList.isNotEmpty()) {
            arrayList.add(AlertTool.AlertPopuBean(time, mPopuwindow))
        }else{
            arrayList = arrayListOf(AlertTool.AlertPopuBean(time, mPopuwindow))
        }
        popuMap[bean.event] = arrayList
        startVoice()
    }

    private fun showAlertOnlyPOpu(title:String, content:String, alertInfoBean: AlertInfoBean? = null, bean : WebAlertBean.BodyBean, time:Long){
        val actionsBean = bean.actions[0]
        val mPopuwindow = TrustBasePopuwindow.
                getPopuwindow(TrustAppUtils.getContext(),
                        R.layout.popuwindow_only_alert,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
                    override fun resultPopuwindowViewListener(v: View?): View {
                        if (alertInfoBean != null) {
                            v!!.findViewById<ImageView>(R.id.popu_alert_ic).setImageResource(alertInfoBean.ic)
                        }
                        v!!.findViewById<TextView>(R.id.popu_alert_title_tx).text = title
                        v.findViewById<TextView>(R.id.popu_alert_content_tx).text = content

                        val btn = v.findViewById<Button>(R.id.popu_alert_only_cancel_btn)
                        btn.text = actionsBean.text
                        btn.setOnClickListener {
                          checkIsStart(bean,time,actionsBean.result)
//                            dataAnalyCenter().sendLocalData(TrustAppConfig.APP_MESSAGER_READ,true.toString())
//                    arouterStartActivity("/activity/LoginActivity")
                        }
                        return v
                    }
                }).setPopupGravity(Gravity.CENTER )
        mPopuwindow?.isAllowDismissWhenTouchOutside = false
        mPopuwindow?.setBackPressEnable(false)
        mPopuwindow?.showPopupWindow()
        var arrayList = popuMap[bean.event]
        if (arrayList != null && arrayList.isNotEmpty()) {
            arrayList.add(AlertTool.AlertPopuBean(time, mPopuwindow))
        }else{
            arrayList = arrayListOf(AlertTool.AlertPopuBean(time, mPopuwindow))
        }
        popuMap[bean.event] = arrayList
        startVoice()
    }



    @Synchronized
    private fun startVoice(){
        if (!mIsStartVoice) {
            mIsStartVoice = true
            alarmVoice()
//            systemVoice()
        }
    }

    private fun stopVoice(){
        mIsStartVoice = false
        vibrator.cancel()
        mMediaPlayer?.stop()
    }

    private fun alarmVoice(){
        vibrator(1000 * 60)
        mMediaPlayer= MediaPlayer.create(TrustAppUtils.getContext(), R.raw.v_warn)
        mMediaPlayer?.isLooping = true
        mMediaPlayer?.stop()
        mMediaPlayer?.prepare()
        mMediaPlayer?.start()
        android.os.Handler().postDelayed({ stopVoice() },1000 * 60)
    }


    private fun systemVoice(){
        vibrator(1000)
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val rt = RingtoneManager.getRingtone(ProjectInit.getApplicationContext(), uri)
        rt.play()
        mIsStartVoice = false
    }



    @SuppressLint("NewApi")
    private fun vibrator(time:Long){
        if(Build.VERSION.SDK_INT <= N_MR1){
            vibrator.vibrate(time)
        }else{
            vibrator.vibrate(VibrationEffect.createOneShot(time,255))

        }

    }




     fun showAlertTestPopu(bean :WebAlertBean.BodyBean, time:Long){
        TrustLogUtils.d(TAG,"activity ${BsActivityLifecycleCallbacks.getInstance().getActivity()}")
        val mPopuwindow = TrustBasePopuwindow.
                getPopuwindow(TrustAppUtils.getContext(),
                        R.layout.popuwindow_alert_test,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
                    override fun resultPopuwindowViewListener(v: View?): View {
                        v!!.findViewById<TextView>(R.id.popu_alert__test_title_tx).text = bean.title
                        v.findViewById<TextView>(R.id.popu_alert__test_content_tx).text = bean.content
//                v.findViewById<Button>(R.id.popu_alert_submint_btn).setOnClickListener {
//                    popuMap[time]?.dismiss()
//                    if (intent != null) {
//                        stopVoice()
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        TrustAppUtils.getContext().startActivity(intent)
//                    }
//                }

                        v.findViewById<Button>(R.id.popu_alert_test_submint_btn).setOnClickListener {
                            stopVoice()
                            popuMap[bean.event]!!.forEach { it.popu.dismiss() }
                            getBsActivityLifecycleCallbacks().finishTestActivity()
//                    arouterStartActivity("/activity/LoginActivity")
                        }
                        return v
                    }
                }).setPopupGravity(Gravity.CENTER )
        mPopuwindow?.isAllowDismissWhenTouchOutside = false
        mPopuwindow?.showPopupWindow()
        var arrayList = popuMap[bean.event]
        if (arrayList != null && arrayList.isNotEmpty()) {
            arrayList.add(AlertTool.AlertPopuBean(time, mPopuwindow))
        }else{
            arrayList = arrayListOf(AlertTool.AlertPopuBean(time, mPopuwindow))
        }
        popuMap[bean.event] = arrayList
        startVoice()
    }



    fun showAlertToast(bean: WebAlertBean.BodyBean){
        getApplicetion().getService()?.showFloatingBox(bean.title,bean.event != DEV_REGISTRY)
//        NotificationUtil.showFullScreen(TrustAppUtils.getContext(),R.mipmap.bs_ic_round,bean.title,bean.event != DEV_REGISTRY)
    }

    fun showAlertPopu(bean: WebAlertBean.BodyBean){
//        showToast("alert Popu")
    }

    fun showAlertNoUi(bean: WebAlertBean.BodyBean){
//        showToast("alert no ui")
    }


    private fun checkIsStart(bean : WebAlertBean.BodyBean,time:Long,result:String?){

        val find = popuMap[bean.event]?.find { it.time == time }
        var intent:Intent? = null
        if (find != null) {
            stopVoice()
            if (bean.event == ALERT_MOVE) {
                intent = Intent(TrustAppUtils.getContext(), RideTrackActivity::class.java)
                intent.putExtra(ALERT_KEY,bean.exts)
            }else{
                intent = Intent(TrustAppUtils.getContext(), BsFragmentActivity::class.java)
                intent.putExtra(ALERT_KEY,result)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (result != null && result != "") {
                TrustAppUtils.getContext().startActivity(intent)
                find.popu.dismiss()
            }else{
                popuMap[bean.event]!!.forEach { it.popu.dismiss()
            }
        }

    }
    }
}