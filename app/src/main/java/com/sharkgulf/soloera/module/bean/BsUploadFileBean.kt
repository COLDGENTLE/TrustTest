package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/3/4
 */
class BsUploadFileBean {
    /**
     * state : 00
     * state_info : 成功
     * data : {"urls":["/user/info/head/141d4a2f-fe59-43ed-b65d-aa24bed80451"]}
     */

    private var state: String? = null
    private var state_info: String? = null
    private var data: DataBean? = null

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

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean) {
        this.data = data
    }

    class DataBean {
        var urls: List<String>? = null
    }
}