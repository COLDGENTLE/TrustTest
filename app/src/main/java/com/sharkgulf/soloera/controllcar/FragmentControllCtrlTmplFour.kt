package com.sharkgulf.soloera.controllcar

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.mvpview.controllcar.IControllCarView
import com.sharkgulf.soloera.presenter.controllcar.ControllCarPresenter
import com.trust.demo.basis.base.TrustMVPFragment
import kotlinx.android.synthetic.main.fragment_controll_ctrl_tmpl_four.*

/**
 *  Created by user on 2019/8/21
 */
class FragmentControllCtrlTmplFour  : TrustMVPFragment<IControllCarView, ControllCarPresenter>(), IControllCarView  {
    override fun resultBattryInfo(isDoublePower: Boolean, battry1: BattInfoBean.BodyBean.BattBean?, battry2: BattInfoBean.BodyBean.BattBean?, emerBattBean: BattInfoBean.BodyBean.EmerBattBean?) {}

    override fun getLayoutId(): Int {
        return R.layout.fragment_controll_ctrl_tmpl_four
    }
    private var mControllCarCallBack: FragmentControllCar.ControllCarCallBack? = null
    fun setControllCarCallBack(callback:FragmentControllCar.ControllCarCallBack):FragmentControllCtrlTmplFour{
        mControllCarCallBack = callback
        return this
    }


    override fun initView(view: View?, savedInstanceState: Bundle?) {
        baseSetOnClick(controll_four_lock_btn)
        baseSetOnClick(controll_four_start_btn)
        baseSetOnClick(controll_four_unlock_btn)
        baseSetOnClick(controll_four_find_btn)
    }

    override fun initData() {}

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.controll_four_lock_btn -> {
                mControllCarCallBack?.carLock(TrustAppConfig.ACTION_OPEN)
            }
            R.id.controll_four_start_btn -> {
                mControllCarCallBack?.carStart(true)
            }
            R.id.controll_four_unlock_btn -> {
                mControllCarCallBack?.carLock(TrustAppConfig.ACTION_CLOSE)
            }
            R.id.controll_four_find_btn -> {
                mControllCarCallBack?.findCar(true)
            }

            else -> {
            }
        }
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

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun onTrustFragmentFirstVisible() {
    }

    override fun resultCarLock(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultStartCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultOpenBucket(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultOpenOrCloseEle(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun resultFindCar(actionType: Int, msg: Int?,str:String?,isSuccess:Boolean?) {
    }

    override fun createPresenter(): ControllCarPresenter {
        return ControllCarPresenter()
    }
}