package com.sharkgulf.soloera;

import com.sharkgulf.soloera.cards.activity.BarAdapter;
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 *Created by Trust on 2018/12/18
 */
public class TrustAppConfig {
    //地图卡片
    public static int BS_MAP_CARD = 0x00001;
    //车辆状态卡片
    public static int BS_CAR_CARD = 0x00002;
    //电池卡片
    public static int BS_BATTERY_CARD = 0x00003;
    //远程控制
    public static int BS_CONTROL_CARD = 0x00004;
    //车辆网络卡片
    public static int BS_CARD_INTERNET_STATUS_CARD = 0x00005;
    //广告卡片 (View)
    public static int BS_AD_CARD = 0x00006;
    //用车报告
    public static int BS_CAR_HISTORY_CARD = 0x00007;
    //车辆云平台注册卡片
    public static int BS_CAR_REGIST_SECTION_BLE_CARD = 0x00008;
    //应急电池卡片
    public static int BS_EMERGENCY_BATTERY_CARD = 0x00009;
    //安防设置卡片
    public static int BS_SECURITY_SETTINGS_CARD = 0x00010;
    public static ArrayList<Integer> mainRvControlOrder = initSectionMainRvControlOrder();


    public static void initAppConfig(){
        iniBleAuthority();
    }

    /**
     * 车辆已经激活
     * 添加全部卡片
     * @return
     */
    public static ArrayList<Integer> initAllMainRvControlOrder(){
        mainRvControlOrder = new ArrayList<>();
//        mainRvControlOrder.add(BS_CAR_CARD);
        mainRvControlOrder.add(BS_CONTROL_CARD);
//        mainRvControlOrder.add(BS_BATTERY_CARD);
        mainRvControlOrder.add(BS_MAP_CARD);
//        mainRvControlOrder.add(BS_SECURITY_SETTINGS_CARD);
        mainRvControlOrder.add(BS_CAR_HISTORY_CARD);
//        mainRvControlOrder.add(BS_CARD_INTERNET_STATUS_CARD);
//        mainRvControlOrder.add(BS_AD_CARD);
        return mainRvControlOrder;
    }

    /**
     * 车辆未激活
     * 添加部分卡片
     */

    public static ArrayList<Integer> initSectionMainRvControlOrder(){
        mainRvControlOrder = new ArrayList<>();
        mainRvControlOrder.add(BS_CONTROL_CARD);
        mainRvControlOrder.add(BS_CAR_REGIST_SECTION_BLE_CARD);
//        mainRvControlOrder.add(BS_CARD_INTERNET_STATUS_CARD);
//        mainRvControlOrder.add(BS_AD_CARD);
        return mainRvControlOrder;
    }

    public static String token = "";//
    public static int userId;//
    public static int bikeId = 0;
    public static String bikeName;
    public static String userPhone = "";

    public static String appChannel = "";

    public static String HTTP_RELEASE_HOST = "https://api.bluesharkmotor.com/";
    public static String HTTP_DEBUG_HOST = "https://api.d.bluesharkmotor.com/";
//    public static String HTTP_RELEASE_HOST = "http://gateway.sg-bs.192.168.0.222.xip.io/";
//    public static String HTTP_DEBUG_HOST = "http://gateway.sg-bs.192.168.0.222.xip.io/";
    public static String HTTP_HOST = HTTP_RELEASE_HOST;

    public static String SOCEK_RELEASE_HOST = "wss://ws.bluesharkmotor.com/ws/conn?token=";
    public static String SOCEK_DEBUG_HOST = "wss://ws.d.bluesharkmotor.com/ws/conn?token=";
    public static String SOCEK_HOST = SOCEK_RELEASE_HOST;

    public static String URL_SEND_SMSV = "app/user/sendsmsvc";//发送短信验证码

    public static String URL_GET_SMSV = "app/user/getcapcha";//获取验证码

