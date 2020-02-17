package com.sharkgulf.soloera.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.WEB_FIND
import com.sharkgulf.soloera.TrustAppConfig.WEB_URL_HOST
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_BS_WEV_VIEW
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_blue_shark.*

/**
 *  Created by user on 2019/5/7
 */
class FragmentBlueShark:TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView {
    companion object {
        var webCanBlack:Boolean = false
        var shoppingIsShow:Boolean = false
    }
    private val TAG = "FragmentBlueShark"
    override fun getLayoutId(): Int {
        return R.layout.fragment_blue_shark
    }
    private var mUrl:String = ""
    override fun initView(view: View?, savedInstanceState: Bundle?) {
//        setWebUrl("http://192.168.0.88:9082/app/find/index")
        mUrl = WEB_URL_HOST+WEB_FIND
        setWebUrl(mUrl)
    }

    override fun initData() {
    }

    override fun baseResultOnClick(v: View) {
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

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun onTrustFragmentFirstVisible() {
    }




    @SuppressLint("SetJavaScriptEnabled")
    fun setWebUrl(url:String?){
//        mType = if (!BsApplication.mAuthentication.getUserLoginStatus()) {
//            SHOPPING_STATUS_VISITOR
//        }else{
//            SHOPPING_STATUS_USER
//        }
//
//
////        fragment_shopping_web.loadUrl(url+mType)

        val settings = blue_shark_webview.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.blockNetworkImage = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        blue_shark_webview.requestFocus()
//        mFragmentShoppingJs.setFragment(this)
//        fragment_shopping_web.addJavascriptInterface(mFragmentShoppingJs,"Native")
//        fragment_shopping_web.loadDataWithBaseURL(null,"https://baidu.com", "text/html",  "utf-8", null);
        blue_shark_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                if (request != null && !request.url.toString().contains(mUrl)) {
                    val params = Bundle()
                    params.putString(TrustAppConfig.WEB_INTENT_USER_TYPE_KEY,request!!.url.toString())
                    arouterStartActivity(ROUTE_PATH_BS_WEV_VIEW,params)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                TrustLogUtils.d(TAG,"onPageFinished $url")
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {

                view?.stopLoading()
                view?.clearHistory()
                TrustLogUtils.d(TAG,"onReceivedError  web view error !")
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                TrustLogUtils.d(TAG,"onPageStarted  $url")
                super.onPageStarted(view, url, favicon)
            }
        }

        blue_shark_webview.webChromeClient = object : WebChromeClient() {

        }
        blue_shark_webview.setOnKeyListener(listener)
        blue_shark_webview.loadUrl(url)
    }

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }

    private val listener = View.OnKeyListener { v, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP) {
            TrustLogUtils.d(TAG,"我是fragment black")
            webCanBlack = blue_shark_webview.canGoBack()
            if (keyCode == KeyEvent.KEYCODE_BACK && blue_shark_webview.canGoBack()) { // 表示按返回键
                // 时的操作
                blue_shark_webview.goBack() // 后退
                // webview.goForward();//前进
                true // 已处理
            }

        }
        false
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}