package com.sharkgulf.soloera.server

import com.google.gson.Gson
import com.sharkgulf.soloera.TrustAppConfig.*
import com.sharkgulf.soloera.dataanalysis.DataAnalusis
import com.sharkgulf.soloera.dataanalysis.DataAnalysisCenter
import com.sharkgulf.soloera.module.bean.socketbean.SocketBean
import com.sharkgulf.soloera.tool.config.*
import com.trust.demo.basis.trust.TrustTools
import com.trust.demo.basis.trust.utils.TrustHttpUtils
import com.trust.demo.basis.trust.utils.TrustLogUtils
import com.trust.retrofit.config.ProjectInit
import okhttp3.*
import okio.ByteString
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import okhttp3.OkHttpClient



/*
 *Created by Trust on 2019/1/21
 * okhttp3  WebSocket
 */
class TrustWebSocket : WebSocketListener() {
    private val TAG = "TrustWebSocket"
    private var mWebSocket:WebSocket? = null
    private var mTrustWebSocket:TrustWebSocket? = null
    private var mSocketStatus:Boolean = false //默认未连接
    private var mTrustWebSocketListener:TrustWebSocketListener? = null
    private val mGson = Gson()
    private var mHost = SOCEK_HOST
    private var mToken = ""

    @Volatile
    private var mIsReConnection = false
    private val mTrustHttp = TrustHttpUtils.getSingleton(ProjectInit.getApplicationContext())

    init {
        mTrustWebSocket = this
        DataAnalysisCenter.getInstance().registerCallBack(WEB_SOKECT_SACK+WEB_SOKECT_SEND+WEB_SOKECT_HEART,object :DataAnalysisCenter.onDataAnalysisCallBack{
            override fun onErrorCallBack(msg: String,timeOutTopic:String?) {}

            override fun onNoticeCallBack(msg: String?) {
                autoTime()
            }
        },"TrustWebSocket")
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        mWebSocket = webSocket
        mSocketStatus = true
        TrustLogUtils.d(TAG,"onOpen")
        showDebugToast("web scoket  success")
       /* webSocket.send("hello world")
        webSocket.send("welcome")
        webSocket.send(ByteString.decodeHex("this is test"))
        webSocket.close(1000, "再见")*/
        sendLocalData(WEB_CONNECT_SUCCESS)
        sendInitData()
        DataAnalysisCenter.getInstance().sendHeartData()
        sendBikeInfo()
    }


    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        TrustLogUtils.d(TAG,"onMessage ByteString :$bytes")

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        mTrustWebSocketListener?.webSocketOnMessage(text)
        TrustLogUtils.d(TAG,"onMessage String : $text")
        DataAnalusis.getInstanse().updateData(text)
    }


    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        mSocketStatus = false
        webSocket.close(1001, "onClosing ")
        sendLocalData(WEB_CONNECT_ERROR)
        TrustLogUtils.d(TAG,"onClosing  code: $code  reason : $reason")

    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        mSocketStatus = false
        sendLocalData(WEB_CONNECT_ERROR)
        TrustLogUtils.d(TAG,"onClosed  code: $code  reason : $reason")
        reConnection()
        showDebugToast("web scoket  close")
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        mSocketStatus = false
        mTrustWebSocketListener?.webSocketError()
        sendLocalData(WEB_CONNECT_ERROR)
        reConnection()
        TrustLogUtils.d("onFailure  error: ${t.message}")
        showDebugToast("web scoket  onFailure ${t.message}")
    }


    fun connect(token:String?){
        if( token != null && !token.equals("") && !mSocketStatus){
        val url = "$mHost$token&did=${getDeviceId()}"
//        val request = Request.Builder().url("wss://ws.d.bluesharkmotor.com/ws/conn?token=test211b780f306cbb79c31965e8ef72bf02").build()
//        val request = Request.Builder().url(url).header("SG_AGENT", getAgent()).build()
//        TrustLogUtils.d(TAG,"url: $url  SG_AGENT :${getAgent()}")
//
//            OkHttpClient.Builder()
//        val okHttpClient = OkHttpClient.Builder()
////            okHttpClient.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), TrustAllCerts())
////                    .hostnameVerifier(TrustAllCerts.TrustAllHostnameVerifier())
//        val client = okHttpClient.build()
//        client.newWebSocket(request,mTrustWebSocket!!)
//        client.dispatcher().executorService().shutdown()

            val mOkHttpClient = OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                    .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                    .build()

            val request = Request.Builder().url(url).header("SG_AGENT", getAgent()).build()

            mOkHttpClient.newWebSocket(request, mTrustWebSocket!!)
            mOkHttpClient.dispatcher().executorService().shutdown()
         mIsReConnection = true
        }
    }


    fun sendMessage(msg:String){
        if (mSocketStatus) {
            mWebSocket?.send(msg)
        }
        TrustLogUtils.d(TAG,"sendMessage:$msg")
      /*  mWebSocket?.send("wtf!")
        mWebSocket?.send(ByteString.decodeHex("adef"))*/
    }

    fun disConnect(){
        TrustLogUtils.d(TAG,"websocket is dissconnect ")
        mIsReConnection = false
        token = null
        mWebSocket?.close(1000,"seeyou")
    }

    fun setWebSocketListener(trustWebSocketListener:TrustWebSocketListener){
        mTrustWebSocketListener = trustWebSocketListener
    }

    private fun reConnection(){
        if (mIsReConnection) {
             TrustTools.delay(10) { d->
          if (mTrustHttp.isNetworkAvailable()) {
              if (!mSocketStatus) {
                  connect(token)
              }
          }else{
              reConnection()
          }
      }

        }
    }

    interface TrustWebSocketListener{
        fun webSocketOnMessage(msg:String)
        //socket 异常断开连接
        fun webSocketError()
    }


    private fun sendInitData(){
            thread {
                Thread.sleep( 4000)
                sendBikeInfo()
                sendBattryInfo()
                sendBikeLocation()
                sendUpdateCarInfo()
            }
    }


     fun sendLocalData(path:String){
        val socketBean = SocketBean<Any>()
        socketBean.path = path
        socketBean.header = SocketBean.HeaderBean()
        socketBean.header.ts = (System.currentTimeMillis() / 1000).toInt()
        socketBean.header?.uuid =UUID.randomUUID().toString()
        socketBean.header?.ack = 0
        val data = mGson.toJson(socketBean)
         TrustLogUtils.d(TAG,"sendLocalData  data $data")
        DataAnalusis.getInstanse().updateData(data)
    }


    private fun autoTime(){
            thread {
                Thread.sleep( 3*1000)
                if (mSocketStatus) {
                DataAnalysisCenter.getInstance().sendHeartData()
            }
        }
    }

    fun getWebSocketStatus():Boolean{
        return  mSocketStatus
    }
}