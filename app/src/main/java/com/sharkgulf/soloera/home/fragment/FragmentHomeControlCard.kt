package com.sharkgulf.soloera.home.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextPaint
import android.text.TextUtils
import android.util.Pair
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.controllcar.ControllCarActivity
import com.sharkgulf.soloera.controllcar.FragmentControllCar
import com.sharkgulf.soloera.controllcar.FragmentControllCtrlTmplOne
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.presenter.controllcar.ControllCarPresenter
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_home_control_card.*
import kotlinx.android.synthetic.main.fragment_home_control_card.home_double_battry_layout
import kotlinx.android.synthetic.main.item_double_battry_layout.*
import kotlinx.android.synthetic.main.item_one_battry_layout.*

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeControlCard : TrustMVPFragment<IControllCarView, ControllCarPresenter>(),IControllCarView {

    private var mCarBean: BsGetCarInfoBean.DataBean.BikesBean? = null
    private val TAG= FragmentHomeControlCard::class.java.canonicalName!!
    private var mCarInfo: CarInfoBean? = null


    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun getLayoutId(): Int { return R.layout.fragment_home_control_card }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(fragment_home_controll_btn)
        baseSetOnClick(fragment_car_card_ic)

        updateFragmentUi()
        dataAnalyCenter().registerCallBack(BLE_CHECK_PASS_WORD_SUCCESS,object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String,timeOutTopic:String?) {}
            override fun onNoticeCallBack(msg: String?) {
                changeUi()
            }
        }, TAG)
        dataAnalyCenter().registerCallBack(BLE_CONNECT_CLOSE,object : DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String,timeOutTopic:String?) {}
            override fun onNoticeCallBack(msg: String?) {
                changeUi()
            }
        }, TAG)

        registBikeStatus()
        registerUpdateCarInfo()
        val fragmentControllCtrlTmplOne = FragmentControllCtrlTmplOne()
        fragmentControllCtrlTmplOne.setControllCarCallBack(mControllCarCallBack)
        childFragmentManager.beginTransaction().replace(R.id.home_control_layout,fragmentControllCtrlTmplOne).commitAllowingStateLoss()

    }

    fun updateFragmentUi() {
        sendBattryInfo(bikeId)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {}

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        if (v.id==R.id.fragment_home_controll_btn) {
            toControll()
        }else if(v.id == R.id.fragment_car_card_ic){
//            CarInfoActivity.StartActivity(mActivity!!, TrustAnalysis.resultString(getBikeInfo(bikeId)))
        }
    }

    private fun toControll() {
//        if (isPowerOk) {
            val pair = Pair<View, String>(fragment_car_card_ic, "carLogo")
            val pair2 = Pair<View, String>(fragment_home_controll_status_layout, "carStatus")
            val intent = Intent(mActivity, ControllCarActivity::class.java)
            intent.putExtra("carBean", mCarBean)
            mActivity!!.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(mActivity, pair, pair2)
                            .toBundle()
            )
//        }else{
//            showToast(getString(R.string.please_add_power))
//        }

    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun createPresenter(): ControllCarPresenter {
        val controllCarPresenter = ControllCarPresenter()
        controllCarPresenter.setTAG(TAG)
        return controllCarPresenter
    }

     fun changeCarInfo(carBean: BsGetCarInfoBean.DataBean.BikesBean){
         getPresenter()?.getBikeInfo()
         mCarBean = carBean
         fragment_car_card_ic.glide(mCarBean!!.pic_b,false)
         changeBikeStatus()
     }

    fun changeCarUi(){
        mCarInfo = getCarInfoData()
        if (mCarInfo != null) {
            try {
                fragment_car_card_ic?.glide(mCarInfo!!.pic_b,false)
                changeBikeStatus()
            }catch (e:Exception){
                TrustLogUtils.d(TAG,e.printStackTrace().toString())
            }
        }
    }


    private fun changeUi(){
        mActivity!!.runOnUiThread {
            val color = if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {
                "#00c6ff"
            }else{
                "#000000"
            }
            home_control_card_ble_ic?.setColor(color)
        }
    }


    override fun onDestroy() {
        val instance = DataAnalysisCenter.getInstance()
        instance.unRegisterCallBack(BLE_CHECK_PASS_WORD_SUCCESS, TAG)
        instance.unRegisterCallBack(BLE_CONNECT_CLOSE, TAG)
        instance.unRegisterCallBack(WEB_SOKECT_RECEIVE+WEB_CAR_STATUS, TAG)
        super.onDestroy()
    }



    private fun registBikeStatus(){
        dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE+WEB_CAR_STATUS,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                changeBikeStatus()
            }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {} },TAG)
    }

    private fun registerUpdateCarInfo(){
        registerUpdateCarInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                if (msg != null) {
                    changeCarUi()
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })
    }


    private fun changeBikeStatus(){
        if (mCarBean != null) {
            val bean = dataAnalyCenter().getData<BikeStatusBean.BodyBean>(WEB_SOKECT_RECEIVE + WEB_CAR_STATUS, mCarBean!!.bike_id)
            if (bean != null) {
                home_control_card_acc_ic?.setIc(R.drawable.controll_status_ele,R.drawable.controll_status_unele,bean.getIsAccOn())
                home_control_card_lock_ic?.setIc(R.drawable.controll_status_lock,R.drawable.controll_status_unlock,bean.getIsLock())
            }
        }
    }

    override fun resultCarLock(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {}

    override fun resultStartCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {}

    override fun resultOpenBucket(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {}

    override fun resultOpenOrCloseEle(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {}

    override fun resultFindCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {}


    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {
        var mileage = 0

        if (!isDouble) {

            home_one_battry_layout.visibility = View.GONE
            home_double_battry_layout.visibility = View.VISIBLE
            mileage = changeBattryStatus(home_batt1_capacity_tv,mileage,home_batt1_text_tv,bean = battry1,isShowTxInfo = false)
            mileage = changeBattryStatus(home_batt2_capacity_tv,mileage,home_batt2_text_tv,bean = battry2,isShowTxInfo = false)
            home_double_mileage_tv.text
            home_double_mileage_tv.text = getMileage(mileage)
        }else{
            home_one_battry_layout.visibility = View.VISIBLE
            home_double_battry_layout.visibility = View.GONE
            mileage = changeBattryStatus(home_batt_capacity_tv,mileage,home_batt_text_tv,bean = battry1,isShowTxInfo = false)
            home_one_mileage_tv.text =getMileage(mileage)
        }

//        adjustTvTextSize(home_double_mileage_tv,home_double_mileage_tv.width,home_double_mileage_tv.text.toString())
    }


    public fun  adjustTvTextSize( tv:TextView, maxWidth:Int, text:String):Float {
        val avaiWidth = maxWidth - tv.getPaddingLeft() - tv.getPaddingRight() - 10;

        if (avaiWidth <= 0 || TextUtils.isEmpty(text)) {
            return tv.getPaint().getTextSize();
        }

        val textPaintClone =  TextPaint(tv.getPaint());
        // note that Paint text size works in px not sp
        var trySize = textPaintClone.getTextSize();

        while (textPaintClone.measureText(text) > avaiWidth) {
            trySize--;
            textPaintClone.setTextSize(trySize);
        }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        return trySize;
    }




    private val mControllCarCallBack = object : FragmentControllCar.ControllCarCallBack {
        override fun carLock(enble: Int) {}
        override fun carStart(enble: Boolean) {}
        override fun findCar(enble: Boolean) {}
        override fun carEle(enble: Boolean) {}
        override fun carBucket(enble: Boolean) {}
    }



}