package com.sharkgulf.soloera

import org.junit.Test

import org.junit.Assert.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test(){
        val time = 60 * 60 * 5
        val format = DecimalFormat("#.#").format(time / 60.0  / 60.0 )
        System.out.println(format)
        val m = 60 * 60 + 60
        if(m < 60) {//秒
            System.out.println(NumFormat(0) + ":" + NumFormat(m.toLong()))
        }

        if(m < 3600) {//分
            System.out.println(NumFormat((m / 60).toLong()) + ":" + NumFormat((m % 60).toLong()))
        }

        if(m < 3600 * 24) {//时
            System.out.println()

        }

        if(m >= 3600 * 24) {//天
             val s = "${ NumFormat((m / 60 / 60 /24).toLong())}天${NumFormat((m / 60 / 60 % 24).toLong())}时:${NumFormat((m / 60 % 60).toLong())} 分:${NumFormat((m % 60).toLong())}"
            System.out.println(s)
        }

        val ss = "2019-11-28 06:52:59"
        val sdf = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")

        val sdfss = SimpleDateFormat("HH:mm:ss")

        System.out.println(sdfss.format(sdf.parse(ss)))


        val sb =  StringBuffer()

        //添加数据
        sb.append("hello").append("world").append("java");
        System.out.println("sb:"+sb);

        //截取功能
        val s = sb.substring(5);
        System.out.println("s:"+s);
        System.out.println("sb:"+sb);

        //public String substring(int start,int end):
        //需求:把world截掉
        val sss = sb.substring(5, 10);
        System.out.println("ss:"+sss);//world
        System.out.println("sb:"+sb);

    }

    fun NumFormat(i: Long): String {
        return if (i.toString().length < 2) {
            "0$i"
        } else {
            i.toString()
        }
    }

}
