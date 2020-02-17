package com.bs.trust.mapslibrary.gd

import android.content.Context
import android.graphics.Bitmap
import com.amap.api.maps.CoordinateConverter
import android.graphics.BitmapFactory
import android.graphics.Color
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.bs.trust.mapslibrary.R
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.model.LatLng






/*
 *Created by Trust on 2018/12/24
 */
class GdMapTool (context:Context){
    companion object {
        private var mGdMapTool:GdMapTool? = null
        fun getInstance():GdMapTool{
            return mGdMapTool!!
        }

        fun initGdMapTool(context:Context):Boolean{
            if (mGdMapTool == null) {
                mGdMapTool = GdMapTool(context)
            }
            return mGdMapTool != null
        }
    }


    private  val mContext:Context = context.applicationContext
    private  val converter = CoordinateConverter(mContext)
    private  val geocodeSearch = GeocodeSearch(mContext)
    private  var GPS_TYPE = CoordinateConverter.CoordType.GPS
    private  var mAddressListerner: onAddressListerner? =  null

    fun coordinateTransformation(lat:Double,long:Double,
                                 gpsType: CoordinateConverter.CoordType =
                                         CoordinateConverter.CoordType.GPS):LatLng{
        GPS_TYPE = gpsType
        converter.from(GPS_TYPE)
        converter.coord(LatLng(lat,long))
        return converter.convert()
    }


    fun setMarker(map: AMap, lat:Double, long:Double, isClearAmap:Boolean = true,
                  isCoordinateTransformation:Boolean = true
                  ,isMoveCenter:Boolean = true
                  ,isSetCircle:Boolean = true
                  ,makerIc:Int =  R.drawable.ic_launcher_round,
                  zoom:Float = 19f,isMoveAnimate :Boolean = true
                  ,tilt:Float = 0f,makerBit:Bitmap? = null):Marker{
        if (isClearAmap) {
            map.clear()
        }
        val markerOption = MarkerOptions()


        val latLng =  if (isCoordinateTransformation) {
            coordinateTransformation(lat, long)
        }else{
            LatLng(lat, long)
        }
        markerOption.position(latLng)
        markerOption.draggable(true)//设置Marker可拖动
        val bit = makerBit ?: BitmapFactory
                .decodeResource(mContext.resources,makerIc)
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(bit))

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = false//设置marker平贴地图效果
//        changeMakerTilt(map,latLng.latitude,latLng.longitude,tilt)
        val marker = map.addMarker(markerOption)
        if (isMoveCenter) {
            showAre(map,latLng.latitude,latLng.longitude)
//            moveMapCenter(map,latLng.latitude,latLng.longitude,zoom,isMoveAnimate,tilt)
        }
        if(isSetCircle){
            setMarkerCircle(map,latLng.latitude,latLng.longitude)
        }

