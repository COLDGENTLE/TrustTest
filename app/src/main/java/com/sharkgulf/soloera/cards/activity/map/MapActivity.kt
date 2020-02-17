package com.sharkgulf.soloera.cards.activity.map

import android.Manifest
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.transition.*
import android.view.*
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.CoordinateConverter
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.sharkgulf.soloera.mvpview.map.IMapView
import com.sharkgulf.soloera.presenter.map.IMapPresenterListener
import com.sharkgulf.soloera.presenter.map.MapPresenter
import com.sharkgulf.soloera.tool.view.dialog.TrustPopuwindow
import com.bs.trust.mapslibrary.MapDialog
import com.bs.trust.mapslibrary.MapUtils
import com.bs.trust.mapslibrary.TrustGaoDeMapsTool
import com.bs.trust.mapslibrary.gd.GdLineTool
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.dn.tim.lib_permission.annotation.Permission
import com.dn.tim.lib_permission.annotation.PermissionCanceled
import com.dn.tim.lib_permission.annotation.PermissionDenied
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.cards.activity.map.LockBikeFragment.Companion.STATUS_LOCK
import com.sharkgulf.soloera.cards.activity.securitysettings.AlertActivity
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.maplibrary.bean.LocationBean
import com.trust.maplibrary.callback.MapDialogCallBack
import com.trust.maplibrary.callback.MapUtilsCallBack
import com.trust.statusbarlibrary.TrustStatusBarUtils
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.map_unlock_car_layout.*

class MapActivity : TrustMVPActivtiy<IMapView,MapPresenter>(),IMapView {



    private var status = SECUTITY_ALERT
    private var aMap:AMap? = null
    private var carBean: CarLoctionBean.BodyBean? = null
    private var carInfoBean: CarInfoBean? = null
    private var bsStatus = false//基站开关
    private var userLocation:LatLng? = null
    private var mtilt:Float = 0f
    private val TAG = MapActivity::class.java.canonicalName!!
    private var mIsShow = true
    override fun getLayoutId(): Int { return R.layout.activity_map }
//    private var mCarInfoBean: BsGetCarInfoBean.DataBean.BikesBean? = null

    private val gpsIcs = arrayListOf(R.drawable.map_gps_status_0_ic
            ,R.drawable.map_gps_status_1_ic,
            R.drawable.map_gps_status_2_ic,R.drawable.map_gps_status_3_ic,R.drawable.map_gps_status_4_ic)
    private var carOlStatus = false
    private var mCarIc:Bitmap? = null
    private var mUserIc:Bitmap? = null
    private val REGISTER_MAP_TOPIC = WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_LOCTION
    private val SEND_MAP_TOPIC = WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCTION
    private var mDisposable: Disposable? = null
    private var mLockBikeFragment:LockBikeFragment? = null
    private var mUnLockBikeFragment:UnLockBikeFragment? = null
    private var mIsFirstInit = true
    override fun initView(savedInstanceState: Bundle?) {

        activity_map_mapview.onCreate(savedInstanceState)// 此方法必须重写
        if (aMap == null) {
            aMap = activity_map_mapview.map
        }
        aMap?.uiSettings?.isZoomControlsEnabled = false
         //指北针
        aMap!!.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
            }

            override fun onCameraChange(cameraPosition: CameraPosition?) {
                test_img.rotation = cameraPosition!!.bearing * -1
            }
        })
        baseSetOnClick(activity_map_type_btn)
        baseSetOnClick(activity_map_car_btn)
        baseSetOnClick(activity_map_user_btn)
        baseSetOnClick(activity_map_bsa_btn)

        baseSetOnClick(back_btn)
        baseSetOnClick(test_img)
        baseSetOnClick(activity_map_car_navigation_btn)
        baseSetOnClick(activity_map_car_online_tx)
        baseSetOnClick(map_offinemap_download_btn)
        baseSetOnClick(map_bike_status_layout)
        baseSetOnClick(activity_map_find_car_btn)

        if (MAP_TYPE != MAP_TYPE_GPS) {//基站
            hideView(activity_map_bsa_bg,bsStatus)
            bsStatus = true
        }
