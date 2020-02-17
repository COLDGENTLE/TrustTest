package com.sharkgulf.soloera.db.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/*
 *Created by Trust on 2019/1/7
 */
@Entity
 class DbBleBean{
    @Id
    var id:Long = 0

//    var mac:String = "EC:3E:EC:59:5B:64"
//
//    var salt:String = "f75fb6042cfe45ae585c9cece5427437"
//
//    var did:String = "2567deb234c8ef93c891e424153a5812"
//
//    var sign:String = "eb15f4d07470cff728821660e77d088a"
//
//    var validation:String = "3600"
    var mac:String = ""

    var salt:String = ""

    var did:String = ""

    var sign:String = ""

    var validation:String = ""

    var bikeId:Int = 0

    var userId:Int = 0

    var bleId:String? = null

    var isConnction:Boolean = false

    var electironic:Boolean = true

    var cushionInduction :Boolean = true
}