package com.sharkgulf.soloera.module.bean

/**
 *  Created by user on 2019/1/29
 */
class BsGetBindInfoBean {

    /**
     * state : 00
     * state_info : OK
     * seq : 0
     * data : {"bind_info":[{"btsign":{"salt":"f75fb6042cfe45ae585c9cece5427437","did":"BC5DEC61C7355A3CC29BAA4C4F0D382C","bt_mac":"C375A5D61E64","validation":"3600","sign":"57021676448f3bbaf1deb424799fecad"},
     * "bike_info":{"bike_id":4,"bike_name":"蓝鲨M6","status":0,"brand":{"brand_id":1,"brand_name":"蓝鲨","logo":""},"model":{"model_id":1,"model_name":"M6","pic_b":"","pic_s":""},"color":"黄","pic_b":"","pic_s":""}}]}
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
        var bind_info: List<BindInfoBean>? = null

        class BindInfoBean {
            /**
             * btsign : {"salt":"f75fb6042cfe45ae585c9cece5427437","did":"BC5DEC61C7355A3CC29BAA4C4F0D382C","bt_mac":"C375A5D61E64","validation":"3600","sign":"57021676448f3bbaf1deb424799fecad"}
             * bike_info : {"bike_id":4,"bike_name":"蓝鲨M6","status":0,"brand":{"brand_id":1,"brand_name":"蓝鲨","logo":""},"model":{"model_id":1,"model_name":"M6","pic_b":"","pic_s":""},"color":"黄","pic_b":"","pic_s":""}
             */

            var btsign: BtsignBean? = null
            var bike_info: BikeInfoBean? = null

            class BtsignBean {
                /**
                 * salt : f75fb6042cfe45ae585c9cece5427437
                 * did : BC5DEC61C7355A3CC29BAA4C4F0D382C
                 * bt_mac : C375A5D61E64
                 * validation : 3600
                 * sign : 57021676448f3bbaf1deb424799fecad
                 */

                var salt: String? = null
                var did: String? = null
                var bt_mac: String? = null
                var validation: String? = null
                var sign: String? = null

            }

            class BikeInfoBean {
                /**
                 * bike_id : 13
                 * owner_id : 0
                 * cc_id : 10012
                 * bike_name :
                 * status : 0
                 * brand : {"brand_id":1,"brand_name":"蓝鲨","logo":""}
                 * model : {"model_id":2,"model_name":"Robor lite","pic_b":"https://api.d.bluesharkmotor.com/file/bike/info/model/2-big.png","pic_s":"https://api.d.bluesharkmotor.com/file/bike/info/model/2-small.png","pic_side":"https://api.d.bluesharkmotor.com/file/bike/info/model/2-side.png","ctrl_tmpl":2}
                 * color : w
                 * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/2-big.png
                 * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/2-small.png
                 * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/2-side.png
                 * ctrl_tmpl : 2
                 * batt_support : 2
                 * vin : vin-00271500046E
                 * plate_num :
                 * bind_days : 1
                 * bike_class : 0
                 * completion : 0
                 * base : {"mac":"00271500046E","imei":"862587030282027","sn":"G510GDA18030100054","imsi":"460041226506277"}
                 * binded_time : 0001-01-01 00:00:00
                 * activated_time : 2019-09-26 02:26:51
                 * order : 0
                 * total_miles : 87
                 * func : {"e_sidestand":0,"saddle_sensor":0}
                 * security : {"mode":0,"custom":{}}
                 */

                private var bike_id: Int = 0
                private var owner_id: Int = 0
                private var cc_id: Int = 0
                private var bike_name: String? = null
                private var status: Int = 0
                private var brand: BrandBean? = null
                private var model: ModelBean? = null
                private var color: String? = null
                private var pic_b: String? = null
                private var pic_s: String? = null
                private var pic_side: String? = null
                private var ctrl_tmpl: Int = 0
                private var batt_support: Int = 0
                private var vin: String? = null
                private var plate_num: String? = null
                private var bind_days: Int = 0
                private var bike_class: Int = 0
                private var completion: Int = 0
                private var base: BaseBean? = null
                private var binded_time: String? = null
                private var activated_time: String? = null
                private var order: Int = 0
                private var total_miles: Int = 0
                private var func: FuncBean? = null
                private var security: SecurityBean? = null
                //app本地后加 为了蓝牙sdk
                var bleId:String? = null
                fun getBike_id(): Int {
                    return bike_id
                }

