package com.sharkgulf.soloera

import com.google.gson.Gson
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter



/*
 *Created by Trust on 2018/12/11
 */
@Entity
class TestBean {
    @Id var id:Long = 0
    @Convert(converter = Test::class,dbType = String::class)
    var test:bean? = null





    class Test :PropertyConverter<bean,String>{
        override fun convertToDatabaseValue(entityProperty: bean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): bean? {
            if (databaseValue == null) {
                return null

            }

            return Gson().fromJson<bean>(databaseValue,bean::class.java)
        }
    }


    class bean{
        var name:String = "this is test"
    }
}