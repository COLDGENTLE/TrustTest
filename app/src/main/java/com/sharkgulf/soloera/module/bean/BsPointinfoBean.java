package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/5/28
 */
public class BsPointinfoBean {

    /**
     * state : 00
     * state_info : 成功
     * seq : 0
     * data : {"info":{"total":242}}
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
         * info : {"total":242}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * total : 242
             */

            private int total;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }
}
