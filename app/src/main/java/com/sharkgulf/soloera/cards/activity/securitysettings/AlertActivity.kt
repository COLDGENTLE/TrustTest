package com.sharkgulf.soloera.cards.activity.securitysettings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.appcompat.widget.SwitchCompat
import android.view.View
import android.widget.CompoundButton
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.securitysettings.ISecuritySettingsView
import com.sharkgulf.soloera.presenter.securitysettings.ISecuritySettingsPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_alert.*
import kotlinx.android.synthetic.main.tite_layout.*

class AlertActivity : TrustMVPActivtiy<ISecuritySettingsView, ISecuritySettingsPresenters>(),ISecuritySettingsView {



    private val TAG = AlertActivity::class.java.canonicalName
    private var showDialog: DialogFragment? = null
    private var viewId:Int? = null
    private var status = SECUTITY_ALERT
    private var viewStatus:Boolean = false

    private var controllStatus = false
    override fun getLayoutId(): Int { return R.layout.activity_alert }

    @SuppressLint("NewApi")
    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = setTextStrings(R.string.alert_title_tx)
        baseSetOnClick(title_back_btn)
        getPresenter()?.getCarInfo()

//        initSwMapStatus(data?.body)


//        swMap.forEach { t, u ->
//            if (t == alert_medium_shock_sw) {
//                alert_medium_shock_sw.isChecked = u as Boolean
//            }else if (t == alert_power_low_sw){
//                alert_power_low_sw.isChecked = u as Boolean
//            }
//        }

        alert_medium_shock_sw.setOnClickListener(listener)
        alert_power_low_sw.setOnClickListener(listener)
        alert_controll_bike_type_sw.setOnClickListener(listener)
//        alert_medium_shock_sw.setOnCheckedChangeListener(mOnCheckedChangeListener)
//        alert_power_low_sw.setOnCheckedChangeListener(mOnCheckedChangeListener)
//        alert_controll_bike_type_sw.setOnCheckedChangeListener(mOnCheckedChangeListener)
    }

    @SuppressLint("NewApi")
    private fun initSwMapStatus(bean:CarInfoBean?) {
        initSwMap(null)
        if (bean != null) {
            val custom = bean.security.custom
            swMap[alert_medium_shock_sw] = custom.isVibr_moderate
            swMap[alert_power_low_sw] = custom.isPower_low
        }
    }

    override fun initData() {}

    @RequiresApi(Build.VERSION_CODES.N)
    override fun baseResultOnClick(v: View) {
       finish()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
//        if (isShow) {
//            showDialog = TrustDialog().showDialog(supportFragmentManager)
//        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        showBsToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg)}

    override fun createPresenter(): ISecuritySettingsPresenters {
        val iSecuritySettingsPresenters = ISecuritySettingsPresenters()
        iSecuritySettingsPresenters.setTAG(TAG)
        return iSecuritySettingsPresenters
    }


    private val listener = View.OnClickListener {it->
        it as SwitchCompat
        val isChecked= it.isChecked

            TrustLogUtils.d(TAG,"is running")
            if (it.id == R.id.alert_controll_bike_type_sw) {
                status = if (isChecked) {
                    SECUTITY_ALERT
                }else{
                    SECUTITY_DO_NOT_DISTURB
                }
                getPresenter()?.changeSecurity(RequestConfig.changeSecurity(status, swMap as java.util.HashMap<Int, Boolean>))
            }else{
                if (status != SECUTITY_DO_NOT_DISTURB) {
                viewId = it.id
                viewStatus = isChecked
                swMap[viewId] = viewStatus
                getPresenter()?.changeSecurity(RequestConfig.changeSecurity(SECUTITY_ALERT, swMap as HashMap<Int, Boolean>))
                }else{
                    it.isChecked = !isChecked
                 }
                }
    }

    private val mOnCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (!controllStatus) {
            controllStatus = true

        }
    }


    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info,this)) {
            sendBikeInfo()
        }else{
            findViewById<SwitchCompat>(viewId!!).isChecked = !viewStatus
        }
    }

    override fun resultTestNotify(bean: BsTestNotifyBean?) {}
    override fun resultLostModu(bean: BsLostModeBean?) {}

    @SuppressLint("NewApi")
    override fun resultCarModule(str: String, module: Int?, bean: CarInfoBean?) {
        TrustLogUtils.d(TAG,"resultCarModule  module:$module bean:${bean?.security?.custom?.isPower_low}  ${bean?.security?.custom?.isVibr_moderate}")
        if (bean != null && module != null) {
            val mode = if (module != null ) {
                module
            }else{
                SECUTITY_ALERT
            }

            var tile = ""
            var msg = ""
            var isChecked = true
            var ic = R.drawable.map_bike_status_alert_ic
            when (mode) {
                SECUTITY_ALERT -> {
                    status = SECUTITY_ALERT
                    tile = getString(R.string.item_security_settings_status_tx)
                    msg = getString(R.string.item_security_settings_status_msg_tx)
                    ic = R.drawable.map_bike_status_alert_ic
                    isChecked = true
                    val custom = bean.security?.custom
                    changeSwStatus(custom)
                    alert_medium_shock_sw.isClickable = true
                    alert_power_low_sw.isClickable = true
                }
                SECUTITY_DO_NOT_DISTURB -> {
                    status = SECUTITY_DO_NOT_DISTURB
                    tile = getString(R.string.itme_security_settings_do_not_disturb_status_tx)
                    msg = getString(R.string.itme_security_settings_do_not_disturb_status_msg_tx)
                    ic = R.drawable.map_bike_status_no_linsen_ic
                    isChecked = false
                    alert_medium_shock_sw.isClickable = false
                    alert_power_low_sw.isClickable = false
                    changeSwStatus(null)
                }
            }
            alert_controll_bike_type_title_tv.text = tile
            alert_controll_bike_type_msg_tv.text = msg

            val textColor:Int  = if (isChecked) {
                resources.getColor(R.color.colorBlack,null)
            }else{
                resources.getColor(R.color.gray6d,null)
            }
            alert_illegal_move_tv.setTextColor(textColor)
            alert_power_remove_tv.setTextColor(textColor)
            alert_serious_shock_tv.setTextColor(textColor)
            alert_medium_shock_tv.setTextColor(textColor)
            alert_power_low_tv.setTextColor(textColor)

            alert_controll_bike_type_sw.isChecked = isChecked
        }

    }

    private fun changeSwStatus(custom: CarInfoBean.SecurityBean.CustomBean?) {
        if (custom != null) {
            TrustLogUtils.d(TAG,"custom?.isVibr_moderate :${custom?.isVibr_moderate}  alert_medium_shock_sw.isChecked:${alert_medium_shock_sw.isChecked}")
            alert_medium_shock_sw.isChecked = custom?.isVibr_moderate
            alert_power_low_sw.isChecked = custom?.isPower_low
            swMap[alert_medium_shock_sw] = custom?.isVibr_moderate
            swMap[alert_power_low_sw] = custom?.isPower_low
        }else{
            alert_medium_shock_sw.isChecked = false
            alert_power_low_sw.isChecked = false
        }
        getPresenter()?.startFilter()
    }

    override fun resultFilter() {
        controllStatus = false
    }
}
