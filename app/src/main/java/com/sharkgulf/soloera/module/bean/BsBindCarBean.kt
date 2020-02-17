package com.sharkgulf.soloera.module.bean

/*
 *Created by Trust on 2019/1/5
 */
class BsBindCarBean {
    /**
     * state : 0000
     * state_info : OK
     * cust_info :
     * seq : 1234
     * data : {"btsign":{"salt":"abcde","did":"abcdef","bt_mac":"ffffff","validation":"123456","sign":"abcdefgh"},"bike_info":{"bike_id":54321,"bike_name":"蓝鲨M6","status":1,"brand":{"brand_id":123,"brand_name":"蓝鲨","logo":"https://pic.bluesharkmotor.com/bs.png"},"model":{"model_id":456,"model_name":"M6","pic_b":"https://pic.bluesharkmotor.com/m6b.png","pic_s":"https://pic.bluesharkmotor.com/m6s.png"},"color":"蓝","color_code":"#0000ff","pic_b":"https://pic.bluesharkmotor.com/m6blueb.png","pic_s":"https://pic.bluesharkmotor.com/m6blueb.png"}}
     */

    private var state: String? = null
    private var state_info: String? = null
    private var cust_info: String? = null
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

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean) {
        this.data = data
    }

    class DataBean {
        /**
         * btsign : {"salt":"abcde","did":"abcdef","bt_mac":"ffffff","validation":"123456","sign":"abcdefgh"}
         * bike_info : {"bike_id":54321,"bike_name":"蓝鲨M6","status":1,"brand":{"brand_id":123,"brand_name":"蓝鲨","logo":"https://pic.bluesharkmotor.com/bs.png"},"model":{"model_id":456,"model_name":"M6","pic_b":"https://pic.bluesharkmotor.com/m6b.png","pic_s":"https://pic.bluesharkmotor.com/m6s.png"},"color":"蓝","color_code":"#0000ff","pic_b":"https://pic.bluesharkmotor.com/m6blueb.png","pic_s":"https://pic.bluesharkmotor.com/m6blueb.png"}
         */

        var btsign: BtsignBean? = null
        var bike_info: BikeInfoBean? = null

        class BtsignBean {
            /**
             * salt : abcde
             * did : abcdef
             * bt_mac : ffffff
             * validation : 123456
             * sign : abcdefgh
             */

            var salt: String? = null
            var did: String? = null
            var bt_mac: String? = null
            var validation: String? = null
            var sign: String? = null
        }

        class BikeInfoBean {
            /**
             * bike_id : 54321
             * bike_name : 蓝鲨M6
             * status : 1
             * brand : {"brand_id":123,"brand_name":"蓝鲨","logo":"https://pic.bluesharkmotor.com/bs.png"}
             * model : {"model_id":456,"model_name":"M6","pic_b":"https://pic.bluesharkmotor.com/m6b.png","pic_s":"https://pic.bluesharkmotor.com/m6s.png"}
             * color : 蓝
             * color_code : #0000ff
             * pic_b : https://pic.bluesharkmotor.com/m6blueb.png
             * pic_s : https://pic.bluesharkmotor.com/m6blueb.png
             */

            var bike_id: Int = 0
            var bike_name: String? = null
            var status: Int = 0
            var brand: BrandBean? = null
            var model: ModelBean? = null
            var color: String? = null
            var color_code: String? = null
            var pic_b: String? = null
            var pic_s: String? = null

            class BrandBean {
                /**
                 * brand_id : 123
                 * brand_name : 蓝鲨
                 * logo : https://pic.bluesharkmotor.com/bs.png
                 */

                var brand_id: Int = 0
                var brand_name: String? = null
                var logo: String? = null
            }

            class ModelBean {
                /**
                 * model_id : 456
                 * model_name : M6
                 * pic_b : https://pic.bluesharkmotor.com/m6b.png
                 * pic_s : https://pic.bluesharkmotor.com/m6s.png
                 */

                var model_id: Int = 0
                var model_name: String? = null
                var pic_b: String? = null
                var pic_s: String? = null
            }
        }
    }
}