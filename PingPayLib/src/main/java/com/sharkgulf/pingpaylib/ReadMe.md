使用
app  AndroidManifest
   <!-- 微信支付 -->
        <!-- 1.需要将以下"替换成自己 APK 的包名"换成在微信平台上注册填写的包名 -->
        <!-- 2.WxPayEntryActivity 这个类在 SDK 内部实现，开发者不需要额外实现该类 -->
        <activity-alias
            android:name="项目包名.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.pingplusplus.android.PaymentActivity" />



api 调用

payData 从服务器请求回来的支付data

PingPay(activity,payData)

支付回调

 addPayCallBack(object :PayCallBack{
            override fun callBack(result: String?, errorMsg: String?, extraMsg: String?) {
                Log.d("lhh","支付回调  result $result  errorMsg $errorMsg  extraMsg $extraMsg")
            }
        })







