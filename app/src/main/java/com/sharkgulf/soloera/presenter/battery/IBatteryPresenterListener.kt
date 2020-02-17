package com.sharkgulf.soloera.presenter.battery

/**
 *  Created by user on 2019/3/5
 */
interface IBatteryPresenterListener {
    fun requestBatteryInfo(map:HashMap<String,Any>?)
    fun setTAG(tag: String)
}