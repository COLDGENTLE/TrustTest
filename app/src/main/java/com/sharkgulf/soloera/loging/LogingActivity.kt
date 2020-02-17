package com.sharkgulf.soloera.loging

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.loging.fragment.LoginFragment
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import android.content.pm.PackageManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.VERIFICATION_CODE_BACK
import com.sharkgulf.soloera.TrustAppConfig.appChannel
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.loging.fragment.LoginVerificationCodeFragment
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_LOGING
import com.sharkgulf.soloera.tool.config.dataAnalyCenter
import com.sharkgulf.soloera.tool.config.DEFULT
import com.sharkgulf.soloera.tool.config.getBsActivityLifecycleCallbacks
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_loging.*
import kotlinx.android.synthetic.main.activity_loging.title_layout
import kotlinx.android.synthetic.main.tite_layout.*
import java.lang.Exception
import kotlin.collections.HashMap


@Route(path = ROUTE_PATH_LOGING)
class LogingActivity : TrustMVPActivtiy<TrustView, TrustPresenters<TrustView>>(), TrustView {
    private val TAG = "LogingActivity"
    override fun getLayoutId(): Int { return R.layout.activity_loging }
    //false 手机短信登录  true pwd 登录
    private var loginStatus  = false
    private var isFinsh = false
    private val loginFragment = LoginFragment()
    private var  type:Int? = DEFULT
    private val loginVerificationCodeFragment = LoginVerificationCodeFragment()
    @SuppressLint("NewApi")
    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(login_change_login_type_btn)
        baseSetOnClick(login_finsh_btn)
        baseSetOnClick(title_back_btn)

        finshActivityList(this, getBsActivityLifecycleCallbacks().getMainHomeActivity(), getBsActivityLifecycleCallbacks().getThreeLoginActivity())
        loginFragment.setChangeFragmentListener(changFragmentListener)
        chooseFragment(loginFragment,false)

        dataAnalyCenter().registerCallBack(VERIFICATION_CODE_BACK, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                backFragment()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        }, TAG)
    }

    override fun initData() {
        val appInfo = this.packageManager
                .getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA)
        appChannel = appInfo.metaData.getString("UMENG_CHANNEL")

    }

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.login_change_login_type_btn) {
            loginStatus = !loginStatus
            loginFragment.getChangeTitleListener().callBack(loginStatus)
            val s = if (loginStatus) {
                getString(R.string.login_no_pwd_login)
            }else{
                getString(R.string.login_pwd_login)
            }
            login_change_login_type_btn.text = s
        }else if(v.id == R.id.login_finsh_btn){
            back()
        }else if(v.id == R.id.title_back_btn){
            changFragmentListener.backLoginFragment()
        }
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}


    @SuppressLint("NewApi")
    fun chooseFragment(fragment: Fragment, isAdd:Boolean) {
        val replace = supportFragmentManager.beginTransaction().replace(R.id.activity_login_fragment_layout,
                fragment)

        if (isAdd) {
            replace.addToBackStack(null)
        }
        replace.commitAllowingStateLoss()
    }

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object : TrustPresenters<TrustView>() {}
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startActivity() {
//        startActivity(Intent(this, ShowCarActivity::class.java), ActivityOptions.makeSceneTransitionAnimation(this, fragment_login_logo_img, "logo")
//                .toBundle())

    }


    companion object {
        fun startActivity(context: Context, type: Int, data: HashMap<String, String>?, phone: String) {
            val intent = Intent(context, LogingActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("data", data)
            intent.putExtra("phone", phone)
            context.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        type = intent?.getIntExtra("type", -1)
        if (type != null && type != -1) {
            if (type == TrustAppConfig.CHECK_THERE) {
                login_change_login_type_btn .visibility = View.INVISIBLE
                val loginVerificationCodeFragment = LoginVerificationCodeFragment()
                loginVerificationCodeFragment.setType(TrustAppConfig.LOGIN_TYPE_THERE)
                loginVerificationCodeFragment.setPhone(intent!!.getStringExtra("phone"))
                loginVerificationCodeFragment.setData(intent.getSerializableExtra("data") as HashMap<String, String>)
                chooseFragment(loginVerificationCodeFragment,false)
            }
        }

    }

    fun backFragment() {
//        supportFragmentManager.popBackStack()
//        val runtime = Runtime.getRuntime()
//        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK)

        try {
            supportFragmentManager.popBackStackImmediate();
//            onBackPressed()
        } catch (e: Exception) {
            TrustLogUtils.e(TAG, e.message)
        }
//        Thread(Runnable {
//            try {
//                // 创建一个Instrumentation对象
//                val inst = Instrumentation()
//                // 调用inst对象的按键模拟方法
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }).start()
//


    }

    interface changeTitleStatus{
        fun callBack(isInit:Boolean)
    }

    interface changeFragmentListener{
        fun startVerificationCodeFragment(phone:String)
        fun backLoginFragment()
        fun canFinsh()
    }

    private val changFragmentListener = object :changeFragmentListener{


        override fun backLoginFragment() {
            title_layout.visibility = View.GONE
            defult_layout.visibility = View.VISIBLE
            backFragment()
        }

        override fun startVerificationCodeFragment(phone:String) {
            isFinsh = false
            title_layout.visibility = View.VISIBLE
            defult_layout.visibility = View.GONE
            loginVerificationCodeFragment.setPhone(phone)
            chooseFragment(loginVerificationCodeFragment,true)
        }

        override fun canFinsh() {
            isFinsh = true
        }
    }


    override fun onBackPressed() {
        back()

    }


    private fun back(){
        if (type != null) {
            type = null
            finish()
        }else{
            if (isFinsh) {
                finish()
            }else{
                changFragmentListener.backLoginFragment()
            }
        }
    }
}
