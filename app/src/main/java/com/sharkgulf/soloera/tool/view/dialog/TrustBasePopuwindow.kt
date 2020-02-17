package com.sharkgulf.soloera.tool.view.dialog

import android.animation.Animator
import android.content.Context
import android.view.View
import razerdp.basepopup.BasePopupWindow
import android.animation.ObjectAnimator


/**
 *  Created by user on 2019/2/21
 */
class TrustBasePopuwindow(context: Context) : BasePopupWindow(context) {

    companion object {
        var layoutId:Int = 0
        var mTrustPopuwindowOnclickListener:TrustPopuwindowOnclickListener? = null
        fun getPopuwindow(context: Context, layout:Int,trustPopuwindowOnclickListener:TrustPopuwindowOnclickListener):BasePopupWindow{
            layoutId = layout
            mTrustPopuwindowOnclickListener = trustPopuwindowOnclickListener
            return TrustBasePopuwindow(context)
        }
    }

    override fun onCreateContentView(): View {
        var createPopupById:View? = createPopupById(layoutId)
        createPopupById = mTrustPopuwindowOnclickListener?.resultPopuwindowViewListener(createPopupById)
        return createPopupById!!
    }




    interface TrustPopuwindowOnclickListener{
        fun resultPopuwindowViewListener(v:View?):View
    }


     fun createAnimator(isShow: Boolean): Animator {
        val showAnimator = ObjectAnimator.ofFloat<View>(displayAnimateView,
                View.TRANSLATION_Y,
                if (isShow) height * 1f else 0f,
                if (isShow) 0f else height *1f)
        showAnimator.duration = 400
        return showAnimator

    }

}