//        mCarInfoBean = intent.getSerializableExtra("carBean") as BsGetCarInfoBean.DataBean.BikesBean?
//        mCarIc = getCarIc()
//        mUserIc = getUserIc()

        mchooseFragmentCallBack.callBack(true,STATUS_LOCK)


        val user = BsApplication.mAuthentication.getUser()
        if (user != null) {
//            map_user_ic.glideUserIc(user.userBean?.icon,true)
        }


        TrustStatusBarUtils.getSingleton(this).setStatusBarColor(this,Color.BLACK)
        TrustStatusBarUtils.getSingleton(this).setStatusBarLightMode(this,false)
//        setStatusBarTransparent()


        map_bike_status_controll_btn.setOnCheckedChangeListener { buttonView, isChecked ->
            status = if (isChecked) {
                SECUTITY_ALERT
            }else{
                SECUTITY_DO_NOT_DISTURB
            }
            //getPresenter()?.changeSecurity(RequestConfig.changeSecurity(status, swMap as HashMap<Int, Boolean>))
        }


    }

    override fun initData() {
        val visibility = if(USE_TYPE != USE_NOMOL){
            View.GONE }else{
            startUserLocation()
            View.VISIBLE }
        activity_map_user_btn.visibility = visibility
        DataAnalysisCenter.getInstance().registerCallBack(REGISTER_MAP_TOPIC,object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String,timeOutTopic:String?) {}

            override fun onNoticeCallBack(msg: String?) {
                getData()
            }
        },TAG+1)

        getData()
//        DataAnalysisCenter.getInstance().sendData(SEND_MAP_TOPIC)
        getPresenter()?.getCarInfo()
    }


    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.activity_map_type_btn -> {
                TrustPopuwindow(this)
                        .setMapPopuwindowListener(mapPopuwindowListener)
                        .showPopupWindow(activity_map_type_btn)
            }
            R.id.activity_map_car_btn -> {
//                isCarCenter = true
//                onclickView(activity_map_user_btn, true, activity_map_car_btn)
//                hideView(activity_map_user_bg,true)
//                hideView(activity_map_car_bg,false)

                val mlatlon = choose()?.mlatlon
                if (mlatlon != null) {

                    if (!GdMapTool(this@MapActivity).moveMapCenter(aMap!!,mlatlon.
                                    latitude,mlatlon.longitude,19f,
                                    true,mtilt,true)) {
                        showToast("车辆位置信息错误!")
                    }
                }else{
                    showToast("车辆位置信息错误!")
                }

            }
            R.id.activity_map_user_btn -> {
//                isCarCenter = false
//                hideView(activity_map_user_bg,false)
//                hideView(activity_map_car_bg,true)
//                onclickView(activity_map_car_btn,true,activity_map_user_btn)
//                showUserIc(false,true)
                showCarAndUser(false,false,true)
                mDisposable?.dispose()
                mDisposable = TrustTools<View>().setCountdown(2,object :TrustTools.CountdownCallBack{
                    override fun callBackCountDown() {
                        startUserLocation()
                    }

                })
//                startUserLocation()
//                var mStatus = false
//                 if (userLocation != null) {
//                     mStatus = GdMapTool(this@MapActivity).moveMapCenter(aMap!!,
//                             userLocation!!.latitude, userLocation!!.longitude,
//                             19f,true,mtilt)
//                     if (!mStatus) {
//                      showToast("用户位置信息错误,请稍后重试!")
//                     }else{
//                         showUserIc(isClear = false, isMoveCenter = true)
//                         startUserLocation()
//                     }
//                }else{
//                     showToast("用户位置信息错误,请稍后重试!")
//                }
//                TrustLogUtils.d(TAG,"点击用户 地点 $userLocation | 移动api $mStatus")
//                getData()
            }
            R.id.activity_map_bsa_btn -> { showCarBs(bsStatus) }
