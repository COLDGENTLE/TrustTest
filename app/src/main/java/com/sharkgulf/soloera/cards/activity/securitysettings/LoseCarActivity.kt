package com.sharkgulf.soloera.cards.activity.securitysettings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.trust.demo.basis.base.TrustMVPActivtiy
import kotlinx.android.synthetic.main.activity_lose_car.*
import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.animation.LinearInterpolator
import com.sharkgulf.soloera.*
import com.sharkgulf.soloera.TrustAppConfig.bikeId
import com.sharkgulf.soloera.module.bean.BsLostModeBean
import com.sharkgulf.soloera.module.bean.BsSecuritySettingsBean
import com.sharkgulf.soloera.module.bean.BsTestNotifyBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.securitysettings.ISecuritySettingsView
import com.sharkgulf.soloera.presenter.securitysettings.ISecuritySettingsPresenters
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.animation.TrustAnimation
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.config.setTextStrings
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.statusbarlibrary.TrustStatusBarUtils
import kotlinx.android.synthetic.main.tite_layout.*


class LoseCarActivity : TrustMVPActivtiy<ISecuritySettingsView, ISecuritySettingsPresenters>(), ISecuritySettingsView {
    override fun resultFilter() {}


    private val  TAG = "LoseCarActivity"
    private var isOpenLoseCar = false
    private var mStatus = TYPE_LOSE_BIKE
    override fun getLayoutId(): Int {
        return R.layout.activity_lose_car
    }

    override fun initView(savedInstanceState: Bundle?) {
        title_tx.text = setTextStrings(R.string.security_settings_alert_title_tx)

        baseSetOnClick(lose_car_submint_btn)
        baseSetOnClick(title_back_btn)

        mStatus = intent.getIntExtra(TYPE_BIKE_KEY, DEFULT)
        changeStatusUi(mStatus)
    }

    override fun initData() {
        changeColor(isOpenLoseCar)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.title_back_btn -> {
                finish()
            }
            R.id.lose_car_submint_btn -> {
                changeUi()
                changeStatusUi(TYPE_LOSE_BIKEING)
                getPresenter()!!.lostModu(RequestConfig.lostModu(bikeId,false))
            }
            else -> {
            }
        }
    }

    private fun changeColor(status:Boolean) {
        val colorList = if (status) {
            intArrayOf(Color.parseColor("#ff5a00"),
                    Color.parseColor("#ff7a00"))
        }else{
            intArrayOf(Color.parseColor("#0072ff"),
                    Color.parseColor("#00c3ff"))
        }
        lose_car_logo_layout.setGradients(colorList)
        TrustStatusBarUtils.getSingleton(this).setStatusBarColor(this, colorList[0])
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

    override fun createPresenter(): ISecuritySettingsPresenters {
        return ISecuritySettingsPresenters()
    }

    private fun changeUi(){
        changeColor(isOpenLoseCar)
        changeStatus(isOpenLoseCar)

        val animator = ObjectAnimator.ofFloat(test_animor, "alpha", 1f, 1f, 1f)
        animator.duration = 1000
        animator.interpolator = LinearInterpolator()


        animator.addUpdateListener { animation ->
            TrustLogUtils.d(TAG," animation?.animatedValue : ${ animation?.animatedValue}")

            if((animation?.animatedValue as Float) < 0.1f){
                changeColor(isOpenLoseCar)
                changeStatus(isOpenLoseCar)
            }
        }
        animator.start()
    }


    /**
     * true  正在请求
     * false 默认
     */
    private fun changeStatus(status:Boolean){
        if (status) {
            lose_car_waiting_ic.visibility = View.VISIBLE
            TrustAnimation
                    .getTrustAnimation()
                    .addRotateAnimation()
                    .startAnimation(lose_car_waiting_ic)
        }else{
            TrustAnimation
                    .getTrustAnimation().cancelAnimation()
            lose_car_waiting_ic.visibility = View.INVISIBLE
        }
    }

    private fun changeText(stringId: Int){

    }

    private fun changeStatusUi(status :Int? = null){
        if (status != null) {
            mStatus = status
        }
        when (mStatus) {
            TYPE_LOSE_BIKE -> {
                changeTx(R.string.lose_bike_tag,
                        R.string.lose_bike_status_tag,
                        R.string.lose_bike_msg,
                        R.string.lose_bike_submint_tx)
            }
            TYPE_LOSE_BIKEING -> {
                changeTx(R.string.lose_bike_taging,
                        R.string.lose_bike_status_tag,
                        R.string.lose_bike_msg,
                        R.string.lose_bike_submint_tx,
                        isSubmintHide = true)
            }
            TYPE_LOSE_BIKE_SUCCESS -> {
                changeTx(R.string.lose_bike_tag_success,
                        R.string.lose_bike_status_tag,
                        R.string.lose_bike_msg,
                        R.string.lose_bike_lose_success_submint_tx,
                        isSubmintHide = false)
            }
            TYPE_LOCK_BIKE -> {

            }
        }
    }


    private fun changeTx(tagTx:Int,statusTagTx:Int,msgTx:Int,submint:Int,tagIc:Int? = null,isSubmintHide:Boolean = false){
        setTextStrings(lose_car_tag_tx, tagTx)
        setTextStrings(lose_car_status_tag_tx, statusTagTx)
        setTextStrings(lose_car_msg_tx, msgTx)
        if (isSubmintHide) {
            lose_car_submint_btn.visibility = View.INVISIBLE
        }else{
            lose_car_submint_btn.visibility = View.VISIBLE
        }
        setTextStrings(lose_car_submint_btn, submint)
        if (tagIc != null) {
            lose_car_tag_ic.setImageResource(tagIc)
        }
    }


    override fun resultLostModu(bean: BsLostModeBean?) {
        if(BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)){
            changeStatusUi(TYPE_LOSE_BIKE_SUCCESS)
        }
    }

    override fun resultChangeSecurity(bean: BsSecuritySettingsBean?) {}

    override fun resultTestNotify(bean: BsTestNotifyBean?) {}
    override fun resultCarModule(str: String, module: Int?, bean: CarInfoBean?) {}

}
