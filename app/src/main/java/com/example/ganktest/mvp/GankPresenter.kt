package com.example.ganktesst.mvp

import android.util.Log
import com.example.ganktesst.App
import com.example.ganktesst.api.RetrofitHelper
import com.example.ganktest.util.applyScheduler
import org.jetbrains.anko.toast

/**
 * Created by Administrator on 2017/10/16.
 */
class GankPresenter(private val mView: GankView) {

    private val mModel by lazy { RetrofitHelper().getGankApi() }

    fun getAndroid(page:Int=1){

        //什么鬼啊，突然不能用了，下面得方法一个都没调用
        mModel.getAndroid(page).applyScheduler().subscribe({
           if (!it.error){
               mView.show(it.results)
               Log.d("result","success   $it ......................")
           }else{
               mView.showError()
               Log.d("result"," error    $it........................")
           }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }

    fun getIos(page:Int=1){

        mModel.getIos(page).applyScheduler().subscribe({
            if (!it.error){
                mView.show(it.results)
            }else{
                mView.showError()
            }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }

    fun getApp(page:Int=1){

        mModel.getApp(page).applyScheduler().subscribe({
            if (!it.error){
                mView.show(it.results)
            }else{
                mView.showError()
            }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }

    fun getWeb(page:Int=1){

        mModel.getWeb(page).applyScheduler().subscribe({
            if (!it.error){
                mView.show(it.results)
            }else{
                mView.showError()
            }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }

    //括展资源
    fun getExShow(page:Int=1){

        mModel.getExShow(page).applyScheduler().subscribe({
            if (!it.error){
                mView.show(it.results)
            }else{
                mView.showError()
            }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }

    //瞎推荐
    fun getBidShow(page:Int=1){

        mModel.getBidShow(page).applyScheduler().subscribe({
            if (!it.error){
                mView.show(it.results)
            }else{
                mView.showError()
            }
        },{
            mView.showError()
            Log.d("Retrofit","$it")
        })
    }
}