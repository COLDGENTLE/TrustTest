package com.sharkgulf.soloera.controllcar

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.presenter.controllcar.ControllCarPresenter
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.fragment_controll_car.*
import kotlinx.android.synthetic.main.tite_layout.*
import java.util.*
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/3/14
 */
class FragmentControllCar : TrustMVPFragment<IControllCarView, ControllCarPresenter>(), IControllCarView {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    private var showDialog: DialogFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_controll_car
    }

    private var isBucket = false
    private var eleStatus = false
    private var lockStatus = false
    private var startStatus = false
    private var findStatus = false

    private var isSendOpenBucket = false
    private var mFragmentControllCtrlTmplOne: FragmentControllCtrlTmplOne? = null
    private var mFragmentControllCtrlTmplTwo: FragmentControllCtrlTmplTwo? = null
    private var mFragmentControllCtrlTmplThere: FragmentControllCtrlTmplThere? = null
    private var mFragmentControllCtrlTmplFour: FragmentControllCtrlTmplFour? = null
    private var mCarBean: BsGetCarInfoBean.DataBean.BikesBean? = null
    private var mCarMap: HashMap<String, Any>? = null
    private val TAG = "FragmentControllCar"
    fun setCarBean(carBean: BsGetCarInfoBean.DataBean.BikesBean) {
        mCarBean = carBean
        mCarMap = hashMapOf(Pair("bike_id", mCarBean!!.bike_id))
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        registBikeStatus()
        changeBikeStatus()
        startLottie("controll_car_bg_data.json", "images")
        test_view2.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {}

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
        title_tx.text = setTextStrings(R.string.controll_car_title_txt)
        baseSetOnClick(controll_car_lock_btn)
        baseSetOnClick(controll_car_unlock_btn)
        baseSetOnClick(controll_car_start_btn)
        baseSetOnClick(controll_car_ele_btn)
        baseSetOnClick(controll_car_find_btn)
        baseSetOnClick(controll_car_bucket_btn)
        baseSetOnClick(title_back_btn)

        controll_car_ic.glide(mCarBean?.pic_b, false, R.drawable.car_ic, true)

        DataAnalysisCenter.getInstance().registerCallBack(BLE_CHECK_PASS_WORD_SUCCESS, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

            override fun onNoticeCallBack(msg: String?) {
                changeUi()
            }
        }, TAG)
        DataAnalysisCenter.getInstance().registerCallBack(BLE_CONNECT_CLOSE, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}

            override fun onNoticeCallBack(msg: String?) {
                changeUi()
            }
        }, TAG)
//        chengeFragment(mCarBean!!.ctrl_tmpl)
        chengeFragment(mCarBean!!.ctrl_tmpl)
        changeUi()


    }

    private fun startLottie(lottieJson: String, lottleImgs: String) {
        test_view.imageAssetsFolder = lottleImgs
        test_view.useHardwareAcceleration()
        test_view.setAnimation(lottieJson)
        test_view.speed = 1F
        test_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

    @SuppressLint("NewApi")
    override fun initData() {
//        mActivity?.window?.enterTransition = Slide().setDuration(2000)
//        window.exitTransition = Slide().setDuration(2000)


        /* if (BsBleEVBHelper.bindBleStatus) {
            val bleConfig = bsDbManger!!.getBleConfig()
            if (bleConfig != null) {
                showWaitDialog("",true,"")
                bsServerBleManger!!.getBsBleEVBHelper().setBleConfig(bleConfig)
                bsServerBleManger!!.getBsBleEVBHelper().conection(object : BsServerBleManger.BsBleListener.conectionListener{
                    override fun conectionStatus(status: Boolean) {
                        diassDialog()
                        if (status) {
                            showToast("与车辆连接成功")
                        }else{
                            showToast("绑定车辆失败")
                        }
                    }
                })
            }
        }else{
            val bleConfig = bsDbManger!!.getBleConfig()
            if (bleConfig != null) {
                showWaitDialog("",true,"")
                bsServerBleManger!!.getBsBleEVBHelper().setBleConfig(bsDbManger!!.getBleConfig()!!)
                bsServerBleManger!!.getBsBleEVBHelper().conection(object : BsServerBleManger.BsBleListener.conectionListener{
                    override fun conectionStatus(status: Boolean) {
                        diassDialog()
                        if (status) {
                            showToast("与车辆连接成功")
                        }else{
                            showToast("绑定车辆失败")
                        }
                    }
                })
            }else{
                showToast("请添加车辆！")
            }
        }
*/
    }

    @SuppressLint("NewApi")
    override fun baseResultOnClick(v: View) {
        mActivity!!.finishAfterTransition()
    }


    override fun resultCarLock(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        changeBtn(controll_car_lock_btn, lockStatus, R.drawable.controll_car_unlock_down, R.drawable.controll_car_unlock)
        changeBtn(controll_car_unlock_btn, !lockStatus, R.drawable.controll_car_lock_down, R.drawable.controll_car_lock)
//        setStatus(msg!!)
    }

    override fun resultStartCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        changeBtn(controll_car_start_btn, startStatus, R.drawable.controll_car_start_down, R.drawable.controll_car_start)
//        setStatus(msg!!)
    }

    override fun resultOpenBucket(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
//        setStatus(msg!!)
        isSendOpenBucket = false
        TrustLogUtils.d(TAG, "  isSendOpenBucket ： $isSendOpenBucket")
    }

    override fun resultOpenOrCloseEle(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        var resultMsg: String = ""
        if (actionType == ACTION_OPEN) {
//            resultMsg = setTextStrings(R.string.controll_car_open_acc_txt, msg!!)
        } else {
//            resultMsg = setTextStrings(R.string.controll_car_close_acc_txt, msg!!)
        }
        changeBtn(controll_car_ele_btn, eleStatus, R.drawable.controll_car_ele_down, R.drawable.controll_car_ele)
//        setStatus(msg!!)
    }

    override fun resultFindCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        findStatus = !findStatus
        changeBtn(controll_car_find_btn, findStatus, R.drawable.controll_car_find_down, R.drawable.controll_car_find)
