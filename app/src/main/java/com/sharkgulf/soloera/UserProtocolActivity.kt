package com.sharkgulf.soloera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sharkgulf.soloera.TrustAppConfig.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_user_protocol.*
import kotlinx.android.synthetic.main.tite_layout.*

class UserProtocolActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView{

    private var webUrl:String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_user_protocol
    }
    private val TAG = "UserProtocolActivity"
    companion object{
        fun startActivity(context: Context,url:String){
            val intent = Intent(context,UserProtocolActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(TrustAppConfig.WEB_INTENT_USER_TYPE_KEY,url)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val type = intent.getStringExtra("type")
        title_tx.text =
        when (type) {
            URL_PRIVACY_POLICY -> {
                webUrl = URL_PRIVACY_POLICY
                "隐私权政策"
            }
            else -> {
                webUrl = URL_USER_SERVICES_AGREEMENT
                "用户服务协议"
            }
        }

        val settings = user_protocol_web_view.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        TrustLogUtils.d(TAG,"webUrl : ${WEB_URL_HOST+webUrl}")
        user_protocol_web_view.loadUrl(WEB_URL_HOST+webUrl)
        baseSetOnClick(title_back_btn)
        user_protocol_web_view.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
               return false
            }
        }
    }

    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
        finish()
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

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object:TrustPresenters<TrustView>(){}
    }
}
