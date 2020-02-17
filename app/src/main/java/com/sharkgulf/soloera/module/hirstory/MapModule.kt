package com.sharkgulf.soloera.module.hirstory

import android.annotation.SuppressLint
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.amap.api.maps.utils.overlay.MovingPointOverlay
import com.amap.api.services.geocoder.RegeocodeResult
import com.bs.trust.mapslibrary.gd.GdLineTool
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *  Created by user on 2019/7/30
 */
class MapModule :MapModuleListener{


    companion object {
        val SPEED_LEVEL_1 = 1.0
        val SPEED_LEVEL_2 = 0.8
        val SPEED_LEVEL_3 = 0.6
        val SPEED_LEVEL_4 = 0.4
        val SPEED_LEVEL_5 = 0.2
        val SPEED_LEVEL_6 = 0.1
    }

    private val mGdLineTool = GdLineTool.getGdLineTools()
    private var mMovingPointOverlay: MovingPointOverlay? =  null
    private var mSpeedLevel:Double = SPEED_LEVEL_1
    private var mSumSpeed = 0
    private val TAG = "MapModule"
    private val LOCATION_END = 0
    private var mLocationListenr:ModuleResultInterface<Boolean>? = null
    private var mGdMapTool = GdMapTool.getInstance()
    private var mSppedTime = 0.0
    private val mMovingListener = MovingPointOverlay.MoveListener {
        progress ->
        val position = mMovingPointOverlay?.position
        var mIndex:Int? = null
        mIndex = mMovingPointOverlay?.index
//            if (position != null) {
//                mGdLineTool.getCoordinates().forEachIndexed { index, latLng ->
//                    TrustLogUtils.d(TAG,"latLng:${latLng.toString()} position:${position.toString()}")
//                    val b = latLng.latitude == position.latitude && latLng.longitude == position.longitude
//                    if (b) { mIndex = index }
//                    TrustLogUtils.d(TAG,"latLng:${latLng.toString()} position:${position.toString()} b:$b index:${ mMovingPointOverlay?.index}")
//                }
//
//            }

        val b = progress.toInt() == LOCATION_END
        if (b) { mSumSpeed = mGdLineTool.getListSize() }
        mLocationListenr?.resultData(b,mIndex)

    }

    override fun drawTrajectory(aMap:AMap,ic:Int,bean: BsRideTrackBean, resultInterface: ModuleResultInterface<Boolean>,isDottedLine:Boolean) {
        var locs = bean.getData()!!.locs
        val observable = Observable.create<Boolean> {
            emitter->
            mGdLineTool.clearCoordinate()
            val newbounds =  LatLngBounds.Builder();

            locs?.forEach {
                newbounds.include( mGdLineTool.addCoordinate(it.lat,it.lng,true));//通过for循环将所有的轨迹点添加进去.
            }
            mGdLineTool.draw(aMap,isDottedLine)
            mPos = locs?.size ?: 0
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(),
                    300));//第二个参数为四周留空宽度.


//            autoStartRideTrack(aMap,ic)
            setMakerIc(aMap, ic)
//            mMovingPointOverlay?.setTotalDuration(mSpeedLevel.toInt())
            emitter.onNext(mMovingPointOverlay!=null)
            emitter.onComplete()
        }
        connection(observable,resultInterface)
    }

    private fun setMakerIc(aMap: AMap, ic: Int,pos:Int = 0) {
        mMovingPointOverlay?.removeMarker()
        mMovingPointOverlay = mGdLineTool.getMovingPointOverlay(aMap, ic, pos = pos)
        mMovingPointOverlay?.setMoveListener(mMovingListener)

        mSumSpeed = mGdLineTool.getListSize()
        controlSpeed(mSpeedLevel)
    }


    override fun locationAddress(arrayList: ArrayList<LatLng>, resultInterface: ModuleResultInterface<ArrayList<String>>) {
        val addressList = arrayListOf<String>()
        arrayList.forEach {
            val latLng = mGdMapTool.coordinateTransformation(it.latitude, it.longitude)
            mGdMapTool.getAddress(latLng.latitude,latLng.longitude,object : GdMapTool.onAddressListerner{
                override fun addressListerner(addressName: String) {
                    addressList.add(addressName)
                    TrustLogUtils.d(TAG,"address $addressName  it ${it.latitude} | ${it.longitude}  latlng ${latLng.latitude} |${latLng.longitude}    arrayList ：${arrayList.size}  addressList:${addressList.size}  ")
                    if (addressList.size == arrayList.size) {
                        resultInterface.resultData(addressList,null)
                    }
                }
                override fun errorListerner(result: RegeocodeResult?, rCode: Int) {
                    addressList.add("rCode : $rCode")
                    if (addressList.size == arrayList.size) {
                        resultInterface.resultData(addressList,null)
                    }
                }

            })
        }
    }


    override fun controlPoint(status: Boolean,resultInterface: ModuleResultInterface<Boolean>) {
        if (status) {
            mLocationListenr = resultInterface
            mMovingPointOverlay?.startSmoothMove()
        }else{
            mMovingPointOverlay?.stopMove()
        }
    }

    override fun controlSpeed(speedLevel: Double) {
        mSpeedLevel = speedLevel
        mSppedTime = mSumSpeed * mSpeedLevel
        mMovingPointOverlay?.setTotalDuration(mSppedTime.toInt())
    }


    @SuppressLint("CheckResult")
    fun <T> connection (observable: Observable<T>, resultInterface: ModuleResultInterface<T>)   {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t->
                    resultInterface.resultData(t,null)
                }, { t->
                    resultInterface.resultError(t.toString())
                })
    }

    override fun linkageMaker(aMap: AMap, ic: Int,pos:Int,resultInterface: ModuleResultInterface<Boolean>) {
        setMakerIc(aMap,ic,pos)
    }

    override fun coordinateTransformation(lat:Double,lng:Double):LatLng{
        return  mGdMapTool.coordinateTransformation(lat,lng)
    }



    private var mPos:Int = 0
    private var mDisposable: Disposable? = null

    fun autoStartRideTrack(aMap:AMap,ic:Int,pos:Int = 0){
        mDisposable?.dispose()
        while (pos<= mPos){
            mDisposable = TrustTools<View>().setCountdown(1000L * 1) {
                setMakerIc(aMap,ic,pos)
            }
        }
    }
}