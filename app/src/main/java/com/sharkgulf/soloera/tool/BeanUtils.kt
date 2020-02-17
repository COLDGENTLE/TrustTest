package com.sharkgulf.soloera.tool

import com.sharkgulf.soloera.tool.arouter.arouterStartActivity
import com.sharkgulf.soloera.tool.config.extUser
import com.trust.demo.basis.base.veiw.TrustView

/**
 *  Created by user on 2019/1/24
 */
class BeanUtils{
    companion object {
         val CODE_SUCCESS =  "00"
         val CODE_TOKEN_NULL =  "1000" //token失效
         val CODE_TOKEN_OUT_TIME =  "1001" //token过期
         val CODE_USER_IS_OK = "1103" //用户已经注册ok、
        private var beanUtils:BeanUtils? = null

        val CODE_DATA_ERROR = -1
        val CODE_DATA_SUCCESS = 0
        val CODE_DATA_TOKEN_NULL = 1
        val CODE_DATA_TOKEN_OUT_TIME = 2
        val CODE_DATA_USER_IS_REGISTER = 3



        fun checkSuccess(code:String,info:String,trustView: TrustView?):Boolean{
            if (beanUtils == null) {
                beanUtils = BeanUtils()
            }

            return if (code == CODE_SUCCESS) {
                true
            }else if(code == CODE_TOKEN_NULL || code == CODE_TOKEN_OUT_TIME){
                trustView?.resultError(info)
                beanUtils?.tokenNothing()
                false
            }else if(code == CODE_USER_IS_OK){
                trustView?.resultError(info)
                false
            }
            else{
                trustView?.resultError(info)
                false
            }
        }

        fun checkResultData(code:String,info:String,trustView: TrustView?):Int{
            if (beanUtils == null) {
                beanUtils = BeanUtils()
            }
            return when(code){
                CODE_SUCCESS ->{CODE_DATA_SUCCESS}
                CODE_TOKEN_NULL ->{
                    trustView?.resultError(info)
                    beanUtils?.tokenNothing()
                    CODE_DATA_TOKEN_NULL}
                CODE_TOKEN_OUT_TIME ->{
                    trustView?.resultError(info)
                    beanUtils?.tokenNothing()
                    CODE_DATA_TOKEN_OUT_TIME}
                CODE_USER_IS_OK ->{CODE_DATA_USER_IS_REGISTER}
                else ->{
                    trustView?.resultError(info)
                    CODE_DATA_ERROR}
            }
        }

    }

    fun tokenNothing(){
        extUser()
        arouterStartActivity("/activity/LoginActivity")
    }
}