    public static String URL_GET_USER_PWD_KEY = "app/user/getpwdsalt";//获取用户密码key

    public static String URL_USER_LOGIN = "app/user/login";//用户登陆

    public static String URL_USER_EXT = "app/user/logout";//用户退出登陆

    public static String URL_GET_USER_INFO = "app/user/getuserinfo";//获取用户信息

    public static String URL_GET_CAR_INFO = "app/bike/getbikelist";//获取车辆信息

    public static String URL_CONTROLL_CAR = "app/bike/remotectrl";//远程控制车辆

    public static String URL_BIND_CAR = "app/bike/addbike";//绑定车辆  获取车辆鉴权信息

    public static String URL_GET_BIND_INFO = "app/bike/getbindinfo";//绑定车辆  获取车辆状态

    public static String URL_UNBIND_CAR = "app/bike/delbike";//删除车辆

    public static String URL_GET_BLE_SIGN = "app/bike/getbtsign";//获取车辆绑定蓝牙签名

    public static String URL_UPDATE_BIND_INRO = "app/bike/updatebindstatus";//更新绑定状态

    public static String URL_GET_CAR_LOCATION = "app/bike/getposition";//获取车辆位置

    public static String URL_UPDATE_CAR_INFO = "app/bike/updateprofile";//更新车辆信息

    public static String URL_CHECK_APP_VERSION = "app/checkupdate";//检查app版本

    public static String URL_UPLOAD_FILE = "app/uploadfile";//用户上传文件

    public static String URL_CHECK_USER_IS_REGISTER = "app/user/checkuserreg";//检查用户注册

    public static String URL_SET_PWD = "app/user/setpassword";//用户设置密码(适合有旧密码和没有旧密码2种情况)

    public static String URL_RESET_PWD = "app/user/resetpassword";//用户重置密码(用户未登录情况)

    public static String URL_UPDATE_PROFILE = "app/user/updateprofile";//更新用户资料

    public static String URL_USER_REGISTER = "app/user/register";//用户注册密码盐

    public static String URL_CHECK_SMSVC = "app/user/validatesmsvc";//验证短信验证码

    public static String URL_GET_POWER= "app/bike/getbattstatus";//获取电池数据

    public static String URL_GET_TIME_LEVEL= "app/bike/gettimelevel";//获取时间粒度（协助app绘图）

    public static String URL_GET_HIRSTORY= "/app/bike/getridesummary";// 获取行车统计

    public static String URL_GET_RIDEREPORT= "/app/bike/getridereport";//# 获取行车报告

    public static String URL_GET_RIDETRACK= "/app/bike/getridetrack";// 获取骑行轨迹

    public static String URL_CHECK_USER_THREE= "/app/user/checkthirdbind";// 检查用户三方是否绑定

    public static String URL_GET_TICKET= "/app/user/getticket";// web获取二级token

    public static String URL_CHECK_IN_STATUS= "/app/user/getcheckinstatus";//检查签到

    public static String URL_POINT_INFO= "/app/mbrpts/getpointinfo"; // 获取积分情况

    public static String URL_POINT_DETAIL= "/app/mbrpts/getpointdetail"; // 获取积分明细

    public static String URL_CHECKIN_DAILY= "/app/user/checkindaily"; // 每日签到

    public static String URL_GET_ORDER_INFO= "/app/order/pingpppay"; // 获取ping++支付订单信息

    public static String URL_GET_PAY_STATUS= "/app/order/payfinished"; // 查询ping++支付结果

    public static String URL_CHANGE_SECURITY= "/app/bike/changesecurity"; // 修改安防设置

    public static String URL_TEST_WARING_NOTIFY= "/push/testnotify"; // 测试报警

    public static String URL_PUSH_POST_PUSH= "/push/postpush"; // 提交极光registerId

    public static String URL_LOST_MODE= "/app/bike/lostmode"; // 丢车模式

