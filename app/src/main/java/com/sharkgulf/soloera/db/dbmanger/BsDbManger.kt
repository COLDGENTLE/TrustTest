package com.sharkgulf.soloera.db.dbmanger

import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.appliction.BsApplication.Companion.macString
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.TrustAppConfig.DEMO_TOKEN
import com.sharkgulf.soloera.TrustAppConfig.MAP_TYPE_GPS
import com.sharkgulf.soloera.db.bean.*
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean
import com.sharkgulf.soloera.tool.config.DEFULT
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustLogUtils
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

/*
 *Created by Trust on 2019/1/7
 */
class BsDbManger (boxStore: BoxStore){
    private val mBoxStore = boxStore
    private var mUserLoginStatusBox:Box<DbUserLoginStatusBean>? = null
    private var mBleBox:Box<DbBleBean>? = null
    private var mCarInfoBox:Box<DbCarInfoBean>? = null
    private var mAlertBeanBox:Box<DbAlertBean>? = null
    private var mAppBeanBox:Box<DbAppBean>? = null
    private var mDbBleManger:BsBleDbManger? = null
    private var mDbCarInfoManger:BsCarInfoDbManger? = null
    private var mDbAlertBeanDbManger:BsAlertBeanDbManger? = null
    private var TAG = BsDbManger::class.java.canonicalName
    companion object {
        var mDbUserLoginStatusBean:DbUserLoginStatusBean? = null
    }

    init {
        mDbBleManger = BsBleDbManger(boxStore)
        mDbCarInfoManger = BsCarInfoDbManger(boxStore)
        mDbAlertBeanDbManger = BsAlertBeanDbManger(boxStore)
        mUserLoginStatusBox  = BsApplication.boxStore.boxFor()
        mBleBox = boxStore.boxFor()
        mCarInfoBox = boxStore.boxFor()
        mAlertBeanBox = boxStore.boxFor()
        mAppBeanBox = boxStore.boxFor()
    }


    fun setUserLoginStatus(phone:String, isLogin:Boolean,
                           isBindCar:Boolean, token:String? = null,
                           bikeList: BsGetCarInfoBean.DataBean? = null, userId:Int = 0, bean: BsGetUserInfoBean.DataBean.UserBean? = null){
        val loginList = mUserLoginStatusBox!!.find(DbUserLoginStatusBean_.userPhone, phone)
        if (!loginList.isEmpty()) {
            loginList.forEach {
                it.userIsBindCar = isBindCar
                it.userLoginStatus = isLogin
                it.userPhone = phone

                if (token != null) {
                    it.userToken = token
                }
                if (bikeList != null) {
                    it.userBikeList = bikeList
                }
                if (userId != 0) {
                    it.userId = userId
                }

                if(bean != null){
                    it.userBean = bean
                }
                mUserLoginStatusBox!!.put(it)
            }
        }else{
            val dbUserLoginStatus = DbUserLoginStatusBean()
            dbUserLoginStatus.userPhone = phone
            dbUserLoginStatus.userLoginStatus = isLogin
            dbUserLoginStatus.userIsBindCar = isBindCar
            if (token != null) {
                dbUserLoginStatus.userToken  = token
            }
            if (bikeList != null) {
                dbUserLoginStatus.userBikeList  = bikeList
            }

            if (userId != 0) {
                dbUserLoginStatus.userId = userId
            }

            if (bean != null) {
                dbUserLoginStatus.userBean = bean
            }
            mDbUserLoginStatusBean = dbUserLoginStatus
            mUserLoginStatusBox!!.put(dbUserLoginStatus)
        }
    }

    fun removeAll(){
        mUserLoginStatusBox!!.removeAll()
    }


    fun setUserLoginStatus(bean: DbUserLoginStatusBean){
        mDbUserLoginStatusBean = bean
        mUserLoginStatusBox!!.put(bean)
    }


    fun checkUserLoginStatus(phone:String): DbUserLoginStatusBean?{
        val loginList = mUserLoginStatusBox!!.find(DbUserLoginStatusBean_.userPhone, phone)
        if (loginList.isEmpty()) {
            return null
        }
        mDbUserLoginStatusBean =  loginList[0]
       return mDbUserLoginStatusBean
    }

