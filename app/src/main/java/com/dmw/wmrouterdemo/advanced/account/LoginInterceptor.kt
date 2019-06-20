package com.dmw.wmrouterdemo.advanced.account

import android.widget.Toast
import com.dmw.lib2.CustomUriResult
import com.dmw.lib2.advanced.services.DemoServiceManager
import com.dmw.lib2.advanced.services.IAccountService
import com.sankuai.waimai.router.core.UriCallback
import com.sankuai.waimai.router.core.UriInterceptor
import com.sankuai.waimai.router.core.UriRequest
import com.sankuai.waimai.router.core.UriResult

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
class LoginInterceptor : UriInterceptor {


    override fun intercept(request: UriRequest, callback: UriCallback) {
        val accountService = DemoServiceManager.getAccountService()
        if (accountService.isLogin()) {
            callback.onNext()
        } else {
            Toast.makeText(request.context, "请先登录", Toast.LENGTH_SHORT).show()
            accountService.registerObserver(object : IAccountService.Observer {
                override fun onLoginSuccess() {
                    accountService.unregisterObserver(this)
                    callback.onNext()
                }

                override fun onLoginCancel() {
                    accountService.unregisterObserver(this)
                    callback.onComplete(CustomUriResult.CODE_LOGIN_CANCEL)
                }

                override fun onLoginFailure() {
                    accountService.unregisterObserver(this)
                    callback.onComplete(CustomUriResult.CODE_LOGIN_FAILURE)
                }

                override fun onLogout() {
                    accountService.unregisterObserver(this)
                    callback.onComplete(UriResult.CODE_ERROR)
                }
            })
            accountService.startLogin(request.context)
        }
    }
}