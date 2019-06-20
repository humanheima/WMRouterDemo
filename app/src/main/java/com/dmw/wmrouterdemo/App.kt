package com.dmw.wmrouterdemo

import android.app.Application
import android.content.Context
import android.content.Intent
import com.dmw.lib2.APPLICATION
import com.dmw.wmrouterdemo.activity.NotFoundActivity
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.annotation.RouterProvider
import com.sankuai.waimai.router.annotation.RouterService
import com.sankuai.waimai.router.common.DefaultRootUriHandler
import com.sankuai.waimai.router.core.Debugger
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.core.UriRequest

/**
 * Created by dumingwei on 2019-06-19.
 * Desc:
 */
@RouterService(
    interfaces = [Context::class],
    key = [APPLICATION],
    singleton = true
)
class App : Application() {


    companion object {

        private lateinit var sApplication: App

        @JvmStatic
        @RouterProvider
        fun provideApplication(): App {
            return sApplication
        }

    }

    override fun onCreate() {
        super.onCreate()
        sApplication = this
        Debugger.setEnableDebug(true)
        Debugger.setEnableLog(true)
        val rootUriHandler = DefaultRootUriHandler(this)
        //配置全局降级策略
        rootUriHandler.globalOnCompleteListener = object : OnCompleteListener {
            override fun onSuccess(request: UriRequest) {

            }

            override fun onError(request: UriRequest, resultCode: Int) {
                //跳转失败到404
                request.context.startActivity(Intent(request.context, NotFoundActivity::class.java))

            }
        }
        Router.init(rootUriHandler)
    }
}