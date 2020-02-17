package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/2
 */
class BsGetUserKeyBean {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"salt":"1234"}
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
         * salt : 1234
         */

        var salt: String? = null
    }
}