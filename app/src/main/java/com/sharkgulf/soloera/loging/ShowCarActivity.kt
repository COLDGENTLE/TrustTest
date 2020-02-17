package com.sharkgulf.soloera.loging

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sharkgulf.soloera.R
import com.trust.demo.basis.base.TrustMVPActivtiy
import com.trust.demo.basis.base.presenter.TrustPresenters
import com.trust.demo.basis.base.veiw.TrustView
import kotlinx.android.synthetic.main.activity_show_car.*
import android.os.Build
import androidx.annotation.RequiresApi


class ShowCarActivity : TrustMVPActivtiy<TrustView,TrustPresenters<TrustView>>(),TrustView {
    override fun getLayoutId(): Int {
        return R.layout.activity_show_car
    }

    override fun initView(savedInstanceState: Bundle?) {
        show_car_lottie_view.useHardwareAcceleration(true)
        show_car_lottie_view.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })



        show_car_lottie_view.playAnimation()
        baseSetOnClick(show_car_black_btn)
    }

    override fun initData() {
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun baseResultOnClick(v: View) {
        finishAfterTransition()
    }

    override fun showWaitDialog(msg: String?, isShow: Boolean, tag: String?) {
    }

    override fun diassDialog() {
    }

    override fun showToast(msg: String?) {
    }

    override fun onTrustViewActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    }

    override fun resultSuccess(msg: String) {
    }

    override fun resultError(msg: String) {
    }

    override fun createPresenter(): TrustPresenters<TrustView> {
        return object :TrustPresenters<TrustView>(){}
    }
}
