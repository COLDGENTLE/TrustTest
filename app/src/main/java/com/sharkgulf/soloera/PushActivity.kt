package com.sharkgulf.soloera

import android.content.Intent
import com.sharkgulf.soloera.jpushlibrary.TrustJPushCallBackActivity
import com.sharkgulf.soloera.tool.alert.AlertDataCenter
import com.trust.demo.basis.trust.utils.TrustLogUtils

class PushActivity : TrustJPushCallBackActivity() {
    private val TAG = PushActivity::class.java.canonicalName
    override fun getLaiyoutId(): Int {
        return R.layout.activity_push
    }

    override fun openedPushCallBack(title: String, msg: String, extras: String?) {
        TrustLogUtils.d(TAG,"extras : $extras")
        if (extras != null) {
            AlertDataCenter.getInstance().sendMsgForHander(extras)
        }else{
            TrustLogUtils.d(TAG,"jpush is no extras ! ----$TAG")
        }
//        if (appIsInit) {
//            startActivity(Intent(context,AlertActivity::class.java))
//        }else{
//            val intent = Intent(this, StartUpActivity::class.java)
//            startActivities(arrayOf(Intent(this,MainHomeActivity::class.java),
//                    Intent(this,AlertActivity::class.java),
//                    intent.putExtra("isStartMain",false)))
//        }
//        appIsInit = true
        finish()
//        TrustTools<View>().setCountdown(1,object : TrustTools.CountdownCallBack{
//            override fun callBackCountDown() {
//
//            }
//        })

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        TrustLogUtils.d(TAG,"onNewIntent")
    }

}
