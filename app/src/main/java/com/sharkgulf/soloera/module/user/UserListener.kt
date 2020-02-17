package com.sharkgulf.soloera.module.user

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/2/23
 */
interface UserListener {
    fun uploadFile(hashMap: HashMap<String, Any>?, resultInterface: ModuleResultInterface<BsUploadFileBean>)
    fun deleteCar(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsDeleteCarBean>)
    fun editUserInfo(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsEditUserInfoBean>)
    fun editPwd(hashMap: HashMap<String,Any>?,resultInterface: ModuleResultInterface<BsSetPwdBean>)
    fun checkinStatus(hashMap: HashMap<String, Any>,resultInterface: ModuleResultInterface<BsCheckinStatusBean>)
    fun pointInFo(hashMap: HashMap<String, Any>,resultInterface: ModuleResultInterface<BsPointinfoBean>)
    fun pointDetail(hashMap: HashMap<String, Any>,resultInterface: ModuleResultInterface<BsPointDetailBean>)
    fun checkinDaily(hashMap: HashMap<String, Any>,resultInterface: ModuleResultInterface<BsCheckinDailyBean>)
}