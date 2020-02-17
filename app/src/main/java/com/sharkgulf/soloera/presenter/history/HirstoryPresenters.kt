package com.sharkgulf.soloera.presenter.history

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.sharkgulf.soloera.TrustAppConfig.BATTERY_ONE
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.hirstory.HirstoryModule
import com.sharkgulf.soloera.module.hirstory.HirstoryModuleListener
import com.sharkgulf.soloera.module.hirstory.MapModule
import com.sharkgulf.soloera.module.hirstory.MapModuleListener
import com.sharkgulf.soloera.mvpview.history.HirstoryView
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackLineChart
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils

/**
 *  Created by user on 2019/4/18
 */
class HirstoryPresenters :TrustPresenters<HirstoryView>() ,HirstoryPresentersListener{
    override fun coordinateTransformation(lat: Double, lng: Double): LatLng {
       return  mMapModule?.coordinateTransformation(lat,lng)!!
    }


    private var TAG= "HirstoryPresenters"
    private var mHirstoryModuleListener: HirstoryModuleListener? = null
    private var mMapModule: MapModuleListener? = null
    init {
        mHirstoryModuleListener = HirstoryModule()
        mMapModule = MapModule()
    }

    fun setTAG(tag:String){
        TAG = tag
        registerHirstoryData()
        registerBattryStatus()
        registerCarInfo()
    }

    private fun registerHirstoryData(){
        sendHirstort()
        registerHirstoryInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {

            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }

    private fun registerCarInfo(){
        registerBikeInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getHirstoryData()
            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }

    private fun registerBattryStatus(){
        val listener = object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                val battryInfoData = getBattryInfoData()
                var battry1: BattInfoBean.BodyBean.BattBean? = null
                var battry2: BattInfoBean.BodyBean.BattBean? = null

                battryInfoData?.batt?.forEach {
                    if (it.info.position == BATTERY_ONE && battry1 == null) {
                        battry1 = it
                    }else{
                        battry2 = it
                    }
                }

                view.resultBattUseNum(battry1,battry2)
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }

        registerBattryInfo(listener,TAG)
    }


    override fun requestTimeLevel(map: HashMap<String, Any>) {
        TrustLogUtils.d(TAG,"requestTimeLevel ")
        mHirstoryModuleListener?.requestTimeLevel(map,object :ModuleResultInterface<BsTimeLevelBean>{
            override fun resultData(bean: BsTimeLevelBean?, pos:Int?) {
                view?.diassDialog()
                view?.resultTimeLevel(bean)
            }

            override fun resultError(msg: String) {
                TrustLogUtils.d(TAG,"requestTimeLevel : $msg")
                resultError(msg)
            }

        })
    }

    override fun requestRideSummary(map: HashMap<String, Any>) {
        mHirstoryModuleListener?.requestRideSummary(map,object :ModuleResultInterface<BsRideSummaryBean>{
            override fun resultData(bean: BsRideSummaryBean?, pos:Int?) {
                view?.diassDialog()
                view?.resultRideSummary(bean)
            }

            override fun resultError(msg: String) {
                TrustLogUtils.d(TAG,"msg:$msg")
//                resultError(msg)
            }

        })
    }

    override fun requestRideReport(map: HashMap<String, Any>) {
        mHirstoryModuleListener?.requestRideReport(map,object :ModuleResultInterface<BsRideReportBean>{
            override fun resultData(bean: BsRideReportBean?, pos:Int?) {
                view?.diassDialog()
                view?.resultRideReport(bean)
            }

            override fun resultError(msg: String) {
                resultError(msg)
            }

        })
    }

    override fun requestRideTrack(map: HashMap<String, Any>) {
        mHirstoryModuleListener?.requestRideTrack(map,object :ModuleResultInterface<BsRideTrackBean>{
            override fun resultData(bean: BsRideTrackBean?, pos:Int?) {
                view?.diassDialog()
                view?.resultRideTrack(bean)
            }

            override fun resultError(msg: String) {
                TrustLogUtils.d(TAG,"msg: $msg")
                resultError(msg)
            }

        })
    }



    override fun drawTrajectory(aMap: AMap, ic: Int, bean: BsRideTrackBean,isDottedLine:Boolean ) {
        mMapModule?.drawTrajectory(aMap,ic,bean,object :ModuleResultInterface<Boolean>{
            override fun resultData(bean: Boolean?, pos:Int?) {view?.resultDrawTrajectory(bean!!) }

            override fun resultError(msg: String) {   TrustLogUtils.d(TAG,"msg: $msg")
                resultError(msg) }
        },isDottedLine)
    }

    fun resultError(msg: String) {
        view?.diassDialog()
        view?.resultError(msg)
    }

    override fun controlPoint(status: Boolean) {
        mMapModule?.controlPoint(status,object :ModuleResultInterface<Boolean>{
            override fun resultData(bean: Boolean?, pos:Int?) {
                view?.resultMoveListener(bean!!,pos)
            }

            override fun resultError(msg: String) {
                resultError(msg)
            }

        })
    }

    override fun controlSpeed(speedLevel: Double) {
        mMapModule?.controlSpeed(speedLevel)
    }

    override fun getAddressList(addresslist: ArrayList<LatLng>) {
        mMapModule?.locationAddress(addresslist,object :ModuleResultInterface<ArrayList<String>>{
            override fun resultData(bean: ArrayList<String>?, pos:Int?){
            view.diassDialog()
                view.resultAddressList(bean)
            }
            override fun resultError(msg: String) {}
        })
    }

    override fun getTrackLineData(list: List<BsRideTrackBean.DataBean.LocsBean>) {
        val data = arrayListOf<TrackLineChart.Data>()


        list.forEach {
            val toDouble = getSpeed(it.speed)
            val item = TrackLineChart.Data(toDouble.toFloat())
            item.setSelectXDesc(TrustTools.getTimes(it.ts * 1000L))
            item.setSelectYDesc("${toDouble}km/h")
            data.add(item)
        }
        return  view.resultTrackLineData(data)

    }


    fun getHirstoryData(){
    val carInfoData = getCarInfoData()

    view.resultCarinfo(carInfoData?.total_miles ?: 0,carInfoData?.bind_days ?: 0,carInfoData?.max_ride_miles ?: 0)
    }


    override fun linkageMaker(aMap: AMap, ic: Int,pos:Int) {
        mMapModule?.linkageMaker(aMap,ic,pos,object :ModuleResultInterface<Boolean>{
            override fun resultError(msg: String) {}
            override fun resultData(bean: Boolean?, pos:Int?){}
        })
    }

    fun dataCombing(){

    }
}








