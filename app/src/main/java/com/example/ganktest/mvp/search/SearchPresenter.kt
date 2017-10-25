package com.example.ganktesst.mvp

import android.util.Log
import com.example.ganktesst.api.RetrofitHelper
import com.example.ganktest.mvp.search.SearchView
import com.example.ganktest.util.applyScheduler

/**
 * Created by Administrator on 2017/10/23.
 */
class SearchPresenter(private val mView: SearchView) {

    private val mModel by lazy { RetrofitHelper().getGankSearchApi() }

    fun search(keyword: String,title: String,page: Int=1){
            mModel.getSearch(keyword,title,page).applyScheduler().subscribe({
                if (!it.error){
                    Log.d("search","$it")
                    if (it.results.size>0){
                        mView.show(it.results)
                    }else{
                        //recyclerView隐藏

                        mView.showError()
                    }
                }else{
                    Log.d("search","error")
                    mView.showError()
                }
            },{
                mView.showError()
                Log.d("Retrofit","$it")
            })
    }
}