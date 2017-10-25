package com.example.ganktesst.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import com.example.ganktesst.R
import com.example.ganktesst.api.data.GankResult
import com.example.ganktesst.util.GankData
import com.example.ganktest.widget.web.CommonWebChormeClient
import com.example.ganktest.widget.web.CommonWebViewClient
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class WebActivity : AppCompatActivity() {

    companion object {
        val ITEM = "contentActivity.mUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initToolbar()
        initWebView()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private lateinit var mCollect: GankResult
    private lateinit var mUrl:String
    private val mData by lazy { GankData(this) }
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebView() {

         mCollect = intent.getSerializableExtra(ITEM) as GankResult
         mCollect = intent.getSerializableExtra(ITEM) as GankResult
        mUrl=mCollect.url
        //webView中设置的progress总有白边，暂时还不知道怎么办
        with(webView) {
            //卧槽，把url提取为全局变量，就出现这个坑了，
            //和with起冲突了，调用了webView.url,而不是自定义的url变量
            //所以才没有响应
            loadUrl(mUrl)
            webChromeClient=CommonWebChormeClient(progressBar)
            //第二参数过会儿再说
            webViewClient=CommonWebViewClient(this@WebActivity)
        }

    }

    //销毁Webview
    override fun onDestroy() {
        webView.loadDataWithBaseURL(null,"","text/html","utf-8",null)
        webView.clearHistory()
        webView.destroy()
        super.onDestroy()
    }



     inner class MyWebViewClient: WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        @SuppressLint("SetJavaScriptEnabled")
        override fun onPageFinished(view: WebView, url: String?) {
            view.settings.javaScriptEnabled=true
            super.onPageFinished(view, url)
            //html加载完之后，添加监听图片的点击js函数
            addImageClickListener()
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
        }
    }

    //注入js函数监听
   private fun addImageClickListener() {
        //这段js函数的功能: 遍历所有的img几点，并添加onClick函数，
        //函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs=document.getElementsByTagName(\"img\");" +
                "{" +
                "objs[i].onClick=function()" +
                "{" +
                "window.imagelistener.openImage(this.src);" +
                "}" +
                "}" +
                "})()")
    }

    //js通信接口
    class JavascriptInterface(private val context: Context){
        fun openImage(img: String){
            val intent= Intent()
            with(intent){
                putExtra("image",img)
                setClass(context,ShowWebImageActivity::class.java)
            }
            context.startActivity(intent)
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //设置WebView，如果有历史纪录可以不用
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.content,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                onBackPressed()
                return true
            }
            //收藏
            R.id.save ->{
                //判断是否已经保存在文件里了
                //查询
                //不确定已经收藏了
                var mItem: GankResult?=null
                val mList=mData.load()
                Log.d("d","$mList")
                for (i in mList){
                    if (i.url== mUrl){
                        mItem=i
                        break
                    }
                }
                if (mItem!=null){
                    item.setIcon(R.drawable.ic_collect)
                    mList.remove(mItem)
                    mData.save(mList)
                }else{
                    item.setIcon(R.drawable.ic_is_collect)
                    mList.add(0,mCollect)
                    mData.save(mList)
                }
                return true
            }
            //点击跳转浏览器
            R.id.send ->   {
                val uri= Uri.parse(mUrl)
                val intent=Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
                return true
            }
            //复制链接
            R.id.cope ->{
                val cmb=getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val data= ClipData.newPlainText("gankUrl",mUrl)
                cmb.primaryClip=data
                toast("复制成功 ")
                return true
            }

            R.id.share ->{
                val i=Intent()
                with(i){

                    setAction(Intent.ACTION_SEND)
                    putExtra(Intent.EXTRA_TEXT,mCollect.desc+"\n"+mCollect.url)
                    setType("text/plain")
                    startActivity(Intent.createChooser(i,"分享到"))
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }


    }
}