//            R.id.activity_map_add_btn->{ changeMapZoom(CameraUpdateFactory.zoomIn())
//                hideView(activity_map_remove_bg,true)
//                hideView(activity_map_add_bg,false)
//
//            }
//            R.id.activity_map_remove_btn->{
//                hideView(activity_map_remove_bg,false)
//                hideView(activity_map_add_bg,true)
//                changeMapZoom(CameraUpdateFactory.zoomOut())}
            R.id.back_btn->{finish()}
            R.id.activity_map_car_navigation_btn ->{navigation()}

            R.id.map_offinemap_download_btn -> {
//                toOffineMapActivty()
            }
            R.id.activity_map_car_online_tx->{ showCarStatus() }
//            R.id.test_img -> { changeAnimeLayout() }
//            R.id.text_layout -> { changeAnimeLayout() }
            R.id.map_bike_status_layout -> {
                if (!isDemoStatus()) {
                    startActivity(Intent(this, AlertActivity::class.java))
                }
               }
            R.id.activity_map_find_car_btn -> { getPresenter()?.requestFindCar(ACTION_OPEN) }
            else -> { }
        }
    }

    private var mIsBoole = false
    private var oldWidth = 0
    private fun changeAnimeLayout() {
        if (oldWidth == 0) {
            oldWidth = map_bike_status_layout.width
        }
        val viewGroup= findViewById<ViewGroup>(android.R.id.content)
        TransitionManager.beginDelayedTransition(viewGroup, ChangeBounds());
        val params= map_bike_status_layout.layoutParams;

        mIsBoole = !mIsBoole
        var bg = 0

        val trustAnimation = TrustAnimation.getTrustAnimation()
        if (!mIsBoole) {
            trustAnimation.addAlphaAnimation(1f,0f,50)
            params.width = oldWidth
            bg = R.drawable.map_btn_up_bg
        }else{
            trustAnimation.addAlphaAnimation(0f,1f,500)
//            params.width = map_bike_status_test_layout.width
            bg = R.drawable.map_bike_status_btn_bg_ic
        }
        trustAnimation.startAnimation(map_bike_status_controll_btn)
        map_bike_status_layout.background = getResources().getDrawable(bg,null)
        map_bike_status_layout.layoutParams = params
    }

    private fun showCarStatus() {
        val msg = if (carOlStatus) {
            getString(R.string.car_ol_success_tx)
        }else{
            getString(R.string.car_ol_error_tx)
        }
//        showToast(msg)
    }

    var testStatus = false

    private fun navigation() {
        if (!isDemoStatus()) {
            if (carBean != null ) {
                CoordinateConverter.CoordType.GPS
                val lat:Double
                val lng:Double
                if (MAP_TYPE == MAP_TYPE_GPS) {
                    lat = carBean!!.gps!!.lat
                    lng = carBean!!.gps!!.lng
                }else{
                    lat = carBean!!.bs!!.lat
                    lng = carBean!!.bs!!.lng
                }
                if (lng != 0.0) {


                    var showMapDialog: MapDialog? = null
                    showMapDialog = MapUtils(mActivity!!, object : MapUtilsCallBack {
                        override fun isUseSuccess(isUse: Boolean, msg: String) {
                            showMapDialog?.dismiss()
                            if (!isUse) {
                                showToast(msg)
                            }
                        }
                    }).showMapDialog(supportFragmentManager!!, LocationBean(lat,
                            lng,MapUtils.ELEBIKE_NAVI,userLocation?.latitude,userLocation?.longitude))

                }else{
                    showToast("车辆位置未知！请重新获取车辆位置!")
                }
            }else{
                showToast("车辆位置未知！请重新获取车辆位置!")
            }
        }

    }


    private fun iniLocation() {
        TrustLogUtils.d(TAG,"userLocation : $userLocation  carBean: $carBean")
        if (userLocation == null && carBean == null) {
            return
        }
        else if (userLocation == null && carBean != null) {
            val choose = choose()
            val mlatlon = choose?.mlatlon
            if (mlatlon != null) {
                showCarIc(mlatlon,true,true,choose.radius != null)
            }
        }

        else if(userLocation != null && carBean == null){
            showUserIc(true,true)
        }

        else{
            showCarAndUser()
        }
    }

    private fun showCarAndUser(isMoveCenter: Boolean = true,isMoveCar:Boolean = false, isMoveUser:Boolean = false) {
        TrustLogUtils.d(TAG,"showCarAndUser")
        val choose = choose()
        if (choose != null ) {
             if (userLocation != null) {

                if (choose.mlatlon.longitude != 0.0 && userLocation?.latitude != 0.0) {
                    GdMapTool(this).showCarAndUser(aMap!!, userLocation!!.latitude, userLocation!!.longitude,
                            choose.mlatlon.latitude, choose.mlatlon.longitude, true,isMoveCenter,mtilt,true)
                    GdLineTool.getGdLineTools().drawLines(aMap!!,
                            arrayListOf(userLocation!!,
                            GdLineTool.getGdLineTools().addCoordinate(choose.mlatlon.latitude,choose.mlatlon.longitude,true) )
                            ,
                            false,true)
                     }
            }
            if (choose.mlatlon.longitude != 0.0) {
                showUserIc(false, isMoveUser)
            }else{
                showUserIc(true, false)
            }
//            showCarIc(choose.mlatlon, false, isMoveCar, choose.radius != null)
            if(choose.diff >= BS_SHOW_TIME ){
                showCarIc(choose.mlatlon, false, isMoveCar, choose.radius != null)
            }else{
                showCarIc(choose.mlatlon, false, isMoveCar, false)
            }

        }else{
            showToast("车辆信息有错误！")
        }
    }

    @Permission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun startUserLocation() {
        Thread(Runnable {
        getPresenter()!!.getUserLocation(object : IMapPresenterListener.MapListener {
            override fun userLocation(aMapLocation: AMapLocation) {
                runOnUiThread {
                    userLocation = LatLng(aMapLocation.
                            latitude,aMapLocation.longitude)
                    if (mIsFirstInit) {
                        mIsFirstInit = false
//                        checkOffinMap(aMapLocation.cityCode,mIsShow)
                        getData()
                    }else{
//                       showCarAndUser(isMoveCenter = false,isMoveUser = true)
                    }

                    TrustLogUtils.d(TAG,"定位结果:成功  mIsFirstInit  $mIsFirstInit")
                }
//                if (!TrustGaoDeMapsTool.getOfflineMapTool(this@MapActivity).checkCityIsDownload(aMapLocation.cityCode)) {
////                    showToast("没有下载当前城市的离线地图，是否下载")
//                }
            }

            override fun errorLocation(msg: String) {
                TrustLogUtils.d(TAG,"定位结果:$msg")
                showToast(msg)
            }

        })

        }).start()
    }

    fun showUserIc(isClear:Boolean,isMoveCenter:Boolean) {
        if (userLocation != null && userLocation!!.latitude != 0.0) {
            GdMapTool(this@MapActivity).setMarker(aMap!!, userLocation!!.latitude, userLocation!!.longitude, isClear,
                    false, isMoveCenter, false,
                    R.drawable.map_location_user_ic, 19f,true,mtilt,null)
        }

    }


    fun showCarIc(latLng: LatLng,isClear:Boolean,isMoveCenter:Boolean,isSetCircle:Boolean) {
        if (latLng.latitude != 0.0) {
            GdMapTool(this@MapActivity).setMarker(aMap!!, latLng.latitude, latLng.longitude, isClear,
                    true, isMoveCenter, isSetCircle,
                    R.drawable.map_location_car_ic, 19f,true,mtilt,mCarIc)
        }
    }

    /**
     * 检查车辆位置信息
     */
    private fun choose(): BsCarBean? {
        if (carBean != null) {

        var bsCarBean: BsCarBean? = null
        val gps = carBean!!.gps
        val bs = carBean!!.bs
//        activity_map_bs_status_ic.setImageResource(bsIcs[bs.level])
        if(gps.level<gpsIcs.size){
            activity_map_gps_status_ic.setImageResource(gpsIcs[gps.level])
        }
        bsCarBean = if (bsStatus) {//基站开
            val ts = carBean?.gps!!.ts
            val systemTimeDataSecond = TrustTools.getSystemTimeDataSecond()
            if(systemTimeDataSecond - ts >= (1 * 60 * 15)){//基站
                BsCarBean(LatLng(bs.lat, bs.lng), bs.ts.toLong(), bs.desc!!, bs.level,
                        bs.update_desc!!.diff, bs.update_desc!!.style, bs.update_desc!!.text, "基站定位", bs.radius)
            }else{
                BsCarBean(LatLng(gps.lat, bs.lng), gps.ts.toLong(), gps.desc!!, gps.level,
                        gps.update_desc!!.diff, gps.update_desc!!.style, gps.update_desc!!.text, "卫星定位", null)
            }
        }else{
            BsCarBean(LatLng(gps.lat, gps.lng), gps.ts.toLong(), gps.desc!!, gps.level,
                    gps.update_desc!!.diff, gps.update_desc!!.style, gps.update_desc!!.text, "卫星定位", null)
        }
        return bsCarBean

        }else{
            return null
        }
    }


    private fun changeMapZoom(update : CameraUpdate,isAnimate:Boolean = true){
        if (isAnimate) {
            aMap!!.animateCamera(update,500,null)
        }else{
            aMap!!.moveCamera(update)
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) { showToast(msg) }

    fun hideView(v:View,isHide:Boolean){
        val viewStatus  = if (isHide) {//隐藏
            View.GONE
        }else{//显示
            View.VISIBLE
        }
        v.visibility = viewStatus
    }

    fun onclickView(v1:View,isClick:Boolean,v2:View){
        v1.isClickable = isClick
        v2.isClickable = !isClick
    }


    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        activity_map_mapview.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        activity_map_mapview.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activity_map_mapview.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        TrustPopuwindow(this).setClearMapType()
        super.onDestroy()
        activity_map_mapview.onDestroy()
        DataAnalysisCenter.getInstance().unRegisterCallBack(REGISTER_MAP_TOPIC,TAG)
    }

    override fun createPresenter(): MapPresenter {
        val mapPresenter = MapPresenter()
        mapPresenter.setTAG(TAG)
        return mapPresenter
    }

    override fun resultCarInfo(bean: BsGetCarLocationBean?) {

    }

    private fun changeCarInfo() {
        val chooseBean = choose()
        if (chooseBean != null) {

            if (chooseBean.mlatlon.latitude != 0.0) {
                map_no_address_layout.visibility = View.GONE
                map_address_info_data_layout.visibility = View.VISIBLE
            activity_map_car_time_tx.text = chooseBean.text

            val color = when {
                chooseBean.diff in (BS_SHOW_YELLOW_TIME + 1)..BS_SHOW_RED_TIME -> "#ffff00"
                chooseBean.diff > BS_SHOW_RED_TIME -> "#ff0000"
                else -> "#000000"
            }

//            activity_map_car_time_tx.setTextColor(Color.parseColor(color))

            activity_map_car_online_tx.text = if (carBean!!.ol_status == 1) {
                carOlStatus = true
                "在线"
            } else {
                carOlStatus = false
                "离线"
            }
            if (chooseBean.radius != null) {
                activity_map_car_region_tx.visibility = View.VISIBLE
                TrustLogUtils.d(TAG,"radiurs : ${chooseBean.radius}")
                activity_map_car_region_tx.text = "精度约 ${chooseBean.radius}m"

            }else{
                activity_map_car_region_tx.visibility = View.INVISIBLE
            }
//            activity_map_car_name_tx.text = carInfoBean?.bike_name
            activity_map_car_address_tx.text = chooseBean.desc
            iniLocation()
            }else{
                showCarAndUser(isMoveCenter = false,isMoveUser = true)
            }
        }else{
            map_no_address_layout.visibility = View.VISIBLE
            map_address_info_data_layout.visibility = View.INVISIBLE
        }

    }


    @PermissionCanceled
    private fun cancel() {
        showToast("请允许，否则无法定位到您的位置!")
    }


    @PermissionDenied
    private fun denied() {
        showToast("请允许，否则无法定位到您的位置!")
    }

    private val mapPopuwindowListener = object :TrustPopuwindow.MapPopuwindowListener{
        override fun CallBack(type: Int) {
            mtilt = when (type) {
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
//            changeCarInfo()
            aMap?.moveCamera(CameraUpdateFactory.changeTilt(mtilt))
        }
    }



    class BsCarBean(val mlatlon:LatLng = LatLng(0.0,0.0), val ts:Long, val desc:String, val level:Int, var diff: Int,
                    var style: String?,var text: String?,var type:String,val radius:Int? = null)



    private fun getData(){
        val data = DataAnalysisCenter.getInstance().getData<CarLoctionBean>("$WEB_SOKECT_RECEIVE$WEB_SOKECT_CAR_LOCTION")
        carInfoBean = getCarInfoData()
        carBean =data?.body
        changeCarInfo()
    }


    /**
     * 锁车
     */
    private val BIKE_STATUS_LOCK = 1
    private val BIKE_STATUS_LOCK_ING = 2
    private val BIKE_STATUS_LOCK_CANCEL = 3
    private val BIKE_STATUS_UNLOCK = 4
    private val BIKE_STATUS_UNLOCK_ING = 5
    private val BIKE_STATUS_UNLOCK_CANCEL = 6

    private val bikeStatus = BIKE_STATUS_LOCK


    private fun initLockLayout(){
        when (bikeStatus) {
            BIKE_STATUS_LOCK -> {
            }
            BIKE_STATUS_LOCK_CANCEL -> {
            }
            BIKE_STATUS_UNLOCK -> {
                unlock_car_unlock_title_tx.visibility = View.VISIBLE
                unlock_car_unlock_layout.visibility = View.GONE
            }

            BIKE_STATUS_UNLOCK_CANCEL -> {

            }
        }
    }

    /**
     * 锁车
     */
    private fun lock(){

    }


    /**
     * 解锁
     */
    private fun unLock(){

    }



    private fun getLockBikeFragment(status:Int,callBack:chooseFragmentCallBack):LockBikeFragment{
        if (mLockBikeFragment == null) {
            mLockBikeFragment = LockBikeFragment()
        }
        mLockBikeFragment?.setCallBack(callBack)
        mLockBikeFragment?.setStatus(status)
        return mLockBikeFragment!!
    }

    private fun getUnLockBikeFragment(status:Int,callBack:chooseFragmentCallBack):UnLockBikeFragment{
        if (mUnLockBikeFragment == null) {
            mUnLockBikeFragment = UnLockBikeFragment()
        }
        mUnLockBikeFragment?.setCallBack(callBack)
        mUnLockBikeFragment?.setStatus(status)
        return mUnLockBikeFragment!!
    }

    private val mchooseFragmentCallBack = object :chooseFragmentCallBack{
        override fun callBack(isLock: Boolean, status: Int) {
            val fragment = if (isLock) {
                getLockBikeFragment(status,this)
            }else{
                getUnLockBikeFragment(status,this)
            }
            supportFragmentManager.beginTransaction().replace(R.id.map_framelayout,fragment).commit()
        }

    }

    interface  chooseFragmentCallBack{
        fun callBack(isLock:Boolean,status:Int)
    }

    private fun showCarBs(status:Boolean){
        val btn1 = getString(R.string.cancel)
        val btn2 = getString(R.string.confirm)
        val msg = if (status) {//开启基站
            getString(R.string.map_bs_close_tx)
        }else{//关闭
            getString(R.string.map_bs_open_tx)
        }
        getGPPopup().showDoubleBtnPopu(msgTx = msg,btn1Tx = btn1,btn2Tx = btn2,
                doubleBtnOnclickListener = object :TrustGeneralPurposePopupwindow.
                PopupOnclickListener.DoubleBtnOnclickListener{
            override fun onClickListener(isBtn1: Boolean) {
                if (!isBtn1) {
                hideView(activity_map_bsa_bg,bsStatus)
                bsStatus = !bsStatus
                MAP_TYPE = if(bsStatus) MAP_TYPE_BS else MAP_TYPE_GPS
                getDbManger().setMapType(bikeId,MAP_TYPE)
                    dataAnalyCenter().sendLocalData(APP_CHOOSE_MAP_BS_STATUS,MAP_TYPE.toString())
                }
            }
        })
    }


    private fun checkOffinMap(cityCode:String,isShow: Boolean){

        val checkCityIsDownload = TrustGaoDeMapsTool.getOfflineMapTool(this,aMap!!)
                .checkCityIsDownload(supportFragmentManager, cityCode,isShow,listener = listener)
        if (!checkCityIsDownload) {
            mIsShow = false
            showNoMap()
        }else{
            map_offinemap_download_btn.visibility = View.GONE
        }
        TrustLogUtils.d(TAG,"isShow : $isShow  checkCityIsDownload : $checkCityIsDownload")
    }

    private val listener = object : MapDialogCallBack.OffinePopuCallBack{
        override fun callback(isFirst: Boolean) {
            if (isFirst) {
                showNoMap()
            }else{
                toOffineMapActivty()
            }
        }
    }

    private fun showNoMap(){
        map_offinemap_download_btn.visibility = View.GONE
    }

    private fun toOffineMapActivty(){
        TrustGaoDeMapsTool.getOfflineMapTool(this,aMap!!).startOfflineActivity(this)
    }

    override fun resultCarModule(str: String,module:Int?) {
        changeBikeStatusUi(module)
    }


    private fun changeBikeStatusUi(module:Int?){
        TrustLogUtils.d(TAG,"module:$module")
        val mode = if (module != null ) {
            module
        }else{
            SECUTITY_ALERT
        }
        var tile = ""
        var msg = ""
        var isChecked = true
        var ic = R.drawable.map_bike_status_alert_ic
        when (mode) {
            SECUTITY_ALERT -> {
                tile = getString(R.string.item_security_settings_status_tx)
                msg = getString(R.string.item_security_settings_status_msg_tx)
                ic = R.drawable.map_bike_status_alert_ic
                isChecked = true
            }
            SECUTITY_DO_NOT_DISTURB -> {
                tile = getString(R.string.itme_security_settings_do_not_disturb_status_tx)
                msg = getString(R.string.itme_security_settings_do_not_disturb_status_msg_tx)
                ic = R.drawable.map_bike_status_no_linsen_ic
                isChecked = false
            }
        }

        map_bike_status_ic.setImageResource(ic)
        map_bike_status_title_tv.text = tile
        map_bike_status_msg_tv.text = msg
        map_bike_status_controll_btn.isChecked = isChecked
    }


    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!,bean.state_info,this)) {
            getPresenter()?.sendCarInfo()
        }else{
            val checked = map_bike_status_controll_btn.isChecked
            map_bike_status_controll_btn.isChecked = !checked
        }
    }


    override fun resultFindCar(actionType: Int, msg: Int?, str: String?, isSuccess: Boolean?) {
        showToast(str)
    }
    override fun resultLcoation(bean: CarLoctionBean?) {

    }
}
