package com.sharkgulf.soloera.module.bean;

/**
 * Created by user on 2019/6/28
 */
public class BsOrderInfoBean {

    /**
     * state : 00
     * state_info : 成功
     * data : {"pingpp":"{\"id\":\"ch_H4C4WLCKWrT0qTWXfH4evDmT\",\"object\":\"charge\",\"created\":1561713044,\"livemode\":false,\"paid\":false,\"refunded\":false,\"reversed\":false,\"app\":\"app_irrz544W5e9SKyfP\",\"channel\":\"wx\",\"order_no\":\"108\",\"client_ip\":\"192.168.1.1\",\"amount\":1049900,\"amount_settle\":1049900,\"currency\":\"cny\",\"subject\":\"blueshark goods\",\"body\":\"body\",\"extra\":{},\"time_paid\":0,\"time_expire\":1561720244,\"time_settle\":0,\"transaction_no\":\"\",\"refunds\":{\"object\":\"list\",\"has_more\":false,\"url\":\"/v1/charges/ch_H4C4WLCKWrT0qTWXfH4evDmT/refunds\",\"data\":[]},\"amount_refunded\":0,\"failure_code\":\"\",\"failure_msg\":\"\",\"metadata\":{\"color\":\"red\"},\"credential\":{\"object\":\"credential\",\"wx\":{\"appId\":\"wxderjjhlgu108syjx\",\"nonceStr\":\"eac69dd2323eccc5c6a9c5fc8c063f71\",\"packageValue\":\"Sign=WXPay\",\"partnerId\":\"1911064850\",\"prepayId\":\"1101000000190628xrlc8gv1wn98evd8\",\"sign\":\"1DF8673E4D54E1CA8066FD4A638164A8\",\"timeStamp\":1561713044}},\"description\":\"\"}"}
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
         * pingpp : {"id":"ch_H4C4WLCKWrT0qTWXfH4evDmT","object":"charge","created":1561713044,"livemode":false,"paid":false,"refunded":false,"reversed":false,"app":"app_irrz544W5e9SKyfP","channel":"wx","order_no":"108","client_ip":"192.168.1.1","amount":1049900,"amount_settle":1049900,"currency":"cny","subject":"blueshark goods","body":"body","extra":{},"time_paid":0,"time_expire":1561720244,"time_settle":0,"transaction_no":"","refunds":{"object":"list","has_more":false,"url":"/v1/charges/ch_H4C4WLCKWrT0qTWXfH4evDmT/refunds","data":[]},"amount_refunded":0,"failure_code":"","failure_msg":"","metadata":{"color":"red"},"credential":{"object":"credential","wx":{"appId":"wxderjjhlgu108syjx","nonceStr":"eac69dd2323eccc5c6a9c5fc8c063f71","packageValue":"Sign=WXPay","partnerId":"1911064850","prepayId":"1101000000190628xrlc8gv1wn98evd8","sign":"1DF8673E4D54E1CA8066FD4A638164A8","timeStamp":1561713044}},"description":""}
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
