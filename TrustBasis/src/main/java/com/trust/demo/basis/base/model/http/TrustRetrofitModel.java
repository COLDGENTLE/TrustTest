package com.trust.demo.basis.base.model.http;

import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.Log;

import com.trust.demo.basis.R;
import com.trust.demo.basis.base.TrustApplication;
import com.trust.demo.basis.base.model.RequestResultListener;
import com.trust.demo.basis.base.model.TrustHttpRequestModel;
import com.trust.demo.basis.trust.TrustTools;
import com.trust.demo.basis.trust.utils.TrustAppUtils;
import com.trust.demo.basis.trust.utils.TrustHttpUtils;
import com.trust.demo.basis.trust.utils.TrustLogUtils;
import com.trust.retrofit.config.ProjectInit;
import com.trust.retrofit.net.RetrofitBuilder;
import com.trust.retrofit.net.TrustRetrofitUtils;


import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * Created by Trust on 2018/6/29.
 *Retrofit 网络请求 model
 *
 * 必须跟TrustRetrofit 一起使用 否则就屏蔽这个类
 */

public class TrustRetrofitModel extends TrustHttpRequestModel {
    public final static String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + TrustAppUtils.getContext().getPackageName();
    public final static String DOWNLOAD_DIR = "/downlaod/";
    public static final int POST_RAW = TrustRetrofitUtils.POST_RAW;
    public static final int PUT_RAW = TrustRetrofitUtils.PUT_RAW;
    public static final int DELETE_RAW = TrustRetrofitUtils.DELETE_RAW;
    @Override
    public <T> void requestGet(String url, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable = getBuilder(url,null, params).get();
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void requestPost(String url, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable = getBuilder(url,null, params).post();
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void requestPut(String url, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable = getBuilder(url,null, params).put();
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void requestDelete(String url, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable = getBuilder(url,null, params).delete();
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void requestJsonParams(String url, int paramsType, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        checkRequestType(url, null, paramsType, params, requestResultListener, clasz);
    }

    @Override
    public <T> void request(String url, String header, int paramsType, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        checkRequestType(url, header, paramsType, params, requestResultListener, clasz);
    }

    private <T> void checkRequestType(String url, String header, int paramsType, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable ;

        switch (paramsType) {
            case TrustRetrofitUtils.POST_RAW:
                observable = getBuild(url, header,params,type).postRaw();
                break;
            case TrustRetrofitUtils.PUT_RAW:
                observable = getBuild(url, header,params,type).putRaw();
                break;
            case TrustRetrofitUtils.DELETE_RAW:
                observable = getBuild(url, header,params,type).deleteRaw();
                break;
            case TrustRetrofitUtils.UPLOAD:
                observable = getBuild(url, header,params,type).upload();
                break;
            default:
                observable = getBuild(url, header,params,type).postRaw();
                break;
        }
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void upload(String url, String filePath, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        Observable<String> observable = getBuild(url, filePath,type).upload();
        request(observable,requestResultListener,clasz);
    }

    @Override
    public <T> void requestUpload(String url,int paramsType, HashMap<String, Object> params, RequestResultListener<T> requestResultListener, Class<T> clasz) {
        checkRequestType(url, "Content-Type", paramsType, params, requestResultListener, clasz);
    }

    @Override
    public <T> void download(String url, long range,String header, HashMap<String, Object> params, RequestResultListener.DownLoadListener downLoadListener) {
        Observable<ResponseBody> observable = getBuild(url, header,params,type).download(range);
        requestDownLoad(url,range,observable,downLoadListener);
    }

    private <T> void request(Observable<String> observable, final RequestResultListener<T> requestResultListener, final Class<T> clasz) {
        TrustHttpUtils singleton = TrustHttpUtils.Companion.getSingleton(ProjectInit.getApplicationContext());
        if (singleton.isNetworkAvailable()) {
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            try {
                                T t = null;
                                t = getTrustAnalysis(s,clasz);
                                requestResultListener.resultSuccess(t);
                            }catch (Throwable e){
                                TrustLogUtils.e("e :"+e.toString());
                                requestResultListener.resultError(e);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            requestResultListener.resultError(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else{
            requestResultListener.netWorkError(TrustTools.getString(TrustApplication.Companion.getContexts(), R.string.HttpTimeOut));
        }

    }


    private  void requestDownLoad(final String url, final long range, Observable<ResponseBody> observable, final RequestResultListener.DownLoadListener downLoadListener) {
        TrustHttpUtils singleton = TrustHttpUtils.Companion.getSingleton(ProjectInit.getApplicationContext());
        if (singleton.isNetworkAvailable()) {
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {


                        @Override
                        public void onSubscribe(Disposable d) { }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            RandomAccessFile randomAccessFile = null;
                            InputStream inputStream = null;
                            long total = range;
                            long responseLength = 0;
                            try {
                                byte[] buf = new byte[2048];
                                int len = 0;
                                responseLength = responseBody.contentLength();
                                inputStream = responseBody.byteStream();
                                String filePath = APP_ROOT_PATH + DOWNLOAD_DIR;
                                File file = new File(filePath, url.substring(url.lastIndexOf('/') + 1));
                                File dir = new File(filePath);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                randomAccessFile = new RandomAccessFile(file, "rwd");
                                if (range == 0) {
                                    randomAccessFile.setLength(responseLength);
                                }
                                randomAccessFile.seek(range);

                                int progress = 0;
                                int lastProgress = 0;

                                while ((len = inputStream.read(buf)) != -1) {
                                    randomAccessFile.write(buf, 0, len);
                                    total += len;
                                    lastProgress = progress;
                                    progress = (int) (total * 100 / randomAccessFile.length());
                                    if (progress > 0 && progress != lastProgress) {
                                        downLoadListener.resultAppDownLoading(total,progress);
                                    }
                                }
                                downLoadListener.resultAppDownDownLoadSuccess();
                            } catch (Exception e) {
                                downLoadListener.resultAppDownLoadError(e);
                                e.printStackTrace();
                            } finally {
                                try {
                                    TrustLogUtils.d("lhh","download total:"+total);
                                    if (randomAccessFile != null) {
                                        randomAccessFile.close();
                                    }

                                    if (inputStream != null) {
                                        inputStream.close();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onError(Throwable e) { downLoadListener.resultAppDownLoadError(e); }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else{
            downLoadListener.netWorkError(TrustTools.getString(TrustApplication.Companion.getContexts(), R.string.HttpTimeOut));
        }

    }

    /**
     * https表单请求
     * @param url 地址
     * @param params  参数
     *
     */
    @NonNull
    private TrustRetrofitUtils getBuild(String url, HashMap<String, Object> params,int type) {
        return TrustRetrofitUtils
                .create()
                .url(url)
                .params(params == null?new HashMap<String, Object>():params)
                .build(type);
    }



    /**
     * 可选 http 或者https 指定请求头  请求
     * @param url 地址
     * @param headler  请求头
     * @param params  参数
     * @param type  请求类型
     *
     */
    @NonNull
    private TrustRetrofitUtils getBuild(String url, String headler, HashMap<String, Object> params , int type) {
        RetrofitBuilder builder = TrustRetrofitUtils
                .create()
                .url(url);
        if (headler != null) {
            builder.body(headler,params==null?"":"multipart/form-data");
            builder.file((File) params.get("files"));
            builder.params(params);
        }else{
            builder.body(params==null?"": new JSONObject(params).toString());
        }
        Log.d("TrustRetrofitUtils","test:"+params==null?"": new JSONObject(params).toString());
        return builder.build(type);
    }


    /**
     * https  上传请求
     * @param url 地址
     * @param filePath 文件路径
     *
     */
    @NonNull
    private TrustRetrofitUtils getBuild(String url, String filePath,int type) {
        return TrustRetrofitUtils
                .create()
                .url(url)
                .file(filePath)
                .build(type);
    }



    private TrustRetrofitUtils getBuilder(String url ,  String headler, HashMap<String, Object> params){
        return  headler == null? getBuild(url,params,type):getBuild(url,headler,params,type);
    }

}
