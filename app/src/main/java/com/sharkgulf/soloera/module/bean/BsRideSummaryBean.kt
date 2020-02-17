package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/4/18
 */
class BsRideSummaryBean {
//    /**
//     * state : 00
//     * state_info : 成功
//     * seq : 0
//     * data : {"ride_summary":[{"miles":168,"times":28736423,"avg_speed":32,"max_speed":48,"ride_count":14,"alert_count":9},{"miles":168,"times":28736423,"avg_speed":32,"max_speed":48,"ride_count":14,"alert_count":9}]}
//     */
//
//    private var state: String? = null
//    private var state_info: String? = null
//    private var seq: Int = 0
//    private var data: DataBean? = null
//
//    fun getState(): String? {
//        return state
//    }
//
//    fun setState(state: String) {
//        this.state = state
//    }
//
//    fun getState_info(): String? {
//        return state_info
//    }
//
//    fun setState_info(state_info: String) {
//        this.state_info = state_info
//    }
//
//    fun getSeq(): Int {
//        return seq
//    }
//
//    fun setSeq(seq: Int) {
//        this.seq = seq
//    }
//
//    fun getData(): DataBean? {
//        return data
//    }
//
//    fun setData(data: DataBean) {
//        this.data = data
//    }
//
//    class DataBean {
//        var ride_summary: List<RideSummaryBean>? = null
//
//        class RideSummaryBean {
//            /**
//             * miles : 168
//             * times : 28736423
//             * avg_speed : 32
//             * max_speed : 48
//             * ride_count : 14
//             * alert_count : 9
//             */
//
//            var miles: Int = 0
//            var times: Int = 0
//            var avg_speed: Int = 0
//            var max_speed: Int = 0
//            var ride_count: Int = 0
//            var alert_count: Int = 0
//        }
//    }

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"ride_summary":[{"date":"2019-10-25","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-26","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-27","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-28","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-29","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-30","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0}],"max_miles":0}
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
         * ride_summary : [{"date":"2019-10-25","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-26","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-27","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-28","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-29","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0},{"date":"2019-10-30","miles":0,"times":0,"avg_speed":0,"max_speed":0,"ride_count":0,"alert_count":0}]
         * max_miles : 0
         */

        var max_miles: Int = 0
        var ride_summary: List<RideSummaryBean>? = null

        class RideSummaryBean {
            /**
             * date : 2019-10-25
             * miles : 0
             * times : 0
             * avg_speed : 0
             * max_speed : 0
             * ride_count : 0
             * alert_count : 0
             */

            var date: String? = null
            var miles: Int = 0
            var times: Int = 0
            var avg_speed: Int = 0
            var max_speed: Int = 0
            var ride_count: Int = 0
            var alert_count: Int = 0
        }
    }
}