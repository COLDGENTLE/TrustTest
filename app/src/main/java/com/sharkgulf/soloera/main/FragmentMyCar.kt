package com.sharkgulf.soloera.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.transition.Explode
import android.view.*
import com.dn.tim.lib_permission.annotation.PermissionCanceled
import com.dn.tim.lib_permission.annotation.PermissionDenied
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.cards.activity.map.MapActivity
import com.sharkgulf.soloera.cards.activity.battery.BatteryActivity
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.home.MainRecyclerAdapter
import com.sharkgulf.soloera.home.cars.CarsAdapter
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.home.IHomeView
import com.sharkgulf.soloera.presenter.home.HomePresenter
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.retrofit.config.ProjectInit
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.LinearLayout
import com.dn.tim.lib_permission.annotation.Permission
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.cards.activity.alert.AlertListActivity
import com.sharkgulf.soloera.cards.activity.securitysettings.SecuritySettingsActivity
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.home.fragment.FragmentHomeControlCard
import com.sharkgulf.soloera.loging.LogingActivity
import com.sharkgulf.soloera.main.MainHomeActivity.Companion.TO_ADD_CAR_REQUEST_CEODE
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.*
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_LOGING
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustBasePopuwindow
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.trust.TrustTools
import razerdp.basepopup.BasePopupWindow


/**
 *  Created by user on 2019/5/7
 */
class FragmentMyCar : TrustMVPFragment<IHomeView, HomePresenter>(), IHomeView {

    private var userStatus: Boolean = false
    private var isUpdate = true
    private var mFragmentListener: MainHomeActivity.onFragmentJumpListener? = null
    private val TAG = FragmentMyCar::class.java.canonicalName!!
    private val REGISTER_CAR_INFO_TOPIC = WEB_SOKECT_RECEIVE + WEB_SOKECT_CAR_INFO
    private var checkUserLoginStatus: DbUserLoginStatusBean? = null
    private var carsBeanList: BsGetCarInfoBean.DataBean? = BsGetCarInfoBean.DataBean()
    private val carsAdapter = CarsAdapter()
    private var popupGravity: BasePopupWindow? = null
    private var mCarBean: BsGetCarInfoBean.DataBean.BikesBean? = null
    private var myCarStatus = DEFULT
    private var myCarBikeId = DEFULT
    private val mBsVideoViewManger = BsVideoViewManger.getInstance()
    private var mBidChoosePos :Int? = null
    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        TrustLogUtils.d(msg + Thread().name)
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun getLayoutId(): Int { return R.layout.activity_main }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        carsAdapter.setAdapterType(false)
        mActivity?.window?.returnTransition = Explode().setDuration(1000)
        mActivity?.window?.returnTransition = Explode().setDuration(1000)
        //设置蓝牙自动重新连接
//        setIsAutoConnect(true)
        mainRecyclerAdapter = MainRecyclerAdapter(mContext)
        mainRecyclerAdapter!!.setOnItemClickListener(object : MainRecyclerAdapter.OnItemClickListener {
            override fun onItemClickListener(cardType: Int) {
                when (cardType) {
                    BS_MAP_CARD -> {//地图
                        val intent = Intent(mContext, MapActivity::class.java)
                        intent.putExtra("carBean", mCarBean)
                        startActivity(intent)
                    }
                    BS_CAR_HISTORY_CARD -> {//行车记录
//                        startActivity(Intent(mContext, CarHistoryActivity::class.java))
                    }
                    BS_BATTERY_CARD -> {//电池
                        if (isPowerOk) {
                            startActivity(Intent(mContext, BatteryActivity::class.java))
                        } else {
                            showPowerNoPop()
                        }
                    }
                    BS_SECURITY_SETTINGS_CARD -> {
                        startActivity(Intent(mContext, SecuritySettingsActivity::class.java))
                    }
                }
                initType()
            }
        })
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        activity_main_recycler.layoutManager = linearLayoutManager
        activity_main_recycler.adapter = mainRecyclerAdapter

        val defaultItemAnimator = DefaultItemAnimator()
        defaultItemAnimator.isRunning { mainRecyclerAdapter!!.notifyDataSetChanged() }
        defaultItemAnimator.addDuration = 50
        defaultItemAnimator.isRunning { }
        defaultItemAnimator.removeDuration = 50
        activity_main_recycler.itemAnimator = defaultItemAnimator


