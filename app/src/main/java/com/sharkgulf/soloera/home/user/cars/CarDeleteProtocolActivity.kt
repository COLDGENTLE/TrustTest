package com.sharkgulf.soloera.home.user.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig.HOME_UPDATE_KEY
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.db.dbmanger.BsDbManger
import com.sharkgulf.soloera.main.MainHomeActivity
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.*
import com.sharkgulf.soloera.tool.view.dialog.TrustDialog
import com.sharkgulf.soloera.tool.view.dialog.TrustGeneralPurposePopupwindow
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.activity_car_delete_protocol.*
import kotlinx.android.synthetic.main.tite_layout.*
import razerdp.basepopup.BasePopupWindow
import kotlin.concurrent.thread

class CarDeleteProtocolActivity : TrustMVPActivtiy<IUser, UserPresenter>(), IUser {
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    private var showDialog: DialogFragment? = null
    private var mPopuwindow: BasePopupWindow? = null
    private val TAG = CarDeleteProtocolActivity::class.java.canonicalName
    override fun resultUploadfile(bean: BsUploadFileBean?) {}

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!, bean.getState_info()!!, this)) {
            mPopuwindow?.dismiss()

            showToast("删除成功")
            TrustLogUtils.d(TAG, "删除车辆 : 删除成功")
            thread {

                Thread.sleep(500)
                BsApplication.bsDbManger!!.clearBle(mBikeId)
                getDbCarInfoManger().clearCarInfo(mBikeId)
                getDbAlertDbManger().clearAlert(mBikeId)

                val carBean = BsDbManger.mDbUserLoginStatusBean?.userBikeList?.bikes

                val find = carBean?.find { it.bike_id == mBikeId }
                carBean?.remove(find)

                BsDbManger.mDbUserLoginStatusBean?.userBikeList?.bikes = carBean
                BsApplication.bsDbManger!!.setUserLoginStatus(BsDbManger.mDbUserLoginStatusBean!!)

                bleDisConnection()


                runOnUiThread {
                    val intent = Intent(mContext, MainHomeActivity::class.java)
                    intent.putExtra(HOME_UPDATE_KEY,true)
                    startActivity(intent)

                    TrustTools<View>().setCountdown(2) {
                        activityList.forEach {
                            when (it) {
                                is CarInfoActivity,
                                is CarsActivity,
                                is CarDeleteProtocolActivity,
                                is CarsEditActivity -> {
                                    it.finish()
                                }
                            }
                        }
                        finish()
                    }

                }
            }
        }else{
//            resultError("失败")
        }
    }

    override fun resultUserExt(bean: BsUserExtBean?) {}

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {}

    override fun resultEditPwd(bean: BsSetPwdBean?) {}

    override fun resultUserKey(bean: BsGetUserKeyBean?) {}

    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    companion object {
        fun startActivitiy(context: Context, bikeId: Int) {
            val intent = Intent(context, CarDeleteProtocolActivity::class.java)
            intent.putExtra("bikeId", bikeId)
            context.startActivity(intent)
        }
    }

    private var mBikeId: Int = -1
    private var mCarDeleteAdapter: CarDeleteAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_car_delete_protocol
    }

    override fun initView(savedInstanceState: Bundle?) {
        mCarDeleteAdapter = CarDeleteAdapter().init(mListener)
        car_deleter_protocol_recycler.adapter = mCarDeleteAdapter
        val lp = LinearLayoutManager(this)
        lp.orientation = LinearLayoutManager.VERTICAL
        car_deleter_protocol_recycler.layoutManager = lp
        baseSetOnClick(title_back_btn)
        baseSetOnClick(car_deleter_protocol_submint_btn)
        title_tx.text = "车辆删除协议"
    }

    override fun initData() {
        val intExtra = intent.getIntExtra("bikeId", -1)
        if (intExtra != -1) {
            mBikeId = intExtra
        }
    }

    override fun baseResultOnClick(v: View) {
        if (v.id == R.id.title_back_btn) {
            finish()
        } else if (v.id == R.id.car_deleter_protocol_submint_btn) {
//            12312asd
//            CarDeleteBleOrServiceActivity.startActivity(this,mBikeId)
            delCar()
//            showPopu()
        }
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
        if (isShow) {
            showDialog = TrustDialog().showDialog(supportFragmentManager)
        }
    }

    override fun diassDialog() {
        showDialog?.dismiss()
    }

    override fun showToast(msg: String?) {
        TrustLogUtils.d(TAG, "删除车辆 : $msg")
//        baseShowToast(msg)

        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    override fun resultSuccess(msg: String) {}

    override fun resultError(msg: String) {
        var msgStr = msg
        TrustLogUtils.d(TAG,"error:$msg")
        if (msgStr == "网络不可用,请检查您手机的网络设置") {
            msgStr = getString(R.string.http_error_tx)
            showOnly(msgStr)

        }else if(msgStr == getString(com.trust.demo.basis.R.string.HttpTimeOut) || msgStr == "失败"){
            msgStr = "啊哦！无法连接到服务器，车辆删除失败"
            showDouble(msgStr)
        }else{
            showBsToast(msg)
        }


//        showToast(msg)
    }



    override fun createPresenter(): UserPresenter { return UserPresenter() }


    private val mListener = object : CarDeleteAdapter.DeleteAdapterListener {
        override fun isAceiptionListener(boolean: Boolean) {
            val id = if (boolean) {
                car_deleter_protocol_submint_btn.isEnabled = true
                R.drawable.blue_bg
            } else {
                car_deleter_protocol_submint_btn.isEnabled = false
                R.drawable.login_register_btn_gray_bg
            }
            car_deleter_protocol_submint_btn.setBackgroundResource(id)
        }

    }

    private fun showPopu() {
        getGPPopup().showDoubleBtnPopu(getString(R.string.del_car_tx), btn1Tx =
        getString(R.string.cancel), btn2Tx = getString(R.string.confirm), doubleBtnOnclickListener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener {
            override fun onClickListener(isBtn1: Boolean) {
                if (!isBtn1) {
                    delCar()
                }
            }
        })

//        mPopuwindow = TrustBasePopuwindow.getPopuwindow(this,R.layout.popupwindow_choose_layout,object : TrustBasePopuwindow.TrustPopuwindowOnclickListener{
//           override fun resultPopuwindowViewListener(v: View): View {
//                v.findViewById<Button>(R.id.popuwindow_choose_layout_submint_btn).setOnClickListener {
//                    mPopuwindow?.dismiss()
//                    val map= hashMapOf<String,Any>()
//                    map["bike_id"] = mBikeId
//                    getPresenter()?.deleteCar(map)
//                }
//                v.findViewById<Button>(R.id.popuwindow_choose_layout_cancel_btn).setOnClickListener {
//                    mPopuwindow?.dismiss()
//                }
//               return v
//           }
//       }).setPopupGravity(Gravity.CENTER )
//        mPopuwindow?.showPopupWindow()
    }

    private fun delCar() {
        val map = hashMapOf<String, Any>()
        map["bike_id"] = mBikeId
        getPresenter()?.deleteCar(map)
    }


    private fun showOnly(msgStr:String){
        getGPPopup().showOnlyBtnPopu("车辆删除失败", msgStr, "我知道了", listener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.OnlyBtnOnclickListener {
            override fun onClickListener(view: BasePopupWindow) {
                view.dismiss()
            }
        })
    }

    private fun showDouble(msgStr:String){
        getGPPopup().showDoubleBtnPopu("车辆删除失败", msgStr, "关闭",btn2Tx = "重试",
                doubleBtnOnclickListener = object : TrustGeneralPurposePopupwindow.PopupOnclickListener.DoubleBtnOnclickListener {
            override fun onClickListener(isBtn1: Boolean) {
                if (!isBtn1) {
                    delCar()
                }
            }
        })
    }
}
