package com.sharkgulf.soloera.bindcar.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.bind.IBindView
import com.sharkgulf.soloera.presenter.bind.BindCarPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.demo.basis.trust.utils.setEditText
import kotlinx.android.synthetic.main.fragment_bind_set_car.*

/*
 *Created by Trust on 2019/1/2
 */
class FragmentBindCarSetCar :TrustMVPFragment<IBindView, BindCarPresenter>(),IBindView {


    companion object{
        var isBindStatus = false
        private var mCallBack: BindCarActivity.onFragmentCallBack? = null
        val STATUS_1 = 1  //正常 设置车辆
        val STATUS_2 = 2  //异常 进入ping阶段
        var status = STATUS_1
        private var mPing:String? = null
        fun setOnFragmentCallBack(callBack: BindCarActivity.onFragmentCallBack){ mCallBack = callBack }

        fun setType(type:Int,ping:String? = null){
            status = type
            mPing = ping
        }

        private var mBindBean:BsGetBindInfoBean.DataBean.BindInfoBean? = null

        fun setBleBean(bean:BsGetBindInfoBean.DataBean.BindInfoBean ){
            mBindBean = bean
        }
    }

    override fun showSeachBtn() {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultGetBindInfo(bean: BsGetBindInfoBean?) {}


    override fun getLayoutId(): Int { return R.layout.fragment_bind_set_car }
    private val TAG = FragmentBindCarSetCar::class.java.canonicalName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBindStatus = false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            initConfig()
        }
    }

    private fun initConfig() {
        if (status == STATUS_1) {
            showSuccess()
        }else{
            showPing()
            getPresenter()?.getDevicesInfo()
        }

        if (mBindBean != null) {
            val modelName = mBindBean?.bike_info?.getModel()!!.model_name!!
            bind_car_ble_connection_car_vin_tv.text = setTextStrings(R.string.cars_vin_tx, mBindBean?.bike_info?.getVin()!!)
            bind_car_ble_connection_car_module_tv.text = setTextSpanneds(R.string.cars_model_name_tx, modelName)
            val bikeName = mBindBean?.bike_info?.getBike_name()
            if (bikeName != null && bikeName != "") {
                fragment_set_car_name_ed.setText(bikeName!!.toCharArray(), 0, bikeName?.length!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(fragment_bind_car_to_success_btn)
        baseSetOnClick(bind_car_ble_connection_error_btn)
        baseSetOnClick(bind_car_ble_connection_submint_btn)
        fragment_set_car_name_ed.setOnEditorActionListener { textView, i, keyEvent -> (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) }
        setEditText(fragment_set_car_name_ed,16)

//        fragment_set_car_mac_tx.text  = setTextStrings(R.string.car_mac_tx, mBindBean?.btsign?.bt_mac!!)
//        fragment_set_car_vin_tx.text  = setTextStrings(R.string.car_vin_tx, mBindBean?.bike_info?.getVin()!!)
//        fragment_set_car_name_ed.hint = mBindBean?.bike_info?.getBike_name()
//        fragment_set_car_logo_im.glide(mBindBean!!.bike_info!!.getPic_b())
    }

    override fun initData() {

        initConfig()
    }

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.bind_car_ble_connection_submint_btn) {
            updateBikeInfo()
        }else if(v.id == R.id.bind_car_ble_connection_error_btn){
            getGPPopup().dissBleConnectionPopu()
            BindCarActivity.getBindCar().back(true)
        }
    }


    fun updateBikeInfo() {
        val toString = fragment_set_car_name_ed.text.trim().toString()
        if (toString == null || toString == "") {
            showToast(getBsString(R.string.bike_name_is_null_tx))
        }else{
            getPresenter()!!.requestUpdateCarInfo(RequestConfig.updateProfile(mBindBean!!,toString))
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {  com.sharkgulf.soloera.tool.config.showToast(msg)}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): BindCarPresenter {
        return BindCarPresenter()
    }


    override fun resultBindCar(bean: BsBindCarBean?) {}

    override fun resultGetBleSign(bean: BsBleSignBean?) {}

    override fun resultUpdateBindStatus(bean: BsUpdateBindStatusBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            isBindStatus = true
            BsApplication.bsDbManger?.setBleConfig(mBindBean!!)
            showSuccess()
        }
    }

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean!!.getState()!!,bean.getState_info()!!,this)) {

            mCallBack?.fragmentCallBack(BindCarActivity.FRAGMENT_TYPE_CLOSE)
        }
    }

    fun showPing(){ changePing() }
    fun showError(msg:String){ changeError(msg)}
    fun showSuccess(){ changeSuccess()}

    private fun changePing(){
        changeTitle(getBsString(R.string.add_bikeing))
        isShowLayout(true,false,false)
        bind_car_ble_connection_ping_load_ic.clearAnimation()
        TrustAnimation.getTrustAnimation().addRotateAnimation(noReset = false).startAnimation(bind_car_ble_connection_ping_load_ic)
    }

    private fun isShowLayout(isShowPing:Boolean,isShowError:Boolean,isShowSuccess:Boolean) {
        mActivity!!.runOnUiThread {
            bind_car_ble_connection_error_layout.visibility = if(isShowError) View.VISIBLE else View.GONE
            bind_car_ble_connection_success_layout.visibility = if(isShowSuccess) View.VISIBLE else View.GONE
            bind_car_ble_connection_ping_layout.visibility = if(isShowPing) View.VISIBLE else View.GONE

            bind_car_ble_connection_error_btn.visibility = if(isShowError) View.VISIBLE else View.GONE
            bind_car_ble_connection_submint_btn.visibility = if(isShowSuccess) View.VISIBLE else View.GONE
        }

    }

    private fun changeError(msg:String){
        mActivity!!.runOnUiThread {
            changeTitle(getBsString(R.string.add_error_tx))
            isShowLayout(false,true,false)
            bind_car_ble_connection_submint_btn.visibility = View.GONE
            bind_car_ble_connection_error_msg_tv.text = msg
            bind_car_ble_connection_error_btn.visibility = View.VISIBLE
        }
    }

    private fun changeSuccess(){
        mActivity!!.runOnUiThread {
            changeTitle(getBsString(R.string.add_success_tx))
            isShowLayout(false,false,true)
        }
    }

    private fun changeTitle(title:String) {
        mCallBack?.changeTitle(title)
    }

    private fun sendPing(ping:String){

        bleSendPing(ping, mBindBean!!.btsign!!.salt!!, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
            override fun resultControllTimeOutCallBack() {
                diassDialog()
                showToast(getBsString(R.string.controll_time_out_tx))
            }

            override fun resultControllCallBack(isSuccess: Boolean,errorString:String?) {
                if (isSuccess) {
                    diassDialog()
                    updateBindStatus(TrustAppConfig.BIND_STATUS_SUCCESS, getDbBleBean(mBindBean!!))
                    gprsActivate(object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
                        override fun resultControllCallBack(isSuccess: Boolean,errorString:String?) {
                            TrustLogUtils.d(TAG,"网络激活结果 :$isSuccess")
                        }

                        override fun resultControllTimeOutCallBack() {
                            TrustLogUtils.d(TAG, "激活超时 $isSuccess")
                            diassDialog()
                        }
                    }, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack.BsBleDissConnction {
                        override fun resultDissConnection() {
                            diassDialog()
                            showToast(getBsString(R.string.device_disconnection_tx))
                        }
                    })
                } else {
                    diassDialog()
                    val msg = if (errorString != null) {
                        errorString
                    }else{
                        getBsString(R.string.device_bind_error_tx)
                    }
                    showError(msg)
//                        showToast(msg)
                }

            }
        })

    }


    override fun resultDeviceStatus(status: Boolean, msg: String?) {
        if (!status && msg != null) {
            showError(msg)
        }else{
            if (mPing != null) {
                sendPing(mPing!!)
            }else{
                showToast("ping 为null")
            }
        }
    }

    fun updateBindStatus(bindStatus:Int,bleBean: DbBleBean) {
        getPresenter()!!.requestUpdateBindStatus(RequestConfig.updateBindStatus(bindStatus,bleBean))
    }
}