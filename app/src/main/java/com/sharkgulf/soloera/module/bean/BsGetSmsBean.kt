package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/2
 */
class BsGetSmsBean {

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"captcha":""}
     */

    private var state: String? = null
    private var state_info: String? = null
    private var seq: Int = 0
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

    fun getSeq(): Int {
        return seq
    }

    fun setSeq(seq: Int) {
        this.seq = seq
    }

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean) {
        this.data = data
    }

    class DataBean {
        /**
         * captcha :
         */

        var captcha: String? = null
    }
}