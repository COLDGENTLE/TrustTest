package com.trust.demo.basis.base

/**
 * Created by Trust on 2018/7/13.
 */
interface ModuleResultInterface <T>{
    fun resultData(bean:T?,pos:Int? = null)
    fun resultError(msg:String)
}