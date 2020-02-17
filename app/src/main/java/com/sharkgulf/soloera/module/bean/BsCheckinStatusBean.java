package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/5/28
 */
public class BsCheckinStatusBean {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"today":0,"stat":{"cons_days":1}}
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
        /**
         * today : 0
         * stat : {"cons_days":1}
         */

        private int today;
        private StatBean stat;

        public int getToday() {
            return today;
        }

        public void setToday(int today) {
            this.today = today;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public static class StatBean {
            /**
             * cons_days : 1
             */

            private int cons_days;

            public int getCons_days() {
                return cons_days;
            }

            public void setCons_days(int cons_days) {
                this.cons_days = cons_days;
            }
        }
    }
}
