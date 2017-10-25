package com.example.ganktest.widget.web

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Created by Administrator on 2017/10/24.
 */
class CustomWebView @JvmOverloads
 constructor(context: Context,attributeSet: AttributeSet?=null,defStyle: Int=0)
    :WebView(context,attributeSet,defStyle){

    companion object {
        val ENCODING_UTF_8="UTF-8"
        val MIME_TYPE="text/html"
    }

    init {
        innt()
    }

    private fun innt() {
        if (isInEditMode){
            return
        }
        with(settings){
            javaScriptEnabled=true
            builtInZoomControls=false
            //设置缓存模式
            cacheMode=WebSettings.LOAD_CACHE_ELSE_NETWORK
            //开启DOM storage API 功能
            domStorageEnabled=true
            //开启database storage功能
            databaseEnabled=true

            val cacheDir=context.filesDir.absolutePath+"web_cache"
            setAppCachePath(cacheDir)
            setAppCacheEnabled(true)

            loadsImagesAutomatically=true
            defaultTextEncodingName= ENCODING_UTF_8
            blockNetworkImage=false
            layoutAlgorithm=WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            useWideViewPort=true
            loadWithOverviewMode=true
            isHorizontalScrollBarEnabled=false
        }

    }
}