    fun findDemoUser():DbUserLoginStatusBean?{
        val query = mUserLoginStatusBox!!.query()
        val find = query.equal(DbUserLoginStatusBean_.isDemo, true).build().find()
        return if(find.isEmpty()) null else find[0]
    }

    fun setDemoUser(bean:BsGetUserInfoBean){
        val query = mUserLoginStatusBox!!.query()
        val find = query.equal(DbUserLoginStatusBean_.isDemo, true).build().find()
        val usersBean = if(find.isEmpty()) DbUserLoginStatusBean() else find[0]
        val user = bean?.getData()?.user
        usersBean.userBean = user
        usersBean.userName = user?.nick_name!!
        usersBean.userPhone = user?.phone_num
        usersBean.isDemo = true
        usersBean.userId = user?.user_id
        usersBean.userToken = DEMO_TOKEN
        mUserLoginStatusBox!!.put(usersBean)
    }

    fun setDemoUserBikes(bean:BsGetCarInfoBean){
        val query = mUserLoginStatusBox!!.query()
        val find = query.equal(DbUserLoginStatusBean_.isDemo, true).build().find()
        val dbUserLoginStatusBean = find[0]
        dbUserLoginStatusBean.userBikeList = bean.data
        mUserLoginStatusBox!!.put(dbUserLoginStatusBean)
    }


    fun checkUserLoginStatus(): DbUserLoginStatusBean?{
        val all = mUserLoginStatusBox!!.all
        if (all.isEmpty()) {
            return null
        }

         all.forEach {
            if (it.userLoginStatus) {
                mDbUserLoginStatusBean = it
                return mDbUserLoginStatusBean
            }
        }
       return null
    }



    fun allUserExt(){
        val all = mUserLoginStatusBox!!.all
        all.forEach {
            it.userLoginStatus = false
            setUserLoginStatus(it)
        }
    }

    fun getRecentLoginUser():DbUserLoginStatusBean?{
        val all = mUserLoginStatusBox!!.all
        return if (all.isEmpty()) {
            null
        }else{
            all[all.size-1]
        }
    }


    fun setBleConfig(bsBindBean: BsGetBindInfoBean.DataBean.BindInfoBean){
        val btsign = bsBindBean.btsign!!
        val ble = mBleBox!!.find(DbBleBean_.mac,btsign.bt_mac!!)
        if (ble.isEmpty()) {
            val dbBle = DbBleBean()
            dbBle.mac = btsign.bt_mac!!
            dbBle.did = btsign.did!!
            dbBle.salt = btsign.salt!!
            dbBle.sign = btsign.sign!!
            dbBle.validation =btsign.validation!!
            dbBle.bikeId = bsBindBean.bike_info!!.getBike_id()
            mBleBox!!.put(dbBle)
        }else{
            ble.forEach {
                it.mac = btsign.bt_mac!!
                it.did = btsign.did!!
                it.salt = btsign.salt!!
                it.sign = btsign.sign!!
                it.validation =btsign.validation!!
                it.bikeId = bsBindBean.bike_info!!.getBike_id()
                mBleBox!!.put(it)
            }
        }
    }

    fun setBleConfig(bsBindBean: DbBleBean){
        mBleBox!!.put(bsBindBean)
    }

    fun clearBle(bikeId: Int){

        val bleBean = mBleBox!!.find(DbBleBean_.bikeId, bikeId.toLong())
        if (bleBean != null) {
            if (!bleBean.isEmpty()) {
                mBleBox!!.remove(bleBean[0].id)
            }
        }
    }


    fun setBleConfig(bikeId: Int,bsBindBean: BsBleSignBean){
        val btsign = bsBindBean.getData()?.btsign!!
        btsign.bt_mac = macString(btsign.bt_mac!!)
        val ble = mBleBox!!.find(DbBleBean_.mac,btsign.bt_mac)
        if (ble.isEmpty()) {
            val dbBle = DbBleBean()
            dbBle.mac = btsign.bt_mac!!
            dbBle.did = btsign.did!!
            dbBle.salt = btsign.salt!!
            dbBle.sign = btsign.sign!!
            dbBle.validation =btsign.validation!!
            dbBle.bikeId =bikeId
            mBleBox!!.put(dbBle)
        }else{
            ble.forEach {
                it.mac = btsign.bt_mac!!
                it.did = btsign.did!!
                it.salt = btsign.salt!!
                it.sign = btsign.sign!!
                it.validation =btsign.validation!!
                it.bikeId =bikeId
                mBleBox!!.put(it)
            }
        }
    }

