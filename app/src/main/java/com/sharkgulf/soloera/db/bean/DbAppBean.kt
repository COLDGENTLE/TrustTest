package com.sharkgulf.soloera.db.bean

import com.sharkgulf.soloera.TrustAppConfig.LOGIN_TYPE_SMS
import com.sharkgulf.soloera.tool.config.DEFULT
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 *  Created by user on 2019/9/24
 */
@Entity
class DbAppBean {
    @Id
    var id:Long = 0

    var appPrivacyPolicy:Boolean = false
    var loginType:Int = LOGIN_TYPE_SMS
    var threeLoginType:Int = DEFULT
    var isDebug:Boolean = false
    var isDevFirstInit:Boolean = false
    var deviceId:String? = null

}