package com.sharkgulf.soloera.tool.config

import android.net.Uri
import android.view.View
import android.widget.MediaController
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.view.BsVideoView

class BsVideoViewManger {
    companion object{
        private var mBsVideoViewManger:BsVideoViewManger? = null
        fun getInstance():BsVideoViewManger{
            if (mBsVideoViewManger == null) {
                mBsVideoViewManger = BsVideoViewManger()
            }
            return mBsVideoViewManger!!
        }
    }
    private var mBsVideoView:BsVideoView? = null

    fun initViewAndStart(view: BsVideoView){
        mBsVideoView=  view
        val context = mBsVideoView!!.context
        val uri = Uri.parse("android.resource://" + context.packageName + "/raw/" + R.raw.demo);
        mBsVideoView?.setVideoURI(uri)

        //创建MediaController对象
        val mediaController =  MediaController(context);
        mediaController.visibility = View.INVISIBLE
        //VideoView与MediaController建立关联
        mBsVideoView?.setMediaController(mediaController);
        //让VideoView获取焦点
//        no_car_demo_video.requestFocus();

        mBsVideoView?.setOnCompletionListener{mPlayer->
            mBsVideoView?.start()
            mPlayer.isLooping = true
        }
        mBsVideoView?.start()

    }


    fun pause(){
        mBsVideoView?.pause()
    }

    fun resume(){
        mBsVideoView?.start()
    }

    fun desry(){
        mBsVideoView?.suspend()
        mBsVideoView = null
    }

}