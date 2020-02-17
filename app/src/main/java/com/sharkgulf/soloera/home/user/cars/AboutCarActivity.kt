package com.sharkgulf.soloera.home.user.cars

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_ABOUT_CAR
import com.sharkgulf.soloera.tool.config.getCarInfoData
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_about_car.*
import kotlinx.android.synthetic.main.tite_layout.*
@Route(path = ROUTE_PATH_ABOUT_CAR)
class AboutCarActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    private var mCarInfo:CarInfoBean? = null
    override fun getLayoutId(): Int { return R.layout.activity_about_car }

    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = getString(R.string.car_info_about_tx)
        baseSetOnClick(title_back_btn)
    }

    override fun initData() {
        mCarInfo = getCarInfoData()
            if (mCarInfo != null) {
                val base = mCarInfo!!.base
                about_car_mac_tv.text = base.mac
                about_car_imei_tv.text = base.imei
                about_car_sn_tv.text = base.sn
                about_car_imsi_tv.text = base.imsi
                about_car_add_time_tv.text = mCarInfo!!.binded_time
                about_car_register_time_tv.text = mCarInfo!!.activated_time
                about_car_bind_car_time_tv.text = mCarInfo!!.bind_days.toString()
                about_car_bind_total_mileage_tv.text = mCarInfo!!.total_miles.toString()
        }
    }

    override fun baseResultOnClick(v: View) {
        finish()
    }

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
