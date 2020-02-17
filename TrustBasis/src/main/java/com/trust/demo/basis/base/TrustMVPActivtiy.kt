package com.trust.demo.basis.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.trust.demo.basis.base.delegate.TrustMvpActivityDelegateImpl
import com.trust.demo.basis.base.delegate.TrustMvpCallback
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.TrustTools
import com.trust.statusbarlibrary.TrustStatusBarUtils
import java.util.concurrent.TimeUnit
import android.view.ViewGroup
import com.sharkgulf.soloera.jpushlibrary.JpushHuaweiOnActivityResult
import com.sharkgulf.soloera.jpushlibrary.JpushHuaweiOnStart
import com.sharkgulf.soloera.jpushlibrary.JpushHuaweiOnStop
import com.sharkgulf.pingpaylib.resultActivity
import com.sharkgulf.trustthreeloginlibrary.MobclickAgentOnPause
import com.sharkgulf.trustthreeloginlibrary.MobclickAgentOnResume
import com.sharkgulf.trustthreeloginlibrary.UmOnActivityResult


/**
 * Created by Trust on 2018/6/25.
 * MVP Activity
 * 第一重代理 ：代理对象
 * 两个特点:
 * 1：实现目标接口 （可以不要）
 * 2：持有目标对象的引用 （必须）
 *
 * 第二重代理：目标对象
 * 实现目标接口
 */
 abstract class TrustMVPActivtiy <V : TrustView ,P : TrustPresenters<V>>:
        AppCompatActivity(),TrustView ,TrustMvpCallback<V,P>{
    protected var mActivity:Activity? = null
    protected var mContext:Context? = null

    protected abstract fun getLayoutId():Int
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun initData()

    private var mPresenter:P? = null
    private var delegateImpl:TrustMvpActivityDelegateImpl<V,P>? = null

    private fun getDelegateImpl():TrustMvpActivityDelegateImpl<V,P> {
        if (delegateImpl ==  null){
            delegateImpl = TrustMvpActivityDelegateImpl<V,P>(this)
        }

        return delegateImpl!!
    }


    companion object {
         val activityList :ArrayList<Activity> = arrayListOf()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        setStatusBar(Color.parseColor("#000000"))
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mActivity = this
        mContext = this
//        init()
        activityList.add(mActivity!!)
        getDelegateImpl().onCreate(savedInstanceState)

        initView(savedInstanceState)
        initData()


//        // 华为,OPPO机型在StatusBarUtil.setLightStatusBar后布局被顶到状态栏上去了
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val content = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
//            if (content != null ) {
//                content.fitsSystemWindows = true
//            }
//        }
    }


     fun finishActivityList(){
        activityList.forEach {
            it.finish()
        }
    }



     fun finishActivityList(activity: Activity){
        activityList.forEach {
            if (it != activity) {
                it.finish()
            }
        }
    }

    fun finshActivityList(activity1: Activity,activity2: Activity ,activity3:Activity? = null){
        activityList.forEach {
            if (it != activity1 && it != activity2) {
                if (activity3 != null) {
                    if (it != activity3) {
                        it.finish()
                    }
                }else{
                    it.finish()
                }
            }

        }
    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }



    override fun onStart() {
        super.onStart()
        getDelegateImpl().onStart()
        JpushHuaweiOnStart(this)
    }

    override fun onRestart() {
        super.onRestart()
        getDelegateImpl().onRestart()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgentOnResume(this)
        getDelegateImpl().onResume()
    }

    override fun onStop() {
        super.onStop()
        getDelegateImpl().onStop()
        JpushHuaweiOnStop(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        getDelegateImpl().onDestroy()
    }

    override fun onPause() {
        super.onPause()
        MobclickAgentOnPause(this)
        getDelegateImpl().onPause()
    }



    override fun createPresenter(): P {
        return this.mPresenter!!
    }

    override fun getMvpView(): V {
        return this as V
    }

    override fun getPresenter(): P? {
        return this.mPresenter
    }

    override fun setPresenter(prensent: P) {
        this.mPresenter = prensent
    }

    protected fun startActivityResult(activity: Activity,clasz:Class<*>,code:Int){
        val intent:Intent = Intent(activity,clasz)
        activity.startActivityForResult(intent,code)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UmOnActivityResult(this,requestCode,resultCode,data)
        resultActivity(requestCode, resultCode, data)
        onTrustViewActivityResult(requestCode, resultCode, data)
        JpushHuaweiOnActivityResult(this,requestCode,resultCode,data)
    }

    protected fun baseSetOnClick(v:View,milliseconds:Long = 1000){
        RxView
                .clicks(v)
                .throttleFirst(milliseconds,TimeUnit.MILLISECONDS)
                .subscribe { baseResultOnClick(v) }
    }

    abstract fun baseResultOnClick(v:View)

    fun setStatusBar(color:Int){
//        TrustStatusBarUtils.getSingleton(this).setStatusBarLightMode(this,false)
        TrustStatusBarUtils.getSingleton(this).setStatusBarColor(this, color)
    }

    fun baseShowToast(msg:String?){
        if (mActivity !=null && !mActivity!!.isFinishing) {
            mActivity!!.runOnUiThread {
                if (msg != null) {
                    Toast.makeText(mActivity,msg,Toast.LENGTH_LONG).show()
                }
            }

        }
    }


    fun getTxString(id:Int):String{
        return TrustTools.getString(this,id)
    }

    fun getMainLayout():ViewGroup{
        return findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as ViewGroup
    }
}