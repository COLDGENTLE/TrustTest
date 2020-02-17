package com.sharkgulf.soloera.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.model.LatLng
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.BsGetCarLocationBean
import com.sharkgulf.soloera.mvpview.map.IMapView
import com.sharkgulf.soloera.presenter.map.MapPresenter
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.sharkgulf.soloera.cards.activity.map.MapActivity
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_home_map_card.*

/*
 *Created by Trust on 2018/12/18
 *
 */
class FragmentHomeMapCard : TrustMVPFragment<IMapView,MapPresenter>(), IMapView {


    override fun resultFindCar(actionType: Int, msg: Int?, str: String?, isSuccess: Boolean?) {}

    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private var mMap: AMap? = null
    private val TAG = FragmentHomeMapCard::class.java.canonicalName!!
    private val REGISTERN_TOPIC = WEB_SOKECT_RECEIVE+WEB_SOKECT_CAR_LOCTION
    private val SEND_TOPIC = WEB_SOKECT_SEND+WEB_SOKECT_CAR_LOCTION
    private var gpsStatusIc = arrayListOf(R.drawable.home_map_no_gps_ic,
            R.drawable.gps_status_1,R.drawable.gps_status_2,R.drawable.gps_status_3,R.drawable.gps_status_4)
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_map_card
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {

        map.onCreate(savedInstanceState)
        if (mMap == null) { mMap = map.map }
        mMap!!.uiSettings.setAllGesturesEnabled(false)
        mMap!!.uiSettings.isZoomControlsEnabled = false
        mMap!!.uiSettings.logoPosition =  AMapOptions.LOGO_POSITION_BOTTOM_RIGHT//

//        mMap!!.mapType = AMap.MAP_TYPE_NIGHT
        registerUpdateCarInfo()
        updateFragmentUi()

    }

    private fun registerUpdateCarInfo(){
        registerUpdateCarInfo(TAG+1, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                if (msg != null) {
                    DataAnalysisCenter.getInstance().sendData(SEND_TOPIC)
                    getData()
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }
    override fun initData() {
        getData()
        DataAnalysisCenter.getInstance().sendData(SEND_TOPIC)

        dataAnalyCenter().registerCallBack(APP_CHOOSE_MAP_BS_STATUS,object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                getData()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        },TAG)
    }



    override fun baseResultOnClick(v: View) {}

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        showToast(msg)
    }


    companion object {
        var positionsBean: CarLoctionBean.BodyBean? = null
    }

    override fun resultCarInfo(bean: BsGetCarLocationBean?) {}

    fun setCarLocation(bean:CarLoctionBean.BodyBean) {
        val showMapAddress = showMapAddress(bean)
        setTextStrings(card_map_car_address_tx, R.string.car_location_tx, if (showMapAddress != "") {
            showMapAddress
        } else {
          ""
        })
    }

    override fun createPresenter(): MapPresenter {
        val mapPresenter = MapPresenter()
        mapPresenter.setTAG(TAG)
        return mapPresenter
    }

    override fun onResume() {
//        BsApplication.read(MAP_TYPE_NAME)
        MAP_TYPE = getDbManger().getMapType(bikeId)
        super.onResume()
    }



    /**
     * 检查车辆位置信息
     */
    fun choose(): MapActivity.BsCarBean {
        var bsCarBean: MapActivity.BsCarBean? = null
        val gps = positionsBean!!.gps
        val bs = positionsBean!!.bs!!
        bsCarBean = if (MAP_TYPE == MAP_TYPE_BS) {//基站开
            val ts = positionsBean?.gps!!.ts
            if(TrustTools.getSystemTimeDataSecond() - ts >= (1 * 60 * 15)){//基站
                MapActivity.BsCarBean(LatLng(bs.lat, bs.lng), bs.ts.toLong(), bs.desc!!, bs.level,
                        bs.update_desc!!.diff, bs.update_desc!!.style, bs.update_desc!!.text, "基站定位", bs.radius)
            }else{
                MapActivity.BsCarBean(LatLng(gps.lat, bs.lng), gps.ts.toLong(), gps.desc!!, gps.level,
                        gps.update_desc!!.diff, gps.update_desc!!.style, gps.update_desc!!.text, "卫星定位", null)
            }
        }else{
            MapActivity.BsCarBean(LatLng(gps.lat, gps.lng), gps.ts.toLong(), gps.desc!!, gps.level,
                    gps.update_desc!!.diff, gps.update_desc!!.style, gps.update_desc!!.text, "卫星定位", null)
        }
        return bsCarBean
    }


