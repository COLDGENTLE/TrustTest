package com.sharkgulf.soloera.cards.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.tite_layout.*

class AppsActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>> (),TrustView{
    override fun getLayoutId(): Int { return R.layout.activity_apps }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        title_tx.text = "轻应用实验室"
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        finish()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }
}
