package com.sharkgulf.soloera.cards.activity.alert

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.fragment_alert_web.*
import kotlinx.android.synthetic.main.tite_layout.*

/**
 *  Created by user on 2019/9/19
 */
class AlertWebFragment:TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView {
    companion object{
        private var mAlertWebFragment:AlertWebFragment? = null
        fun getInstanse():AlertWebFragment{
            if (mAlertWebFragment == null) {
                mAlertWebFragment = AlertWebFragment()
            }
            return mAlertWebFragment!!
        }
    }
    private var mUrl:String? = null

    fun setUrl(url:String){
        mUrl = url
    }

    override fun getLayoutId(): Int { return R.layout.fragment_alert_web}

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        title_tx.text = "故障详情"
    }

    override fun initData() {
        val settings = fragment_alert_web_wv.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        fragment_alert_web_wv.loadUrl(mUrl)
    }

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.title_back_btn) {
            mActivity?.finish()
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }
}