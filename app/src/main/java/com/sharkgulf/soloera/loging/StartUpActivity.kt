package com.sharkgulf.soloera.loging

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_start_up.*
import com.sharkgulf.soloera.main.MainHomeActivity


@Route(path = "/activity/StartUpActivity")
class StartUpActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun initData() {}

    override fun getLayoutId(): Int { return R.layout.activity_start_up}

    override fun initView(savedInstanceState: Bundle?) {
        isInit = true
        init()
        isStartMain = intent.getBooleanExtra("isStartMain",true)
    }

    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    companion object{
        var isInit =  false
    }


    override fun isVoiceInteraction(): Boolean {
        return false
    }

    private val TAG = "StartUpActivity"
    private var isStartMain = true

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }









    @TargetApi(Build.VERSION_CODES.N)
    private fun init() {
        test____btn.postDelayed({
            if (isStartMain) {
//                ARouter.getInstance().build(ROUTE_PATH_MAIN_HOME).navigation()
                startActivity()
            }
         finish()
        },2000)

    }

    private fun startActivity(){
        startActivity(Intent(this, MainHomeActivity::class.java))
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        finish()
    }
}
