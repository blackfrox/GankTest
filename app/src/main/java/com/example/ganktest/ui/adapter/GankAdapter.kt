package com.example.ganktesst.ui.adapter

import android.content.Intent
import android.support.constraint.solver.Goal
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.ganktesst.R
import com.example.ganktesst.api.data.GankResult
import com.example.ganktesst.ui.WebActivity
import com.example.ganktest.util.getDate
import com.example.ganktest.util.getTimeBefore
import kotlinx.android.synthetic.main.item_footer.view.*
import kotlinx.android.synthetic.main.item_gank.view.*

/**
 * Created by Administrator on 2017/10/16.
 */
class GankAdapter(private val mList: MutableList<GankResult>,
                  val showError: ()->  Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var status=1
    companion object {
        val LOAD_MORE=0
        val LOAD_PULL_TO=1
        val LOAD_NONE=2
        val LOAD_ERROR=3

       val TYPE_FOOTER=-1

    }

    val Load_MORE=0
    val Load_PULL_TO=1
    val Load_NONE=2
    val Load_Error=3
    override fun getItemViewType(position: Int): Int {
        when(position){
            //java: if((position)+1)=itemCount
            (itemCount-1) -> return TYPE_FOOTER
            else ->return position
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){

            TYPE_FOOTER ->{
                val view=LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_footer,parent,false)
                return FooterViewHolder(view,showError)
            }
            else ->{
                val view=LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_gank,parent,false)
                return ViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int =mList.size+1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(holder){
            is ViewHolder -> holder.bindToItem(mList[position])
            is FooterViewHolder ->holder.bindItem(status)
        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindToItem(item: GankResult){
           with(item){
               with(itemView){
                   //卧槽，这时间转换我都不知道找了几个
                   //最后还是自己弄了
                   //搜索api里获取不到createAt参数
                   if (createdAt!=null){
                       val t = getDate(createdAt).time
                       val timeString = getTimeBefore(t)
                       tv_time.text = timeString
                   }else{
                       tv_time.visibility=View.GONE
                   }


                   tv_desc.text=desc
                    images?.let {
                        Glide.with(context)
                                .load(images[0])
                                .dontAnimate()
                                .into(item_image)
                        item_image.visibility=View.VISIBLE
                    }
                   tv_who.text="by $who"
                   //以后有机会替换成图片吧
                   tv_tag.text=item.type
                   setOnClickListener {
                       val i= Intent(context, WebActivity::class.java)
                       i.putExtra(WebActivity.ITEM,item)
                       context.startActivity(i)
                   }
               }
           }
        }
    }

    class FooterViewHolder(itemView: View,
                           val showError:() -> Unit
     ): RecyclerView.ViewHolder(itemView){

        fun bindItem(status: Int){


            with(itemView){
              when(status){
                  LOAD_MORE ->{
                      progress.visibility=View.VISIBLE
                      tv_load_prompt.text="正在加载...."
                      visibility=View.VISIBLE
                  }
                  LOAD_PULL_TO ->{
                      progress.visibility=View.GONE
                      tv_load_prompt.text="上拉加载更多"
                      visibility=View.VISIBLE
                  }
                  LOAD_NONE ->{
                      progress.visibility=View.GONE
                      tv_load_prompt.text="已无更多数据"
                  }
                  LOAD_ERROR ->{
                      progress.visibility=View.GONE
                      tv_load_prompt.text="请检查网络，并点击重试"
                      tv_load_prompt.setOnClickListener {
                          showError
                      }
                  }
              }
            }
        }
    }

    //change recycler state
    fun updateLoadStatus(s: Int){
        status=s
    }


}