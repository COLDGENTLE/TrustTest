package com.sharkgulf.soloera.cards.activity.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.bs.trust.mapslibrary.TrustGaoDeMapsTool
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.BsRideReportBean
import com.sharkgulf.soloera.module.bean.BsRideSummaryBean
import com.sharkgulf.soloera.module.bean.BsRideTrackBean
import com.sharkgulf.soloera.module.bean.BsTimeLevelBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.WebAlertBean
import com.sharkgulf.soloera.module.hirstory.MapModule
import com.sharkgulf.soloera.mvpview.history.HirstoryView
import com.sharkgulf.soloera.presenter.history.HirstoryPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_RIDE_TRACK
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustPopuwindow
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackLineChart
import com.sharkgulf.soloera.tool.view.trackprogressview.TrackProgressView
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_ride_track.*



@Route(path = ROUTE_PATH_RIDE_TRACK)
class RideTrackActivity : TrustMVPActivtiy<HirstoryView, HirstoryPresenters>(), HirstoryView{


    override fun resultCarinfo(totalMiles: Int, bindDays: Int, maxMils: Int) {}

    override fun resultBattUseNum(batt1: BattInfoBean.BodyBean.BattBean?, batt2: BattInfoBean.BodyBean.BattBean?) {}

    companion object {
        fun routeStartActivity(bikeId:Int,bean:BsRideReportBean.DataBean.RidesBean){
            val params = Bundle()
            params.putSerializable("bean",bean)
            params.putInt("bikeId",bikeId)
            arouterStartActivity(ROUTE_PATH_RIDE_TRACK,params)
        }
    }
    private var mPoints:List<BsRideTrackBean.DataBean.LocsBean>? = null
    private var mStart: BsRideTrackBean.DataBean.LocsBean? = null
    private var mEnd: BsRideTrackBean.DataBean.LocsBean? = null
    private val TAG = "RideTrackActivity"
    private var aMap: AMap? = null
    private var mIsEnd = true
    private var mIsBig = false
    private var mTilt = 0f
    private var isShowinfo = true
    private var mZoom = 1f
    private var isFirst = false
    private val speedLevelList = arrayListOf(MapModule.SPEED_LEVEL_1,MapModule.SPEED_LEVEL_2,
            MapModule.SPEED_LEVEL_3,MapModule.SPEED_LEVEL_4,MapModule.SPEED_LEVEL_5,MapModule.SPEED_LEVEL_6)
    private var speedLevel = 1

    private var mBean:BsRideReportBean.DataBean.RidesBean? = null
    override fun getLayoutId(): Int { return R.layout.activity_ride_track }

    override fun initView(savedInstanceState: Bundle?) {
        TrustPopuwindow.mapType = 0
        ride_track_map.onCreate(savedInstanceState)// 此方法必须重写

        if (aMap == null) {
            aMap = ride_track_map.map
        }

        val uiSettings = aMap!!.uiSettings//实例化UiSettings类对象
        uiSettings.zoomPosition
        uiSettings.isZoomControlsEnabled = false
        baseSetOnClick(ride_track_control_btn)
        baseSetOnClick(ride_track_control_speed_btn)
        baseSetOnClick(ride_track_black_btn)
        baseSetOnClick(ride_track_control_map_btn)
        baseSetOnClick(ride_track_map_type_btn)
        baseSetOnClick(ride_track_control_data_btn)
        ride_track__track_progress_view.setOnProgressListener(listener)

    }

