package com.sharkgulf.soloera.module.bean.socketbean;

/**
 * Created by user on 2019/8/7
 */
public class CarLoctionBean {


    /**
     * path : /push/bike/bikeposition
     * header : {"to":"210","uuid":"3fea13dd-34e6-4f2c-a133-a2fb7f8026e4","ts":1574750110,"ack":1,"event":0}
     * body : {"bike_id":8,"bs":{"city":"上海市","desc":"上海市 闵行区 宜山路 靠近上海银行(漕河泾开发区支行)","details":"上海银行(漕河泾开发区支行)","district":"宜山路","lat":31.1674344,"level":3,"lng":121.3881565,"province":"上海市","radius":0,"ts":1574732265,"update_desc":{"diff":297,"style":"#969696","text":"5小时前"}},"gps":{"city":"","desc":"上海市闵行区虹桥镇莲花路1725号兴迪商务大厦","details":"虹桥镇","district":"闵行区","lat":31.17089,"level":0,"lng":121.385895,"province":"上海市","ts":1574732265,"update_desc":{"diff":297,"style":"#969696","text":"5小时前"}},"ol_status":1}
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
         * uuid : 3fea13dd-34e6-4f2c-a133-a2fb7f8026e4
         * ts : 1574750110
         * ack : 1
         * event : 0
         */

        private String to;
        private String uuid;
        private int ts;
        private int ack;
        private int event;

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

        public int getEvent() {
            return event;
        }

        public void setEvent(int event) {
            this.event = event;
        }
    }

    public static class BodyBean {
        /**
         * bike_id : 8
         * bs : {"city":"上海市","desc":"上海市 闵行区 宜山路 靠近上海银行(漕河泾开发区支行)","details":"上海银行(漕河泾开发区支行)","district":"宜山路","lat":31.1674344,"level":3,"lng":121.3881565,"province":"上海市","radius":0,"ts":1574732265,"update_desc":{"diff":297,"style":"#969696","text":"5小时前"}}
         * gps : {"city":"","desc":"上海市闵行区虹桥镇莲花路1725号兴迪商务大厦","details":"虹桥镇","district":"闵行区","lat":31.17089,"level":0,"lng":121.385895,"province":"上海市","ts":1574732265,"update_desc":{"diff":297,"style":"#969696","text":"5小时前"}}
         * ol_status : 1
         */

        private int bike_id;
        private BsBean bs;
        private GpsBean gps;
        private int ol_status;

        public int getBike_id() {
            return bike_id;
        }

        public void setBike_id(int bike_id) {
            this.bike_id = bike_id;
        }

        public BsBean getBs() {
            return bs;
        }

        public void setBs(BsBean bs) {
            this.bs = bs;
        }

        public GpsBean getGps() {
            return gps;
        }

        public void setGps(GpsBean gps) {
            this.gps = gps;
        }

        public int getOl_status() {
            return ol_status;
        }

        public void setOl_status(int ol_status) {
            this.ol_status = ol_status;
        }

        public static class BsBean {
            /**
             * city : 上海市
             * desc : 上海市 闵行区 宜山路 靠近上海银行(漕河泾开发区支行)
             * details : 上海银行(漕河泾开发区支行)
             * district : 宜山路
             * lat : 31.1674344
             * level : 3
             * lng : 121.3881565
             * province : 上海市
             * radius : 0
             * ts : 1574732265
             * update_desc : {"diff":297,"style":"#969696","text":"5小时前"}
             */

            private String city;
            private String desc;
            private String details;
            private String district;
            private double lat;
            private int level;
            private double lng;
            private String province;
            private int radius;
            private int ts;
            private UpdateDescBean update_desc;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public int getRadius() {
                return radius;
            }

            public void setRadius(int radius) {
                this.radius = radius;
            }

            public int getTs() {
                return ts;
            }

            public void setTs(int ts) {
                this.ts = ts;
            }

            public UpdateDescBean getUpdate_desc() {
                return update_desc;
            }

            public void setUpdate_desc(UpdateDescBean update_desc) {
                this.update_desc = update_desc;
            }

            public static class UpdateDescBean {
                /**
                 * diff : 297
                 * style : #969696
                 * text : 5小时前
                 */

                private int diff;
                private String style;
                private String text;

                public int getDiff() {
                    return diff;
                }

                public void setDiff(int diff) {
                    this.diff = diff;
                }

                public String getStyle() {
                    return style;
                }

                public void setStyle(String style) {
                    this.style = style;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }
        }

        public static class GpsBean {
            /**
             * city :
             * desc : 上海市闵行区虹桥镇莲花路1725号兴迪商务大厦
             * details : 虹桥镇
             * district : 闵行区
             * lat : 31.17089
             * level : 0
             * lng : 121.385895
             * province : 上海市
             * ts : 1574732265
             * update_desc : {"diff":297,"style":"#969696","text":"5小时前"}
             */

            private String city;
            private String desc;
            private String details;
            private String district;
            private double lat;
            private int level;
            private double lng;
            private String province;
            private int ts;
            private UpdateDescBeanX update_desc;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public int getTs() {
                return ts;
            }

            public void setTs(int ts) {
                this.ts = ts;
            }

            public UpdateDescBeanX getUpdate_desc() {
                return update_desc;
            }

            public void setUpdate_desc(UpdateDescBeanX update_desc) {
                this.update_desc = update_desc;
            }

            public static class UpdateDescBeanX {
                /**
                 * diff : 297
                 * style : #969696
                 * text : 5小时前
                 */

                private int diff;
                private String style;
                private String text;

                public int getDiff() {
                    return diff;
                }

                public void setDiff(int diff) {
                    this.diff = diff;
                }

                public String getStyle() {
                    return style;
                }

                public void setStyle(String style) {
                    this.style = style;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }
        }
    }
}
