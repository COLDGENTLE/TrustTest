package com.sharkgulf.soloera.presenter.securitysettings

/**
 *  Created by user on 2019/8/19
 */
interface ISecuritySettingsPresentersListener {
    fun changeSecurity(map:HashMap<String,Any>)
    fun testNotify(map:HashMap<String,Any>)
    fun lostModu(map:HashMap<String,Any>)
}