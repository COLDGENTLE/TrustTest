package com.sharkgulf.soloera.presenter.map

import com.amap.api.location.AMapLocation
import com.bs.trust.mapslibrary.gd.GdMapTool

/*
 *Created by Trust on 2018/12/26
 */
interface IMapPresenterListener {
    fun requestCarInfo(map:HashMap<String,Any>)

    fun requestGdCarAddress(lat:Double,lng:Double,addressListerner:GdMapTool.onAddressListerner)

    fun getUserLocation(mapListener: IMapPresenterListener.MapListener)
    fun changeSecurity(map:HashMap<String,Any>)
    /**
     * 寻车
     */
    fun requestFindCar(actionType:Int,hashMap: HashMap<String,Any>? = null)



    interface MapListener{
        fun userLocation(aMapLocation: AMapLocation)
        fun errorLocation(msg:String)
    }
}