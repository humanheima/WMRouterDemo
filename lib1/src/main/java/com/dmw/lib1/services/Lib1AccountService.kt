package com.dmw.lib1.services

import android.content.Context
import com.dmw.lib2.APPLICATION
import com.dmw.lib2.LIB1_ACCOUNT_SERVICE
import com.dmw.lib2.LOGIN
import com.dmw.lib2.advanced.services.IAccountService
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.annotation.RouterProvider
import com.sankuai.waimai.router.annotation.RouterService

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */

@RouterService(
    interfaces = [IAccountService::class],
    key = [LIB1_ACCOUNT_SERVICE],
    singleton = true
)
class Lib1AccountService(context: Context) : IAccountService {

    companion object {

        /**
         * 在声明实现类时，可以在类中定义一个返回值类型为该实现类且无参数的静态方法，并使用RouterProvider注解标注。
         * 当调用Router获取实例时，如果没有指定Factory，则优先调用Provider方法获取实例，
         * 找不到Provider再使用无参数构造.
         */
        @RouterProvider
        @JvmStatic
        fun getInstance(): Lib1AccountService {
            return Lib1AccountService(Router.getService(Context::class.java, APPLICATION))
        }
    }

    private var mIsLogin = false

    private val mObserver = arrayListOf<IAccountService.Observer>()

    override fun isLogin(): Boolean {
        return mIsLogin
    }

    override fun startLogin(context: Context) {
        Router.startUri(context, LOGIN)
    }

    override fun registerObserver(observer: IAccountService.Observer) {
        if (!mObserver.contains(observer)) {
            mObserver.add(observer)
        }
    }

    override fun unregisterObserver(observer: IAccountService.Observer) {
        mObserver.remove(observer)
    }

    override fun notifyLoginSuccess() {
        mIsLogin = true
        val observers = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginSuccess()
        }
    }

    override fun notifyLoginCancel() {
        val observers = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginCancel()
        }
    }

    override fun notifyLoginFailure() {
        val observers = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLoginFailure()
        }
    }

    override fun notifyLogout() {
        mIsLogin = false
        val observers = getObservers()
        for (i in observers.indices.reversed()) {
            observers[i].onLogout()
        }
    }

    private fun getObservers(): Array<IAccountService.Observer> {
        return mObserver.toTypedArray()
    }
}