package com.sharkgulf.soloera.server;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;

import android.util.Log;

import com.sharkgulf.soloera.module.HttpModel;
import com.trust.demo.basis.base.model.RequestResultListener;
import com.trust.demo.basis.trust.utils.TrustLogUtils;

import java.io.File;
import java.util.HashMap;

import static com.trust.demo.basis.base.model.http.TrustRetrofitModel.APP_ROOT_PATH;
import static com.trust.demo.basis.base.model.http.TrustRetrofitModel.DOWNLOAD_DIR;


/**
 * 创建时间：2018/3/7
 * 编写人：damon
 * 功能描述 ：
 */

public class DownloadIntentService extends IntentService {

    private static final String TAG = "DownloadIntentService";
    private NotificationManager mNotifyManager;
    private String mDownloadFileName;
    private Notification mNotification;

    public DownloadIntentService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String downloadUrl = intent.getExtras().getString("download_url");
        final int downloadId = intent.getExtras().getInt("download_id");
        mDownloadFileName = intent.getExtras().getString("download_file");

        Log.d(TAG, "download_url --" + downloadUrl);
        Log.d(TAG, "download_file --" + mDownloadFileName);

        final File file = new File(APP_ROOT_PATH + DOWNLOAD_DIR + mDownloadFileName);
        long range = 0;
        int progress = 0;
        if (file.exists()) {
            range = 0;
            progress = (int) (range * 100 / file.length());
            if (range == file.length()) {
                installApp(file);
                return;
            }
        }

        Log.d(TAG, "range = " + range);


        new HttpModel().requestDownLoad("http://dldir1.qq.com/qqmi/aphone_p2p/TencentVideo_V6.0.0.14297_848.apk",
                new HashMap<String, Object>(),0,new RequestResultListener.DownLoadListener (){
                    @Override
                    public void resultAppDownDownLoadSuccess() {
                        TrustLogUtils.d("TAG","resultAppDownDownLoadSuccess");
                    }

                    @Override
                    public void resultAppDownLoading(Long rang, int progress) {
                        TrustLogUtils.d("TAG","resultAppDownLoading"+progress);
                    }

                    @Override
                    public void resultAppDownLoadError(Throwable t) {
                        TrustLogUtils.d("TAG","resultAppDownLoadError"+t.getMessage());
                    }

                    @Override
                    public void netWorkError(String msg) {
                        TrustLogUtils.d("TAG","netWorkError"+msg);
                    }
                });
    }

    private void installApp(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
