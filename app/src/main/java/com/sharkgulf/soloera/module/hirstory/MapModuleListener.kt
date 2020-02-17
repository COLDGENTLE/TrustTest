package com.sharkgulf.soloera.module.hirstory

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/7/30
 */
interface MapModuleListener {
    //划轨迹并初始化移动控制器
    fun drawTrajectory(aMap: AMap, ic:Int, bean: BsRideTrackBean, resultInterface: ModuleResultInterface<Boolean>,isDottedLine:Boolean )

    //控制暂停、开始
    fun controlPoint(status:Boolean,resultInterface: ModuleResultInterface<Boolean>)

    //控制速度等级
    fun controlSpeed(speedLevel:Double)

    //获取精度坐标
    fun locationAddress(arrayList: ArrayList<LatLng>,resultInterface: ModuleResultInterface<ArrayList<String>>)

    //联动地图maker跟 折线图
    fun linkageMaker(aMap: AMap, ic:Int,pos:Int,resultInterface: ModuleResultInterface<Boolean>)

    fun coordinateTransformation(lat:Double,lng:Double):LatLng
}