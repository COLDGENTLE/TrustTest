package com.sharkgulf.soloera.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2019/7/9
 */
public class BsGetCarInfoBean implements Serializable {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"bikes":[{"bike_id":5,"owner_id":0,"cc_id":0,"bike_name":"蓝鲨正式车0010","status":1,"brand":{"brand_id":1,"brand_name":"蓝鲨","logo":""},"model":{"model_id":1,"model_name":"正式车","pic_b":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png","pic_s":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png","pic_side":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png","ctrl_tmpl":3},"color":"绿","pic_b":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png","pic_s":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png","pic_side":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png","ctrl_tmpl":3,"batt_support":2,"vin":"123213213","plate_num":"","bind_days":1,"bike_class":0,"completion":0,"base":{"mac":"002715000010","imei":"866274030001220","sn":"G510AAA17070300122","imsi":"460041210304549"},"binded_time":"2019-08-28 09:42:51","activated_time":"2019-08-28 09:44:39","order":691,"total_miles":87,"func":{"e_sidestand":0,"saddle_sensor":0,"ctrl_tmpl":0},"security":{"mode":2,"custom":{"batt_out":true,"move":true,"vibr_severe":true}}}]}
     */

    private String state;
    private String state_info;
    private int seq;
    private DataBean data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_info() {
        return state_info;
    }

