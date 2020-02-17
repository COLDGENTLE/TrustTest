package com.sharkgulf.soloera.presenter.user

/**
 *  Created by user on 2019/2/23
 */
interface IUserPresenterListener {
    fun uploadFile(hashMap: HashMap<String, Any>? = null)
    fun deleteCar(hashMap: HashMap<String,Any>? = null)
    fun userExt(hashMap: HashMap<String, Any>? = null)
    fun editUserInfo(hashMap: HashMap<String, Any>? = null)
    fun editPwd(hashMap: HashMap<String, Any>? = null)
    fun getUserKey(hashMap: HashMap<String, Any>? = null)
    fun checkUserRegister(hashMap: HashMap<String, Any>? = null)
    fun userResiter(hashMap: HashMap<String, Any>? = null)
    fun userInfo(hashMap: HashMap<String, Any>? = null)
    fun checkinStatus(hashMap: HashMap<String, Any> = hashMapOf())
    fun pointInFo(hashMap: HashMap<String, Any>? =  hashMapOf())
    fun pointDetail(hashMap: HashMap<String, Any>? =  hashMapOf())
    fun checkinDaily(hashMap: HashMap<String, Any>? = hashMapOf())
}