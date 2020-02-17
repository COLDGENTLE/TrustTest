package com.sharkgulf.soloera.module.bean.socketbean;

import java.util.List;

/**
 * Created by user on 2019/8/9
 */
public class BattInfoBean {
    /**
     * path : /push/bike/battinfo
     * header : {"to":"210","uuid":"da597a76-c0b6-4d01-be76-ee7763034d3f","ts":1573726616,"ack":1}
     * body : {"batt":[{"info":{"cycle":0,"loss":0,"position":1,"prod_date":"2019-10-15 18:16:56","rated_c":30,"rated_v":48,"sn":"BAT_A1234567890","type":0,"version":"BMS1.1.0"},"status":{"capacity":100,"charge":1,"charge_es":20,"faults":[45],"in_use":1,"mile_es":0,"temp":100,"temp_ind":0,"vol":48000}},{"info":{"cycle":0,"loss":0,"position":2,"prod_date":"2019-10-15 18:16:56","rated_c":30,"rated_v":48,"sn":"BAT_A1234567890","type":0,"version":"BMS1.1.1"},"status":{"capacity":100,"charge":0,"charge_es":20,"faults":[147],"in_use":1,"mile_es":0,"temp":100,"temp_ind":0,"vol":48000}}],"bike_id":33,"emer_batt":{"left_days":4,"time_desc":"刚刚","vol":400}}
     */

    private String path;
    private HeaderBean header;
    private BodyBean body;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeaderBean {
        /**
         * to : 210
         * uuid : da597a76-c0b6-4d01-be76-ee7763034d3f
         * ts : 1573726616
         * ack : 1
         */

        private String to;
        private String uuid;
        private int ts;
        private int ack;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getTs() {
            return ts;
        }

        public void setTs(int ts) {
            this.ts = ts;
        }

        public int getAck() {
            return ack;
        }

        public void setAck(int ack) {
            this.ack = ack;
        }
    }

    public static class BodyBean {
        /**
         * batt : [{"info":{"cycle":0,"loss":0,"position":1,"prod_date":"2019-10-15 18:16:56","rated_c":30,"rated_v":48,"sn":"BAT_A1234567890","type":0,"version":"BMS1.1.0"},"status":{"capacity":100,"charge":1,"charge_es":20,"faults":[45],"in_use":1,"mile_es":0,"temp":100,"temp_ind":0,"vol":48000}},{"info":{"cycle":0,"loss":0,"position":2,"prod_date":"2019-10-15 18:16:56","rated_c":30,"rated_v":48,"sn":"BAT_A1234567890","type":0,"version":"BMS1.1.1"},"status":{"capacity":100,"charge":0,"charge_es":20,"faults":[147],"in_use":1,"mile_es":0,"temp":100,"temp_ind":0,"vol":48000}}]
         * bike_id : 33
         * emer_batt : {"left_days":4,"time_desc":"刚刚","vol":400}
         */

        private int bike_id;
        private EmerBattBean emer_batt;
        private List<BattBean> batt;

        public int getBike_id() {
            return bike_id;
        }

        public void setBike_id(int bike_id) {
            this.bike_id = bike_id;
        }

        public EmerBattBean getEmer_batt() {
            return emer_batt;
        }

        public void setEmer_batt(EmerBattBean emer_batt) {
            this.emer_batt = emer_batt;
        }

        public List<BattBean> getBatt() {
            return batt;
        }

        public void setBatt(List<BattBean> batt) {
            this.batt = batt;
        }

        public static class EmerBattBean {
            /**
             * left_days : 4
             * time_desc : 刚刚
             * vol : 400
             */

            private int left_days;
            private String time_desc;
            private int vol;

            public int getLeft_days() {
                return left_days;
            }

            public void setLeft_days(int left_days) {
                this.left_days = left_days;
            }

            public String getTime_desc() {
                return time_desc;
            }

            public void setTime_desc(String time_desc) {
                this.time_desc = time_desc;
            }

