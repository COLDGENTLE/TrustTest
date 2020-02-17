package com.sharkgulf.soloera.loging

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView

class AddCarShowsActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_add_car_shows
    }

    override fun initView(savedInstanceState: Bundle?) {
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

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }
}
