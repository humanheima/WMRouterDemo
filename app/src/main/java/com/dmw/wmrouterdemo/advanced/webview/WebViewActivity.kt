package com.dmw.wmrouterdemo.advanced.webview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.dmw.lib2.INNER_URL_REGEX
import com.dmw.wmrouterdemo.R
import com.sankuai.waimai.router.annotation.RouterRegex
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Crete by dumingwei on 2019-06-20
 * Desc: 对于指定域名的http(s)链接，使用特定的WebViewActivity打开
 *
 */
@RouterRegex(
    regex = INNER_URL_REGEX,
    exported = true,
    priority = 1
)
class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webview.webViewClient = DemoWebViewClient()
        webview.webChromeClient = DemoChromeClient()

        if (intent.dataString == null) {
            finish()
            return
        }
        webview.loadUrl(intent.dataString)
    }

    inner class DemoWebViewClient : WebViewClient() {

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            Toast.makeText(this@WebViewActivity, "加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    inner class DemoChromeClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.VISIBLE
                progressBar.progress = newProgress

            }
            super.onProgressChanged(view, newProgress)

        }

    }
}
