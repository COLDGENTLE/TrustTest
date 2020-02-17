package com.sharkgulf.soloera.module

import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/9/9
 */
interface DbModelListener {
    fun putAlertBean(bikeId:Int,day:String,bean: BsAlertBean,resultInterface: ModuleResultInterface<Boolean>? = null)
    fun getAlertNewestTime(bikeId:Int,day:String,resultInterface: ModuleResultInterface<Int>)
    fun getAlertList(bikeId:Int,day:String,isRead:Boolean,resultInterface: ModuleResultInterface<DbAlertBean>)
}