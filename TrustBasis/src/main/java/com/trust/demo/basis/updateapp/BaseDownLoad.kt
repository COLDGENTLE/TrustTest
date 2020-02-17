package com.trust.demo.basis.updateapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import androidx.core.content.FileProvider
import com.trust.demo.basis.trust.utils.TrustAppUtils
import java.io.File

class BaseDownLoad(val context: Context,val apkUrl:String,val listener:IBaseDownLoad) {
    init {
        authoritiesName =  "${context.packageName}.fileprovider"
        mBsDownLoad = this
    }

    private val mHandler = object :Handler(){
        override fun handleMessage(msg: Message?) {
            if (msg != null) {
                when (msg.what) {
                    READ -> {
                    }
                    LOAD -> { listener.loadingDownLoad(msg.arg1,msg.arg2) }
                    END ->  { listener.loadingSuccess(msg.obj as File)}
                    ERROR -> { listener.errorDownLoad(msg.obj as Exception)}
                    else -> {
                    }
                }
            }
        }
    }
    /*
    * 从服务器中下载APK
    */
    fun downLoadApk() {

        object : Thread() {

            override fun run() {

                try {
                    val file = DownLoadManager.getFileFromServer(apkUrl, object :IBaseDownLoad.ApkListener{
                        override fun resultAplInfo(maxLength: Int, progress: Int) {
                            val obtain = Message.obtain()
                            obtain.what = LOAD
                            obtain.arg1 = maxLength
                            obtain.arg2 = progress
                            mHandler.sendMessage(obtain)

                        }
                    }, context)
                    Thread.sleep(3000)
                    val obtain = Message.obtain()
                    obtain.what = END
                    obtain.obj = file
                    mHandler.sendMessage(obtain)

                } catch (e: Exception) {
                    val obtain = Message.obtain()
                    obtain.what = ERROR
                    obtain.obj = e
                    mHandler.sendMessage(obtain)
                }

            }
        }.start()

    }


   companion object{
       //安装apk
       var authoritiesName:String? = null
       val READ = 0
       val LOAD = 1
       val ERROR = 2
       val END = 3
        var mBsDownLoad:BaseDownLoad? = null

       fun getInstance():BaseDownLoad?{
           return mBsDownLoad
       }

       fun installApk(context: Context,file: File) {
           val intent = Intent()
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               val apkUri = FileProvider.getUriForFile(context, authoritiesName!!, file)
               // 由于没有在Activity环境下启动Activity,设置下面的标签
               //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               //添加这一句表示对目标应用临时授权该Uri所代表的文件
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
               intent.action = Intent.ACTION_VIEW
               intent.setDataAndType(apkUri, "application/vnd.android.package-archive")

           } else {
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
               intent.action = android.content.Intent.ACTION_VIEW
               intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")

           }
           context.startActivity(intent)


           //        Intent intent = new Intent();
           //        //执行动作
           //        intent.setAction(Intent.ACTION_VIEW);
           //
           //        if (Build.VERSION.SDK_INT >= 24) {
           //            Uri apkUri =
           //                    FileProvider.getUriForFile(context, authoritiesName, file);
           //            // 由于没有在Activity环境下启动Activity,设置下面的标签
           ////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           //            //添加这一句表示对目标应用临时授权该Uri所代表的文件
           //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           //            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           //            intent.setAction(Intent.ACTION_VIEW);
           //            intent.setDataAndType(apkUri,"application/vnd.android.package-archive");
           //
           //        }else{
           //            //执行的数据类型
           //            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
           //        }
           //        try{
           //            context.startActivity(intent);
           //        }catch (Exception e ){
           //            Log.d("test:",e.toString());
           //        }
       }
   }

}