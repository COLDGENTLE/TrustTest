package com.sharkgulf.soloera.appliction

import com.alibaba.android.arouter.launcher.ARouter
import com.bs.trust.mapslibrary.gd.GdMapTool
import com.sharkgulf.soloera.BuildConfig
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.jpushlibrary.getRegistrationId
import com.sharkgulf.soloera.jpushlibrary.initJPush
import com.sharkgulf.soloera.tool.config.*
import com.tencent.bugly.crashreport.CrashReport
import com.trust.retrofit.config.ProjectInit
import com.sharkgulf.trustthreeloginlibrary.UmConfigInit
import com.sharkgulf.trustthreeloginlibrary.threeLogin
import com.trust.demo.basis.trust.utils.TrustFileUtils
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.umeng.commonsdk.utils.UMUtils
import io.objectbox.android.AndroidObjectBrowser

/**
 *  Created by user on 2019/12/5
 *  初始化 app一些 配置 以及 第三方库
 */
class InitThirdParty(application: BsApplication) {
    private val bsApplication = application

    init { initConfig() }

    private fun initConfig(){
        initAppConfig()
        initRetrofit()
        initBugly()
        initUm()
        initGdMap()
        initJpush()
        initARouter()
        initNotification()
    }



    private fun initBugly(){
        CrashReport.initCrashReport(bsApplication, "ec00ed3ff9", true)
    }

    private fun initRetrofit(){
        //初始化网络请求框架
        ProjectInit.init(bsApplication).setApiHost(TrustAppConfig.HTTP_HOST).configure()
    }

    private fun initGdMap(){
        //初始化地图lib
        GdMapTool.initGdMapTool(bsApplication)
    }

    private fun initUm(){
        UmConfigInit("5e4a146965b5ec273b5aeb56",bsApplication,"UMENG_CHANNEL")
        threeLogin(bsApplication,"wx31795d5be0dceee8",
                "fc3094210a4263fff476e98db20520fb",
                "101572894", "58d585786f67ee421c1bcb1df3655e10",
                "2044111460", "7f0e02ff8b7b0dd83ad53272c665b7df",
                "https://sns.whalecloud.com/sina2/callback")
    }

    private fun initJpush(){
        initJPush(bsApplication, hashMapOf(Pair("onNotifyMessageOpened","${bsApplication.packageName}.PushActivity")))
    }

    private fun initARouter(){
        if (UMUtils.isDebug(bsApplication)) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()   // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(bsApplication) // 尽可能早，推荐在Application中初始化
    }

    private fun initNotification(){

    }

    private fun initAppConfig(){
        TrustLogUtils.setIsDebug(BuildConfig.DEBUG)
        TrustAppConfig.HTTP_HOST = if (getAppConfig().getIsDebug()) {
//            CrashHandler.getInstance(this)
            showToast("now is debug")
            BsApplication.boxStore.let { val started = AndroidObjectBrowser(BsApplication.boxStore).start(bsApplication) }
            TrustAppConfig.SOCEK_HOST = TrustAppConfig.SOCEK_DEBUG_HOST
            TrustAppConfig.WEB_URL_HOST = TrustAppConfig.WEB_URL_DEBUG_HOST
            TrustAppConfig.HTTP_DEBUG_HOST
        }
        else{
            TrustAppConfig.SOCEK_HOST = TrustAppConfig.SOCEK_RELEASE_HOST
            TrustAppConfig.WEB_URL_HOST = TrustAppConfig.WEB_URL_RELEASE_HOST
            TrustAppConfig.HTTP_RELEASE_HOST
        }

        if (getChannel("UMENG_CHANNEL") == "BS_DEV" && !getAppConfig().getisDevFirstInit()) {
            BsApplication.boxStore.let { val started = AndroidObjectBrowser(BsApplication.boxStore).start(bsApplication) }
//            CrashHandler.getInstance(this)
            showToast("now is debug")
            getAppConfig().setDevFirstInit()
            getAppConfig().setAppModule(true)
            TrustAppConfig.WEB_URL_HOST = TrustAppConfig.WEB_URL_DEBUG_HOST
            TrustAppConfig.HTTP_HOST = TrustAppConfig.HTTP_DEBUG_HOST
            TrustAppConfig.SOCEK_HOST = TrustAppConfig.SOCEK_DEBUG_HOST
        }



        bsApplication.registerActivityLifecycleCallbacks(BsActivityLifecycleCallbacks.getInstance())


        TrustHttpUtils.getSingleton(bsApplication).registerReceiver(object : TrustHttpUtils.TrustHttpUtilsListener{
            override fun onAvailable() {}

            override fun onLost() { showToast(bsApplication.getString(R.string.http_error_tx)) }
        })



        val registrationId = getRegistrationId(bsApplication)
        if (registrationId != null) { getAppConfig().setDbDeviceId(registrationId) }
        TrustLogUtils.d(TAG,"registrationId:$registrationId")
        initAlertInfoMap()
        val bleInfo = getDbBleManger().getBleInfo()
        TrustLogUtils.d(TAG,"bleinfo $bleInfo")

        TrustFileUtils.createFile("","BsConfig.txt")


    }
}