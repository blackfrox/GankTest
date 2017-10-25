package com.example.ganktesst.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.ganktesst.App
import com.example.ganktesst.R
import com.example.ganktesst.api.data.Gank
import com.example.ganktesst.api.data.GankResult
import com.example.ganktesst.mvp.GankView
import com.example.ganktesst.mvp.SearchPresenter
import com.example.ganktesst.ui.adapter.GankAdapter
import com.example.ganktesst.util.s
import com.example.ganktest.mvp.search.SearchView
import com.example.ganktest.util.ifNetConnect
import com.wyt.searchbox.SearchFragment
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.sdk25.coroutines.__GestureOverlayView_OnGestureListener
import org.jetbrains.anko.toast

class SearchActivity : AppCompatActivity(),SearchView {


    companion object {
        val KEY_WORD="keyword"
        val TITLE="title"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        initView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private var page=1
    private lateinit var title: String
    private lateinit var keyword: String
    private val mPresenter by lazy { SearchPresenter(this) }
    private var mList = mutableListOf<GankResult>()
    private lateinit var mAdapter : GankAdapter
    private var isLoadMore=false
    private lateinit var searchFragment: SearchFragment

 private fun initView() {

     //测试所以吧title注释了，之后要改回来
//         title=intent.getStringExtra(TITLE)
     title="Android"
        searchFragment=SearchFragment.newInstance()
        searchFragment.show(supportFragmentManager,SearchFragment.TAG)
        searchFragment.setOnSearchClickListener {
            ifNetConnect({
                keyword=it
                //只能搜索中文，字母只会返回同一个数据
                mPresenter.search(it, title)
            },{
                toast("请检查网络")
            })

        }

     recyclerView.layoutManager=LinearLayoutManager(this)

     //下滑滚动事件
     var lastVisibleItemPosition=0
     recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
         override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
             super.onScrollStateChanged(recyclerView, newState)
             when(newState){
                 RecyclerView.SCROLL_STATE_IDLE ->{
                     val manager=recyclerView.layoutManager as LinearLayoutManager
                     lastVisibleItemPosition=manager.findLastVisibleItemPosition()
                     when(lastVisibleItemPosition){
                         0 -> mAdapter.updateLoadStatus(mAdapter.Load_NONE)
                     //java: lastVisibleItem+1=manager.itemCount()
                         manager.itemCount-1 ->{
                             mAdapter.updateLoadStatus(mAdapter.Load_PULL_TO)
                             isLoadMore=true
                             mAdapter.updateLoadStatus(mAdapter.Load_MORE)
                             Handler().postDelayed({
                                 page+=1
                                 mPresenter.search(keyword,title,page)
                             },500)
                         }
                     }
                 }
             }
         }

         override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
             super.onScrolled(recyclerView, dx, dy)
             lastVisibleItemPosition=(recyclerView?.layoutManager as LinearLayoutManager)
                     .findLastVisibleItemPosition()
         }
     })
    }


    override fun show(it: List<GankResult>) {

        recyclerView.visibility= View.VISIBLE
        if (isLoadMore){
            mList.addAll(it)
            mAdapter.notifyDataSetChanged()
        }else{
            mList= it as MutableList<GankResult>
            mAdapter = GankAdapter(mList){
                //当上拉刷新没网络时调用
                mPresenter.search(keyword,title,page)
            }
            recyclerView.adapter = mAdapter
        }
    }

    override fun showError() {
        if (mList.size<0){
            recyclerView.visibility=View.GONE
        }
        mAdapter.updateLoadStatus(GankAdapter.LOAD_ERROR)

        App.instance.toast("数据获取失败")
    }

    //当搜索结果为空时调用
    override fun hideRecyclerView() {
        recyclerView.visibility=View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            R.id.action_search ->{
                searchFragment.show(supportFragmentManager,SearchFragment.TAG)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
