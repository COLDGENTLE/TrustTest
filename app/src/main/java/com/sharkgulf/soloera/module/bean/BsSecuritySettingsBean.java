package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/8/19
 */
public class BsSecuritySettingsBean {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     */

    private String state;
    private String state_info;
    private int seq;

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
}
