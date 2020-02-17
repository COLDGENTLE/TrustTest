package com.sharkgulf.soloera.cards.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.main.FragmentBlueShark
import com.sharkgulf.soloera.main.FragmentShopping
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.tite_layout.*

class FindBsActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {

    override fun createPresenter():
            TrustPresenters<TrustView> { return object : TrustPresenters<TrustView>(){} }


    override fun getLayoutId(): Int { return R.layout.activity_find_bs }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        title_tx.text = getString(R.string.find_bs_title_tx)
        supportFragmentManager.beginTransaction()
                .replace(R.id.find_bs_framlayout,FragmentBlueShark()).commitAllowingStateLoss()
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        onBackPressed()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun onBackPressed() {
        if (!FragmentShopping.webCanBlack  && !FragmentBlueShark.webCanBlack && !FragmentBlueShark.shoppingIsShow) {
           finish()
        }

    }
}