    public static String URL_ALERT_LIST= "/push/event/getlist"; // 消息列表

    public static String URL_UPDATE_PHONE= "/app/user/updatepn"; // 修改手机号

    public static String URL_UPDATE_PHONE_AND_THREE= "/app/user/bindpn"; // 第三方登录关联手机号


    /**user/register
     * 验证码页面跳转类型
     */
    public static int INTENT_ACTIVITY = 0;//从手机号登陆跳转
    public static int INTENT_FRAGMENT = 1;//忘记密码跳转

    public static int PWD_RESET = 1;//忘记密码
    public static int PWD_REGISTER = 0;//注册


    /**
     * 验证码类型
     */
    public static int SEND_SMS_SVC_TYPE_SMS = 1;//短信
    public static int SEND_SMS_SVC_TYPE_VOICE = 2;//语音

    /**
     * 三方登录类型
     */
    public static int LOGIN_WX = 0;
    public static int LOGIN_WB = 1;
    public static int LOGIN_QQ = 2;
    public static String THREE_LOGIN_TYPE_KEY = "THREE_LOGIN_TYPE_KEY";
    /**
     * 登陆类型
     */

    public static int LOGIN_TYPE_SMS = 1;//短信登陆
    public static int LOGIN_TYPE_PWD = 2;//手机密码登陆
    public static int LOGIN_TYPE_EMAIL = 3;//邮箱密码登陆
    public static int LOGIN_TYPE_THERE = 4;//第三方登陆

    public static int CHECK_THERE = 3; //检查三方

    public static String LOGIN_TYPE_KEY = "LOGIN_TYPE_KEY";
    public static String EXT_TYPE_KEY = "EXT_TYPE_KEY";


    /**
     * 更新绑定状态
     */
    public static int BIND_STATUS_SUCCESS  =1;
    public static int BIND_STATUS_ERROR  =2;

    /**
     * 触发类型
     */

    public static int TYPE_USER = 0;//用户触发
    public static int TYPE_SYSTEM = 1;//程序触发


    /**
     * 获取防刷验证码类型Anti-brush
     */
    public static int ANTI_BRUSH_TYPE_NUM_IMG = 1;//数字 图片
    public static int ANTI_BRUSH_TYPE_IMG = 2;//滑动图片


    /**
     * 车辆控制 下发类型
     */
    public static int CONTROLL_CAR_BLE = 0;//蓝牙下发
    public static int CONTROLL_CAR_INTERNET = 1;//网络下发


    /**
     * 手机与蓝牙连接状态
     */
    public static int CONTROLL_STATUS = CONTROLL_CAR_INTERNET;

    /**
     * 车辆控制 蓝牙 action
     */
    public static int ACTION_OPEN = 2;//开锁
    public static int ACTION_CLOSE = 3;//关锁
    public static int ACTION_NO_MUSIC_CLOSE = 4;//静音关闭关

    public static int ACTION_LOCK_BIKE = 1;//锁车
    public static int ACTION_LOCK_BIKE_CANCEL = 2;//取消锁车
    public static int ACTION_UNLOCK_BIKE = 3;//解锁
    public static int ACTION_UNLOCK_BIKE_CANCEL = 4;//取消解锁

    /**
     * 车辆控制 intenet
     */
    public static int INTERNET_ACTION_START_CAR = 1;//一键启动
    public static int INTERNET_ACTION_OPEN_ELE = 2;//开电门
    public static int INTERNET_ACTION_OFF_LOCK = 3;//设防
    public static int INTERNET_ACTION_OPEN_LOCK = 4;//撤防
    public static int INTERNET_ACTION_OFF_ELE = 5;//关电门
    public static int INTERNET_ACTION_FIND_CAR = 6;//寻车
    public static int INTERNET_ACTION_OPEN_BUCKET = 7;//开坐桶


