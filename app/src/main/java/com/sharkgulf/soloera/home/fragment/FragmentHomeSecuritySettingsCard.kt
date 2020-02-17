package com.sharkgulf.soloera.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_home_security_settings_card.*

/**
 *  Created by user on 2019/8/5
 */
class FragmentHomeSecuritySettingsCard :TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView{
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_security_settings_card
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {registerUpdateCarInfo()}

    private val TAG = "FragmentHomeSecuritySettingsCard"
    override fun initData() {
        dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE + WEB_SOKECT_CAR_INFO,dataAnalysisCallBack,TAG)
        dataAnalyCenter().registerCallBack(APP_HOME_SECURITY_SETTINGS,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                dataAnalyCenter().sendData(WEB_SOKECT_SEND + WEB_SOKECT_CAR_INFO)
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        },TAG)
    }

    private val dataAnalysisCallBack = object :DataAnalysisCenter.onDataAnalysisCallBack{
        override fun onNoticeCallBack(msg: String?) {
            changeUi()
        }
        override fun onErrorCallBack(msg: String, timeOutMsg: String?) {}
    }

    override fun onResume() {
        super.onResume()
        changeUi()
    }

    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
        TrustLogUtils.d(TAG,"onTrustFragmentVisibleChange $isVisible")
    }

    override fun onTrustFragmentFirstVisible() {}

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }

    fun changeUi(){
        val data = dataAnalyCenter().getData<CarInfoBean>(WEB_SOKECT_RECEIVE + WEB_SOKECT_CAR_INFO)
        val bikeInfo = data
        val mode = if (data != null && bikeInfo!=null) {
            bikeInfo.security.mode
        }else{
            SECUTITY_ALERT
        }

        when (mode) {
            SECUTITY_ALERT -> {
                setUi(R.string.item_security_settings_status_tx,R.string.item_security_settings_status_msg_tx,R.drawable.home_security_settings_alert_ic)
            }
            SECUTITY_DO_NOT_DISTURB -> {
                setUi(R.string.itme_security_settings_do_not_disturb_status_tx,R.string.itme_security_settings_do_not_disturb_status_msg_tx,R.drawable.home_security_settings_do_not_disturb_ic)
            }
            SECUTITY_LOSE_CAR -> {
                setUi(R.string.itme_security_settings_lose_status_tx,R.string.itme_security_settings_lose_status_msg_tx,R.drawable.home_security_settings_alert_ic)
            }

        }
    }

    private fun setUi(title:Int,msg:Int,ic:Int){
        card_security_settings_status_ic?.setImageResource(ic)
        security_settings_title_tx?.text = setTextStrings(title)
        security_settings_msg_tx?.text = setTextStrings(msg)
    }


    private fun registerUpdateCarInfo(){
        registerUpdateCarInfo(TAG, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                if (msg != null) {
                    changeUi()
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }
}