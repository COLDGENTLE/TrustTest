package com.sharkgulf.soloera.bindcar.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.bindcar.BindCarActivity.Companion.FRAGMENT_TYPE_BIND_CAR
import com.sharkgulf.soloera.bindcar.BindCarActivity.Companion.FRAGMENT_TYPE_BIND_CAR_SET_CAR
import com.sharkgulf.soloera.bindcar.fragment.FragmentBindCar.Companion.STATUS_INIT
import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.bind.IBindView
import com.sharkgulf.soloera.presenter.bind.BindCarPresenter
import com.sharkgulf.soloera.tool.*
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import com.tuo.customview.VerificationCodeView
import kotlinx.android.synthetic.main.fragment_bind_car_connection.*
import kotlinx.android.synthetic.main.fragment_bind_set_car.*
import razerdp.basepopup.BasePopupWindow
import android.view.inputmethod.InputMethodManager




/**
 *  Created by user on 2019/8/13
 */
class FragmentBindCarConnection:TrustMVPFragment<IBindView, BindCarPresenter>(), IBindView {
    override fun resultDeviceStatus(status: Boolean, msg: String?) {}
    private var mTrustTools:TrustTools<View> = TrustTools()

    companion object{
         var mCallBack: BindCarActivity.onFragmentCallBack? = null
        fun setOnFragmentCallBack(callBack: BindCarActivity.onFragmentCallBack){
            mCallBack = callBack
        }
    }

    private var mStatus = STATUS_INIT

    override fun showSeachBtn() {}
    private var mBindInfoBean:BsGetBindInfoBean.DataBean.BindInfoBean? = null

