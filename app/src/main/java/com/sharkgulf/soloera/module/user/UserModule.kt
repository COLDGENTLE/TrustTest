package com.sharkgulf.soloera.module.user

import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.HttpModel
import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface
import com.trust.retrofit.config.ProjectInit
import com.trust.retrofit.net.TrustRetrofitUtils

/**
 *  Created by user on 2019/2/23
 */
class UserModule: HttpModel(),UserListener {
    override fun checkinStatus(hashMap: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckinStatusBean>) {
        sendRrequest(TrustAppConfig.URL_CHECK_IN_STATUS,hashMap!!, BsCheckinStatusBean::class.java,resultInterface)
    }

    override fun pointInFo(hashMap: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsPointinfoBean>) {
        sendRrequest(TrustAppConfig.URL_POINT_INFO,hashMap!!, BsPointinfoBean::class.java,resultInterface)
    }

    override fun pointDetail(hashMap: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsPointDetailBean>) {
        sendRrequest(TrustAppConfig.URL_POINT_DETAIL,hashMap!!, BsPointDetailBean::class.java,resultInterface)

    }

    override fun checkinDaily(hashMap: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckinDailyBean>) {
        sendRrequest(TrustAppConfig.URL_CHECKIN_DAILY,hashMap!!, BsCheckinDailyBean::class.java,resultInterface)

    }


    override fun uploadFile(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsUploadFileBean>) {
        ProjectInit.setHeader("Content-Type","multipart/form-data")
        sendRrequest(TrustAppConfig.URL_UPLOAD_FILE,hashMap!!, BsUploadFileBean::class.java,resultInterface, TrustRetrofitUtils.UPLOAD)
    }

    override fun deleteCar(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsDeleteCarBean>) {
        sendRrequest(TrustAppConfig.URL_UNBIND_CAR,hashMap!!, BsDeleteCarBean::class.java,resultInterface)
    }

    override fun editUserInfo(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsEditUserInfoBean>) {
        sendRrequest(TrustAppConfig.URL_UPDATE_PROFILE,hashMap!!, BsEditUserInfoBean::class.java,resultInterface)
    }

    override fun editPwd(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsSetPwdBean>) {
        sendRrequest(TrustAppConfig.URL_SET_PWD,hashMap!!, BsSetPwdBean::class.java,resultInterface)
    }

}