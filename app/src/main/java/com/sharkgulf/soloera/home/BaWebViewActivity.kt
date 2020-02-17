package com.sharkgulf.soloera.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.WEB_INTENT_USER_TYPE_KEY
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_BS_WEV_VIEW
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_ba_web_view.*
import kotlinx.android.synthetic.main.tite_layout.*

@Route(path = ROUTE_PATH_BS_WEV_VIEW)
class BaWebViewActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    private val TAG = "BaWebViewActivity"
    private var mIsBack:Boolean = true
    override fun getLayoutId(): Int {
        return R.layout.activity_ba_web_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        val url = intent.getStringExtra(WEB_INTENT_USER_TYPE_KEY)
        setWebUrl(bs_web_view,url)
        baseSetOnClick(title_back_btn)
    }

    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
        finishWebView()
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
        return object :TrustPresenters<TrustView>(){}
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun setWebUrl(v:WebView,url:String?){
        val settings = v.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
//        mFragmentShoppingJs.setFragment(this)
//        fragment_shopping_web.addJavascriptInterface(mFragmentShoppingJs,"Native")
//        fragment_shopping_web.loadDataWithBaseURL(null,"https://baidu.com", "text/html",  "utf-8", null);
        v.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                TrustLogUtils.d("FragmentShopping","onPageFinished $url")
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                TrustLogUtils.d("FragmentShopping","onReceivedError")
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                TrustLogUtils.d("FragmentShopping","onPageStarted  $url")
                super.onPageStarted(view, url, favicon)
            }
        }

        v.webChromeClient = object : WebChromeClient() {

        }
        v.setOnKeyListener(listener)
        v.loadUrl(url)
    }


    private val listener = View.OnKeyListener { v, keyCode, event ->

        if (event.action == KeyEvent.ACTION_UP) {

            if (keyCode == KeyEvent.KEYCODE_BACK && bs_web_view.canGoBack()) { // 表示按返回键
                mIsBack = bs_web_view.canGoBack()
                TrustLogUtils.d(TAG,"返回一次")
                // 时的操作
                bs_web_view.goBack() // 后退
                // webview.goForward();//前进
                true // 已处理
            }

        }
        false
    }

    override fun onBackPressed() {
        finishWebView()
    }

    private fun finishWebView(){
        if (!bs_web_view.canGoBack()) {
            finish()
        }
    }


}
