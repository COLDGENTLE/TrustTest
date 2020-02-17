package com.bs.trust.mapslibrary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.trust.maplibrary.bean.LocationBean
import com.trust.maplibrary.callback.MapDialogCallBack
import com.trust.maplibrary.callback.MapUtilsCallBack
import java.io.File

/**
 * Created by Trust on 2018/5/23.
 *应用内部 调用外部 高德百度地图导航
 */
class MapUtils (context: Context,mapUtilsCallBack : MapUtilsCallBack){
    private var mContext:Context = context
    private var mapUtilsCallBack : MapUtilsCallBack = mapUtilsCallBack
    private var locationBean: LocationBean?=null
    private var mapPackageName:String ?= null
    private var mapDialog:MapDialog? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mapUtils : MapUtils? = null
         const val BAI_DU_PACKAGE_NAME:String = "com.baidu.BaiduMap"
         const val GAO_DE_PACKAGE_NAME:String = "com.autonavi.minimap"
         const val ELEBIKE_NAVI :Int = 0 //电动车骑行导航
         const val BIKE_NAVI :Int = 1 //自行车车骑行导航
         const val CAR_NAVI:Int = 2 //行车导航
        fun getSingleton(context: Context,mapUtilsCallBack : MapUtilsCallBack): MapUtils {
            if (mapUtils == null) {
                mapUtils = MapUtils(context, mapUtilsCallBack)
            }
            return mapUtils!!
        }

    }

    fun showMapDialog(manager: FragmentManager, locationBean: LocationBean ):MapDialog{
         mapDialog = MapDialog(object : MapDialogCallBack {
            override fun dialogCallBack(type: String) {
                getSingleton(mContext, mapUtilsCallBack).startMapUtils(type, locationBean)
            }
        })
        mapDialog!!.show(manager,"dialog")
        return mapDialog as MapDialog
    }


    private fun startMapUtils(mapType:String,locationBean: LocationBean){
        mapPackageName = mapType
        this.locationBean = locationBean
//        mapUtilsCallBack!!.isInstallByread(mapStatus)

        if (locationBean.mLat!=0.0) {

        }

        when(mapPackageName){
            BAI_DU_PACKAGE_NAME ->{startBaiDuMap()}
            GAO_DE_PACKAGE_NAME ->{startGaoDeMap()}
        }
    }

    /**
     * 检查有没有安装地图
     */
    @SuppressLint("SdCardPath")
    private fun isInstallByread(packageName:String):Boolean {
        return File("/data/data/$packageName").exists()
    }

    /**
     * 开始使用高德地图
     */
    private fun startGaoDeMap(){
        if (isInstallByread(mapPackageName!!)) {
            val uriParse:String = when (locationBean?.mType) {
                ELEBIKE_NAVI-> {
                    "amapuri://openFeature?featureName=OnRideNavi&rideType=elebike&sourceApplication=test&lat=${locationBean?.mLat}&lon=${locationBean?.mLon}&dev=0"
                }
                BIKE_NAVI->{
                    "amapuri://openFeature?featureName=OnRideNavi&rideType=bike&sourceApplication=test&lat=${locationBean?.mLat}&lon=${locationBean?.mLon}&dev=0"
                }
                CAR_NAVI->{
                    "androidamap://navi?sourceApplication=test&lat=" + locationBean?.mLat + "&lon=" + locationBean?.mLon + "&dev=0&style=2"
                }
                else -> {
                    ""
                }

            }
            val intent = Intent("android.intent.action.VIEW", Uri.parse(uriParse))

//            val intent = Intent("android.intent.action.VIEW",
//                    android.net.Uri.parse("androidamap://navi?sourceApplication=test&lat=" + locationBean?.mLat + "&lon=" + locationBean?.mLon + "&dev=0&style=2"))
//            val intent = Intent("android.intent.action.VIEW", Uri.parse("amapuri://openFeature?featureName=OnRideNavi&rideType=elebike&sourceApplication=test&lat=${locationBean?.mLat}&lon=${locationBean?.mLon}&dev=0"))
            intent.`package` = "com.autonavi.minimap"
            mContext.startActivity(intent)
            mapUtilsCallBack.isUseSuccess(true,"正在拉起地图请稍后...")
        }else{
            mapUtilsCallBack.isUseSuccess(false,"没有安装高德地图")
        }
    }

    /**
     * 开始使用百度地图
     */
    private fun startBaiDuMap(){
        if (locationBean?.mUserLat != null) {
        if (isInstallByread(mapPackageName!!)) {
            var coordType:String  = "gcj02"
            if (locationBean?.mLocationType != null) {
                coordType = locationBean?.mLocationType!!
            }
            val uriParse:String = when (locationBean?.mType) {
                ELEBIKE_NAVI -> {
                    "baidumap://map/bikenavi?origin=${locationBean?.mUserLat},${locationBean?.mUserLong}&destination=${locationBean?.mLat},${locationBean?.mLon}&coord_type=$coordType&src=andr.baidu.openAPIdemo"
                }
                BIKE_NAVI->{
                    "baidumap://map/bikenavi?origin=${locationBean?.mUserLat},${locationBean?.mUserLong}&destination=${locationBean?.mLat},${locationBean?.mLon}&coord_type=$coordType&src=andr.baidu.openAPIdemo"
                }
                CAR_NAVI->{
                    "baidumap://map/navi?location=${locationBean?.mLat},${locationBean?.mLon}&coord_type=$coordType&src=andr.baidu.openAPIdemo"
                }
                else -> {
                    ""
                }
            }
            val intent = Intent("android.intent.action.VIEW",Uri.parse(uriParse))
            mContext.startActivity(intent)
            mapUtilsCallBack.isUseSuccess(true,"正在拉起地图请稍后...")
        }else{
            mapUtilsCallBack.isUseSuccess(false,"没有安装百度地图")
        }
        }else{
            mapUtilsCallBack.isUseSuccess(false,"百度地图需要获取用户位置,请先获取用户位置")
        }
    }

}