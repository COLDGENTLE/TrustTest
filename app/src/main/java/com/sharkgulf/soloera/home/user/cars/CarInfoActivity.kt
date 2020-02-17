package com.sharkgulf.soloera.home.user.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.CAR_INFO_KEY
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsDeleteCarBean
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.cars.ICars
import com.sharkgulf.soloera.presenter.cars.CarsPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_ABOUT_CAR
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.getCarInfoData
import com.sharkgulf.soloera.tool.config.glide
import com.sharkgulf.soloera.tool.config.registerUpdateCarInfo
import com.sharkgulf.soloera.tool.config.setTextStrings
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustAnalysis
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_user_car_info.*
import kotlinx.android.synthetic.main.tite_layout.*
@Route(path = "/activity/CarInfoActivity")
class CarInfoActivity : TrustMVPActivtiy<ICars,CarsPresenter>(),ICars {
//    private var mBsGetCarInfoBean:BsGetCarInfoBean.DataBean.BikesBean? = null
    private var mCarInfoBean:CarInfoBean? = null
    private val TAG = CarInfoActivity::class.java.canonicalName
    private val mCarType = arrayListOf(R.string.car_info_light_bicycle_tx
            ,R.string.car_info_light_motorcycle_tx,R.string.car_info_motorcycle_tx)
    override fun getLayoutId(): Int {
        return R.layout.activity_user_car_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        baseSetOnClick(car_info_car_ic_img)
        baseSetOnClick(car_info_btn)
        baseSetOnClick(car_info_about_car_btn)
        title_tx.text = setTextStrings(R.string.car_info_title)
    }
    private var mCarInfoStr:String? = null
    override fun initData() {
        registerUpdateCarInfo()
//        mCarInfoStr = intent.getStringExtra("carInfo")
//        mBsGetCarInfoBean = TrustAnalysis.resultTrustBean(mCarInfoStr,BsGetCarInfoBean.DataBean.BikesBean::class.java)

        changeUi()
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.title_back_btn -> {
                finish()
            }
            R.id.car_info_btn ->{
                CarsEditActivity.startActivity(this,TrustAnalysis.resultString(mCarInfoBean))
            }
            R.id.car_info_about_car_btn -> {
                val params = Bundle()
                params.putString(CAR_INFO_KEY,mCarInfoStr)
                arouterStartActivity(ROUTE_PATH_ABOUT_CAR,params)
            }
            else -> {
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun createPresenter(): CarsPresenter {
        return CarsPresenter()
    }

    companion object {
        fun StartActivity(context: Context,carInfo: String){
            val intent = Intent(context, CarInfoActivity::class.java)
//            intent.putExtra("carInfo",carInfo)
            context.startActivity(intent)
        }
    }


    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
//            mBsGetCarInfoBean = bean
            changeUi()
        }
    }

    private fun changeUi(){
        runOnUiThread {
            mCarInfoBean = getCarInfoData()
//        val bean = mBsGetCarInfoBean?.getData()?.bikes?.get(0)
            setTextStrings(car_info_complete_tx, R.string.car_info_complete_tx, mCarInfoBean?.completion.toString())
//        car_info_complete_tx.text = "资料完整度${mBsGetCarInfoBean?.completion}%"
            TrustLogUtils.d(TAG,"mCarInfoBean?.bike_name :${mCarInfoBean?.bike_name}  id:${mCarInfoBean?.bike_id}")

            car_info_plate_num_tx.text = mCarInfoBean?.plate_num
            var bike_class = mCarInfoBean?.bike_class
            var num = 0
            if (bike_class != null) {
                num = bike_class
            }
            car_info_bike_class_tx.text = setTextStrings(mCarType[num++])

            car_info_bind_days_tx.text = mCarInfoBean?.bind_days.toString()
            car_info_vin_tx.text = mCarInfoBean?.vin
            setTextStrings(car_info_total_mileage_tx, R.string.car_info_total_mileage_num_tx, mCarInfoBean?.total_miles.toString())
            try {
                car_info_car_ic_img.glide(mCarInfoBean?.pic_b,false)
            }catch (e:Exception){
                TrustLogUtils.d(TAG,e.printStackTrace().toString())
            }
            car_info_name_tx.text = mCarInfoBean?.bike_name
        }

    }

    private fun registerUpdateCarInfo(){
        registerUpdateCarInfo(TAG, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                runOnUiThread {
                    if (msg == null) {
                        changeUi()
                    }
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }
}
