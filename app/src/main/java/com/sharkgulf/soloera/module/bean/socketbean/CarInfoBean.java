package com.sharkgulf.soloera.module.bean.socketbean;

/**
 * Created by user on 2019/8/15
 */
public class CarInfoBean {

        /**
         * activated_time : 0001-01-01 00:00:00
         * base : {"imei":"","imsi":"","mac":"","sn":""}
         * batt_support : 0
         * bike_class : 0
         * bike_id : 5
         * bike_name : 蓝鲨正式车0010
         * bind_days : 1
         * binded_time : 0001-01-01 00:00:00
         * brand : {"brand_id":1,"brand_name":"蓝鲨","logo":""}
         * cc_id : 10004
         * color : 绿
         * completion : 0
         * ctrl_tmpl : 3
         * func : {"e_sidestand":0,"saddle_sensor":0}
         * model : {"ctrl_tmpl":3,"model_id":1,"model_name":"正式车","pic_b":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png","pic_s":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png","pic_side":"https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png"}
         * order : 0
         * owner_id : 4
         * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png
         * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png
         * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png
         * plate_num :
         * security : {"custom":null,"mode":0}
         * status : 1
         * total_miles : 87
         * vin :
         */

        private String activated_time;
        private BaseBean base;
        private int batt_support;
        private int bike_class;
        private int bike_id;
        private String bike_name;
        private int bind_days;
        private String binded_time;
        private BrandBean brand;
        private int cc_id;
        private String color;
        private int completion;
        private int ctrl_tmpl;
        private FuncBean func;
        private ModelBean model;
        private int order;
        private int owner_id;
        private String pic_b;
        private String pic_s;
        private String pic_side;
        private String plate_num;
        private SecurityBean security;
        private int status;
        private int total_miles;
        private String vin;
        private int max_ride_miles;
        private int max_ride_track_id;

    public int getMax_ride_miles() {
        return max_ride_miles;
    }

    public void setMax_ride_miles(int max_ride_miles) {
        this.max_ride_miles = max_ride_miles;
    }

    public int getMax_ride_track_id() {
        return max_ride_track_id;
    }

    public void setMax_ride_track_id(int max_ride_track_id) {
        this.max_ride_track_id = max_ride_track_id;
    }

    public String getActivated_time() {
            return activated_time;
        }

        public void setActivated_time(String activated_time) {
            this.activated_time = activated_time;
        }

        public BaseBean getBase() {
            return base;
        }

        public void setBase(BaseBean base) {
            this.base = base;
        }

        public int getBatt_support() {
            return batt_support;
        }

        public void setBatt_support(int batt_support) {
            this.batt_support = batt_support;
        }

        public int getBike_class() {
            return bike_class;
        }

        public void setBike_class(int bike_class) {
            this.bike_class = bike_class;
        }

        public int getBike_id() {
            return bike_id;
        }

        public void setBike_id(int bike_id) {
            this.bike_id = bike_id;
        }

        public String getBike_name() {
            return bike_name;
        }

        public void setBike_name(String bike_name) {
            this.bike_name = bike_name;
        }

        public int getBind_days() {
            return bind_days;
        }

        public void setBind_days(int bind_days) {
            this.bind_days = bind_days;
        }

        public String getBinded_time() {
            return binded_time;
        }

        public void setBinded_time(String binded_time) {
            this.binded_time = binded_time;
        }

        public BrandBean getBrand() {
            return brand;
        }

        public void setBrand(BrandBean brand) {
            this.brand = brand;
        }

        public int getCc_id() {
            return cc_id;
        }

