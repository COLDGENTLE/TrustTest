package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/2/19
 */
class BsCheckSmsvcBean {

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : null
     */

    private var state: String? = null
    private var state_info: String? = null
    private var seq: Int = 0
    private var data: Any? = null

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

    fun getData(): Any? {
        return data
    }

    fun setData(data: Any) {
        this.data = data
    }
}