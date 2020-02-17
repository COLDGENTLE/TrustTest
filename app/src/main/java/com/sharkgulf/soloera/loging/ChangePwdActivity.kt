package com.sharkgulf.soloera.loging

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.INTENT_ACTIVITY
import com.sharkgulf.soloera.TrustAppConfig.SEND_SMS_SVC_TYPE_VOICE
import com.sharkgulf.soloera.loging.fragment.LoginResetPwdFragment
import com.sharkgulf.soloera.loging.fragment.LoginResetPwdVerificationFragment
import com.sharkgulf.soloera.tool.BsFragmentManger
import com.sharkgulf.soloera.tool.config.DEFULT
import com.sharkgulf.soloera.tool.config.getBsColor
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.tite_layout.*

class ChangePwdActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    companion object{
        var mStatus = DEFULT
    }
    private var title:String? = null
    private val mBsBuilder = BsFragmentManger.BsFragmentBuilder(supportFragmentManager,R.id.change_pwd_framelayout)
            .setIsReplace(false)
    private var mBsFragmentManger:BsFragmentManger? = null

    override fun getLayoutId(): Int { return R.layout.activity_change_pwd }

    override fun initView(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("title")
        chengeFragment()
        title_layout.setBackgroundColor(getBsColor(R.color.blue2fc))
//        mBsFragmentManger =  mBsBuilder.setFragmentList(arrayListOf(fragment,loginResetPwdVerificationFragment))
//                .builder()
//        mBsFragmentManger?.setBsFragmentMangerListener(object :BsFragmentManger.BsFragmentMangerListener{
//            override fun isNothingFragmentShow() {
//                finish()
//            }
//        })
//        mBsFragmentManger?.showChooseFragment(0)
//        mBsFragmentManger?.showChooseFragment(loginResetPwdVerificationFragment)
    }

    private fun chengeFragment() {
        mStatus = DEFULT
        if (title != "注册新用户") {
            title_tx.text = title
        }
        val titleType = intent.getIntExtra("titleType", 0)
        baseSetOnClick(title_back_btn)
        val loginResetPwdVerificationFragment = LoginResetPwdVerificationFragment()
        val fragmentType = intent.getIntExtra("fragment", -1)
        val fragment = if (fragmentType == 2) {
            loginResetPwdVerificationFragment.setIntentType(INTENT_ACTIVITY)
            loginResetPwdVerificationFragment.setType(SEND_SMS_SVC_TYPE_VOICE)
            loginResetPwdVerificationFragment
        } else {
            val loginResetPwdFragment = LoginResetPwdFragment()
            loginResetPwdFragment.setType(titleType)
            loginResetPwdFragment
        }
        supportFragmentManager.beginTransaction().replace(R.id.change_pwd_framelayout, fragment).commit()
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.title_back_btn) {
            if (mStatus == DEFULT) {
                finish()
            }else{
                chengeFragment()
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object : TrustPresenters<TrustView>(){}
    }



}
