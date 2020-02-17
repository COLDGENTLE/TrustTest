package com.sharkgulf.soloera.home.user.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Switch
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.bikeId
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.home.user.UserEditActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_cars_edit.*
import kotlinx.android.synthetic.main.tite_layout.*
/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * <pre>
 *     author: vic
 *     time  : 18-6-9
 *     desc  : ${END}
 * </pre>
 */

@Route(path = "/activity/CarsEditActivity")
class CarsEditActivity : TrustMVPActivtiy<IUser,UserPresenter>(),IUser {


    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    private val TAG = CarsEditActivity::class.java.canonicalName
    private val mCarType = arrayListOf(R.string.car_info_light_bicycle_tx
            ,R.string.car_info_light_motorcycle_tx,R.string.car_info_motorcycle_tx)
    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    private var mBikeId:Int = DEFULT

    companion object {
        fun startActivity(context:Context,carInfo:String){
            val intent = Intent(context,CarsEditActivity::class.java)
            intent.putExtra("carInfo",carInfo)
            context.startActivity(intent)
        }
    }

    override fun resultUploadfile(bean: BsUploadFileBean?) {}
//    private var mBsGetCarInfoBean:BsGetCarInfoBean.DataBean.BikesBean? = null
    private var mCarInfo:CarInfoBean? = null
    private var mBleBikeInfo: DbBleBean? = null
    private var mBleConnectionStatus = false
    override fun getLayoutId(): Int { return R.layout.activity_cars_edit }

    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = setTextStrings(R.string.car_edit_title)
        baseSetOnClick(car_edit_dele_car_btn)
        baseSetOnClick(title_back_btn)
        baseSetOnClick(car_edit_name_layout)
        baseSetOnClick(car_edit_car_num_layout)



        val listener = View.OnClickListener { v->
            v as Switch
            val checked = v.isChecked
            if (v.id == R.id.car_edit_electironic_sw) {
                if (!controllElectironicLoad(checked)) {
                    car_edit_electironic_sw.isChecked = !checked
                }
            }else{
                if (!controllCushionInduction(checked)) {
                    car_edit_cushion_induction_sw.isChecked = !checked
                }
            }

        }

        car_edit_electironic_sw.setOnClickListener(listener)
        car_edit_cushion_induction_sw.setOnClickListener(listener)
        getPresenter()?.getBleStatus()

