package com.sharkgulf.soloera.module.hirstory

import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/**
 *  Created by user on 2019/4/18
 */
class HirstoryModule :HttpModel() ,HirstoryModuleListener{

    override fun requestTimeLevel(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsTimeLevelBean>) {
        sendRrequest(URL_GET_TIME_LEVEL,map,BsTimeLevelBean::class.java,resultInterface)
    }

    override fun requestRideSummary(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideSummaryBean>) {
        sendRrequest(URL_GET_HIRSTORY,map,BsRideSummaryBean::class.java,resultInterface)
    }

    override fun requestRideReport(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideReportBean>) {
        sendRrequest(URL_GET_RIDEREPORT,map,BsRideReportBean::class.java,resultInterface)
    }

    override fun requestRideTrack(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideTrackBean>) {
        sendRrequest(URL_GET_RIDETRACK,map,BsRideTrackBean::class.java,resultInterface)
    }

}