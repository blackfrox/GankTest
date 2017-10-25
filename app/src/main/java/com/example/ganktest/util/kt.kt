package com.example.ganktest.util



import android.util.Log
import com.example.ganktesst.App
import com.example.ganktesst.util.NetworkUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Administrator on 2017/9/19.
 */
fun <T> Observable<T>.applyScheduler()=
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

/**
 * 获取当前时间
 */
fun getNowDate(): String {
    val date=Date()
    val formatter=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return formatter.format(date)
}

//2017-10-11T12:40:42.545Z
fun getDate(timeStr: String): Date {
//   var timeStr="2017-10-11T12:40:42.545Z"
    val date=timeStr.substring(0,10)
    val t=timeStr.substring(11,19)
    val mills=date+" "+t
//    Log.d("mDate","$timeStr")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val pos = ParsePosition(0)
    return formatter.parse(mills, pos)
}

/**
 * 将时间戳转为代表“距离现在多久之前"的字符串
 */
fun getTimeBefore(t: Long): String {

    //当前时间减去发布的时间差
    val time = (System.currentTimeMillis() - t) / 1000
    //div 除以
    val minute = time.div(60)
    val hour = time.div(60 * 60)
    val day = time.div(60 * 60 * 24)
    //minus 减去   times 乘以   为什么要乘法呢，直接用time参数不就行了
    //对的，这是整个值减去minute之后剩余的second值
    // val second=time.minus((minute.times(60)))
    //Log.d("time","$day")
    val timeStr: String
    if (day > 0) {
        timeStr = "${day}天"
    } else if (hour > 0) {
        timeStr = "${hour}小时"
    } else if (minute > 3) {
        timeStr ="${minute}分钟"
    } else {
        timeStr = "刚刚"
        return timeStr
    }
    return timeStr + "前"
}

//判断当前是否有网络连接
inline fun ifNetConnect(take:()->Unit,error:()->  Unit){
    if (NetworkUtil.isNetConneted(App.instance)){
        take()
    }else{
        App.instance.toast("当前无网络")
        error()
    }
}