        baseSetOnClick(fragment_main_message_btn)

        baseSetOnClick(main_car_settings)
        baseSetOnClick(home_ble_im)
        baseSetOnClick(no_car_submint_btn)
        baseSetOnClick(no_car_demo_btn)
        baseSetOnClick(main_black_demo_btn)
        baseSetOnClick(no_car_settings)
        baseSetOnClick(text_btn_test)
//        baseSetOnClick(bs_my_car_no_login_layout)
        setBleCallBack()
        carsAdapter.setList(carsBeanList)
        carsAdapter.setListener(object : CarsAdapter.CarsAdapterItemListener {
            override fun startaCarInfo(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {}

            override fun delCars(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {}

            override fun toAddCars() {
                popupGravity?.dismiss()
                val intent = Intent(mActivity, BindCarActivity::class.java)
                mActivity!!.startActivityForResult(intent, TO_ADD_CAR_REQUEST_CEODE)
            }

            override fun chooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
                popupGravity?.dismiss()
                TrustLogUtils.d(TAG,"updateChooseCar :: ${bsCarsBean.bike_name}")
                updateChooseCar(bsCarsBean)
            }
        })


        baseSetOnClick(spinner_bikes_layout)


        dataAnalyCenter().registerCallBack(REGISTER_CAR_INFO_TOPIC, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

            override fun onNoticeCallBack(msg: String?) {
                getCarInfo()
            }
        }, TAG)


