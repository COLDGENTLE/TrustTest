package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/5/28
 */
public class BsTicketBean {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"ticket":"ticket425b0acd94e90e2d671ff089edbcfcba","expires_in":120}
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
         * ticket : ticket425b0acd94e90e2d671ff089edbcfcba
         * expires_in : 120
         */

        private String ticket;
        private int expires_in;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
