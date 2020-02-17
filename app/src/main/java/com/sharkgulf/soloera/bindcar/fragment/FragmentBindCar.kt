package com.sharkgulf.soloera.bindcar.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.appliction.BsApplication.Companion.macString
import com.sharkgulf.soloera.appliction.BsApplication.Companion.removeString
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.bind.IBindView
import com.sharkgulf.soloera.presenter.bind.BindCarPresenter
import com.sharkgulf.soloera.tool.*
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.shark.sharkbleclient.SharkBleDeviceInfo
import com.sharkgulf.soloera.bindcar.BindCarActivity.Companion.FRAGMENT_TYPE_BIND_CAR_CONNECTION
import com.sharkgulf.soloera.bindcar.adapter.SeachCarsAdapter
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.bind_car_help_layout.*
import kotlinx.android.synthetic.main.fragment_bind_car.*
import kotlinx.android.synthetic.main.wait_layout.*
import razerdp.basepopup.BasePopupWindow


/*
 *Created by Trust on 2019/1/2
 */
class FragmentBindCar :TrustMVPFragment<IBindView, BindCarPresenter>(),IBindView {


    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}
    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            initConfig()
        }
    }

    private fun initConfig() {
        mCallBack?.changeTitle(getBsString(R.string.seach_bikes_tx))
        if (mStatus == STATUS_NO_FOIND) {
            showLayout(false)
        } else {
            bleStopScanner()
            startFind()
        }
    }


    override fun onTrustFragmentFirstVisible() {}

    private val TAG = FragmentBindCar::class.java.canonicalName
    private var showDialog: DialogFragment? = null
    private var mBindStatus:Boolean = false
    private var isShow = false
    private var mCarBindBean:BsGetBindInfoBean.DataBean.BindInfoBean? = null
    override fun getLayoutId(): Int { return R.layout.fragment_bind_car }
    private var btmac:String = ""
//    private var webBannerAdapter:WebBannerAdapter? = null
    private var seachCarsAdapter: SeachCarsAdapter? = null
    val mlist = arrayListOf<BsGetBindInfoBean>()


    @SuppressLint("NewApi")
    override fun initView(view: View?, savedInstanceState: Bundle?) {
        bleSetIsReConnection(false)
        bleDisConnection()
        seachCarsAdapter = SeachCarsAdapter()
        seachCarsAdapter?.setItemOnClickListener(listener)
//        webBannerAdapter!!.setOnBannerItemClickListener {
//            position ->
////            Toast.makeText(mActivity, "点击了第  $position  项", Toast.LENGTH_SHORT).show()
//        }
        recycler.adapter = seachCarsAdapter
        val lp = LinearLayoutManager(mActivity!!)
        lp.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = lp
//        recycler.setMoveSpeed(0.5f)
        baseSetOnClick(fragment_bind_car_next_btn)
        baseSetOnClick(fragment_bind_car_search_agin_btn)
        baseSetOnClick(help_layout_reseach_layout)
        baseSetOnClick(help_layout_back_btn)
        baseSetOnClick(help_layout_reseach_btn)
    }

    override fun initData() {
        initConfig()
    }

    private var bleList = arrayListOf<SharkBleDeviceInfo>()
    private val mBleScannerCallBack = object : BsBleTool.BsBleCallBack{
        override fun resultConnectionStatus(status: Boolean) {
            mActivity?.runOnUiThread {
                if (isShow && BLE_STATUS != BLE_STATUS_DEFUT) {
                    if (status) {
                        BLE_STATUS = BLE_STATUS_PING
                        getPresenter()?.getDevicesInfo()
                    }else{
//                showToast("与车辆蓝牙连接断开，请不要远离车辆，请尝试重新添加车辆")
                        showOnlyPopu(getBsString(R.string.ble_connection_bike_error_tx))
                        showLayout(false)
                    }
                }
            }
        }


        override fun resultScannerCallBack(device: SharkBleDeviceInfo) {
//            TrustLogUtils.d(TAG,"获取到的ble : device.deviceId:${device.deviceId}  address : ${device.device.address}" )
            bleList.add(device)
            getbindCar(removeString(device.deviceId))
        }
    }
    override fun resultDeviceStatus(status: Boolean, msg: String?) {
        if (!status && msg != null) {
            showLayout(false)
            showOnlyPopu(msg)
        }else{
            if (mCarBindBean != null) {

                isShow = false
                mCallBack?.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR_CONNECTION,mac = btmac,bikeId = bikeId,carBindBean = mCarBindBean,isNewStart = true )
            }
        }

    }

    /**
     * 获取指定车辆绑定的信息
     */
    fun getbindCar(mac:String) {
        getPresenter()!!.requestGetBindInfo(RequestConfig.requestGetBindInfo(mac))
    }

    /**
     * 申请暂时锁住车辆  防止其他手机绑定
     */
