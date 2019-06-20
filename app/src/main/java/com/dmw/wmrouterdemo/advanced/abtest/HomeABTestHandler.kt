package com.dmw.wmrouterdemo.advanced.abtest

import android.content.Intent
import com.dmw.lib2.HOME_AB_TEST
import com.sankuai.waimai.router.activity.AbsActivityHandler
import com.sankuai.waimai.router.annotation.RouterUri
import com.sankuai.waimai.router.core.UriRequest

/**
 * Created by dumingwei on 2019-06-20.
 * Desc:根据后台下发的ABTest策略，同一个链接跳转不同的Activity。
 * 其中AbsActivityHandler是WMRouter提供的用于跳转Activity的UriHandler通用基类。
 *
 */
@RouterUri(
    path = [HOME_AB_TEST]
)
class HomeABTestHandler : AbsActivityHandler() {

    override fun createIntent(request: UriRequest): Intent {
        return if (FakeABTestService.getHomeABStrategy() == "A") {
            Intent(request.context, HomeActivityA::class.java)
        } else {
            Intent(request.context, HomeActivityB::class.java)

        }

    }
}