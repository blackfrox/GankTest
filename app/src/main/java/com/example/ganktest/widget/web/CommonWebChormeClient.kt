package com.example.ganktest.widget.web

import android.support.v7.app.ActionBar
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar

/**
 * Created by Administrator on 2017/10/24.
 */
class CommonWebChormeClient(val mBar: ProgressBar,
                            val mLoadingView: View?=null,
                            val mActionBar: ActionBar?=null): WebChromeClient() {

    override fun onReceivedTitle(view: WebView?, title: String?) {

        mActionBar?.title=title
    }


    override fun onProgressChanged(view: WebView?, newProgress: Int) {

        mBar.progress=newProgress
        if (newProgress>=100){
            mBar.visibility=View.GONE
            //mLoadView好像跟网上保存账号有关，暂时不动，设为可null
            mLoadingView?.visibility=View.GONE
        }
    }


}