package com.bs.trust.mapslibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.fragment.app.FragmentManager
import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.maps.MapsInitializer

import com.amap.api.maps.offlinemap.OfflineMapActivity
import com.amap.api.maps.offlinemap.OfflineMapManager
import com.amap.api.maps.offlinemap.OfflineMapStatus
import com.trust.maplibrary.callback.MapDialogCallBack
import java.text.BreakIterator

/*
 *Created by Trust on 2018/12/18
 */
class OfflineMapTool(context: Context,map: AMap?) {
    private val mContext:Context = context
    private val TAG = OfflineMapTool::class.java.canonicalName
    private val offlineMapDownloadListener = object :OfflineMapManager.OfflineMapDownloadListener{
        override fun onDownload(status: Int, completeCode: Int, name: String?) {
            Log.d(TAG,"onDownload: $name completeCode: $completeCode status:$status")
            OfflineMapStatus.SUCCESS
        }

        override fun onCheckUpdate(hasNew: Boolean, name: String?) {
        }

        override fun onRemove(success: Boolean, name: String?, describe: String?) {
        }

    }
    private val offlineMapManager:OfflineMapManager = OfflineMapManager(mContext,offlineMapDownloadListener)
    fun startOfflineActivity(activity: Activity){
        //在Activity页面调用startActvity启动离线地图组件
        activity.startActivity(Intent(activity.applicationContext,
                OfflineMapActivity::class.java))
    }

    fun checkOfflineMapCity(cityName:String):Boolean{
        var haveMap = false
        val offlineMapCityList = offlineMapManager.offlineMapCityList
        offlineMapCityList.forEach {
            if (it.city == cityName) {
                haveMap = true
            }
        }
        return haveMap
    }



    fun checkCityIsDownload (manager: FragmentManager, cityCode:String, isShowPop:Boolean = false, listener: MapDialogCallBack.OffinePopuCallBack):Boolean{
//        offlineMapManager!!.downloadByCityCode(cityCode)
//        offlineMapManager!!.downloadByCityCode(cityCode)
//        MapsInitializer.sdcardDir = Environment.getExternalStorageDirectory().toString() + "/trust/"
        val downloadingCityList = offlineMapManager!!.downloadingCityList
        val downloadOfflineMapCityList = offlineMapManager!!.downloadOfflineMapCityList
        Log.d(TAG,"当前城市 cityCode:$cityCode  ${downloadOfflineMapCityList.size} downloadingCityList: ${downloadingCityList.size}")
        if (downloadOfflineMapCityList.isNotEmpty()) {
            downloadOfflineMapCityList.forEach {
                Log.d(TAG,"离县城是列表 it.code ${it.code} cityCode:$cityCode")
                if (it.code == cityCode) {
                    return true
                }
            }
        }
        if (isShowPop) {
            MapDialogUtils().showOffine1(manager,"1",listener)
        }
        return false
    }
}