package com.sharkgulf.soloera.module.bean;

import java.util.ArrayList;

/**
 * Created by user on 2019/5/28
 */
public class BsPointDetailBean {


    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"details":[{"ts":1563343645,"type":1,"channel":1000,"name":"每日签到","points":3},{"ts":1563272338,"type":1,"channel":1000,"name":"每日签到","points":3},{"ts":1562312981,"type":1,"channel":1000,"name":"每日签到","points":3},{"ts":1562221305,"type":1,"channel":1000,"name":"每日签到","points":3},{"ts":1558322169,"type":1,"channel":1000,"name":"每日签到","points":3},{"ts":1557476031,"type":1,"channel":2000,"name":"积分兑换","points":30},{"ts":1557476015,"type":1,"channel":2000,"name":"积分兑换","points":30},{"ts":1557475885,"type":1,"channel":2000,"name":"积分兑换","points":30},{"ts":1557475870,"type":1,"channel":2000,"name":"积分兑换","points":30},{"ts":1557475841,"type":1,"channel":1000,"name":"每日签到","points":30}]}
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

    public static class DataBean {
        private ArrayList<DetailsBean> details;

        public ArrayList<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(ArrayList<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * ts : 1563343645
             * type : 1
             * channel : 1000
             * name : 每日签到
             * points : 3
             */

            private long ts;
            private int type;
            private int channel;
            private String name;
            private int points;

            public long getTs() {
                return ts;
            }

            public void setTs(long ts) {
                this.ts = ts;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getChannel() {
                return channel;
            }

            public void setChannel(int channel) {
                this.channel = channel;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPoints() {
                return points;
            }

            public void setPoints(int points) {
                this.points = points;
            }
        }
    }
}
