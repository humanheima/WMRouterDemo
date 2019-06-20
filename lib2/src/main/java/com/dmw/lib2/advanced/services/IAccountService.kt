package com.dmw.lib2.advanced.services

import android.content.Context

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
interface IAccountService {

    fun isLogin(): Boolean

    fun startLogin(context: Context)

    fun registerObserver(observer: Observer)

    fun unregisterObserver(observer: Observer)

    fun notifyLoginSuccess()

    fun notifyLoginCancel()

    fun notifyLoginFailure()

    fun notifyLogout()

    interface Observer {

        fun onLoginSuccess()

        fun onLoginCancel()

        fun onLoginFailure()

        fun onLogout()
    }
}