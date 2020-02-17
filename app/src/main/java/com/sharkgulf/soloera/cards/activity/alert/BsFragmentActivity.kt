package com.sharkgulf.soloera.cards.activity.alert

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.config.ALERT_KEY
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/9/19
 */
class BsFragmentActivity:TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int { return R.layout.bs_fragment_activitiy_layout}

    override fun initView(savedInstanceState: Bundle?) {}

    override fun initData() {
        val stringExtra = intent.getStringExtra(ALERT_KEY)
        val instanse = AlertWebFragment()
        instanse.setUrl(stringExtra)
        supportFragmentManager.beginTransaction().replace(R.id.bs_fragment_activity_fragmentlayout,instanse).commitAllowingStateLoss()
    }

    override fun baseResultOnClick(v: View) {}

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