        car_edit_electironic_sw.setOnTouchListener { view, motionEvent -> motionEvent.getAction() == MotionEvent.ACTION_MOVE }
        car_edit_cushion_induction_sw.setOnTouchListener { view, motionEvent -> motionEvent.getAction() == MotionEvent.ACTION_MOVE }
    }

    private fun controllCushionInduction(checked: Boolean) :Boolean{
        var checkBleIsOk = checkBleIsOk()
        if (checkBleIsOk) {
            if(getIsRead()){
                car_edit_cushion_induction_sw.visibility = View.GONE
                chengImageView(cushion_inducation_load_im)
                getPresenter()?.controllCushionInduction(mBikeId,"",checked)
            }else{
                checkBleIsOk = false
                showToast("点击过于频繁")
            }
        }else{
            checkBleIsOk = false
            showToast(getBsString(R.string.please_change_ble_is_use_tx))
        }
        return checkBleIsOk
    }

    private fun controllElectironicLoad(checked: Boolean) :Boolean{
        var checkBleIsOk = checkBleIsOk()
        if (checkBleIsOk) {
            if(getIsRead()){
            car_edit_electironic_sw.visibility = View.GONE
            chengImageView(electironic_load_im)
            getPresenter()?.controllElectironic(mBikeId,"",checked)
            }else{
                checkBleIsOk = false
                showToast("点击过于频繁")
            }
        }else{
            checkBleIsOk = false
            showToast(getBsString(R.string.please_change_ble_is_use_tx))
        }
        return checkBleIsOk
    }

    private fun checkBleIsOk():Boolean{
        val bleInfo = getDbBleManger().getBleInfo(mBikeId)
        return mBikeId != DEFULT && bleInfo != null && bleInfo.mac == getBleId()
    }

    override fun initData() {
//        mBsGetCarInfoBean = TrustAnalysis.resultTrustBean(intent.getStringExtra("carInfo"), BsGetCarInfoBean.DataBean.BikesBean::class.java)
        mBikeId = intent.getIntExtra(TrustAppConfig.KEY_SELECT_BIKR_ID, DEFULT)
        if(mBikeId != DEFULT){ getPresenter()?.getBikeInfo(mBikeId) }
        changeBleBikeInfo()

        registerBleBikeInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                runOnUiThread {
                    changeBleBikeInfo()
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        })

        val listener = object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) {
                runOnUiThread {
                    if (msg != null) {
                        showToast(msg)
                    }
                    car_edit_electironic_sw.visibility = View.VISIBLE
                    car_edit_cushion_induction_sw.visibility = View.VISIBLE
                    finshChangeTextDrawable(true)
                    changeBleBikeInfo()
                }
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

        }

        registerBleEletrion(TAG,listener)
        registerBleCushionInduction(TAG,listener)
        getPresenter()?.setTAG(TAG,mBikeId)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.car_edit_dele_car_btn -> {
                if (mCarInfo != null) {
                CarDeleteProtocolActivity.startActivitiy(this,mCarInfo!!.bike_id)
                }else{
                    showBsToast("请保证手机联网,并在首页切换至少一次该车")
                }
            }
            R.id.title_back_btn->{
                updateCarInfo()
                finish()
            }
            R.id.car_edit_name_layout -> { startEditActivitiy(mBikeId,5) }
            R.id.car_edit_car_num_layout -> {startEditActivitiy(mBikeId,4)}
            R.id.car_edit_electironic_sw -> { controllElectironicLoad(!car_edit_electironic_sw.isChecked)}
            R.id.car_edit_cushion_induction_sw -> { controllCushionInduction(!car_edit_cushion_induction_sw.isChecked)}
            else -> { }
        }
    }

    private fun startEditActivitiy(bid:Int,type:Int) {
        val intent = Intent(this, UserEditActivity::class.java)
        intent.putExtra("bid",bid)
        intent.putExtra("type",type)
        startActivity(intent)
    }

    private fun updateCarInfo() {
        getPresenter()?.requestUpdateCarInfo(RequestConfig.updateCarProfile(mCarInfo!!),mCarInfo!!)
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg)}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            showToast(bean.getState_info())
            BsApplication.bsDbManger!!.clearBle(bikeId)
            val carBean = BsDbManger.mDbUserLoginStatusBean?.userBikeList!!.bikes
            carBean!!.forEach {
                if (it.bike_id == bikeId) {
                    carBean.remove(it)
                }
            }
            BsDbManger.mDbUserLoginStatusBean?.userBikeList!!.bikes = carBean
            BsApplication.bsDbManger!!.setUserLoginStatus(BsDbManger.mDbUserLoginStatusBean!!)

           activityList.forEach {
               when (it) {
                   is CarInfoActivity,
                   is CarsActivity,
                   is CarsEditActivity-> {
                       it.finish()
                   }
               }
            }
        }
    }


    fun changeUi(){
        car_edit_name_tv.setText(mCarInfo?.bike_name?.toCharArray(),0,mCarInfo?.bike_name?.length!!)
        car_edit_version_tx.text = mCarInfo?.brand!!.brand_name
//        car_edit_version_tx.setText(mBsGetCarInfoBean?.bike_name?.toCharArray(),0,mBsGetCarInfoBean?.brand!!.brand_name)
        car_edit_type_tx.text = setTextStrings(mCarType[mCarInfo!!.bike_class])
        car_edit_mode_tx.setText(mCarInfo?.model?.model_name?.toCharArray(),0,mCarInfo?.model?.model_name?.length!!)
        val plate_num = mCarInfo?.plate_num
        if(plate_num!=null && plate_num != ""){
            car_edit_car_num_tv.setText(plate_num.toCharArray(),0,plate_num.length!!)
        }
//        car_edit_car_num_ed.setText(mBsGetCarInfoBean?.plate_num?.toCharArray(),0,mBsGetCarInfoBean?.plate_num?.length!!)
//        car_edit_motor_or_engine_ed.setText(mBsGetCarInfoBean?.model?.model_name?.toCharArray(),0,mBsGetCarInfoBean?.model?.model_name?.length!!)
        car_edit_vin_tx.setText(mCarInfo?.vin?.toCharArray(),0,mCarInfo?.vin?.length!!)


        if (!mBleConnectionStatus) {

        }
    }

    override fun createPresenter(): UserPresenter {
        val userPresenter = UserPresenter()
        return userPresenter
    }


    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) { }
    }

    override fun resultCarInfo(bean: CarInfoBean?) {
        mCarInfo = bean
        changeUi()
    }


    private fun changeBleBikeInfo(){
        mBleBikeInfo = getDbBleManger().getBleInfo(mBikeId)
        if (mBleBikeInfo != null) {
            car_edit_electironic_sw?.isChecked = mBleBikeInfo!!.electironic
            car_edit_cushion_induction_sw?.isChecked = mBleBikeInfo!!.cushionInduction
        }
    }

    override fun resultBleStatus(isconnection: Boolean) {
        mBleConnectionStatus = isconnection
    }
}
