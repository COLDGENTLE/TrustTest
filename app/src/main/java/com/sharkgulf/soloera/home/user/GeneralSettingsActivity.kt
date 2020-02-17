package com.sharkgulf.soloera.home.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bs.trust.mapslibrary.TrustGaoDeMapsTool
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_general_settings.*
import kotlinx.android.synthetic.main.tite_layout.*
@Route(path = "/activity/GeneralSettingsActivity")
class GeneralSettingsActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_general_settings
    }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(activity_general_settings_offlinemap_btn)
        baseSetOnClick(title_back_btn)
        baseSetOnClick(activity_general_settings_about_bs_btn)
        title_tx.text = "通用设置"
    }

    override fun initData() {
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
             R.id.activity_general_settings_offlinemap_btn-> {
                 TrustGaoDeMapsTool.getOfflineMapTool(this@GeneralSettingsActivity).startOfflineActivity(this)
            }
            R.id.title_back_btn -> {
                finish()
            }
            R.id.activity_general_settings_about_bs_btn ->{
                startActivity(Intent(mContext, AboutBsActivity::class.java))
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
