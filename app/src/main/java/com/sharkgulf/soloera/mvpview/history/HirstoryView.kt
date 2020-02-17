package com.sharkgulf.soloera.mvpview.history

import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackLineChart
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/4/18
 */
interface HirstoryView:TrustView {
    fun resultTimeLevel(bean: BsTimeLevelBean?)
    fun resultRideSummary(bean: BsRideSummaryBean?)
    fun resultRideReport(bean: BsRideReportBean?)
    fun resultRideTrack(bean: BsRideTrackBean?)

    fun resultDrawTrajectory(status:Boolean)
    fun resultMoveListener(isEnd:Boolean,pos:Int?)

    fun resultAddressList(addressList:ArrayList<String>?)
    fun resultBattUseNum(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?)
    fun resultCarinfo(totalMiles:Int,bindDays:Int,maxMils:Int)
    fun resultTrackLineData(list:List< TrackLineChart.Data>)
}