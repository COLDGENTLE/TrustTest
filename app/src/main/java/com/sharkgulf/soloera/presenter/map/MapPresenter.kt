package com.sharkgulf.soloera.presenter.map

import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.sharkgulf.soloera.module.map.MapGdMode
import com.sharkgulf.soloera.module.map.MapGdModeListener
import com.sharkgulf.soloera.module.map.MapHttpMode
import com.sharkgulf.soloera.module.map.MapModelListener
import com.sharkgulf.soloera.mvpview.map.IMapView
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.bikeId
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.controllcar.ControllCarBleMode
import com.sharkgulf.soloera.module.controllcar.ControllCarInternetMode
import com.sharkgulf.soloera.module.controllcar.ControllCarModeListener
import com.sharkgulf.soloera.module.securitysettings.ISecuritySettingsMoudle
import com.sharkgulf.soloera.module.securitysettings.ISecuritySettingsMoudleListener
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2018/12/26
 */
class MapPresenter:TrustPresenters<IMapView>(),IMapPresenterListener {


    private var mapHttpMode:MapModelListener? = null
    private var mapGdMode:MapGdModeListener? = null

    private var controllBle: ControllCarModeListener? = null
    private var controllInternet: ControllCarModeListener? = null

    private var httpMode: ISecuritySettingsMoudleListener? = null
    private var TAG = ""
    init {
        mapHttpMode = MapHttpMode()
        mapGdMode = MapGdMode()
        httpMode = ISecuritySettingsMoudle()
        controllBle = ControllCarBleMode()
        controllInternet = ControllCarInternetMode()
    }

    fun setTAG(tag:String){
        TAG = tag
        registerBikeInfo()
        registerUpdateBikeInfo()
        registerLoction()
    }

    override fun changeSecurity(map: HashMap<String, Any>) {
        view.showWaitDialog(null,true,null)
        httpMode?.changeSecurity(map,object :ModuleResultInterface<BsSecuritySettingsBean>{
            override fun resultData(bean: BsSecuritySettingsBean?,pos:Int?) {
                view.diassDialog()
                view.resultChangeSecurity(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    private fun registerLoction(){
        regusterLocation(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
              view.resultLcoation(getBikeLocation(bikeId))
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        })
    }


    private fun registerBikeInfo(){
       registerBikeInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getCarInfo()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }

fun getCarInfo() {
        val carInfoData = getCarInfoData(bikeId)
        val str = if (carInfoData != null) {
            val mode = carInfoData.security.mode
            if (mode == SECUTITY_ALERT) {
                getBsString(R.string.alert_is_open_tx)
            } else if (mode == SECUTITY_DO_NOT_DISTURB) {
                getBsString(R.string.no_alert_is_open_tx)
            } else {
                getBsString(R.string.alert_is_open_tx)
            }
        } else {
            getBsString(R.string.alert_is_open_tx)
        }
    TrustLogUtils.d(TAG,"map:carinfo:$bikeId")
        TrustAppConfig.initSwMap(carInfoData?.security)
        view.resultCarModule(str,carInfoData?.security?.mode)
    }

    fun sendCarInfo(){
        sendBikeInfo()
    }

    override fun requestCarInfo(map:HashMap<String,Any>) {
        mapHttpMode!!.requestCarInfo(map,object :ModuleResultInterface<BsGetCarLocationBean>{
            override fun resultData(bean: BsGetCarLocationBean?,pos:Int?) {
                view.resultCarInfo(bean)
            }
            override fun resultError(msg: String) {
                view.resultError(msg)
            }
        })
    }


    override fun requestGdCarAddress(lat: Double, lng: Double, addressListerner: GdMapTool.onAddressListerner){
        mapGdMode!!.getAddressName(lat,lng,addressListerner)
    }


    override fun getUserLocation(mapListener: IMapPresenterListener.MapListener) {
        mapGdMode!!.getUserLocation(mapListener)
    }


    override fun requestFindCar(actionType: Int, hashMap: HashMap<String, Any>?) {
        val resultListener = object : ModuleResultInterface<Int> {
            override fun resultData(bean: Int?,pos:Int?) {
                view.diassDialog()
                view.resultFindCar(actionType,bean,checkAction(bean),checkIsSuccess(bean))
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultFindCar(actionType, TrustAppConfig.DEFUTE)
            }

        }
        if (TrustAppConfig.CONTROLL_STATUS == TrustAppConfig.CONTROLL_CAR_BLE) {//蓝牙
            controllBle!!.requestFindCar(actionType,initMap(),resultListener)
        }else{//网络
            controllInternet!!.requestFindCar(actionType,initMap(),object :ModuleResultInterface<Int>{
                override fun resultData(bean: Int?,pos:Int?) {
                    view.diassDialog()
                    view.resultFindCar(actionType,bean,checkAction(bean),checkIsSuccess(bean))
                }

                override fun resultError(msg: String) {
                    view.diassDialog()
                    view.resultFindCar(actionType, TrustAppConfig.DEFUTE)
                }
            })
        }
    }

    private fun initMap():HashMap<String,Any>{
        return  hashMapOf<String,Any>(Pair<String,Any>("bike_id",bikeId))
    }

    private fun checkIsSuccess(action:Int?):Boolean{
        return action == TrustAppConfig.ACTION_SUCCESS
    }

    private fun checkAction(action:Int?):String?{
        return when (action) {
            TrustAppConfig.ACTION_SUCCESS -> {
                getString(R.string.controll_car_find_car_success_tx)
            }
            TrustAppConfig.ACTION_TIME_OUT -> {
                getString(R.string.controll_car_find_car_time_out_tx)
            }
            TrustAppConfig.ACTION_ERROR -> {
                getString(R.string.controll_car_find_car_error_tx)
            }
            TrustAppConfig.INTERNET_ERROR -> {
                getString(R.string.http_error_tx)
            }
            TrustAppConfig.WEBSOCKET_ERROR -> {
                getString(R.string.controll_car_find_car_time_out_tx)
            }
            else -> {
                "错误码 $action"
            }
        }
    }

    private fun registerUpdateBikeInfo(){
        registerUpdateCarInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                sendCarInfo()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }


}
