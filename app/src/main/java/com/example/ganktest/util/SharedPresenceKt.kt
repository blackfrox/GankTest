package com.example.ganktesst.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


/**
 * Created by Administrator on 2017/10/8.
 *  不要忘了吧这个更新到常用util文件夹里
 */
fun Context.getSharedPresence(name: String): SharedPreferences {
   return getSharedPreferences(name,MODE_PRIVATE)
}

fun SharedPreferences.putBoolean(key: String,value: Boolean){
    edit().putBoolean(key,value)
            .apply()
}

fun SharedPreferences.getBoolean(key: String): Boolean {
   return getBoolean(key,false)
}

fun SharedPreferences.putString(key: String,value: String){
    edit().putString(key,value)
            .apply()
}

fun SharedPreferences.getString(key: String,value: String):String{
    return getString(key,value)
}
