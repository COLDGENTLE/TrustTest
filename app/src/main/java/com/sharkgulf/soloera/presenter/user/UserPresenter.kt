package com.sharkgulf.soloera.presenter.user

import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bind.BindCarMode
import com.sharkgulf.soloera.module.bind.BindCarModeListener
import com.sharkgulf.soloera.module.controllcar.ControllCarBleMode
import com.sharkgulf.soloera.module.controllcar.ControllCarModeListener
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.module.user.UserListener
import com.sharkgulf.soloera.module.user.UserModule
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.bind.IBindCarPresenterListener
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import kotlin.collections.HashMap
import kotlin.concurrent.thread

/**
 *  Created by user on 2019/2/23
 */
class UserPresenter:TrustPresenters<IUser>() ,IUserPresenterListener, IBindCarPresenterListener {

    private var TAG = ""
    private var mUserCarsListener: UserListener? = null
    private var mHome:HomeModeListener? = null
    private var mModel:HttpModel? = null
    private var mControllCarBleMode:ControllCarBleMode? = null
    private var mBle: ControllCarModeListener? = null
    private  var bindMode : BindCarModeListener? = null
    init {
        mUserCarsListener = UserModule()
        mHome  = HttpModel()
        mModel = HttpModel()
        mBle = ControllCarBleMode()
        bindMode = BindCarMode()
        mControllCarBleMode = ControllCarBleMode()
    }


    fun setTAG(tag:String,bid: Int? = null){
        TAG = tag
        registerBikeInfoData(bid)
        registerBikeBleStatus(bid)
    }


