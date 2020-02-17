package com.sharkgulf.soloera.home.user.checkininfo

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.setTextSpanneds
import com.sharkgulf.soloera.tool.config.setTextStrings
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import kotlinx.android.synthetic.main.activity_user_checkin_info.*
import kotlinx.android.synthetic.main.tite_layout.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.config.getAuthentication


@Route(path = "/activity/UserCheckinInfoActivity")
class UserCheckinInfoActivity :TrustMVPActivtiy<IUser,UserPresenter>(),IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}
    private val REFRESH =  1
    private val LOAD_MORE =  2
    private var mIsRefreshStatus = REFRESH
    private var mCheckinInfoAdapter:CheckinInfoAdapter? = null
    override fun initView(savedInstanceState: Bundle?) {
        baseSetOnClick(title_back_btn)
        mCheckinInfoAdapter = CheckinInfoAdapter()
        val lp = LinearLayoutManager(this)
        lp.orientation = LinearLayout.VERTICAL
        activity_user_checkin_info.layoutManager = lp
        activity_user_checkin_info.adapter = mCheckinInfoAdapter
        title_tx.text = setTextStrings(R.string.integral_info_title_tx)
        activity_user_checkin_info.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        integral_info_num_tx.text = setTextSpanneds(R.string.integral_info_num_tx, BsApplication.mAuthentication.getUserCheckinNum().toString(), 60)
        user_checkin_info_swlayout.setRefreshHeader(object : BezierRadarHeader(this){}.setEnableHorizontalDrag(true))
        user_checkin_info_swlayout.setRefreshFooter(object : BallPulseFooter(this){}.setSpinnerStyle(SpinnerStyle.Scale))
        user_checkin_info_swlayout.setOnRefreshListener {  getNewInfo() }
        user_checkin_info_swlayout.setOnLoadMoreListener { getMoreInfo()}
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_checkin_info
    }

    private var mlimit = 10


    override fun initData() {
        //getNewInfo()
        val userCarList = getAuthentication().getUserCarList()
        val bikes = userCarList?.bikes
        val bg =if (userCarList
                != null && bikes != null
                && bikes.isNotEmpty()) {
            R.drawable.user_checkin_info_user_bg
        }else{
            R.drawable.user_checkin_info_no_user_bg
        }
        checkin_info_bg.setImageResource(bg)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.title_back_btn -> {
                finish()
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
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        clearReshStatus(false)
        showToast(msg)
    }


    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {}

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {
        if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
            clearReshStatus(true)
            val details = bean.data.details
            if (details.isNotEmpty()) {
                mCheckinInfoAdapter?.setData(mIsRefreshStatus == LOAD_MORE,list = details)
            }else{
                showToast("没有更多的数据了")
            }
        }
    }

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    override fun createPresenter(): UserPresenter {
        return UserPresenter()
    }


    private fun getNewInfo(){
        mIsRefreshStatus = REFRESH
        getCheckinInfo(TrustTools.getSystemTimeDataMillisecond())
    }

    private fun getMoreInfo(){
        mIsRefreshStatus = LOAD_MORE
        getCheckinInfo()
    }

    private fun getCheckinInfo(today:Long? = null){
        val pointDetailMap = hashMapOf<String,Any>()
        pointDetailMap["limit"] = mlimit
        pointDetailMap["before"] = today ?: mCheckinInfoAdapter!!.getLostDataTime()
        getPresenter()?.pointDetail(pointDetailMap)
    }


    private fun clearReshStatus(isSuccess:Boolean){
       if(mIsRefreshStatus == REFRESH){
           user_checkin_info_swlayout.finishRefresh(isSuccess)
       }else{
           user_checkin_info_swlayout.finishLoadMore(isSuccess)
       }
    }
}
