package com.sharkgulf.soloera.home.user

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.mvpview.user.IUser
import com.sharkgulf.soloera.presenter.user.UserPresenter
import com.dn.tim.lib_permission.annotation.Permission
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.trust.TrustTools
import kotlinx.android.synthetic.main.activity_user_ic.*
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import com.sharkgulf.soloera.appliction.BsApplication
import com.sharkgulf.soloera.RequestConfig
import com.sharkgulf.soloera.db.bean.DbUserLoginStatusBean
import com.sharkgulf.soloera.module.bean.*
import com.sharkgulf.soloera.module.bean.socketbean.CarInfoBean
import com.sharkgulf.soloera.tool.BeanUtils
import com.sharkgulf.soloera.tool.config.finishTransition
import com.trust.demo.basis.trust.utils.TrustAnalysis
import java.io.*
import java.lang.Exception


class UserIcActivity :TrustMVPActivtiy<IUser,UserPresenter>(),IUser
{
    override fun resultBleStatus(isconnection: Boolean) {}

    override fun resultUpdatePhone(bean: BsHttpBean?) {}

    override fun resultPhoneLogin(bean: BsGetSmsBean?) {}

    /**
    * 动作标志：无
    */
    private val NONE = 0
    /**
     * 动作标志：拖动
     */
    private val DRAG = 1
    /**
     * 动作标志：缩放
     */
    private val ZOOM = 2
    /**
     * 初始化动作标志
     */
    private var mode = NONE

    /**
     * 记录起始坐标
     */
    private val start = PointF()
    /**
     * 记录缩放时两指中间点坐标
     */
    private val mid = PointF()
    private var oldDist = 1f


    private val TAG = UserIcActivity::class.java.canonicalName
    override fun resultCarInfo(bean: CarInfoBean?) {}

    override fun resultUpdateCarInfo(bean: BsUpdateCarInfoBean?) {}

    override fun resultCheckinStatus(bean: BsCheckinStatusBean?) {}

    override fun resultPointInFo(bean: BsPointinfoBean?) {}

    override fun resultPointDetail(bean: BsPointDetailBean?) {}

    override fun resultCheckinDaily(bean: BsCheckinDailyBean?) {}

    private var mBean : DbUserLoginStatusBean? = null
    private var mFile:File? = null
    var clipview:ClipView? = null
    companion object {
        fun startActivity(context:Context,bean:DbUserLoginStatusBean){
            val intent = Intent(context,UserIcActivity::class.java)
            intent.putExtra("bean",TrustAnalysis.resultString(bean))
            context.startActivity(intent)
        }

        var mBitmap:Bitmap? = null
    }

    //缩放比例
    private val matrix = Matrix()
    private val savedMatrix = Matrix()
    override fun getLayoutId(): Int { return R.layout.activity_user_ic }

    override fun initView(savedInstanceState: Bundle?) {

        clipview = ClipView(this)

        if (mBitmap != null) {
//            profile_image.setImageBitmap(mBitmap)


//            likeView.setImageBitmap(mBitmap)

            val observer = src_pic.getViewTreeObserver()
            observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                override fun onGlobalLayout() {
                    src_pic.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                    clipview!!.addOnDrawCompleteListener(object :ClipView.OnDrawListenerComplete{
                        override fun onDrawComplete() {
                            clipview!!.removeOnDrawCompleteListener()
                            val radius = clipview!!.radius
                            val clipRadius = if (radius ==0.0f) { 441.33f }else{ radius }

                            val midX = clipview!!.circleCenterPX
                            val midY = clipview!!.circleCenterPY

                            val imageWidth = mBitmap!!.getWidth()
                            val imageHeight = mBitmap!!.getHeight()
                            // 按裁剪框求缩放比例
                            val scale = clipRadius * 3.0f / imageWidth


                            if (radius == 0.0f) {
                                // 起始中心点
                                val imageMidX = imageWidth * scale / 16
                                val imageMidY = imageHeight * scale / 64
                                src_pic.setScaleType(ImageView.ScaleType.MATRIX)

                                // 缩放
                                matrix.postScale(scale, scale)
                                // 平移
                                matrix.postTranslate(midX - imageMidX, midY - imageMidY)
                            }else{
                                // 起始中心点
                                val imageMidX = imageWidth * scale / 2
                                val imageMidY = imageHeight * scale / 2
                                src_pic.setScaleType(ImageView.ScaleType.MATRIX)

                                // 缩放
                                matrix.postScale(scale, scale)
                                // 平移
                                matrix.postTranslate(midX - imageMidX, midY - imageMidY)
                            }

//                           z if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                src_pic.setImageBitmap(mBitmap)
                                src_pic.setImageMatrix(matrix)
//                            }

                        }

                    })

                    matrix.reset()
                    likeView.addView(clipview, ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                }
            })
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                src_pic.setImageBitmap(mBitmap)
//            }
        }


