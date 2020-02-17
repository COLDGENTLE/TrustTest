package com.sharkgulf.soloera.presenter.login

import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.module.login.LoginModelListener
import com.sharkgulf.soloera.module.login.LoginVerificationCodeModelListener
import com.sharkgulf.soloera.mvpview.login.ILoginVerificationCodeView
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlin.collections.HashMap

/*
 *Created by Trust on 2018/12/10
 */
class LoginVerificationCodePresenter:TrustPresenters<ILoginVerificationCodeView>(),ILoginVerificationCodePresenterListener,ILoginPresenterListener {
    override fun getCheckUserThree(map: java.util.HashMap<String, Any>) {

    }

    override fun requestUserRegister(map: java.util.HashMap<String, Any>) {
        http?.userRegisterKey(map,object :ModuleResultInterface<BsUserRegisterKeyBean>{
            override fun resultData(bean: BsUserRegisterKeyBean?,pos:Int?) {
                view.resultUserregister(bean)
            }

            override fun resultError(msg: String) {
                view.resultError(msg)
            }

        })
    }


    private var loginModle:LoginVerificationCodeModelListener? = null
    private var loginModelListener: LoginModelListener? = null
    private var homeModle:HomeModeListener? = null
    private var http :HttpModel? = null
    init {
        loginModle = HttpModel()
        loginModelListener = HttpModel()
        homeModle = HttpModel()
        http = HttpModel()
    }


    override fun reacquireVerificationCode(map : HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModle!!.reacquireVerificationCode(map,object :ModuleResultInterface<BsLoginBean>{
            override fun resultData(bean: BsLoginBean?,pos:Int?) {
                view.diassDialog()
                view.resultReacquireVerificationCodeCallBack(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }



    override fun submintVerificationCode(map : HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModle!!.submintVerificationCode(map,object :ModuleResultInterface<BsLoginBean>{
            override fun resultData(bean: BsLoginBean?,pos:Int?) {
                view.diassDialog()
                TrustLogUtils.d("submintVerificationCode ： resultData")
                view.resultSubmintVerificationCodeCallBack(bean)
            }
            override fun resultError(msg: String) {
                view.diassDialog()
                TrustLogUtils.d("submintVerificationCode ： resultError")
                view.resultError(msg)
            }
        })
    }

    override fun getCapcha(map: HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModle!!.getCapcha(map,object :ModuleResultInterface<BsGetCapchaBean>{
            override fun resultData(bean: BsGetCapchaBean?,pos:Int?) {
                view.diassDialog()
                view.resultCapcha(bean)
            }
            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun phoneLogin(map: HashMap<String, Any>) {
        view.showWaitDialog("",true,"")
        loginModelListener!!.phoneLogin(map,object :ModuleResultInterface<BsGetSmsBean>{
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


    override fun setPwd(map: HashMap<String, Any>) {
        loginModelListener!!.setPwd(map,object :ModuleResultInterface<BsSetPwdBean>{
            override fun resultData(bean: BsSetPwdBean?,pos:Int?) {
                view.diassDialog()
                view.resultSetPwd(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }
        })
    }

    override fun getUserKey(map: HashMap<String, Any>) {
        loginModelListener!!.getUserKey(map,object :ModuleResultInterface<BsGetUserKeyBean>{
            override fun resultData(bean: BsGetUserKeyBean?,pos:Int?) {
                view.diassDialog()
                view.resultUserKey(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun getUserRegisterKey(map: HashMap<String, Any>) {
        loginModelListener!!.userRegisterKey(map,object :ModuleResultInterface<BsUserRegisterKeyBean>{
            override fun resultData(bean: BsUserRegisterKeyBean?,pos:Int?) {
                view.diassDialog()
                view.resultUserRegisterKey(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun getCheckSmsvc(map: HashMap<String, Any>) {
        loginModle?.getCheckSmsvc(map,object :ModuleResultInterface<BsCheckSmsvcBean>{
            override fun resultData(bean: BsCheckSmsvcBean?,pos:Int?) {
                view.diassDialog()
                view.resultCheckSmsvc(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun pwdLogin(map: HashMap<String, Any>) {
        loginModelListener?.pwdLogin(map,object :ModuleResultInterface<BsLoginBean>{
            override fun resultData(bean: BsLoginBean?,pos:Int?) {
                view.diassDialog()
                view.resultPwdLogin(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
            }

        })
    }

    override fun checkUserRegister(map: java.util.HashMap<String, Any>) {
        loginModelListener?.checkUserRegister(map,object :ModuleResultInterface<BsCheckUserRegisterBean>{
            override fun resultData(bean: BsCheckUserRegisterBean?,pos:Int?) {
                view.diassDialog()
                view.resultCheckUserRegister(bean)
            }

            override fun resultError(msg: String) {
                view.diassDialog()
                view.resultError(msg)
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

}