    /**
     * 用车报告 当天详情 itemType
     */
    public static int ITEM_TYPE_STROKE = 0;
    public static int ITEM_TYPE_MALFUNCTION = 1;

    public static List<Integer> hirstoryItemTypeList = iniAllInfo();

    public static List<Integer> iniAllInfo(){
        hirstoryItemTypeList = new ArrayList<>();
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        hirstoryItemTypeList.add(ITEM_TYPE_STROKE);
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        hirstoryItemTypeList.add(ITEM_TYPE_STROKE);
        hirstoryItemTypeList.add(ITEM_TYPE_STROKE);
        hirstoryItemTypeList.add(ITEM_TYPE_STROKE);
        hirstoryItemTypeList.add(ITEM_TYPE_MALFUNCTION);
        return hirstoryItemTypeList;
    }


    /**
     * 车辆网络状态
     */

    public static int CAR_STATUS_GPRS_SUCCECC = 1;//正常
    public static int CAR_STATUS_GPRS_NO_ACTIVATION = 2;//未激活
    public static int CAR_STATUS_GPRS_UNKNOWN = 3;//待审核

    public static int USER_NO_CAR = 0;

    /**
     * 用户注册type
     */
    public static int USER_REGISTER_PHONE = 1;
    public static int USER_REGISTER_EMAIL = 2;
    public static int USER_REGISTER_THERE = 3;

    /**
     * 是否是 注册  或者 忘记密码状态
     */
    public static int uiTypeStatus = PWD_REGISTER;


    /**
     * 定位模式
     * 0 GPS定位
     * 1 基站定位
     *
     * MAP_TYPE_NAME  保存本地文件得名称
     */
    public static int MAP_TYPE_GPS = 0;
    public static int MAP_TYPE_BS = 1;

    public static int MAP_TYPE = MAP_TYPE_GPS;

    public static String MAP_TYPE_NAME = "MAP_TYPE_NAME";


    /**
     * 隐私 用户 协议连接
     */

    public static String WEB_URL_DEBUG_HOST = "https://app-web.d.blueshark.com/";
    public static String WEB_URL_RELEASE_HOST = "https://app-web.blueshark.com/";
    public static String WEB_URL_HOST = WEB_URL_RELEASE_HOST;
    //隐私政策
    public static String URL_PRIVACY_POLICY = "app/privacy";
    //用户服务协议
    public static String URL_USER_SERVICES_AGREEMENT = "app/userprotocol";
    //web页面参数key
    public static String WEB_INTENT_USER_TYPE_KEY = "type";
    //发现
    public static String WEB_FIND = "app/find/index";
    //心悦
    public static String WEB_EXPER = "app/exper/index";
    /**
     * 电池支持数量
     */
    public static int BATTERY_ONLY = 1;
    public static int BATTERY_DOUBLE = 2;
    //是否是双电池
    public static Boolean isDouble = false;

    public static int [] batteryIcs = new int[]{R.drawable.battery_1,R.drawable.battery_2,
            R.drawable.battery_3,R.drawable.battery_4,R.drawable.battery_5,R.drawable.battery_6,
            R.drawable.battery_7,R.drawable.battery_8,R.drawable.battery_9,R.drawable.battery_10,};
    public static int getBatteryIc(int num){
        return batteryIcs[num-1];
    }


    /**
     * 蓝牙鉴权指令 错误码
     */
    public static HashMap<Integer,String> mBleAuthority = new HashMap<>();

    public static void iniBleAuthority(){
        mBleAuthority.put(-1,"ble指令超时");
        mBleAuthority.put(1,"鉴权失败");
        mBleAuthority.put(2,"数据为空");
        mBleAuthority.put(3,"uuid不合法");
        mBleAuthority.put(4,"没有此特征值");
        mBleAuthority.put(5,"没有此功能");
        mBleAuthority.put(7,"ble内部操作返回失败");
        mBleAuthority.put(8,"其他错误");
        mBleAuthority.put(9,"设备处于绑定状态不能绑定");
        mBleAuthority.put(10,"设备处于出厂状态");
    }

