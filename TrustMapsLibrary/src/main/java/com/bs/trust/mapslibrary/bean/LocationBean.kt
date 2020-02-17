package com.trust.maplibrary.bean

/**
 * Created by Trust on 2018/5/24.
 */
class LocationBean (lat: Double, lon: Double ,type:Int,userLat:Double? = 0.0,userLong:Double? = 0.0,locationType:String? = null){
    var mLat = lat
    var mLon = lon
    var mUserLat = userLat
    var mUserLong = userLong
    var mType = type
    var mLocationType =locationType //坐标类型
}