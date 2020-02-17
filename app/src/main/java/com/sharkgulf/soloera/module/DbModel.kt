package com.sharkgulf.soloera.module

import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.module.bean.BsAlertBean
import com.trust.demo.basis.base.ModuleResultInterface

/**
 *  Created by user on 2019/9/9
 */
class DbModel :DbModelListener{


    private val mDb = bsDbManger

    override fun putAlertBean(bikeId: Int, day: String, bean: BsAlertBean, resultInterface: ModuleResultInterface<Boolean>?) {
        val alertBean = mDb?.setAlertBean(day,bean)
        resultInterface?.resultData(alertBean,null)
    }

    override fun getAlertNewestTime(bikeId: Int,day:String, resultInterface: ModuleResultInterface<Int>) {
        resultInterface.resultData(mDb?.getAlertBean(bikeId,day),null)
    }

    override fun getAlertList(bikeId: Int, day: String, isRead:Boolean,resultInterface: ModuleResultInterface<DbAlertBean>) {
        resultInterface.resultData(mDb?.getBsAlertBean(bikeId,day,isRead),null)
    }

}