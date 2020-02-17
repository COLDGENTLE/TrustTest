package com.sharkgulf.soloera.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsOrderInfoBean
import com.sharkgulf.soloera.module.bean.BsOrderStatusBean
import com.sharkgulf.soloera.module.bean.BsTicketBean
import com.sharkgulf.soloera.presenter.web.WebPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.pingpaylib.PayCallBack
import com.sharkgulf.pingpaylib.PingPay
import com.sharkgulf.pingpaylib.addPayCallBack
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_shopping.*

/**
 *  Created by user on 2019/5/7
 */
class FragmentShopping:TrustMVPFragment<com.sharkgulf.soloera.mvpview.web.WebView, WebPresenter>(),com.sharkgulf.soloera.mvpview.web.WebView{

    companion object {
        var webCanBlack:Boolean = false
        var shoppingIsShow:Boolean = false
    }

    private val mFragmentShoppingJs = FragmentShoppingJs()
//    private val url = "https://eshop.d.blueshark.com/index.php?s=/wap"
    private val url = "http://192.168.0.239:9082/app/exper/index"
    private val parames :String = "&AppName=BlueShark&PayAction=1&LoginFirst="
    private var mType:Int = SHOPPING_STATUS_VISITOR
    override fun getLayoutId(): Int {
        return R.layout.fragment_shopping
    }

    @SuppressLint("SetJavaScriptEnabled", "NewApi")
    override fun initView(view: View?, savedInstanceState: Bundle?) {
//        setWebUrl("http://192.168.1.161/niushop_shawan/index.php?s=/wap&AppName=BlueShark&PayAction=1&LoginFirst=0")
//        setWebUrl("https://eshop.d.blueshark.com/index.php?s=/wap&AppName=BlueShark&PayAction=1&LoginFirst=0")

        test_btn.setOnClickListener {
//            fragment_shopping_web.reload()

//            TrustLogUtils.d("FragmentShopping","web 是否可以回退 ${ fragment_shopping_web.canGoBack()}")
        }

        fragment_shopping_web.setOnKeyListener(listener)


        test_reflish_btn.setOnClickListener {
            fragment_shopping_web.reload()
        }

    }

    private val listener = View.OnKeyListener { v, keyCode, event ->
        if (event.action == KeyEvent.ACTION_UP) {
            TrustLogUtils.d("FragmentShopping","我是fragment black")
            webCanBlack = fragment_shopping_web.canGoBack()
            if (keyCode == KeyEvent.KEYCODE_BACK && fragment_shopping_web.canGoBack()) { // 表示按返回键
                // 时的操作
                fragment_shopping_web.goBack() // 后退
                // webview.goForward();//前进
                 true // 已处理
            }

        }
        false
    }

    override fun initData() {
        addPayCallBack(mPayCallBack)

        changeUi()
//        setWebUrl("https://eshop.d.blueshark.com/index.php?s=/wap&AppName=BlueShark&PayAction=1&LoginFirst=$type")
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

    override fun createPresenter(): WebPresenter {
        return WebPresenter()
    }

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
        shoppingIsShow = isVisible
        if (isVisible) {

            fragment_shopping_web.setOnKeyListener(listener)
        }else{
            fragment_shopping_web.setOnKeyListener(null)
        }
        TrustLogUtils.d("FragmentShopping","isVisible $isVisible   userVisibleHint   $userVisibleHint")
    }

    override fun onTrustFragmentFirstVisible() {
        TrustLogUtils.d("FragmentShopping","onTrustFragmentFirstVisible")
    }


    fun callJsTicket(jsMethod:String){
        mActivity!!.runOnUiThread {
            fragment_shopping_web.evaluateJavascript(jsMethod,null)
        }
    }

    fun getTicket(){
        if (BsApplication.mAuthentication.getUserLoginStatus()) {
            getPresenter()?.getTicket(RequestConfig.getTicket())
        }else{
            arouterStartActivity("/activity/LoginActivity")
        }
    }

    fun getOrderInfo(channel:String,amount:Int,order_no:String){
        mOrderId = order_no.toInt()
        getPresenter()?.getOrderInfo(RequestConfig.getOrderInfo(channel,amount,order_no))
    }

    fun getOrderStatus(){
        getPresenter()?.getOrderStatus(hashMapOf(Pair<String,Any>("order_id",mOrderId)))
    }

    override fun resultTicket(bean: BsTicketBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info!!,this)) {
            mFragmentShoppingJs.setTicket(bean.data.ticket)
        }
    }

    private var mOrderId:Int = 0
    override fun resultOrderInfo(bean: BsOrderInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info!!,this)) {
            PingPay(mActivity!!,bean.data.pingpp)
        }
    }


    private val mPayCallBack = object : PayCallBack {
        override fun callBack(result: String?, errorMsg: String?, extraMsg: String?, payStatus: Boolean) {
            if (payStatus) {
                getOrderStatus()
            }else{
                setWebUrl(mFragmentShoppingJs.mFailUrl+parames)
            }
        }
    }

    override fun resultOrderStatus(bean: BsOrderStatusBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info!!,this)) {
            setWebUrl(mFragmentShoppingJs.mSuccessUrl+parames)
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    fun setWebUrl(url:String?){
        mType = if (!BsApplication.mAuthentication.getUserLoginStatus()) {
            SHOPPING_STATUS_VISITOR
        }else{
            SHOPPING_STATUS_USER
        }


//        fragment_shopping_web.loadUrl(url+mType)
        fragment_shopping_web.loadUrl(url)

        val settings = fragment_shopping_web.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        mFragmentShoppingJs.setFragment(this)
        fragment_shopping_web.addJavascriptInterface(mFragmentShoppingJs,"Native")
//        fragment_shopping_web.loadDataWithBaseURL(null,"https://baidu.com", "text/html",  "utf-8", null);
        fragment_shopping_web.webViewClient = object : WebViewClient() {
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

        fragment_shopping_web.webChromeClient = object : WebChromeClient() {

        }
    }

//     private  fun changeUi(){
//        setWebUrl(url+parames)
//    }
    private  fun changeUi(){
        setWebUrl(WEB_URL_HOST+WEB_EXPER)
//        setWebUrl(url)
    }

    fun updateFragment(){
        changeUi()
    }
}