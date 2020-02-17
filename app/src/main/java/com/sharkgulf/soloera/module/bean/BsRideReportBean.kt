package com.sharkgulf.soloera.module.bean

import java.io.Serializable

/**
 *  Created by user on 2019/4/18
 */
class BsRideReportBean :Serializable{
    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"rides":[{"ts":1572624000000000000,"ts_display":"00:00","track_id":76264,"begin_time":"2019-11-02 00:00:00","end_time":"2019-11-02 09:50:06","begin_pos":"","end_pos":"","miles":18893,"times":15785708,"avg_speed":4,"max_speed":37,"min_speed":1,"carbon_save":3778}],"alerts":null}
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

    class DataBean :Serializable{
        /**
         * rides : [{"ts":1572624000000000000,"ts_display":"00:00","track_id":76264,"begin_time":"2019-11-02 00:00:00","end_time":"2019-11-02 09:50:06","begin_pos":"","end_pos":"","miles":18893,"times":15785708,"avg_speed":4,"max_speed":37,"min_speed":1,"carbon_save":3778}]
         * alerts : null
         */

        var rides: List<RidesBean>? = null

        class RidesBean:Serializable {
            /**
             * ts : 1572624000000000000
             * ts_display : 00:00
             * track_id : 76264
             * begin_time : 2019-11-02 00:00:00
             * end_time : 2019-11-02 09:50:06
             * begin_pos :
             * end_pos :
             * miles : 18893
             * times : 15785708
             * avg_speed : 4
             * max_speed : 37
             * min_speed : 1
             * carbon_save : 3778
             */

            var ts: Long = 0
            var ts_display: String? = null
            var track_id: Int = 0
            var begin_time: String? = null
            var end_time: String? = null
            var begin_pos: String? = null
            var end_pos: String? = null
            var miles: Int = 0
            var times: Int = 0
            var avg_speed: Int = 0
            var max_speed: Int = 0
            var min_speed: Int = 0
            var carbon_save: Int = 0
        }
    }
}