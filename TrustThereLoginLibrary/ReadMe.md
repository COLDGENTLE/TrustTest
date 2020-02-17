友盟lib使用

依赖本lib

统计
在baseactivity里面添加
 override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
 }

 override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
 }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
  }


在 主项目app build下
defaultConfig{
 manifestPlaceholders = [qqappid: "你的id"]
}


在主项目报名下
创建 wxapi 包 创建
class WXEntryActivity: WXCallbackActivity (){
}


manifest下
  <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

修改lib manifest下 qq授权页 的id scheme  为指定应用id



初始化um
CHANNEL 是 manifest 中定义  <meta-data>  渠道 key
  UmConfigInit(key,appliction,CHANNEL)

 初始化三方登陆

 threeLogin()


 三方登陆调用
 umThreeLogin(mActivity!!).startSinaLogin(mActivity!!,ThreeListener)
 umThreeLogin(mActivity!!).startQQLogin(mActivity!!,ThreeListener)
 umThreeLogin(mActivity!!).startWxLogin(mActivity!!,ThreeListener)

ThreeListener 中  resultUserinfo(partner: String, data: MutableMap<String, String>)
partner 渠道名
data 第三方相关信息