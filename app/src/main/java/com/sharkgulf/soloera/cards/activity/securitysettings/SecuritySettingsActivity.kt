package com.sharkgulf.soloera.cards.activity.securitysettings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.mvpview.securitysettings.ISecuritySettingsView
import com.sharkgulf.soloera.presenter.securitysettings.ISecuritySettingsPresenters
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_security_settings.*
import kotlinx.android.synthetic.main.tite_layout.*
import androidx.fragment.app.DialogFragment
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.utils.TrustAppUtils




class SecuritySettingsActivity : TrustMVPActivtiy<ISecuritySettingsView, ISecuritySettingsPresenters>(), ISecuritySettingsView {
    override fun resultFilter() {}

    override fun resultLostModu(bean: BsLostModeBean?) {}
    private var status = SECUTITY_ALERT
    private var oldStatus = SECUTITY_ALERT
    private val TAG = "SecuritySettingsActivity"
    override fun getLayoutId(): Int { return R.layout.activity_security_settings }
    private var showDialog: DialogFragment? = null

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(security_settings_alert_choose_layout)
        baseSetOnClick(home_security_settings_do_not_disturb_choose_layout)
        baseSetOnClick(security_settings_alert_btn)
        baseSetOnClick(security_settings_lose_car_btn)
        baseSetOnClick(security_settings_test_alert_tx)
        baseSetOnClick(title_back_btn)
        title_tx.text = setTextSpanneds(R.string.item_security_settings_title_tx)

        security_settings_alert_msg_tx.text = setTextSpanneds(R.string.security_settings_alert_msg_tx, "", 20)
        security_settings_do_not_disturb_msg_tx.text = setTextSpanneds(R.string.security_settings_do_not_disturb_msg_tx, "", 20)
        security_settings_lose_car_msg_tx.text = setTextSpanneds(R.string.security_settings_lose_car_msg_tx, "", 20)
        security_settings_lose_car_app_alert_tx.text = setTextSpanneds(R.string.security_settings_lose_car_app_alert_tx, "", 20)

    }

    override fun initData() {
        val data = getCarInfoData()
        if (data != null) {
            status = data.security.mode
        }else{
            status = SECUTITY_ALERT
        }
        changeUi()
    }

    override fun baseResultOnClick(v: View) {
        when(v.id){
            R.id.security_settings_alert_choose_layout-> {status = SECUTITY_ALERT
                changeSecurity()
                }
            R.id.home_security_settings_do_not_disturb_choose_layout-> {status = SECUTITY_DO_NOT_DISTURB
                changeSecurity()
                }
            R.id.security_settings_alert_btn ->{startActivity(Intent(this,AlertActivity::class.java))}
            R.id.security_settings_lose_car_btn ->{startActivity(Intent(this,LoseCarActivity::class.java))}
            R.id.security_settings_test_alert_tx ->{testPush()}
            R.id.title_back_btn -> {finish()}

        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
//        if (isShow) {
//            showDialog = TrustDialog().showDialog(supportFragmentManager)
//        }
    }

    override fun diassDialog() { showDialog?.dismiss() }

    override fun showToast(msg: String?) {
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg) }

    override fun createPresenter(): ISecuritySettingsPresenters {
        return ISecuritySettingsPresenters()
    }

    private fun changeUi(){
        security_settings_alert_choose_ic.visibility = if (status == SECUTITY_ALERT) View.VISIBLE else View.INVISIBLE
        home_security_settings_do_not_disturb_choose_ic.visibility = if (status == SECUTITY_DO_NOT_DISTURB) View.VISIBLE else View.INVISIBLE

        swMap
    }

    private fun changeSecurity(){
        getPresenter()?.changeSecurity(RequestConfig.changeSecurity(status, swMap as HashMap<Int, Boolean>))
    }


    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info,this)) {
            dataAnalyCenter().sendLocalData(APP_HOME_SECURITY_SETTINGS)
            changeUi()
        }
    }

    private fun testPush(){
        if (!TrustAppUtils.isNotificationEnabled(this@SecuritySettingsActivity)) {

            getGPPopup().showDoubleBtnPopu(getString(R.string.prompt_tx),
                    getString(R.string.notification_msg_tx),getString(R.string.cancel),
                    getString(R.string.go_set_tx),notificationListener)
        }else{
            startActivity(Intent(this@SecuritySettingsActivity,WaitTestNotifyActivity::class.java))
        }
    }

    override fun resultTestNotify(bean: BsTestNotifyBean?) {}

    private val notificationListener = object :TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener{
        override fun onClickListener(isBtn1: Boolean) {
            if (!isBtn1) {
                TrustAppUtils.gotoSet(this@SecuritySettingsActivity)
            }
        }
    }
    override fun resultCarModule(str: String, module: Int?, bean: CarInfoBean?) {}

}
