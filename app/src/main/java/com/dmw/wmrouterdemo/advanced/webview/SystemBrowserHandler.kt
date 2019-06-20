package com.dmw.wmrouterdemo.advanced.webview

import android.content.Intent
import com.sankuai.waimai.router.core.UriCallback
import com.sankuai.waimai.router.core.UriHandler
import com.sankuai.waimai.router.core.UriRequest
import com.sankuai.waimai.router.core.UriResult

/**
 * Created by dumingwei on 2019-06-20.
 * Desc: 对于其他http(s)链接，使用系统浏览器打开
 */
class SystemBrowserHandler : UriHandler() {

    override fun handleInternal(request: UriRequest, callback: UriCallback) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = request.uri
            request.context.startActivity(intent)
            callback.onComplete(UriResult.CODE_SUCCESS)
        } catch (e: Exception) {
            callback.onComplete(UriResult.CODE_ERROR)
        }

    }

    override fun shouldHandle(request: UriRequest): Boolean {
        return true
    }
}