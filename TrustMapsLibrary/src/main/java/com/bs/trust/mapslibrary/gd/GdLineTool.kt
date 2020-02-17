package com.bs.trust.mapslibrary.gd

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.utils.SpatialRelationUtil
import com.amap.api.maps.utils.overlay.MovingPointOverlay


/**
 *  Created by user on 2019/7/10
 */
class GdLineTool {
    companion object {
        private var mGdLineTool:GdLineTool? = null
        fun getGdLineTools():GdLineTool{
            if (mGdLineTool == null) {
                mGdLineTool = GdLineTool()
            }
            return mGdLineTool!!
        }
    }
    private var mGdMapTool:GdMapTool? = null

    init {
        mGdMapTool = GdMapTool.getInstance()
    }

    fun drawLines(map: AMap, lines: ArrayList<LatLng>,isCoordinateTransformation: Boolean = false,isDottedLine:Boolean ){

        val newbounds =  LatLngBounds.Builder();
        var mlist = arrayListOf<LatLng>()
        if (isCoordinateTransformation) {
            lines.forEach {
                val coordinateTransformation = mGdMapTool!!.coordinateTransformation(it.latitude, it.longitude)
                newbounds.include(coordinateTransformation);//通过for循环将所有的轨迹点添加进去.
                mlist.add(coordinateTransformation)
            }
        }else{
            lines.forEach {
                newbounds.include(it);//通过for循环将所有的轨迹点添加进去.
                mlist.add(it)
            }
            mlist = lines
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                300));//第二个参数为四周留空宽度.

        val polylineOptions = PolylineOptions()
        polylineOptions.isDottedLine = isDottedLine
//        map.addPolyline(polylineOptions.
//                addAll(mlist).width(10f).colorValues(listOf(Color
//                .parseColor("#00c0ff"),Color.parseColor("#226eee"))))
        map.addPolyline(polylineOptions.
                addAll(mlist).width(10f).useGradient(true).color(Color.parseColor("#00c0ff")))
    }

    private val mCoordinates:ArrayList<LatLng> = arrayListOf()
    fun addCoordinate(lat:Double ,lon:Double,isCoordinateTransformation:Boolean = false):LatLng{

        val newbounds =  LatLngBounds.Builder();

        return if (isCoordinateTransformation) {
            val coordinateTransformation = mGdMapTool!!.coordinateTransformation(lat, lon)
            newbounds.include(coordinateTransformation);//通过for循环将所有的轨迹点添加进去.
            mCoordinates.add(coordinateTransformation)
            coordinateTransformation
        }else{
            val latLng = LatLng(lat, lon)
            mCoordinates.add(latLng)
            latLng
        }

    }

    fun clearCoordinate(){
        mCoordinates.clear()
    }

    fun getCoordinates():ArrayList<LatLng>{
        return mCoordinates
    }

    fun draw(map: AMap,isDottedLine:Boolean){
        drawLines(map,mCoordinates,isDottedLine = isDottedLine)
    }




    private var listSize = 0
    fun getListSize():Int{
        return listSize
    }

    //获取点平滑移动控制类 并划线
    fun getMovingPointOverlay(map: AMap,makerIc:Int,makerBit:Bitmap? = null,pos:Int = 0):MovingPointOverlay?{
        if (mCoordinates.isNotEmpty()) {
            var startLatLng = if(pos <mCoordinates.size){
                mCoordinates[pos]
            }else{
                mCoordinates[pos-1]
            }

            //mGdMapTool?.showCarAndUser(map,startLatLng.latitude,startLatLng.longitude,endLatLng.latitude,endLatLng.longitude,false)
            val marker = mGdMapTool?.setMarker(map,startLatLng.latitude
                    ,startLatLng.longitude,false,false,
                    false,false,makerIc,19f,false,0f,
                    makerBit)

            val list = mCoordinates.clone() as ArrayList<LatLng>
            if (pos !=0) { list.subList(0,pos).clear() }
            Log.d("lhhh","选择的 pos :$pos  startLatLng: ${startLatLng.latitude} | ${startLatLng.longitude} ")
            listSize = list.size
            val movingPointOverlay = MovingPointOverlay(map,marker)
            movingPointOverlay.setPoints(list)
            return movingPointOverlay
        }else{
            return null
        }
    }


    private fun addLatLng(aMap:AMap,latLng:LatLng){

    }

}