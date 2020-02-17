package com.sharkgulf.soloera.cards.activity.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.map_lock_car_layout.*
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/9/2
 */
class LockBikeFragment:TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView {
    companion object{
        val STATUS_LOCK = 1
        val STATUS_UNLOCK_ING = 2
        val STATUS_UNLOCK_CANCEL = 3
        val IS_LOSE_BIKE :Boolean = false
    }

    private var mStatus:Int = STATUS_LOCK
    private var mCallBack: MapActivity.chooseFragmentCallBack? = null
    override fun getLayoutId(): Int {
        return R.layout.map_lock_car_layout
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        when (mStatus) {
            STATUS_LOCK -> {
                lock_car_lock_btn_layout.visibility = View.VISIBLE
                lock_car_unlock_cancel_btn.visibility = View.GONE

            }
            STATUS_UNLOCK_ING -> {
                lock_car_lock_btn_layout.visibility = View.GONE
                lock_car_unlock_cancel_btn.visibility = View.VISIBLE
            }
        }
        baseSetOnClick(lock_car_unlock_btn)
        baseSetOnClick(lock_car_lose_btn)
        baseSetOnClick(lock_car_open_voice_btn)
    }

    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.lock_car_unlock_btn -> {
                unLock()
            }
            R.id.lock_car_lose_btn -> {
            }
            R.id.lock_car_open_voice_btn -> {
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

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }


    fun setStatus(status:Int){
        mStatus = status
    }

    fun setCallBack(callBack: MapActivity.chooseFragmentCallBack){
        mCallBack = callBack
    }

    private fun unLock(){
        lock_car_lock_btn_layout.visibility = View.GONE
        lock_car_unlock_cancel_btn.visibility = View.VISIBLE
        lock_car_lock_title_tx.text = "正在解除此车的锁定……"
        lock_car_lock_msg_tx.text = "受车辆网络环境影响，该过程可能需要几分钟此车将在车辆网络环境良好时解除锁定"

        thread {

            Thread.sleep(3000)
            mActivity!!.runOnUiThread {
                mCallBack?.callBack(false,mStatus)
            }
        }
    }
}