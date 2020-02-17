package com.sharkgulf.soloera.cards.activity.securitysettings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_wait_test_notify_help.*
import kotlinx.android.synthetic.main.tite_layout.*

class WaitTestNotifyHelpActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_wait_test_notify_help
    }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(test_notify_help_btn)
        baseSetOnClick(title_back_btn)
    }

    override fun initData() {
        title_tx.text = "APP推送帮助"
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.test_notify_help_btn -> {
                val i =  Intent()
                setResult(WaitTestNotifyActivity.RESULT_CODE, i)
                finish()
            }
            R.id.title_back_btn -> {
                finish()
            }
            else -> {
            }
        }
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
}