            public int getVol() {
                return vol;
            }

            public void setVol(int vol) {
                this.vol = vol;
            }
        }

        public static class BattBean {
            /**
             * info : {"cycle":0,"loss":0,"position":1,"prod_date":"2019-10-15 18:16:56","rated_c":30,"rated_v":48,"sn":"BAT_A1234567890","type":0,"version":"BMS1.1.0"}
             * status : {"capacity":100,"charge":1,"charge_es":20,"faults":[45],"in_use":1,"mile_es":0,"temp":100,"temp_ind":0,"vol":48000}
             */

            private InfoBean info;
            private StatusBean status;

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public StatusBean getStatus() {
                return status;
            }

            public void setStatus(StatusBean status) {
                this.status = status;
            }

            public static class InfoBean {
                /**
                 * cycle : 0
                 * loss : 0
                 * position : 1
                 * prod_date : 2019-10-15 18:16:56
                 * rated_c : 30
                 * rated_v : 48
                 * sn : BAT_A1234567890
                 * type : 0
                 * version : BMS1.1.0
                 */

                private int cycle;
                private int loss;
                private int position;
                private String prod_date;
                private int rated_c;
                private int rated_v;
                private String sn;
                private int type;
                private String version;

                public int getCycle() {
                    return cycle;
                }

                public void setCycle(int cycle) {
                    this.cycle = cycle;
                }

                public int getLoss() {
                    return loss;
                }

                public void setLoss(int loss) {
                    this.loss = loss;
                }

                public int getPosition() {
                    return position;
                }

                public void setPosition(int position) {
                    this.position = position;
                }

                public String getProd_date() {
                    return prod_date;
                }

                public void setProd_date(String prod_date) {
                    this.prod_date = prod_date;
                }

                public int getRated_c() {
                    return rated_c;
                }

                public void setRated_c(int rated_c) {
                    this.rated_c = rated_c;
                }

                public int getRated_v() {
                    return rated_v;
                }

                public void setRated_v(int rated_v) {
                    this.rated_v = rated_v;
                }

                public String getSn() {
                    return sn;
                }

                public void setSn(String sn) {
                    this.sn = sn;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }
            }

            public static class StatusBean {
                /**
                 * capacity : 100
                 * charge : 1
                 * charge_es : 20
                 * faults : [45]
                 * in_use : 1
                 * mile_es : 0
                 * temp : 100
                 * temp_ind : 0
                 * vol : 48000
                 */

                private int capacity;
                private int charge;
                private int charge_es;
                private int in_use;
                private int mile_es;
                private int temp;
                private int temp_ind;
                private int vol;
                private List<Integer> faults;

                public int getCapacity() {
                    return capacity;
                }

                public void setCapacity(int capacity) {
                    this.capacity = capacity;
                }

                public int getCharge() {
                    return charge;
                }

                public void setCharge(int charge) {
                    this.charge = charge;
                }

                public int getCharge_es() {
                    return charge_es;
                }

                public void setCharge_es(int charge_es) {
                    this.charge_es = charge_es;
                }

                public int getIn_use() {
                    return in_use;
                }

                public void setIn_use(int in_use) {
                    this.in_use = in_use;
                }

                public int getMile_es() {
                    return mile_es;
                }

                public void setMile_es(int mile_es) {
                    this.mile_es = mile_es;
                }

                public int getTemp() {
                    return temp;
                }

                public void setTemp(int temp) {
                    this.temp = temp;
                }

                public int getTemp_ind() {
                    return temp_ind;
                }

                public void setTemp_ind(int temp_ind) {
                    this.temp_ind = temp_ind;
                }

                public int getVol() {
                    return vol;
                }

                public void setVol(int vol) {
                    this.vol = vol;
                }

                public List<Integer> getFaults() {
                    return faults;
                }

                public void setFaults(List<Integer> faults) {
                    this.faults = faults;
                }
            }
        }
    }
}
