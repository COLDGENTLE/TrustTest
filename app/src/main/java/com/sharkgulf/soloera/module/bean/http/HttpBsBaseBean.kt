package com.sharkgulf.soloera.module.bean.http

/**
 * Created by Trust on 2019/11/5
 */
open class HttpBsBaseBean<T> {
    internal var state: String? = null
    internal var state_info: String? = null
    internal var data: T? = null

    fun getState(): String? {
        return state
    }

    fun setState(state: String) {
        this.state = state
    }

    fun getState_info(): String? {
        return state_info
    }

    fun setState_info(state_info: String) {
        this.state_info = state_info
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T) {
        this.data = data
    }
}