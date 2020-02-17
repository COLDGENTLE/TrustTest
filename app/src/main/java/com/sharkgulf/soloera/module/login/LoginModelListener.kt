package com.sharkgulf.soloera.module.login

import com.sharkgulf.soloera.module.bean.*
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/*
 *Created by Trust on 2018/12/10
 */
interface LoginModelListener {
    fun phoneLogin(map :HashMap<String, Any>,resultInterface: ModuleResultInterface<BsGetSmsBean>)
    fun pwdLogin(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsLoginBean>)
    fun getUserKey(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsGetUserKeyBean>)
    fun checkUserRegister(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckUserRegisterBean>)
    fun setPwd(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsSetPwdBean>)
    fun userRegisterKey(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsUserRegisterKeyBean>)
    fun checkUserThree(map:HashMap<String,Any>,resultInterface: ModuleResultInterface<BsUserThressBean>)
}