//        setStatus(msg!!)
    }


    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
//        if (isShow) {
//            showDialog = TrustDialog().showDialog(childFragmentManager)
//        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        baseShowToast(msg)
    }

    override fun createPresenter(): ControllCarPresenter {
        return ControllCarPresenter()
    }


    private fun hideOrShowCar(v: View, start: Float, end: Float, time: Long = 1000) {
        TrustAnimation
                .getTrustAnimation()
                .addAlphaAnimation(start, end, time)
                .startAnimation(v)
    }

    private fun changeBtn(v: ImageView, isOpen: Boolean, openIc: Int, closeIc: Int) {
//        val ic = if (isOpen) {//打开
//            openIc
//        }else{//关闭
//            closeIc
//        }
//        v.setImageResource(ic)
    }

    interface ControllCarCallBack {
        fun carLock(enble: Int)
        fun carStart(enble: Boolean)
        fun findCar(enble: Boolean)
        fun carEle(enble: Boolean)
        fun carBucket(enble: Boolean)
    }

    private val mControllCarCallBack = object : ControllCarCallBack {
        override fun carLock(enble: Int) {
            TrustLogUtils.d(TAG, " enble ： ${enble} ")
            val msg = when (enble) {
                ACTION_OPEN -> {
                    "正在设防……"
                }
                ACTION_CLOSE -> {
                    "正在撤防……"
                }
                ACTION_NO_MUSIC_CLOSE -> {
                    "正在静音撤防……"
                }
                else -> {
                    ""
                }
            }

            setStatus(msg)
            getPresenter()!!.requestCarLock(enble, mCarMap)
        }

        override fun carStart(enble: Boolean) {
            setStatus("正在启动中……")
            getPresenter()!!.requestStartCar(if (enble) ACTION_OPEN else ACTION_CLOSE, mCarMap)
        }

        override fun findCar(enble: Boolean) {
            setStatus("寻车指令下发中……")
            getPresenter()!!.requestFindCar(if (enble) ACTION_OPEN else ACTION_CLOSE, mCarMap)
        }

        override fun carEle(enble: Boolean) {
            setStatus(if (enble) "正在打开电门……" else "正在关闭电门……")
            getPresenter()!!.requestOpenOrCloseEle(if (enble) ACTION_OPEN else ACTION_CLOSE, mCarMap)
        }

        override fun carBucket(enble: Boolean) {
            isSendOpenBucket = true
            setStatus(if (enble) "正在打开座桶……" else "关坐桶中")
            getPresenter()!!.requestOpenBucket(if (enble) ACTION_OPEN else ACTION_CLOSE, mCarMap)
        }

    }

    private fun chengeFragment(type: Int) {
        TrustLogUtils.d(TAG, "mCarBean!!.ctrl_tmpl ：${mCarBean!!.ctrl_tmpl}")
        val fragment: Fragment = when (type) {
            1 -> {
                if (mFragmentControllCtrlTmplOne == null) {
                    mFragmentControllCtrlTmplOne = FragmentControllCtrlTmplOne()
                    mFragmentControllCtrlTmplOne?.setControllCarCallBack(mControllCarCallBack)
                }
                mFragmentControllCtrlTmplOne!!
            }

            2 -> {
                if (mFragmentControllCtrlTmplTwo == null) {
                    mFragmentControllCtrlTmplTwo = FragmentControllCtrlTmplTwo()
                    mFragmentControllCtrlTmplTwo?.setControllCarCallBack(mControllCarCallBack)
                }
                mFragmentControllCtrlTmplTwo!!
            }

            3 -> {
                if (mFragmentControllCtrlTmplThere == null) {
                    mFragmentControllCtrlTmplThere = FragmentControllCtrlTmplThere()
                    mFragmentControllCtrlTmplThere?.setControllCarCallBack(mControllCarCallBack)
                }
                mFragmentControllCtrlTmplThere!!
            }

            4 -> {
                if (mFragmentControllCtrlTmplFour == null) {
                    mFragmentControllCtrlTmplFour = FragmentControllCtrlTmplFour()
                    mFragmentControllCtrlTmplFour?.setControllCarCallBack(mControllCarCallBack)
                }
                mFragmentControllCtrlTmplFour!!
            }
            else -> {
                if (mFragmentControllCtrlTmplOne == null) {
                    mFragmentControllCtrlTmplOne = FragmentControllCtrlTmplOne()
                    mFragmentControllCtrlTmplOne?.setControllCarCallBack(mControllCarCallBack)
                }
                mFragmentControllCtrlTmplOne!!
            }
        }
        childFragmentManager.beginTransaction().replace(R.id.activity_controll_car_controll_layout, fragment).commit()
    }

    private fun changeUi() {
        mActivity!!.runOnUiThread {
            val color = if (CONTROLL_STATUS == CONTROLL_CAR_BLE) {
                "#00c6ff"
            } else {
                "#000000"
            }
            controll_car_ble_status_ic?.setColor(color)
        }

    }

    override fun onDestroy() {
        val instance = DataAnalysisCenter.getInstance()
        instance.unRegisterCallBack(BLE_CHECK_PASS_WORD_SUCCESS, "TAG")
        instance.unRegisterCallBack(BLE_CONNECT_CLOSE, "TAG")
        super.onDestroy()
    }


    @Synchronized
    private fun setStatus(msg: String) {
        TrustLogUtils.d(TAG, "setStatus $msg")
        mActivity?.runOnUiThread { showControllToast(controll_car_web_status_tv, msg) }
//        controll_car_web_status_tv.visibility = View.VISIBLE
//        controll_car_web_status_tv.text = msg

    }


    private fun registBikeStatus() {
        dataAnalyCenter().registerCallBack(WEB_SOKECT_RECEIVE + WEB_CAR_STATUS, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onNoticeCallBack(msg: String?) {
                TrustLogUtils.d(TAG, "msg:$msg")
                changeBikeStatus()
            }

            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        }, TAG)
    }

    private fun changeBikeStatus() {
        val bean = dataAnalyCenter().getData<BikeStatusBean.BodyBean>(WEB_SOKECT_RECEIVE + WEB_CAR_STATUS, mCarBean!!.bike_id)
        if (bean != null) {
            controll_car_acc_status_ic?.setIc(R.drawable.controll_status_ele, R.drawable.controll_status_unele, bean.getIsAccOn())
            controll_car_lock_status_ic?.setIc(R.drawable.controll_status_lock, R.drawable.controll_status_unlock, bean.getIsLock())

            chenage(bean.getIsSstatus())

        }
    }


    private fun chenage(isSstatus: Boolean) {
        TrustLogUtils.d(TAG, "isSstatus : $isSstatus  isSendOpenBucket ： $isSendOpenBucket")
        if (isSendOpenBucket && isSstatus) {

            mActivity!!.runOnUiThread {
                if (isBucket) {//关闭
                } else {//开启
                    isBucket = true
                    test_view2.speed = 1F
                    hideOrShowCar(controll_car_ic, 1.0f, 0.0f, 200)
                    test_view2.visibility = View.VISIBLE
                    test_view2.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            if (isBucket) {
                                isSendOpenBucket = false
                                test_view2.visibility = View.GONE
                                hideOrShowCar(controll_car_ic, 0.0f, 1.0f)
                                isBucket = false
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }
                    })
                    test_view2.playAnimation()
                    changeBtn(controll_car_bucket_btn, isBucket, R.drawable.controll_car_sitting_bucket_down, R.drawable.controll_car_sitting_bucket)
                }
            }
        }
    }

    private var mIsRunning = false
    @Synchronized
    private fun showControllToast(v: TextView, msg: String?) {
        if (!mIsRunning) {
            v.visibility = View.VISIBLE
            v.text = msg
            mIsRunning = true
            ChangeControllToast(v)
        } else {
            v.text = msg
        }
    }

    private fun ChangeControllToast(v: TextView?) {
        thread {
            Thread.sleep(3000)
            mIsRunning = false
            mActivity?.runOnUiThread {
                if (v != null) {
                    hideOrShowCar(v, 1.0f, 0.0f, 200)
                }
            }
        }

    }
}