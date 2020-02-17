package com.trust.demo.basis.trust.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trust.demo.basis.base.TrustApplication;

import java.io.File;

/**
 * Created by Trust on 2018/5/19.
 * <p>
 * App工具类
 */
public class TrustAppUtils {

    /**
     * 获取上下文对象
     *
     * @return 上下文对象
     */
    public static Context getContext() {
        return TrustApplication.Companion.getContexts();
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return TrustApplication.Companion.getHandlers();
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return TrustApplication.Companion.getMainThreadIds();
    }

    /**
     * 获取版本名称
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取版本号
     */
    public static int getAppVersionCode(Context context) {
        int versioncode = -1;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    @SuppressLint("NewApi")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "todo";
        }
        return tm.getImei();
    }

    /**
     * 显示软键盘
     */
    public static void openSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }

    /**
     * 获取SD卡路径
     *
     * @return 如果sd卡不存在则返回null
     */
    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment
                .MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }

    /**
     * 安装文件
     *
     * @param data
     */
    public static void promptInstall(Context context, Uri data) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

    public static void copy2clipboard(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("clip", text);
        cm.setPrimaryClip(clip);
    }

    /**
     * 判断是否运行在主线程
     *
     * @return true：当前线程运行在主线程
     * fasle：当前线程没有运行在主线程
     */
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    /**
     * 运行在主线程
     *
     * @param r 运行的Runnable对象
     */
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }


    public static String getResourcesString(Context context, int stringId) {
        return context.getResources().getString(stringId);
    }

    public static String getEdString(EditText editText) {
        if (editText != null) {
            return editText.getText().toString().trim();
        }
        return null;
    }


    public static String getChannel(String channelKey) throws PackageManager.NameNotFoundException {
        String channelStr = getContext().getPackageManager()
                .getApplicationInfo(getContext().getPackageName(),
                        PackageManager.GET_META_DATA).metaData.getString(channelKey);
        return channelStr;
    }

    //判断是否开启推送
    public static boolean isNotificationEnabled(Context context) {
        boolean isOpened = false;
        try {
            isOpened = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        return isOpened;

    }

    //跳转打开推送设置
    public static void gotoSet(Context context) {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }


    public static void initBleStatus(BleStatus.BleStatusListener listener) {
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        TrustAppUtils.getContext().registerReceiver(new BleStatus(listener), intentFilter);
    }

    public static class BleStatus extends BroadcastReceiver {
        private BleStatusListener listener;

        public BleStatus(BleStatusListener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    listener.BleClose();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    listener.BleCloseing();
                    break;
                case BluetoothAdapter.STATE_ON:
                    listener.BleOpen();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    listener.BleOpening();
                    break;
            }
        }

        public interface BleStatusListener {
            public void BleOpening();

            public void BleOpen();

            public void BleClose();

            public void BleCloseing();
        }
    }



    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean isEMUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("HUAWEI".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean isOPPO() {
        String manufacturer = Build.MANUFACTURER;
        if ("OPPO".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean isVIVO() {
        String manufacturer = Build.MANUFACTURER;
        if ("vivo".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
}
