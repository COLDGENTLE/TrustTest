package com.sharkgulf.soloera.tool

import android.app.Activity
import android.content.Context
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import android.content.pm.PackageManager
import android.content.pm.PackageInfo



/**
 *  Created by user on 2019/2/20
 */
class ReadWriteUtils(context: Context) {
    private val mContext:Context = context

    /**
     *@author chenzheng_Java
     *保存用户输入的内容到文件
     */
    fun  save(data:String,fileName:String) {
        try {
            /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的，
 * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的
       *  public abstract FileOutputStream openFileOutput(String name, int mode)
       *  throws FileNotFoundException;
       * openFileOutput(String name, int mode);
       * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名
       *     该文件会被保存在/data/data/应用名称/files/擦汗enzheng_java.txt
       * 第二个参数，代表文件的操作模式
       *     MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖
       *     MODE_APPEND 私有  重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件
       *     MODE_WORLD_READABLE 公用 可读
       *     MODE_WORLD_WRITEABLE 公用 可读写
       * */
            val outputStream = mContext.openFileOutput(fileName,
            Activity.MODE_PRIVATE)
            outputStream.write(data.toByteArray())
            outputStream.flush()
            outputStream.close()
            } catch ( e: FileNotFoundException) {
            e.printStackTrace()
            } catch (e: IOException) {
            e.printStackTrace()
            }
        }
    /**
       * @author chenzheng_java
       * 读取刚才用户保存的内容
       */
   fun read(fileName:String):String? {
        try {
            val inputStream = mContext.openFileInput(fileName)
            val bytes = ByteArray(4)
            val arrayOutputStream =  ByteArrayOutputStream()
            while (inputStream.read(bytes) != -1) {
            arrayOutputStream.write(bytes, 0, bytes.size)
            }
            inputStream.close()
            arrayOutputStream.close()
            return  String(arrayOutputStream.toByteArray())
            } catch ( e:FileNotFoundException) {
            e.printStackTrace()
            } catch ( e:IOException) {
            e.printStackTrace()
            }

        return null
        }


    fun startApp(packname:String,bindStatus:Boolean = false){
        val packageManager = mContext.packageManager
        if (checkPackInfo(packname)) {
            val intent = packageManager.getLaunchIntentForPackage(packname)
            intent.putExtra("bindStatus",bindStatus)
            mContext.startActivity(intent!!)
        } else {
            Toast.makeText(mContext, "没有安装$packname", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private fun checkPackInfo(packname: String): Boolean {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = mContext.packageManager.getPackageInfo(packname, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return packageInfo != null
    }


    companion object {
        private var mReadWriteTool:ReadWriteUtils? = null
        fun getReadWriteTool(context:Context): ReadWriteUtils {
            if (mReadWriteTool == null) {
                mReadWriteTool = ReadWriteUtils(context.applicationContext)
            }
            return mReadWriteTool!!
        }
    }
}