package com.dmw.wmrouterdemo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sankuai.waimai.router.common.DefaultUriRequest
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.core.UriRequest

class UriProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DefaultUriRequest.startFromProxyActivity(this, object : OnCompleteListener {
            override fun onSuccess(request: UriRequest) {
                finish()
            }

            override fun onError(request: UriRequest, resultCode: Int) {
                finish()
            }
        })
    }
}