//    fun bindCar(mac:String){
//        val bindCar = hashMapOf<String, Any>()
//        bindCar["bt_mac"] = mac
//        bindCar["did"] = "BC5DEC61C7355A3CC29BAA4C4F0D382C"
//        getPresenter()!!.requestBindCar(bindCar)
//    }

    fun updateBikeInfo() {
        val bike_info = hashMapOf<String, Any>()
        bike_info["token"] = token
        bike_info["bike_info"] = 54321
        getPresenter()!!.requestUpdateCarInfo(bike_info)
    }

    private var pos :Int = 0
    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.fragment_bind_car_next_btn -> {
                mCarBindBean = mlist[pos].getData()!!.bind_info!![0]
                btmac = mCarBindBean!!.btsign!!.bt_mac!!
                bikeId = mCarBindBean!!.bike_info!!.getBike_id()

                bleConection(getDbBleBean(mCarBindBean!!))
            }
            R.id.fragment_bind_car_search_agin_btn,R.id.help_layout_reseach_layout ,R.id.help_layout_reseach_btn-> {
                BLE_STATUS = BLE_STATUS_DEFUT
                bleDisConnection()
                startFind()
            }
            R.id.help_layout_back_btn -> {
                BLE_STATUS = BLE_STATUS_DEFUT
                bleDisConnection()
                isShow = false
                BindCarActivity.getBindCar().back() }
            else -> { }
        }

    }




    /**
     * 连接蓝牙
     * 鉴权
     * 给服务器上报绑定结果
     */
    fun bleConection(ble: DbBleBean) {
        showLayout(true)
        bleStopScanner()
        TrustLogUtils.d(BLE_TAG,"ble.bleId  : ${ble.bleId} ")
        if (ble.bleId != null) {
            bleConnection(null, ble.bleId!!)
            BLE_STATUS = BLE_STATUS_CONEECTION
        }else{
            showDebugToast("bleid is null")
        }
    }


    fun chooseFragment(fragment: Fragment){
//        mActivity!!.supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.activity_bind_car_frame_layout,fragment).commit()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(childFragmentManager)
        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        fragment_bind_car_wait_layout.visibility = View.GONE
        showToast(msg)
    }

    override fun createPresenter(): BindCarPresenter { return BindCarPresenter() }


    override fun resultBindCar(bean: BsBindCarBean?) {}

    override fun resultGetBleSign(bean: BsBleSignBean?) {}

    override fun resultUpdateBindStatus(bean: BsUpdateBindStatusBean?) {
        mActivity?.runOnUiThread {
            if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
                if (mBindStatus) {
                    bsDbManger!!.setUserLoginStatus(BindCarActivity.userPhone!!,true,true, token)
                    val fragmentBindCarSetCar = FragmentBindCarSetCar()
                    chooseFragment(fragmentBindCarSetCar)
                    bsDbManger!!.setBleConfig(mlist[pos].getData()!!.bind_info!![0])
                }
            }else{
                endFind()
            }
        }
    }

    override fun resultGetBindInfo(bean: BsGetBindInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this) && bean.getData()?.bind_info != null) {
            resultCarInfo(bean)
        }
    }

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {
//        showToast("resultUpdateCarInfo")
    }

    override fun onDestroy() {
        bleStopScanner()
        super.onDestroy()
    }


    //开始搜索
    private fun startFind(){
        bleStopScanner()
        mlist.clear()
        seachCarsAdapter!!.setUrlList(mlist)
        showHelpLayout(false)
        showLayout(true)
        bleSetBsBleScannerCallBack(mBleScannerCallBack)
        bleStartScanner()
        getPresenter()?.startTiming()
    }

    //结束搜索
    private fun endFind(){
        getPresenter()?.stopTiming()
        showLayout(false)
        bleStopScanner()
    }

    //显示指定布局
    private fun showLayout(isShowWaitLayout:Boolean){
        if (isShowWaitLayout) {
            TrustAnimation.getTrustAnimation().addRotateAnimation(noReset = false).startAnimation(wait_load_layout_ic)
            setBtnIsclick(false,R.drawable.no_click_btn_bg)
            fragment_bind_car_wait_layout?.visibility = View.VISIBLE
            fragment_bind_car_list_layout?.visibility = View.GONE
        }else{
            TrustAnimation.getTrustAnimation().cancelAnimation()
            getPresenter()?.stopTiming()
            setBtnIsclick(true,R.drawable.login_register_btn_bg)
            fragment_bind_car_wait_layout?.visibility = View.GONE
            fragment_bind_car_list_layout?.visibility = View.VISIBLE
        }
    }


    //从服务器返回蓝鲨车辆信息
    private fun resultCarInfo(bean: BsGetBindInfoBean){
        try {
            val bindInfoList = bean.getData()?.bind_info!!
            for (it in bindInfoList){
                btmac = it.btsign?.bt_mac!!
                val substring = macString(btmac)
                it.btsign?.bt_mac = substring

                if (mlist.isEmpty()) {
                    val bleBean = bleList.find { it.deviceId == macString(btmac) }
                    TrustLogUtils.d(TAG,"bleBean?.deviceId btmac ${macString(btmac)} ${bleBean?.deviceId}  ${bleBean?.device?.address}")
                    bean.getData()!!.bind_info!![0].bike_info?.bleId = bleBean?.deviceId
                    mlist.add(bean)
                }else{
                    if (mlist.indexOf(bean) == -1) {
                        val bleBean = bleList.find { it.deviceId == macString(btmac) }
                        TrustLogUtils.d(TAG,"bleBean?.deviceId btmac ${macString(btmac)} ${bleBean?.deviceId}  ${bleBean?.device?.address}")
                        bean.getData()!!.bind_info!![0].bike_info?.bleId = bleBean?.deviceId
                        mlist.add(bean)
                    }
                }

                showLayout(false)
                getBsString(fragment_bind_car_num_tx,R.string.seach_bike_num_tx,mlist.size.toString())
                //刷新适配器
                seachCarsAdapter!!.setUrlList(mlist)
//                webBannerAdapter!!.notifyDataSetChanged()
            }
        }catch (e:Exception){
            TrustLogUtils.d(TAG,e)
        }

    }


    fun setType(status:Int){
        isShow = true
        mStatus = status
    }
    private var mStatus:Int = STATUS_INIT

    companion object{
         var mCallBack: BindCarActivity.onFragmentCallBack? = null
         val STATUS_INIT = 1
         val STATUS_NO_FOIND = 2
         val STATUS_FOIND = 3

        fun setOnFragmentCallBack(callBack: BindCarActivity.onFragmentCallBack){
            mCallBack = callBack
        }

        val BLE_STATUS_DEFUT = DEFULT
        val BLE_STATUS_CONEECTION = 0;
        val BLE_STATUS_READ_BIKE_STATUS = 1;
        val BLE_STATUS_PING = 2;
        var BLE_STATUS = 0;
    }


    override fun showSeachBtn() {
        //show help

        showHelpLayout(true)
//        getGPPopup().showHelpPopu()
//        fragment_bind_car_list_layout?.visibility = View.VISIBLE
    }

    private fun setBtnIsclick(isClick:Boolean,backGroundResource:Int){
        fragment_bind_car_next_btn?.isClickable = isClick
        fragment_bind_car_next_btn?.setBackgroundResource(backGroundResource)
    }

    private val listener = object : SeachCarsAdapter.onSeachCarItemOnClickListener{
        override fun onClickListener(pos: Int) {
            mCarBindBean = mlist[pos].getData()!!.bind_info!![0]
            btmac = mCarBindBean!!.btsign!!.bt_mac!!
            bikeId = mCarBindBean!!.bike_info!!.getBike_id()
            bleConection(getDbBleBean(mCarBindBean!!))
        }
    }


     fun showHelpLayout(isShow:Boolean){
        showLayout(false)
        fragment_bind_car_help_layout?.visibility = if(isShow) View.VISIBLE else View.GONE
        if (isShow) {
            bleStopScanner()
        }
    }
    private fun showOnlyPopu(msg:String){

        getGPPopup().showTrustOnlyBtnPopu(getBsString(R.string.add_error_tx),msg,getBsString(R.string.see_help_tx),object :TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener{
            override fun onClickListener(view: BasePopupWindow) {
                view.dismiss()
                showHelpLayout(true)
            }
        })

//
//        getGPPopup().showTrustDoubleBtnPopu(getBsString(R.string.add_error_tx),msg,"取消",getBsString(R.string.see_help_tx),object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener{
//            override fun onClickListener(isBtn1: Boolean) {
//                if (!isBtn1) {
//                    showHelpLayout(true)
//                }
//            }
//        })
//        getGPPopup().showOnlyBtnPopu(getBsString(R.string.add_error_tx),msg, getBsString(R.string.see_help_tx),object :TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener{
//            override fun onClickListener(view: BasePopupWindow) {
//                view.dismiss()
//                showHelpLayout(true)
//            }
//        },true)
    }

    override fun onResume() {
        TrustLogUtils.d(TAG,"onResume")
        super.onResume()
    }

    override fun onPause() {
        TrustLogUtils.d(TAG,"onPause")
        super.onPause()
    }
}