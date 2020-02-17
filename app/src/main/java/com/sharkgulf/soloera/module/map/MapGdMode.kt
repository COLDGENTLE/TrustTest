package com.sharkgulf.soloera.module.map

import android.util.Log
import com.amap.api.location.AMapLocationListener
import com.sharkgulf.soloera.appliction.BsApplication.Companion.applicationContext
import com.sharkgulf.soloera.presenter.map.IMapPresenterListener
import com.bs.trust.mapslibrary.TrustGaoDeMapsTool
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.trust.demo.basis.trust.utils.TrustLogUtils

/*
 *Created by Trust on 2018/12/26
 */
class MapGdMode :MapGdModeListener{
    private val mapTool = GdMapTool(applicationContext!!)
    private val gdLocation = TrustGaoDeMapsTool.getGdLocation(applicationContext!!)
    override fun getAddressName(lat: Double, lng: Double, addressListerner: GdMapTool.onAddressListerner) {
        mapTool.getAddress(lat,lng,addressListerner)
    }
    private var isFirst = false


    override fun getUserLocation(mapListener: IMapPresenterListener.MapListener) {
        if (!isFirst) {
            isFirst = true
            gdLocation.onceLocation(AMapLocationListener {
                amapLocation ->
                Log.d("map","启动定位 onceLocation")
                if (amapLocation != null) {
                    if (amapLocation.errorCode == 0) {
                        mapListener.userLocation(amapLocation)
                    }else{
                        TrustLogUtils.e("location Error, ErrCode:"
                                + amapLocation.errorCode + ", errInfo:"
                                + amapLocation.errorInfo)
                        mapListener.errorLocation(amapLocation.errorInfo)
                    }
                }

            })
        }
        gdLocation.startLocation()
    }
}