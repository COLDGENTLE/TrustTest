package com.trust.demo.basis.updateapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.trust.demo.basis.trust.utils.TrustAppUtils;
import com.trust.retrofit.ssl.TrustAllCerts;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Trust on 2016/7/28.
 */
public class DownLoadManager {
    public static File getFileFromServer(String path, ProgressDialog pd,Context context) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            HttpsURLConnection
                    .setDefaultSSLSocketFactory(TrustAllCerts.createSSLSocketFactory());

            URL url = new URL(path);

            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();

            HttpsURLConnection https = (HttpsURLConnection) conn;
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
            conn.setConnectTimeout(5000);

            //获取到文件的大小

            pd.setMax(conn.getContentLength());

            InputStream is = conn.getInputStream();

            File file = checkVerison();

            FileOutputStream fos = new FileOutputStream(file);

            BufferedInputStream bis = new BufferedInputStream(is);

            byte[] buffer = new byte[1024];

            int len ;

            int total=0;

            while((len =bis.read(buffer))!=-1){

                fos.write(buffer, 0, len);

                total+= len;

                //获取当前下载量

                pd.setProgress(total);

            }

            fos.close();

            bis.close();

            is.close();

            return file;

        }

        else{

            return null;

        }

    }

    @NotNull
    private static File checkVerison() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            insertImageFileIntoMediaStore("update.apk","update.apk",Environment.getExternalStorageDirectory().getPath());

//            copyFile("")
            return new File(Environment.getExternalStorageDirectory(), "updata.apk");
        }else{
            return new File(Environment.getExternalStorageDirectory(), "updata.apk");
        }
    }


    public static File getFileFromServer(String path, IBaseDownLoad.ApkListener listener, Context context) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            HttpsURLConnection
                    .setDefaultSSLSocketFactory(TrustAllCerts.createSSLSocketFactory());

            URL url = new URL(path);

            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();

            HttpsURLConnection https = (HttpsURLConnection) conn;
            https.setHostnameVerifier(DO_NOT_VERIFY);
            conn = https;
            conn.setConnectTimeout(5000);
            int contentLength = conn.getContentLength();
            //获取到文件的大小
            listener.resultAplInfo(contentLength,0);


            InputStream is = conn.getInputStream();

            File file = checkVerison();

            FileOutputStream fos = new FileOutputStream(file);

            BufferedInputStream bis = new BufferedInputStream(is);

            byte[] buffer = new byte[1024];

            int len ;

            int total=0;

            while((len =bis.read(buffer))!=-1){

                fos.write(buffer, 0, len);

                total+= len;

                //获取当前下载量
                listener.resultAplInfo(contentLength,total);

            }

            fos.close();

            bis.close();

            is.close();

            return file;

        }

        else{

            return null;

        }

    }



    public static SSLSocketFactory getSSLSocketFactory(Context context, String name)  {

        try
        {
            /*
            InputStream certificates = context.getAssets().open(name);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;

            for (InputStream certificate : certificates)
            {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias,
                        certificateFactory.generateCertificate(certificate));

                try
                {
                    if (certificate != null){
                        certificate.close();}
                } catch (IOException e)
                {
                }
            }

*/

            //初始化keystore
            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(context.getAssets().open("cacerts_sy.bks"), "changeit".toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory
                    .getDefaultAlgorithm());

            keyManagerFactory.init(clientKeyStore, "000000".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(clientKeyStore);

//
//            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers()
//                    , new SecureRandom());


            sslContext.init
                    (null,trustManagerFactory.getTrustManagers(),new SecureRandom()
                            //第一个参数是验证服务器返回 第二个参数 是请求的时候带着 服务器的证书让服务器验证的
                    );

            return sslContext.getSocketFactory();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };




    /**
     * AndroidQ以上保存图片到公共目录
     *
     * @param imageName 图片名称
     * @param imageType 图片类型
     * @param relativePath 缓存路径
     */
    private static Uri insertImageFileIntoMediaStore (String imageName, String imageType,
                                                      String relativePath) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return null;
        }
        if (TextUtils.isEmpty(relativePath)) {
            return null;
        }
        Uri insertUri = null;
        ContentResolver resolver = TrustAppUtils.getContext().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DESCRIPTION, imageName);
        //设置文件类型为image/*

        values.put( MediaStore.Images.Media.MIME_TYPE, "image/" + imageType);
        //注意：MediaStore.Images.Media.RELATIVE_PATH需要targetSdkVersion=29,
        //故该方法只可在Android10的手机上执行
        values.put("apkPath", relativePath);

        Uri external = MediaStore.Files.getContentUri("apk");
        insertUri = resolver.insert(external, values);
        return insertUri;
    }


    public static OutputStream copyFile (String sourceFilePath, final Uri insertUri) throws FileNotFoundException {
        if (insertUri == null) {
            return null;
        }
        ContentResolver resolver = TrustAppUtils.getContext().getContentResolver();

     return  resolver.openOutputStream(insertUri);
    }
}
