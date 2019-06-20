package com.dmw.wmrouterdemo

import android.content.Intent
import com.dmw.wmrouterdemo.activity.TestUriRequestActivity
import com.sankuai.waimai.router.core.UriCallback
import com.sankuai.waimai.router.core.UriHandler
import com.sankuai.waimai.router.core.UriRequest
import com.sankuai.waimai.router.core.UriResult

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
class LocalDowngradeUriHandler : UriHandler() {

    override fun handleInternal(request: UriRequest, callback: UriCallback) {
        val uriString = request.uri.toString()
        if (uriString.startsWith("/local_downgrade")) {
            try {
                val intent = Intent(request.context, TestUriRequestActivity::class.java)
                request.context.startActivity(intent)
                callback.onComplete(UriResult.CODE_ERROR)
            } catch (e: Exception) {
                callback.onComplete(UriResult.CODE_ERROR)
            }
        } else {
            callback.onNext()
        }
    }

    override fun shouldHandle(request: UriRequest): Boolean {
        return request.uri.toString().startsWith("/local_downgrade")
    }
}