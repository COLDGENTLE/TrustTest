package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/6/28
 */
public class BsOrderStatusBean {


    /**
     * state : 00
     * state_info : 成功
     * data : {"pingpp":""}
     */

    private String state;
    private String state_info;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pingpp :
         */

        private String pingpp;

        public String getPingpp() {
            return pingpp;
        }

        public void setPingpp(String pingpp) {
            this.pingpp = pingpp;
        }
    }
}
