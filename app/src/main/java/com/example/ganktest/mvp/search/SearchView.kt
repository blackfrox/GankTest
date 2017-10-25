package com.example.ganktest.mvp.search

import com.example.ganktesst.api.data.GankResult

/**
 * Created by Administrator on 2017/10/24.
 */
interface SearchView {
    fun show(it: List<GankResult>)
    fun showError()
    fun hideRecyclerView()
}