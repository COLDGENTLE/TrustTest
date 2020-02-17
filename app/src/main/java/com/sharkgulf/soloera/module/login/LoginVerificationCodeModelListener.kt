package com.sharkgulf.soloera.module.login

import com.sharkgulf.soloera.module.bean.BsCheckSmsvcBean
import com.sharkgulf.soloera.module.bean.BsGetCapchaBean
import com.sharkgulf.soloera.module.bean.BsLoginBean
import com.trust.demo.basis.base.ModuleResultInterface
import java.util.HashMap

/*
 *Created by Trust on 2018/12/10
 */
interface LoginVerificationCodeModelListener {
    fun reacquireVerificationCode(map : HashMap<String, Any>,resultInterface: ModuleResultInterface<BsLoginBean>)
    fun submintVerificationCode(map : HashMap<String, Any>,resultInterface: ModuleResultInterface<BsLoginBean>)

    fun getCapcha(map : HashMap<String, Any>,resultInterface: ModuleResultInterface<BsGetCapchaBean>)

    fun getCheckSmsvc(map: HashMap<String, Any>, resultInterface: ModuleResultInterface<BsCheckSmsvcBean>)
}