package com.example.ganktesst

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.example.ganktesst.ui.MainActivity
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/10/15.
 */
class App: Application() {


    companion object {
        var instance: App by Delegates.notNull<App>()
    }

    override fun onCreate() {
        super.onCreate()
        instance=this

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                Log.d("ActivityName","${activity.localClassName}")
                //不知道为什么mToolbar为null，明明之前一个应用可以用的
                with(activity as AppCompatActivity) {
                    if (toolbar != null) {
                        setSupportActionBar(toolbar)
                        when(activity){
                            is MainActivity ->{}
                            else ->{
                                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                            }
                        }
                    }

                }


            }
            override fun onActivityPaused(activity: Activity?) {


            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }



        })
    }
}