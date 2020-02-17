package com.sharkgulf.soloera.module

import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.home.HomeModeListener
import com.sharkgulf.soloera.module.login.LoginModelListener
import com.sharkgulf.soloera.module.login.LoginVerificationCodeModelListener
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.getAgent
import com.sharkgulf.soloera.tool.config.getAuthentication
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.demo.basis.base.model.RequestResultListener
import com.trust.demo.basis.base.model.TrustModel
import com.trust.demo.basis.base.model.http.TrustRetrofitModel
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.*
import com.trust.retrofit.config.ProjectInit
import com.trust.retrofit.net.TrustRetrofitUtils
import org.json.JSONObject

/*
 *Created by Trust on 2018/12/10
 */
open class HttpModel :TrustModel<TrustRetrofitModel>() ,UtilsListener, LoginModelListener, LoginVerificationCodeModelListener ,HomeModeListener,HttpModelListener{

    override fun requestUpdatePhoneAndThree(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsTicketBean>) {
        sendRrequest(URL_UPDATE_PHONE_AND_THREE,map,BsTicketBean::class.java,resultInterface)
    }

    override fun requestDownLoad(url:String,map: HashMap<String, Any>, range:Long,downLoadListener: RequestResultListener.DownLoadListener) {
        sendRrequestDownLoad<String>(url,range,map,downLoadListener)
    }

    override fun requestUpdate(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsHttpBean>) {
        sendRrequest(URL_UPDATE_PHONE,map,BsHttpBean::class.java,resultInterface)
    }

    private val TAG = "HttpModel"

    override fun createRequestModule(): TrustRetrofitModel {
        return TrustRetrofitModel()
    }

    override fun updatePushId(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsPushIdBean>) {
        sendRrequest(URL_PUSH_POST_PUSH,map,BsPushIdBean::class.java,resultInterface)
    }

    /**
     * 手机登陆  （获取短信验证码）
     */
     override fun phoneLogin(map :HashMap<String, Any>,resultInterface: ModuleResultInterface<BsGetSmsBean>) {
        sendRrequest(URL_SEND_SMSV,map,BsGetSmsBean::class.java,resultInterface)
    }

    override fun pwdLogin(map :HashMap<String, Any>,resultInterface: ModuleResultInterface<BsLoginBean>) {
        sendRrequest(URL_USER_LOGIN,map,BsLoginBean::class.java,resultInterface)
    }

    override fun checkUserRegister(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckUserRegisterBean>) {
        sendRrequest(URL_CHECK_USER_IS_REGISTER,map,BsCheckUserRegisterBean::class.java,resultInterface)
    }


    override fun reacquireVerificationCode(map :HashMap<String, Any>,resultInterface: ModuleResultInterface<BsLoginBean>) {

    }

    override fun submintVerificationCode(map : HashMap<String, Any>,resultInterface: ModuleResultInterface<BsLoginBean>) {
        sendRrequest(URL_USER_LOGIN,map,BsLoginBean::class.java,resultInterface)
    }

     override fun getUserKey(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetUserKeyBean>) {
         sendRrequest(URL_GET_USER_PWD_KEY,map,BsGetUserKeyBean::class.java,resultInterface)
     }



    override fun getCapcha(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetCapchaBean>) {
        sendRrequest(URL_GET_SMSV,map,BsGetCapchaBean::class.java, resultInterface)
    }

    override fun checkUserThree(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsUserThressBean>) {
        sendRrequest(URL_CHECK_USER_THREE,map,BsUserThressBean::class.java, resultInterface)
    }

    protected fun <T> sendRrequest(uri:String,map: HashMap<String, Any>,clasz :Class<T> ,resultInterface: ModuleResultInterface<T>,type:Int = TrustRetrofitUtils.POST_RAW) {
        if (TrustHttpUtils.getSingleton(TrustAppUtils.getContext()).isNetworkAvailable()) {
            val resultListener = object : RequestResultListener<T> {
                override fun resultSuccess(bean: T?) {
                    resultInterface.resultData(bean)
                }

                override fun resultError(e: Throwable?) {
                    val checkHttpError = TrustHttpErrorUtils.checkHttpError(e!!)
                    resultInterface.resultError(checkHttpError!!)
                }

                override fun netWorkError(msg: String?) {
                    resultInterface.resultError(msg!!)
                }

            }

            when (type) {
                TrustRetrofitUtils.UPLOAD-> {
                    requestModule?.requestUpload(uri,
                            type,
                            addMapConfig(map,uri),
                            resultListener, clasz)
                }

                else -> {
                    requestModule!!.requestJsonParams(uri,
                            type,
                            addMapConfig(map,uri),
                            resultListener, clasz)
                }
            }

        }else{
            resultInterface.resultError(TrustAppUtils.getContext().getString(R.string.http_error_tx))
        }


    }


