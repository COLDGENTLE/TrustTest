package com.sharkgulf.soloera.tool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.sharkgulf.soloera.R;
import com.sharkgulf.soloera.home.MainActivity;
import com.trust.demo.basis.trust.utils.TrustAppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sharkgulf.soloera.tool.config.PublicMangerKt.getContext;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class NotificationUtil {
    private static String channelId = "nomorlNotification";
    private static Context mContext;
    public static void initNotificationConfig(Context context){
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "即时提示通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance,context);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(String channelId, String channelName, int importance, Context context) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }




    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
    public static void showNotification(Context context) {
        Notification notification = new NotificationCompat.Builder(context)
                /**设置通知左边的大图标**/
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                /**设置通知右边的小图标**/
                .setSmallIcon(R.mipmap.ic_launcher)
                /**通知首次出现在通知栏，带上升动画效果的**/
                .setTicker("通知来了")
                /**设置通知的标题**/
                .setContentTitle("这是一个通知的标题")
                /**设置通知的内容**/
                .setContentText("这是一个通知的内容这是一个通知的内容")
                /**通知产生的时间，会在通知信息里显示**/
                .setWhen(System.currentTimeMillis())
                /**设置该通知优先级**/
                .setPriority(Notification.PRIORITY_DEFAULT)
                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
                .setAutoCancel(true)
                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
                .setOngoing(false)
                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        /**发起通知**/
        notificationManager.notify(0, notification);
    }

    /**
     * 显示一个下载带进度条的通知
     *
     * @param context 上下文
     */
    public static void showNotificationProgress(Context context) {
        //进度条通知
        final NotificationCompat.Builder builderProgress = new NotificationCompat.Builder(context);
        builderProgress.setContentTitle("下载中");
        builderProgress.setSmallIcon(R.mipmap.ic_launcher);
        builderProgress.setTicker("进度条通知");
        builderProgress.setProgress(100, 0, false);
        final Notification notification = builderProgress.build();
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        //发送一个通知
        notificationManager.notify(2, notification);
        /**创建一个计时器,模拟下载进度**/
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int progress = 0;

            @Override
            public void run() {
                Log.i("progress", progress + "");
                while (progress <= 100) {
                    progress++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //更新进度条
                    builderProgress.setProgress(100, progress, false);
                    //再次通知
                    notificationManager.notify(2, builderProgress.build());
                }
                //计时器退出
                this.cancel();
                //进度条退出
                notificationManager.cancel(2);
                return;//结束方法
            }
        }, 0);
    }

    /**
     * 悬挂式，支持6.0以上系统
     *
     * @param context
     */

    private static int notificationNum = 1;
    private static List<Integer> notificationList = new ArrayList<>();

    @SuppressLint("NewApi")
    public static void showFullScreen(final Context context, int ic, String contentTitle, Boolean isError) {
        synchronized (context){
            Intent clickIntent = new Intent();
            final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,channelId);
//            mBuilder.setContentTitle(title)
//                    .setContentText(msg)
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                    .setOnlyAlertOnce(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setColor(Color.RED);

//            mBuilder.setContentText(contentTitle)
//                    .setSmallIcon(ic)
//                    .setOnlyAlertOnce(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setColor(Color.RED);
            int layoutId;

            if (isXiaoMi()) {
                layoutId = R.layout.notification_xiao_mi_layout;
            }else{
                if (isError) {
                    layoutId = R.layout.notification_layout;
                }else{
                    layoutId = R.layout.notification_nomol_layout;
                }
            }


            RemoteViews remoteView = new RemoteViews(getContext().getPackageName(),layoutId);
            remoteView.setTextViewText(R.id.notification_layout_tv, contentTitle);
            if (isXiaoMi()) {
                int textColor =0;
                if (isError) {
                    textColor = context.getColor(R.color.colorRead);
                }else{
                    textColor = context.getColor(R.color.colorBlack);
                }
                remoteView.setTextColor(R.id.notification_layout_tv,textColor);
            }
//            mBuilder.setCustomContentView(remoteView);
//            mBuilder.setCustomBigContentView(remoteView);
//            mBuilder.setCustomHeadsUpContentView(remoteView);
            mBuilder.setContent(remoteView).setSmallIcon(ic).setOnlyAlertOnce(true).setDefaults(Notification.DEFAULT_ALL).setWhen(System.currentTimeMillis());
            clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            // 点击删除
            Intent cancelIntent = new Intent();
            cancelIntent.setAction("notice_cancel");
            cancelIntent.putExtra("id", 1);
            PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(clickPendingIntent);
            mBuilder.setDeleteIntent(cancelPendingIntent);
            Notification build = mBuilder.setAutoCancel(true).build();
            notificationManager.notify(notificationNum, build);

//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);
//            Intent mIntent = new Intent();
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
//            builder.setContentIntent(pendingIntent);
//            builder.setSmallIcon(ic);
////        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
//            builder.setAutoCancel(true);
//            builder.setContentTitle(contentTitle);
//            //设置点击跳转
//            Intent hangIntent = new Intent();
//            //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
//            PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            builder.setFullScreenIntent(hangPendingIntent, true);
//            notificationManager.notify(notificationNum, builder.build());
            notificationList.add(notificationNum++);
            autocancleNotification();
        }

    }

    private static void autocancleNotification(){
        synchronized (mHandler){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        mHandler.sendEmptyMessage(CANCEL_NOTIFICATION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private static final int CANCEL_NOTIFICATION = 1;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CANCEL_NOTIFICATION) {
                cancelNotification(mContext);
            }
        }
    };
    public static void cancelNotification(Context context){
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (!notificationList.isEmpty()) {
            notificationManager.cancel(notificationList.get(0));
            notificationList.remove(0);
        }
    }


    /**
     * 折叠式
     *
     * @param context
     */
    public static void shwoNotify(Context context) {
//        //先设定RemoteViews
//        RemoteViews view_custom = new RemoteViews(context.getPackageName(), R.layout.view_custom);
//        //设置对应IMAGEVIEW的ID的资源图片
//        view_custom.setImageViewResource(R.id.custom_icon, R.mipmap.icon);
//        view_custom.setTextViewText(R.id.tv_custom_title, "今日头条");
//        view_custom.setTextColor(R.id.tv_custom_title, Color.BLACK);
//        view_custom.setTextViewText(R.id.tv_custom_content, "金州勇士官方宣布球队已经解雇了主帅马克-杰克逊，随后宣布了最后的结果。");
//        view_custom.setTextColor(R.id.tv_custom_content, Color.BLACK);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"chart");
//        mBuilder.setContent(view_custom)
//                .setContentIntent(PendingIntent.getActivity(context, 4, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//                .setTicker("有新资讯")
//                .setPriority(Notification.PRIORITY_HIGH)// 设置该通知优先级
//                .setOngoing(false)//不是正在进行的   true为正在进行  效果和.flag一样
//                .setSmallIcon(R.mipmap.icon);
//        Notification notify = mBuilder.build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        notificationManager.notify(4, notify);
    }

    private static boolean isXiaoMi(){
        return TrustAppUtils.isMIUI();
    }
}