    /**
     * 时间戳
     */
    //一天的时间戳
    public static Long DAY_TIME = 1000 * 60 * 60 * 24L;
    public static String[] weeks = new String[]{"周一","周二","周三","周四","周五","周六","周日"};


    /**
     * 蓝牙鉴权
     */
    public static int BLE_TYPE_CONTEXT = 0;//直接鉴权
    public static int BLE_TYPE_FIRST_CONTEXT = 1;//第一次鉴权


    /**
     * 删除车辆状态
     */
    public static int DELETE_CAR_NO_START = 0;
    public static int DELETE_CAR_STARTING = 1;
    public static int DELETE_CAR_SUCCESS = 2;
    public static int DELETE_CAR_ERROR = 3;


    /**
     * 日周月
     */
    public static int DATA_TYPE_DAY = 1;
    public static int DATA_TYPE_WEEK = 2;
    public static int DATA_TYPE_MONTH = 3;
    public static int DATA_TYPE = DATA_TYPE_DAY;
    public static List<BarAdapter.HistoryBean> dayList;
    public static List<BarAdapter.HistoryBean> weekList;
    public static List<BarAdapter.HistoryBean> monthList;
    public static HashMap<String,ArrayList<BarAdapter.HistoryBean>> dayMonthMap = new HashMap<>();


    /**
     * 绑定车辆的时间
     */
    public static Long bindTime = 0L;


    /**
     * 跳转login页面的类型
     */
    //主页面跳转
    public static int LOGIN_TYPE_MAIN_HOME = 0;
    //退出跳转
    public static int LOGIN_TYPE_EXITE = 1;

    //跳转type key
    public static String LOGIN_INTENT_TYPE = "LOGIN_INTENT_TYPE";


    // 车辆信息 key
    public static String CAR_INFO_KEY = "CAR_INFO_KEY";

    /**
     * 签到状态
     */
    public static int CHECK_IN_STATUS_NOTHING = 0;
    public static int CHECK_IN_STATUS_SUCCESS = 1;


    /**
     * 商城用户登录状态
     */
    public static int SHOPPING_STATUS_VISITOR =  0;
    public static int SHOPPING_STATUS_USER =  1;



    /**
     *webSocket
     */
    //车辆位置getlist
    public static String WEB_SOKECT_CAR_LOCTION = "/bike/bikeposition";
    //电池信息
    public static String WEB_SOKECT_CAR_POWER = "/bike/battinfo";
    //行车统计
    public static String WEB_SOKECT_CAR_RIDE_COUNT = "/bike/ridecount";
    //车辆状态
    public static String WEB_SOKECT_CAR_INFO = "/bike/bikeinfo";
    //开电门
    public static String WEB_SOKECT_CAR_ACCON = "/cmd/bikeaccon";
    //关电门
    public static String WEB_SOKECT_CAR_ACCOFF = "/cmd/bikeaccoff";
    //一键启动
    public static String WEB_SOKECT_CAR_START = "/cmd/bikestart";
    //设防
    public static String WEB_SOKECT_CAR_ALERTON = "/cmd/bikealerton";
    //撤防
    public static String WEB_SOKECT_CAR_ALERTOFF = "/cmd/bikealertoff";
    //寻车
    public static String WEB_SOKECT_CAR_FIND = "/cmd/bikefind";
    //开坐桶
    public static String WEB_SOKECT_CAR_BUCKET_OPEN = "/cmd/bikebucketopen";
    //锁车
    public static String WEB_SOKECT_CAR_LOCK = "/cmd/bikelock";
    //取消锁车
    public static String WEB_SOKECT_CAR_LOCK_CANCEL = "/cmd/bikelockcancel";
    //解锁
    public static String WEB_SOKECT_CAR_UNLOCK = "/cmd/bikeunlock";
    //取消解锁
    public static String WEB_SOKECT_CAR_UNLOCK_CANCEL = "/cmd/bikeunlockcancel";
    //抢登
    public static String WEB_SOKECT_CAR_ROB_LOGIN = "/rob/login";
    //蓝牙激活
    public static String WEB_BLE_STATUS = "ble/checkpassword/success";
    //车辆当前状态
    public static String WEB_CAR_STATUS = "/bike/bikestatus";
    //推送报警
    public static String WEB_PUSH_ALERT = "/bikealert";


