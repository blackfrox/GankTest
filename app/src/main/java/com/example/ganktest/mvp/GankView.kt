package com.example.ganktesst.mvp

import com.example.ganktesst.api.data.GankResult

/**
 * Created by Administrator on 2017/10/16.
 */
interface GankView {
    fun show(it: List<GankResult>)
    fun showError()
}