# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Trust\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript trustinterface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-ignorewarnings

#-----指定代码的压缩级别
-optimizationpasses 5
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify


##---------------Begin: proguard configuration for Gson ----------
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keepattributes Signature
-keepattributes *Annotation*



##---------------End: proguard configuration for Gson ----------

#--------library
#-keep class cn.iwgang.countdownview {*;}

#--------zxing
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}

##-------okio
#-dontwarn okio.**
#-keep class okio.**{*;}
#-keep interface okio.**{*;}

##-------不混淆cordova
#-keep class com.phonegap.ebike.MainActivity{*;}
#-keep class org.apache.cordova.** {*;}
#-keep public class org.apache.cordova.** {public private protected *;}

#--------GaoDe
#3D 地图 V5.0.0之前：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.amap.mapcore.*{*;}
    -keep   class com.amap.api.trace.**{*;}

#    3D 地图 V5.0.0之后：
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.**{*;}
    -keep   class com.amap.api.trace.**{*;}

#    定位
    -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}

#    搜索
    -keep   class com.amap.api.services.**{*;}

#    2D地图
    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}

#    导航
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}



# 友盟
-keep class com.umeng.** {*;}
#----volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }


# Ping++ 混淆过滤
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

# 支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

# 微信或QQ钱包混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

# 银联支付混淆过滤
-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

# 招行一网通混淆过滤
-keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
    public <init>(android.app.Activity);
    public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
    public void callKeyBoardActivity();
}

# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#-----rxbinding2.0
-keep class com.jakewharton.rxbinding2.view.**{*;}


#-----不混淆基本配置
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.widget.BaseAdapter {*;}


#如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
-dontwarn android.support.**

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature



#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-------
-keep class com.sharkgulf.soloera.module.bean.** {*;}
-keep class com.sharkgulf.soloera.db.bean.** {*;}

#路由
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}