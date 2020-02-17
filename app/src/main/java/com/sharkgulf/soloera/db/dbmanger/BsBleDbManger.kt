package com.sharkgulf.soloera.db.dbmanger

import com.sharkgulf.soloera.db.bean.DbBleBean
import com.sharkgulf.soloera.db.bean.DbBleBean_
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/**
 *  Created by user on 2019/9/30
 */
class BsBleDbManger(boxStore: BoxStore?) {
    private var mBleBox: Box<DbBleBean> = boxStore!!.boxFor()

    fun getBleInfo():DbBleBean?{
        val query = mBleBox.query()
        val list = query.equal(DbBleBean_.isConnction, true).build().find()
        return if(list.isEmpty()) null else list[0]
    }

    fun getBleInfo(bid:Int):DbBleBean?{
        val query = mBleBox.query()
        val list = query.equal(DbBleBean_.bikeId, bid.toLong()).build().find()
        return if(list.isEmpty()) null else list[0]
    }

    fun setBleInfoCushionInduction(bid:Int,cushionInduction:Boolean){
        val bsBleInfo = getBsBleInfo(bid)
        bsBleInfo.cushionInduction = cushionInduction
        setBleInfo(bsBleInfo)
    }

    fun setBleInfoElectironic(bid:Int,electironic:Boolean){
        val bsBleInfo = getBsBleInfo(bid)
        bsBleInfo.electironic = electironic
        setBleInfo(bsBleInfo)
    }


    private fun setBleInfo(dbBleBean: DbBleBean){
        mBleBox.put(dbBleBean)
    }

    private fun getBsBleInfo(bid:Int):DbBleBean{
        val query = mBleBox.query()
        val list = query.equal(DbBleBean_.bikeId, bid.toLong()).build().find()
        var dbBleBean:DbBleBean? = null
        if (list.isEmpty()) {
            dbBleBean = DbBleBean()
        }else{
            dbBleBean = list[0]
        }
        return dbBleBean!!
    }
}