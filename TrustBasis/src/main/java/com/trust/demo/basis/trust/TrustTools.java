package com.trust.demo.basis.trust;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.trust.demo.basis.trust.utils.TrustAppUtils;
import com.trust.demo.basis.trust.utils.TrustLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.trust.demo.basis.trust.utils.TrustMD5Utils.getMD5;

/**
 * Created by Trust on 2017/8/7.
 * 基础工具类
 */

public class TrustTools<T extends View> implements Serializable {
    private static  TrustTools trustTools;
    private  static String configPackName;
    public static  TrustTools create(String packName){
        configPackName = packName;
        if (trustTools == null) {
            trustTools = new TrustTools();
        }
        return trustTools;
    }

    public Disposable disposable;


    /**
     * 倒计时显示
     */
    public  void  Countdown(Activity activity, final T value , final int time, final String msg){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Observable.interval(0,1, TimeUnit.SECONDS).take(time +1).map(new Function<Long, Object>() {
                    @Override
                    public Object apply(@NonNull Long aLong) throws Exception {
                        return time -aLong;
                    }
                }).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                value.setEnabled(false);//不可点击
                            }
                        }).observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@NonNull Object o) {
                                checkT(value,o+"秒");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                value.setEnabled(true);//
                                checkT(value,msg);
                            }
                        });
            }
        });
    }


    public  void  Countdown(final Activity activity, final T value , final int time, final String msg, final IsFinshTimeListener isFinshTimeListener){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Observable.interval(0,1, TimeUnit.SECONDS).take(time +1).map(new Function<Long, Object>() {
                    @Override
                    public Object apply(@NonNull Long aLong) throws Exception {

                        return time -aLong;
                    }
                }).subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                value.setEnabled(false);//不可点击
                            }
                        }).observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@NonNull Object o) {
                                if (Integer.parseInt(o.toString())==30) {
                                    if (activity!=null && !activity.isFinishing()) {
                                        isFinshTimeListener.showVoiceVerificationCode();
                                    }
                                }
                                checkT(value,o+msg);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                if (activity!=null && !activity.isFinishing()) {
                                    value.setEnabled(true);
                                    isFinshTimeListener.isFinshTimeListener();
                                }
                            }
                        });
            }
        });
    }

    public interface IsFinshTimeListener{
        void isFinshTimeListener();
        void showVoiceVerificationCode();}



    private void checkT(T v,String msg){
        if(v instanceof Button){
            ((Button)v).setText(msg);
        }else if(v instanceof TextView){
            ((TextView)v).setText(msg);
        }
    }

    /**
     * 自定义时间时间倒计时
     * @param time
     */
    public  Disposable setCountdown(int time, final CountdownCallBack countdownCallBack){
        return Observable
                .fromArray(100)
                .subscribeOn(Schedulers.io())
                .timer(time*1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ObservableTransformer<Object, Object>() {

                    @Override
                    public ObservableSource<Object> apply(Observable<Object> upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        countdownCallBack.callBackCountDown();
                    }
                });


    }



    /**
     * 自定义时间时间倒计时
     * @param time
     */
    public  Disposable setCountdown(Long time, final CountdownCallBack countdownCallBack){
        return Observable
                .fromArray(100)
                .subscribeOn(Schedulers.io())
                .timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ObservableTransformer<Object, Object>() {

                    @Override
                    public ObservableSource<Object> apply(Observable<Object> upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        countdownCallBack.callBackCountDown();
                    }
                });


    }

    public interface CountdownCallBack{
        void callBackCountDown();
    }



    //-----------------------时间--------------------------------------

    /**
     * 获取系统时间
     * @return
     */
    public static String getSystemTimeString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());//获取当前时间
        String systemTime = formatter.format(dateTime);
        return systemTime;
    }

    /**
     * 获取系统时间 data 毫秒
     * @return
     */
    public static long getSystemTimeDataMillisecond(){
        return System.currentTimeMillis();
    }


    /**
     * 获取系统时间 data 秒
     * @return
     */
    public static long getSystemTimeDataSecond(){
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 根据指定的时间 获取long
     * @param format
     * @return
     */
    public static long getTime(String format) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            long time = sdf.parse(format).getTime();

            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getTimes(String format) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long time = sdf.parse(format).getTime();

            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }





    public static long getTime( String format,String pattern) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat(pattern);
            long time = sdf.parse(format).getTime();

            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTime(Date time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str=sdf.format(time);
        return str;
    }



    public static String getTime(Long time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String str=sdf.format(time);
        return str;
    }

    public static String getTime(Long time,String pattern){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        String str=sdf.format(time);
        return str;
    }

    public static String getTimes(Long time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String str=sdf.format(time);
        return str;
    }


    public static String getTimeInfo(Long time){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
        String str=sdf.format(time);
        return str;
    }

    public static String getTime(Date time , String pattern){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        String str=sdf.format(time);
        return str;
    }

    private static final long ONE_MINUTE = 60;
    public static int getMinute(long time){
        int day = (int) (time / (24 * 60 * 60 * 1000));
        int hour = (int) (time / (60 * 60 * 1000) - day * 24);
        int mine = (int) ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        int seconds = (int) (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - mine * 60);

        return mine;
    }

    /**
     * 省略小数点后几位
     * @param v  小数
     * @param scale  几位
     *
     */
    public   static   double   round(double v,int   scale){
        if(scale<0){
            throw   new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b   =   new BigDecimal(Double.toString(v));
        BigDecimal one   =   new BigDecimal("1");
        return   b.divide(one,scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取app当前版本
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String resultAppVersion(Context context)  {

        try {
            PackageManager packageManager = context.getPackageManager();

            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    //-------------------获取相册里面的相片------------------------------------------------------------------

    /**
     * 得到图片的bitmap
     * @param data
     * @param context
     * @return
     */
    public Bitmap getImages(Intent data , Context context){
        Bitmap bitmap = null;
        if(Build.VERSION.SDK_INT >=19){
            //4.4以上
            bitmap =  handlerImageOnKikat(data,context);
        }else{
            //4.4一下
            bitmap =  handlerImageBeforeKiKat(data ,context);
        }

        return bitmap;
    }
    public static String openAlbumImgPath = null;
    /**
     * 4.4以上获取
     * @param data
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Bitmap handlerImageOnKikat(Intent data , Context context){

        Uri url = data.getData();
        if(DocumentsContract.isDocumentUri(context,url)){
            //如果是document类型的url,通过document ID 处理
            String docId = DocumentsContract.getDocumentId(url);
            if("com.android.providers.media.documents".equals(url.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                openAlbumImgPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection
                        ,context);
            }else if("com.android.providers.downloads.documents".equals(url.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                openAlbumImgPath = getImagePath(contentUri,null,context);
            }

        }else if("content".equalsIgnoreCase(url.getScheme())){
            //如果是content类型Uri 使用普通方式
            openAlbumImgPath = getImagePath(url,null,context);
        } else if ("file".equalsIgnoreCase(url.getScheme())) {
            //如果是file类型的uri 直接获取图片路径
            openAlbumImgPath = url.getPath();
        }
        return displayImage(openAlbumImgPath);
    }

    /**
     * 4.4一下获取
     * @param data
     * @param context
     * @return
     */
    private Bitmap handlerImageBeforeKiKat(Intent data , Context context){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null ,context);
        return displayImage(imagePath);
    }



    private String getImagePath(Uri uri, String selection , Context context){
        String path = null;
        //通过Uri和selection 获取真实图片路径
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }

    private Bitmap displayImage(String imagePath) {
        if (imagePath == null) {
            TrustLogUtils.e("找不到这个文件!");
            return null;
        }else{
            return   bitmapCompressionRotate(imagePath);
        }
    }

    //-----------------------图片压缩--------------------------------------------------------------

    public static int BASE64_FLAGS = Base64.NO_WRAP;
    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon,BASE64_FLAGS );
    }


    /**
     * 图片转成string
     *
     * @param map
     * @return
     */
    public static void convertIconToString(final Map<String,Bitmap> map , final onBitmapStringCallBack bitmapStringCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               Map<String,String> newMap = new WeakHashMap<String, String>();
                for (String key : map.keySet()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                    Bitmap bitmap = map.get(key);
                    int num = bitmap.getByteCount();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] appicon = baos.toByteArray();// 转为byte数组
                    newMap.put(key, Base64.encodeToString(appicon, BASE64_FLAGS));
                }
                bitmapStringCallBack.CallBack(newMap);
            }
        }).start();
    }
    public interface onBitmapStringCallBack{void CallBack(Object objectList);}


    /**
     * 图片转成string
     *
     * @return
     */
    public static void convertIconToString(final List<Bitmap> bitmapList, final onBitmapStringCallBack bitmapStringCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> newBitmapList = new ArrayList<String>();
                for (Bitmap bitmap : bitmapList) {
                    if (bitmap != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] appicon = baos.toByteArray();// 转为byte数组
                        newBitmapList.add(Base64.encodeToString(appicon, BASE64_FLAGS));
                    }
                }

                bitmapStringCallBack.CallBack(newBitmapList);
            }
        }).start();
    }




    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static byte[] convertStringToByte(String img){
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(img, BASE64_FLAGS);
            return bitmapArray;
        }catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapCompression(Bitmap bitmap){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio);
        canvas.drawBitmap(bitmap, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        TrustLogUtils.d("bitmap:"+(bitmap.getByteCount() / 1024 )+"KB"
                +"|bitmap2 大小:"+(result.getByteCount() / 1024 )+"KB");
        return result;
    }

    /**
     * bitmap 压缩,旋转,保存
     * @param fileName
     * @param filePath
     * @return
     */
    public static Bitmap bitmapCompressionRotate(String fileName, String filePath){
        try{
            int quality = 80;
            Bitmap bitmap = null;
            //
            File f = new File(filePath,fileName + ".jpg");
            int rotate = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(filePath);
                int result = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch(result) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    default:
                        rotate =  0;
                        break;
                }
            } catch (IOException e) {

                e.printStackTrace();
                TrustLogUtils.e("读取img 错误:"+e.toString());

            }
            TrustLogUtils.d("rotate :"+rotate);
            // 1:compress bitmap
            try {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true) {
                    int VARIETY_SIZE = (rotate==90 || rotate==270)?height_tmp:width_tmp;
                    if (VARIETY_SIZE / 2 <= 600){
                        if(VARIETY_SIZE>600 && VARIETY_SIZE-600>300){
                        }else{
                            break;
                        }
                    }
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }
                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
                int max = width_tmp>height_tmp?width_tmp:height_tmp;
                int min = width_tmp>height_tmp?height_tmp:width_tmp;
                int value = (int)((float)max*10/min);
                if(value>15){
                    quality = 80;
                }else{
                    quality = 90;
                }
            } catch (FileNotFoundException e) {
                System.gc();
            } catch(OutOfMemoryError e){
                System.gc();
                e.printStackTrace();
            }
            // 2:rotate bitmap
            if(f.exists()){
//                f.delete();
            }

            if(rotate>0){
                Matrix mtx = new Matrix();
                mtx.postRotate(rotate);
                try{
                    Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), mtx, true);
                    if(roateBitmap!=null && roateBitmap!=bitmap){
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                        bitmap = roateBitmap;
                    }
                }catch(OutOfMemoryError e){
                    System.gc();
                    e.printStackTrace();
                }
            }




            /*
            压缩后的bitmap  保存到指定的路径
            // 3:save bitmap
            FileOutputStream fileOutputStream = new FileOutputStream(f.getPath());
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            bitmap.recycle();
            os.flush();
            os.close();
            */

            System.gc();
            return bitmap;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }




    /**
     * bitmap 压缩,旋转,保存

     * @return
     */
    public Bitmap bitmapCompressionRotate(String path){

        Uri imageContentUri = getImageContentUri(TrustAppUtils.getContext(), path);

        Bitmap bitmapFromUri = getBitmapFromUri(TrustAppUtils.getContext(), imageContentUri);
        TrustLogUtils.d("找不到文件使用url查找:");
        try{

            InputStream inputStream= new FileInputStream(new File(path));
            return bitmapFromUri;
//            return getBitmap(path);
        }catch(FileNotFoundException e){
            TrustLogUtils.d("找不到文件使用url查找:");
            e.printStackTrace();
                return getBitmap();
//            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public void getTestBitMap(Uri path){
//        Uri imageContentUri = getImageContentUri(TrustAppUtils.getContext(), path);

        Bitmap bitmapFromUri = getBitmapFromUri(TrustAppUtils.getContext(), path);
        TrustLogUtils.d("Trust");
    }

    @Nullable
    private Bitmap getBitmap(String path) {
        int quality = 80;
        Bitmap bitmap = null;
        //
        File f = new File(path);
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
        }catch (FileNotFoundException e){
            TrustLogUtils.e("没有找到文件");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        // 1:compress bitmap
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                int VARIETY_SIZE = (rotate==90 || rotate==270)?height_tmp:width_tmp;
                if (VARIETY_SIZE / 2 <= 600){
                    if(VARIETY_SIZE>600 && VARIETY_SIZE-600>300){
                    }else{
                        break;
                    }
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap =  BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            int max = width_tmp>height_tmp?width_tmp:height_tmp;
            int min = width_tmp>height_tmp?height_tmp:width_tmp;
            int value = (int)((float)max*10/min);
            if(value>15){
                quality = 80;
            }else{
                quality = 90;
            }
        } catch (FileNotFoundException e) {
            TrustLogUtils.e("FileNotFoundException:"+e.toString());
            System.gc();
        } catch(OutOfMemoryError e){
            System.gc();
            e.printStackTrace();
        }
        // 2:rotate bitmap
        if(f.exists()){
//                f.delete();
        }
        if(rotate>0){
            Matrix mtx = new Matrix();
            mtx.postRotate(rotate);
            try{
                Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), mtx, true);
                if(roateBitmap!=null && roateBitmap!=bitmap){
                    bitmap.recycle();
                    bitmap = null;
                    System.gc();
                    bitmap = roateBitmap;
                }
            }catch(OutOfMemoryError e){
                System.gc();
                e.printStackTrace();
            }
        }


            /*
            压缩后的bitmap  保存到指定的路径
            // 3:save bitmap
            FileOutputStream fileOutputStream = new FileOutputStream(f.getPath());
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            bitmap.recycle();
            os.flush();
            os.close();
            */

        System.gc();
        return bitmap;
    }

    public InputStream inputStream;//小米手机拍摄路径不对 获取不到图片是用流来读

    public void setInputStream(InputStream inputStream) {
        try {
            trustTools.baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > -1 ) {
                trustTools.baos.write(buffer, 0, len);
            }
            trustTools.baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(trustTools.baos.toByteArray());
    }

    public ByteArrayOutputStream baos ;//缓存流
    public Bitmap getBitmap(){
        Bitmap bitmap = null;
        int rotate = 0;
        int quality = 80;
        try {

            ExifInterface exifInterface = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                exifInterface = new ExifInterface(trustTools.getInputStream());
            }
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
            TrustLogUtils.d("TrustBase","orientation :"+orientation+"|rotate:"+rotate);
        }catch (FileNotFoundException e){
            TrustLogUtils.e("ExifInterface 没有找到文件");
            TrustLogUtils.d("TrustBase","FileNotFoundException"+e.toString());
            System.gc();
        }
        catch (IOException e) {
            e.printStackTrace();
            TrustLogUtils.d("TrustBase","FileNotFoundException"+e.toString());
            System.gc();
        }


        //

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeStream(trustTools.getInputStream(),null,options);

        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        TrustLogUtils.d("TrustBase","width_tmp"+width_tmp+"|options.outWidth:"+options.outWidth);
        int scale = 1;
        while (true) {
            int VARIETY_SIZE = (rotate==90 || rotate==270)?height_tmp:width_tmp;
            if (VARIETY_SIZE / 2 <= 600){
                if(VARIETY_SIZE>600 && VARIETY_SIZE-600>300){
                }else{
                    break;
                }
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // decode with inSampleSize
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = scale;
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap =  BitmapFactory.decodeStream(trustTools.getInputStream(), null, options2);
        TrustLogUtils.d("TrustBase","BitmapFactory:"+bitmap);
        int max = width_tmp>height_tmp?width_tmp:height_tmp;
        int min = width_tmp>height_tmp?height_tmp:width_tmp;
        int value = (int)((float)max*10/min);
        if(value>15){
            quality = 80;
        }else{
            quality = 90;
        }

        if(rotate>0){
            Matrix mtx = new Matrix();
            mtx.postRotate(rotate);
            try{
                Bitmap roateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), mtx, true);
                if(roateBitmap!=null && roateBitmap!=bitmap){
                    bitmap.recycle();
                    bitmap = null;
                    System.gc();
                    bitmap = roateBitmap;
                }
            }catch(OutOfMemoryError e){
                System.gc();
                e.printStackTrace();
            }
        }

        TrustLogUtils.d("TrustBase","BitmapFactory:"+bitmap);
        System.gc();
        return bitmap;
    }



    //--------------------------调用色相头------------------------------------------------------------------
    public static final int TAKE_PHOtO = 1;
    public Uri imageUri;
    public  String openCamera(Activity activity, int requestCode){

        //创建File 对象，用于存储拍照后的图片
        File outputImage = new File(activity.getExternalCacheDir(), TrustTools.getSystemTimeDataMillisecond()+".jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (configPackName != null) {
                trustTools.imageUri = FileProvider.getUriForFile(activity, configPackName+".fileprovider", outputImage);
            }else{
                throw new RuntimeException("包名不能为空!");
            }
        } else {
            trustTools.imageUri = Uri.fromFile(outputImage);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,trustTools.imageUri);
        activity.startActivityForResult(intent, requestCode);
        return trustTools.imageUri.getPath();
    }





    /**
     * 打开本地相册

     */
    public  void openAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }



    //------------------------------------检查是不是json----------------------------------------------------------------------
    public static boolean checkIsJson(String value){
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    //------------------------------------保存用户密码----------------------------------------------------------------------
    public final String spfTag = "UserMsg";
    public  void setUserMsg(Context context , String editorTag, String key, String value){
        SharedPreferences.Editor editdf = context.getSharedPreferences(editorTag,
                Activity.MODE_PRIVATE).edit();
        editdf.putString(key,value);
        editdf.apply();
    }

    public String getUserMsg(Context context , String editorTag, String key){
        SharedPreferences editor = context.getSharedPreferences(editorTag,
                Context.MODE_PRIVATE);
        return editor.getString(key, null);
    }




    //------------------------------------保留小数点几位----------------------------------------------------------------------

    public String conversionType(float num){
        DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足1位,会以0补足.
        return decimalFormat.format(num);
    }



    public boolean checkVerificationCode(String verificationCode){
        if (TextUtils.isEmpty(verificationCode) || verificationCode.length() != 6) {
            return false;
        }
        return true;
    }


    public void changeSubmintBtn(View v,boolean isClickable,int background){
        v.setClickable(isClickable);
        v.setBackgroundResource(background);
    }


    //---------获取设备mac地址------------------------------------------------------------------------------------
    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            strMac = getLocalMacAddressFromWifiInfo(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            strMac = getMacAddress(context);
            return strMac;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (!TextUtils.isEmpty(getMacAddress(context))) {
                strMac = getMacAddress(context);
                return strMac;
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                strMac = getMachineHardwareAddress();
                return strMac;
            } else {
                strMac = getLocalMacAddressFromBusybox();
                return strMac;
            }
        }

        return "02:00:00:00:00:00";
    }



    /**
     * 根据wifi信息获取本地mac
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") WifiInfo winfo = wifi.getConnectionInfo();
        String mac =  winfo.getMacAddress();
        return mac;
    }

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String macAddress0 = getMacAddress0(context);
            if (!TextUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat/sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());
            }

        }
        return macSerial;
    }

    @SuppressLint("MissingPermission")
    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress0:" + e.toString());
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {

            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
        }
        return strMacAddr;
    }
    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface
                    .getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }



    /**
     * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
     *
     */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    //-------获取资源文件中的字符串---------------------------------------------------------
    public static String getString(Context context ,int stringId){
        Context applicationContext = context.getApplicationContext();
        return applicationContext.getString(stringId);
    }


    /**
     * 延时
     * @param seconds
     * @param delayListener
     */
    public static void delay(int seconds, final DelayListener delayListener){
        final Disposable[] disposable = {null};
        Observable.just("Amit")
                //延时两秒，第一个参数是数值，第二个参数是事件单位
                .delay(seconds, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(String s) {
                        delayListener.callBack(disposable[0]);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public interface DelayListener{
         void callBack(Disposable d);
    }


    public static Uri getImageContentUri(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    // 通过uri加载图片
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
