package com.trust.demo.basis.trust.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 *  Created by user on 2019/11/25
 */
class EditTextInputFilter(mAX_EN: Int): InputFilter {
    var MAX_EN: Int = mAX_EN// 最大英文/数字长度 一个汉字算两个字母
    var regEx = "[\\u4e00-\\u9fa5]" // unicode编码，判断是否为汉字

   

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        val destCount = dest.toString().length + getChineseCount(dest.toString())
        val sourceCount = source.toString().length + getChineseCount(source.toString())
        var name = ""
        var count = 0
        var i = 0
        if (destCount + sourceCount > MAX_EN) {
            if (destCount < MAX_EN) {
                while (destCount + count < MAX_EN) {
                    ++i
                    name = source.subSequence(0, i).toString()
                    count = name.length + getChineseCount(name)
                    if (destCount + count > MAX_EN) {
                        --i
                    }
                }
                return if (i == 0) "" else source.subSequence(0, i).toString()
            }
            return ""
        } else {
            return source
        }
    }

    private fun getChineseCount(str: String): Int {
        var count = 0
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        while (m.find()) {
            //楼下的朋友提供更简洁的代谢
            count++
        }
        return count
    }
}