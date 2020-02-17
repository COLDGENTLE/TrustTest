package com.trust.retrofit.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Trust on 2018/6/28.
 */

public class TrustRetrofitUtils {
    private final HashMap<String,Object> PARAMS;
    private final String URL;
    private final RequestBody BODY;

    private final File File;
    public static final int POST_RAW = 0,PUT_RAW = 1,DELETE_RAW = 2,UPLOAD = 7,DOWNLOAD = 8;
    public static final int TRUST_RETROFIT = 0,TRUST_RETROFIT_SSL = 1,TRUST_RETROFIT_SSL_TRUST_ALL = 2;
    private int retrofitType = 0;
    private TrustRetrofitCreator mTrustRetrofitCreator = new TrustRetrofitCreator();
    public TrustRetrofitUtils(HashMap<String, Object> params,
                              String url,
                              RequestBody body,
                              java.io.File file,
                              int retrofitType) {
        PARAMS = params;
        URL = url;
        BODY = body;
        File = file;
        this.retrofitType = retrofitType;
    }

    public static RetrofitBuilder create(){
        return new RetrofitBuilder();
    }


    private<T> Observable<T> request(HttpMethod method){
        TrustService trustService;
        switch (retrofitType) {
            case TRUST_RETROFIT:
                trustService = mTrustRetrofitCreator.getTrustService();
                break;
            case TRUST_RETROFIT_SSL:
                trustService = mTrustRetrofitCreator.getTrustServiceSSL();
                break;
            case TRUST_RETROFIT_SSL_TRUST_ALL:
                trustService = mTrustRetrofitCreator.getTrustServiceSSLTrustAll();
                break;
                default:
                    trustService = mTrustRetrofitCreator.getTrustService();
                    break;
        }
        Observable<T> observable = null;

        switch (method){
            case GET:
                observable = (Observable<T>) trustService.get(URL,PARAMS);
                break;
            case POST:
                observable = (Observable<T>) trustService.post(URL,PARAMS);
                break;
            case PUT:
                observable = (Observable<T>) trustService.put(URL,PARAMS);
                break;
            case DELETE:
                observable = (Observable<T>) trustService.delete(URL,PARAMS);
                break;
            case UPLOAD:
                Set<Map.Entry<String, Object>> sets = PARAMS.entrySet();
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                for(Map.Entry<String, Object> entry : sets) {
                    builder.addFormDataPart(entry.getKey(),entry.getValue().toString());

                    if (entry.getValue() instanceof File) {
                        builder.addFormDataPart(entry.getKey(), File.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), File));
                    }
                }

                RequestBody requestBody =builder.build();
                observable = (Observable<T>) trustService.upload(URL,requestBody);
                break;
            case POST_RAW:
                observable = (Observable<T>) trustService.postRaw(URL,BODY);
                break;
            case PUT_RAW:
                observable = (Observable<T>) trustService.putRaw(URL,BODY);
                break;
            case DELETE_RAW:
                observable = (Observable<T>) trustService.deleteRaw(URL,BODY);
                    break;
            case DOWNLOAD:
                break;
            default:
                    break;
        }
        return observable;
    }


    private<T> Observable<T> request(long range){
        TrustService trustService;
        switch (retrofitType) {
            case TRUST_RETROFIT:
                trustService = mTrustRetrofitCreator.getTrustService();
                break;
            case TRUST_RETROFIT_SSL:
                trustService = mTrustRetrofitCreator.getTrustServiceSSL();
                break;
            case TRUST_RETROFIT_SSL_TRUST_ALL:
                trustService = mTrustRetrofitCreator.getTrustServiceSSLTrustAll();
                break;
            default:
                trustService = mTrustRetrofitCreator.getTrustService();
                break;
        }
        Observable<T> observable = null;
        observable = (Observable<T>) trustService.download("bytes=" + Long.toString(range),URL);
        return observable;
    }



    //各种请求
    public final Observable<String> get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String> post() {
        return request(HttpMethod.POST);
    }

    public final Observable<String> put() {
        return request(HttpMethod.PUT);
    }

    public final Observable<String> delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String> putRaw() {
        return request(HttpMethod.PUT_RAW);
    }

    public final Observable<String> postRaw() {
        return request(HttpMethod.POST_RAW);
    }

    public final Observable<String> deleteRaw() {
        return request(HttpMethod.DELETE_RAW);
    }

    public final Observable<String> upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody> download(long range) {
        return request(range);
    }

}