    fun ClearBleData(bikeId:Int){
        val bleBean = mBleBox!!.find(DbBleBean_.bikeId,bikeId.toLong())
        if (bleBean != null) {
            mBleBox!!.remove(bleBean[0].id)
        }
    }





    fun getBleConfig(): DbBleBean?{
        val all = mBleBox!!.all
        if (all.isEmpty()) {
            return null
        }
        return all[all.size-1]
    }

    fun getBleConfig(bikeId: Int): DbBleBean?{
        val ble = mBleBox!!.find(DbBleBean_.bikeId,bikeId.toLong())
        if (ble.isEmpty()) {
            return null
        }
        return ble[ble.size-1]
    }



    fun setUserLoginStatus(bean:BsGetUserInfoBean){
        mDbUserLoginStatusBean = BsApplication.bsDbManger!!.checkUserLoginStatus(TrustAppConfig.userPhone)
        if (mDbUserLoginStatusBean != null) {
            mDbUserLoginStatusBean!!.userNickName = bean.getData()!!.user!!.nick_name!!
            mDbUserLoginStatusBean!!.userName = bean.getData()!!.user!!.real_name!!
            mDbUserLoginStatusBean!!.userBean = bean.getData()?.user
            BsApplication.bsDbManger!!.setUserLoginStatus(mDbUserLoginStatusBean!!)
        }else{
            mDbUserLoginStatusBean = DbUserLoginStatusBean()
            mDbUserLoginStatusBean!!.userNickName = bean.getData()!!.user!!.nick_name!!
            mDbUserLoginStatusBean!!.userName = bean.getData()!!.user!!.real_name!!
            mDbUserLoginStatusBean!!.userBean = bean.getData()?.user
            BsApplication.bsDbManger!!.setUserLoginStatus(mDbUserLoginStatusBean!!)
        }
    }


    fun getBikeInfo(bikeId: Int):BsGetCarInfoBean.DataBean.BikesBean?{
        val bikeList = mDbUserLoginStatusBean?.userBikeList!!.bikes
        bikeList!!.forEach {
            if (bikeId == it.bike_id) {
                return it
            }
        }
        return null
    }


    fun getCarInfo(bikeId: Int):DbCarInfoBean?{
        val carBean = mCarInfoBox!!.find(DbCarInfoBean_.bikeId, bikeId.toLong())
        return if(carBean.isEmpty()) null else carBean[0]
    }


    fun setCarInfo(bikeId: Int,any: Any){
        var carInfo = getCarInfo(bikeId)
        if (carInfo == null) {
            carInfo = DbCarInfoBean()
            carInfo.bikeId = bikeId
        }
        when (any) {
            is BattInfoBean -> {
                carInfo.bikeBattertInfo = any
            }
            is CarLoctionBean -> {
                carInfo.bikeLoctionInfo = any
            }
            is CarInfoBean -> {
                TrustLogUtils.d(TAG,"添加bike Info bikeId:${bikeId}")
                carInfo.bikeInfo = any
            }
            is BikeStatusBean -> {
                carInfo.bikeStatus = any
            }
            is BikeStatusBean ->{
                carInfo.bikeStatus = any
            }
            else -> {
            }
        }

        TrustLogUtils.d(TAG,"添加bike  bikeId:$bikeId :${carInfo?.bikeInfo?.bike_name}  :${carInfo?.bikeInfo?.status}")
        mCarInfoBox?.put(carInfo)
    }

    fun setBikeStatus(bikeId: Int,bean: BikeStatusBean){
        var carInfo = getCarInfo(bikeId)
        if (carInfo == null) {
            carInfo = DbCarInfoBean()
            carInfo.bikeId = bikeId
        }
        carInfo.bikeStatus = bean
        mCarInfoBox?.put(carInfo)
    }

