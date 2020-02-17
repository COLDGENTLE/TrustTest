package com.sharkgulf.soloera.cards.activity.battery

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.battery.IBatteryView
import com.sharkgulf.soloera.presenter.battery.BatteryPresenter
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_battery_info.*
import kotlinx.android.synthetic.main.tite_layout.*

class BatteryInfoActivity : TrustMVPActivtiy<IBatteryView, BatteryPresenter>() , IBatteryView {
    private val TAG = BatteryInfoActivity::class.java.canonicalName!!

    override fun createPresenter(): BatteryPresenter {
        val batteryPresenter = BatteryPresenter()
        batteryPresenter.setTAG(TAG)
        return batteryPresenter
    }

    override fun getLayoutId(): Int { return R.layout.activity_battery_info }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        title_tx.text = "电池基本信息"
    }

    override fun initData() { getPresenter()?.getBattery() }

    override fun baseResultOnClick(v: View) { finish() }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun resultBatteryInfo(bean: BsBatteryInfoBean?) {}

    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {
//        if (battry1 != null && battry2 != null) {
            battert_fragment1_layout.visibility = View.VISIBLE
//            battert_fragment2_layout.visibility = View.VISIBLE
            (battert_fragment1 as BatteryFragment).getBatteryInfoChangeListener().infoChangesListener(1,battry1)
//            (battert_fragment2 as BatteryFragment).getBatteryInfoChangeListener().infoChangesListener(2,battry2)
//        }else if(battry1 != null){
//            battert_fragment1_layout.visibility = View.VISIBLE
//            battert_fragment2_layout.visibility = View.GONE
//            (battert_fragment1 as BatteryFragment).getBatteryInfoChangeListener().infoChangesListener(battry1)
//        }else{
//            battert_fragment1_layout.visibility = View.GONE
//            battert_fragment2_layout.visibility = View.VISIBLE
//            (battert_fragment2 as BatteryFragment).getBatteryInfoChangeListener().infoChangesListener(battry2)
//        }


    }


}
