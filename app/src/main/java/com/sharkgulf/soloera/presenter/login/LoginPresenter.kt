package com.sharkgulf.soloera.presenter.login

import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.module.login.LoginModelListener
import com.sharkgulf.soloera.mvpview.login.ILoginView
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import java.util.HashMap

/*
 *Created by Trust on 2018/12/10
 */
class LoginPresenter:TrustPresenters<ILoginView>(),ILoginPresenterListener {


    override fun getUserInfo(map: HashMap<String, Any>) {
        homeModle!!.getUserInfo(map,object :ModuleResultInterface<BsGetUserInfoBean>{
            override fun resultData(bean: BsGetUserInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultUserInfo(bean)
            }

            override fun resultError(bean: String) {
                view.diassDialog()
                view.resultError(bean)
            }
        })
    }

    override fun getCarInfo(map: HashMap<String, Any>) {
        homeModle!!.getCarInfo(map,object:ModuleResultInterface<BsGetCarInfoBean>{
            override fun resultData(bean: BsGetCarInfoBean?,pos:Int?) {
                view.diassDialog()
                view.resultCarInfo(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    private var homeModle: HomeModeListener? = null
    private var loginModle:LoginModelListener? = null
    private var modle:HttpModel? = null
    init {
        loginModle = HttpModel()
        homeModle = HttpModel()
        modle = HttpModel()
    }


    override fun phoneLogin(map : HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModle!!.phoneLogin(map,object :ModuleResultInterface<BsGetSmsBean>{
            override fun resultData(msg: BsGetSmsBean?,pos:Int?) {
                view.diassDialog()
                view.resultPhoneLoginCallBack(msg)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun pwdLogin(map : HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModle!!.pwdLogin(map,object :ModuleResultInterface<BsLoginBean>{
            override fun resultData(bean: BsLoginBean?,pos:Int?) {
                view.resultPwdLoginCallBack(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun getUserKey(map: HashMap<String, Any>) {
        loginModle!!.getUserKey(map,object :ModuleResultInterface<BsGetUserKeyBean>{
            override fun resultData(bean: BsGetUserKeyBean?,pos:Int?) {

                view.resultUserKey(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
            }
        })
    }

    override fun checkUserRegister(map: HashMap<String, Any>) {
        loginModle!!.checkUserRegister(map,object :ModuleResultInterface<BsCheckUserRegisterBean>{
            override fun resultData(bean: BsCheckUserRegisterBean?,pos:Int?) {
                view.resultCheckUserRegister(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun getCheckUserThree(map: HashMap<String, Any>) {
        loginModle!!.checkUserThree(map,object :ModuleResultInterface<BsUserThressBean>{
            override fun resultData(bean: BsUserThressBean?,pos:Int?) {
                view.resultCheckUserThree(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun requestUserRegister(map: HashMap<String, Any>) {
        modle!!.userRegisterKey(map,object :ModuleResultInterface<BsUserRegisterKeyBean>{
            override fun resultData(bean: BsUserRegisterKeyBean?,pos:Int?) {
                view.resultUserregister(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }
}