package com.example.ganktesst.util

import android.content.Context
import android.util.Base64
import com.example.ganktesst.api.data.GankResult
import com.example.ganktesst.db.GankTable
import java.io.*
import java.util.*

/**
 * Created by Administrator on 2017/10/18.
 */
class GankData(val context: Context) {

    companion object {
        val SAVE="data.save"
        val DATA="gank_data"
    }
    fun save(mList: MutableList<GankResult>){
        val listString=list2String(mList)
     context.getSharedPresence(SAVE)
             .putString(DATA,listString)
    }

    fun load(): MutableList<GankResult> {
        val defaultVale=list2String(mutableListOf<GankResult>())
        val listString=context.getSharedPresence(SAVE)
                .getString(DATA,defaultVale)
        val list=string2List(listString)
        return list
    }

    @Throws(IOException::class)
    private fun list2String(mList: MutableList<GankResult>): String{
        //实例化一个ByteArrayOutPutStream对象，用来装在压缩后的字节文件
        val byteArrayOutputStream=ByteArrayOutputStream()
        //然后将得到的字符数据装载到ObjectOutPutStream
        val objectOutputStream=ObjectOutputStream(
                byteArrayOutputStream)
        //writeObject 负责写入特定类的对象的状态，以便相应的readObject可以还原它
        objectOutputStream.writeObject(mList)
        //最后用Base64.encode将字节文件转换成Base64编码保存到String中
        val listString=String(Base64.encode(
                byteArrayOutputStream.toByteArray(),Base64.DEFAULT))
        //关闭objectOutputStream
        objectOutputStream.close()
        return listString
    }

    @SuppressWarnings("unchecked")
    @Throws(IOException::class)
    private fun string2List(listString: String): MutableList<GankResult> {
        val mobileBytes=Base64.decode(listString.toByteArray(),Base64.DEFAULT)
        val byteArrayInputStream=ByteArrayInputStream(
                mobileBytes)
        val objectInputStream=ObjectInputStream(byteArrayInputStream)
        val mList=objectInputStream.readObject()
        objectInputStream.close()
        return mList as MutableList<GankResult>
    }
}