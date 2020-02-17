package com.sharkgulf.soloera.cards.activity.map

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.map_unlock_car_layout.*
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/9/2
 */
class UnLockBikeFragment:TrustMVPFragment<TrustView,TrustPresenters<TrustView>>(),TrustView {
    companion object{
        val STATUS_UN_LOCK = 1
        val STATUS_LOCKING = 2
        val STATUS_LOCK_CANCEL = 3
    }
    private var mStatus = STATUS_UN_LOCK
    private var mCallBack : MapActivity.chooseFragmentCallBack? = null
    override fun getLayoutId(): Int {
        return R.layout.map_unlock_car_layout
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        when (mStatus) {
            STATUS_UN_LOCK -> {
                unlock_car_unlock_title_tx.visibility = View.VISIBLE
                unlock_car_unlock_layout.visibility = View.GONE
                unlock_car_lock_cancel_btn.visibility = View.GONE
            }
            STATUS_LOCKING -> {
                unlock_car_unlock_title_tx.visibility = View.GONE
                unlock_car_unlock_layout.visibility = View.VISIBLE
            }
            else -> {
            }
        }

        baseSetOnClick(unlock_car_unlock_title_tx)
        baseSetOnClick(unlock_car_unlock_btn)
        baseSetOnClick(unlock_car_lock_btn)
        baseSetOnClick(unlock_car_lock_cancel_btn)
    }

    override fun initData() {

    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.unlock_car_unlock_title_tx -> {
                unlock_car_unlock_title_tx.visibility = View.GONE
                unlock_car_unlock_layout.visibility = View.VISIBLE
                unlock_car_lock_btn_layout.visibility = View.VISIBLE
                unlock_car_lock_cancel_btn.visibility = View.GONE
            }
            R.id.unlock_car_unlock_btn -> {
                unlock_car_unlock_title_tx.visibility = View.VISIBLE
                unlock_car_unlock_layout.visibility = View.GONE
                unlock_car_lock_cancel_btn.visibility = View.GONE
            }
            R.id.unlock_car_lock_btn -> {
                locking()
            }
            R.id.unlock_car_lock_cancel_btn -> {
                unlock_car_unlock_title_tx.visibility = View.VISIBLE
                unlock_car_unlock_layout.visibility = View.GONE
                unlock_car_lock_cancel_btn.visibility = View.GONE
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

    /**
     * 锁车中
     */
    private fun locking(){
        unlock_car_lock_title_tx.text = "正在锁定中..."
        unlock_car_lock_msg_tx.text = "受车辆网络环境影响，该过程可能需要几分钟。此车将在车辆网络环境良好时被锁定"
        unlock_car_lock_cancel_btn.visibility = View.VISIBLE
        unlock_car_lock_btn_layout.visibility = View.GONE


        thread {
            Thread.sleep(5000)
            mActivity!!.runOnUiThread {
                mCallBack?.callBack(true,mStatus)
            }
        }
    }

    fun setCallBack(callBack: MapActivity.chooseFragmentCallBack){
        mCallBack = callBack
    }
}