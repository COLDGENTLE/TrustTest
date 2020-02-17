package com.sharkgulf.soloera.home.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.UserProtocolActivity
import com.sharkgulf.soloera.module.bean.BsAppVersionInfoBean
import com.sharkgulf.soloera.mvpview.UtilsView
import com.sharkgulf.soloera.presenter.UtilsPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.tool.config.getAppConfig
import com.sharkgulf.soloera.tool.config.setHtmlSpanneds
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustAppUtils
import com.trust.demo.basis.updateapp.CheckVersionTask
import com.trust.demo.basis.updateapp.UpdataInfo
import kotlinx.android.synthetic.main.activity_about_bs.*
import kotlinx.android.synthetic.main.tite_layout.*

class AboutBsActivity : TrustMVPActivtiy<UtilsView, UtilsPresenters>(),UtilsView {


    override fun getLayoutId(): Int {
        return R.layout.activity_about_bs
    }
    private var mDeBugExpansionNum = 0
    private val EXPANSION_NUM = 20
    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = setTextSpanneds(R.string.about_bs)
        baseSetOnClick(about_version_btn)
        baseSetOnClick(title_back_btn)
        baseSetOnClick(about_bs_debug_expansion_btn)

        about_version_tx.text = "version:${TrustAppUtils.getAppVersionName(this)}"
        about_bs_privacy_policy_tv.text = setHtmlSpanneds(R.string.privacy_policy_tx,"",color =R.color.blueF1,callBack = SizeLabel.HtmlOnClickListener { action ->
            if (action == "0") {
                intentProtocol(TrustAppConfig.URL_USER_SERVICES_AGREEMENT)
            }else{
                intentProtocol(TrustAppConfig.URL_PRIVACY_POLICY)
            }
        })
        about_bs_privacy_policy_tv.movementMethod = LinkMovementMethod.getInstance()
        about_bs_privacy_policy_tv.highlightColor = Color.parseColor("#00000000")
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.about_version_btn -> {
                getPresenter()?.requestAppVersion(RequestConfig.requestAppVersion())
            }
            R.id.about_bs_debug_expansion_btn -> {
                if (mDeBugExpansionNum == EXPANSION_NUM) {
                    val isDebug = getAppConfig().getIsDebug()
                    getAppConfig().setAppModule(!isDebug)
                    showToast("已经切换域名请退出App")
                }
                mDeBugExpansionNum++
            }
            else -> {
                finish()
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun createPresenter(): UtilsPresenters {
        return UtilsPresenters()
    }

    override fun resultAppVersionInfo(bean: BsAppVersionInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            val data = bean.getData()
            Thread(CheckVersionTask(this,"${TrustAppUtils.getContext()
                    .packageName}.fileprovider",TrustAppUtils.getAppVersionName(this),
                    UpdataInfo(data?.ver_code.toString(),"https://qd.myapp.com/myapp/qqteam/Androidlite/qqlite_3.7.1.704_android_r110206_GuanWang_537057973_release_10000484.apk" ,data?.des )
            )).start()
        }
    }

    /**
     * 跳转协议页面
     */
    private fun intentProtocol(type:String){
        val intent = Intent(mActivity, UserProtocolActivity::class.java)
        intent.putExtra(TrustAppConfig.WEB_INTENT_USER_TYPE_KEY,type)
        mActivity?.startActivity(intent)
    }
}
