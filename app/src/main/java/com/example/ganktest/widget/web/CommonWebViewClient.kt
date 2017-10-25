package com.example.ganktest.widget.web

import android.app.Activity
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by Administrator on 2017/10/24.
 */
class CommonWebViewClient(private val mActivity: Activity): WebViewClient(){


    //原本有一堆override方法，但是并没有做改动，所以没写，不知道写了有什么作用？

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (view?.url!=null&&view?.url.startsWith("orpheus")){
            return true
        }
        val url=view?.url
        if (url!=null&&url.startsWith("http")){
            return true
        }

        return true
    }

}