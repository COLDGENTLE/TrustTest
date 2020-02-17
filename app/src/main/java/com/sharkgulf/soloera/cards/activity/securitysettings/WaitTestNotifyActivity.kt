package com.sharkgulf.soloera.cards.activity.securitysettings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.mvpview.securitysettings.ISecuritySettingsView
import com.sharkgulf.soloera.presenter.securitysettings.ISecuritySettingsPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.wait_test_notify_layout.*
import kotlin.concurrent.thread
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.Animator.AnimatorListener
import com.sharkgulf.soloera.tool.view.HoloCircularProgressBar
import android.animation.Animator
import android.widget.TextView
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.config.setViewHide
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.tite_layout.*


/**
 *  Created by user on 2019/9/5
 */
class WaitTestNotifyActivity : TrustMVPActivtiy<ISecuritySettingsView, ISecuritySettingsPresenters>(), ISecuritySettingsView {
    override fun resultFilter() {}

    companion object{
         val REQUEST_CODE = 1
         val RESULT_CODE = 2
    }
    private var misAutoBlack = true
    private var mProgressBarAnimator: ObjectAnimator? = null

    private val TAG = "WaitTestNotifyActivity"
    override fun getLayoutId(): Int {
        return R.layout.wait_test_notify_layout
    }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(test_notify_finsh_btn)
        baseSetOnClick(test_notify_resend_btn)
        baseSetOnClick(test_notify_end_btn)
        baseSetOnClick(title_back_btn)
        title_tx.text = "APP报警通知测试"
    }

    override fun initData() {
        sendTest()
    }

    override fun baseResultOnClick(v: View) {
        when(v.id){
            R.id.title_back_btn,
            R.id.test_notify_finsh_btn,
            R.id.test_notify_end_btn->{ finish() }
            R.id.test_notify_resend_btn->{ startActivityForResult(Intent(this,WaitTestNotifyHelpActivity::class.java), REQUEST_CODE) }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {

    }

    override fun resultError(msg: String) {

    }

    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {

    }

    override fun resultTestNotify(bean: BsTestNotifyBean?) {
        if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            startActivity(intent)
        }
    }

    override fun resultLostModu(bean: BsLostModeBean?) {

    }

    private fun sendTest(){
        misAutoBlack = true
        test_notify_resend_btn.isClickable = false
        if (misAutoBlack) {
            setViewHide(test_alert_auto_black_layout,false)
            setViewHide(test_alert_wait_layout,true)
            setViewHide(test_alert_wait_btn_layout,true)
        }else{
            setViewHide(test_alert_auto_black_layout,true)
            setViewHide(test_alert_wait_layout,false)
            setViewHide(test_alert_wait_btn_layout,false)
        }
        if (misAutoBlack) {
            misAutoBlack = false
            auto(test_notify_auto_black_progressbar,3,test_notify_auto_black_num_tx)
        }else{
            misAutoBlack = true
            auto(test_notify_progressbar,30,test_notify_num_tx)
        }


        thread {
            Thread.sleep(3000)
            runOnUiThread {
                getPresenter()?.testNotify(RequestConfig.testNotify())
            }
           runOnUiThread {
               if (misAutoBlack) {
                   setViewHide(test_alert_auto_black_layout,false)
                   setViewHide(test_alert_wait_layout,true)
                   setViewHide(test_alert_wait_btn_layout,true)
               }else{
                   setViewHide(test_alert_auto_black_layout,true)
                   setViewHide(test_alert_wait_layout,false)
                   setViewHide(test_alert_wait_btn_layout,false)
               }
               auto(test_notify_progressbar,30,test_notify_num_tx)
           }
        }

    }

    override fun createPresenter(): ISecuritySettingsPresenters {
        return ISecuritySettingsPresenters()
    }

    private fun autoWait(){
//        test_notify_resend_btn.isClickable = false
//        animate(test_notify_progressbar,null)
////        animate(test_notify_progressbar, null, 0f, 1000)
//        test_notify_progressbar.animation
//        var i = 30
//        thread {
//            while (i != -1){
//                changeNum(i--)
//                Thread.sleep(1000)
//            }
//        }
    }

    private fun auto(v:HoloCircularProgressBar,num:Int,vTv:TextView){
        test_notify_resend_btn.isClickable = false
        animate(v,null)
        v.animation
        var i = num
        thread {
            while (i != -1){
                changeNum(i--,v,vTv)
                Thread.sleep(1000)
            }
        }
    }

    private fun changeNum(num:Int,v:HoloCircularProgressBar,vTv:TextView){
        runOnUiThread {
            if (num == 0) {
                v.clearAnimation()
                test_notify_resend_btn.isClickable = true
            }
            animate(v,null)
//            test_notify_num_tx.text = num.toString()
            vTv.text = num.toString()
        }
    }

    private fun animate(progressBar: HoloCircularProgressBar,
                        listener: AnimatorListener?) {
        progressBar.progress = 0f
        val progress = (Math.random() * 2).toFloat()
        TrustLogUtils.d(TAG,"progress $progress")
        val duration = 990
        animate(progressBar, listener, 1f, duration)
    }



    private fun animate(progressBar: HoloCircularProgressBar, listener: AnimatorListener?,
                        progress: Float, duration: Int) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress)
        mProgressBarAnimator?.duration = duration.toLong()

        mProgressBarAnimator?.addListener(object : AnimatorListener {

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                progressBar.progress = progress
                TrustLogUtils.d(TAG,"onAnimationEnd progress $progress")
            }

            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationStart(animation: Animator) {}
        })
        if (listener != null) {
            mProgressBarAnimator?.addListener(listener)
        }
        mProgressBarAnimator?.reverse()
        mProgressBarAnimator?.addUpdateListener(AnimatorUpdateListener { animation -> progressBar.progress = animation.animatedValue as Float })
        progressBar.markerProgress = progress
        mProgressBarAnimator?.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            sendTest()
        }

    }
    override fun resultCarModule(str: String, module: Int?, bean: CarInfoBean?) {}

}