package com.example.ganktesst.api

import com.example.ganktesst.api.data.Gank
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Administrator on 2017/10/15.
 */
interface GankService {

    //http://gank.io/api/data/Android/10/1
    @GET("Android/10/{page}")
    fun getAndroid(@Path("page") page: Int) :Observable<Gank>

    //IOS
    @GET("iOS/10/{page}")
    fun getIos(@Path("page") page: Int) :Observable<Gank>

    @GET("App/10/{page}")
    fun getApp(@Path("page") page: Int) :Observable<Gank>

    @GET("前端/10/{page}")
    fun getWeb(@Path("page") page: Int) :Observable<Gank>

    @GET("拓展资源/10/{page}")
    fun getExShow(@Path("page") page: Int) :Observable<Gank>

    @GET("瞎推荐/10/{page}")
    fun getBidShow(@Path("page") page: Int) :Observable<Gank>

//    @GET("data/福利/10/{page}")
//    fun getMeiZhiData(@Path("page") page: Int) :Observable<Meizhi>
//    Android/count/10/page/1
//原本1可以改成其他，但是暂时不想写
    @GET("{key}/category/{type}/count/10/page/{page}")
    fun getSearch(@Path("key") key: String,
                  @Path("type") type: String,
                  @Path("page") page: Int): Observable<Gank>

}