        public void setCc_id(int cc_id) {
            this.cc_id = cc_id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getCompletion() {
            return completion;
        }

        public void setCompletion(int completion) {
            this.completion = completion;
        }

        public int getCtrl_tmpl() {
            return ctrl_tmpl;
        }

        public void setCtrl_tmpl(int ctrl_tmpl) {
            this.ctrl_tmpl = ctrl_tmpl;
        }

        public FuncBean getFunc() {
            return func;
        }

        public void setFunc(FuncBean func) {
            this.func = func;
        }

        public ModelBean getModel() {
            return model;
        }

        public void setModel(ModelBean model) {
            this.model = model;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(int owner_id) {
            this.owner_id = owner_id;
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

        public String getPlate_num() {
            return plate_num;
        }

        public void setPlate_num(String plate_num) {
            this.plate_num = plate_num;
        }

        public SecurityBean getSecurity() {
            return security;
        }

        public void setSecurity(SecurityBean security) {
            this.security = security;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTotal_miles() {
            return total_miles;
        }

        public void setTotal_miles(int total_miles) {
            this.total_miles = total_miles;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public static class BaseBean {
            /**
             * imei :
             * imsi :
             * mac :
             * sn :
             */

            private String imei;
            private String imsi;
            private String mac;
            private String sn;

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getImsi() {
                return imsi;
            }

            public void setImsi(String imsi) {
                this.imsi = imsi;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }
        }

        public static class BrandBean {
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

        public static class FuncBean {
            /**
             * e_sidestand : 0
             * saddle_sensor : 0
             */

            private int e_sidestand;
            private int saddle_sensor;

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
        }

        public static class ModelBean {
            /**
             * ctrl_tmpl : 3
             * model_id : 1
             * model_name : 正式车
             * pic_b : https://api.d.bluesharkmotor.com/file/bike/info/model/1-big.png
             * pic_s : https://api.d.bluesharkmotor.com/file/bike/info/model/1-small@3x.png
             * pic_side : https://api.d.bluesharkmotor.com/file/bike/info/model/1-side.png
             */

            private int ctrl_tmpl;
            private int model_id;
            private String model_name;
            private String pic_b;
            private String pic_s;
            private String pic_side;

            public int getCtrl_tmpl() {
                return ctrl_tmpl;
            }

            public void setCtrl_tmpl(int ctrl_tmpl) {
                this.ctrl_tmpl = ctrl_tmpl;
            }

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
        }

        public static class SecurityBean {

            /**
             * mode : 1
             * custom : {"move":true,"batt_out":true,"vibr_severe":true,"batt_in":false,"vibr_moderate":false,"vibr_mild":false,"power_low":false}
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

            public static class CustomBean {
                /**
                 * move : true
                 * batt_out : true
                 * vibr_severe : true
                 * batt_in : false
                 * vibr_moderate : false
                 * vibr_mild : false
                 * power_low : false
                 */

                private boolean move;
                private boolean batt_out;
                private boolean vibr_severe;
                private boolean batt_in;
                private boolean vibr_moderate;
                private boolean vibr_mild;
                private boolean power_low;

                public boolean isMove() {
                    return move;
                }

                public void setMove(boolean move) {
                    this.move = move;
                }

                public boolean isBatt_out() {
                    return batt_out;
                }

                public void setBatt_out(boolean batt_out) {
                    this.batt_out = batt_out;
                }

                public boolean isVibr_severe() {
                    return vibr_severe;
                }

                public void setVibr_severe(boolean vibr_severe) {
                    this.vibr_severe = vibr_severe;
                }

                public boolean isBatt_in() {
                    return batt_in;
                }

                public void setBatt_in(boolean batt_in) {
                    this.batt_in = batt_in;
                }

                public boolean isVibr_moderate() {
                    return vibr_moderate;
                }

                public void setVibr_moderate(boolean vibr_moderate) {
                    this.vibr_moderate = vibr_moderate;
                }

                public boolean isVibr_mild() {
                    return vibr_mild;
                }

                public void setVibr_mild(boolean vibr_mild) {
                    this.vibr_mild = vibr_mild;
                }

                public boolean isPower_low() {
                    return power_low;
                }

                public void setPower_low(boolean power_low) {
                    this.power_low = power_low;
                }
            }
        }
}
