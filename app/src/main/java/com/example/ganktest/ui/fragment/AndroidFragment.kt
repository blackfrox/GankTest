package com.example.ganktesst.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ganktesst.App
import com.example.ganktesst.R
import com.example.ganktesst.R.id.recyclerView
import com.example.ganktesst.R.id.swipe_layout
import com.example.ganktesst.api.data.GankResult
import com.example.ganktesst.mvp.GankPresenter
import com.example.ganktesst.mvp.GankView
import com.example.ganktesst.ui.adapter.GankAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast

/**
 * Created by Administrator on 2017/10/18.
 */
class AndroidFragment (): Fragment(),GankView {

    companion object {
        fun Search(){

        }
    }
    private val  mPresenter: GankPresenter by lazy { GankPresenter(this) }
    private var page=1
    //原本是需要考虑类型，再重写一个adapter，暂时先不写o((>ω< ))o
    private lateinit var mAdapter: GankAdapter
    private lateinit var mList : MutableList<GankResult>
//    private val mList by lazy { mutableListOf<GankResult>() }
    private var isLoadMore=false //判断

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater?.inflate(R.layout.content_main,container,false)
        return view
    }

    private var t=0
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        t+=1
        Log.d("fragment11","AndroidFragment is Created $t")
//        initView()
    }

    private fun initView() {
        mPresenter.getAndroid() //替换成下面那句

        recyclerView.layoutManager = LinearLayoutManager(activity)

        //优化性能，如果item布局样式是一样的
        recyclerView.setHasFixedSize(true)

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
                                    mPresenter.getAndroid(page)
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


        swipe_layout.setOnRefreshListener {
            if (swipe_layout.isRefreshing==true){
                    mPresenter.getAndroid()
            }
        }

        val fab=activity.findViewById<FloatingActionButton>(R.id.fab)
        fab?.setOnClickListener {
            //上划到顶部
            //18.32 等searchActivity做完再测试
            recyclerView.layoutManager.scrollToPosition(0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
    }
  override fun show(it: List<GankResult>) {

      swipe_layout.isRefreshing=false
      //妈了个鸡得，是这里出问题了，但为什么presenter还是不能调试，
      //以前是可以的
      emptyView.visibility=View.GONE
      recyclerView.visibility=View.VISIBLE
//        Log.d("result","  show        $it...................")
        //原本在java中是需要加一个isLoadMore变量来配合list使用的
        //在kotlin中好像没这个必要了，不过还是先写着，说不定以后用的到
        //会添加新功能之类的
        if (isLoadMore){
            mList.addAll(it)
            mAdapter.notifyDataSetChanged()
        }else{
            //我知道为什么之前的项目可以用，是因为每次访问的数据并不相同，所以添加
            //就不会看到重复了
            //原本是想使用mList.addAll()方法
            //可惜结果是
            //在刷新的时候添加了重复数据
            //但现在这个方法有个问题，资源的利用率不是很好，
            //暂时还不知道怎么办
            mList= it as MutableList<GankResult>
            mAdapter = GankAdapter(mList){
                //当上拉刷新没网络时调用
                mPresenter.getAndroid(page)
            }
            recyclerView.adapter = mAdapter
        }
    }
    override fun showError() {
        swipe_layout.isRefreshing=false
        if (mList.size<0){
            recyclerView.visibility=View.GONE
        }
        mAdapter.updateLoadStatus(GankAdapter.LOAD_ERROR)

        App.instance.toast("数据获取失败")
    }
    private fun initRecyclerView() {
        //原本是打算内置一个emptyView，但发现和retrofit的缓存
        //合用之后效果不太好，其实是和adapter的下拉加载冲突了，显示不了
//        recyclerView.setEmptyView(findViewById(R.id.emptyView))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        //优化性能，如果item布局样式是一样的
        recyclerView.setHasFixedSize(true)
    }
}