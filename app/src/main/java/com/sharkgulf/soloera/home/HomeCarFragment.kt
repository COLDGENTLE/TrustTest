package com.sharkgulf.soloera.home

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.cards.activity.map.MapActivity
import com.sharkgulf.soloera.cards.activity.battery.BatteryActivity
import com.sharkgulf.soloera.cards.activity.history.CarHistoryActivity
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.mvpview.home.IHomeView
import com.sharkgulf.soloera.presenter.home.HomePresenter
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_car_home_layout.*

/**
 *  Created by user on 2019/4/2
 */
class HomeCarFragment :TrustMVPFragment<IHomeView, HomePresenter>(), IHomeView {
    override fun resultDemoBikeInfo(bean: BsGetCarInfoBean?, dbBean: DbUserLoginStatusBean?) {}

    override fun resultUpdateBikesInfo() {}

    override fun resultBikeStatus(bean: BikeStatusBean.BodyBean?) {}

    override fun resultBleStatus(isConnection: Boolean, isCheckPassWordSuccess: Boolean) {}

    override fun resultIsRead(isRead: Boolean) {}

    override fun resultPushId(bean: BsPushIdBean?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private var mBikeBean: BsGetCarInfoBean.DataBean.BikesBean? = null
    private var mainRecyclerAdapter:MainRecyclerAdapter? = null
    var y = 0f
    override fun getLayoutId(): Int {
        return R.layout.fragment_car_home_layout
    }

    fun setBikeId(bikeBean: BsGetCarInfoBean.DataBean.BikesBean){
        mBikeBean = bikeBean
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        mainRecyclerAdapter = MainRecyclerAdapter(mContext)
        mainRecyclerAdapter!!.setOnItemClickListener(object :MainRecyclerAdapter.OnItemClickListener{
            override fun onItemClickListener(cardType: Int) {
                when(cardType){
                    TrustAppConfig.BS_MAP_CARD ->{
                        val intent = Intent(mContext, MapActivity::class.java)
                        startActivity(intent)}
                    TrustAppConfig.BS_CAR_HISTORY_CARD ->{//行车记录
                        startActivity(Intent(mContext, CarHistoryActivity::class.java))
                    }
                    TrustAppConfig.BS_BATTERY_CARD ->{//电池
                        startActivity(Intent(mContext, BatteryActivity::class.java))
                    }
                }
            }
        })

        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragment__car_home_recycler.layoutManager = linearLayoutManager
        fragment__car_home_recycler.adapter = mainRecyclerAdapter

        val defaultItemAnimator = DefaultItemAnimator()
        defaultItemAnimator.isRunning {
            mainRecyclerAdapter!!.notifyDataSetChanged()
        }
        defaultItemAnimator.addDuration = 1000
        defaultItemAnimator.removeDuration = 1000
        fragment__car_home_recycler.itemAnimator = defaultItemAnimator
        fragment__car_home_recycler.setOnTouchListener { v, event ->
            TrustLogUtils.d("event y : ${event.y}")

            when (event?.action) {
                MotionEvent.ACTION_DOWN-> {
                    y = event.y
                }
                MotionEvent.ACTION_MOVE-> {
                    if(y - event.y > 50){
                        TrustLogUtils.d("滑动方向 : 上")
                    }else if(event.y - y > 50){
                        TrustLogUtils.d("滑动方向 : 下")
                    }
                }
                MotionEvent.ACTION_UP-> {
                }
            }
             false
        }
    }

    override fun initData() {
        changeHomeUi(mBikeBean?.status!!)
    }

    override fun baseResultOnClick(v: View) {
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
    }

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
    }

    override fun resultCarInfo(bean: BsGetCarInfoBean?) {
    }

    override fun resultExt(bean: BsUserExtBean?) {
    }

    override fun resultDelCar(bean: BsDelCarBean?) {
    }

    override fun resultBleSign(bean: BsBleSignBean?) {
    }

    override fun createPresenter(): HomePresenter {
        return HomePresenter()
    }


    /**
     * 检查车辆GPRS状态 并且换对应的ui
     */
    private fun changeHomeUi(status: Int) {
        if (status == CAR_STATUS_GPRS_SUCCECC){//车辆正常
            initAllMainRvControlOrder()
            mainRecyclerAdapter!!.notifyDataSetChanged()
        }else{
            //车辆需要激活
            initSectionMainRvControlOrder()
            mainRecyclerAdapter!!.notifyDataSetChanged()
        }
    }

}