    fun setAlertBean(day:String,bean:BsAlertBean):Boolean{
        val list = bean.data?.list
        if (list != null) {
            val bikeId = list[0].bid
            val ts = list[0].ts
            val dbBean = findData(bikeId,day)
            val alertBean = if (dbBean != null && dbBean.isNotEmpty()) { dbBean[0] }else{ DbAlertBean() }
            val oldBean = alertBean.alerBean
            if (oldBean != null) {
                val oldList = oldBean.data.list
                val list = bean.data.list
                if(oldList.size >0){
                    val newList = arrayListOf<BsAlertBean.DataBean.ListBean>()
                    val oldlistBean = oldList[0]
                    list.forEach {
                        if(it.ts >= oldlistBean.ts && it.uuid != oldlistBean.uuid){
                            newList.add(it)
                        }
                    }
                    if (newList.isNotEmpty()) {
                        alertBean.alerBean!!.data.list.addAll(0,newList)
                    }
                }

            }else{
                alertBean.alerBean = bean
            }
            alertBean.day = TrustTools.getTime(ts * 1000L)
            alertBean.bikeId = bikeId
            mAlertBeanBox?.put(alertBean)
        }
        return mAlertBeanBox != null
    }

    fun getAlertBean(bikeId: Int,day:String,isRead:Boolean = false):Int?{
        val list = findData(bikeId,day)
        return if (list == null || list.isEmpty()) { null }
        else{
            if (list[0].day == day) {
                val timeList = list[0].alerBean!!.data.list
                if (timeList.isEmpty()) { null }else{ timeList[0].ts }
            }else{
                return null
            }
        }
    }

    fun getBsAlertBean(bikeId: Int,day:String,isRead:Boolean = false):DbAlertBean?{
        val find = findData(bikeId,day)
        return if (find == null || find.isEmpty()) { null }
        else{
            val alerBean = find[0]
            if (alerBean.day == day) {
                alerBean.isRead = isRead
                mAlertBeanBox?.put(alerBean)
                alerBean
            }else{
                return null
            }
        }
    }

    fun getMapType(bikeId: Int):Int{
        val carInfo = getCarInfo(bikeId)
        return carInfo?.userMapType ?: MAP_TYPE_GPS
    }

    fun setMapType(bikeId: Int,mapType:Int){
        val carInfo = getCarInfo(bikeId)
        if (carInfo != null) {
            carInfo.userMapType = mapType
            mCarInfoBox!!.put(carInfo)
        }
    }

    fun getDbAppBean ():DbAppBean{
        val all = mAppBeanBox?.all
        val dbAppBean = if (all != null) {
            if (all.isEmpty()) {
                val bean = DbAppBean()
                mAppBeanBox?.put(bean)
                bean
            }else{
                all[0]!!
            }
        }else{
            val bean = DbAppBean()
            mAppBeanBox?.put(bean)
            bean
        }

        return dbAppBean
    }

    fun setDbAppBean(bean:DbAppBean){
        mAppBeanBox?.put(bean)
    }


    fun getDbBleMagner():BsBleDbManger{
        return mDbBleManger!!
    }

    fun getDbCarInfoManger():BsCarInfoDbManger{
        return mDbCarInfoManger!!
    }

    fun getDbAlertDbManger():BsAlertBeanDbManger{
        return mDbAlertBeanDbManger!!
    }


    private fun findData(bikeId: Int,day:String):List<DbAlertBean>?{
        if (bikeId == DEFULT) {
            return findData(day)
        }else{
            mAlertBeanBox?.query()?.equal(DbAlertBean_.bikeId,bikeId.toLong())?.notEqual(DbAlertBean_.day, TrustTools.getTime(System.currentTimeMillis()))?.build()?.remove()
            return mAlertBeanBox?.query()?.equal(DbAlertBean_.bikeId, bikeId.toLong())
                    ?.equal(DbAlertBean_.day, TrustTools.getTime(System.currentTimeMillis()))?.build()?.find()
        }

    }

    private fun findData(day:String):List<DbAlertBean>?{
        mAlertBeanBox?.query()?.notEqual(DbAlertBean_.day,day)?.build()?.remove()
        return mAlertBeanBox?.query()?.equal(DbAlertBean_.day,day)?.build()?.find()
    }
}