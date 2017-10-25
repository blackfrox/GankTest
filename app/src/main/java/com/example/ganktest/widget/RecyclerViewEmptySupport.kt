package com.example.ganktesst.widget

import android.content.Context
import android.support.v7.widget.ActionBarContextView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by Administrator on 2017/10/15.
 */
class RecyclerViewEmptySupport
     @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defStyle: Int=0)
    : RecyclerView(context,attributeSet,defStyle) {

    private lateinit var emptyView: View
    private val observer=object : AdapterDataObserver(){

        override fun onChanged() {
            showEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            showEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            showEmptyView()
        }
    }

    private fun showEmptyView(){
        if (adapter!=null){
            if (adapter.itemCount==0){
                emptyView.visibility=View.VISIBLE
                visibility=View.GONE
            }else{
                emptyView.visibility=View.GONE
                visibility=View.VISIBLE
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.let {
            it.registerAdapterDataObserver(observer)
            observer.onChanged()
        }
    }

    fun setEmptyView(v: View){
        emptyView=v
    }

}