                fun setBike_id(bike_id: Int) {
                    this.bike_id = bike_id
                }

                fun getOwner_id(): Int {
                    return owner_id
                }

                fun setOwner_id(owner_id: Int) {
                    this.owner_id = owner_id
                }

                fun getCc_id(): Int {
                    return cc_id
                }

                fun setCc_id(cc_id: Int) {
                    this.cc_id = cc_id
                }

                fun getBike_name(): String? {
                    return bike_name
                }

                fun setBike_name(bike_name: String) {
                    this.bike_name = bike_name
                }

                fun getStatus(): Int {
                    return status
                }

                fun setStatus(status: Int) {
                    this.status = status
                }

                fun getBrand(): BrandBean? {
                    return brand
                }

                fun setBrand(brand: BrandBean) {
                    this.brand = brand
                }

                fun getModel(): ModelBean? {
                    return model
                }

                fun setModel(model: ModelBean) {
                    this.model = model
                }

                fun getColor(): String? {
                    return color
                }

                fun setColor(color: String) {
                    this.color = color
                }

                fun getPic_b(): String? {
                    return pic_b
                }

                fun setPic_b(pic_b: String) {
                    this.pic_b = pic_b
                }

                fun getPic_s(): String? {
                    return pic_s
                }

                fun setPic_s(pic_s: String) {
                    this.pic_s = pic_s
                }

                fun getPic_side(): String? {
                    return pic_side
                }

                fun setPic_side(pic_side: String) {
                    this.pic_side = pic_side
                }

                fun getCtrl_tmpl(): Int {
                    return ctrl_tmpl
                }

                fun setCtrl_tmpl(ctrl_tmpl: Int) {
                    this.ctrl_tmpl = ctrl_tmpl
                }

                fun getBatt_support(): Int {
                    return batt_support
                }

                fun setBatt_support(batt_support: Int) {
                    this.batt_support = batt_support
                }

                fun getVin(): String? {
                    return vin
                }

                fun setVin(vin: String) {
                    this.vin = vin
                }

                fun getPlate_num(): String? {
                    return plate_num
                }

                fun setPlate_num(plate_num: String) {
                    this.plate_num = plate_num
                }

                fun getBind_days(): Int {
                    return bind_days
                }

                fun setBind_days(bind_days: Int) {
                    this.bind_days = bind_days
                }

                fun getBike_class(): Int {
                    return bike_class
                }

                fun setBike_class(bike_class: Int) {
                    this.bike_class = bike_class
                }

                fun getCompletion(): Int {
                    return completion
                }

                fun setCompletion(completion: Int) {
                    this.completion = completion
                }

                fun getBase(): BaseBean? {
                    return base
                }

                fun setBase(base: BaseBean) {
                    this.base = base
                }

                fun getBinded_time(): String? {
                    return binded_time
                }

                fun setBinded_time(binded_time: String) {
                    this.binded_time = binded_time
                }

                fun getActivated_time(): String? {
                    return activated_time
                }

                fun setActivated_time(activated_time: String) {
                    this.activated_time = activated_time
                }

                fun getOrder(): Int {
                    return order
                }

                fun setOrder(order: Int) {
                    this.order = order
                }

                fun getTotal_miles(): Int {
                    return total_miles
                }

                fun setTotal_miles(total_miles: Int) {
                    this.total_miles = total_miles
                }

                fun getFunc(): FuncBean? {
                    return func
                }

                fun setFunc(func: FuncBean) {
                    this.func = func
                }

                fun getSecurity(): SecurityBean? {
                    return security
                }

                fun setSecurity(security: SecurityBean) {
                    this.security = security
                }

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
                     * model_id : 2
                     * model_name : Robor lite
                     * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/2-big.png
                     * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/2-small.png
                     * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/2-side.png
                     * ctrl_tmpl : 2
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
                     * mac : 00271500046E
                     * imei : 862587030282027
                     * sn : G510GDA18030100054
                     * imsi : 460041226506277
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
                     */

                    var e_sidestand: Int = 0
                    var saddle_sensor: Int = 0
                }

                class SecurityBean {
                    /**
                     * mode : 0
                     * custom : {}
                     */

                    var mode: Int = 0
                    var custom: CustomBean? = null

                    class CustomBean
                }
            }
        }
    }
}