        registerUpdateCarInfo(TAG, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

            override fun onNoticeCallBack(msg: String?) {
                val carInfoData = getCarInfoData()
                if (msg != null) {

                } else {
                    //更新车辆信息
                    if (carInfoData != null) {
                        changeLocalCarInfoUi(carInfoData)
                    }
                }

            }
        })

        registerMainNoReadStatus()
        showNoBikeLayout()
        no_car_demo_btn.setOnTouchListener { v, event ->
            TrustLogUtils.d(TAG,"no_car_demo_btn_ on touch listener")
            false
        }
    }

     fun updateChooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean?) {
         if (bsCarsBean != null) {
             if (mCarBean?.bike_id != bsCarsBean.bike_id) {
                 initHomeUi()
                 mCarBean = bsCarsBean
                 bikeId = bsCarsBean.bike_id
                 TrustLogUtils.d(TAG,"UpdateBikeInfo : ${bsCarsBean.bike_name}")
                 updateCarInfo(bsCarsBean)
                 sendBikeInfo(bsCarsBean.bike_id)
                 dataAnalyCenter().setBikeId(bsCarsBean.bike_id)
                 bleStartConnection(bsCarsBean.bike_id)
                 getBikeInfo(bsCarsBean.bike_id)
                 getBikeStatus()
                 getPresenter()?.getBleInfo(RequestConfig.getBleInfo(bsCarsBean.bike_id))
                 sendUpdateCarInfo(bsCarsBean.bike_id)

             }

         }else{
             changeHomeUi()
         }

    }

    private fun showPowerNoPop() {
        getGPPopup().showOnlyBtnPopu(msgTx = getString(R.string.no_power_tx)
                , btnTx = getString(R.string.popupwindow_disagree_privacy_policy_cancel_tx)
                , listener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener {
            override fun onClickListener(view: BasePopupWindow) {
                view.dismiss()
            }
        })
    }

    private fun initType() {
        MainHomeActivity.mFragmentJumpType = DEFUTE
    }

    private fun getCarInfo(): Boolean {

        val data =  getCarInfoData(bikeId)
        if (data != null) {
            TrustLogUtils.d(TAG,"demo model changeHomeUi data.bike_id == bikeId ${data.bike_id } ${bikeId}")
            if (data.bike_id == bikeId) {
              changeHomeUi(data.status)
            }
            return true
        }
        return false
    }


    @Permission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun bleStartConnection(bike_id: Int) {
        bleDisConnection()
        TrustLogUtils.d(TAG,"连接的车辆id ： $bike_id")
        appStartBle(bike_id)
//            bleConnec(true, bleConfig.mac, bleConfig.salt)
    }

    @Permission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun bleStartConnection() {
        bleDisConnection()
        if (mCarBean != null) {
            TrustLogUtils.d(TAG,"连接的车辆id ： ${mCarBean!!.bike_id}")
            appStartBle(mCarBean!!.bike_id)
        }
//            bleConnec(true, bleConfig.mac, bleConfig.salt)
    }

    @SuppressLint("NewApi")
    override fun initData() {
        if (BsApplication.mAuthentication.getUserLoginStatus()) {
            updateFragment()
            sendBikeStatus()
        }

    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun baseResultOnClick(v: View) {
        if (v.id ==  R.id.main_car_settings || v.id == R.id.no_car_settings) { mFragmentListener?.onControllDrawLayout() }else{

            MainHomeActivity.mFragmentJumpType = DEFUTE
            when (v.id) {
                R.id.main_add_cars_btn -> {
                    startActivity(Intent(mContext, BindCarActivity::class.java))
                }
                R.id.fragment_main_message_btn -> {
                    val userCarList = getAuthentication().getUserCarList()
                    val bikes = userCarList?.bikes
                        TrustLogUtils.d(TAG,"当前是否显示 未读 隐藏")
                        fragment_read_status_ic?.visibility = View.GONE
                        val intent = Intent(mContext, AlertListActivity::class.java)
                        intent.putExtra(CHOOSE_POS_KEY, carsAdapter.getItemPos())
                        startActivity(intent)
                }
                R.id.spinner_bikes_layout -> {
                    val userCarList = getAuthentication().getUserCarList()
                    if (userCarList != null && userCarList.bikes.size >=2) {
                        showPopu()
                    }
                }

                R.id.no_car_submint_btn->{ checkUserStatus() }
                R.id.no_car_demo_btn ->{ getPresenter()?.requestDemoData() }
                R.id.text_btn_test ->{ getPresenter()?.requestDemoData() }
                R.id.main_black_demo_btn -> {
                    maxBindTime = DEFUTE.toLong()
                    initAppConfig(false,null)
                    updateFragment(true) }
                else -> { }
            }

        }
    }


    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            bsDbManger!!.setUserLoginStatus(bean)
//            changeUi()
        }
        userId = bean?.getData()?.user?.user_id!!
        changeUi()
        getBikes()
    }

    override fun resultUpdateBikesInfo() { getBikes() }
    private fun getBikes() {
        val map1 = hashMapOf<String, Any>()
        map1["user_id"] = userId
        getPresenter()!!.getCarInfo(map1)
    }

    override fun resultExt(bean: BsUserExtBean?) {}

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.state!!, bean.state_info!!, this)) {
            checkUserLoginStatus = bsDbManger?.checkUserLoginStatus()

            bean.data.bikes.sortBy { it.bind_days }

            bean.data.bikes.forEach {
                val time = TrustTools.getTime(it.binded_time!!)
                TrustLogUtils.d(TAG,"binded time : ${it.binded_time!!} | time:$time")
                if(maxBindTime < time){
                    maxBindTime = time
                }
            }

            checkUserLoginStatus?.userBikeList = bean.data
            bsDbManger!!.setUserLoginStatus(checkUserLoginStatus!!)
            mFragmentListener?.onUpdateUi()
            changeUi()
        }

    }

    /**
     * 检查车辆GPRS状态 并且换对应的ui
     */
    private fun changeHomeUi(status: Int = USER_NO_CAR) {
        val updateUi = isUpdateUi(status)
        TrustLogUtils.d(TAG,"demo model changeHomeUi ： $status ")
        TrustLogUtils.d(TAG,"demo model changeHomeUi ： USE_TYPE $USE_TYPE ")

//        if (myCarStatus != status && myCarBikeId != bikeId) {
        if (updateUi && USE_TYPE == USE_NOMOL) {
            mActivity?.runOnUiThread {
                if (status == USER_NO_CAR) {//无车
                    showNoBikeLayout()
                } else if (status == CAR_STATUS_GPRS_SUCCECC) {//车辆正常
                    TrustLogUtils.d(TAG,"demo model changeHomeUi ： showbikeNomol ")
                    TrustLogUtils.d(TAG,"bike status nomoll")
                    showbikeNomol()
                } else {//异常

                    toolbar.visibility = View.VISIBLE
                    no_car_layout.visibility = View.INVISIBLE

                    carIsOk = false
                    carStatus = CAR_STATUS_GPRS_NO_ACTIVATION
                    //车辆异常
                    activity_main_recycler?.visibility = View.VISIBLE
//                    activity_user_no_car_layout?.visibility = View.GONE
                    initSectionMainRvControlOrder()
                    mainRecyclerAdapter!!.updateList()
                    mainRecyclerAdapter!!.notifyDataSetChanged()
                    if (carsBeanList?.bikes != null && carsBeanList?.bikes!!.size > 0) {
                        if (mCarBean == null) {
                            updateCarInfo(carsBeanList?.bikes!![getBidPos(carsBeanList?.bikes!!)])
                        } else {
                            TrustLogUtils.d(TAG,"UpdateBikeInfo : ${mCarBean!!.bike_name}")
                            updateCarInfo(mCarBean!!)
                        }
                    }
                }
                sendMainHomeUpdate()
//            }
                myCarStatus = status
                myCarBikeId = bikeId
            }
        }else if(USE_TYPE == USE_DEMO){
            showbikeNomol()
            sendMainHomeUpdate()
//            }
        }


    }

    private fun showbikeNomol() {
        TrustLogUtils.d(TAG,"updata bike Success Layout")
        TrustLogUtils.d(TAG,"demo model changeHomeUi ： USE_TYPE $USE_TYPE ")
        no_car_layout.visibility = View.GONE
        toolbar.visibility = View.VISIBLE
        activity_main_recycler.visibility = View.VISIBLE
        carIsOk = true
        if (myCarStatus != CAR_STATUS_GPRS_SUCCECC) {
            //                    activity_user_no_car_layout.visibility = View.GONE
            initAllMainRvControlOrder()
            mainRecyclerAdapter!!.updateList()
            mainRecyclerAdapter!!.notifyDataSetChanged()
            if (carsBeanList?.bikes != null && carsBeanList?.bikes!!.size > 0) {
                if (mCarBean == null) {
                    updateCarInfo(carsBeanList?.bikes!![getBidPos(carsBeanList?.bikes!!)])
                } else {
                    TrustLogUtils.d(TAG, "UpdateBikeInfo : ${mCarBean!!.bike_name}")
                    updateCarInfo(mCarBean!!)
                }
            }
            myCarStatus = CAR_STATUS_GPRS_SUCCECC
            myCarBikeId = bikeId
        }
    }

    override fun resultDelCar(bean: BsDelCarBean?) {}

    override fun createPresenter(): HomePresenter { return HomePresenter() }


    @PermissionCanceled
    private fun cancel() {}


    @PermissionDenied
    private fun denied() {}


    companion object { var mainRecyclerAdapter: MainRecyclerAdapter? = null }

    override fun resultBleSign(bean: BsBleSignBean?) {
        if (bean?.getState().equals("00")) {
            bsDbManger?.setBleConfig(bikeId, bean!!)
        }
    }


    private fun changeUi() {
        mActivity!!.runOnUiThread {
            checkUserLoginStatus = bsDbManger?.checkUserLoginStatus(userPhone)
            carsBeanList = checkUserLoginStatus?.userBikeList
            carsAdapter.setList(carsBeanList)
            carsAdapter.notifyDataSetChanged()
            if (carsBeanList != null) {
                val bikes = carsBeanList!!.bikes
                if (bikes != null && bikes.isNotEmpty()) {

                    if(bikes.size >=2){
                        fragment_my_spinner.visibility = View.VISIBLE
                    }else{
                        fragment_my_spinner.visibility = View.GONE
                    }
                    cheangeBikeName(bikes[getBidPos(bikes)]?.bike_name)
                }
            }

            if (checkUserLoginStatus != null) {
                val bikes = checkUserLoginStatus?.userBikeList?.bikes
                toolbar.visibility = View.VISIBLE
                if (bikes == null || bikes?.isEmpty()!!) {
                    changeHomeUi()
                } else {
                    changeBikeConfig(bikes!![getBidPos(bikes)])
//            bindTime = 1546660870000
                    if (!getCarInfo()) {
                        TrustLogUtils.d(TAG,"demo model changeHomeUi")
                        changeHomeUi(bikes!![getBidPos(bikes)].status)
                    }
                }

            } else {
                changeHomeUi()
            }
        }

    }

    private fun cheangeBikeName(name:String?) {
        TrustLogUtils.d(TAG,"车辆名称：$name")
        if(USE_TYPE != USE_DEMO){
            main_car_info?.text = name
        }
    }

    private fun changeBikeConfig(bike: BsGetCarInfoBean.DataBean.BikesBean) {
        isDouble = bike.batt_support == BATTERY_DOUBLE
        bindTime = TrustTools.getTime(bike.binded_time!!)
    }


    private fun updateCarInfo(carBean: BsGetCarInfoBean.DataBean.BikesBean) {
        TrustLogUtils.d(TAG,"车辆名称：${carBean.bike_name}")
        bikeId = carBean.bike_id
        cheangeBikeName(carBean.bike_name)
        bikeName = carBean.bike_name
        //防止正常状态下 车辆fragment 没有初始化成导致车辆图标更新失败
        Thread(Runnable {
            Thread.sleep(500)
            mActivity?.runOnUiThread {
                val fragment = mFragmentManger[FragmentHomeControlCard::class.java.canonicalName!!]
                if (fragment != null) {
                    val fragmentHomeControlCard: FragmentHomeControlCard = fragment as FragmentHomeControlCard
                    fragmentHomeControlCard.changeCarInfo(carBean)
                }
            }
        }).start()
    }


    fun updateFragment(isUpdateFragemnt:Boolean = false) {
        TrustLogUtils.d(TAG,"当前状态 isUpdateFragemnt:${isUpdateFragemnt} USE_TYPE:$USE_TYPE isLogin:${getAuthentication().getUserLoginStatus()}")

        if(USE_TYPE == USE_NOMOL) {
            isUpdate = isUpdateFragemnt

            if (getAuthentication().getUserLoginStatus()) {
                //设置蓝牙自动重新连接
//            setIsAutoConnect(true)

                checkUserLoginStatus = getAuthentication().getUser()
                if (checkUserLoginStatus != null) {
                    carsBeanList = checkUserLoginStatus!!.userBikeList

                    carsAdapter.setList(carsBeanList)
                    carsAdapter.notifyDataSetChanged()

                    token = checkUserLoginStatus!!.userToken
                    userId = checkUserLoginStatus!!.userId
                    userPhone = checkUserLoginStatus!!.userPhone
                    ProjectInit.addToken(token)
                    getPresenter()?.updatePushId(RequestConfig.updatePushId())
                    val carBean = checkUserLoginStatus!!.userBikeList
                    val bikes = carBean?.bikes
                    if (carBean?.bikes != null) {
                        if (!bikes?.isEmpty()!!) {
                            if (mCarBean == null) {
                                mCarBean = bikes[getBidPos(bikes)]
                                bikeId = bikes!![getBidPos(bikes)].bike_id
                            }
                        }
                        val bids = IntArray(bikes.size)

                        bikes.forEachIndexed { index, bikesBean ->
                            bids[index] = bikesBean.bike_id
                        }
                        if (bids.isNotEmpty()) {
                            getPresenter()?.getAlertList(RequestConfig.alertList(bids, TrustTools.getTime(System.currentTimeMillis())))
                        }
                    }

                    BsApplication.mBsApplication?.setUserLoginStatus(checkUserLoginStatus!!.userLoginStatus)
                    changeUi()

                    val mapUser = hashMapOf<String, Any>()
                    mapUser["user_id"] = userId
                    getPresenter()?.getUserInfo(mapUser)
                    sendUpdateCarInfo()
                }
                userStatus = true
            } else {
                showNoBikeLayout()


//            activity_user_no_car_layout.visibility = View.VISIBLE
                userStatus = false

            }
        }
    }

    private val htmlOnClickListener = SizeLabel.HtmlOnClickListener { action -> arouterStartActivity(ROUTE_PATH_LOGING) }

    override fun resultPushId(bean: BsPushIdBean?) {}

    override fun onDestroy() {
        DataAnalysisCenter.getInstance().unRegisterCallBack(REGISTER_CAR_INFO_TOPIC, TAG)
        super.onDestroy()
    }

    private fun sendMainHomeUpdate() {
        dataAnalyCenter().sendLocalData(APP_MAIN_HOME_UPDATE)
    }


    fun setFragmentListener(listener: MainHomeActivity.onFragmentJumpListener) {
        mFragmentListener = listener
    }

    private fun changeLocalCarInfoUi(bean: CarInfoBean?) {
        mActivity?.runOnUiThread {
            if (bean != null) {
                TrustLogUtils.d(TAG,"车辆名称：${bean.bike_name}")
               cheangeBikeName(bean.bike_name)
                val user = getAuthentication().getUser()
                val bikeList = user?.userBikeList
                if (user != null && bikeList != null) {
                    user.userBikeList?.bikes?.forEach { it ->
                        if (it.bike_id == bean.bike_id) {
                            it?.bike_name = bean.bike_name
                        }
                    }
                    getAuthentication().setCarInfo(user.userBikeList)
                    carsAdapter.setList(bikeList)
                    carsAdapter.notifyDataSetChanged()
                }
            }


        }
    }

    private fun showPopu() {
        popupGravity = TrustBasePopuwindow.getPopuwindow(mContext!!, R.layout.popu_cars_layout, object : TrustBasePopuwindow.TrustPopuwindowOnclickListener {
            @SuppressLint("WrongConstant")
            override fun resultPopuwindowViewListener(v: View?): View {
                val carsRecycle = v!!.findViewById<RecyclerView>(R.id.pupo_recycler)
                v!!.findViewById<View>(R.id.popu_car_layout_close_btn).setOnClickListener { popupGravity?.dismiss() }
                val lp = LinearLayoutManager(mActivity)
                lp.orientation = LinearLayout.VERTICAL
                carsRecycle.layoutManager = lp
                carsRecycle.adapter = carsAdapter
                return v
            }
        }).setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE,Gravity.BOTTOM)
        val sas = popupGravity as TrustBasePopuwindow
        popupGravity?.showAnimator = sas.createAnimator(true)
        popupGravity?.dismissAnimator = sas.createAnimator(false)
        popupGravity?.showPopupWindow()
    }

    override fun resultIsRead(isRead: Boolean) {
//        dataAnalyCenter().sendLocalData(APP_MESSAGER_READ, isRead.toString())
        val viewVisibiliyu = if (isRead) {
            View.GONE
        } else {
            View.VISIBLE
        }
        TrustLogUtils.d(TAG,"当前是否显示 未读 ${isRead}")
        fragment_read_status_ic.visibility = viewVisibiliyu
    }

    private fun registerMainNoReadStatus() {
        dataAnalyCenter().registerCallBack(APP_MESSAGER_READ, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                val viewVisibiliyu = if (msg!!.toBoolean()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                TrustLogUtils.d(TAG,"当前是否显示 未读 ${msg!!.toBoolean()}")
                fragment_read_status_ic.visibility = viewVisibiliyu
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }, TAG)
    }

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}



    override fun resultBleStatus(isConnection: Boolean, isCheckPassWordSuccess: Boolean) {
        TrustLogUtils.d(BLE_TAG,"resultBleStatus : $isCheckPassWordSuccess")
        val ic = if (isCheckPassWordSuccess) { R.drawable.home_ble_enable_ic }else{ R.drawable.home_ble_disenable_ic }
        home_ble_im?.setImageResource(ic)
    }



    private fun showNoBikeLayout(){
        if (myCarStatus != USER_NO_CAR) {
            cheangeBikeName(getString(R.string.bike_demo_tx))
            no_car_layout.visibility = View.VISIBLE
            activity_main_recycler.visibility = View.GONE
            fragment_my_spinner.visibility = View.GONE
//        activity_user_no_car_layout.visibility = View.VISIBLE
            carIsOk = false


            mBsVideoViewManger.initViewAndStart(no_car_demo_video)

            val text = if (checkUser()) {//登录没车
                R.string.bind_car_tx
            }else{//未登录
                R.string.my_login_or_register__tx
            }
            no_car_submint_btn.text = getBsString(text)
        }

    }

    override fun onResume() {
        super.onResume()
        mBsVideoViewManger.resume()
    }

    override fun onPause() {
        super.onPause()
        mBsVideoViewManger.pause()
    }


    override fun resultBikeStatus(bean: BikeStatusBean.BodyBean?) {
        val ic = if (bean != null && bean.getIsOl()) {
            R.drawable.home_bike_status_connect_ic
        }else{
            R.drawable.home_bike_status_unconnect_ic
        }
        home_bike_status_ic.setImageResource(ic)
    }


    //判断是否刷新布局
    private fun isUpdateUi(status: Int):Boolean{
        if(isUpdate){
            initHomeUi()
            isUpdate = false
         return true
        }
        else if (myCarStatus != status  ) {
            return true
        }else if(myCarBikeId != bikeId){
            return true
        }
        else {
            //无车
            if (status == USER_NO_CAR) {
            } else if (status == CAR_STATUS_GPRS_SUCCECC) {
                updateShowFragmentUi()
            }//未激活
            else { }
            return false
        }
    }



    private fun checkUserStatus(){
        val checkUser = checkUser()
        if (checkUser != null && checkUser) {
            startActivity(Intent(mActivity!!,BindCarActivity::class.java))
        }else{
            startActivity(Intent(mActivity!!,LogingActivity::class.java))
        }
    }


    override fun resultDemoBikeInfo(bean: BsGetCarInfoBean?,dbBean:DbUserLoginStatusBean?) {
        spinner_bikes_layout.isClickable = false
        no_car_layout.visibility = View.GONE
        activity_main_recycler.visibility = View.VISIBLE
        if (bean != null) {
            fragment_my_spinner.visibility = View.GONE
            mBsVideoViewManger.desry()
            val bikesBean = bean?.data.bikes[0]

            demoBikeList = IntArray(bean?.data.bikes.size)
            bean?.data.bikes.forEachIndexed { index, bikesBean ->
                val time = TrustTools.getTime(bikesBean.binded_time!!)
                if(maxBindTime < time){
                    maxBindTime = time
                }
                demoBikeList[index] = bikesBean.bike_id}
            changeBikeConfig(bikesBean)
            initAppConfig(true,bikesBean.bike_id)
            changeHomeUi(1)
            cheangeBikeName(getString(R.string.bike_demo_tx))
            TrustLogUtils.d(TAG,"demo model bean != null")
        }else if (dbBean != null){
            fragment_my_spinner.visibility = View.GONE
            mBsVideoViewManger.desry()
            var bikes = dbBean.userBikeList?.bikes
            val bikesBean = bikes!![0]

            demoBikeList = IntArray(bikes.size)
            bikes.forEachIndexed { index, bikesBean ->
                val time = TrustTools.getTime(bikesBean.binded_time!!)
                if(maxBindTime < time){
                    maxBindTime = time
                }


                demoBikeList[index] = bikesBean.bike_id
            }
            changeBikeConfig(bikesBean)
            initAppConfig(true,bikesBean.bike_id)
            changeHomeUi(1)
            cheangeBikeName(getString(R.string.bike_demo_tx))
            TrustLogUtils.d(TAG,"demo model dbBean != null")
        }
        home_bike_status_ic.setImageResource(R.drawable.home_bike_status_connect_ic)
        sendUpdateCarInfo()
    }

    fun initAppConfig(isDemo:Boolean,bike_id: Int?){
        if (isDemo && bike_id != null) {
            main_black_demo_btn.visibility = View.VISIBLE
            main_car_settings.visibility = View.GONE
            USE_TYPE = USE_DEMO
            bikeId = bike_id
            getApplicetion().getWebSocket().connect(DEMO_TOKEN)
            dataAnalyCenter().setBikeId(bikeId)
        }else if (!isDemo){
            spinner_bikes_layout.isClickable = true
            main_black_demo_btn.visibility = View.GONE
            main_car_settings.visibility = View.VISIBLE
            USE_TYPE = USE_NOMOL
            bikeId = DEFULT
        }
        webSocketdissconnection()
    }

    fun getBidPos(list:List<BsGetCarInfoBean.DataBean.BikesBean>):Int{
        return if (isDemoStatus()) {
            0
        }else{
            mBidChoosePos = getAuthentication().getChooseBidPos(true,list)
            if (mBidChoosePos != null) { mBidChoosePos!! }else{ 0 }
        }

    }

    //初始化首页
    private fun initHomeUi(){
        home_bike_status_ic.setImageResource(R.drawable.home_bike_status_unconnect_ic)
        home_ble_im?.setImageResource(R.drawable.home_ble_disenable_ic)
        TrustLogUtils.d(TAG,"当前是否显示 未读 隐藏")
        fragment_read_status_ic.visibility = View.GONE
    }
}