package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/3/5
 */
class BsBatteryInfoBean{

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"emer_batt":{"left_days":4,"time_desc":"刚刚"},"batt1":{"info":{"sn":"BAT_A1234567890","type":0,"rated_v":60,"rated_c":20,"cycle":0,"loss":0,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.1"},"status":{"in_use":1,"charge":1,"charge_es":0,"temp":1,"vol":48,"mile_es":1111,"capacity":90}},"batt2":{"info":{"sn":"BAT_B1234567890------","type":0,"rated_v":70,"rated_c":30,"cycle":11,"loss":20,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.2"},"status":{"in_use":1,"charge":0,"charge_es":0,"temp":55,"vol":49,"mile_es":88,"capacity":85}}}
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
         * emer_batt : {"left_days":4,"time_desc":"刚刚"}
         * batt1 : {"info":{"sn":"BAT_A1234567890","type":0,"rated_v":60,"rated_c":20,"cycle":0,"loss":0,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.1"},"status":{"in_use":1,"charge":1,"charge_es":0,"temp":1,"vol":48,"mile_es":1111,"capacity":90}}
         * batt2 : {"info":{"sn":"BAT_B1234567890------","type":0,"rated_v":70,"rated_c":30,"cycle":11,"loss":20,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.2"},"status":{"in_use":1,"charge":0,"charge_es":0,"temp":55,"vol":49,"mile_es":88,"capacity":85}}
         */

        var emer_batt: EmerBattBean? = null
        var batt1: Batt1Bean? = null
        var batt2: Batt2Bean? = null

        class EmerBattBean {
            /**
             * left_days : 4
             * time_desc : 刚刚
             */

            var left_days: Int = 0
            var time_desc: String? = null
        }

        class Batt1Bean {
            /**
             * info : {"sn":"BAT_A1234567890","type":0,"rated_v":60,"rated_c":20,"cycle":0,"loss":0,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.1"}
             * status : {"in_use":1,"charge":1,"charge_es":0,"temp":1,"vol":48,"mile_es":1111,"capacity":90}
             */

            var info: InfoBean? = null
            var status: StatusBean? = null

            class InfoBean {
                /**
                 * sn : BAT_A1234567890
                 * type : 0
                 * rated_v : 60
                 * rated_c : 20
                 * cycle : 0
                 * loss : 0
                 * prod_date : 2019-04-16 14:28:26
                 * version : BMS1.1.1
                 */

                var sn: String? = null
                var type: Int = 0
                var rated_v: Int = 0
                var rated_c: Int = 0
                var cycle: Int = 0
                var loss: Int = 0
                var prod_date: String? = null
                var version: String? = null
            }

            class StatusBean {
                /**
                 * in_use : 1
                 * charge : 1
                 * charge_es : 0
                 * temp : 1
                 * vol : 48
                 * mile_es : 1111
                 * capacity : 90
                 */

                var in_use: Int = 0
                var charge: Int = 0
                var charge_es: Int = 0
                var temp: Int = 0
                var vol: Int = 0
                var mile_es: Int = 0
                var capacity: Int = 0
            }
        }

        class Batt2Bean {
            /**
             * info : {"sn":"BAT_B1234567890------","type":0,"rated_v":70,"rated_c":30,"cycle":11,"loss":20,"prod_date":"2019-04-16 14:28:26","version":"BMS1.1.2"}
             * status : {"in_use":1,"charge":0,"charge_es":0,"temp":55,"vol":49,"mile_es":88,"capacity":85}
             */

            var info: InfoBeanX? = null
            var status: StatusBeanX? = null

            class InfoBeanX {
                /**
                 * sn : BAT_B1234567890------
                 * type : 0
                 * rated_v : 70
                 * rated_c : 30
                 * cycle : 11
                 * loss : 20
                 * prod_date : 2019-04-16 14:28:26
                 * version : BMS1.1.2
                 */

                var sn: String? = null
                var type: Int = 0
                var rated_v: Int = 0
                var rated_c: Int = 0
                var cycle: Int = 0
                var loss: Int = 0
                var prod_date: String? = null
                var version: String? = null
            }

            class StatusBeanX {
                /**
                 * in_use : 1
                 * charge : 0
                 * charge_es : 0
                 * temp : 55
                 * vol : 49
                 * mile_es : 88
                 * capacity : 85
                 */

                var in_use: Int = 0
                var charge: Int = 0
                var charge_es: Int = 0
                var temp: Int = 0
                var vol: Int = 0
                var mile_es: Int = 0
                var capacity: Int = 0
            }
        }
    }
}