package com.trust.demo.basis.updateapp

import java.io.File

interface IBaseDownLoad {
    fun loadingDownLoad(maxLength: Int,progress:Int)
    fun endDownLoadView()
    fun loadingSuccess(file:File)
    fun errorDownLoad(t:Exception)

    interface ApkListener{
        fun resultAplInfo(maxLength:Int,progress: Int)
    }
}