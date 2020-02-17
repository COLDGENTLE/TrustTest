package com.sharkgulf.soloera.db.bean

import com.sharkgulf.soloera.module.bean.BsGetUserInfoBean
import com.google.gson.Gson
import com.sharkgulf.soloera.module.bean.BsGetCarInfoBean
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

/*
 *Created by Trust on 2019/1/7
 * 用户登录状态
 */
@Entity
class DbUserLoginStatusBean {
    @Id
    var id:Long = 0

    var userPhone:String? = null

    var userLoginStatus:Boolean = false

    var userIsBindCar:Boolean = false

    var userNickName:String = "鲨仔"

    var userName:String = "某某"

    var userSalt:String? = null //用户密码加密密钥

    var userToken:String? = null

    var bidPos:Int? = null

    var isDemo:Boolean = false


    @Convert(converter = dbCarBean::class,dbType = String::class)
    var userBikeList: BsGetCarInfoBean.DataBean? = null //用户车辆列表

    var userId:Int = 0  //用户id

    var userCheckInNum:Int = 0


    @Convert(converter = dbUserBean::class,dbType = String::class)
    var userBean:BsGetUserInfoBean.DataBean.UserBean? = null

    class dbCarBean:PropertyConverter<BsGetCarInfoBean.DataBean, String>{
        override fun convertToDatabaseValue(entityProperty: BsGetCarInfoBean.DataBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): BsGetCarInfoBean.DataBean? {
            if (databaseValue == null) {
                return null
            }

            return Gson().fromJson(databaseValue,BsGetCarInfoBean.DataBean::class.java)
        }

    }

    class dbUserBean : PropertyConverter<BsGetUserInfoBean.DataBean.UserBean, String> {
        override fun convertToDatabaseValue(entityProperty: BsGetUserInfoBean.DataBean.UserBean?): String? {
            if (entityProperty == null) {
                return null
            }
            return Gson().toJson(entityProperty)
        }

        override fun convertToEntityProperty(databaseValue: String?): BsGetUserInfoBean.DataBean.UserBean? {
            if (databaseValue == null) {
                return null
            }
            return Gson().fromJson(databaseValue, BsGetUserInfoBean.DataBean.UserBean::class.java)
        }
    }
}