    public void setState_info(String state_info) {
        this.state_info = state_info;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private List<BikesBean> bikes;

        public List<BikesBean> getBikes() {
            return bikes;
        }

        public void setBikes(List<BikesBean> bikes) {
            this.bikes = bikes;
        }

        public static class BikesBean implements Serializable{
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

            private int bike_id;
            private int owner_id;
            private int cc_id;
            private String bike_name;
            private int status;
            private BrandBean brand;
            private ModelBean model;
            private String color;
            private String pic_b;
            private String pic_s;
            private String pic_side;
            private int ctrl_tmpl;
            private int batt_support;
            private String vin;
            private String plate_num;
            private int bind_days;
            private int bike_class;
            private int completion;
            private BaseBean base;
            private String binded_time;
            private String activated_time;
            private int order;
            private int total_miles;
            private FuncBean func;
            private SecurityBean security;

            public int getBike_id() {
                return bike_id;
            }

            public void setBike_id(int bike_id) {
                this.bike_id = bike_id;
            }

            public int getOwner_id() {
                return owner_id;
            }

            public void setOwner_id(int owner_id) {
                this.owner_id = owner_id;
            }

            public int getCc_id() {
                return cc_id;
            }

            public void setCc_id(int cc_id) {
                this.cc_id = cc_id;
            }

            public String getBike_name() {
                return bike_name;
            }

            public void setBike_name(String bike_name) {
                this.bike_name = bike_name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public BrandBean getBrand() {
                return brand;
            }

            public void setBrand(BrandBean brand) {
                this.brand = brand;
            }

            public ModelBean getModel() {
                return model;
            }

            public void setModel(ModelBean model) {
                this.model = model;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getPic_b() {
                return pic_b;
            }

            public void setPic_b(String pic_b) {
                this.pic_b = pic_b;
            }

            public String getPic_s() {
                return pic_s;
            }

            public void setPic_s(String pic_s) {
                this.pic_s = pic_s;
            }

            public String getPic_side() {
                return pic_side;
            }

            public void setPic_side(String pic_side) {
                this.pic_side = pic_side;
            }

            public int getCtrl_tmpl() {
                return ctrl_tmpl;
            }

            public void setCtrl_tmpl(int ctrl_tmpl) {
                this.ctrl_tmpl = ctrl_tmpl;
            }

            public int getBatt_support() {
                return batt_support;
            }

            public void setBatt_support(int batt_support) {
                this.batt_support = batt_support;
            }

            public String getVin() {
                return vin;
            }

            public void setVin(String vin) {
                this.vin = vin;
            }

            public String getPlate_num() {
                return plate_num;
            }

            public void setPlate_num(String plate_num) {
                this.plate_num = plate_num;
            }

            public int getBind_days() {
                return bind_days;
            }

            public void setBind_days(int bind_days) {
                this.bind_days = bind_days;
            }

            public int getBike_class() {
                return bike_class;
            }

            public void setBike_class(int bike_class) {
                this.bike_class = bike_class;
            }

            public int getCompletion() {
                return completion;
            }

            public void setCompletion(int completion) {
                this.completion = completion;
            }

            public BaseBean getBase() {
                return base;
            }

            public void setBase(BaseBean base) {
                this.base = base;
            }

            public String getBinded_time() {
                return binded_time;
            }

            public void setBinded_time(String binded_time) {
                this.binded_time = binded_time;
            }

            public String getActivated_time() {
                return activated_time;
            }

            public void setActivated_time(String activated_time) {
                this.activated_time = activated_time;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getTotal_miles() {
                return total_miles;
            }

            public void setTotal_miles(int total_miles) {
                this.total_miles = total_miles;
            }

            public FuncBean getFunc() {
                return func;
            }

            public void setFunc(FuncBean func) {
                this.func = func;
            }

            public SecurityBean getSecurity() {
                return security;
            }

            public void setSecurity(SecurityBean security) {
                this.security = security;
            }

            public static class BrandBean implements Serializable{
                /**
                 * brand_id : 1
                 * brand_name : 蓝鲨
                 * logo :
                 */

                private int brand_id;
                private String brand_name;
                private String logo;

                public int getBrand_id() {
                    return brand_id;
                }

                public void setBrand_id(int brand_id) {
                    this.brand_id = brand_id;
                }

                public String getBrand_name() {
                    return brand_name;
                }

                public void setBrand_name(String brand_name) {
                    this.brand_name = brand_name;
                }

                public String getLogo() {
                    return logo;
                }

                public void setLogo(String logo) {
                    this.logo = logo;
                }
            }

            public static class ModelBean implements Serializable{
                /**
                 * model_id : 1
                 * model_name : 正式车
                 * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png
                 * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png
                 * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png
                 * ctrl_tmpl : 3
                 */

                private int model_id;
                private String model_name;
                private String pic_b;
                private String pic_s;
                private String pic_side;
                private int ctrl_tmpl;

                public int getModel_id() {
                    return model_id;
                }

                public void setModel_id(int model_id) {
                    this.model_id = model_id;
                }

                public String getModel_name() {
                    return model_name;
                }

                public void setModel_name(String model_name) {
                    this.model_name = model_name;
                }

                public String getPic_b() {
                    return pic_b;
                }

                public void setPic_b(String pic_b) {
                    this.pic_b = pic_b;
                }

                public String getPic_s() {
                    return pic_s;
                }

                public void setPic_s(String pic_s) {
                    this.pic_s = pic_s;
                }

                public String getPic_side() {
                    return pic_side;
                }

                public void setPic_side(String pic_side) {
                    this.pic_side = pic_side;
                }

                public int getCtrl_tmpl() {
                    return ctrl_tmpl;
                }

                public void setCtrl_tmpl(int ctrl_tmpl) {
                    this.ctrl_tmpl = ctrl_tmpl;
                }
            }

            public static class BaseBean implements Serializable{
                /**
                 * mac : 002715000010
                 * imei : 866274030001220
                 * sn : G510AAA17070300122
                 * imsi : 460041210304549
                 */

                private String mac;
                private String imei;
                private String sn;
                private String imsi;

                public String getMac() {
                    return mac;
                }

                public void setMac(String mac) {
                    this.mac = mac;
                }

                public String getImei() {
                    return imei;
                }

                public void setImei(String imei) {
                    this.imei = imei;
                }

                public String getSn() {
                    return sn;
                }

                public void setSn(String sn) {
                    this.sn = sn;
                }

                public String getImsi() {
                    return imsi;
                }

                public void setImsi(String imsi) {
                    this.imsi = imsi;
                }
            }

            public static class FuncBean implements Serializable{
                /**
                 * e_sidestand : 0
                 * saddle_sensor : 0
                 * ctrl_tmpl : 0
                 */

                private int e_sidestand;
                private int saddle_sensor;
                private int ctrl_tmpl;

                public int getE_sidestand() {
                    return e_sidestand;
                }

                public void setE_sidestand(int e_sidestand) {
                    this.e_sidestand = e_sidestand;
                }

                public int getSaddle_sensor() {
                    return saddle_sensor;
                }

                public void setSaddle_sensor(int saddle_sensor) {
                    this.saddle_sensor = saddle_sensor;
                }

                public int getCtrl_tmpl() {
                    return ctrl_tmpl;
                }

                public void setCtrl_tmpl(int ctrl_tmpl) {
                    this.ctrl_tmpl = ctrl_tmpl;
                }
            }

            public static class SecurityBean implements Serializable{
                /**
                 * mode : 2
                 * custom : {"batt_out":true,"move":true,"vibr_severe":true}
                 */

                private int mode;
                private CustomBean custom;

                public int getMode() {
                    return mode;
                }

                public void setMode(int mode) {
                    this.mode = mode;
                }

                public CustomBean getCustom() {
                    return custom;
                }

                public void setCustom(CustomBean custom) {
                    this.custom = custom;
                }

                public static class CustomBean implements Serializable{
                    /**
                     * batt_out : true
                     * move : true
                     * vibr_severe : true
                     */

                    private boolean batt_out;
                    private boolean move;
                    private boolean vibr_severe;

                    public boolean isBatt_out() {
                        return batt_out;
                    }

                    public void setBatt_out(boolean batt_out) {
                        this.batt_out = batt_out;
                    }

                    public boolean isMove() {
                        return move;
                    }

                    public void setMove(boolean move) {
                        this.move = move;
                    }

                    public boolean isVibr_severe() {
                        return vibr_severe;
                    }

                    public void setVibr_severe(boolean vibr_severe) {
                        this.vibr_severe = vibr_severe;
                    }
                }
            }
        }
    }
}