    private var showDialog: BasePopupWindow? = null
    override fun getLayoutId(): Int { return R.layout.fragment_bind_car_connection }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(bind_car_connection_next_btn)
        baseSetOnClick(bind_car_connection_search_agin_btn)
        set_car_layout?.visibility = View.GONE
        bind_car_connection_pin_code_ed.setInputCompleteListener(inputCompleteListener)
    }

    fun setBleBean(bindInfoBean:BsGetBindInfoBean.DataBean.BindInfoBean){ mBindInfoBean = bindInfoBean }

    override fun initData() { initConfig() }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            initConfig()
        }
    }

    private fun initConfig() {
        if (mStatus == STATUS_INIT) {
            bind_car_connection_pin_code_ed.clearInputContent()
        }

    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.bind_car_connection_next_btn -> {
                sendPing()
            }
            R.id.bind_car_connection_search_agin_btn -> { finshFragment(true) }
            else -> {
            }
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
//            showDialog = TrustDialog().showDialog(childFragmentManager)
//            showDialog = getGPPopup().showWaitPopu("正在校验车辆")

        }
    }

    override fun diassDialog() { showDialog?.dismiss() }

    override fun showToast(msg: String?) {  com.sharkgulf.soloera.tool.config.showToast(msg) }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultBindCar(bean: BsBindCarBean?) {}

    override fun resultGetBleSign(bean: BsBleSignBean?) {}

    override fun resultUpdateBindStatus(bean: BsUpdateBindStatusBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!,this)) {
            bsDbManger?.setBleConfig(mBindInfoBean!!)
            mCallBack?.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR_SET_CAR,carBindBean = mBindInfoBean,isNewStart = true)
        }
    }

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultGetBindInfo(bean: BsGetBindInfoBean?) {}


    override fun createPresenter(): BindCarPresenter {
        return BindCarPresenter()
    }

    private fun sendPing(){
        val ping = bind_car_connection_pin_code_ed.inputContent
        if (ping.length != 4) {
            showToast(getBsString(R.string.ping_error_tx))
        }else{
            hideSoftKeyboard(mActivity!!, listOf(bind_car_connection_pin_code_ed))
            FragmentBindCarSetCar.setType(FragmentBindCarSetCar.STATUS_2,ping)
            FragmentBindCarSetCar.setBleBean(mBindInfoBean!!)
            mCallBack?.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR_SET_CAR,carBindBean = mBindInfoBean,isNewStart = true)
//            getGPPopup().showBleConnectionPopu(mActivity!!,bind_car_connection_pin_code_ed.height*2,ping,mBindInfoBean!!)
//            showWaitDialog("正在绑定",true,"test")
//
//            if (!checkStatus()) {
//                return
//            }
//
//            bleSendPing(ping, mBindInfoBean!!.btsign!!.salt!!, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
//                override fun resultControllTimeOutCallBack() {
//                    diassDialog()
//                    showToast("指令超时")
//                }
//
//                override fun resultControllCallBack(isSuccess: Boolean,errorString:String?) {
//                    if (isSuccess) {
//                        TrustLogUtils.d(TAG,"网络激活")
//                        diassDialog()
//                        updateBindStatus(BIND_STATUS_SUCCESS, getDbBleBean(mBindInfoBean!!))
//                        gprsActivate(object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
//                            override fun resultControllCallBack(isSuccess: Boolean,errorString:String?) {
//                                TrustLogUtils.d(TAG,"网络激活结果 :$isSuccess")
//                            }
//
//                            override fun resultControllTimeOutCallBack() {
//                                TrustLogUtils.d(TAG, "激活超时 $isSuccess")
//                                diassDialog()
//                            }
//                        }, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack.BsBleDissConnction {
//                            override fun resultDissConnection() {
//                                diassDialog()
//                                showToast("设备断开连接！")
//                            }
//                        })
//                    } else {
//                        diassDialog()
//                        val msg = if (errorString != null) {
//                            errorString
//                        }else{
//                            "车辆绑定异常!"
//                        }
//                        showError(msg)
////                        showToast(msg)
//                    }
//
//                }
//            })


//            checkPassword("0123456789ABCDEE",object :BsBleTool.BsBleCallBack.BsBleConrollCallBack{
//                override fun resultControllCallBack(isSuccess: Boolean) {
//                    showToast("蓝牙檢查結果 $isSuccess")
//                }
//
//                override fun resultControllTimeOutCallBack() {
//                    showToast("蓝牙指令超时")
//                }
//
//            })
        }
    }


    private fun finshFragment(isSearchAgain:Boolean){
        mCallBack?.fragmentCallBack(FRAGMENT_TYPE_BIND_CAR,if(isSearchAgain) FragmentBindCar.STATUS_FOIND else FragmentBindCar.STATUS_NO_FOIND)
    }




    fun updateBindStatus(bindStatus:Int,bleBean: DbBleBean) {
        getPresenter()!!.requestUpdateBindStatus(RequestConfig.updateBindStatus(bindStatus,bleBean))
    }

    private fun checkStatus():Boolean{
        var msg:Int? = null
        val result = when (bleReadDeviceStatus()) {
            BIKE_STATUS_DEFUTE -> {
                msg =R.string.no_bind_tx
                false
            }
            BIKE_STATUS_ADDED -> {
                msg = R.string.bind_tx
                false
            }
            BIKE_STATUS_ADDING -> {
                msg = R.string.bind_ing_tx
                false
            }
            BIKE_STATUS_READY_ADD -> {
                true
            }
            else -> {
                false
            }
        }
        if (!result && msg != null) {
            showError(getBsString(msg))
        }

        return result
    }

    private fun showError(msg:String){
        diassDialog()
        getGPPopup().showOnlyBtnPopu(msgTx = setTextStrings(R.string.add_car_error_tx,msg),btnTx = getBsString(R.string.add_car_tx),listener = object :TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener{
            override fun onClickListener(view: BasePopupWindow) {
                view.dismiss()
                mCallBack?.fragmentCallBack(-1)
            }
        })
    }

    private val inputCompleteListener =  object : VerificationCodeView.InputCompleteListener {
        override fun deleteContent() {

        }

        override fun inputComplete() {
            val inputContent = bind_car_connection_pin_code_ed.inputContent
            if (inputContent.length >= 4) {
                sendPing()
            }
        }
    }


    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    fun hideSoftKeyboard(context: Context, viewList: List<View>?) {
        if (viewList == null) return

        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        for (v in viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}