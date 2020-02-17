package com.sharkgulf.soloera.dataanalysis

import com.sharkgulf.soloera.appliction.BsApplication.Companion.bsDbManger
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean

/**
 *  Created by user on 2019/8/19
 */
class SocketCarBean{
    private var mBsDbManger = bsDbManger
    fun setCarLoctionBean(carLoctionBean: CarLoctionBean){
        mBsDbManger?.setCarInfo(carLoctionBean.body.bike_id,carLoctionBean)
    }

    fun setBattInfoBean(battInfoBean: BattInfoBean){
        mBsDbManger?.setCarInfo(battInfoBean.body.bike_id,battInfoBean)
    }

    fun setCarInfoBean(carInfoBean: CarInfoBean){
        mBsDbManger?.setCarInfo(carInfoBean.bike_id,carInfoBean)
    }

    fun setCarInfoBikeStatus(bikeStatusBean: BikeStatusBean){
        mBsDbManger?.setCarInfo(bikeStatusBean.getBody()!!.bike_id,bikeStatusBean)
    }

    fun getCarLoctionBean(bikeId:Int): CarLoctionBean?{
        return bsDbManger?.getCarInfo(bikeId)?.bikeLoctionInfo
    }

    fun getBattInfoBean(bikeId:Int): BattInfoBean?{
        val bikeBattertInfo = bsDbManger?.getCarInfo(bikeId)?.bikeBattertInfo
        return bsDbManger?.getCarInfo(bikeId)?.bikeBattertInfo
    }

    fun getCarInfoBean(bikeId:Int): CarInfoBean?{
        return bsDbManger?.getCarInfo(bikeId)?.bikeInfo
    }

    fun getCarStatus(bikeId:Int):BikeStatusBean?{
        return bsDbManger?.getCarInfo(bikeId)?.bikeStatus
    }

}