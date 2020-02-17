package com.sharkgulf.soloera.module.bean.socketbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2019/9/9
 */
public class WebAlertBean implements Serializable {


    /**
     * path : /push/bikealert
     * header : {"to":"210","uuid":"3271f0f5-bf17-4834-be9e-f9ee5f2bc982","ts":1575253327,"ack":1,"event":0}
     * body : {"actions":[{"action":2,"result":"","text":"忽略"},{"action":1,"result":"路径详情","text":"查看详情"}],"bid":8,"content":"为确保车辆安全，请立刻前往确认","did":10007,"event":1010,"expire":0,"exts":{"avg_speed":959,"begin_pos":"上海市 闵行区 友东路 靠近赢嘉大厦B座","begin_time":"2019-11-28 10:11:50","carbon_save":522,"end_pos":"上海市 闵行区 宜山路 靠近兴迪商务大厦","end_time":"2019-11-28 12:55:14","max_speed":0,"miles":2613,"min_speed":0,"times":9804,"track_id":76598,"ts":1574907110000000000,"ts_display":"10:11","type":2},"icon":"","name":"","pop":1,"priority":0,"sound":1,"title":"您的爱车于10:22 在未打开电门时发生移动","ts":1575253327,"type":1,"uid":210}
     */

    private String path;
    private HeaderBean header;
    private BodyBean body;

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

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeaderBean implements Serializable{
        /**
         * to : 210
         * uuid : 3271f0f5-bf17-4834-be9e-f9ee5f2bc982
         * ts : 1575253327
         * ack : 1
         * event : 0
         */

        private String to;
        private String uuid;
        private int ts;
        private int ack;
        private int event;

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

        public int getEvent() {
            return event;
        }

        public void setEvent(int event) {
            this.event = event;
        }
    }

    public static class BodyBean implements Serializable{
        /**
         * actions : [{"action":2,"result":"","text":"忽略"},{"action":1,"result":"路径详情","text":"查看详情"}]
         * bid : 8
         * content : 为确保车辆安全，请立刻前往确认
         * did : 10007
         * event : 1010
         * expire : 0
         * exts : {"avg_speed":959,"begin_pos":"上海市 闵行区 友东路 靠近赢嘉大厦B座","begin_time":"2019-11-28 10:11:50","carbon_save":522,"end_pos":"上海市 闵行区 宜山路 靠近兴迪商务大厦","end_time":"2019-11-28 12:55:14","max_speed":0,"miles":2613,"min_speed":0,"times":9804,"track_id":76598,"ts":1574907110000000000,"ts_display":"10:11","type":2}
         * icon :
         * name :
         * pop : 1
         * priority : 0
         * sound : 1
         * title : 您的爱车于10:22 在未打开电门时发生移动
         * ts : 1575253327
         * type : 1
         * uid : 210
         */

        private int bid;
        private String content;
        private int did;
        private int event;
        private int expire;
        private ExtsBean exts;
        private String icon;
        private String name;
        private int pop;
        private int priority;
        private int sound;
        private String title;
        private int ts;
        private int type;
        private int uid;
        private String txt;
        private List<ActionsBean> actions;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public int getEvent() {
            return event;
        }

        public void setEvent(int event) {
            this.event = event;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public ExtsBean getExts() {
            return exts;
        }

        public void setExts(ExtsBean exts) {
            this.exts = exts;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPop() {
            return pop;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getSound() {
            return sound;
        }

        public void setSound(int sound) {
            this.sound = sound;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTs() {
            return ts;
        }

        public void setTs(int ts) {
            this.ts = ts;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public List<ActionsBean> getActions() {
            return actions;
        }

        public void setActions(List<ActionsBean> actions) {
            this.actions = actions;
        }

        public static class ExtsBean implements Serializable{
            /**
             * avg_speed : 959
             * begin_pos : 上海市 闵行区 友东路 靠近赢嘉大厦B座
             * begin_time : 2019-11-28 10:11:50
             * carbon_save : 522
             * end_pos : 上海市 闵行区 宜山路 靠近兴迪商务大厦
             * end_time : 2019-11-28 12:55:14
             * max_speed : 0
             * miles : 2613
             * min_speed : 0
             * times : 9804
             * track_id : 76598
             * ts : 1574907110000000000
             * ts_display : 10:11
             * type : 2
             */

            private int avg_speed;
            private String begin_pos;
            private String begin_time;
            private int carbon_save;
            private String end_pos;
            private String end_time;
            private int max_speed;
            private int miles;
            private int min_speed;
            private int times;
            private int track_id;
            private long ts;
            private String ts_display;
            private int type;

            public int getAvg_speed() {
                return avg_speed;
            }

            public void setAvg_speed(int avg_speed) {
                this.avg_speed = avg_speed;
            }

            public String getBegin_pos() {
                return begin_pos;
            }

            public void setBegin_pos(String begin_pos) {
                this.begin_pos = begin_pos;
            }

            public String getBegin_time() {
                return begin_time;
            }

            public void setBegin_time(String begin_time) {
                this.begin_time = begin_time;
            }

            public int getCarbon_save() {
                return carbon_save;
            }

            public void setCarbon_save(int carbon_save) {
                this.carbon_save = carbon_save;
            }

            public String getEnd_pos() {
                return end_pos;
            }

            public void setEnd_pos(String end_pos) {
                this.end_pos = end_pos;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public int getMax_speed() {
                return max_speed;
            }

            public void setMax_speed(int max_speed) {
                this.max_speed = max_speed;
            }

            public int getMiles() {
                return miles;
            }

            public void setMiles(int miles) {
                this.miles = miles;
            }

            public int getMin_speed() {
                return min_speed;
            }

            public void setMin_speed(int min_speed) {
                this.min_speed = min_speed;
            }

            public int getTimes() {
                return times;
            }

            public void setTimes(int times) {
                this.times = times;
            }

            public int getTrack_id() {
                return track_id;
            }

            public void setTrack_id(int track_id) {
                this.track_id = track_id;
            }

            public long getTs() {
                return ts;
            }

            public void setTs(long ts) {
                this.ts = ts;
            }

            public String getTs_display() {
                return ts_display;
            }

            public void setTs_display(String ts_display) {
                this.ts_display = ts_display;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class ActionsBean implements Serializable{
            /**
             * action : 2
             * result :
             * text : 忽略
             */

            private int action;
            private String result;
            private String text;

            public int getAction() {
                return action;
            }

            public void setAction(int action) {
                this.action = action;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
