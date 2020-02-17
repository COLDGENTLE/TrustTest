package com.sharkgulf.soloera.db.bean

import com.google.gson.Gson
import com.sharkgulf.soloera.TrustAppConfig
import com.sharkgulf.soloera.module.bean.socketbean.BattInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.BikeStatusBean
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.module.bean.socketbean.CarLoctionBean
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

/**
 *  Created by user on 2019/8/15
 */
@Entity
class DbCarInfoBean {
    @Id
    var id:Long = 0

    var bikeId:Int = -1
    var userMapType :Int = TrustAppConfig.MAP_TYPE_GPS
    @Convert(converter = DbLoctionBean::class,dbType = String::class)
    var bikeLoctionInfo: CarLoctionBean? = null

    @Convert(converter = DbBattertBean::class,dbType = String::class)
    var bikeBattertInfo: BattInfoBean? = null

    @Convert(converter = DbCarInfosBean::class,dbType = String::class)
    var bikeInfo: CarInfoBean? = null

    @Convert(converter = DbBikeStatusBean::class,dbType = String::class)
    var bikeStatus:BikeStatusBean? = null


    class DbLoctionBean: PropertyConverter<CarLoctionBean, String> {
        override fun convertToDatabaseValue(entityProperty: CarLoctionBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): CarLoctionBean? {
            if (databaseValue == null) {
                return null
            }

            return Gson().fromJson(databaseValue,CarLoctionBean::class.java)
        }

    }

    class DbBattertBean: PropertyConverter<BattInfoBean, String> {
        override fun convertToDatabaseValue(entityProperty: BattInfoBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): BattInfoBean? {
            if (databaseValue == null) {
                return null
            }
            return Gson().fromJson(databaseValue,BattInfoBean::class.java)
        }

    }


    class DbCarInfosBean: PropertyConverter<CarInfoBean, String> {
        override fun convertToDatabaseValue(entityProperty: CarInfoBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): CarInfoBean? {
            if (databaseValue == null) {
                return null
            }
            return Gson().fromJson(databaseValue,CarInfoBean::class.java)
        }

    }

    class DbBikeStatusBean: PropertyConverter<BikeStatusBean, String> {
        override fun convertToDatabaseValue(entityProperty: BikeStatusBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): BikeStatusBean? {
            if (databaseValue == null) {
                return null
            }
            return Gson().fromJson(databaseValue,BikeStatusBean::class.java)
        }

    }
}