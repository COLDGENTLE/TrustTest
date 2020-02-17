package com.bs.trust.mapslibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.amap.api.maps.AMap
import com.bs.trust.mapslibrary.gd.GdLocation


/*
 *Created by Trust on 2018/12/18
 */
class TrustGaoDeMapsTool {

    companion object {
        //离线地图
        @SuppressLint("StaticFieldLeak")
        private var  offlineMapTool:OfflineMapTool? = null

        fun getOfflineMapTool(context: Context,aMap: AMap? = null):OfflineMapTool{

                offlineMapTool = OfflineMapTool(context.applicationContext,aMap)

            return offlineMapTool!!
        }


        fun startOfflineActivity(activity: Activity){
            //在Activity页面调用startActvity启动离线地图组件
            activity.startActivity(Intent(activity.applicationContext,
                    com.amap.api.maps.offlinemap.OfflineMapActivity::class.java))
        }
        fun changeMapType(map: AMap, type:Int){
            map.mapType = type// 设置卫星地图模式，aMap是地图控制器对象。
        }

        private var gdLocation: GdLocation? = null
        fun getGdLocation(context: Context):GdLocation{
            if (gdLocation == null) {
                gdLocation = GdLocation(context.applicationContext)
            }
            return gdLocation!!
        }

    }
}