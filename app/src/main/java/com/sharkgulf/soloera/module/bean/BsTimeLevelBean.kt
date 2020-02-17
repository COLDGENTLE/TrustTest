package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/4/18
 */
class BsTimeLevelBean {
    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"time":[{"begin_date":"2019-01-01","end_date":"2019-01-01","display":"2019-01-01","month":"2019-01-01","week":"2018-12-31"},{"begin_date":"2019-01-02","end_date":"2019-01-02","display":"2019-01-02","month":"2019-01-01","week":"2018-12-31"}]}
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
        var time: List<TimeBean>? = null

        class TimeBean {
            /**
             * begin_date : 2019-01-01
             * end_date : 2019-01-01
             * display : 2019-01-01
             * month : 2019-01-01
             * week : 2018-12-31
             */

            var begin_date: String? = null
            var end_date: String? = null
            var display: String? = null
            var month: String? = null
            var week: String? = null
        }
    }
}