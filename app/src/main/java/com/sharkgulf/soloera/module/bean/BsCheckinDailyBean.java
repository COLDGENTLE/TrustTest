package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/5/28
 */
public class BsCheckinDailyBean {

    /**
     * state : 1120
     * state_info : 今天已签到
     * seq : 0
     * data : null
     */

    private String state;
    private String state_info;
    private int seq;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