    override fun initData() {
        mBean = intent.getSerializableExtra("bean") as BsRideReportBean.DataBean.RidesBean?
        val bikeId = intent.getIntExtra("bikeId", -1)
        if (mBean != null && bikeId != -1) {
            ride_track_start_tx.text ="起点:" + mBean?.begin_pos
            ride_track_end_tx.text = "终点:" +mBean?.end_pos
            getPresenter()?.requestRideTrack(RequestConfig.requestRideTrack(bikeId, mBean!!.track_id))
            ride_track__track_progress_view.setMaxYAxis(mBean!!.max_speed / 1000)

            show(mBean!!.ts*1000,mBean!!.begin_time!!,mBean!!.end_time!!)
        }
        changeMapData()
//        else{
//            finish()
//        }
        val extsBean = intent.getSerializableExtra(ALERT_KEY) as WebAlertBean.BodyBean.ExtsBean?
        if (extsBean != null ) {
            ride_track_start_tx.text ="起点:" + extsBean.begin_pos
            ride_track_end_tx.text = "终点:" +extsBean.end_pos
            ride_track__track_progress_view.setMaxYAxis(extsBean.max_speed / 1000)
            show(extsBean.ts * 1000,extsBean.begin_time,extsBean.end_time)
            getPresenter()?.requestRideTrack(RequestConfig.requestRideTrack(TrustAppConfig.bikeId, extsBean.track_id))
        }



        changeMapData(extsBean)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.ride_track_control_btn -> {
                openOrClose()
            }
            R.id.ride_track_control_speed_btn -> {
                controllSpeed()
            }
            R.id.ride_track_black_btn -> {
                finish()
            }
            R.id.ride_track_control_map_btn -> {

                changeMap()
            }
            R.id.ride_track_map_type_btn -> {
                TrustPopuwindow(this)
                        .setMapPopuwindowListener(mapPopuwindowListener)
                        .showPopupWindow(ride_track_map_type_btn)
            }
            R.id.ride_track_control_data_btn -> {
                changeShowInfo()
            }
            else -> { }
        }

    }

    private fun changeShowInfo() {
        isShowinfo = !isShowinfo
        ride_track_control_data_btn.setImageResource(if(isShowinfo)R.drawable.ride_track_control_map_hirsory_line_ic  else R.drawable.ride_track_control_map_hirsory_ic  )
        ride_track_info.visibility = if(isShowinfo) View.VISIBLE else View.GONE
        ride_track__track_progress_view.visibility = if(!isShowinfo) View.VISIBLE else View.GONE
    }

    private fun controllSpeed() {
        if (speedLevel == speedLevelList.size) {
            speedLevel = 0
        }
        val ic = when (speedLevel) {
            0 -> { R.drawable.ride_track_control_speed_1_ic }
            1 -> { R.drawable.ride_track_control_speed_2_ic}
            2 -> { R.drawable.ride_track_control_speed_3_ic }
            3 -> { R.drawable.ride_track_control_speed_4_ic }
            4 -> { R.drawable.ride_track_control_speed_5_ic }
            5 -> { R.drawable.ride_track_control_speed_6_ic }
            else -> {
                null
            }
        }

        if (ic != null) {
            ride_track_control_speed_btn.setImageResource(ic)
            getPresenter()?.controlSpeed(speedLevelList[speedLevel++])
        }
    }

    private fun openOrClose() {
        getPresenter()?.controlPoint(mIsEnd)
        changeUi()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
//        TrustLogUtils.d(TAG,msg)
        showToast(msg)
    }

    override fun resultTimeLevel(bean: BsTimeLevelBean?) {}

    override fun resultRideSummary(bean: BsRideSummaryBean?) {}

    override fun resultRideReport(bean: BsRideReportBean?) {}

    override fun resultRideTrack(bean: BsRideTrackBean?) {
        if (BeanUtils.checkSuccess(bean!!.getState()!!,bean.getState_info()!!,this)) {
            mPoints = bean.getData()!!.locs
            if (mPoints != null) {
                if (mPoints!!.isNotEmpty()) {
                    mStart = mPoints!![0]
                    mEnd = mPoints!![mPoints!!.size-1]
                    showMaker(true)
                    getPresenter()?.drawTrajectory(aMap!!,R.drawable.map_location_car_ic,bean,false)
                    getPresenter()?.getTrackLineData(mPoints!!)
                }else{
                    showToast("无数据")
                }
            }else{
                showToast("无数据")
            }
        }
    }

    private fun showMaker(isClearMap:Boolean) {
        GdMapTool.getInstance().setMarker(aMap!!, mStart!!.lat, mStart!!.lng, isClearMap, true, false, false, R.drawable.history_start, 19f, false)
        GdMapTool.getInstance().setMarker(aMap!!, mEnd!!.lat, mEnd!!.lng, false, true, false, false, R.drawable.history_end, 19f, false)
    }

    override fun createPresenter(): HirstoryPresenters {
        return HirstoryPresenters()
    }


    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        ride_track_map.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        ride_track_map.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ride_track_map.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        ride_track_map.onDestroy()
    }



    override fun resultDrawTrajectory(status: Boolean) {
        if (!isFirst) {
            isFirst = true

            openOrClose()
        }
        TrustLogUtils.d(TAG,"resultDrawTrajectory : $status")
    }

    override fun resultMoveListener(isEnd: Boolean,pos:Int?) {
        runOnUiThread {
            if (isEnd) {
                mOldPos = DEFULT
                changePos(0)
                changeViewPos(0)
                changeUi()
            }
            if (pos != null && pos != 0 && !isEnd) {
                if (mOldPos != -1) {
                    changeViewPos(mOldPos+pos)
                }else{
                    changeViewPos(pos)
                }
            }
            TrustLogUtils.d(TAG,"pos:$pos mOldPos:$mOldPos  isEnd:$isEnd ")

        }
    }


    override fun resultAddressList(addressList: ArrayList<String>?) {
        addressList?.forEach {
//            TrustLogUtils.d("MapModule","我是activity $it")
        }
//        ride_track_start_tx.text = addressList!![0]
//        ride_track_end_tx.text = addressList[1]

//        GdMapTool(this).setMarker(aMap!!,mStart!!.lat,mStart!!.lng,false,true,false,false,R.drawable.history_start,19f,false)
//        GdMapTool(this).setMarker(aMap!!,mEnd!!.lat,mEnd!!.lng,false,true,false,false,R.drawable.history_end,19f,false)
    }

    private fun changeUi(){
        mIsEnd = !mIsEnd
        ride_track_control_btn.setImageResource(if(mIsEnd) R.drawable.ride_track_control_start_ic else R.drawable.ride_track_control_pause_ic)
    }

    private fun changeMap(){
        if (!mIsBig) {
            mZoom = aMap!!.cameraPosition.zoom
        }
        mIsBig = !mIsBig
        ride_track_control_map_btn.setImageResource(if(mIsBig) R.drawable.ride_track_control_map_latter_ic else R.drawable.ride_track_control_map_big_ic)
        aMap?.moveCamera(if(!mIsBig) CameraUpdateFactory.zoomTo(mZoom) else CameraUpdateFactory.zoomTo(19f))
    }

    private fun changeMapData(){
        if (mBean != null) {
            ride_track_sum_tx.text = setTextSpanneds(R.string.ride_track_sum_tx,getMileageDouble(mBean!!.miles))
            ride_track_speed_tx.text = setTextSpanneds(R.string.ride_track_speed_tx,getSpeed(mBean!!.avg_speed))
            ride_track_time_tx.text = setTextSpanneds(R.string.ride_track_time_tx,getHMS(mBean!!.times))
//            ride_track_max_speed_tx.text = setTextSpanneds(R.string.ride_track_max_speed_tx,mBean!!.max_speed.toString())
        }
    }

    private fun changeMapData(bean:WebAlertBean.BodyBean.ExtsBean?){
        if (bean != null) {
            ride_track_sum_tx.text = setTextSpanneds(R.string.ride_track_sum_tx,getMileageDouble(bean!!.miles))
            ride_track_speed_tx.text = setTextSpanneds(R.string.ride_track_speed_tx,getSpeed(bean!!.avg_speed))
            ride_track_time_tx.text = setTextSpanneds(R.string.ride_track_time_tx,getHMS(bean!!.times))
//            ride_track_max_speed_tx.text = setTextSpanneds(R.string.ride_track_max_speed_tx,bean!!.max_speed.toString())
        }
    }


    private val mapPopuwindowListener = object :TrustPopuwindow.MapPopuwindowListener{
        override fun CallBack(type: Int) {
            mTilt = when (type) {
                0 -> {//标准地图
                    TrustGaoDeMapsTool.changeMapType(aMap!!,AMap.MAP_TYPE_NORMAL)
                    0f
                }
                1 -> {//卫星地图
                    TrustGaoDeMapsTool.changeMapType(aMap!!,AMap.MAP_TYPE_SATELLITE)
                    0f
                }
                2 -> {//3d地图
                    TrustGaoDeMapsTool.changeMapType(aMap!!,AMap.MAP_TYPE_NORMAL)
                    40f
                }
                else -> {
                    0f
                }
            }
           aMap?.moveCamera(CameraUpdateFactory.changeTilt(mTilt))
        }
    }


    override fun resultTrackLineData(list: List<TrackLineChart.Data>) {
        if (list.isNotEmpty()) {
            ride_track__track_progress_view.setData(list)
        }
    }

    private var mOldPos = DEFULT
    private val listener = TrackProgressView.OnProgressListener { progress->

        if (mPoints != null && mPoints!!.isNotEmpty()) {
            if (mOldPos != progress) {
                mOldPos = progress
                if (!mIsEnd) {
                    openOrClose()
                }
                changePos(progress)
            }
        }
    }

    private fun changePos(progress: Int) {
        getPresenter()?.linkageMaker(aMap!!, R.drawable.map_location_car_ic, progress)
    }

    private fun changeViewPos(progress: Int){
        ride_track__track_progress_view.setProgress(progress)
    }

    private fun show(time:Long,startTime:String,endTime:String){
        TrustLogUtils.d(TAG,"行程时间戳: $time")
        stroke_time_tx .text = "${TrustTools.getTime(TrustTools.getTimes(startTime))} ${TrustTools.getTimes(TrustTools.getTimes(startTime))} ~ ${TrustTools.getTimes(TrustTools.getTimes(endTime!!))}"
    }
}
