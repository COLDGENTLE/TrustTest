package com.sharkgulf.soloera.cards.activity.battery

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RelativeLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsBatteryInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.battery.IBatteryView
import com.sharkgulf.soloera.presenter.battery.BatteryPresenter
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_BATTERY
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_battery.*
import kotlinx.android.synthetic.main.tite_layout.*
import java.lang.Exception

@Route(path = ROUTE_PATH_BATTERY)
class BatteryActivity : TrustMVPActivtiy<IBatteryView,BatteryPresenter>() ,IBatteryView{


    private val titles = arrayListOf(setTextStrings(R.string.battery1_tx),
            setTextStrings(R.string.battery2_tx))
    private var mBatteryAdapter:BatteryAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_battery
    }

    private var isHaveBattet = false
    private val REGISTER_TOPIC = WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_POWER
    private val SEND_TOPIC = WEB_SOKECT_SEND+WEB_SOKECT_CAR_POWER
    private val TAG = BatteryActivity::class.java.canonicalName!!
    private var mBattery1Listener: BatteryFragment.BatteryInfoChangeListener? = null
    private var mBattery2Listener: BatteryFragment.BatteryInfoChangeListener? = null

    @SuppressLint("NewApi")
    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        baseSetOnClick(battery_info_btn)
        setTextStrings(title_tx, R.string.battery_title_tx)
        mBatteryAdapter = BatteryAdapter(supportFragmentManager)
        val list = arrayListOf<Fragment>()
        val batteryFragment1 = BatteryFragment()
        val batteryFragment2 = BatteryFragment()
        mBattery1Listener = batteryFragment1.getBatteryInfoChangeListener()
        mBattery2Listener = batteryFragment2.getBatteryInfoChangeListener()
        list.add(batteryFragment1)
        list.add(batteryFragment2)
        mBatteryAdapter?.setTitlesFragments(titles,list)
    }

    @SuppressLint("NewApi")
    override fun initData() { getPresenter()?.getBattery() }


    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.battery_info_btn -> {
                if (!isDemoStatus()) {
                    startActivity(Intent(this,BatteryInfoActivity::class.java))
                }else{
                    showToast(getBsString(R.string.no_login_battery))
                }

//                if (isHaveBattet) {
//                }else{
//                    showToast("当前无电池")
//                }
            }
            else -> {
                finish()
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { showBsToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    override fun createPresenter(): BatteryPresenter {
        val batteryPresenter = BatteryPresenter()
        batteryPresenter.setTAG(TAG)
        return batteryPresenter
    }

    override fun resultBatteryInfo(bean: BsBatteryInfoBean?) {}

    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?,
                                  battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {

        try {
            var mileage = 0

            mileage = changeBatteryInfoUi(battery1_num_tv,battery1_tem_text_tv,battery2_add_power_tv,battry1,battery_status_im,battery_bg,battery_toolbar,battery_status_tv)
            battery_mileage_tv.text = getMileage(mileage)
            battery_wave_progress_view.value = mileage.toFloat()
            circle_progress_bar.value = mileage.toFloat()
            if (emerBattBean != null) {
                battery_day_tv.text = setTextStrings(R.string.emergency_tx,emerBattBean.left_days.toString())
                battery_time_tv.text = setTextStrings(R.string.emergency_time_tx,emerBattBean.time_desc)
            }
            isHaveBattet = !(battry1 == null && battry2 == null)
        }catch (e:Exception){
            e.printStackTrace()
            TrustLogUtils.d(TAG,"error：${e.printStackTrace()}")
        }

    }


}
