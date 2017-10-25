package com.example.ganktesst.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.ganktesst.R
import com.example.ganktesst.ui.fragment.AndroidFragment
import com.wyt.searchbox.SearchFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    companion object {
        val ANDROID="Android"
        val IOS="iOS"
        val APP="App"
        val WEB="前端"
        val EXT_SHOW="拓展资源"
        val BID_SHOW="瞎推荐"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView(savedInstanceState)
//        initFragmentTest()
    }

    private fun initFragmentTest() {
        startActivity(Intent(this,SearchActivity::class.java))
//        val fragment=AndroidFragment()
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.container,fragment)
//                .commit()
    }


    private var menuItemId=-1
    private var mTitle="Android"
    private lateinit var currentFragment: Fragment
    private val androidFragment by lazy { AndroidFragment() }
    private fun initView(savedInstanceState: Bundle?) {

        savedInstanceState?.let {
            mTitle=it.getString("title")
            menuItemId=it.getInt("menuItemId")
        }



//        saveSuggestionProvider()

        //默认打开fragment
        when(mTitle){
            "Android","iOS",
            "App","前端","拓展资源",
            "瞎推荐"->{
                currentFragment=androidFragment
                //怎么吧标题传过去
                //难道写个监听器？那还不如写两个判断
            }
        }

        //根据Fragment动态调整title
        when(currentFragment){
            is AndroidFragment -> mTitle="Android"
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title=mTitle


        supportFragmentManager.beginTransaction()
                .replace(R.id.container,currentFragment)
                .commit()

        if (menuItemId!=-1){
            //设置默认点击
            nav_view.setCheckedItem(menuItemId)
        }

        //time得到自1970年一月一日到现在的秒数
//         val t=getNowTime().time
//      Log.d("mDate","${t}")

        fab.setOnClickListener { view ->


            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private lateinit var searchFragment: SearchFragment

//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//        outState?.putString("title",toolbar.title.toString())
//        outState?.putInt("menuItemId",menuItemId)
//    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_search -> {
                val i=Intent(this,SearchActivity::class.java)
                i.putExtra(SearchActivity.TITLE,mTitle)
                startActivity(i)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_save -> {
                menuItemId=item.itemId
               supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AndroidFragment())
                        .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
