package com.dmw.lib2.advanced.services

import com.dmw.lib2.FAKE_ACCOUNT_SERVICE
import com.sankuai.waimai.router.Router

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:
 */
class DemoServiceManager {

    companion object {

        fun getAccountService(): IAccountService {
            return Router.getService(
                IAccountService::class.java,
                FAKE_ACCOUNT_SERVICE
            )
        }
    }
}