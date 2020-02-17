package com.sharkgulf.soloera.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.transition.Explode
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.cards.activity.battery.BatteryActivity
import com.sharkgulf.soloera.cards.activity.history.CarHistoryActivity
import com.sharkgulf.soloera.cards.activity.map.MapActivity
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.home.cars.CarsAdapter
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.home.IHomeView
import com.sharkgulf.soloera.presenter.home.HomePresenter
import com.sharkgulf.soloera.tool.BeanUtils

import com.dn.tim.lib_permission.annotation.PermissionCanceled
import com.dn.tim.lib_permission.annotation.PermissionDenied
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.retrofit.config.ProjectInit
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : TrustMVPActivtiy<IHomeView, HomePresenter>(), IHomeView {
    override fun resultDemoBikeInfo(bean: BsGetCarInfoBean?, dbBean: DbUserLoginStatusBean?) {}

    override fun resultUpdateBikesInfo() {}


    override fun resultBikeStatus(bean: BikeStatusBean.BodyBean?) {}

    override fun resultBleStatus(isConnection: Boolean, isCheckPassWordSuccess: Boolean) {}

    override fun resultIsRead(isRead: Boolean) {}

    override fun resultPushId(bean: BsPushIdBean?) {}

    private var checkUserLoginStatus: DbUserLoginStatusBean? = null
    private var carsBeanList :BsGetCarInfoBean.DataBean = BsGetCarInfoBean.DataBean()
    private val carsAdapter = CarsAdapter()

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) {
        TrustLogUtils.d(msg+Thread().name)
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {
//        showToast(msg)
    }

    override fun resultError(msg: String) {
        TrustLogUtils.e("MainActivity error $msg")
//        showToast(msg)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView(savedInstanceState: Bundle?) {
        window.returnTransition = Explode().setDuration(1000)
        window.returnTransition = Explode().setDuration(1000)
        //设置蓝牙自动重新连接
//        setIsAutoConnect(true)
        mainRecyclerAdapter = MainRecyclerAdapter(this)
        mainRecyclerAdapter!!.setOnItemClickListener(object :MainRecyclerAdapter.OnItemClickListener{
            override fun onItemClickListener(cardType: Int) {
                when(cardType){
                    BS_MAP_CARD->{
                        val intent = Intent(this@MainActivity, MapActivity::class.java)
                        startActivity(intent)}
                    BS_CAR_HISTORY_CARD->{//行车记录
                        startActivity(Intent(this@MainActivity, CarHistoryActivity::class.java)) }
                    BS_BATTERY_CARD->{//电池
                        startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
                    }
                }
            }
        })
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        activity_main_recycler.layoutManager = linearLayoutManager
        activity_main_recycler.adapter = mainRecyclerAdapter

        val defaultItemAnimator = DefaultItemAnimator()
        defaultItemAnimator.isRunning {
            mainRecyclerAdapter!!.notifyDataSetChanged()
        }
        defaultItemAnimator.addDuration = 500
        defaultItemAnimator.removeDuration = 500
        activity_main_recycler.itemAnimator = defaultItemAnimator











        carsAdapter.setList(carsBeanList)
        carsAdapter.setListener(object :CarsAdapter.CarsAdapterItemListener{
            override fun startaCarInfo(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
            }

            override fun delCars(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
//                getPresenter()!!.delCar(hashMapOf())
            }

            override fun toAddCars() {
                startActivity(Intent(mActivity,BindCarActivity::class.java))
                finish()
            }

            override fun chooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
                showToast("点击了车辆")
            }

        })


//        var findViewById = activity_user_no_car_layout.findViewById<View>(R.id.test_refrshLayout)


    }

    override fun initData() {
        if (userPhone != "") {
            checkUserLoginStatus = bsDbManger?.checkUserLoginStatus(userPhone)
        }else{
            checkUserLoginStatus = bsDbManger?.checkUserLoginStatus()
        }
        if (checkUserLoginStatus != null) {
            token = checkUserLoginStatus!!.userToken
            userId = checkUserLoginStatus!!.userId
            userPhone = checkUserLoginStatus!!.userPhone
            ProjectInit.addToken(token)

            val carBean = checkUserLoginStatus!!.userBikeList
            if (carBean?.bikes != null) {
                if (!carBean.bikes?.isEmpty()!!) {
                    bikeId = carBean.bikes!![0].bike_id
                }
            }
//
//            val map2  = hashMapOf<String,Any>()
//            map2["token"] = token
//            getPresenter()!!.userExt(map2)

        }



        mSectionListener = sectionListener
        ble()




        val mapUser = hashMapOf<String,Any>()
        mapUser["user_id"] = userId
        getPresenter()?.getUserInfo(mapUser)
        changeUi()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun baseResultOnClick(v: View) {
        when (v.id) {

            R.id.main_add_cars_btn->{
                startActivity(Intent(this,BindCarActivity::class.java))
            }
//            R.id.main_show_car_btn->{
//                startActivity(Intent(this,ShowCarActivity::class.java))
//            }
            else -> {
            }
        }
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
            bsDbManger!!.setUserLoginStatus(bean)
//            changeUi()
        }
        changeUi()
        val map1  = hashMapOf<String,Any>()
        map1["user_id"] = userId
        getPresenter()!!.getCarInfo(map1)
    }

    override fun resultExt(bean: BsUserExtBean?) {

    }

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
       if(BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)){
                checkUserLoginStatus = bsDbManger?.checkUserLoginStatus()
                checkUserLoginStatus?.userBikeList = bean?.getData()
                bsDbManger!!.setUserLoginStatus(checkUserLoginStatus!!)
           changeUi()
       }

    }

    fun addEmergencyBatteryCard(){
       mainRecyclerAdapter?.addData(2, TrustAppConfig.BS_EMERGENCY_BATTERY_CARD)
    }

    fun removeEmergencyBatteryCard(){
        mainRecyclerAdapter?.removeData(TrustAppConfig.BS_EMERGENCY_BATTERY_CARD)
    }

    /**
     * 检查车辆GPRS状态 并且换对应的ui
     */
    private fun changeHomeUi(status: Int = USER_NO_CAR) {

    }

    override fun resultDelCar(bean: BsDelCarBean?) {
        showToast("删除车辆成功")
    }

    override fun createPresenter(): HomePresenter{
        return HomePresenter()
    }



    @PermissionCanceled
    private fun cancel() {
    }


    @PermissionDenied
    private fun denied() {
    }



    private var closeTime = 0L
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {

            if((System.currentTimeMillis() - closeTime) < 800){
                super.onKeyDown(keyCode, event)
            }else{
                closeTime = System.currentTimeMillis()
                Toast.makeText(mActivity!!,"再按一次退出！",Toast.LENGTH_LONG).show()
                false
            }

        }else{
            super.onKeyDown(keyCode, event)
        }
    }

    private var sectionListener:CarSectionListener = object :CarSectionListener{
        override fun sectionListener() {
            changeHomeUi(CAR_STATUS_GPRS_SUCCECC)
        }
    }


    companion object {
        var mSectionListener:CarSectionListener? = null
         var mainRecyclerAdapter:MainRecyclerAdapter? = null

        interface CarSectionListener{
            fun sectionListener()
        }

    }



    /**
     * 连接蓝牙
     */
    fun ble(){
        if (CONTROLL_STATUS != CONTROLL_CAR_BLE) {
            val bleConfig = BsApplication.bsDbManger!!.getBleConfig()
            if (bleConfig != null) {
//                bleStartScan(null,bleConfig,true)
            }else{
                if (bikeId!=0) {
                    val map = hashMapOf<String,Any>()
                    map["bike_id"] = bikeId
                    map["did"] = "BC5DEC61C7355A3CC29BAA4C4F0D382C"
                    getPresenter()?.getBleInfo(map)
                }
            }
        }
    }


    override fun resultBleSign(bean: BsBleSignBean?) {
        if (bean?.getState().equals("00")) {
            TrustLogUtils.d("resultBleSign"+bean?.getData()?.btsign?.bt_mac)
            bsDbManger?.setBleConfig(bikeId,bean!!)
            if (CONTROLL_STATUS == CONTROLL_CAR_INTERNET) {
                ble()
            }
        }
    }


    private fun changeUi(){
        val bean = bsDbManger?.checkUserLoginStatus(userPhone)
        if (checkUserLoginStatus != null) {
            val userBean = checkUserLoginStatus?.userBean
            if (userBean != null) {
            }
        }

        carsAdapter.setList(bean?.userBikeList!!)
        carsAdapter.notifyDataSetChanged()
        if(bean?.userBikeList?.bikes == null || bean?.userBikeList?.bikes?.isEmpty()!!){
            changeHomeUi()
        }else{
            val battSupport = bean?.userBikeList?.bikes!![0].batt_support
            isDouble = battSupport == BATTERY_DOUBLE
//            bindTime = TrustTools.getTime(bean?.userBikeList?.bikes!![0].binded_time!!)
            changeHomeUi(bean?.userBikeList?.bikes!![0].status)
        }
//        val homeCarFragment = HomeCarFragment()
//        homeCarFragment.setBikeId(bean?.userBikeList?.bikes!![0])
//        supportFragmentManager.beginTransaction().replace(R.id.activity_main_framelayout,homeCarFragment).commitAllowingStateLoss()
    }




    override fun onResume() {
        changeUi()
        super.onResume()
    }
}