        baseSetOnClick(user_ic_more_btn)
        baseSetOnClick(user_ic_logo_submint_btn)
        baseSetOnClick(user_ic_back_btn)
        src_pic.setOnTouchListener { view, motionEvent ->
            onViewTouched(view,motionEvent)
        }
    }

    override fun initData() {
        mBean = TrustAnalysis.resultTrustBean(intent.getStringExtra("bean"),DbUserLoginStatusBean::class.java)
    }

    override fun baseResultOnClick(v: View) {
        when (v.id) {
            R.id.user_ic_more_btn -> {
                submintLogo()
            }
            R.id.user_ic_logo_submint_btn->{
                submintLogo()
            }
            R.id.user_ic_back_btn->{
                finishTransition()
            }
            else -> {
            }
        }
    }

    @Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun submintLogo() {
        try{
            val bitmap = getBitmap()
            if (bitmap != null) {
                mFile = testAsa(bitmap)
                val map = hashMapOf<String, Any>()
                map["type"] = 1
                map["class"] = 1
                map["files"] = mFile!!
                getPresenter()?.uploadFile(map)
            }else{
                showToast("请确认截图是否正确！")
            }

        }catch (e:Exception){
            showToast("请确认截图是否正确！")
        }
    }


    fun testAsa(bitmap:Bitmap):File{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 10//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            val length = baos.toByteArray().size
        }

        //图片名


        val file = File(Environment.getExternalStorageDirectory(),  "bs_user_logo.png")
        if (!file.exists()) {
            file.createNewFile()
        }
        try {
            val fos = FileOutputStream(file)
            try {
                fos.write(baos.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {

                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {

            e.printStackTrace()
        }

        // recycleBitmap(bitmap);
        return file

    }



    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
        com.sharkgulf.soloera.tool.config.showToast(msg)
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
        showToast(msg)
    }

    override fun resultUploadfile(bean: BsUploadFileBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            mFile?.delete()
            mBean?.userBean?.icon = bean.getData()?.urls!![0]
            getPresenter()?.editUserInfo(RequestConfig.requestEditUserInfo(mBean!!))
        }
    }

    override fun resultDeleteCar(bean: BsDeleteCarBean?) {
    }

    private var imgPath:String = ""
    private var mTrustTools:TrustTools<View> = TrustTools.create("com.sharkgulf.blueshark")


    @Permission(Manifest.permission.READ_EXTERNAL_STORAGE)
    private fun openAlbum(){
        mTrustTools.openAlbum(this@UserIcActivity, 1)
    }
    @Permission(Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
    private fun openCamera(){
        imgPath = mTrustTools.openCamera(this@UserIcActivity, 2)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==1) {
            if (resultCode == RESULT_OK) {
                val bitmap = TrustTools<View>().getImages(data, this)
//                likeView.reset()
//                likeView.bitmap = CropViewUtils.scaleBitmapForWidth(bitmap,1080)
            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_OK) {
                mTrustTools.setInputStream(contentResolver
                        .openInputStream(mTrustTools.imageUri))
                val bitmapCompressionRotate = mTrustTools.bitmapCompressionRotate(imgPath)
//                likeView.reset()
//                likeView.bitmap = CropViewUtils.scaleBitmapForWidth(bitmapCompressionRotate,1080)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun createPresenter(): UserPresenter {
        return UserPresenter()
    }


    override fun resultUserExt(bean: BsUserExtBean?) {

    }

    override fun resultEditUserInfo(bean: BsEditUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            getPresenter()?.userInfo(RequestConfig.requestUserOrCarInfo())
            showToast(bean.getState_info())
        }
    }

    override fun resultEditPwd(bean: BsSetPwdBean?) {

    }

    override fun resultUserKey(bean: BsGetUserKeyBean?) {

    }
    override fun resultCheckUserIsResiger(bean: BsCheckUserRegisterBean?) {}

    override fun resultUserResiger(bean: BsUserRegisterKeyBean?) {}

    override fun resultUserInfo(bean: BsGetUserInfoBean?) {
        if (BeanUtils.checkSuccess(bean?.getState()!!,bean.getState_info()!!,this)) {
            BsApplication.bsDbManger!!.setUserLoginStatus(bean)
            finish()
        }
    }


    /**
     * @return 裁剪后的图片
     * @description 获取裁剪框内截图
     */
    private fun getBitmap(): Bitmap? {

        try {
            // srcPic.getDrawingCache()获取View截图在某些情况下报错了。
            // 现在用一种新的获取view中图像的方法取代getDrawingCache()方法.
            // 另：在使用createBitmap()增加try..catch..以防止不断生成bitmap可能导致的oom
            val startX = (clipview!!.getCircleCenterPX() - clipview!!.getRadius()) .toInt()
            val startY = (clipview!!.getCircleCenterPY() - clipview!!.getRadius()) .toInt()
            Log.i(TAG, "getBitmap()：startX=" + startX
                    + ",startY=" + startY
                    + ",clipview.getClipWidth()=" + clipview!!.getClipWidth()
                    + ",clipview.getWidth()=" + clipview!!.getWidth()
                    + ",clipview.getCircleCenterPX()=" + clipview!!.getCircleCenterPX()
                    + ",clipview.getRadius()=" + clipview!!.getRadius()
                    + ",clipview.getCircleCenterPY()=" + clipview!!.getCircleCenterPY())
            val finalBitmap = Bitmap.createBitmap(
                    loadBitmapFromView(src_pic)!!,
                    startX, startY, clipview!!.getClipWidth(),
                    clipview!!.getClipHeight())
            // 释放资源
            src_pic.destroyDrawingCache()
            Log.i(TAG, "getBitmap()  finalBitmap=$finalBitmap")
            return getCircleBitmap(finalBitmap)
        } catch (err: OutOfMemoryError) {
            Toast.makeText(this, "保存头像失败", Toast.LENGTH_SHORT).show()
            Log.e(TAG, err.message)
            return null
        } catch (e: Exception) {
            Toast.makeText(this, "保存头像失败!", Toast.LENGTH_SHORT).show()
            Log.e(TAG, e.message)
            return null
        }

    }


    /**
     * 根据view的宽高,截图当前view显示的图像
     * @param v     需要截图的view
     * @return  截取view当前显示的图像,返回的bitmap
     */
    fun loadBitmapFromView(v: View?): Bitmap? {
        if (v == null) {
            return null
        }
        try {
            val screenshot = Bitmap.createBitmap(v.width, v.height,
                    Bitmap.Config.ARGB_8888)
            val c = Canvas(screenshot)
            v.draw(c)
            return screenshot
        } catch (err: OutOfMemoryError) {
        }

        return null
    }


    /**
     * @description 获取圆形裁剪框内截图
     * @param bitmap src图片
     * @return
     */
    fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        //在画布上绘制一个圆 -1是为了去掉白色的边框
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                (bitmap.width / 2 - 1).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        Log.i(TAG, "getCircleBitmap()     output=$output")
        return output
    }


    fun onViewTouched(v: View, event: MotionEvent): Boolean {
        val view = v as ImageView
        when (event.action and MotionEvent.ACTION_MASK) {
            //在第一个点被按下时触发
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(src_pic.getImageMatrix())
                // 设置开始点位置
                start.set(event.x, event.y)
                mode = DRAG
            }
            //当屏幕上已经有点被按住，此时再按下其他点时触发。
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                // 最先放下的两个手指距大于10像素时
                if (oldDist > 10f) {
                    savedMatrix.set(src_pic.getImageMatrix())
                    midPoint(mid, event)
                    mode = ZOOM
                }
            }
            //当屏幕上唯一的点被放开时触发
            MotionEvent.ACTION_UP -> {
            }
            // 当触点离开屏幕，但是屏幕上还有触点(手指)时触发
            MotionEvent.ACTION_POINTER_UP -> mode = NONE
            //当有点在屏幕上移动时触发
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {//只有一个触点时,通过两点之间的距离移动
                matrix.set(savedMatrix)
                matrix.postTranslate(event.x - start.x, event.y - start.y)
            } else if (mode == ZOOM) {//当有两个以上触点时
                val newDist = spacing(event)//计算最先两点现在的距离
                //因为ACTION_MOVE很灵敏,会因手指颤抖一直被触发,这里我们添加一个判断,
                //当现在的两点距离比老的两点之间的距离的改变幅度>5个像素就执行缩放,
                //这样可以减少非意愿的微小缩放,同时节省一点内存空间
                val changeDist = if (newDist > oldDist + 5)
                    true
                else
                    if (newDist < oldDist - 5) true else false
                if (changeDist) {
                    matrix.set(savedMatrix)
                    val scale = newDist / oldDist
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }
        view.imageMatrix = matrix//缩放或者拖动
        return true
    }


    /**
     * 多点触控时，计算最先放下的两指距离
     *
     * @param event
     * @return
     */
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    /**
     * 多点触控时，计算最先放下的两指中心坐标
     *
     * @param point
     * @param event
     */
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

}
