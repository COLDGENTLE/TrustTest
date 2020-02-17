package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/10
 */
class BsDelCarBean {

    /**
     * state : 0000
     * state_info : OK
     * cust_info :
     * seq : 1234
     */

    private var state: String? = null
    private var state_info: String? = null
    private var cust_info: String? = null
    private var seq: Int = 0

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

    fun getCust_info(): String? {
        return cust_info
    }

    fun setCust_info(cust_info: String) {
        this.cust_info = cust_info
    }

    fun getSeq(): Int {
        return seq
    }

    fun setSeq(seq: Int) {
        this.seq = seq
    }
}