    private fun getData(){
        //positions
        val data = DataAnalysisCenter.getInstance().getData<CarLoctionBean>(REGISTERN_TOPIC)
        positionsBean = data?.body
        TrustLogUtils.d(TAG,"当前bikeid: $bikeId")
        if (positionsBean != null) {
            val choose = choose()
            if (choose.mlatlon.longitude != 0.0) {
                setCarLocation(positionsBean!!)
                val color = when {
                    choose.diff in (BS_SHOW_YELLOW_TIME + 1)..BS_SHOW_RED_TIME -> "#ffff00"
                    choose.diff > BS_SHOW_RED_TIME -> "#ff0000"
                    else -> "#000000"
                }
//                card_map_car_location_info_tx?.setTextColor(Color.parseColor(color))
//                card_map_car_location_info_tx?.text = "${choose.text}  ${choose.type}"
                card_map_car_location_info_tx?.text = choose.text
                val level = choose.level
                if (level ==0) {
                    home_map_car_gps_ic?.setDrawableRightColor(getBsColor(R.color.colorRead))
                }else{
                    home_map_car_gps_ic?.setDrawableRightColor(getBsColor(R.color.greenf55))
                }
                home_map_car_gps_ic?.setDrawableRight(gpsStatusIc[level])

                GdMapTool(mContext!!).setMarker(mMap!!, choose.mlatlon.latitude,choose.mlatlon.longitude, true, true, true
                        , choose.radius != null, R.drawable.map_location_car_ic, 18f,true,0f, null)
            }else{
                initLayout()
            }
        }else{
            initLayout()
        }
    }

    override fun onDestroy() {
        DataAnalysisCenter.getInstance().unRegisterCallBack(REGISTERN_TOPIC,TAG)
        super.onDestroy()
    }

    override fun resultCarModule(str: String,module:Int?) {
        TrustLogUtils.d(TAG,"home_map_car_module_tv : $str")
        var textColor = 0
        if (module != null) {
            if (module == SECUTITY_ALERT) {
                textColor = getBsColor(R.color.orangerD00)
                home_map_car_module_tv.setDrawableLeft(R.drawable.icon_alert_on_ic)
            }else{
                textColor = getBsColor(R.color.greenb5b)
                home_map_car_module_tv.setDrawableLeft(R.drawable.icon_alert_off_ic)
            }
        }
        home_map_car_module_tv.setTextColor(textColor)
        if (str != null && str !="") {
          home_map_car_module_tv.text = str
        }else{
            home_map_car_module_tv.text = getBsString(R.string.alert_is_open_tx)
        }
//        adjustTvTextSize(home_map_car_module_tv,home_map_car_module_tv.width,home_map_car_module_tv.text.toString())


    }

    fun updateFragmentUi(){
        getPresenter()?.getCarInfo()
        getData()
    }


    private fun initLayout(){
        home_map_car_gps_ic.setDrawableRight(R.drawable.home_map_no_gps_ic)
        card_map_car_address_tx.text = ""
        card_map_car_location_info_tx.text = ""
        home_map_car_module_tv.text = getBsString(R.string.alert_is_open_tx)
        mMap?.clear()
    }


    override fun resultLcoation(bean: CarLoctionBean?) {
        getData()
    }
}