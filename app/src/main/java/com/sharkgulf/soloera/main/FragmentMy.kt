package com.sharkgulf.soloera.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.dn.tim.lib_permission.annotation.Permission
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.CHECK_IN_STATUS_SUCCESS
import com.sharkgulf.soloera.TrustAppConfig.KEY_SELECT_BIKR_ID
import com.sharkgulf.soloera.bindcar.BindCarActivity
import com.sharkgulf.soloera.cards.activity.AppsActivity
import com.sharkgulf.soloera.home.cars.CarsAdapter
import com.sharkgulf.soloera.home.user.AboutBsActivity
import com.sharkgulf.soloera.home.user.UserInfoActivity
import com.sharkgulf.soloera.home.user.cars.CarsEditActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.*
import com.sharkgulf.soloera.tool.arouter.ROUTE_PATH_LOGING
import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPFragment
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_user.*



/**
 *  Created by user on 2019/5/7
 */
class FragmentMy : TrustMVPFragment<IUser,UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    private val TAG = FragmentMy::class.java.canonicalName
    private var mAdapter:CarsAdapter? = null
    private val mAuthentication = getAuthentication()
    private var mFragmentListener: MainHomeActivity.onFragmentJumpListener? = null
    private var mIsOnclick = false

    fun setFragmentListener(listener: MainHomeActivity.onFragmentJumpListener){ mFragmentListener = listener }

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun getLayoutId(): Int { return R.layout.activity_user }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
//        my_ic?.glideUserIc(R.drawable.user_img_ic,true)
        /*baseSetOnClick(user_ext_btn)*/
        baseSetOnClick(user_info_btn,1000)
        baseSetOnClick(user_cars_btn,1000)
        baseSetOnClick(user_app_version_btn,1000)
        baseSetOnClick(fragment_my_checkin_btn,1000)
        baseSetOnClick(user_app_general_settings_btn,1000)
        baseSetOnClick(fragment_my_checkin_info_btn,1000)
        baseSetOnClick(my_tourist_login_btn,1000)
        baseSetOnClick(my_tourist_login_ic_btn,1000)
        baseSetOnClick(user_apps_btn,1000)
        baseSetOnClick(user_devices_center_btn,1000)
        baseSetOnClick(user_help_btn,1000)
        baseSetOnClick(user_settings_btn,1000)
        val lp = LinearLayoutManager(mActivity!!)
        lp.orientation = LinearLayoutManager.VERTICAL
        mAdapter = CarsAdapter()
        mAdapter?.setListener(listener)
        user_devices_recycler.layoutManager = lp
        user_devices_recycler.adapter = mAdapter

        fragment_my_checkin_info_btn.setDrawableRightColor(getBsColor(R.color.gray555))
        user_devices_center_btn.setDrawableRightColor(getBsColor(R.color.blue0ff))
        user_apps_btn.setDrawableRightColor(getBsColor(R.color.gray555))
        user_settings_btn.setDrawableRightColor(getBsColor(R.color.gray555))
        user_help_btn.setDrawableRightColor(getBsColor(R.color.gray555))
    }

    override fun initData() {
        if (getAuthentication().getUserLoginStatus()) {
            updateFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun baseResultOnClick(v: View) {
        if (checkIsLogin()) {
            when (v.id) {
                /* R.id.user_ext_btn->{
                   val checkUserLoginStatus = BsApplication.bsDbManger!!.checkUserLoginStatus()
                   checkUserLoginStatus!!.userLoginStatus = false
                   BsApplication.bsDbManger!!.setUserLoginStatus(checkUserLoginStatus)
                   startActivity(Intent(this,LogingActivity::class.java))
                   finishActivityList()
               }
               else -> {
               }*/
                R.id.user_app_version_btn ->{
                    startActivity(Intent(mContext, AboutBsActivity::class.java))
                }
                R.id.user_info_btn->{
                    startActivity(Intent(mContext, UserInfoActivity::class.java))
                }
                R.id.user_cars_btn->{
//                startActivity(Intent(this, CarsActivity::class.java))
                }
                R.id.fragment_my_checkin_btn->{

                    getPresenter()?.checkinDaily()
//                getPresenter()?.pointDetail()
//                getPresenter()?.pointInFo()
                }

                R.id.user_settings_btn->{
                    arouterStartActivity("/activity/GeneralSettingsActivity")
                }

                R.id.fragment_my_checkin_info_btn ->{
                    arouterStartActivity("/activity/UserCheckinInfoActivity")
                }

                R.id.my_tourist_login_btn ,R.id.my_tourist_login_ic_btn-> { arouterStartActivity(ROUTE_PATH_LOGING) }
                R.id.user_apps_btn -> {startActivity(Intent(mActivity!!,AppsActivity::class.java))}
                R.id.user_devices_center_btn ->{ startBindActivity() }
                R.id.user_help_btn -> { showHelpPopu()}
            }
        }else{
            mFragmentListener!!.onControllDrawLayout()
        }

    }



    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {}

    override fun diassDialog() {}

    override fun showToast(msg: String?) { com.sharkgulf.soloera.tool.config.showToast(msg)}

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {}



    override fun createPresenter():UserPresenter {
        return UserPresenter()
    }

    override fun onResume() {
        changeUi()
        super.onResume()
    }

    @SuppressLint("NewApi", "StringFormatMatches")
    private fun changeUi(){
        if (mActivity != null && !mActivity!!.isFinishing) {
            val checkUserLoginStatus = mAuthentication.getUser()
            if (checkUserLoginStatus != null) {
                user_phone_tx.text = checkUserLoginStatus.userPhone
                user_name_tx.text = checkUserLoginStatus.userNickName
                adjustTvTextSize(user_name_tx,user_name_tx.width,user_name_tx.text.toString())
                if (checkUserLoginStatus.userBean?.phone_num != null) {

                    user_name_tx.text = checkUserLoginStatus.userBean?.nick_name
                    user_phone_tx.text = formatPhone(checkUserLoginStatus.userBean?.phone_num!!)
                    user_logo_img.glideUserIc(checkUserLoginStatus.userBean?.icon,true)
                    fragment_my_integral_tx.text  =  setTextStrings(R.string.integral_tx,"${mAuthentication.getUserCheckinNum()}")
                }
            }

            setTextStrings(user_version_tx, R.string.user_bs_version_tx, TrustTools.resultAppVersion(mContext))
//        user_version_tx.text = setTextStrings(R.string.user_bs_version_tx,TrustTools.resultAppVersion(mContext))
        }

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

    override fun onTrustFragmentVisibleChange(isVisible: Boolean) {}

    override fun onTrustFragmentFirstVisible() {}


    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {
         if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
             mActivity?.runOnUiThread {

                 val status = if (bean.data.today == CHECK_IN_STATUS_SUCCESS) {
                     fragment_my_checkin_btn.text = setTextStrings(R.string.user_integral_check_in_success_tx, "")
                     false
                 }else{
                     fragment_my_checkin_btn.text = setTextStrings(R.string.user_integral_check_in_tx, "")
                     true
                 }
                 fragment_my_checkin_btn.isClickable = status
                 TrustLogUtils.d(TAG,"今天是否已经签到: ${bean.data.today}  status:$status")
             }

         }
    }

    override fun resultPointInFo(bean: BsPointinfoBean?) {
        if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
            mAuthentication.setUserCheckinNum(bean.data!!.info.total)
            changeUi()
        }
    }

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {
        if (BeanUtils.checkSuccess(bean!!.state,bean.state_info,this)) {
            fragment_my_checkin_btn.text = setTextStrings(R.string.user_integral_check_in_success_tx, "")
            fragment_my_checkin_btn.isClickable = false
        }else{
            fragment_my_checkin_btn.text = setTextStrings(R.string.user_integral_check_in_tx, "")
            fragment_my_checkin_btn.isClickable = true
        }
        updateFragment()
    }


     fun updateFragment(){

         if (mAuthentication.getUserLoginStatus()) {
             my_tourist_layout.visibility = View.GONE
             user_info_btn.visibility = View.VISIBLE
//             fragment_my_checkin_info_btn.visibility = View.GONE
             changeUi()
             //获取签到状态
//             getPresenter()?.checkinStatus()
             //获取积分情况
//             getPresenter()?.pointInFo()

             val user = mAuthentication.getUser()
             if (user != null) {
                 mAdapter?.setList(user.userBikeList)
                 mAdapter?.notifyDataSetChanged()
                 updateUserCarIc()
             }
         }else{
             mAdapter?.clearList()
             mFragmentListener?.onChooseCar(null)
             my_tourist_layout.visibility = View.VISIBLE
             user_info_btn.visibility = View.GONE
//             fragment_my_checkin_info_btn.visibility = View.GONE
             updateUserCarIc()
         }
    }

    private fun updateUserCarIc() {
        mIsOnclick = mAdapter!!.getLister() == 0
        val color = if (mAdapter!!.getLister() == 0) {
            user_devices_center_btn.isClickable = false
            R.color.transparent
        }else{
            user_devices_center_btn.isClickable = true
            R.color.colorWhiteGay
        }

        user_devices_center_btn.setDrawableRightColor(getBsColor(color))
//        user_devices_center_btn.compoundDrawables.forEach {
//            TrustLogUtils.d(TAG,"当前是否设置了 drawable ${it}")
//            it?.setVisible(true, false)
//
//        }
    }

    private val listener = object : CarsAdapter.CarsAdapterItemListener{
        override fun startaCarInfo(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
            var intent = Intent(mActivity!!, CarsEditActivity::class.java)
            intent.putExtra(KEY_SELECT_BIKR_ID,bsCarsBean.bike_id)
            startActivity(intent)
        }

        override fun toAddCars() { startBindActivity() }

        override fun chooseCar(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {
//            mFragmentListener?.onChooseCar(bsCarsBean)
        }

        override fun delCars(bsCarsBean: BsGetCarInfoBean.DataBean.BikesBean) {}
    }

    private fun startBindActivity() {
        if (checkIsLogin()) {
            startActivity(Intent(mActivity, BindCarActivity::class.java))
        }
    }


    private fun showHelpPopu() {
            getGPPopup().showListPopu(arrayListOf("021-52216418"),object :TrustGeneralPurposePopupwindow.ListPopuAdapter.itemOnlickListener{
                override fun callBack(data: String) {
                    showCallPhone(data)
                }
            },getString(R.string.please_call_phone_title_tx))
    }


    @Permission(Manifest.permission.CALL_PHONE)
    private fun showCallPhone(data: String) {

        getGPPopup().showDoubleBtnPopu(getString(R.string.call_phone),  getString(R.string.is_call_phone,data), doubleBtnOnclickListener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener {
            override fun onClickListener(isBtn1: Boolean) {
                if (!isBtn1) {
                    callPhone(data)
                }
            }
        }, btn1Tx = getString(R.string.cancel), btn2Tx = getString(R.string.call))
    }



    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    fun callPhone(phoneNum :String ){
        val intent =  Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }


}