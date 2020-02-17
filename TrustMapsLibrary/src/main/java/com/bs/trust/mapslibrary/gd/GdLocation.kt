package com.bs.trust.mapslibrary.gd

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/*
 *Created by Trust on 2018/12/26
 */
 class GdLocation(context: Context){
    private var mLocationClient:AMapLocationClient? = null
    private var mLocationListener:AMapLocationListener? = null
    private var mLocationOption :AMapLocationClientOption? = null

    init {
        mLocationClient = AMapLocationClient(context.applicationContext)
        mLocationOption = AMapLocationClientOption()
        mLocationOption!!.isOnceLocation = true
        mLocationOption!!.isOnceLocationLatest = true
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption!!.interval = 2000
    }


    fun locationType(type:AMapLocationClientOption.AMapLocationMode
                     = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy):GdLocation{
        mLocationOption!!.locationMode = type
        return this
    }


    fun onceLocation(locationListener:AMapLocationListener):GdLocation{
        Log.d("map","启动定位 onceLocation")
        mLocationListener = locationListener
        mLocationClient!!.setLocationListener(mLocationListener)
        return this
    }


    fun startLocation(){
         mLocationClient!!.setLocationOption(mLocationOption)
         mLocationClient!!.startLocation()
        Log.d("map","启动定位 startLocation")
    }

    fun stopLocation(){
        mLocationClient!!.stopLocation()
    }
}