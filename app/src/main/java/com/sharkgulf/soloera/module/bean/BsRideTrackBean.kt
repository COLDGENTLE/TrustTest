package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/4/18
 */
class BsRideTrackBean {

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"locs":[{"lng":121.385992,"lat":31.170008,"speed":3,"ts":1574923567}]}
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
        var locs: List<LocsBean>? = null

        class LocsBean {
            /**
             * lng : 121.385992
             * lat : 31.170008
             * speed : 3
             * ts : 1574923567
             */

            var lng: Double = 0.toDouble()
            var lat: Double = 0.toDouble()
            var speed: Int = 0
            var ts: Int = 0
        }
    }
}