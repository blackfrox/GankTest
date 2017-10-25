package com.example.ganktesst.db

import org.litepal.crud.DataSupport
//突然发现写起来太麻烦了，弃了
class GankTable(val createdAt: String,
                      val desc: String,val images:List<String>?=null,
                      val type: String,val url: String,
                     val who: String): DataSupport()