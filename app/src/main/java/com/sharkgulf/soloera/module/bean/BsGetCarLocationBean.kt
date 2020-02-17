package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/5
 */
class BsGetCarLocationBean {
    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"positions":[{"bike_id":4,"gps":{"ts":1552362850,"lng":121.389774,"lat":31.168932,"desc":"上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)","level":1,"update_desc":{"diff":224,"style":"#000000","text":"4小时前"}},"bs":{"ts":1552362850,"lng":121.389774,"lat":31.168932,"desc":"上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)","level":0,"radius":1,"update_desc":{"diff":224,"style":"#000000","text":"4小时前"}},"ol_status":1}]}
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
        var positions: List<PositionsBean>? = null

        class PositionsBean {
            /**
             * bike_id : 4
             * gps : {"ts":1552362850,"lng":121.389774,"lat":31.168932,"desc":"上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)","level":1,"update_desc":{"diff":224,"style":"#000000","text":"4小时前"}}
             * bs : {"ts":1552362850,"lng":121.389774,"lat":31.168932,"desc":"上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)","level":0,"radius":1,"update_desc":{"diff":224,"style":"#000000","text":"4小时前"}}
             * ol_status : 1
             */

            var bike_id: Int = 0
            var gps: GpsBean? = null
            var bs: BsBean? = null
            var ol_status: Int = 0

            class GpsBean {
                /**
                 * ts : 1552362850
                 * lng : 121.389774
                 * lat : 31.168932
                 * desc : 上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)
                 * level : 1
                 * update_desc : {"diff":224,"style":"#000000","text":"4小时前"}
                 */

                var ts: Int = 0
                var lng: Double = 0.toDouble()
                var lat: Double = 0.toDouble()
                var desc: String? = null
                var level: Int = 0
                var update_desc: UpdateDescBean? = null

                class UpdateDescBean {
                    /**
                     * diff : 224
                     * style : #000000
                     * text : 4小时前
                     */

                    var diff: Int = 0
                    var style: String? = null
                    var text: String? = null
                }
            }

            class BsBean {
                /**
                 * ts : 1552362850
                 * lng : 121.389774
                 * lat : 31.168932
                 * desc : 上海市闵行区虹桥镇宜山路1722号汉庭酒店(上海漕河泾宜山路店)
                 * level : 0
                 * radius : 1
                 * update_desc : {"diff":224,"style":"#000000","text":"4小时前"}
                 */

                var ts: Int = 0
                var lng: Double = 0.toDouble()
                var lat: Double = 0.toDouble()
                var desc: String? = null
                var level: Int = 0
                var radius: Int = 0
                var update_desc: UpdateDescBeanX? = null

                class UpdateDescBeanX {
                    /**
                     * diff : 224
                     * style : #000000
                     * text : 4小时前
                     */

                    var diff: Int = 0
                    var style: String? = null
                    var text: String? = null
                }
            }
        }
    }
}