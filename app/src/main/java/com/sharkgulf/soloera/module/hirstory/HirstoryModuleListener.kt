package com.sharkgulf.soloera.module.hirstory

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/**
 *  Created by user on 2019/4/18
 */
interface HirstoryModuleListener {
    fun requestTimeLevel(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsTimeLevelBean>)
    fun requestRideSummary(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideSummaryBean>)
    fun requestRideReport(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideReportBean>)
    fun requestRideTrack(map : HashMap<String, Any>, resultInterface: ModuleResultInterface<BsRideTrackBean>)


}