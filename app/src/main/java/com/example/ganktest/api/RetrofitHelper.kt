package com.example.ganktesst.api

import android.content.Context
import com.example.ganktesst.App
import com.example.ganktesst.util.NetworkUtil
import com.google.gson.Gson
import okhttp3.*
import org.jetbrains.anko.db.classParser
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/10/15.
 */
class RetrofitHelper{
    private var mOkHttpClient: OkHttpClient?=null

    val GANK_BASE_URL="http://gank.io/api/data/"
    val GANK_SEARCH_BASE_URL="http://gank.io/api/search/query/"
    init {
        initOkHttpClient()
    }


    fun getGankSearchApi()=
            createApi(GANK_SEARCH_BASE_URL,GankService::class.java)

    fun getGankApi()=
            createApi(GANK_BASE_URL,GankService::class.java)


    /**
     * 根据传入的baseUrl和api创建retrofit
     */

    private fun <T>createApi(baseUrl: String, clazz: Class<T>): T {
        val retrofit=Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(clazz)
    }
    /**
     * 初始化OkHttpClient，设置缓存，设置超时时间
     */
    private fun initOkHttpClient() {

        //单例模式: 保证一个类仅有一个实例，并提供一个访问它的全局访问点
        if (mOkHttpClient==null){
            //使用synchronized进行同步处理，并且双重判断是否为null，
            //我们看到synchronized里面又进行了是否为null判断
            //这是因为一个线程进入了该代码，如果另一个线程在等待，
            //这时候前一个线程创建了一个实例出来完毕后，另一个
            //线程获得锁进入该同步代码，实例已经存在，没必要再次创建
            synchronized(RetrofitHelper::class.java){
                if (mOkHttpClient==null){
                    //设置http缓存

                    val cache = Cache(File(App.instance.cacheDir, "HttpCache")
                            , 1024 * 1024 * 10)
                    mOkHttpClient=OkHttpClient.Builder()
                            .cache(cache)
                            .addNetworkInterceptor(CacheInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30,TimeUnit.SECONDS)
                            .writeTimeout(20,TimeUnit.SECONDS)
                            .readTimeout(20,TimeUnit.SECONDS)
                            .build()
                }
            }
        }
    }

    /**
     * 为okHttp添加缓存，这里考虑到服务器不支持缓存时，从而让okHttp支持缓存
     */
    private class CacheInterceptor: Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {

            //有网络时，设置缓存超时时间为1天
            val maxAge=60*60*1
            //无网络时，设置超时为7天
            val maxStale=60*60*24*7
            var request=chain.request()
            if(NetworkUtil.isNetConneted(App.instance)){
                //有网络时只从网络读取
                request=request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build()
            }else{
                //无网络时只从缓存中读取
                request= request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            var response=chain.proceed(request)
            if (NetworkUtil.isNetConneted(App.instance)){
                response=response.newBuilder()
                        .removeHeader("Pragma")
                        .addHeader("Cache-Control","public, max-age=$maxAge")
                        .build()
            }else{
                response=response.newBuilder()
                        .removeHeader("Pragma")
                        .addHeader("Cache-Control","public, only-if-cached,max-stale=$maxStale")
                        .build()
            }
            return response
        }

    }
}