package com.sharkgulf.soloera.controllcar

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.presenter.controllcar.ControllCarPresenter
import com.sharkgulf.soloera.tool.config.chengTextDrawableView
import com.sharkgulf.soloera.tool.config.finshChangeTextDrawable
import com.sharkgulf.soloera.tool.view.ControllUtils
import com.trust.demo.basis.base.TrustMVPFragment
import kotlinx.android.synthetic.main.fragment_controll_ctrl_tmpl_one.*

/**
 *  Created by user on 2019/8/21
 */
class FragmentControllCtrlTmplOne : TrustMVPFragment<IControllCarView, ControllCarPresenter>(), IControllCarView {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun getLayoutId(): Int { return R.layout.fragment_controll_ctrl_tmpl_one }
    private var mControllCarCallBack: FragmentControllCar.ControllCarCallBack? = null

    fun setControllCarCallBack(callback:FragmentControllCar.ControllCarCallBack):FragmentControllCtrlTmplOne{
        mControllCarCallBack = callback
        return this
    }

    private var mControllUtils = ControllUtils.getInstance()

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(controll_one_lock_btn)
        baseSetOnClick(controll_one_start_btn)
        baseSetOnClick(controll_one_unlock_btn)
        baseSetOnClick(controll_one_find_btn)
        baseSetOnClick(controll_one_bucket_btn)
    }

    override fun initData() {
    }

    override fun baseResultOnClick(v: View) {
        if (mControllUtils.getIsRead()) {
            when (v.id) {
                R.id.controll_one_lock_btn -> {
                    mControllCarCallBack?.carLock(ACTION_OPEN)
                    chengTextDrawableView(controll_one_lock_btn, WEB_SOKECT_CAR_ALERTON)
                    getPresenter()?.requestCarLock(ACTION_OPEN)
                }
                R.id.controll_one_start_btn -> {
                    mControllCarCallBack?.carStart(true)
                    chengTextDrawableView(controll_one_start_btn,WEB_SOKECT_CAR_START)
                    getPresenter()?.requestStartCar(ACTION_OPEN)
                }
                R.id.controll_one_unlock_btn -> {
                    mControllCarCallBack?.carLock(ACTION_CLOSE)
                    chengTextDrawableView(controll_one_unlock_btn,WEB_SOKECT_CAR_ALERTOFF)
                    getPresenter()?.requestCarLock(ACTION_CLOSE)
                }
                R.id.controll_one_find_btn -> {
                    mControllCarCallBack?.findCar(true)
                    chengTextDrawableView(controll_one_find_btn,WEB_SOKECT_CAR_FIND)
                    getPresenter()?.requestFindCar(ACTION_OPEN)
                }
                R.id.controll_one_bucket_btn -> {
                    chengTextDrawableView(controll_one_bucket_btn,WEB_SOKECT_CAR_BUCKET_OPEN)
                    getPresenter()?.requestOpenBucket(ACTION_OPEN)
//                thread {
//                    Thread.sleep(5000)
//                    mActivity?.runOnUiThread {
//                        ControllUtils.getInstance().end(true)
//                    }
//                }

                }
                else -> {
                }
         }

        }else{
            showToast(getString(R.string.filter_action_tx))
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
       com.sharkgulf.soloera.tool.config.showToast(msg)
//        Toast.makeText(mActivity!!,msg,Toast.LENGTH_LONG).show()
//        baseShowToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}

    override fun resultCarLock(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        showToast(str)
        finshChangeTextDrawable(isSuccess!!)
    }

    override fun resultStartCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        showToast(str)
        finshChangeTextDrawable(isSuccess!!)
    }

    override fun resultOpenBucket(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        showToast(str)
        finshChangeTextDrawable(isSuccess!!)
    }

    override fun resultOpenOrCloseEle(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        showToast(str)
        finshChangeTextDrawable(isSuccess!!)
    }

    override fun resultFindCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
        showToast(str)
        finshChangeTextDrawable(isSuccess!!)
    }

    override fun createPresenter(): ControllCarPresenter {
        return ControllCarPresenter()
    }



}