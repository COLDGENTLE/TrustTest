package com.sharkgulf.soloera.db.bean

import com.google.gson.Gson
import com.sharkgulf.soloera.module.bean.BsAlertBean
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

/**
 *  Created by user on 2019/9/9
 */
@Entity
class DbAlertBean {
    @Id
    var id:Long = 0

    var bikeId :Int = -1

    var day:String? = null

    var isRead:Boolean = false

    @Convert(converter = DbBsAlertBean::class,dbType = String::class)
    var alerBean: BsAlertBean? = null


    class DbBsAlertBean: PropertyConverter<BsAlertBean, String> {
        override fun convertToDatabaseValue(entityProperty: BsAlertBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): BsAlertBean? {
            if (databaseValue == null) {
                return null
            }

            return Gson().fromJson(databaseValue,BsAlertBean::class.java)
        }

    }
}