    protected fun <T> sendRrequestDownLoad(uri:String,range:Long,map: HashMap<String, Any>,downLoadListener: RequestResultListener.DownLoadListener) {
        if (TrustHttpUtils.getSingleton(TrustAppUtils.getContext()).isNetworkAvailable()) {
            requestModule!!.download<String>(uri,range,"",map,downLoadListener)
        }else{
            downLoadListener.netWorkError(TrustAppUtils.getContext().getString(R.string.http_error_tx))
        }

    }

    override fun getUserInfo(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetUserInfoBean>) {
        sendRrequest(URL_GET_USER_INFO,map,BsGetUserInfoBean::class.java,resultInterface)
    }

    override fun getCarInfo(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetCarInfoBean>) {
        sendRrequest(URL_GET_CAR_INFO,map,BsGetCarInfoBean::class.java,resultInterface)
    }

    override fun userExt(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsUserExtBean>) {
        sendRrequest(URL_USER_EXT,map,BsUserExtBean::class.java,resultInterface)
    }

    override fun delCar(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsDelCarBean>) {
        sendRrequest(URL_UNBIND_CAR,map,BsDelCarBean::class.java,resultInterface)
    }


    override fun setPwd(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsSetPwdBean>) {
        sendRrequest(URL_RESET_PWD,map,BsSetPwdBean::class.java,resultInterface)
    }

    override fun userRegisterKey(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsUserRegisterKeyBean>) {
        sendRrequest(URL_USER_REGISTER,map,BsUserRegisterKeyBean::class.java,resultInterface)
    }


    override fun getCheckSmsvc(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckSmsvcBean>) {
        sendRrequest(URL_CHECK_SMSVC,map,BsCheckSmsvcBean::class.java,resultInterface)
    }

    override fun requestAppVersion(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsAppVersionInfoBean>) {
        sendRrequest(URL_CHECK_APP_VERSION,map,BsAppVersionInfoBean::class.java,resultInterface)
    }

    override fun requestLostMode(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsLostModeBean>) {
        sendRrequest(URL_LOST_MODE,map,BsLostModeBean::class.java,resultInterface)
    }

    override fun requestAlertList(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsAlertBean>) {
        sendRrequest(URL_ALERT_LIST,map,BsAlertBean::class.java,resultInterface)
    }



    /**
     * 统一添加 ts seq
     */
    fun addMapConfig(map:HashMap<String, Any>?,tag:String) :HashMap<String, Any>?{
        val time = TrustTools.getSystemTimeDataSecond()
        val user = getAuthentication().getUser()
        if (user != null) {
            if (user.userLoginStatus) {
                token = getAuthentication().getToken()
                ProjectInit.addToken(token)
            }
        }

        if (token == null || token == "") {
            ProjectInit.addToken(TrustMD5Utils.getMD5("BLUE", time.toString(), "SHARK"))
        }
        if (map != null) {
            val isUseTemporaryToken = map["isUseTemporaryToken"]
            if (isUseTemporaryToken != null && isUseTemporaryToken as Boolean) {
                ProjectInit.addToken(TrustMD5Utils.getMD5("BLUE", time.toString(), "SHARK"))
            }

            val isUseDemoToken = map["isUseDemoToken"]
            if (isUseDemoToken != null && isUseDemoToken as Boolean) {
                ProjectInit.addToken(DEMO_TOKEN)
            }


        }
        if(USE_TYPE == USE_DEMO){
            ProjectInit.addToken(DEMO_TOKEN)
        }


        ProjectInit.setHeader("SGAGENT", getAgent())
        map?.set("ts", time)
        map?.set("seq", time)
        ProjectInit.setHeader("ts",time.toString())
        val jsonObject = JSONObject(map as Map<*, *>)
        TrustLogUtils.d(TAG,"tag : $tag |map :${jsonObject.toString()}")
        return map
    }


    override fun requestDemoData(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetCarInfoBean>) {
       fun getBikeInfo(map:HashMap<String, Any>){
           getCarInfo(map,object :ModuleResultInterface<BsGetCarInfoBean>{
               override fun resultData(bean: BsGetCarInfoBean?, pos: Int?) {
                   if (BeanUtils.checkSuccess(bean!!.state!!,bean.state_info!!,null)) {
                       getAuthentication().setDemoUserBikes(bean)
                       resultInterface.resultData(bean)
                   }else{resultInterface.resultError(bean.getState_info()!!)}
               }

               override fun resultError(msg: String) { resultInterface.resultError(msg) }

           })
       }


        getUserInfo(map,object :ModuleResultInterface<BsGetUserInfoBean>{
            override fun resultData(bean: BsGetUserInfoBean?, pos: Int?) {
                if (BeanUtils.checkSuccess(bean!!.getState()!!,bean.getState_info()!!,null)) {
                    userId = bean.getData()?.user?.user_id!!
                    map["user_id"] = userId
                    getBikeInfo(map)
                    getAuthentication().setDemoUser(bean)
                }else{resultInterface.resultError(bean.getState_info()!!)}
            }

            override fun resultError(msg: String) {resultInterface.resultError(msg)}
        })


    }

}