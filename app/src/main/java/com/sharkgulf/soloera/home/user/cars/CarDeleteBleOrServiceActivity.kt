package com.sharkgulf.soloera.home.user.cars

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_car_delete_ble_or_service.*
import kotlinx.android.synthetic.main.tite_layout.*

class CarDeleteBleOrServiceActivity : TrustMVPActivtiy<IUser, UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {

    }

    override fun resultPointInFo(bean: BsPointinfoBean?) {

    }

    override fun resultPointDetail(bean: BsPointDetailBean?) {

    }

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {

    }

    companion object {
        fun startActivity(context: Context,bikeId:Int){
            val intent = Intent(context,CarDeleteBleOrServiceActivity::class.java)
            intent.putExtra("bikeId",bikeId)
            context.startActivity(intent)
        }

        private val BLE_CODE  = 0x0011
    }
    private var mBikeId:Int = -1
    private var mStatus = false //删除状态   false 是普通删除  true是强力删除
    private val carDeketeBleOrServiceAdapter = CarDeketeBleOrServiceAdapter().init()
    override fun getLayoutId(): Int {
        return R.layout.activity_car_delete_ble_or_service
    }

    override fun initView(savedInstanceState: Bundle?) {
        carDeketeBleOrServiceAdapter.setmBleOrServiceListener(mBleOrServiceListener)
        mBikeId = intent.getIntExtra("bikeId",-1)
        car_delete_ble_or_service_recycler.adapter = carDeketeBleOrServiceAdapter
        val lp = LinearLayoutManager(this)
        lp.orientation = LinearLayoutManager.VERTICAL
        car_delete_ble_or_service_recycler.layoutManager = lp
        baseSetOnClick(title_back_btn)
        baseSetOnClick(car_delete_ble_or_service_must_dele_btn)
        title_tx.text = "车辆删除"
    }

    override fun initData() {
        checkBleIsOpen()
    }
    var i  = 0
    override fun baseResultOnClick(v: View) {
        if(v.id == R.id.title_back_btn){
            finish()
        }else{
            mStatus = true
            val map= hashMapOf<String,Any>()
            map["bike_id"] = TrustAppConfig.bikeId
            getPresenter()?.deleteCar(map)
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BLE_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    setmp1("蓝牙模块未开启", TrustAppConfig.DELETE_CAR_ERROR)
                }
                else -> {
                    setmp1("正在链接蓝牙", TrustAppConfig.DELETE_CAR_STARTING)
                    deletBle()
                }
            }
        }
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        TrustLogUtils.d("error $msg")
        setmp3(msg, TrustAppConfig.DELETE_CAR_ERROR)
    }

    override fun createPresenter(): UserPresenter {
        return UserPresenter()
    }


    private val mBleOrServiceListener = object : CarDeketeBleOrServiceAdapter.BleOrServiceListener{
        override fun bleOrServiceListener(pos: Int) {
            mStatus = false
            setmp3("",TrustAppConfig.DELETE_CAR_STARTING)
            val map= hashMapOf<String,Any>()
            map["bike_id"] = TrustAppConfig.bikeId
            getPresenter()?.deleteCar(map)
        }
    }

    /**
     * 删除车辆第一步
     */
    private fun setmp1(statusTx:String,status:Int){
        carDeketeBleOrServiceAdapter.getStemp(0,statusTx,status)
    }

    /**
     * 删除车辆第二步
     */
    private fun setmp2(statusTx:String,status:Int){
        setmp1("",TrustAppConfig.DELETE_CAR_SUCCESS)
        carDeketeBleOrServiceAdapter.getStemp(1,statusTx,status)
    }

    /**
     * 删除车辆第三步
     */
    private fun setmp3(statusTx:String,status:Int){
        setmp2("",TrustAppConfig.DELETE_CAR_SUCCESS)
        carDeketeBleOrServiceAdapter.getStemp(2,statusTx,status)
    }


    private fun checkBleIsOpen(){
        val intent = Intent(
                BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(intent,BLE_CODE)
    }

    /**
     * 通过ble 连接设备
     */
    private fun deletBle(){
        if (TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE) {//已经连接直接下发删除指令
            unbind()
        }else{//未连接需要先连接
            Thread(Runnable {
                while (true){
                    Thread.sleep(1000)
                    if (TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE) {
                        runOnUiThread {
                            unbind()
                        }
                        break
                    }
                }
            }).start()
        }
    }


    /**
     * ble 跟服务器解绑
     */
    fun unbind(){
        setmp2("",TrustAppConfig.DELETE_CAR_STARTING)

    }


    override fun resultUploadfile(bean: BsUploadFileBean?) {
    }

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            showToast("车辆删除成功")
            if (!mStatus) {
                setmp3("",TrustAppConfig.DELETE_CAR_SUCCESS)
            }
            BsApplication.bsDbManger!!.clearBle(TrustAppConfig.bikeId)
            val carBean = BsDbManger.mDbUserLoginStatusBean?.userBikeList?.bikes
            carBean?.forEach {
                if (it.bike_id == TrustAppConfig.bikeId) {
                    carBean.remove(it)
                }
            }
            BsDbManger.mDbUserLoginStatusBean?.userBikeList?.bikes = carBean
            BsApplication.bsDbManger!!.setUserLoginStatus(BsDbManger.mDbUserLoginStatusBean!!)

            TrustTools<View>().setCountdown(2) {
                activityList.forEach {
                    when (it) {
                        is CarInfoActivity,
                        is CarsActivity,
                        is CarDeleteProtocolActivity,
                        is CarsEditActivity-> {
                            it.finish()
                        }
                    }
                }
                finish()
            }
        }

    }

    override fun resultUserExt(bean: BsUserExtBean?) {
    }

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {
    }

    override fun resultEditPwd(bean: BsSetPwdBean?) {
    }

    override fun resultUserKey(bean: BsGetUserKeyBean?) {
    }

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {
    }

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
    }

}
