package com.sharkgulf.soloera.presenter.history

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.sharkgulf.soloera.module.bean.BsRideTrackBean

/**
 *  Created by user on 2019/4/18
 */
interface HirstoryPresentersListener {
    fun requestTimeLevel(map: HashMap<String, Any>)
    fun requestRideSummary(map: HashMap<String, Any>)
    fun requestRideReport(map: HashMap<String, Any>)
    fun requestRideTrack(map: HashMap<String, Any>)

    //划轨迹并初始化移动控制器
    fun drawTrajectory(aMap: AMap, ic:Int, bean: BsRideTrackBean,isDottedLine:Boolean )

    //控制暂停、开始
    fun controlPoint(status:Boolean)

    //控制速度
    fun controlSpeed(speedLevel:Double)

    //地理编码
    fun getAddressList(addresslist:ArrayList<LatLng>)

    fun getTrackLineData(list:List<BsRideTrackBean.DataBean.LocsBean>)
    fun linkageMaker(aMap: AMap, ic: Int, pos:Int)
    fun coordinateTransformation(lat:Double,lng:Double):LatLng
}