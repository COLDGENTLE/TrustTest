package com.sharkgulf.soloera.cards.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.sharkgulf.soloera.R
import com.sharkgulf.soloera.tool.SizeLabel
import com.sharkgulf.soloera.tool.config.setHtmlSpanneds
import com.trust.demo.basis.trust.utils.TrustLogUtils
import kotlinx.android.synthetic.main.popupwindow_first_privacy_policy.*

class AlertPopupwindowPrivacyPolicyActivity : Activity() {
    private val TAG = "AlertPopupwindowPrivacyPolicyActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popupwindow_first_privacy_policy)
//        popupwindow_privacy_policy_tx.text = setHtmlSpanneds(R.string.popupwindow_first_privacy_policy_tx,
//                color = R.color.textBlue,callBack = SizeLabel.HtmlOnClickListener {
//           TrustLogUtils.d(TAG,"点击了隐私 ")
//            showToast("点击了隐私")
//        })
        popupwindow_privacy_policy_tx.text = setHtmlSpanneds(R.string.popupwindow_first_privacy_policy_tx,"",R.color.textBlue,SizeLabel.HtmlOnClickListener { action ->

            TrustLogUtils.d(TAG,"点击了隐私 $action")
        })

        popupwindow_privacy_policy_tx.movementMethod = LinkMovementMethod.getInstance()
        popupwindow_privacy_policy_tx.highlightColor = Color.parseColor("#00000000")
    }
}
