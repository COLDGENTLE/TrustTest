package com.sharkgulf.soloera.db.dbmanger

import com.sharkgulf.soloera.db.bean.DbAlertBean
import com.sharkgulf.soloera.db.bean.DbAlertBean_
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 *  Created by user on 2019/10/11
 */
class BsAlertBeanDbManger (boxStore: BoxStore){
    private var mAlertBox: Box<DbAlertBean> = boxStore.boxFor()
    private val TAG= "BsAlertBeanDbManger"

    fun clearAlert(bikeId:Int){
        val list = mAlertBox.find(DbAlertBean_.bikeId, bikeId.toLong())
        if (list.isNotEmpty()) {
            mAlertBox.remove(list)
        }
    }
}