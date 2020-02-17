package com.sharkgulf.soloera.module.bean;

import java.util.List;

/**
 * Created by user on 2019/9/9
 */
public class BsAlertBean {

    /**
     * state : 00
     * state_info : 成功
     * data : {"total":0,"list":[{"uuid":"83d5de5a-5352-43a9-86da-adb35bf0e60f","uid":0,"bid":33,"did":0,"name":"非法位移","status":0,"chan":"websocket","ts":1575253657,"event":1010,"type":0,"title":"您的爱车于10:27 在未打开电门时发生移动","count":1,"result":"","exts":"{\"ts\": 1574907110000000000, \"type\": 2, \"miles\": 2613, \"times\": 9804, \"end_pos\": \"上海市 闵行区 宜山路 靠近兴迪商务大厦\", \"end_time\": \"2019-11-28 12:55:14\", \"track_id\": 76598, \"avg_speed\": 959, \"begin_pos\": \"上海市 闵行区 友东路 靠近赢嘉大厦B座\", \"max_speed\": 0, \"min_speed\": 0, \"begin_time\": \"2019-11-28 10:11:50\", \"ts_display\": \"10:11\", \"carbon_save\": 522}"},{"uuid":"ed7b3d03-6cac-405f-82db-f6f6f4d5e31b","uid":0,"bid":33,"did":0,"name":"严重震动","status":0,"chan":"websocket","ts":1575253613,"event":1013,"type":0,"title":"您的爱车于10:26发生严重震动","count":1,"result":"","exts":"{}"},{"uuid":"88784c21-f279-4147-83a9-5f0e653f6e00","uid":0,"bid":33,"did":0,"name":"电池已插入电池仓1","status":0,"chan":"websocket","ts":1575253545,"event":1006,"type":0,"title":"您爱车的电池已插入电池仓1","count":1,"result":"","exts":"{}"},{"uuid":"1ab0e147-7b02-4836-9d67-74998be7329d","uid":0,"bid":33,"did":0,"name":"车辆电机霍尔线路异常","status":0,"chan":"websocket","ts":1575253529,"event":5,"type":0,"title":"您爱车的电机霍尔线路于10:25发生故障（故障码005）","count":1,"result":"https://app-web.d.blueshark.com/app/troubleshoot/5","exts":"{}"},{"uuid":"a75cee62-db87-4364-b736-8282af313c69","uid":0,"bid":33,"did":0,"name":"车辆转把故障","status":0,"chan":"websocket","ts":1575253316,"event":1,"type":0,"title":"您爱车的转把于10:21发生故障（故障码001）","count":1,"result":"https://app-web.d.blueshark.com/app/troubleshoot/1","exts":"{}"}]}
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
         * total : 0
         * list : [{"uuid":"83d5de5a-5352-43a9-86da-adb35bf0e60f","uid":0,"bid":33,"did":0,"name":"非法位移","status":0,"chan":"websocket","ts":1575253657,"event":1010,"type":0,"title":"您的爱车于10:27 在未打开电门时发生移动","count":1,"result":"","exts":"{\"ts\": 1574907110000000000, \"type\": 2, \"miles\": 2613, \"times\": 9804, \"end_pos\": \"上海市 闵行区 宜山路 靠近兴迪商务大厦\", \"end_time\": \"2019-11-28 12:55:14\", \"track_id\": 76598, \"avg_speed\": 959, \"begin_pos\": \"上海市 闵行区 友东路 靠近赢嘉大厦B座\", \"max_speed\": 0, \"min_speed\": 0, \"begin_time\": \"2019-11-28 10:11:50\", \"ts_display\": \"10:11\", \"carbon_save\": 522}"},{"uuid":"ed7b3d03-6cac-405f-82db-f6f6f4d5e31b","uid":0,"bid":33,"did":0,"name":"严重震动","status":0,"chan":"websocket","ts":1575253613,"event":1013,"type":0,"title":"您的爱车于10:26发生严重震动","count":1,"result":"","exts":"{}"},{"uuid":"88784c21-f279-4147-83a9-5f0e653f6e00","uid":0,"bid":33,"did":0,"name":"电池已插入电池仓1","status":0,"chan":"websocket","ts":1575253545,"event":1006,"type":0,"title":"您爱车的电池已插入电池仓1","count":1,"result":"","exts":"{}"},{"uuid":"1ab0e147-7b02-4836-9d67-74998be7329d","uid":0,"bid":33,"did":0,"name":"车辆电机霍尔线路异常","status":0,"chan":"websocket","ts":1575253529,"event":5,"type":0,"title":"您爱车的电机霍尔线路于10:25发生故障（故障码005）","count":1,"result":"https://app-web.d.blueshark.com/app/troubleshoot/5","exts":"{}"},{"uuid":"a75cee62-db87-4364-b736-8282af313c69","uid":0,"bid":33,"did":0,"name":"车辆转把故障","status":0,"chan":"websocket","ts":1575253316,"event":1,"type":0,"title":"您爱车的转把于10:21发生故障（故障码001）","count":1,"result":"https://app-web.d.blueshark.com/app/troubleshoot/1","exts":"{}"}]
         */

        private int total;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * uuid : 83d5de5a-5352-43a9-86da-adb35bf0e60f
             * uid : 0
             * bid : 33
             * did : 0
             * name : 非法位移
             * status : 0
             * chan : websocket
             * ts : 1575253657
             * event : 1010
             * type : 0
             * title : 您的爱车于10:27 在未打开电门时发生移动
             * count : 1
             * result :
             * exts : {"ts": 1574907110000000000, "type": 2, "miles": 2613, "times": 9804, "end_pos": "上海市 闵行区 宜山路 靠近兴迪商务大厦", "end_time": "2019-11-28 12:55:14", "track_id": 76598, "avg_speed": 959, "begin_pos": "上海市 闵行区 友东路 靠近赢嘉大厦B座", "max_speed": 0, "min_speed": 0, "begin_time": "2019-11-28 10:11:50", "ts_display": "10:11", "carbon_save": 522}
             */

            private String uuid;
            private int uid;
            private int bid;
            private int did;
            private String name;
            private int status;
            private String chan;
            private int ts;
            private int event;
            private int type;
            private String title;
            private int count;
            private String result;
            private String exts;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getBid() {
                return bid;
            }

            public void setBid(int bid) {
                this.bid = bid;
            }

            public int getDid() {
                return did;
            }

            public void setDid(int did) {
                this.did = did;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getChan() {
                return chan;
            }

            public void setChan(String chan) {
                this.chan = chan;
            }

            public int getTs() {
                return ts;
            }

            public void setTs(int ts) {
                this.ts = ts;
            }

            public int getEvent() {
                return event;
            }

            public void setEvent(int event) {
                this.event = event;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public String getExts() {
                return exts;
            }

            public void setExts(String exts) {
                this.exts = exts;
            }
        }
    }
}
