package com.sharkgulf.soloera.presenter.messager

/*
 *Created by Trust on 2018/12/26
 */
interface IMessagerPresenterListener {
    fun requestAlertList(map:HashMap<String,Any>)
    fun getAlertList(map:HashMap<String,Any>)
}