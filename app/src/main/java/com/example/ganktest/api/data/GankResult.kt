package com.example.ganktesst.api.data

import java.io.Serializable

/**
 * Created by Administrator on 2017/10/15.
 * 因为images有时是没有的，所以需要null
 * (_id=59dc7149421aa94e07d18490, createAt=null,
 * desc=使用 Kotlin 实现的一个 Dribbble 客户端,
 * images=[http://img.gank.io/05d6552f-97ba-4d52-ad33-3caeba5cb327,
 * http://img.gank.io/84594f1b-d10e-42a3-afc1-c7d2bf9ac0cf],
 * publishedAt=2017-10-11T12:40:42.545Z, source=web, type=Gank,
 * url=https://github.com/armcha/Ribble, used=true, who= Thunder Bouble)
 *
 * 时间类型 yyyy-MM-dd HH:mm:ss
 */
data class GankResult(val _id: String,val createdAt: String,
                      val desc: String,val images:List<String>?=null,
                      val publishedAt: String,val source: String,
                      val type: String,val url: String,
                      val used: Boolean,val who: String): Serializable