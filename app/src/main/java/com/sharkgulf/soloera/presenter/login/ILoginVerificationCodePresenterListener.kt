package com.sharkgulf.soloera.presenter.login

/*
 *Created by Trust on 2018/12/10
 */
interface ILoginVerificationCodePresenterListener {
    //重新获取短信验证码
    fun reacquireVerificationCode(map : HashMap<String, Any>)
    //验证码登陆
    fun submintVerificationCode(map : HashMap<String, Any>)
    //获取防刷验证
    fun getCapcha(map:HashMap<String,Any>)

    fun phoneLogin(map:HashMap<String,Any>)

    fun getUserInfo(map:HashMap<String,Any>)

    fun setPwd(map:HashMap<String,Any>)

    fun getUserKey(map:HashMap<String,Any>)

    fun getUserRegisterKey(map: HashMap<String, Any>)

    fun getCheckSmsvc(map: HashMap<String, Any>)

    fun pwdLogin(map:HashMap<String,Any>)

    fun checkUserRegister(map: HashMap<String, Any>)

    fun getCarInfo(map: HashMap<String, Any>)
}