        return marker
    }


    fun setMarkerCircle(map: AMap, lat:Double, lon:Double,radius:Double = 40.0,color:Int = Color.argb(50, 1, 1, 1),strokeWidth:Float = 1F):Circle{
       return map.addCircle(CircleOptions().center(LatLng(lat,lon))
                .radius(radius)
                .fillColor(color).strokeWidth(strokeWidth))
    }


    fun moveMapCenter(map: AMap,lat: Double,long: Double,zoom:Float = 19F,isMoveAnimate:Boolean,tilt:Float ,isCoordinateTransformation: Boolean = false):Boolean{
        return if (lat!=0.0 && long != 0.0) {
            val latLng = if (isCoordinateTransformation) {
                coordinateTransformation(lat,long)
            }else{
                LatLng(lat,long)
            }
            if (isMoveAnimate) {
                showAre(map,latLng.latitude,latLng.longitude)
//                map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
//                        LatLng(latLng.latitude,latLng.longitude) , zoom, tilt,0f)), 1000, null)
            }else{
//                map.moveCamera(  CameraUpdateFactory.newCameraPosition(CameraPosition(
//                        LatLng(latLng.latitude,latLng.longitude) , zoom, 0f,0f)))
            }
            true
        }else{
            false
        }



    }

    fun changeMakerTilt(map: AMap,lat: Double,long: Double,tilt:Float){
        map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(
                LatLng(lat,long) , 19F, tilt,0f)), 1000, null)
    }

    fun getAddress (lat:Double,lon:Double,addressListerner: onAddressListerner){
        geocodeSearch.setOnGeocodeSearchListener(onGeocodeSearchListener)
        mAddressListerner = addressListerner
        val latLng = coordinateTransformation(lat, lon)
        val query = RegeocodeQuery(LatLonPoint(latLng.latitude,latLng.longitude), 200f,
                GeocodeSearch.AMAP)
        geocodeSearch.getFromLocationAsyn(query)
    }


    interface onAddressListerner{ fun addressListerner(addressName:String)
    fun errorListerner(result: RegeocodeResult?, rCode: Int)}


    private val onGeocodeSearchListener =  object : GeocodeSearch.OnGeocodeSearchListener{
        override fun onRegeocodeSearched(result: RegeocodeResult?, rCode: Int){

            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result?.regeocodeAddress != null
                        && result.regeocodeAddress.formatAddress != null) {
                    if (mAddressListerner != null) {
                        mAddressListerner!!.addressListerner(result.regeocodeAddress.formatAddress + "附近")
                    }
                }
            }else{
                if (mAddressListerner != null) {
                    mAddressListerner!!.errorListerner(result,rCode)
                }
            }

        }

        override fun onGeocodeSearched(geocodeResult: GeocodeResult?, p1: Int) {

        }
    }


    /**
     * 根据2个坐标返回一个矩形Bounds
     * 以此来智能缩放地图显示
     */
    fun createBounds(latA: Double, lngA: Double, latB: Double, lngB: Double): LatLngBounds {
        val northeastLatLng: LatLng
        val southwestLatLng: LatLng

        val topLat: Double?
        val topLng: Double?
        val bottomLat: Double?
        val bottomLng: Double?
        if (latA >= latB) {
            topLat = latA
            bottomLat = latB
        } else {
            topLat = latB
            bottomLat = latA
        }
        if (lngA >= lngB) {
            topLng = lngA
            bottomLng = lngB
        } else {
            topLng = lngB
            bottomLng = lngA
        }
        northeastLatLng = LatLng(topLat!!, topLng!!)
        southwestLatLng = LatLng(bottomLat!!, bottomLng!!)
        return LatLngBounds(southwestLatLng, northeastLatLng)
    }


    /**
     * 动态缩放 车辆和用户 地图缩放等级
     */
    fun showCarAndUser(map: AMap,carLat:Double,carLon:Double,userLat:Double,userLon:Double , isClear:Boolean = true , isMove:Boolean = true ,mtilt:Float = 0f,isCoordinateTransformation: Boolean = false){
        if (carLat != 0.0 && carLon != 0.0 && userLat != 0.0 && userLon != 0.0) {
            val car = if (isCoordinateTransformation) {
                coordinateTransformation(carLat,carLon)
            }else{
                LatLng(carLat,carLon)
            }
        if (isClear) {
            map.clear()
        }
        if (isMove) {

        map.animateCamera(CameraUpdateFactory.
                newLatLngBounds(createBounds(car.latitude,car.longitude,userLat,userLon), 300),500L,null)
        }
            map.moveCamera(CameraUpdateFactory.changeTilt(mtilt))

        }
    }


    fun showAre(map:AMap,lat: Double,long: Double){
        val newbounds =  LatLngBounds.Builder();
        newbounds.include(LatLng(lat,long));//通过for循环将所有的轨迹点添加进去.

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                600));//第二个参数为四周留空宽度.
    }


    fun showAre(map:AMap,arrayList: ArrayList<LatLng>){
        val newbounds =  LatLngBounds.Builder();
        arrayList.forEach {
            newbounds.include(it);//通过for循环将所有的轨迹点添加进去.
        }

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                300));//第二个参数为四周留空宽度.
    }
}