    private fun registerBikeInfoData(bid: Int?){
        sendBikeInfo(bid)
        registerBikeInfo(TAG,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
            override fun onNoticeCallBack(msg: String?) {
                getBikeInfo(bid)
            }
        })
    }

    private fun registerBikeBleStatus(bid:Int?){
        registerBleCheckPassWordSuccess(object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) { view.resultBleStatus(true) }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        },TAG)

        registerBleDissConnection(object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onNoticeCallBack(msg: String?) { view.resultBleStatus(false) }
            override fun onErrorCallBack(msg: String, timeOutTopic: String?) {}
        },TAG)
    }

    override fun deleteCar(hashMap: HashMap<String, Any>?) {
        view.showWaitDialog("正在删除车辆",true,"")
        mUserCarsListener?.deleteCar(hashMap,object :ModuleResultInterface<BsDeleteCarBean>{
            override fun resultData(bean: BsDeleteCarBean?,pos:Int?) {
                mBle?.requestDelBle(1)
                view.diassDialog()
                view.resultDeleteCar(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun updatePhone(hashMap: HashMap<String, Any>) {
        view.showWaitDialog("正在修改手机号码",true,"")
        mModel?.requestUpdate(hashMap,object :ModuleResultInterface<BsHttpBean>{
            override fun resultData(bean: BsHttpBean?, pos: Int?) {
                view.diassDialog()
                view.resultUpdatePhone(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }


    override fun uploadFile(hashMap: HashMap<String, Any>?) {
        mUserCarsListener?.uploadFile(hashMap,object :ModuleResultInterface<BsUploadFileBean>{
            override fun resultData(bean: BsUploadFileBean?,pos:Int?) {
                view.diassDialog()
                view.resultUploadfile(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun userExt(hashMap: HashMap<String, Any>?) {
        view.showWaitDialog(null,true,null)
        mHome?.userExt(hashMap!!,object :ModuleResultInterface<BsUserExtBean>{
            override fun resultData(bean: BsUserExtBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultUserExt(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view?.resultError(msg)
            }

        })
    }


    override fun editUserInfo(hashMap: HashMap<String, Any>?) {
        view.showWaitDialog(null , true ,null)
        mUserCarsListener?.editUserInfo(hashMap!!,object :ModuleResultInterface<BsEditUserInfoBean>{
            override fun resultData(bean: BsEditUserInfoBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultEditUserInfo(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun editPwd(hashMap: HashMap<String, Any>?) {
        mUserCarsListener?.editPwd(hashMap,object :ModuleResultInterface<BsSetPwdBean>{
            override fun resultData(bean: BsSetPwdBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultEditPwd(bean)
            }
            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }
        })
    }


    override fun getUserKey(hashMap: HashMap<String, Any>?) {
        mModel?.getUserKey(hashMap!!,object :ModuleResultInterface<BsGetUserKeyBean>{
            override fun resultData(bean: BsGetUserKeyBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultUserKey(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun checkUserRegister(hashMap: HashMap<String, Any>?) {
        mModel?.checkUserRegister(hashMap!!,object :ModuleResultInterface<BsCheckUserRegisterBean>{
            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

            override fun resultData(bean: BsCheckUserRegisterBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultCheckUserIsResiger(bean)
            }

        })
    }

    override fun userResiter(hashMap: HashMap<String, Any>?) {
        mModel?.userRegisterKey(hashMap!!,object :ModuleResultInterface<BsUserRegisterKeyBean>{
            override fun resultData(bean: BsUserRegisterKeyBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultUserResiger(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun userInfo(hashMap: HashMap<String, Any>?) {
        mHome?.getUserInfo(hashMap!!,object :ModuleResultInterface<BsGetUserInfoBean>{
            override fun resultData(bean: BsGetUserInfoBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultUserInfo(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun checkinStatus(hashMap: HashMap<String, Any>) {
        mUserCarsListener?.checkinStatus(hashMap,object :ModuleResultInterface<BsCheckinStatusBean>{
            override fun resultData(bean: BsCheckinStatusBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultCheckinStatus(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun pointInFo(hashMap: HashMap<String, Any>?) {
        mUserCarsListener?.pointInFo(hashMap!!,object :ModuleResultInterface<BsPointinfoBean>{
            override fun resultData(bean: BsPointinfoBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultPointInFo(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun pointDetail(hashMap: HashMap<String, Any>?) {
        mUserCarsListener?.pointDetail(hashMap!!,object :ModuleResultInterface<BsPointDetailBean>{
            override fun resultData(bean: BsPointDetailBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultPointDetail(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun checkinDaily(hashMap: HashMap<String, Any>?) {
        mUserCarsListener?.checkinDaily(hashMap!!,object :ModuleResultInterface<BsCheckinDailyBean>{
            override fun resultData(bean: BsCheckinDailyBean?,pos:Int?) {
                view?.diassDialog()
                view?.resultCheckinDaily(bean)
            }

            override fun resultError(msg: String) {
                view?.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun requestBindCar(hashMap: HashMap<String, Any>?) {}

    override fun requestGetBleSign(hashMap: HashMap<String, Any>?) {}

    override fun requestUpdateBindStatus(hashMap: HashMap<String, Any>?) {}

    override fun requestUpdateCarInfo(hashMap: HashMap<String, Any>?,carInfoBean: CarInfoBean?) {

        bindMode!!.requestUpdateCarInfo(hashMap,object :ModuleResultInterface<BsUpdateCarInfoBean>{
            override fun resultData(bean: BsUpdateCarInfoBean?,pos:Int?) {
                setCarInfoData(carInfoBean!!)
                thread {
                    Thread.sleep(1000)
                    sendUpdateCarInfo()
                }
                view.diassDialog()
                view.resultUpdateCarInfo(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
                view.diassDialog()
            }
        })
    }

    override fun requestGetBindInfo(hashMap: HashMap<String, Any>?) {}

    override fun startTiming() {}

    override fun stopTiming() {}


    fun getBikeInfo(selectId:Int? = null){
        val carInfoData = if (selectId != null) {
            getCarInfoData(selectId)
        }else{getCarInfoData()}
        if (carInfoData != null) {
            view.resultCarInfo(carInfoData)
        }
    }

    override fun phoneLogin(map: HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        mModel!!.phoneLogin(map,object :ModuleResultInterface<BsGetSmsBean>{
            override fun resultData(bean: BsGetSmsBean?,pos:Int?) {
                view.diassDialog()
                view.resultPhoneLogin(bean)
            }

            override fun resultError(bean: String) {
                view.diassDialog()
                view.resultError(bean)
            }
        })
    }

    fun controllElectironic(bid:Int,mac:String,isOpen:Boolean){
        mControllCarBleMode?.controllElectironic(bid,mac,isOpen)
    }



    fun controllCushionInduction(bid:Int,mac:String,isOpen: Boolean){
        mControllCarBleMode?.controllCushionInduction(bid,mac,isOpen)
    }

    fun getBleStatus(){
        view.resultBleStatus(CONTROLL_STATUS == CONTROLL_CAR_BLE)
    }


}