    //web 心跳
    public static String WEB_SOKECT_HEART = "/heart/ping";

    //客户端发起receive
    public static String WEB_SOKECT_SEND = "/ws";
    //客户端接收
    public static String WEB_SOKECT_RECEIVE = "/push";
    //客户端响应
    public static String WEB_SOKECT_CACK = "/cack";
    //service 响应
    public static String WEB_SOKECT_SACK = "/sack";


    //web连接状态
    public static String WEB_CONNECT_SUCCESS = "web/connect/success";
    public static String WEB_CONNECT_ERROR = "web/connect/error";

    //蓝牙连接
    public static String BLE_CONNECT_SUCCESS = "ble/connect/success";
    public static String BLE_CONNECT_CLOSE = "ble/connect/close";
    public static String BLE_STATUS = "ble/connect/status";
    public static String BLE_ELETRION = "ble/cmd/eletrion";
    public static String BLE_CUSHION_INDUCTION = "ble/cmd/cushionInduction";
    public static String BLE_CONTROLL_BIKE_INFO = "ble/result/bikeInfo";
    public static String BLE_CHECK_PASS_WORD_SUCCESS = "ble/checkpassword/success";


    //app内部通讯
    //是否有未读消息
    public static String APP_MESSAGER_READ = "app/messager/read";
    //是否更新首页安防设置状态
    public static String APP_HOME_SECURITY_SETTINGS = "app/status/securitySettings/update/";
    //mainHomeActivity update
    public static String APP_MAIN_HOME_UPDATE = "app/status/mainHome/update/";
    //手机号发送验证码调用返回
    public static String VERIFICATION_CODE_BACK = "app/controll/login/back/";
    //地图页面修改 基站开关
    public static String APP_CHOOSE_MAP_BS_STATUS = "app/map/bs/status/";
    //本地更新车辆信息
    public static String APP_UPDATE_CAR_INFO = "app/update/car/info/";
    //刷新本地车辆列表
    public static String APP_UPDATE_BIKE_LIST = "app/update/bike/list/";


    public static int BLE_STATUS_CLOSE = 0;
    public static int BLE_STATUS_OPEN = 1;
    public static int BLE_STATUS_CONNECT_SUCCESS = 2;
    public static int BLE_STATUS_CONNECT_CLOSE = 3;



    //web 是否应答
    public static int IS_ANSWER = 1;
    public static int NO_ANSWER = 0;




    public static HashMap swMap = new HashMap<Integer,Boolean>();

    public static void initSwMap(CarInfoBean.SecurityBean  bean){

        if (bean != null) {
            CarInfoBean.SecurityBean.CustomBean custom = bean.getCustom();
            if (custom != null) {
                swMap.put(R.id.alert_medium_shock_sw,custom.isVibr_moderate());
                swMap.put(R.id.alert_power_low_sw,custom.isPower_low());
            }else{
                swMap.put(R.id.alert_medium_shock_sw,false);
                swMap.put(R.id.alert_power_low_sw,false);
            }
        }else{
            swMap.put(R.id.alert_medium_shock_sw,false);
            swMap.put(R.id.alert_power_low_sw,false);
        }
            swMap.put(R.id.alert_move,true);
            swMap.put(R.id.alert_batt_out,true);
            swMap.put(R.id.alert_serious_shock_sw,true);

    }


