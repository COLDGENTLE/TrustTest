package com.sharkgulf.soloera.presenter.login

import java.util.HashMap

/*
 *Created by Trust on 2018/12/10
 */
interface ILoginPresenterListener {
    fun phoneLogin(map : HashMap<String, Any>)
    fun pwdLogin(map :HashMap<String, Any>)
    fun getUserKey(map :HashMap<String, Any>)
    fun checkUserRegister(map: HashMap<String, Any>)
    fun getCarInfo(map: HashMap<String, Any>)
    fun getUserInfo(map: HashMap<String, Any>)
    fun getCheckUserThree(map: HashMap<String, Any>)
    fun requestUserRegister(map:HashMap<String,Any>)
}