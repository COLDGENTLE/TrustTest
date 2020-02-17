package com.sharkgulf.soloera.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.ble.BsBleTool
import com.sharkgulf.soloera.cards.SosServiceActivity
import com.sharkgulf.soloera.cards.activity.FindBsActivity
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.fragment_home_car_section_card.*
import kotlinx.android.synthetic.main.fragment_home_car_section_card.home_battery_sos_btn
import kotlinx.android.synthetic.main.fragment_home_car_section_card.home_find_bs_btn

/*
 *Created by Trust on 2018/12/18
 */
class FragmentHomeSectionCard : TrustMVPFragment<TrustView, TrustPresenters<TrustView>>(), TrustView {
    private val SECTION_ING = 1
    private val SECTION_SUCCESS = 2
    private val SECTION_ERROR = 3
    private val SECTION_NOTHING = 4
    private val SECTION_DISS_CONNECTION = 5
    private var sectionStatus = SECTION_ING

    private var isFirstConnection = false
    private val NO_SIM = 1
    private val SIM_EXPIRED = 2
    private val TAG = "FragmentHomeSectionCard"

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun onTrustFragmentFirstVisible() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_car_section_card
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initMap()
        submint()
        swichStatus(SECTION_ING)
        baseSetOnClick(home_find_bs_btn)
        baseSetOnClick(home_battery_sos_btn)

    }

    override fun initData() {
        setIsReGprsActivate(true)
        dataAnalyCenter().registerCallBack(TrustAppConfig.BLE_CHECK_PASS_WORD_SUCCESS, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
            override fun onNoticeCallBack(msg: String?) {
                submint()
            }
        }, TAG)
        dataAnalyCenter().registerCallBack(TrustAppConfig.BLE_CONNECT_CLOSE, object : DataAnalysisCenter.onDataAnalysisCallBack {
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
            override fun onNoticeCallBack(msg: String?) {
//                swichStatus(SECTION_DISS_CONNECTION)
            }
        }, TAG)

    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.home_find_bs_btn -> {
                startActivity(Intent(mActivity!!, FindBsActivity::class.java))
            }
            else -> {
                startActivity(Intent(mActivity!!, SosServiceActivity::class.java))
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

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object : TrustPresenters<TrustView>() {}
    }

    /**
     * 提交激活
     */
    fun submint() {
        swichStatus(SECTION_ING)
        if (!isFirstConnection) {
            isFirstConnection = true
            gprsActivate(object : BsBleTool.BsBleCallBack.BsBleConrollCallBack {
                override fun resultControllCallBack(isSuccess: Boolean, errorString: String?) {
                    if (isSuccess) {
                        swichStatus(SECTION_SUCCESS)
                    } else {
                        swichStatus(SECTION_ERROR)
                    }
                    isFirstConnection = false
                }

                override fun resultControllTimeOutCallBack() {
                    swichStatus(SECTION_NOTHING)
                    isFirstConnection = false
                }

            }, object : BsBleTool.BsBleCallBack.BsBleConrollCallBack.BsBleDissConnction {
                override fun resultDissConnection() {
                    isFirstConnection = false
//                    swichStatus(SECTION_DISS_CONNECTION)
                }
            })
        }

    }


    private fun swichStatus(status: Int) {
        sectionStatus = status
        val txtClass = map[sectionStatus]
        when (sectionStatus) {
            SECTION_ING,
            SECTION_NOTHING,
            SECTION_DISS_CONNECTION,
            SECTION_SUCCESS -> {
                changeUi(txtClass!!.titleTx, txtClass.msgTx)
            }
            SECTION_ERROR -> {
                val gprsActivateStatus = getGprsActivateStatus()
                val result = if (gprsActivateStatus == NO_SIM) {
                    setTextStrings(R.string.bike_section_title_section_error_no_sim_msg_tx)
                } else {
                    setTextStrings(R.string.bike_section_title_section_error_sim_expired_msg_tx)
                }
                changeUi(txtClass!!.titleTx, result)
            }
        }
    }

    private val map = hashMapOf<Int, TxtClass>()

    private fun initMap() {
        map[SECTION_ING] = TxtClass(setTextStrings(R.string.bike_section_title_sectioning_tx),
                setTextStrings(R.string.bike_section_title_sectioning_msg_tx), false)

        map[SECTION_SUCCESS] = TxtClass(setTextStrings(R.string.bike_section_title_sectioning_tx),
                setTextStrings(R.string.bike_section_title_sectioning_msg_tx), false)

        map[SECTION_NOTHING] = TxtClass(setTextStrings(R.string.bike_section_title_sectioning_tx),
                setTextStrings(R.string.bike_section_title_sectioning_msg_tx), false)

        map[SECTION_DISS_CONNECTION] = TxtClass(setTextStrings(R.string.bike_section_title_section_dissconnection_ble_tx),
                setTextStrings(R.string.bike_section_title_section_dissconnection_ble_msg_tx), false)

        map[SECTION_ERROR] = TxtClass(setTextStrings(R.string.bike_section_title_section_error_tx),
                setTextStrings(R.string.bike_section_title_section_dissconnection_ble_msg_tx), false)
    }

    private fun changeUi(title: String, msg: String) {
        mActivity!!.runOnUiThread {
            sectopm_card_status_tv?.text = title
            sectopm_card_tx_tv?.text = msg
        }
    }

    class TxtClass(var titleTx: String, var msgTx: String, isShowBtn: Boolean)

    override fun onDestroy() {
        dataAnalyCenter().unRegisterCallBack(TrustAppConfig.BLE_CHECK_PASS_WORD_SUCCESS, TAG)
        dataAnalyCenter().unRegisterCallBack(TrustAppConfig.BLE_CONNECT_CLOSE, TAG)
        super.onDestroy()
    }
}