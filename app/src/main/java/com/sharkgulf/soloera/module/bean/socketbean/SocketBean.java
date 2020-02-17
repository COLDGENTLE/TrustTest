package com.sharkgulf.soloera.module.bean.socketbean;

/**
 * Created by user on 2019/8/7
 */
public class SocketBean<T> {

    /**
     * path : /push/bikeaccon
     * header : {"to":"85","uuid":"00862049-8490-4ecb-ab27-a1c73f229e4d","ts":1565161729,"ack":0}
     * body : {"acc":1,"name":"开电门"}
     */

    private String path;
    private HeaderBean header  ;
    private T body ;

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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public static class HeaderBean {
        /**
         * to : 85
         * uuid : 00862049-8490-4ecb-ab27-a1c73f229e4d
         * ts : 1565161729
         * ack : 0
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


}
