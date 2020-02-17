package com.sharkgulf.soloera.tool.testtool;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MD5Utils {

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5Utils() {
    }

    // 返回形式为数字跟字符串
    public static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    public static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(byte[] bByte) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteToString(md.digest(bByte));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMD5Code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }



    public static String formatServiceData(Map<String, byte[]> servicesDatas) {
        if (servicesDatas == null) {
            return "";
        }
        String str = "";
        for (String key : servicesDatas.keySet()) {
            str += "uuid:0x" + key + " data:0x" + OKBLEDataUtils.BytesToHexString(servicesDatas.get(key)) + "\n";
        }

        return str.substring(0, str.length() - 1);
    }


}