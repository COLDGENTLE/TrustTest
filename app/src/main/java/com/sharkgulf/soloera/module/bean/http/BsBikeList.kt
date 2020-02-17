package com.sharkgulf.soloera.module.bean.http

/**
 * Created by Trust on 2019/11/5
 */
class BsBikeList {
    private var bikes: List<BikesBean>? = null

    fun getBikes(): List<BikesBean>? {
        return bikes
    }

    fun setBikes(bikes: List<BikesBean>) {
        this.bikes = bikes
    }

    class BikesBean {
        /**
         * bike_id : 5
         * owner_id : 0
         * cc_id : 0
         * bike_name : 蓝鲨正式车0010
         * status : 1
         * brand : {"brand_id":1,"brand_name":"蓝鲨","logo":""}
         * model : {"model_id":1,"model_name":"正式车","pic_b":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png","pic_s":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png","pic_side":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png","ctrl_tmpl":3}
         * color : 绿
         * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png
         * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png
         * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png
         * ctrl_tmpl : 3
         * batt_support : 2
         * vin : 123213213
         * plate_num :
         * bind_days : 1
         * bike_class : 0
         * completion : 0
         * base : {"mac":"002715000010","imei":"866274030001220","sn":"G510AAA17070300122","imsi":"460041210304549"}
         * binded_time : 2019-08-28 09:42:51
         * activated_time : 2019-08-28 09:44:39
         * order : 691
         * total_miles : 87
         * func : {"e_sidestand":0,"saddle_sensor":0,"ctrl_tmpl":0}
         * security : {"mode":2,"custom":{"batt_out":true,"move":true,"vibr_severe":true}}
         */

        var bike_id: Int = 0
        var owner_id: Int = 0
        var cc_id: Int = 0
        var bike_name: String? = null
        var status: Int = 0
        var brand: BrandBean? = null
        var model: ModelBean? = null
        var color: String? = null
        var pic_b: String? = null
        var pic_s: String? = null
        var pic_side: String? = null
        var ctrl_tmpl: Int = 0
        var batt_support: Int = 0
        var vin: String? = null
        var plate_num: String? = null
        var bind_days: Int = 0
        var bike_class: Int = 0
        var completion: Int = 0
        var base: BaseBean? = null
        var binded_time: String? = null
        var activated_time: String? = null
        var order: Int = 0
        var total_miles: Int = 0
        var func: FuncBean? = null
        var security: SecurityBean? = null

        class BrandBean {
            /**
             * brand_id : 1
             * brand_name : 蓝鲨
             * logo :
             */

            var brand_id: Int = 0
            var brand_name: String? = null
            var logo: String? = null
        }

        class ModelBean {
            /**
             * model_id : 1
             * model_name : 正式车
             * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png
             * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png
             * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png
             * ctrl_tmpl : 3
             */

            var model_id: Int = 0
            var model_name: String? = null
            var pic_b: String? = null
            var pic_s: String? = null
            var pic_side: String? = null
            var ctrl_tmpl: Int = 0
        }

        class BaseBean {
            /**
             * mac : 002715000010
             * imei : 866274030001220
             * sn : G510AAA17070300122
             * imsi : 460041210304549
             */

            var mac: String? = null
            var imei: String? = null
            var sn: String? = null
            var imsi: String? = null
        }

        class FuncBean {
            /**
             * e_sidestand : 0
             * saddle_sensor : 0
             * ctrl_tmpl : 0
             */

            var e_sidestand: Int = 0
            var saddle_sensor: Int = 0
            var ctrl_tmpl: Int = 0
        }

        class SecurityBean {
            /**
             * mode : 2
             * custom : {"batt_out":true,"move":true,"vibr_severe":true}
             */

            var mode: Int = 0
            var custom: CustomBean? = null

            class CustomBean {
                /**
                 * batt_out : true
                 * move : true
                 * vibr_severe : true
                 */

                var isBatt_out: Boolean = false
                var isMove: Boolean = false
                var isVibr_severe: Boolean = false
            }
        }
    }
}