package com.sharkgulf.soloera.db.dbmanger

import com.sharkgulf.soloera.db.bean.DbCarInfoBean
import com.sharkgulf.soloera.db.bean.DbCarInfoBean_
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 *  Created by user on 2019/10/11
 */
class BsCarInfoDbManger(boxStore: BoxStore) {
    private var mCarInfoBox: Box<DbCarInfoBean> = boxStore.boxFor()
    private val TAG= "BsCarInfoDbManger"
    fun clearCarInfo(bikeId:Int){
        val list = mCarInfoBox.find(DbCarInfoBean_.bikeId, bikeId.toLong())

        list.forEach {
            TrustLogUtils.d(TAG,"bikeId $bikeId   list : ${it.id}")
        }
        if (list.isNotEmpty()) {
            TrustLogUtils.d(TAG,"删除指令  ${list[0].id}")
            mCarInfoBox.remove(list[0].id)
        }
    }
}