    //电池仓位
    public static int BATTERY_ONE = 1;
    public static int BATTERY_TWO = 2;
    public static int BATTERY_CHARGE_STATUS_OPEN = 1;

    //车辆当前状态
    public static int carStatus = -1;

    //App是否啓動過
    public static boolean appIsInit = false;

    //默认
    public static int DEFUTE = -1;

    //体验 登录
    public static int EXPEROEMCE = 1;

    //选择车辆小标
    public static String CHOOSE_POS_KEY = "CHOOSE_POS_KEY";

    //车辆当前状态   false   代表 车辆当前状态异常 可能原因是 无车 / 未激活   true 车辆正常
    public static boolean carIsOk = false;

    //电池卡片当前能否进入
    public static boolean isPowerOk = false;

    //基站显示分钟
    public static int BS_SHOW_TIME = 15;

    public static int BS_SHOW_YELLOW_TIME = 60 * 1; //单位分钟

    public static int BS_SHOW_RED_TIME = 60 * 24; //单位分钟


    public static String COLOR_READ = "#ff7805";
    public static String COLOR_GREEN = "#00aefd";

    //电池状态
    public static int BATTRY_GOOD_TEMP = 0;
    public static int BATTRY_ADD_LOW_TEMP = 1;
    public static int BATTRY_PUT_LOW_TEMP = 2;
    public static int BATTRY_ADD_HIGH_TEMP = 3;
    public static int BATTRY_PUT_HIGH_TEMP = 4;

    //车辆绑定状态
    //未绑定
    public static int BIKE_STATUS_DEFUTE = 0;
    //已绑定
    public static int BIKE_STATUS_ADDED = 1;
    //绑定中
    public static int BIKE_STATUS_ADDING = 2;
    //准备绑定
    public static int BIKE_STATUS_READY_ADD = 3;

    //车辆控制
    public static int ACTION_SUCCESS = 0;
    public static int ACTION_ERROR = 1;
    public static int ACTION_TIME_OUT = 3;
    public static int INTERNET_ERROR = 4;
    public static int WEBSOCKET_ERROR = 5;
    public static int BIKE_IS_OFF = 6;



    //电池状态
    //普通
    public static int BATTRY_START_NOMORL = 0;
    //充电
    public static int BATTRY_START_ADD_POWER = 1;
    //移除
    public static int BATTRY_START_NOTHING = 2;
    //故障
    public static int BATTRY_STATUS_ERROR = 3;


    //车辆是否在线
    //在线
    public static int BIKE_OL = 1;
    //离线
    public static int BIKE_OFF = 2;
    public static int BIKE_OL_STATUS = BIKE_OFF;


    //进入首页的类型
    //正常进入 首页
    public static String HOME_UPDATE_KEY = "HOME_UPDATE_KEY";


    //修改用户昵称
    public static int UPDATE_USER_NICK_NAME = 0;
    //修改用户手机号
    public static int UPDATE_USER_PHONE = 1;
    //修改用户真实姓名
    public static int UPDATE_USER_NAME = 2;
    //修改用户签名
    public static int UPDATE_USER_SIGNE = 3;


    public static boolean isFirstStartHomeMain = true;
    //app是否有更新
    public static int APP_UPDATE = 1;
    //app更新方式
    public static int APP_MUST_UPDATE = 1;
    public static int APP_CAN_UPDATE = 0;

    //指定车辆id
    public static String KEY_SELECT_BIKR_ID = "KEY_SELECT_BIKR_ID";

    //用户使用方式
    public static int USE_NOMOL = 0;
    public static int USE_DEMO = 1;
    public static int USE_TYPE = USE_NOMOL;

    //虚拟演示 token
    public static String DEMO_TOKEN = "mock211b780f306cbb79c31965e8ef72bf01";


    public static int [] demoBikeList ;
    public static Long maxBindTime = Long.valueOf(DEFUTE);
}
